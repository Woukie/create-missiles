package net.woukie.createmissiles.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class InfernalAshLayer extends Block {
    public static final int MAX_HEIGHT = 8;
    public static final IntegerProperty LAYERS;
    protected static final VoxelShape[] SHAPE_BY_LAYER;
    public static final int HEIGHT_IMPASSABLE = 5;

    public InfernalAshLayer(BlockBehaviour.Properties properties) {
        super(properties);
        properties.forceSolidOn();
        this.registerDefaultState((this.stateDefinition.any()).setValue(LAYERS, 1));
    }

    @Override
    public void stepOn(Level level, BlockPos blockPos, BlockState blockState, Entity entity) {
        entity.hurt(level.damageSources().onFire(), 1);
        super.stepOn(level, blockPos, blockState, entity);
    }

    @Override
    public boolean canBeReplaced(BlockState blockState, Fluid fluid) {
        return super.canBeReplaced(blockState, fluid);
    }

    @Override
    public boolean isPathfindable(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, PathComputationType pathComputationType) {
        if (Objects.requireNonNull(pathComputationType) == PathComputationType.LAND) {
            return blockState.getValue(LAYERS) < HEIGHT_IMPASSABLE;
        }
        return false;
    }

    @Override
    public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return SHAPE_BY_LAYER[blockState.getValue(LAYERS)];
    }

    @Override
    public VoxelShape getCollisionShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return SHAPE_BY_LAYER[blockState.getValue(LAYERS) - 1];
    }

    @Override
    public VoxelShape getBlockSupportShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos) {
        return SHAPE_BY_LAYER[blockState.getValue(LAYERS)];
    }

    @Override
    public VoxelShape getVisualShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return SHAPE_BY_LAYER[blockState.getValue(LAYERS)];
    }

    @Override
    public boolean useShapeForLightOcclusion(BlockState blockState) {
        return true;
    }

    @Override
    public float getShadeBrightness(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos) {
        return blockState.getValue(LAYERS) == MAX_HEIGHT ? 0.2F : 1.0F;
    }

    @Override
    public boolean canSurvive(BlockState blockState, LevelReader levelReader, BlockPos blockPos) {
        BlockState blockState2 = levelReader.getBlockState(blockPos.below());
        if (blockState2.is(BlockTags.SNOW_LAYER_CANNOT_SURVIVE_ON)) {
            return false;
        } else if (blockState2.is(BlockTags.SNOW_LAYER_CAN_SURVIVE_ON)) {
            return true;
        } else {
            return Block.isFaceFull(blockState2.getCollisionShape(levelReader, blockPos.below()), Direction.UP) || blockState2.is(this) && blockState2.getValue(LAYERS) == MAX_HEIGHT;
        }
    }

    @Override
    public BlockState updateShape(BlockState blockState, Direction direction, BlockState blockState2, LevelAccessor levelAccessor, BlockPos blockPos, BlockPos blockPos2) {
        return !blockState.canSurvive(levelAccessor, blockPos) ? Blocks.AIR.defaultBlockState() : super.updateShape(blockState, direction, blockState2, levelAccessor, blockPos, blockPos2);
    }

    @Override
    public boolean canBeReplaced(BlockState blockState, BlockPlaceContext blockPlaceContext) {
        int i = blockState.getValue(LAYERS);
        if (blockPlaceContext.getItemInHand().is(this.asItem()) && i < MAX_HEIGHT) {
            if (blockPlaceContext.replacingClickedOnBlock()) {
                return blockPlaceContext.getClickedFace() == Direction.UP;
            } else {
                return true;
            }
        } else {
            return i == 1;
        }
    }

    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext) {
        BlockState blockState = blockPlaceContext.getLevel().getBlockState(blockPlaceContext.getClickedPos());
        if (blockState.is(this)) {
            int i = blockState.getValue(LAYERS);
            return blockState.setValue(LAYERS, Math.min(MAX_HEIGHT, i + 1));
        } else {
            return super.getStateForPlacement(blockPlaceContext);
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(LAYERS);
    }

    static {
        LAYERS = BlockStateProperties.LAYERS;
        SHAPE_BY_LAYER = new VoxelShape[]{Shapes.empty(), Block.box(0.0F, 0.0F, 0.0F, 16.0F, 2.0F, 16.0F), Block.box(0.0F, 0.0F, 0.0F, 16.0F, 4.0F, 16.0F), Block.box(0.0F, 0.0F, 0.0F, 16.0F, 6.0F, 16.0F), Block.box(0.0F, 0.0F, 0.0F, 16.0F, 8.0F, 16.0F), Block.box(0.0F, 0.0F, 0.0F, 16.0F, 10.0F, 16.0F), Block.box(0.0F, 0.0F, 0.0F, 16.0F, 12.0F, 16.0F), Block.box(0.0F, 0.0F, 0.0F, 16.0F, 14.0F, 16.0F), Block.box(0.0F, 0.0F, 0.0F, 16.0F, 16.0F, 16.0F)};
    }
}
