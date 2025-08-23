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

public class Explosion {
    public static final int EXPLOSION_CHUNK_SIZE = 15;

    private final Vec3 originPosition;
    private final BlockPos originBlockPosition;
    private final double power;
    private final ResourceKey<Level> levelKey;
    private final Level level;

    private final List<ExplodingAreaWorker> workers = new ArrayList<>();
    private final List<Thread> threads = new ArrayList<>();


    private boolean complete = false;

    public Explosion(Level level, Vec3 originPosition, double power) {
        final ConcurrentHashMap<BlockPos, Float> hardnessMap = new ConcurrentHashMap<>();
        this.levelKey = level.dimension();
        this.level = level;
        this.originPosition = originPosition;
        this.originBlockPosition = BlockPos.containing(originPosition);
        this.power = power;

        int maxRadius = (int)(this.power / 0.315); // TODO: set to real max distance of effected block
        BlockPos start = originBlockPosition.offset(-maxRadius, -maxRadius, -maxRadius);
        BlockPos end = originBlockPosition.offset(maxRadius, maxRadius, maxRadius);
        for (int x = start.getX(); x <= end.getX(); x += EXPLOSION_CHUNK_SIZE) {
            for (int y = start.getY(); y <= end.getY(); y += EXPLOSION_CHUNK_SIZE) {
                for (int z = start.getZ(); z <= end.getZ(); z += EXPLOSION_CHUNK_SIZE) {
                    BlockPos chunkStart = new BlockPos(x - EXPLOSION_CHUNK_SIZE / 2, y - EXPLOSION_CHUNK_SIZE / 2, z - EXPLOSION_CHUNK_SIZE / 2);
                    BlockPos chunkEnd = new BlockPos(chunkStart.getX() + EXPLOSION_CHUNK_SIZE - 1, chunkStart.getY() + EXPLOSION_CHUNK_SIZE - 1, chunkStart.getZ() + EXPLOSION_CHUNK_SIZE - 1);
                    ExplodingAreaWorker worker = new ExplodingAreaWorker(chunkStart, chunkEnd, originBlockPosition, level, power, hardnessMap);
                    Thread thread = new Thread(worker);
                    workers.add(worker);
                    threads.add(thread);

                    double distance = new Vec3(x, y, z).distanceTo(originPosition);
                    double proximity = 1 - (distance / maxRadius);
                    int priority = (int) Math.clamp(proximity * (Thread.MAX_PRIORITY - Thread.MIN_PRIORITY) + Thread.MIN_PRIORITY, Thread.MIN_PRIORITY, Thread.MAX_PRIORITY);
                    thread.setPriority(priority);
                    thread.start();
                }
            }
        }
    }

    public void serverTick(MinecraftServer server) {
        long startTime = System.nanoTime();
        boolean keepDestroying = true;
        complete = true;
        while (keepDestroying) {
            if (System.nanoTime() - startTime > 5000000) {
                complete = false;
                return;
            }

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