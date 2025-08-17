package net.woukie.createmissiles.block.launchpad;

import com.simibubi.create.content.kinetics.base.KineticBlock;
import com.simibubi.create.content.kinetics.simpleRelays.ICogWheel;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.WorldlyContainerHolder;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.woukie.createmissiles.MultiblockHelper;
import net.woukie.createmissiles.block.controlpanel.ControlPanelBlockEntity;
import net.woukie.createmissiles.registry.BlockEntities;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class LaunchPadBlock extends KineticBlock implements IBE<LaunchPadBlockEntity>, ICogWheel, WorldlyContainerHolder {
    public LaunchPadBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull WorldlyContainer getContainer(@NotNull BlockState blockState, @NotNull LevelAccessor levelAccessor, @NotNull BlockPos blockPos) {
            return new InputContainer(levelAccessor, blockPos);
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
        return BlockEntities.LAUNCH_PAD.get();
    }

    @Override
    public boolean hasShaftTowards(LevelReader world, BlockPos pos, BlockState state, Direction face) {
        return face == Direction.DOWN;
    }

    @Override
    public @NotNull VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter worldIn, @NotNull BlockPos pos, @NotNull CollisionContext context) {
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

    static class InputContainer extends SimpleContainer implements WorldlyContainer {
        private final LevelAccessor level;
        private final BlockPos pos;

        public InputContainer(LevelAccessor levelAccessor, BlockPos blockPos) {
            super(1);
            this.level = levelAccessor;
            this.pos = blockPos;
        }

        @Override
        public int getMaxStackSize() {
            return 1;
        }

        @Override
        public int @NotNull [] getSlotsForFace(@NotNull Direction direction) {
            return new int[]{0};
        }

        @Override
        public boolean canPlaceItemThroughFace(int i, @NotNull ItemStack itemStack, @Nullable Direction direction) {
            ControlPanelBlockEntity controlPanelBlockEntity = MultiblockHelper.findControlPanelFromLaunchPad((Level) level, pos);
            if (controlPanelBlockEntity == null) return false;
            return controlPanelBlockEntity.findAcceptingRecipe(itemStack) != null;
        }

        @Override
        public boolean canTakeItemThroughFace(int i, @NotNull ItemStack itemStack, @NotNull Direction direction) {
            return false;
        }

        @Override
        public void setChanged() {
            ItemStack item = getItem(0);
            if (item.isEmpty()) return;

            ControlPanelBlockEntity controlPanelBlockEntity = MultiblockHelper.findControlPanelFromLaunchPad((Level) level, pos);
            if (controlPanelBlockEntity == null) return;
            controlPanelBlockEntity.giveItem(item);
        }
    }
}
