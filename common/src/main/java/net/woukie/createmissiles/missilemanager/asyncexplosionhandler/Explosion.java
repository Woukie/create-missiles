package net.woukie.createmissiles.missilemanager.asyncexplosionhandler;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.*;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3d;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

import static net.woukie.createmissiles.Util.traverseSupercover;

public class Explosion {
    private final Map<BlockPos, Float> hardnessCache = new HashMap<>();
    private final Set<BlockPos> processedBlocks = new HashSet<>();
    private final PriorityQueue<BlockPos> queuedBlocks = new PriorityQueue<>((o1, o2) ->
            Double.compare(this.originPosition.distanceTo(o1.getCenter()), this.originPosition.distanceTo(o2.getCenter())));

    private static final Direction[] DIRECTIONS = Direction.values();
    private static final double HARDNESS_MULTIPLIER = 0.3;
    private static final double HARDNESS_OFFSET = 0.315;

    private Vec3 originPosition;
    private BlockPos originBlockPosition;
    private double radiusOfLastChange = 0;
    private double power;
    private ResourceKey<Level> levelKey;
    private boolean complete = false;

    public Explosion() {
    }

    public Explosion(Level level, Vec3 originPosition, double power) {
        this.levelKey = level.dimension();
        this.originPosition = originPosition;
        this.originBlockPosition = BlockPos.containing(originPosition);
        this.power = power;
    }

    public void serverTick(MinecraftServer server) {
        final long startTime = System.currentTimeMillis();
        final Level level = server.getLevel(levelKey);
        if (level == null) {
            this.complete = true;
            return;
        }

        if (queuedBlocks.isEmpty() && processedBlocks.isEmpty()) {
            hardnessCache.put(originBlockPosition, level.getBlockState(originBlockPosition).getBlock().getExplosionResistance());
            queuedBlocks.offer(originBlockPosition);
        }

        while (System.currentTimeMillis() - startTime < 20 && !queuedBlocks.isEmpty()) {
            final BlockPos blockPos = queuedBlocks.poll();
            final double distance = blockPos.getCenter().distanceTo(originPosition);

            if (distance - radiusOfLastChange > 2) {
                this.complete = true;
                return;
            }

            if (damageBlock(blockPos, level)) {
                radiusOfLastChange = distance;
            }

            processedBlocks.add(blockPos);
            for (final Direction dir : DIRECTIONS) {
                final BlockPos newPos = blockPos.relative(dir);
                if (queuedBlocks.contains(newPos) || processedBlocks.contains(newPos))
                    continue;
                queuedBlocks.offer(newPos);
                hardnessCache.put(newPos, level.getBlockState(newPos).getBlock().getExplosionResistance());
            }
        }

        if (queuedBlocks.isEmpty()) {
            this.complete = true;
        }
    }

    private boolean damageBlock(BlockPos blockPos, Level level) {
        final double distance = blockPos.getCenter().distanceTo(originPosition);
        if (this.power - distance * HARDNESS_OFFSET <= 0) {
            return false;
        }

        final Vector3d from = new Vector3d(originPosition.x, originPosition.y, originPosition.z);
        final Vector3d to = new Vector3d(blockPos.getX() + 0.5, blockPos.getY() + 0.5, blockPos.getZ() + 0.5);

        final AtomicReference<Float> totalHardness = new AtomicReference<>(0f);
        final AtomicReference<Integer> passedCount = new AtomicReference<>(0);

        traverseSupercover(from, to, pos -> {
            final BlockPos traversedPos = BlockPos.containing(pos.x, pos.y, pos.z);
            final float hardness = hardnessCache.containsKey(traversedPos) ?
                    hardnessCache.get(traversedPos) :
                    level.getBlockState(traversedPos).getBlock().getExplosionResistance();

            totalHardness.updateAndGet(current -> current + hardness);
            passedCount.updateAndGet(a -> ++a);
            return false;
        });

        final int passedBlocks = passedCount.get();

        // Avoid division by zero
        if (passedBlocks == 0) {
            return false;
        }

        final double averageHardness = totalHardness.get() / (double) passedBlocks;
        double powerLeft = (0.8 + Math.random() * 0.2) * this.power - ((HARDNESS_MULTIPLIER * averageHardness + HARDNESS_OFFSET) * distance);

        final float blockHardness = level.getBlockState(blockPos).getBlock().getExplosionResistance();
        powerLeft -= HARDNESS_MULTIPLIER * blockHardness + HARDNESS_OFFSET;

        if (powerLeft > 0) {
            level.destroyBlock(blockPos, true);
            return true;
        }
        return false;
    }

    public boolean isComplete() {
        return this.complete;
    }

    public CompoundTag save() {
//        final CompoundTag blockDataTag = new CompoundTag();
//        final long[] positions = blockData.keySet().stream().mapToLong(BlockPos::asLong).toArray();
//        blockDataTag.putLongArray("Positions", positions);
//
//        final ListTag blockDataValues = new ListTag();
//        blockData.values().forEach(data -> blockDataValues.add(data.save()));
//        blockDataTag.put("Data", blockDataValues);
//
//        final CompoundTag data = new CompoundTag();
//        data.put("BlockData", blockDataTag);
//
//        final long[] queueArray = queuedBlocks.stream().mapToLong(BlockPos::asLong).toArray();
//        data.putLongArray("BlockQueue", queueArray);
//
//        data.putString("Dimension", levelKey.location().getPath());
//        data.putDouble("PositionX", originPosition.x);
//        data.putDouble("PositionY", originPosition.y);
//        data.putDouble("PositionZ", originPosition.z);
//        data.putDouble("Radius", radius);
//        data.putDouble("RadiusOfLastChange", radiusOfLastChange);
//        data.putDouble("Power", power);
//
//        return data;
        return new CompoundTag();
    }

    public static Explosion loadFrom(CompoundTag data) {
//        final Explosion explosion = new Explosion();
//
//        final String dimension = data.getString("Dimension");
//        explosion.levelKey = ResourceKey.create(Registries.DIMENSION, new ResourceLocation(dimension));
//
//        explosion.originPosition = new Vec3(
//                data.getDouble("PositionX"),
//                data.getDouble("PositionY"),
//                data.getDouble("PositionZ")
//        );
//
//        explosion.radius = data.getDouble("Radius");
//        explosion.radiusSquared = explosion.radius * explosion.radius;
//        explosion.power = data.getDouble("Power");
//        explosion.radiusOfLastChange = data.getDouble("RadiusOfLastChange");
//
//        // Load block queue
//        final long[] queueData = data.getLongArray("BlockQueue");
//        explosion.queuedBlocks.clear();
//        for (final long pos : queueData) {
//            explosion.queuedBlocks.offer(BlockPos.of(pos));
//        }
//
//        // Load block data
//        final CompoundTag blockDataTag = data.getCompound("BlockData");
//        explosion.blockData.clear();
//
//        final long[] positions = blockDataTag.getLongArray("Positions");
//        final ListTag blockDataValues = blockDataTag.getList("Data", Tag.TAG_COMPOUND);
//
//        for (int i = 0; i < positions.length && i < blockDataValues.size(); i++) {
//            final BlockPos pos = BlockPos.of(positions[i]);
//            final BlockData blockData = BlockData.load(blockDataValues.get(i));
//            explosion.blockData.put(pos, blockData);
//        }
//
//        return explosion;
        return new Explosion();
    }
}