package net.woukie.createmissiles.missiles.asyncexplosionhandler;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.ProtectionEnchantment;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.woukie.createmissiles.CreateMissiles;
import org.joml.Vector3d;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

import static net.woukie.createmissiles.Util.traverseSupercover;
import static net.woukie.createmissiles.missiles.asyncexplosionhandler.ExplodingAreaWorker.HARDNESS_MULTIPLIER;
import static net.woukie.createmissiles.missiles.asyncexplosionhandler.ExplodingAreaWorker.HARDNESS_OFFSET;

public class Explosion {
    public static final int EXPLOSION_CHUNKS = 2;

    private final BlockPos originBlockPosition;
    private final double power, decay;
    private final ResourceKey<Level> levelKey;
    private final Level level;

    private ListTag workerData;

    private final List<ExplodingAreaWorker> workers = new ArrayList<>();

    private final int maxRadius;
    private final ConcurrentHashMap<BlockPos, Float> hardnessMap; // Max size for end explosion climbs to ~44 million blocks
    private boolean complete = false;
    private boolean endEarly = false;

    public Explosion(Level level, Vec3 originPosition, double power) {
        this(level, originPosition, power, 0.3);
    }

    public Explosion(Level level, Vec3 originPosition, double power, double decay) {
        this.levelKey = level.dimension();
        this.level = level;
        this.originBlockPosition = BlockPos.containing(originPosition);
        this.power = power;
        this.decay = decay;
        maxRadius = (int)((this.power - this.decay - HARDNESS_OFFSET) / HARDNESS_OFFSET);
        this.hardnessMap = new ConcurrentHashMap<>((int) (4.0/3.0 * Math.PI * maxRadius * maxRadius * maxRadius), 0.75f, EXPLOSION_CHUNKS * EXPLOSION_CHUNKS * EXPLOSION_CHUNKS);
    }

    public void damageEntities() {
        var c1 = originBlockPosition.getCenter().subtract(new Vec3(maxRadius, maxRadius, maxRadius));
        var c2 = originBlockPosition.getCenter().add(new Vec3(maxRadius, maxRadius, maxRadius));
        List<Entity> entities = this.level.getEntities(null, new AABB(c1.x, c1.y, c1.z, c2.x, c2.y, c2.z));
        entities.forEach(entity -> {
            if (!(entity instanceof LivingEntity)) return;
            if (entity.ignoreExplosion()) return;
            double proximity = Math.sqrt(entity.distanceToSqr(originBlockPosition.getCenter()));
            double proportionToEdge = proximity / (double) maxRadius;
            double proportionToCenter = 1 - proportionToEdge;
            if (!(proportionToEdge <= (double) 1.0F)) return;

            final AtomicReference<Float> totalHardness = new AtomicReference<>(0f);
            final AtomicReference<Integer> passedCount = new AtomicReference<>(0);
            Vector3d origin = new Vector3d(originBlockPosition.getX(), originBlockPosition.getY(), originBlockPosition.getZ()).add(0.5, 0.5, 0.5);
            Vector3d target = new Vector3d(entity.position().x, entity.position().y, entity.position().z);
            final double startPower = this.power;
            traverseSupercover(origin, target, traversedPos -> {
                double distance = traversedPos.distance(origin);
                if (distance > maxRadius) return true;

                final BlockPos traversedBlockPos = BlockPos.containing(traversedPos.x, traversedPos.y, traversedPos.z);
                final float hardness = level.getBlockState(traversedBlockPos).getBlock().getExplosionResistance();
                totalHardness.updateAndGet(current -> current + hardness);
                passedCount.updateAndGet(a -> ++a);

                final int passedBlocks = Math.max(passedCount.get(), 1);
                final double averageHardness = totalHardness.get() / (double) passedBlocks;
                final double powerLeft = startPower - ((HARDNESS_MULTIPLIER * (averageHardness / 5) + HARDNESS_OFFSET + this.decay) * distance);

                return powerLeft <= 0;
            });

            final double distance = target.distance(origin);
            final int passedBlocks = Math.max(passedCount.get(), 1);
            final double averageHardness = totalHardness.get() / (double) passedBlocks;
            final double powerLeft = startPower - ((HARDNESS_MULTIPLIER * (averageHardness / 5) + HARDNESS_OFFSET + this.decay) * distance);
            if (powerLeft <= 0) return;
            double exposure = powerLeft / this.power;
            exposure = exposure >= 1 ? 1 : 1 - Math.pow(2, -10 * exposure); // Adjusted for a further falloff
            double impact = proportionToCenter * exposure;
            entity.hurt(level.damageSources().explosion(null, null), (int)((impact * impact * impact) * 7 * power + 1));
            exposure = ProtectionEnchantment.getExplosionKnockbackAfterDampener((LivingEntity)entity, exposure);
            entity.setDeltaMovement(entity.getDeltaMovement().add(entity.position().subtract(originBlockPosition.getCenter()).normalize().scale(exposure)));
        });
    }

