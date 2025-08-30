package net.woukie.createmissiles.missilemanager.asyncexplosionhandler;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.level.Level;
import org.joml.Vector3d;

import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.atomic.AtomicReference;

import static net.woukie.createmissiles.Util.traverseSupercover;

public class ExplodingAreaWorker implements Runnable {
    private final BlockPos start, end;
    private final Vector3d origin;
    private final Level level;
    private final Double power, decay;
    private final ConcurrentHashMap<BlockPos, Float> hardnessMap;
    private final double maxDistance;

    private PriorityBlockingQueue<BlockPos> brokenBlocks;
    private boolean calculatedBlocks = false;
    private boolean doneX = false;
    private boolean doneY = false;
    private boolean doneZ = false;
    private int countX = 0;
    private int countY = 0;
    private int countZ = 0;

    private boolean endEarly = false;

    public static final double HARDNESS_MULTIPLIER = 0.3;
    public static final double HARDNESS_OFFSET = 0.315;

    public ExplodingAreaWorker(BlockPos start, BlockPos end, BlockPos originBlock, Level level, Double power, ConcurrentHashMap<BlockPos, Float> hardnessMap, Double decay) {
        this.start = start;
        this.end = end;
        this.origin = new Vector3d(originBlock.getX() + 0.5, originBlock.getY() + 0.5, originBlock.getZ() + 0.5);
        this.level = level;
        this.power = power;
        this.decay = decay;
        this.hardnessMap = hardnessMap;
        this.maxDistance = (this.power - this.decay - HARDNESS_OFFSET) / HARDNESS_OFFSET;
        brokenBlocks = new PriorityBlockingQueue<>();
    }

    public void run() {
        final boolean includeLowerX = origin.x - start.getX() - 0.5 >= (int) maxDistance;
        final boolean includeUpperX = end.getX() + 0.5 - origin.x >= (int) maxDistance - 1;
        final boolean includeLowerY = origin.y - start.getY() - 0.5 >= (int) maxDistance;
        final boolean includeUpperY = end.getY() + 0.5 - origin.y >= (int) maxDistance - 1;
        final boolean includeLowerZ = origin.z - start.getZ() - 0.5 >= (int) maxDistance;
        final boolean includeUpperZ = end.getZ() + 0.5 - origin.z >= (int) maxDistance - 1;

        while ((!this.doneX || !this.doneY || !this.doneZ)) {
            if (endEarly) return;
            processX(includeLowerX, includeUpperX);
            processY(includeLowerY, includeUpperY);
            processZ(includeLowerZ, includeUpperZ);
        }

        calculatedBlocks = true;
    }

    private void processX(boolean includeLowerX, boolean includeUpperX) {
        if (doneX) return;
        if (includeLowerX || includeUpperX) {
            if (countX >= maxDistance * maxDistance) doneX = true;
            countX++;
            int z = (int)(Math.random() * (end.getZ() - start.getZ())) + start.getZ();
            int y = (int)(Math.random() * (end.getY() - start.getY())) + start.getY();
            if (includeLowerX) traverseBlock(new BlockPos(start.getX(), y, z));
            if (includeUpperX) traverseBlock(new BlockPos(end.getX(), y, z));
            return;
        }
        doneX = true;
    }

    private void processY(boolean includeLowerY, boolean includeUpperY) {
        if (doneY) return;
        if (includeLowerY || includeUpperY) {
            if (countY >= maxDistance * maxDistance) doneY = true;
            countY++;
            int x = (int)(Math.random() * (end.getX() - start.getX())) + start.getX();
            int z = (int)(Math.random() * (end.getZ() - start.getZ())) + start.getZ();
            if (includeLowerY) traverseBlock(new BlockPos(x, start.getY(), z));
            if (includeUpperY) traverseBlock(new BlockPos(x, end.getY(), z));
            return;
        }
        doneY = true;
    }

    private void processZ(boolean includeLowerZ, boolean includeUpperZ) {
        if (doneZ) return;
        if (includeLowerZ || includeUpperZ) {
            if (countZ >= maxDistance * maxDistance) doneZ = true;
            countZ++;
            int x = (int)(Math.random() * (end.getX() - start.getX())) + start.getX();
            int y = (int)(Math.random() * (end.getY() - start.getY())) + start.getY();
            if (includeLowerZ) traverseBlock(new BlockPos(x, y, start.getZ()));
            if (includeUpperZ) traverseBlock(new BlockPos(x, y, end.getZ()));
            return;
        }
        doneZ = true;
    }

    private void traverseBlock(BlockPos blockPos) {
        if (endEarly) return;
        final double startPower = (0.8 + Math.random() * 0.2) * this.power;
        final AtomicReference<Float> totalHardness = new AtomicReference<>(0f);
        final AtomicReference<Integer> passedCount = new AtomicReference<>(0);
        traverseSupercover(origin, new Vector3d(blockPos.getX() + 0.5, blockPos.getY() + 0.5, blockPos.getZ() + 0.5), traversedPos -> {
            if (endEarly) return true;
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
                final double powerLeft = startPower - ((HARDNESS_MULTIPLIER * averageHardness + HARDNESS_OFFSET + this.decay) * distance);
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
        if (endEarly) return false;
        BlockPos blockPos = brokenBlocks.poll();
        if (blockPos != null) {
            level.destroyBlock(blockPos, true);
            return true;
        }
        return false;
    }

    public void endEarly() {
        this.endEarly = true;
    }

    public CompoundTag save() {
        CompoundTag data = new CompoundTag();
        data.putInt("CountX", countX);
        data.putInt("CountY", countY);
        data.putInt("CountZ", countZ);
        data.putLongArray("BrokenBlocks", brokenBlocks.stream().map(BlockPos::asLong).toList());
        System.out.println(brokenBlocks.size());
        return data;
    }

    public void load(CompoundTag data) {
        countX = data.getInt("CountX");
        countY = data.getInt("CountY");
        countZ = data.getInt("CountZ");
        brokenBlocks = new PriorityBlockingQueue<>(Arrays.stream(data.getLongArray("BrokenBlocks")).mapToObj(BlockPos::of).toList());
        System.out.println(brokenBlocks.size());
    }
}
