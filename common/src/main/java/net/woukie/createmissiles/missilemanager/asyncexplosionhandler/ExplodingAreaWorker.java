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
        boolean includeLowerX = origin.x - start.getX() - 0.5 >= (int) maxDistance;
        boolean includeUpperX = end.getX() + 0.5 - origin.x >= (int) maxDistance - 1;
        boolean includeLowerY = origin.y - start.getY() - 0.5 >= (int) maxDistance;
        boolean includeUpperY = end.getY() + 0.5 - origin.y >= (int) maxDistance - 1;
        boolean includeLowerZ = origin.z - start.getZ() - 0.5 >= (int) maxDistance;
        boolean includeUpperZ = end.getZ() + 0.5 - origin.z >= (int) maxDistance - 1;

        List<BlockPos> blocks = new ArrayList<>((int) (3 * maxDistance * maxDistance));

        if (includeLowerX || includeUpperX) {
            for (int z = start.getZ(); z < end.getZ(); z++) {
                for (int y = start.getY(); y < end.getY(); y++) {
                    if (includeLowerX) blocks.add((int)(Math.random() * blocks.size()), new BlockPos(start.getX(), y, z));
                    if (includeUpperX) blocks.add((int)(Math.random() * blocks.size()), new BlockPos(end.getX(), y, z));
                }
            }
        }

        if (includeLowerY || includeUpperY) {
            for (int x = start.getX(); x < end.getX(); x++) {
                for (int z = start.getZ(); z < end.getZ(); z++) {
                    if (includeLowerY) blocks.add((int)(Math.random() * blocks.size()), new BlockPos(x, start.getY(), z));
                    if (includeUpperY) blocks.add((int)(Math.random() * blocks.size()), new BlockPos(x, end.getY(), z));
                }
            }
        }

        if (includeLowerZ || includeUpperZ) {
            for (int x = start.getX(); x < end.getX(); x++) {
                for (int y = start.getY(); y < end.getY(); y++) {
                    if (includeLowerZ) blocks.add((int)(Math.random() * blocks.size()), new BlockPos(x, y, start.getZ()));
                    if (includeUpperZ) blocks.add((int)(Math.random() * blocks.size()), new BlockPos(x, y, end.getZ()));
                }
            }
        }

        for (BlockPos pos : blocks)
            traverseBlock(pos);
        calculatedBlocks = true;
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
