package net.woukie.createmissiles.inventory;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.woukie.createmissiles.entity.drone.SendDroneMessage;
import net.woukie.createmissiles.registry.Packets;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

import static net.woukie.createmissiles.registry.Menus.DRONE;

public class DroneMenu extends AbstractBasicMenu {
//    First 4 integers each represent 32 bits of the entity UUID
    private final ContainerData dataAccess;

    public DroneMenu(int id, Inventory inventory, ContainerData dataAccess) {
        super(DRONE.get(), id, new SimpleContainer(1));
        checkContainerSize(container, 1);
        checkContainerDataCount(dataAccess, 8);
        this.dataAccess = dataAccess;

        this.addSlot(new Slot(container, 0, 66, 54) {
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

        this.addDataSlots(dataAccess);
    }

    public DroneMenu(int id, Inventory inventory) {
        this(id, inventory, new SimpleContainerData(8));
    }

    public boolean isBasic() {
        return dataAccess.get(7) == 0;
    }

    public boolean hasEmptyMap() {
        return getSlot(0).getItem().is(Items.MAP);
    }

    @Override
    public boolean stillValid(@NotNull Player player) {
        return dataAccess.get(6) == 0;
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

    public int getInitialX() {
        return dataAccess.get(4);
    }

    public int getInitialZ() {
        return dataAccess.get(5);
    }

    public void clickLaunch(BlockPos desination) {
        int uuid0 = dataAccess.get(0);
        int uuid1 = dataAccess.get(1);
        int uuid2 = dataAccess.get(2);
        int uuid3 = dataAccess.get(3);
        long mostSigBits = ((long) uuid0 << 32) | (uuid1 & 0xFFFFFFFFL);
        long leastSigBits = ((long) uuid2 << 32) | (uuid3 & 0xFFFFFFFFL);
        Packets.SEND_DRONE.sendToServer(new SendDroneMessage(new UUID(mostSigBits, leastSigBits), desination));
    }
}
