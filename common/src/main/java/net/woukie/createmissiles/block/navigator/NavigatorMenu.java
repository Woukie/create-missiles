package net.woukie.createmissiles.block.navigator;

import net.minecraft.core.BlockPos;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.woukie.createmissiles.block.MissileAbstractMenu;
import net.woukie.createmissiles.block.navigator.messages.ClickFuelMessage;
import net.woukie.createmissiles.block.navigator.messages.ClickMapMessage;
import net.woukie.createmissiles.registry.MissileItems;
import net.woukie.createmissiles.registry.MissilePackets;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;

import static net.woukie.createmissiles.registry.MissileMenus.NAVIGATOR;

public class NavigatorMenu extends MissileAbstractMenu {
    private final ContainerData containerData;
    private final Container schematicatorContainer;

    public NavigatorMenu(int id, Inventory playerInventory, Container container, ContainerData containerData, Container schematicatorContainer) {
        super(NAVIGATOR.get(), id, container);
        checkContainerSize(container, 1);
        checkContainerDataCount(containerData, 10);
        this.schematicatorContainer = schematicatorContainer;
        this.containerData = containerData;

        this.addSlot(new Slot(container, 0, 66, 35) {
            public boolean mayPlace(@NotNull ItemStack itemStack) {
                return itemStack.is(Items.FILLED_MAP);
            }
        });

        if (schematicatorContainer != null) {
            checkContainerSize(schematicatorContainer, 3);

            this.addSlot(new InvisibleSlot(schematicatorContainer, 0));
            this.addSlot(new InvisibleSlot(schematicatorContainer, 1));
            this.addSlot(new InvisibleSlot(schematicatorContainer, 2));
        }

        for(int j = 0; j < 3; ++j) {
            for(int k = 0; k < 9; ++k) {
                this.addSlot(new Slot(playerInventory, k + j * 9 + 9, 8 + k * 18, 84 + j * 18));
            }
        }

        for(int j = 0; j < 9; ++j) {
            this.addSlot(new Slot(playerInventory, j, 8 + j * 18, 142));
        }

        this.addDataSlots(containerData);
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
        return containerData.get(9) / 100.0;
    }

    public ItemStack getMap() {
        ItemStack item = getSlot(0).getItem();
        if (!item.is(Items.FILLED_MAP))
            return null;
        return item;
    }

    public ItemStack getWarhead() {
        ItemStack item = getSlot(1).getItem();
        if (!item.is(MissileItems.WARHEAD_SCHEMATIC.get()))
            return null;
        return item;
    }

    public ItemStack getChassis() {
        ItemStack item = getSlot(2).getItem();
        if(!item.is(MissileItems.CHASSIS_SCHEMATIC.get()))
            return null;
        return item;
    }

    public ItemStack getThruster() {
        ItemStack item = getSlot(3).getItem();
        if (!item.is(MissileItems.THRUSTER_SCHEMATIC.get()))
            return null;
        return item;
    }

    public Container getSchematicatorContainer() {
        return schematicatorContainer;
    }

    public void clickMap(double x, double z) {
        MissilePackets.NAVIGATOR_CLICK_MAP.sendToServer(new ClickMapMessage(getSource(), x, z));
    }

    public void clickFuel(double fuelClickZ) {
        MissilePackets.NAVIGATOR_CLICK_FUEL.sendToServer(new ClickFuelMessage(getSource(), fuelClickZ));
    }

    private static class InvisibleSlot extends Slot {
        public InvisibleSlot(Container container, int i) {
            super(container, i, 0, 0);
        }

        public boolean mayPlace(@NotNull ItemStack itemStack) {
            return false;
        }

        public boolean isActive() {
            return false;
        }

        public boolean mayPickup(@NotNull Player player) {
            return false;
        }

        public boolean isHighlightable() {
            return false;
        }
    }
}
