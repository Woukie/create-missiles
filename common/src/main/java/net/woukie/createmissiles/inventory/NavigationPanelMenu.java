package net.woukie.createmissiles.inventory;

import net.minecraft.core.BlockPos;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.level.saveddata.maps.MapDecoration;
import net.woukie.createmissiles.block.navigationpanel.messages.ClickFuelMessage;
import net.woukie.createmissiles.block.navigationpanel.messages.ClickMapMessage;
import net.woukie.createmissiles.registry.Items;
import net.woukie.createmissiles.registry.Packets;
import org.jetbrains.annotations.NotNull;

import static net.woukie.createmissiles.registry.Menus.NAVIGATION_PANEL;

public class NavigationPanelMenu extends AbstractBasicMenu {
    private final ContainerData containerData;
    private final Container assemblyPanelContainer;

    public NavigationPanelMenu(int id, Inventory playerInventory, Container container, ContainerData containerData, Container assemblyPanelContainer) {
        super(NAVIGATION_PANEL.get(), id, container);
        checkContainerSize(container, 1);
        checkContainerDataCount(containerData, 12);
        this.containerData = containerData;
        this.assemblyPanelContainer = assemblyPanelContainer;

        this.addSlot(new Slot(container, 0, 66, 35) {
            public boolean mayPlace(@NotNull ItemStack itemStack) {
                if (!itemStack.is(net.minecraft.world.item.Items.FILLED_MAP))
                    return false;

                var data = MapItem.getSavedData(itemStack, playerInventory.player.level());
                if (data == null) return false;

                for(MapDecoration mapDecoration : data.getDecorations())
                    if (mapDecoration.getType() == MapDecoration.Type.RED_X)
                        return false;
                return !data.isExplorationMap();
            }
        });

        checkContainerSize(assemblyPanelContainer, 3);
        this.addSlot(new InvisibleSlot(assemblyPanelContainer, 0));
        this.addSlot(new InvisibleSlot(assemblyPanelContainer, 1));
        this.addSlot(new InvisibleSlot(assemblyPanelContainer, 2));

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

    public NavigationPanelMenu(int id, Inventory inventory) {
        this(id, inventory, new SimpleContainer(1), new SimpleContainerData(12), new SimpleContainer(3));
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

    public boolean launchPadExists() {
        return containerData.get(10) == 1;
    }

    public boolean assemblyPanelAbsent() {
        return containerData.get(11) != 1;
    }

    public ItemStack getMap() {
        ItemStack item = getSlot(0).getItem();
        if (!item.is(net.minecraft.world.item.Items.FILLED_MAP))
            return null;
        return item;
    }

    public ItemStack getWarhead() {
        if (assemblyPanelAbsent()) return null;
        ItemStack item = assemblyPanelContainer.getItem(0);
        if (!item.is(Items.WARHEAD_ASSEMBLY.get()))
            return null;
        return item;
    }

    public ItemStack getChassis() {
        if (assemblyPanelAbsent()) return null;
        ItemStack item = assemblyPanelContainer.getItem(1);
        if(!item.is(Items.CHASSIS_ASSEMBLY.get()))
            return null;
        return item;
    }

    public ItemStack getThruster() {
        if (assemblyPanelAbsent()) return null;
        ItemStack item = assemblyPanelContainer.getItem(2);
        if (!item.is(Items.THRUSTER_ASSEMBLY.get()))
            return null;
        return item;
    }

    public void clickMap(double x, double z) {
        Packets.NAVIGATION_PANEL_CLICK_MAP.sendToServer(new ClickMapMessage(getSource(), x, z));
    }

    public void clickFuel(double fuelClickZ) {
        Packets.NAVIGATION_PANEL_CLICK_FUEL.sendToServer(new ClickFuelMessage(getSource(), fuelClickZ));
    }
}
