package net.woukie.createmissiles.block.launchpadcontroller;

import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.MapItem;
import net.woukie.createmissiles.registry.MissileMenus;
import org.jetbrains.annotations.NotNull;

public class LaunchPadControllerMenu extends AbstractContainerMenu {
    private final Container container;

    public LaunchPadControllerMenu(MenuType<?> type, int i, Inventory inventory) {
        this(i, inventory, new SimpleContainer(2));
    }

    public LaunchPadControllerMenu(int i, Inventory playerInventory, Container container) {
        super(MissileMenus.LAUNCH_PAD_CONTROLLER_MENU.get(), i);
        checkContainerSize(container, 2);
        this.container = container;

        this.addSlot(new Slot(container, 1, 14, 21) {
            public boolean mayPlace(@NotNull ItemStack itemStack) {
                return itemStack.is(Items.FILLED_MAP);
            }
        });

        this.addSlot(new Slot(container, 0, 14, 55) {
            public boolean mayPlace(@NotNull ItemStack itemStack) {
                return itemStack.is(Items.FIREWORK_ROCKET);
            }
        });

        for(int j = 0; j < 3; ++j) {
            for(int k = 0; k < 9; ++k) {
                this.addSlot(new Slot(playerInventory, k + j * 9 + 9, 8 + k * 18, 84 + j * 18));
            }
        }

        for(int j = 0; j < 9; ++j) {
            this.addSlot(new Slot(playerInventory, j, 8 + j * 18, 142));
        }
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        Slot clickedSlot = getSlot(index);
        if (!clickedSlot.hasItem())
            return ItemStack.EMPTY;

        ItemStack stack = clickedSlot.getItem();
        int size = container.getContainerSize();
        boolean success;
        if (index < size) {
            success = !moveItemStackTo(stack, size, slots.size(), false);
        } else
            success = !moveItemStackTo(stack, 0, size - 1, false);

        return success ? ItemStack.EMPTY : stack;
    }

    @Override
    public boolean stillValid(Player player) {
        return this.container.stillValid(player);
    }

//    @Override
//    public void clicked(int i, int j, ClickType clickType, Player player) {
//        ItemStack itemstack = this.container.getItem(1);
//
//        Integer mapId = MapItem.getMapId(itemstack);
//        MapItemSavedData mapItemSavedData = MapItem.getSavedData(mapId, player.level());
//
//        if (mapItemSavedData != null) {
//            int centerX = mapItemSavedData.centerX;
//            System.out.println(centerX);
//        }
//
//
//        super.clicked(i, j, clickType, player);
//    }

    //    Map click positions are from 0 to 128 inclusive
    public void clickMap(int mapClickX, int mapClickZ) {

        ItemStack itemstack = this.container.getItem(1);
        Integer mapId = MapItem.getMapId(itemstack);

//        MapItemSavedData mapItemSavedData = MapItem.getSavedData(mapId, level);
//
//        if (mapItemSavedData != null) {
//            int centerX = mapItemSavedData.centerX;
//            System.out.println(centerX);
//        }
    }
}
