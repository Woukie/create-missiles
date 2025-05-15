package net.woukie.createmissiles.block.controller;

import net.minecraft.core.BlockPos;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.woukie.createmissiles.block.MissileAbstractMenu;
import net.woukie.createmissiles.block.controller.messages.ClickLaunchMessage;
import net.woukie.createmissiles.registry.MissilePackets;

import static net.woukie.createmissiles.registry.MissileMenus.CONTROLLER;

public class ControllerMenu extends MissileAbstractMenu {
    ContainerData containerData;

    protected ControllerMenu(int id, Inventory playerInventory, Container container, ContainerData containerData) {
        super(CONTROLLER.get(), id, container);
        this.containerData = containerData;

        checkContainerDataCount(containerData, 3);
        checkContainerSize(container, 128);
    }

    public ControllerMenu(int id, Inventory inventory) {
        this(id, inventory, new SimpleContainer(128), new SimpleContainerData(3));
    }

    private BlockPos getPosition() {
        return new BlockPos(
                containerData.get(0),
                containerData.get(1),
                containerData.get(2)
        );
    }

    public void clickLaunch() {
        MissilePackets.CONTROLLER_CLICK_LAUNCH.sendToServer(new ClickLaunchMessage(getPosition()));
    }
}
