package net.woukie.createmissiles.block;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3d;

public class FlamingFireBlock extends BaseFireBlock {
    public final int radius = 2;
    public final int height = 2;

    public FlamingFireBlock(BlockBehaviour.Properties properties) {
        super(properties, 2.0F);
    }

    @Override
    public void tick(BlockState blockState, ServerLevel serverLevel, BlockPos blockPos, RandomSource randomSource) {
        serverLevel.scheduleTick(blockPos, this, 10 + randomSource.nextInt(5));
        double distance = serverLevel.random.nextGaussian() * radius;
        double angle = serverLevel.random.nextFloat() * Math.PI * 2;
        Vector3d offset = new Vector3d(distance, 0, 0).rotateY(angle);

        for (int y = height; y >= -height; y--) {
            BlockPos position = blockPos.offset((int)offset.x, y, (int)offset.z);
            if (serverLevel.isEmptyBlock(position) && !serverLevel.isEmptyBlock(position.offset(0, -1, 0))) {
                serverLevel.setBlock(position, Blocks.FIRE.defaultBlockState(), 3);
                return;
            }
        }
    }

    @Override
    public @NotNull RenderShape getRenderShape(@NotNull BlockState blockState) {
        return RenderShape.MODEL;
    }

    @Override
    public BlockState updateShape(BlockState blockState, Direction direction, BlockState blockState2, LevelAccessor levelAccessor, BlockPos blockPos, BlockPos blockPos2) {
        return this.canSurvive(blockState, levelAccessor, blockPos) ? this.defaultBlockState() : Blocks.AIR.defaultBlockState();
    }

    @Override
    public boolean canSurvive(BlockState blockState, LevelReader levelReader, BlockPos blockPos) {
        BlockPos blockPos2 = blockPos.below();
        return levelReader.getBlockState(blockPos2).isFaceSturdy(levelReader, blockPos2, Direction.UP);
    }

    @Override
    protected boolean canBurn(BlockState blockState) {
        return true;
    }

    @Override
    public void onPlace(BlockState blockState, Level level, BlockPos blockPos, BlockState blockState2, boolean bl) {
        super.onPlace(blockState, level, blockPos, blockState2, bl);
        level.scheduleTick(blockPos, this, 0);

        if (!blockState.canSurvive(level, blockPos)) {
            level.removeBlock(blockPos, false);
        }
    }
}
