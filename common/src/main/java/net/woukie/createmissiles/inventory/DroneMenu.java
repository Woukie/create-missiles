package net.woukie.createmissiles.inventory;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.woukie.createmissiles.CreateMissiles;
import net.woukie.createmissiles.entity.drone.SendDroneMessage;
import net.woukie.createmissiles.registry.Packets;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

import static net.woukie.createmissiles.registry.Menus.DRONE;

public class DroneMenu extends AbstractBasicMenu {
//    First 8 integers each represent 16 bits of the 128 bit entity UUID
    private final ContainerData dataAccess;

    public DroneMenu(int id, Inventory inventory, ContainerData dataAccess, Container container) {
        super(DRONE.get(), id, container);
        checkContainerSize(container, 1);
        checkContainerDataCount(dataAccess, 12);
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
        this(id, inventory, new SimpleContainerData(12), new SimpleContainer(1));
    }

    public boolean isBasic() {
        return dataAccess.get(11) == 0;
    }

    public boolean hasEmptyMap() {
        return getSlot(0).getItem().is(Items.MAP);
    }

    @Override
    public boolean stillValid(@NotNull Player player) {
        return dataAccess.get(10) == 0;
    }

    public int getInitialX() {
        return dataAccess.get(8);
    }

    public int getInitialZ() {
        return dataAccess.get(9);
    }

    public void clickLaunch(BlockPos desination) {
        int uuid0 = dataAccess.get(0);  // Most sig bits 63-48
        int uuid1 = dataAccess.get(1);  // Most sig bits 47-32
        int uuid2 = dataAccess.get(2);  // Most sig bits 31-16
        int uuid3 = dataAccess.get(3);  // Most sig bits 15-0
        int uuid4 = dataAccess.get(4);  // Least sig bits 63-48
        int uuid5 = dataAccess.get(5);  // Least sig bits 47-32
        int uuid6 = dataAccess.get(6);  // Least sig bits 31-16
        int uuid7 = dataAccess.get(7);  // Least sig bits 15-0
        long mostSigBits = ((long) uuid0 << 48) |
                ((long) uuid1 << 32) |
                ((long) uuid2 << 16) |
                ((long) uuid3);
        long leastSigBits = ((long) uuid4 << 48) |
                ((long) uuid5 << 32) |
                ((long) uuid6 << 16) |
                ((long) uuid7);
        CreateMissiles.LOGGER.info("CLICK LAUNCH");
        CreateMissiles.LOGGER.info(new UUID(mostSigBits, leastSigBits).toString());
        CreateMissiles.LOGGER.info(desination.toString());
        Packets.SEND_DRONE.sendToServer(new SendDroneMessage(new UUID(mostSigBits, leastSigBits), desination));
    }
}
