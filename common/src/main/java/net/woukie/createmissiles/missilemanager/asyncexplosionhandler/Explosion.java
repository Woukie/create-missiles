package net.woukie.createmissiles.missilemanager.asyncexplosionhandler;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import static net.woukie.createmissiles.missilemanager.asyncexplosionhandler.ExplodingAreaWorker.HARDNESS_OFFSET;

public class Explosion {
    public static final int EXPLOSION_CHUNKS = 2;

    private final Vec3 originPosition;
    private final BlockPos originBlockPosition;
    private final double power;
    private final ResourceKey<Level> levelKey;
    private final Level level;

    private final List<ExplodingAreaWorker> workers = new ArrayList<>();
    private final List<Thread> threads = new ArrayList<>();

    private boolean complete = false;

    public Explosion(Level level, Vec3 originPosition, double power) {
        this.levelKey = level.dimension();
        this.level = level;
        this.originPosition = originPosition;
        this.originBlockPosition = BlockPos.containing(originPosition);
        this.power = power;
        final int radius = (int)((this.power -0.3 -HARDNESS_OFFSET) / HARDNESS_OFFSET);
        final int chunkSize = radius * 2 / EXPLOSION_CHUNKS;
        final ConcurrentHashMap<BlockPos, Float> hardnessMap = new ConcurrentHashMap<>((int) (4.0/3.0 * Math.PI * radius * radius * radius), 0.75f, EXPLOSION_CHUNKS * EXPLOSION_CHUNKS * EXPLOSION_CHUNKS);

        for (int x = 0; x < EXPLOSION_CHUNKS; x++) {
            for (int y = 0; y < EXPLOSION_CHUNKS; y++) {
                for (int z = 0; z < EXPLOSION_CHUNKS; z++) {
                    BlockPos start = originBlockPosition.offset(-radius + chunkSize * x, -radius + chunkSize * y, -radius + chunkSize * z);
                    BlockPos end = start.offset(chunkSize, chunkSize, chunkSize);
                    ExplodingAreaWorker worker = new ExplodingAreaWorker(start, end, originBlockPosition, level, power, hardnessMap);
                    Thread thread = new Thread(worker);
                    workers.add(worker);
                    threads.add(thread);
                    thread.start();
                }
            }
        }
    }

    public void serverTick(MinecraftServer server) {
        boolean keepDestroying = true;
        complete = true;
        while (keepDestroying) {
            keepDestroying = false;
            for (ExplodingAreaWorker worker : workers) {
                if (!worker.isComplete()) {
                    complete = false;
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
        return new CompoundTag();
    }
}