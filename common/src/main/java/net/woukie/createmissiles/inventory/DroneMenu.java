package net.woukie.createmissiles.inventory;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.woukie.createmissiles.registry.Menus;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

import static net.woukie.createmissiles.registry.Menus.DRONE_PANEL;

public class DroneMenu extends AbstractBasicMenu {

    public DroneMenu(int id, Inventory inventory) {
        super(DRONE_PANEL.get(), id, new SimpleContainer(1));
        checkContainerSize(container, 1);

        this.addSlot(new Slot(container, 0, 152, 44) {
            public int getMaxStackSize(){return 1;}
            public boolean mayPlace(@NotNull ItemStack itemStack) {return itemStack.is(Items.MAP);}
        });

        for(int j = 0; j < 3; ++j) {
            for(int k = 0; k < 9; ++k) {
                this.addSlot(new Slot(inventory, k + j * 9 + 9, 8 + k * 18, 84 + j * 18));
            }
        }

        for(int j = 0; j < 9; ++j) {
            this.addSlot(new Slot(inventory, j, 8 + j * 18, 142));
        }
    }
    @Override
    public boolean stillValid(@NotNull Player player) {
        return true;
    }
    @Override
    public void removed(@NotNull Player player) {
        super.removed(player);
        if (!player.level().isClientSide) {
            if (!player.isAlive() || player instanceof ServerPlayer && ((ServerPlayer)player).hasDisconnected()) {
                ItemStack itemStack = this.container.removeItemNoUpdate(0);
                if (!itemStack.isEmpty()) {
                    player.drop(itemStack, false);
                }
            } else if (player instanceof ServerPlayer) {
                player.getInventory().placeItemBackInInventory(this.container.removeItemNoUpdate(0));
            }

        }
    }
}
