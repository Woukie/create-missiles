package net.woukie.createmissiles.missilemanager.asyncexplosionhandler;

import com.simibubi.create.Create;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.FloatTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.woukie.createmissiles.CreateMissiles;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import static net.woukie.createmissiles.missilemanager.asyncexplosionhandler.ExplodingAreaWorker.HARDNESS_OFFSET;

public class Explosion {
    public static final int EXPLOSION_CHUNKS = 2;

    private final BlockPos originBlockPosition;
    private final double power;
    private final ResourceKey<Level> levelKey;
    private final Level level;

    private ListTag workerData;

    private final List<ExplodingAreaWorker> workers = new ArrayList<>();

    private final ConcurrentHashMap<BlockPos, Float> hardnessMap; // Max size for end explosion climbs to ~44 million blocks
    private boolean complete = false;
    private boolean endEarly = false;

    public Explosion(Level level, Vec3 originPosition, double power) {
        this.levelKey = level.dimension();
        this.level = level;
        this.originBlockPosition = BlockPos.containing(originPosition);
        this.power = power;
        final int radius = (int)((this.power -0.3 -HARDNESS_OFFSET) / HARDNESS_OFFSET);
        this.hardnessMap = new ConcurrentHashMap<>((int) (4.0/3.0 * Math.PI * radius * radius * radius), 0.75f, EXPLOSION_CHUNKS * EXPLOSION_CHUNKS * EXPLOSION_CHUNKS);
    }

    public void startCrunching() {
        final int radius = (int)((this.power -0.3 -HARDNESS_OFFSET) / HARDNESS_OFFSET);
        final int chunkSize = radius * 2 / EXPLOSION_CHUNKS;

        for (int x = 0; x < EXPLOSION_CHUNKS; x++) {
            for (int y = 0; y < EXPLOSION_CHUNKS; y++) {
                for (int z = 0; z < EXPLOSION_CHUNKS; z++) {
                    BlockPos start = originBlockPosition.offset(-radius + chunkSize * x, -radius + chunkSize * y, -radius + chunkSize * z);
                    BlockPos end = start.offset(chunkSize, chunkSize, chunkSize);
                    ExplodingAreaWorker worker = new ExplodingAreaWorker(start, end, originBlockPosition, level, power, hardnessMap);
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
                    if (worker.destroyBlock(level)) {
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