    public void startCrunching() {
        final int chunkSize = maxRadius * 2 / EXPLOSION_CHUNKS;

        for (int x = 0; x < EXPLOSION_CHUNKS; x++) {
            for (int y = 0; y < EXPLOSION_CHUNKS; y++) {
                for (int z = 0; z < EXPLOSION_CHUNKS; z++) {
                    BlockPos start = originBlockPosition.offset(-maxRadius + chunkSize * x, -maxRadius + chunkSize * y, -maxRadius + chunkSize * z);
                    BlockPos end = start.offset(chunkSize, chunkSize, chunkSize);
                    ExplodingAreaWorker worker = new ExplodingAreaWorker(start, end, originBlockPosition, level, power, hardnessMap, decay);
                    int i = x * (EXPLOSION_CHUNKS * EXPLOSION_CHUNKS) + y * EXPLOSION_CHUNKS + z;
                    if (workerData != null && workerData.size() > i) {
                        worker.load((CompoundTag) workerData.get(i));
                    }

                    Thread thread = new Thread(worker);
                    workers.add(worker);
                    thread.start();
                }
            }
        }
        CreateMissiles.LOGGER.info("Started explosion workers at {}", originBlockPosition.toShortString());

        workerData = null;
    }

    public void serverTick(MinecraftServer server) {
        boolean keepDestroying = true;
        complete = true;
        while (keepDestroying) {
            keepDestroying = false;
            for (ExplodingAreaWorker worker : workers) {
                if (!worker.isComplete()) {
                    complete = false;
                    if (endEarly) return;
                    if (worker.processBlock(level)) {
                        keepDestroying = true;
                    }
                }
            }
        }
    }

    public boolean isComplete() {
        return this.complete;
    }

    public CompoundTag save() {
        CompoundTag hardnessData = new CompoundTag();
        hardnessData.putLongArray("Keys", hardnessMap.keySet().stream().map(BlockPos::asLong).toList());
        hardnessData.putByteArray("Values", hardnessMap.values().stream().map(Float::byteValue).toList());

        ListTag workerList = new ListTag();
        workerList.addAll(workers.stream().map(ExplodingAreaWorker::save).toList());

        CompoundTag data = new CompoundTag();
        data.putString("Level", levelKey.location().getPath());
        data.putInt("PositionX", originBlockPosition.getX());
        data.putInt("PositionY", originBlockPosition.getY());
        data.putInt("PositionZ", originBlockPosition.getZ());
        data.put("Workers", workerList);
        data.putDouble("Power", power);
        data.put("Hardness", hardnessData);
        return data;
    }

    public static Explosion load(CompoundTag data, MinecraftServer server) {
        Level level = server.getLevel(ResourceKey.create(Registries.DIMENSION, new ResourceLocation(data.getString("Level"))));
        Vec3 origin = new Vec3(data.getInt("PositionX"), data.getInt("PositionY"), data.getInt("PositionZ"));
        if (level == null) return null;
        Explosion explosion = new Explosion(level, origin, data.getDouble("Power"));
        CompoundTag hardnessData = data.getCompound("Hardness");
        long[] hardnessKeys = hardnessData.getLongArray("Keys");
        byte[] hardnessValues = hardnessData.getByteArray("Values");
        for (int i = 0; i < hardnessKeys.length; i++)
            explosion.hardnessMap.put(BlockPos.of(hardnessKeys[i]), (float)hardnessValues[i]);
        explosion.workerData = data.getList("Workers", 10);
        return explosion;
    }

    public void stopCrunching() {
        endEarly = true;
        workers.forEach(ExplodingAreaWorker::endEarly);
    }

    public BlockPos getOrigin() {
        return originBlockPosition;
    }
}