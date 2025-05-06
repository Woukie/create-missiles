package net.woukie.createmissiles.block.launchpadcontroller;

import io.netty.buffer.Unpooled;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.woukie.createmissiles.registry.MissileItems;
import net.woukie.createmissiles.registry.MissileMenus;
import net.woukie.createmissiles.registry.MissilePackets;
import org.jetbrains.annotations.NotNull;

public class LaunchPadControllerMenu extends AbstractContainerMenu {
    private final Container container;
    private final ContainerData containerData;

    @Environment(EnvType.CLIENT)
    public LaunchPadControllerMenu(MenuType<?> type, int i, Inventory inventory) {
        this(i, inventory, new SimpleContainer(4), new SimpleContainerData(5));
    }

    public LaunchPadControllerMenu(int i, Inventory playerInventory, Container container, ContainerData containerData) {
        super(MissileMenus.LAUNCH_PAD_CONTROLLER_MENU.get(), i);
        checkContainerSize(container, 4);
        checkContainerDataCount(containerData, 5);

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

    public boolean armed() {
        int targetX = getTargetZ();
        int targetZ = getTargetX();
        if (targetX < 0 || targetZ < 0 || targetX > 128 || targetZ > 128)
            return false;

        if (container.getItem(0).isEmpty() || container.getItem(1).isEmpty() || container.getItem(2).isEmpty() || container.getItem(3).isEmpty())
            return false;

        ItemStack map = getSlot(0).getItem();
        ItemStack warhead = getSlot(1).getItem();
        ItemStack chassis = getSlot(2).getItem();
        ItemStack thruster = getSlot(3).getItem();
        return map.is(Items.FILLED_MAP) &&
                warhead.is(MissileItems.WARHEAD_SCHEMATIC.get()) &&
                chassis.is(MissileItems.CHASSIS_SCHEMATIC.get()) &&
                thruster.is(MissileItems.THRUSTER_SCHEMATIC.get());
    }

    public int getTargetX() {
        return containerData.get(0);
    }

    public int getTargetZ() {
        return containerData.get(1);
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
            BlockPos pos = getPosition();

            FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
            buf.writeBlockPos(pos);

            MissilePackets.CONTROLLER_LAUNCH.sendToServer(new ControllerLaunchMessage(pos));
        }
    }

    public void clickMap(int targetX, int targetZ) {
        BlockPos pos = getPosition();

        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
        buf.writeBlockPos(pos);
        buf.writeInt(targetX);
        buf.writeInt(targetZ);

        MissilePackets.SET_CONTROLLER_TARGET.sendToServer(new SetControllerTargetMessage(pos, targetX, targetZ));
    }

    private BlockPos getPosition() {
        int x = this.containerData.get(2);
        int y = this.containerData.get(3);
        int z = this.containerData.get(4);
        return new BlockPos(x, y, z);
    }
}
