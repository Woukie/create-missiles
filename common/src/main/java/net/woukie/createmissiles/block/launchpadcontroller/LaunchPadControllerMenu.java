package net.woukie.createmissiles.block.launchpadcontroller;

import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.woukie.createmissiles.registry.MissileMenus;
import org.jetbrains.annotations.NotNull;

public class LaunchPadControllerMenu extends AbstractContainerMenu {
    private final Container container;

    public LaunchPadControllerMenu(MenuType<LaunchPadControllerMenu> launchPadControllerMenuMenuType, int i, Inventory inventory) {
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
    public ItemStack quickMoveStack(Player player, int i) {
        return null;
    }

    @Override
    public boolean stillValid(Player player) {
        return this.container.stillValid(player);
    }
}
