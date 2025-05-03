package net.woukie.createmissiles.block.launchpadcontroller;

import com.simibubi.create.foundation.utility.VoxelShaper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.woukie.createmissiles.registry.MissileBlockEntities;

public class LaunchPadControllerBlock extends Block implements EntityBlock {
    private static final VoxelShape LeftPillarNorth = Shapes.box(12/16.0, 0/16.0, 15/16.0, 14/16.0, 15/16.0, 16/16.0);
    private static final VoxelShape RightPillarNorth = Shapes.box(2/16.0, 0/16.0, 15/16.0, 4/16.0, 15/16.0, 16/16.0);
    private static final VoxelShape DisplayNorth = Shapes.box(1/16.0, 9.325/16.0, 12.05/16.0, 15/16.0, 17.075/16.0, 1);
    private static final VoxelShape BaseBoxNorth = Shapes.box(3/16.0, 1/16.0, 13/16.0, 13/16.0, 5/16.0, 15/16.0);

    private static final VoxelShape VoxelShape = Shapes.or(LeftPillarNorth, RightPillarNorth, DisplayNorth, BaseBoxNorth);

    public static final DirectionProperty FACING;

    public LaunchPadControllerBlock(Properties properties) {
        super(properties);
        this.registerDefaultState((this.stateDefinition.any()).setValue(FACING, Direction.NORTH));
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
            if (blockEntity instanceof LaunchPadControllerBlockEntity) {
                Containers.dropContents(level, blockPos, (LaunchPadControllerBlockEntity)blockEntity);
            }

            super.onRemove(blockState, level, blockPos, blockState2, bl);
        }
    }

    @Override
    public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return VoxelShaper.forDirectional(VoxelShape, Direction.NORTH).get(blockState.getValue(FACING));
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext) {
        return this.defaultBlockState().setValue(FACING, blockPlaceContext.getHorizontalDirection().getOpposite());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    public BlockState rotate(BlockState blockState, Rotation rotation) {
        return blockState.setValue(FACING, rotation.rotate(blockState.getValue(FACING)));
    }

    public BlockState mirror(BlockState blockState, Mirror mirror) {
        return blockState.rotate(mirror.getRotation(blockState.getValue(FACING)));
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new LaunchPadControllerBlockEntity(MissileBlockEntities.LAUNCH_PAD_CONTROLLER.get(), blockPos, blockState);
    }

    protected void openContainer(Level level, BlockPos blockPos, Player player) {
        BlockEntity blockEntity = level.getBlockEntity(blockPos);
        if (blockEntity instanceof LaunchPadControllerBlockEntity) {
            player.openMenu((MenuProvider)blockEntity);
        }
    }

    public void setPlacedBy(Level level, BlockPos blockPos, BlockState blockState, LivingEntity livingEntity, ItemStack itemStack) {
        if (itemStack.hasCustomHoverName()) {
            BlockEntity blockEntity = level.getBlockEntity(blockPos);
            if (blockEntity instanceof LaunchPadControllerBlockEntity) {
                ((LaunchPadControllerBlockEntity)blockEntity).setCustomName(itemStack.getHoverName());
            }
        }
    }

    static {
        FACING = BlockStateProperties.HORIZONTAL_FACING;
    }
}
