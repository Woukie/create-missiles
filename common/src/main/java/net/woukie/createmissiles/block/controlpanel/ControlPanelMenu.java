package net.woukie.createmissiles.block.controlpanel;

import net.minecraft.core.BlockPos;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.woukie.createmissiles.block.InvisibleSlot;
import net.woukie.createmissiles.block.MissileAbstractMenu;
import net.woukie.createmissiles.block.controlpanel.messages.ClickLaunchMessage;
import net.woukie.createmissiles.registry.Items;
import net.woukie.createmissiles.registry.Packets;

import static net.woukie.createmissiles.registry.Menus.CONTROL_PANEL;

public class ControlPanelMenu extends MissileAbstractMenu {
    Container assemblyPanelContainer;
    Container navigatorContainer;

    ContainerData controlPanelData;

    protected ControlPanelMenu(int id, Inventory playerInventory, Container container, ContainerData controlPanelData, Container assemblyPanelContainer, Container navigatorContainer) {
        super(CONTROL_PANEL.get(), id, container);

        checkContainerDataCount(controlPanelData, 7);
        checkContainerSize(container, 96);
        checkContainerSize(navigatorContainer, 1);
        checkContainerSize(assemblyPanelContainer, 3);

        this.controlPanelData = controlPanelData;
        this.addDataSlots(controlPanelData);

        this.assemblyPanelContainer = assemblyPanelContainer;
        this.navigatorContainer = navigatorContainer;
        this.addSlot(new InvisibleSlot(navigatorContainer, 0));
        this.addSlot(new InvisibleSlot(assemblyPanelContainer, 0));
        this.addSlot(new InvisibleSlot(assemblyPanelContainer, 1));
        this.addSlot(new InvisibleSlot(assemblyPanelContainer, 2));

        for (int i = 0; i < 96; i++)
            this.addSlot(new InvisibleSlot(container, i));

        for(int j = 0; j < 3; ++j)
            for(int k = 0; k < 9; ++k)
                this.addSlot(new Slot(playerInventory, k + j * 9 + 9, 8 + k * 18, 84 + j * 18));

        for(int j = 0; j < 9; ++j)
            this.addSlot(new Slot(playerInventory, j, 8 + j * 18, 142));
    }

    public ControlPanelMenu(int id, Inventory inventory) {
        this(id, inventory, new SimpleContainer(96), new SimpleContainerData(7), new SimpleContainer(3), new SimpleContainer(1));
    }

    public void clickLaunch() {
        Packets.CONTROL_PANEL_CLICK_LAUNCH.sendToServer(new ClickLaunchMessage(getPosition()));
    }

    public BlockPos getPosition() {
        return new BlockPos(
                controlPanelData.get(0),
                controlPanelData.get(1),
                controlPanelData.get(2)
        );
    }

    public boolean launchPadExists() {
        return controlPanelData.get(3) == 1;
    }

    public boolean assemblyPanelExists() {
        return controlPanelData.get(4) == 1;
    }

    public boolean navigatorExists() {
        return controlPanelData.get(5) == 1;
    }

    public boolean launchPadPowered() {
        return controlPanelData.get(6) != 0;
    }

    public ItemStack getWarhead() {
        if (!assemblyPanelExists()) return null;
        ItemStack item = assemblyPanelContainer.getItem(0);
        if (!item.is(Items.WARHEAD_ASSEMBLY.get()))
            return null;
        return item;
    }

    public ItemStack getChassis() {
        if (!assemblyPanelExists()) return null;
        ItemStack item = assemblyPanelContainer.getItem(1);
        if (!item.is(Items.CHASSIS_ASSEMBLY.get()))
            return null;
        return item;
    }

    public ItemStack getThruster() {
        if (!assemblyPanelExists()) return null;
        ItemStack item = assemblyPanelContainer.getItem(2);
        if (!item.is(Items.THRUSTER_ASSEMBLY.get()))
            return null;
        return item;
    }

    public boolean hasDestination() {
        if(!navigatorExists()) return false;
        ItemStack item = navigatorContainer.getItem(0);
//        Has mapCrosshair positions by default, and map data is null in rare situations (need the level to get map data anyway which is annoying to get here, don't want to offload this method to block entity)
        return item.is(net.minecraft.world.item.Items.FILLED_MAP);
    }
}
