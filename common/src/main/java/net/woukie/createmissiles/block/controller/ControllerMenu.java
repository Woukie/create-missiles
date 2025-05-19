package net.woukie.createmissiles.block.controller;

import net.minecraft.core.BlockPos;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.woukie.createmissiles.block.InvisibleSlot;
import net.woukie.createmissiles.block.MissileAbstractMenu;
import net.woukie.createmissiles.block.controller.messages.ClickLaunchMessage;
import net.woukie.createmissiles.registry.MissileItems;
import net.woukie.createmissiles.registry.MissilePackets;

import static net.woukie.createmissiles.registry.MissileMenus.CONTROLLER;

public class ControllerMenu extends MissileAbstractMenu {
    Container schematicatorContainer;
    Container navigatorContainer;

    ContainerData controllerData;

    protected ControllerMenu(int id, Inventory playerInventory, Container container, ContainerData controllerData, Container schematicatorContainer, Container navigatorContainer) {
        super(CONTROLLER.get(), id, container);

        checkContainerDataCount(controllerData, 6);
        checkContainerSize(container, 128);
        checkContainerSize(navigatorContainer, 1);
        checkContainerSize(schematicatorContainer, 3);

        this.controllerData = controllerData;
        this.addDataSlots(controllerData);

        this.schematicatorContainer = schematicatorContainer;
        this.navigatorContainer = navigatorContainer;
        this.addSlot(new InvisibleSlot(navigatorContainer, 0));
        this.addSlot(new InvisibleSlot(schematicatorContainer, 0));
        this.addSlot(new InvisibleSlot(schematicatorContainer, 1));
        this.addSlot(new InvisibleSlot(schematicatorContainer, 2));

        for (int i = 0; i < 128; i++)
            this.addSlot(new InvisibleSlot(container, i));

        for(int j = 0; j < 3; ++j)
            for(int k = 0; k < 9; ++k)
                this.addSlot(new Slot(playerInventory, k + j * 9 + 9, 8 + k * 18, 84 + j * 18));

        for(int j = 0; j < 9; ++j)
            this.addSlot(new Slot(playerInventory, j, 8 + j * 18, 142));
    }

    public ControllerMenu(int id, Inventory inventory) {
        this(id, inventory, new SimpleContainer(128), new SimpleContainerData(6), new SimpleContainer(3), new SimpleContainer(1));
    }

    public void clickLaunch() {
        MissilePackets.CONTROLLER_CLICK_LAUNCH.sendToServer(new ClickLaunchMessage(getPosition()));
    }

    public BlockPos getPosition() {
        return new BlockPos(
                controllerData.get(0),
                controllerData.get(1),
                controllerData.get(2)
        );
    }

    public boolean launchPadExists() {
        return controllerData.get(3) == 1;
    }

    public boolean schematicatorExists() {
        return controllerData.get(4) == 1;
    }

    public boolean navigatorExists() {
        return controllerData.get(5) == 1;
    }

    public ItemStack getWarhead() {
        if (!schematicatorExists()) return null;
        ItemStack item = schematicatorContainer.getItem(0);
        if (!item.is(MissileItems.WARHEAD_SCHEMATIC.get()))
            return null;
        return item;
    }

    public ItemStack getChassis() {
        if (!schematicatorExists()) return null;
        ItemStack item = schematicatorContainer.getItem(1);
        if (!item.is(MissileItems.CHASSIS_SCHEMATIC.get()))
            return null;
        return item;
    }

    public ItemStack getThruster() {
        if (!schematicatorExists()) return null;
        ItemStack item = schematicatorContainer.getItem(2);
        if (!item.is(MissileItems.THRUSTER_SCHEMATIC.get()))
            return null;
        return item;
    }

    public boolean hasDestination() {
        if(!navigatorExists()) return false;
        ItemStack item = navigatorContainer.getItem(0);
//        Has mapCrosshair positions by default, and map data is null in rare situations (need the level to get map data anyway which is annoying to get here, don't want to offload this method to block entity)
        return item.is(Items.FILLED_MAP);
    }
}
