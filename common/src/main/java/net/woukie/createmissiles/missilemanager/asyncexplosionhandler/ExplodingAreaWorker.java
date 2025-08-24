package net.woukie.createmissiles.missilemanager.asyncexplosionhandler;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3d;

import java.util.*;
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
    private boolean calculatedBlocks = false;

    public static final double HARDNESS_MULTIPLIER = 0.3;
    public static final double HARDNESS_OFFSET = 0.315;

    public ExplodingAreaWorker(BlockPos start, BlockPos end, BlockPos originBlock, Level level, Double power, ConcurrentHashMap<BlockPos, Float> hardnessMap) {
        this.start = start;
        this.end = end;
        this.origin = new Vector3d(originBlock.getX() + 0.5, originBlock.getY() + 0.5, originBlock.getZ() + 0.5);
        this.level = level;
        this.power = power;
        this.hardnessMap = hardnessMap;
        brokenBlocks = new PriorityBlockingQueue<>();
    }

    public void run() {
        List<BlockPos> positions = new ArrayList<>();
        for (int x = start.getX(); x <= end.getX(); x++)
            for (int y = start.getY(); y <= end.getY(); y++)
                for (int z = start.getZ(); z <= end.getZ(); z++)
                    positions.add(new BlockPos(x, y, z));
        positions = positions.stream().sorted((o1, o2) -> Double.compare(origin.distance(o1.getX(), o1.getY(), o1.getZ()), origin.distance(o2.getX(), o2.getY(), o2.getZ()))).toList();

        for (BlockPos blockPos : positions) {
            Vector3d realBlockPos = new Vector3d(blockPos.getX() + 0.5, blockPos.getY() + 0.5, blockPos.getZ() + 0.5);
            if (level.isEmptyBlock(blockPos)) continue;
            final double distance = realBlockPos.distance(origin);
            if (distance > (this.power -0.3 -HARDNESS_OFFSET) / HARDNESS_OFFSET) continue;
            final float blockHardness = level.getBlockState(blockPos).getBlock().getExplosionResistance();
            hardnessMap.put(blockPos, blockHardness);
            final AtomicReference<Float> totalHardness = new AtomicReference<>(0f);
            final AtomicReference<Integer> passedCount = new AtomicReference<>(0);
            final AtomicReference<Boolean> broken = new AtomicReference<>(false);
            traverseSupercover(origin, realBlockPos, pos -> {
                final BlockPos traversedPos = BlockPos.containing(pos.x, pos.y, pos.z);
                final float hardness = hardnessMap.containsKey(traversedPos) ?
                        hardnessMap.get(traversedPos) :
                        level.getBlockState(traversedPos).getBlock().getExplosionResistance();
                totalHardness.updateAndGet(current -> current + hardness);
                passedCount.updateAndGet(a -> ++a);

                final int passedBlocks = Math.max(passedCount.get(), 1);
                final double averageHardness = totalHardness.get() / (double) passedBlocks;
                double powerLeft = (0.8 + Math.random() * 0.2) * this.power - ((HARDNESS_MULTIPLIER * averageHardness + HARDNESS_OFFSET + 0.3) * distance);
                powerLeft -= HARDNESS_MULTIPLIER * blockHardness + HARDNESS_OFFSET + 0.3;
                broken.set(powerLeft > 0);
                return powerLeft <= 0;
            });

            if (broken.get())
                brokenBlocks.offer(blockPos);
        }
        calculatedBlocks = true;
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
