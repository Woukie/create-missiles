package net.woukie.createmissiles.block.controller;

import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.woukie.createmissiles.block.MissileAbstractMenu;

import static net.woukie.createmissiles.registry.MissileMenus.CONTROLLER;

public class ControllerMenu extends MissileAbstractMenu {
    protected ControllerMenu(int id, Inventory playerInventory, Container container) {
        super(CONTROLLER.get(), id, container);
    }

    public ControllerMenu(int id, Inventory inventory) {
        this(id, inventory, new SimpleContainer(128));
    }
}
