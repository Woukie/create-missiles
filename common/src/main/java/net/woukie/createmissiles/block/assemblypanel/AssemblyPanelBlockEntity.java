package net.woukie.createmissiles.block.assemblypanel;

import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.woukie.createmissiles.block.entity.AbstractBasicBlockEntity;
import net.woukie.createmissiles.inventory.AssemblyPanelMenu;
import net.woukie.createmissiles.registry.Items;
import org.jetbrains.annotations.NotNull;

public class AssemblyPanelBlockEntity extends AbstractBasicBlockEntity {
    public AssemblyPanelBlockEntity(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);
        items = NonNullList.withSize(3, ItemStack.EMPTY);
    }

    @Override
    protected @NotNull Component getDefaultName() {
        return Component.translatable("container.createmissiles.assembly_panel");
    }

    @Override
    protected @NotNull AbstractContainerMenu createMenu(int id, @NotNull Inventory playerInventory) {
        return new AssemblyPanelMenu(id, playerInventory, this);
    }

    @Override
    public int getMaxStackSize() {
        return 1;
    }

    @Override
    public boolean canPlaceItem(int i, @NotNull ItemStack itemStack) {
        if (i == 0) {
            return itemStack.is(Items.WARHEAD_ASSEMBLY.get());
        } else if (i == 1) {
            return itemStack.is(Items.CHASSIS_ASSEMBLY.get());
        } else if (i == 2) {
            return itemStack.is(Items.THRUSTER_ASSEMBLY.get());
        }

        return false;
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
}
