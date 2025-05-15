package net.woukie.createmissiles.block.controller;

import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.woukie.createmissiles.block.MissileAbstractBlockEntity;
import net.woukie.createmissiles.block.navigator.NavigatorInstanceTracker;
import org.jetbrains.annotations.NotNull;

public class ControllerBlockEntity extends MissileAbstractBlockEntity {
    private boolean initialized;

    private final ContainerData dataAccess;

    public ControllerBlockEntity(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);
        this.items = NonNullList.withSize(128, ItemStack.EMPTY);
        this.dataAccess = new ContainerData() {
            @Override
            public int get(int i) {
                return switch (i) {
                    case 0 -> getBlockPos().getX();
                    case 1 -> getBlockPos().getY();
                    case 2 -> getBlockPos().getZ();
                    default -> 0;
                };
            }

            @Override
            public void set(int i, int j) {}

            @Override
            public int getCount() {
                return 3;
            }
        };
    }

    public void serverTick() {
        if (!initialized && hasLevel()) {
            initialized = true;
            ControllerInstanceTracker.add(this);
        }
    }

    public void clickLaunch() {
        System.out.println("clickLaunch triggered on server!");
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
        ControllerInstanceTracker.remove(this);
    }

    @Override
    public void load(@NotNull CompoundTag compoundTag) {
        super.load(compoundTag);
        this.items = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        ContainerHelper.loadAllItems(compoundTag, this.items);
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag compoundTag) {
        super.saveAdditional(compoundTag);
        ContainerHelper.saveAllItems(compoundTag, this.items);
    }

    @Override
    protected @NotNull Component getDefaultName() {
        return Component.translatable("container.createmissiles.controller");
    }

    @Override
    protected @NotNull AbstractContainerMenu createMenu(int id, @NotNull Inventory playerInventory) {
        return new ControllerMenu(id, playerInventory, this, dataAccess);
    }
}
