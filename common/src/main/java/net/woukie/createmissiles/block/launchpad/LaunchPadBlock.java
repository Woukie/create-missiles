package net.woukie.createmissiles.block.launchpad;

import com.simibubi.create.content.kinetics.base.KineticBlock;
import com.simibubi.create.content.kinetics.simpleRelays.ICogWheel;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.woukie.createmissiles.registry.MissileBlockEntities;

// TODO: implment WorldlyContainerHolder instead of having logic on blockentity
public class LaunchPadBlock extends KineticBlock implements IBE<LaunchPadBlockEntity>, ICogWheel {
    public LaunchPadBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    public Axis getRotationAxis(BlockState state) {
        return Axis.Y;
    }

    @Override
    public Class<LaunchPadBlockEntity> getBlockEntityClass() {
        return LaunchPadBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends LaunchPadBlockEntity> getBlockEntityType() {
        return MissileBlockEntities.LAUNCH_PAD.get();
    }

    @Override
    public boolean hasShaftTowards(LevelReader world, BlockPos pos, BlockState state, Direction face) {
        return face == Direction.DOWN;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return Shapes.or(Shapes.box(0, 0, 0, 1, 6.0/16.0, 1), Shapes.box(0, 10.0/16.0, 0, 1, 13.0/16.0, 1));
    }

    @Override
    public SpeedLevel getMinimumRequiredSpeedLevel() {
        return SpeedLevel.of(16);
    }

    @Override
    public boolean isSmallCog() {
        return true;
    }
}
