package net.woukie.createmissiles.block.navigator;

import net.minecraft.core.BlockPos;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.woukie.createmissiles.block.MissileAbstractMenu;

import static net.woukie.createmissiles.registry.MissileMenus.NAVIGATOR;

public class NavigatorMenu extends MissileAbstractMenu {
    private final ContainerData containerData;
    private final Container schematicatorContainer;

    protected NavigatorMenu(int id, Inventory playerInventory, Container container, ContainerData containerData, Container schematicatorContainer) {
        super(NAVIGATOR.get(), id, container);
        this.schematicatorContainer = schematicatorContainer;
        this.containerData = containerData;

        for(int j = 0; j < 3; ++j) {
            for(int k = 0; k < 9; ++k) {
                this.addSlot(new Slot(playerInventory, k + j * 9 + 9, 8 + k * 18, 84 + j * 18));
            }
        }

        for(int j = 0; j < 9; ++j) {
            this.addSlot(new Slot(playerInventory, j, 8 + j * 18, 142));
        }

        checkContainerSize(container, 1);
        checkContainerDataCount(containerData, 10);
        if (schematicatorContainer != null)
            checkContainerSize(schematicatorContainer, 3);
    }

    public NavigatorMenu(int id, Inventory inventory) {
        this(id, inventory, new SimpleContainer(1), new SimpleContainerData(10), new SimpleContainer(3));
    }

    public int getMapCrosshairX() {
        return containerData.get(0);
    }

    public int getMapCrosshairZ() {
        return containerData.get(1);
    }

    public BlockPos getTarget() {
        if (containerData.get(2) == 0) {
            return null;
        }

        return new BlockPos(
                containerData.get(3),
                containerData.get(4),
                containerData.get(5)
        );
    }

    public BlockPos getSource() {
        return new BlockPos(
                containerData.get(6),
                containerData.get(7),
                containerData.get(8)
        );
    }

    public double getFuelPercent() {
        return ((double) containerData.get(9)) / 100.0D;
    }

    public ItemStack getMap() {
        return getSlot(0).getItem();
    }

    public Container getSchematicatorContainer() {
        return schematicatorContainer;
    }
}
