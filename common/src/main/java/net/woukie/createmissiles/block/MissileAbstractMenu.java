package net.woukie.createmissiles.block;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class MissileAbstractMenu extends AbstractContainerMenu {
    private final Container container;

    protected MissileAbstractMenu(@Nullable MenuType<?> menuType, int id, Container container) {
        super(menuType, id);
        this.container = container;
    }

    @Override
    public ItemStack quickMoveStack(@NotNull Player player, int i) {
        Slot clickedSlot = getSlot(i);
        if (!clickedSlot.hasItem())
            return ItemStack.EMPTY;

        ItemStack stack = clickedSlot.getItem();
        int size = container.getContainerSize();
        boolean success;
        if (i < size) {
            success = !moveItemStackTo(stack, size, slots.size(), false);
        } else
            success = !moveItemStackTo(stack, 0, size, false);

        return success ? ItemStack.EMPTY : stack;
    }

    @Override
    public boolean stillValid(@NotNull Player player) {
        return this.container.stillValid(player);
    }
}
