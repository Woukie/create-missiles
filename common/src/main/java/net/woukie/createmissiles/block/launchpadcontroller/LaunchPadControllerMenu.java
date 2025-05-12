package net.woukie.createmissiles.block.launchpadcontroller;

import io.netty.buffer.Unpooled;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.woukie.createmissiles.item.schematic.ChassisSchematic;
import net.woukie.createmissiles.item.schematic.ThrusterSchematic;
import net.woukie.createmissiles.item.schematic.WarheadSchematic;
import net.woukie.createmissiles.missilemanager.TrajectoryData;
import net.woukie.createmissiles.missilemanager.parts.Chassis;
import net.woukie.createmissiles.missilemanager.parts.PartRegistry;
import net.woukie.createmissiles.missilemanager.parts.Thruster;
import net.woukie.createmissiles.missilemanager.parts.Warhead;
import net.woukie.createmissiles.registry.MissileItems;
import net.woukie.createmissiles.registry.MissilePackets;
import org.jetbrains.annotations.NotNull;

import static net.woukie.createmissiles.block.launchpadcontroller.LaunchPadControllerBlockEntity.*;
import static net.woukie.createmissiles.registry.MissileMenus.LAUNCH_PAD_CONTROLLER;

public class LaunchPadControllerMenu extends AbstractContainerMenu {
    private final Container container;
    private final ContainerData containerData;

    public LaunchPadControllerMenu(int i, Inventory playerInventory, Container container, ContainerData containerData) {
        super(LAUNCH_PAD_CONTROLLER.get(), i);
        checkContainerSize(container, 4);
        checkContainerDataCount(containerData, 11);

        this.container = container;
        this.containerData = containerData;

        this.addSlot(new Slot(container, 0, 88, 61) {
            public boolean mayPlace(@NotNull ItemStack itemStack) {
                return itemStack.is(Items.FILLED_MAP);
            }
        });

        this.addSlot(new Slot(container, 1, 8, 14) {
            public boolean mayPlace(@NotNull ItemStack itemStack) {
                return itemStack.is(MissileItems.WARHEAD_SCHEMATIC.get());
            }
        });

        this.addSlot(new Slot(container, 2, 8, 32) {
            public boolean mayPlace(@NotNull ItemStack itemStack) {
                return itemStack.is(MissileItems.CHASSIS_SCHEMATIC.get());
            }
        });

        this.addSlot(new Slot(container, 3, 8, 50) {
            public boolean mayPlace(@NotNull ItemStack itemStack) {
                return itemStack.is(MissileItems.THRUSTER_SCHEMATIC.get());
            }
        });

        this.addDataSlots(this.containerData);

        for(int j = 0; j < 3; ++j) {
            for(int k = 0; k < 9; ++k) {
                this.addSlot(new Slot(playerInventory, k + j * 9 + 9, 8 + k * 18, 84 + j * 18));
            }
        }

        for(int j = 0; j < 9; ++j) {
            this.addSlot(new Slot(playerInventory, j, 8 + j * 18, 142));
        }
    }

    public LaunchPadControllerMenu(LaunchPadControllerMenu menu, Inventory inventory, Component component) {
        this(menu.containerId, inventory, new SimpleContainer(4), new SimpleContainerData(11));
    }

    public LaunchPadControllerMenu(int id, Inventory inventory) {
        this(id, inventory, new SimpleContainer(4), new SimpleContainerData(11));
    }

    public boolean armed() {
        int mapCrosshairX = getMapCrosshairX();
        int mapCrosshairZ = getMapCrosshairZ();
        if (mapCrosshairX < 0 || mapCrosshairZ < 0 || mapCrosshairX > 128 || mapCrosshairZ > 128)
            return false;

        Warhead warhead = getWarhead();
        Chassis chassis = getChassis();
        Thruster thruster = getThruster();
        ItemStack map = container.getItem(SLOT_MAP);

        return warhead != null && chassis != null && thruster != null && map.is(Items.FILLED_MAP);
    }

    public Warhead getWarhead() {
        ItemStack warhead = container.getItem(SLOT_WARHEAD);
        if (!warhead.is(MissileItems.WARHEAD_SCHEMATIC.get()))
            return null;
        return WarheadSchematic.getWarhead(warhead);
    }

    public Chassis getChassis() {
        ItemStack chassis = container.getItem(SLOT_CHASSIS);
        if (!chassis.is(MissileItems.CHASSIS_SCHEMATIC.get()))
            return null;
        return ChassisSchematic.getChassis(chassis);
    }

    public Thruster getThruster() {
        ItemStack thruster = container.getItem(SLOT_THRUSTER);
        if (!thruster.is(MissileItems.THRUSTER_SCHEMATIC.get()))
            return null;
        return ThrusterSchematic.getThruster(thruster);
    }

    public int getMapCenterX() {
        return containerData.get(0);
    }

    public int getMapCenterZ() {
        return containerData.get(1);
    }

    public BlockPos getPos() {
        return new BlockPos(containerData.get(2), containerData.get(3), containerData.get(4));
    }

    public BlockPos getImpactPos() {
        return new BlockPos(containerData.get(5), containerData.get(6), containerData.get(7));
    }

    public float getFuelPercentage() {
        return (float)containerData.get(8) / 100F;
    }

    public int getMapCrosshairX() {
        return containerData.get(9);
    }

    public int getMapCrosshairZ() {
        return containerData.get(10);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        Slot clickedSlot = getSlot(index);
        if (!clickedSlot.hasItem())
            return ItemStack.EMPTY;

        ItemStack stack = clickedSlot.getItem();
        int size = container.getContainerSize();
        boolean success;
        if (index < size) {
            success = !moveItemStackTo(stack, size, slots.size(), false);
        } else
            success = !moveItemStackTo(stack, 0, size, false);

        return success ? ItemStack.EMPTY : stack;
    }

    @Override
    public boolean stillValid(Player player) {
        return this.container.stillValid(player);
    }

    public void clickLaunch() {
        if (armed()) {
            MissilePackets.CONTROLLER_LAUNCH.sendToServer(new ControllerLaunchMessage(getPos()));
        }
    }

    public void clickMap(double mapCrosshairX, double mapCrosshairZ) {
        MissilePackets.SET_CONTROLLER_TARGET.sendToServer(new SetControllerTargetMessage(getPos(), mapCrosshairX, mapCrosshairZ));
    }
}
