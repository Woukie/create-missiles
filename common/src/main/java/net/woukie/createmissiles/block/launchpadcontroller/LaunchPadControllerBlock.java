package net.woukie.createmissiles.block.launchpadcontroller;

import com.simibubi.create.foundation.utility.VoxelShaper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.woukie.createmissiles.registry.MissileBlockEntities;
import org.jetbrains.annotations.Nullable;

public class LaunchPadControllerBlock extends HorizontalDirectionalBlock implements EntityBlock {
    private static final VoxelShape LeftPillarNorth = Shapes.box(12/16.0, 0/16.0, 15/16.0, 14/16.0, 15/16.0, 16/16.0);
    private static final VoxelShape RightPillarNorth = Shapes.box(2/16.0, 0/16.0, 15/16.0, 4/16.0, 15/16.0, 16/16.0);
    private static final VoxelShape DisplayNorth = Shapes.box(1/16.0, 9.325/16.0, 12.05/16.0, 15/16.0, 17.075/16.0, 1);
    private static final VoxelShape BaseBoxNorth = Shapes.box(3/16.0, 1/16.0, 13/16.0, 13/16.0, 5/16.0, 15/16.0);

    private static final VoxelShape VoxelShape = Shapes.or(LeftPillarNorth, RightPillarNorth, DisplayNorth, BaseBoxNorth);

    public LaunchPadControllerBlock(Properties properties) {
        super(properties);
        this.registerDefaultState((this.stateDefinition.any()).setValue(FACING, Direction.NORTH));
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

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new LaunchPadControllerBlockEntity(MissileBlockEntities.LAUNCH_PAD_CONTROLLER.get(), blockPos, blockState);
    }
}
