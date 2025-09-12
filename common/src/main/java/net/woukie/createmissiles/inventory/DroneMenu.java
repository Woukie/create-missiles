package net.woukie.createmissiles.inventory;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.IntArrayTag;
import net.minecraft.nbt.NbtUtils;
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

import java.util.Arrays;
import java.util.UUID;

import static net.woukie.createmissiles.registry.Menus.DRONE;

public class DroneMenu extends AbstractBasicMenu {
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

    public boolean hasEmptyMap() {
        return getSlot(0).getItem().is(Items.MAP);
    }

    public void clickLaunch(BlockPos desination) {
        var shorts = new int[]{
                dataAccess.get(0),
                dataAccess.get(1),
                dataAccess.get(2),
                dataAccess.get(3),
                dataAccess.get(4),
                dataAccess.get(5),
                dataAccess.get(6),
                dataAccess.get(7)
        };
        long mostSigBits = ((long)(shorts[0] & 0xFFFF) << 48) |
                ((long)(shorts[1] & 0xFFFF) << 32) |
                ((long)(shorts[2] & 0xFFFF) << 16) |
                ((long)(shorts[3] & 0xFFFF));
        long leastSigBits = ((long)(shorts[4] & 0xFFFF) << 48) |
                ((long)(shorts[5] & 0xFFFF) << 32) |
                ((long)(shorts[6] & 0xFFFF) << 16) |
                ((long)(shorts[7] & 0xFFFF));
        Packets.SEND_DRONE.sendToServer(new SendDroneMessage(new UUID(mostSigBits, leastSigBits), desination));
    }

    public int getInitialX() {
        return dataAccess.get(8);
    }

    public int getInitialZ() {
        return dataAccess.get(9);
    }

    public boolean isBasic() {
        return dataAccess.get(11) == 0;
    }

    @Override
    public boolean stillValid(@NotNull Player player) {
        return dataAccess.get(10) == 0;
    }
}
