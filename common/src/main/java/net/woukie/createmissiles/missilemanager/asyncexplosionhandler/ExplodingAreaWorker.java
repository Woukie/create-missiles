package net.woukie.createmissiles.missilemanager.asyncexplosionhandler;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import org.joml.Vector3d;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.atomic.AtomicReference;

import static net.woukie.createmissiles.Util.traverseSupercover;

public class ExplodingAreaWorker implements Runnable {
    private final BlockPos start, end;
    private final Vector3d origin;
    private final PriorityBlockingQueue<BlockPos> brokenBlocks;
    private final Level level;
    private final Double power;
    private final ConcurrentHashMap<BlockPos, Float> hardnessMap;
    private final double maxDistance;
    private boolean calculatedBlocks = false;

    private boolean doneX = false;
    private boolean doneY = false;
    private boolean doneZ = false;
    private int countX = 0;
    private int countY = 0;
    private int countZ = 0;

    public static final double HARDNESS_MULTIPLIER = 0.3;
    public static final double HARDNESS_OFFSET = 0.315;

    public ExplodingAreaWorker(BlockPos start, BlockPos end, BlockPos originBlock, Level level, Double power, ConcurrentHashMap<BlockPos, Float> hardnessMap) {
        this.start = start;
        this.end = end;
        this.origin = new Vector3d(originBlock.getX() + 0.5, originBlock.getY() + 0.5, originBlock.getZ() + 0.5);
        this.level = level;
        this.power = power;
        this.hardnessMap = hardnessMap;
        this.maxDistance = (this.power - 0.3 - HARDNESS_OFFSET) / HARDNESS_OFFSET;
        brokenBlocks = new PriorityBlockingQueue<>();
    }

    public void run() {
        final boolean includeLowerX = origin.x - start.getX() - 0.5 >= (int) maxDistance;
        final boolean includeUpperX = end.getX() + 0.5 - origin.x >= (int) maxDistance - 1;
        final boolean includeLowerY = origin.y - start.getY() - 0.5 >= (int) maxDistance;
        final boolean includeUpperY = end.getY() + 0.5 - origin.y >= (int) maxDistance - 1;
        final boolean includeLowerZ = origin.z - start.getZ() - 0.5 >= (int) maxDistance;
        final boolean includeUpperZ = end.getZ() + 0.5 - origin.z >= (int) maxDistance - 1;

        while (!this.doneX || !this.doneY || !this.doneZ) {
            processX(includeLowerX, includeUpperX);
            processY(includeLowerY, includeUpperY);
            processZ(includeLowerZ, includeUpperZ);
        }

        calculatedBlocks = true;
    }

    private void processX(boolean includeLowerX, boolean includeUpperX) {
        if (doneX) return;
        if (includeLowerX || includeUpperX) {
            int z = (int)(Math.random() * (end.getZ() - start.getZ())) + start.getZ();
            int y = (int)(Math.random() * (end.getY() - start.getY())) + start.getY();
            if (includeLowerX) traverseBlock(new BlockPos(start.getX(), y, z));
            if (includeUpperX) traverseBlock(new BlockPos(end.getX(), y, z));
            countX++;
            if (countX >= maxDistance * maxDistance) doneX = true;
            return;
        }
        doneX = true;
    }

    private void processY(boolean includeLowerY, boolean includeUpperY) {
        if (doneY) return;
        if (includeLowerY || includeUpperY) {
            int x = (int)(Math.random() * (end.getX() - start.getX())) + start.getX();
            int z = (int)(Math.random() * (end.getZ() - start.getZ())) + start.getZ();
            if (includeLowerY) traverseBlock(new BlockPos(x, start.getY(), z));
            if (includeUpperY) traverseBlock(new BlockPos(x, end.getY(), z));
            countY++;
            if (countY >= maxDistance * maxDistance) doneY = true;
            return;
        }
        doneY = true;
    }

    private void processZ(boolean includeLowerZ, boolean includeUpperZ) {
        if (doneZ) return;
        if (includeLowerZ || includeUpperZ) {
            int x = (int)(Math.random() * (end.getX() - start.getX())) + start.getX();
            int y = (int)(Math.random() * (end.getY() - start.getY())) + start.getY();
            if (includeLowerZ) traverseBlock(new BlockPos(x, y, start.getZ()));
            if (includeUpperZ) traverseBlock(new BlockPos(x, y, end.getZ()));
            countZ++;
            if (countZ >= maxDistance * maxDistance) doneZ = true;
            return;
        }
        doneZ = true;
    }

    private void traverseBlock(BlockPos blockPos) {
        final double startPower = (0.8 + Math.random() * 0.2) * this.power;
        final AtomicReference<Float> totalHardness = new AtomicReference<>(0f);
        final AtomicReference<Integer> passedCount = new AtomicReference<>(0);
        traverseSupercover(origin, new Vector3d(blockPos.getX() + 0.5, blockPos.getY() + 0.5, blockPos.getZ() + 0.5), traversedPos -> {
            double distance = traversedPos.distance(origin);
            if (distance > maxDistance) return true;

            final BlockPos traversedBlockPos = BlockPos.containing(traversedPos.x, traversedPos.y, traversedPos.z);
            final boolean alreadyCalculated = hardnessMap.containsKey(traversedBlockPos);
            final float hardness = hardnessMap.computeIfAbsent(traversedBlockPos,
                    p -> level.getBlockState(p).getBlock().getExplosionResistance());
            totalHardness.updateAndGet(current -> current + hardness);
            passedCount.updateAndGet(a -> ++a);

            if (!alreadyCalculated) {
                final int passedBlocks = Math.max(passedCount.get(), 1);
                final double averageHardness = totalHardness.get() / (double) passedBlocks;
                final double powerLeft = startPower - ((HARDNESS_MULTIPLIER * averageHardness + HARDNESS_OFFSET + 0.3) * distance);
                if (powerLeft > 0) {
                    brokenBlocks.offer(traversedBlockPos);
                } else {
                    return true;
                }
            }

            return false;
        });
    }

    public boolean isComplete() {
        return calculatedBlocks && brokenBlocks.isEmpty();
    }

    public boolean destroyBlock(Level level) {
        BlockPos blockPos = brokenBlocks.poll();
        if (blockPos != null) {
            level.destroyBlock(blockPos, true);
            return true;
        }
        return false;
    }
}
