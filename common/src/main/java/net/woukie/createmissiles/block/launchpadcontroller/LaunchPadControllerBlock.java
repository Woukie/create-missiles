package net.woukie.createmissiles.block.launchpadcontroller;

import com.simibubi.create.foundation.block.IBE;
import com.simibubi.create.foundation.utility.VoxelShaper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.woukie.createmissiles.registry.MissileBlockEntities;

public class LaunchPadControllerBlock extends HorizontalDirectionalBlock implements IBE<LaunchPadControllerBlockEntity> {
    private static final VoxelShape leftPillarNorth = Shapes.box(12/16.0, 0/16.0, 15/16.0, 14/16.0, 15/16.0, 16/16.0);
    private static final VoxelShape rightPillarNorth = Shapes.box(2/16.0, 0/16.0, 15/16.0, 4/16.0, 15/16.0, 16/16.0);
    private static final VoxelShape displayNorth = Shapes.box(1/16.0, 9.325/16.0, 12.05/16.0, 15/16.0, 17.075/16.0, 1);
    private static final VoxelShape baseBoxNorth = Shapes.box(3/16.0, 1/16.0, 13/16.0, 13/16.0, 5/16.0, 15/16.0);
    private static final VoxelShape voxelShape = Shapes.or(leftPillarNorth, rightPillarNorth, displayNorth, baseBoxNorth);

    public LaunchPadControllerBlock(Properties properties) {
        super(properties);
    }

    public InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        } else {
            this.openContainer(level, blockPos, player);
            return InteractionResult.CONSUME;
        }
    }

    public void onRemove(BlockState blockState, Level level, BlockPos blockPos, BlockState blockState2, boolean bl) {
        if (!blockState.is(blockState2.getBlock())) {
            BlockEntity blockEntity = level.getBlockEntity(blockPos);
            if (blockEntity instanceof LaunchPadControllerBlockEntity controller) {
                Containers.dropContents(level, blockPos, controller);
            }

            super.onRemove(blockState, level, blockPos, blockState2, bl);
        }
    }


    @Override
    public RenderShape getRenderShape(BlockState blockState) {
        return RenderShape.MODEL;
    }

    @Override
    public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return VoxelShaper.forDirectional(voxelShape, Direction.NORTH).get(blockState.getValue(FACING));
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext) {
        return this.defaultBlockState().setValue(FACING, blockPlaceContext.getHorizontalDirection().getOpposite());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public <S extends BlockEntity> BlockEntityTicker<S> getTicker(Level level, BlockState blockState, BlockEntityType<S> type) {
        return (level1, blockPos, blockState1, blockEntity) -> {
            if (blockEntity instanceof LaunchPadControllerBlockEntity controller)
                controller.tick();
        };
    }

    @Override
    public Class<LaunchPadControllerBlockEntity> getBlockEntityClass() {
        return LaunchPadControllerBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends LaunchPadControllerBlockEntity> getBlockEntityType() {
        return MissileBlockEntities.LAUNCH_PAD_CONTROLLER.get();
    }

    protected void openContainer(Level level, BlockPos blockPos, Player player) {
        BlockEntity blockEntity = level.getBlockEntity(blockPos);
        if (blockEntity instanceof LaunchPadControllerBlockEntity) {
            player.openMenu((MenuProvider)blockEntity);
        }
    }
}
