package net.woukie.createmissiles.block.launchpad;

import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.Container;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class LaunchPadBlockEntity extends KineticBlockEntity implements Container, WorldlyContainer {
    protected ItemStack item;

    public LaunchPadBlockEntity(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);
    }

    @Override
    public int getContainerSize() {
        return 1;
    }

    @Override
    public boolean isEmpty() {
        return item.isEmpty();
    }

    @Override
    public @NotNull ItemStack getItem(int i) {
        return i == 0 ? item : ItemStack.EMPTY;
    }

    @Override
    public @NotNull ItemStack removeItem(int i, int j) {
        if (i != 0 || j <= 0) return ItemStack.EMPTY;
        return item.split(j);
    }

    @Override
    public @NotNull ItemStack removeItemNoUpdate(int i) {
        if (i != 0) return ItemStack.EMPTY;
        ItemStack returnStack = item;
        item = ItemStack.EMPTY;
        return returnStack;
    }

    @Override
    public void setItem(int i, @NotNull ItemStack itemStack) {
        if (i == 0)
            item = itemStack;
    }

    @Override
    public boolean stillValid(@NotNull Player player) {
        return Container.stillValidBlockEntity(this, player);
    }

    @Override
    public void clearContent() {
        item = ItemStack.EMPTY;
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
