package net.woukie.createmissiles;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.woukie.createmissiles.block.controller.ControllerBlockEntity;
import net.woukie.createmissiles.block.launchpad.LaunchPadBlockEntity;
import net.woukie.createmissiles.registry.BlockEntities;

public class MultiblockHelper {
    public static BlockPos findCorner(BlockPos origin, Direction facing, Level level) {
        Direction right = facing.getClockWise();
        for (int offset = 0; offset < 3; offset++) {
            BlockPos corner = origin.relative(facing).relative(right, -offset);
            if (checkForLaunchPad(corner, right, facing, level)) {
                return corner;
            }
        }

        return null;
    }

    public static BlockEntity findEdgeBlock(BlockEntity origin, Level level, BlockEntityType<?> type) {
        Direction facing = origin.getBlockState().getValue(HorizontalDirectionalBlock.FACING).getOpposite();
        BlockPos corner = MultiblockHelper.findCorner(origin.getBlockPos(), facing, level);
        return MultiblockHelper.findEdgeBlock(corner, facing, level, type);
    }

    public static BlockEntity findEdgeBlock(BlockPos corner, Direction facing, Level level, BlockEntityType<?> type) {
        if (corner == null)
            return null;

        Direction right = facing.getClockWise();
        for (int i = 0; i < 3; i++) {
            BlockPos leftEdge = corner.relative(right, -1).relative(facing, i);
            BlockEntity leftEntity = level.getBlockEntity(leftEdge);
            if (leftEntity != null && leftEntity.getType() == type)
                if (leftEntity.getBlockState().getValue(HorizontalDirectionalBlock.FACING) == right.getOpposite())
                    return leftEntity;

            BlockPos farEdge = corner.relative(facing, 3).relative(right, i);
            BlockEntity farEntity = level.getBlockEntity(farEdge);
            if (farEntity != null && farEntity.getType() == type)
                if (farEntity.getBlockState().getValue(HorizontalDirectionalBlock.FACING) == facing)
                    return farEntity;

            BlockPos backEdge = corner.relative(facing, -1).relative(right, i);
            BlockEntity backEntity = level.getBlockEntity(backEdge);
            if (backEntity != null && backEntity.getType() == type)
                if (backEntity.getBlockState().getValue(HorizontalDirectionalBlock.FACING) == facing.getOpposite())
                    return backEntity;

            BlockPos rightEdge = corner.relative(right, 3).relative(facing, i);
            BlockEntity rightEntity = level.getBlockEntity(rightEdge);
            if (rightEntity != null && rightEntity.getType() == type)
                if (rightEntity.getBlockState().getValue(HorizontalDirectionalBlock.FACING) == right)
                    return rightEntity;
        }

        return null;
    }

    private static boolean checkForLaunchPad(BlockPos corner, Direction right, Direction facing, Level level) {
        for (int x = 0; x < 3; x++) {
            for (int z = 0; z < 3; z++) {
                BlockPos targetBlock = corner.relative(right, x).relative(facing, z);
                BlockEntity blockEntity = level.getBlockEntity(targetBlock);
                if (!(blockEntity instanceof LaunchPadBlockEntity))
                    return false;
            }
        }

        return true;
    }

    public static ControllerBlockEntity findControllerFromLaunchPad(Level level, BlockPos pos) {
        Direction forward = Direction.NORTH;
        Direction right = forward.getClockWise();

        BlockPos corner = pos;
        while (level.getBlockEntity(corner.relative(forward)) instanceof LaunchPadBlockEntity)
            corner = corner.relative(forward);
        while (level.getBlockEntity(corner.relative(right)) instanceof LaunchPadBlockEntity)
            corner = corner.relative(right);

        for (int x = 0; x < 3; x++)
            for (int z = 0; z < 3; z++)
                if (!(level.getBlockEntity(corner.relative(forward, -x).relative(right, -z)) instanceof LaunchPadBlockEntity))
                    return null;

        BlockEntity blockEntity = MultiblockHelper.findEdgeBlock(corner, forward.getOpposite(), level, BlockEntities.CONTROLLER.get());
        if (blockEntity != null)
            return (ControllerBlockEntity)blockEntity;

        return null;
    }
}
