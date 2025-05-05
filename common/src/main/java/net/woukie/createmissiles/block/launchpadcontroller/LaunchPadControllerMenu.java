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
import net.woukie.createmissiles.registry.MissileMenus;
import net.woukie.createmissiles.registry.MissilePackets;
import org.jetbrains.annotations.NotNull;

public class LaunchPadControllerMenu extends AbstractContainerMenu {
    private final Container container;
    private final ContainerData containerData;

    @Environment(EnvType.CLIENT)
    public LaunchPadControllerMenu(MenuType<?> type, int i, Inventory inventory) {
        this(i, inventory, new SimpleContainer(2), new SimpleContainerData(5));
    }

    public LaunchPadControllerMenu(int i, Inventory playerInventory, Container container, ContainerData containerData) {
        super(MissileMenus.LAUNCH_PAD_CONTROLLER_MENU.get(), i);
        checkContainerSize(container, 2);
        checkContainerDataCount(containerData, 5);

        this.container = container;
        this.containerData = containerData;

        this.addSlot(new Slot(container, 1, 14, 21) {
            public boolean mayPlace(@NotNull ItemStack itemStack) {
                return itemStack.is(Items.FILLED_MAP);
            }
        });

        this.addSlot(new Slot(container, 0, 14, 55) {
            public boolean mayPlace(@NotNull ItemStack itemStack) {
                return itemStack.is(Items.FIREWORK_ROCKET);
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
            success = !moveItemStackTo(stack, 0, size - 1, false);

        return success ? ItemStack.EMPTY : stack;
    }

    @Override
    public boolean stillValid(Player player) {
        return this.container.stillValid(player);
    }

    public void clickMap(int targetX, int targetZ) {
        int x = this.containerData.get(2);
        int y = this.containerData.get(3);
        int z = this.containerData.get(4);
        BlockPos pos = new BlockPos(x, y, z);

        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
        buf.writeBlockPos(pos);
        buf.writeInt(targetX);
        buf.writeInt(targetZ);

        MissilePackets.SET_CONTROLLER_TARGET.sendToServer(new SetControllerTargetMessage(pos, targetX, targetZ));
    }
}
