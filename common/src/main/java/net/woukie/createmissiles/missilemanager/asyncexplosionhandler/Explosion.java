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
        final ConcurrentHashMap<BlockPos, Float> hardnessMap = new ConcurrentHashMap<>();
        this.levelKey = level.dimension();
        this.level = level;
        this.originPosition = originPosition;
        this.originBlockPosition = BlockPos.containing(originPosition);
        this.power = power;

        int maxRadius = (int)(this.power / 0.315);
        int chunkSize = maxRadius * 2 / EXPLOSION_CHUNKS;
        BlockPos start = originBlockPosition.offset(-maxRadius, -maxRadius, -maxRadius);
        BlockPos end = originBlockPosition.offset(maxRadius, maxRadius, maxRadius);
        for (int x = start.getX(); x <= end.getX(); x += chunkSize) {
            for (int y = start.getY(); y <= end.getY(); y += chunkSize) {
                for (int z = start.getZ(); z <= end.getZ(); z += chunkSize) {
                    BlockPos chunkStart = new BlockPos(x, y, z);
                    BlockPos chunkEnd = new BlockPos(chunkStart.getX() + chunkSize - 1, chunkStart.getY() + chunkSize - 1, chunkStart.getZ() + chunkSize - 1);
                    ExplodingAreaWorker worker = new ExplodingAreaWorker(chunkStart, chunkEnd, originBlockPosition, level, power, hardnessMap);
                    Thread thread = new Thread(worker);
                    workers.add(worker);
                    threads.add(thread);
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