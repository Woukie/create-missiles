package net.woukie.createmissiles.inventory;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractBasicMenu extends AbstractContainerMenu {
    protected final Container container;

    protected AbstractBasicMenu(@Nullable MenuType<?> menuType, int id, Container container) {
        super(menuType, id);
        this.container = container;
    }

//    Fabric bug where moveItemStackTo does not consider targets maxStackCount
    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player player, int clickedSlot) {
        Slot slot = this.slots.get(clickedSlot);

        if (!slot.hasItem()) return ItemStack.EMPTY;

        ItemStack clickedStack = slot.getItem();
        ItemStack clonedStack = clickedStack.copy();

        int containerSize = container.getContainerSize();
        boolean moveToContainer = clickedSlot >= containerSize;

        if (moveToContainer) {
            if(moveItemStackTo(clickedStack, 0, containerSize)) {
                return ItemStack.EMPTY;
            }
        } else {
            if (moveItemStackTo(clickedStack, containerSize, slots.size())) {
                return ItemStack.EMPTY;
            }
        }

        // Update original stack size
        if (clickedStack.isEmpty()) {
            slot.setByPlayer(ItemStack.EMPTY);
        } else {
            slot.setChanged();
        }

        if (clickedStack.getCount() == clonedStack.getCount()) {
            return ItemStack.EMPTY;
        }

        slot.onTake(player, clickedStack);

        return clonedStack;
    }

    @Override
    public boolean stillValid(@NotNull Player player) {
        return this.container.stillValid(player);
    }

//    Considers max slot size and doesn't have that backwards parameter
    protected boolean moveItemStackTo(@NotNull ItemStack clickedStack, int startSlotIndex, int endSlotIndex) {
        boolean success = false;
        int currentSlotIndex = startSlotIndex;

        if (clickedStack.isStackable()) {
            while(!clickedStack.isEmpty() && currentSlotIndex < endSlotIndex) {
                Slot slot = getSlot(currentSlotIndex);
                ItemStack targetStack = slot.getItem();

                if (!targetStack.isEmpty() && ItemStack.isSameItemSameComponents(clickedStack, targetStack)) {
                    var maxStackSize = Math.min(targetStack.getMaxStackSize(), slot.getMaxStackSize());

                    if (targetStack.getCount() < maxStackSize) {
                        int moveCapacity = maxStackSize - targetStack.getCount();
                        int toMove = Math.min(clickedStack.getCount(), moveCapacity);

                        clickedStack.shrink(toMove);
                        targetStack.grow(toMove);

                        if (toMove > 0) {
                            slot.setChanged();
                            success = true;
                        }
                    }
                }

                ++currentSlotIndex;
            }
        }

        if (!clickedStack.isEmpty()) {
            currentSlotIndex = startSlotIndex;

            while(currentSlotIndex < endSlotIndex) {
                Slot slot = getSlot(currentSlotIndex);
                ItemStack targetStack = slot.getItem();

                if (targetStack.isEmpty() && slot.mayPlace(clickedStack)) {
                    slot.setByPlayer(clickedStack.split(Math.min(slot.getMaxStackSize(), clickedStack.getCount())));
                    slot.setChanged();
                    success = true;
                    break;
                }

                ++currentSlotIndex;
            }
        }

        return !success;
    }
}
