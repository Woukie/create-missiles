package net.woukie.createmissiles.block.launchpad;

import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.Container;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.woukie.createmissiles.MultiblockHelper;
import net.woukie.createmissiles.block.controller.ControllerBlockEntity;
import net.woukie.createmissiles.registry.MissileBlockEntities;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class LaunchPadBlockEntity extends KineticBlockEntity implements Container, WorldlyContainer {
    public LaunchPadBlockEntity(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);
    }

    private ControllerBlockEntity getController() {
        if (level == null) return null;

        Direction forward = Direction.NORTH;
        Direction right = forward.getClockWise();

        BlockPos corner = getBlockPos();
        while (level.getBlockEntity(corner.relative(forward)) instanceof LaunchPadBlockEntity)
            corner = corner.relative(forward);
        while (level.getBlockEntity(corner.relative(right)) instanceof LaunchPadBlockEntity)
            corner = corner.relative(right);

        for (int x = 0; x < 3; x++)
            for (int z = 0; z < 3; z++)
                if (!(level.getBlockEntity(corner.relative(forward, -x).relative(right, -z)) instanceof LaunchPadBlockEntity))
                    return null;

        BlockEntity blockEntity = MultiblockHelper.findEdgeBlock(corner, forward.getOpposite(), level, MissileBlockEntities.CONTROLLER.get());
        if (blockEntity != null)
            return (ControllerBlockEntity)blockEntity;

        return null;
    }

    @Override
    public int getContainerSize() {
        return 1;
    }

    @Override
    public boolean isEmpty() {
        return true;
    }

    @Override
    public @NotNull ItemStack getItem(int i) {
        return ItemStack.EMPTY;
    }

    @Override
    public @NotNull ItemStack removeItem(int i, int j) {
        if (i != 0 || j <= 0) return ItemStack.EMPTY;
        return ItemStack.EMPTY;
    }

    @Override
    public @NotNull ItemStack removeItemNoUpdate(int i) {
        return ItemStack.EMPTY;
    }

    @Override
    public void setItem(int i, @NotNull ItemStack itemStack) {
        if (i != 0) return;
        ControllerBlockEntity controller = getController();
        if (controller == null) return;
        controller.giveItem(itemStack);
    }

    @Override
    public boolean stillValid(@NotNull Player player) {
        return Container.stillValidBlockEntity(this, player);
    }

    @Override
    public void clearContent() { }

    @Override
    public boolean canPlaceItem(int i, ItemStack itemStack) {
        ControllerBlockEntity controller = getController();
        if (controller == null) return false;
        return controller.canGiveItem(itemStack) == 0;
    }

    @Override
    public int @NotNull [] getSlotsForFace(@NotNull Direction direction) {
        return new int[]{0};
    }

    @Override
    public boolean canPlaceItemThroughFace(int i, @NotNull ItemStack itemStack, @Nullable Direction direction) {
        return true;
    }

    @Override
    public boolean canTakeItemThroughFace(int i, @NotNull ItemStack itemStack, @NotNull Direction direction) {
        return false;
    }
}
