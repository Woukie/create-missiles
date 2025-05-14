package net.woukie.createmissiles.block.schematicator;

import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.woukie.createmissiles.block.MissileAbstractBlockEntity;
import net.woukie.createmissiles.registry.MissileItems;
import org.jetbrains.annotations.NotNull;

public class SchematicatorBlockEntity extends MissileAbstractBlockEntity {
    public SchematicatorBlockEntity(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);
        this.items = NonNullList.withSize(3, ItemStack.EMPTY);
    }

    @Override
    protected @NotNull Component getDefaultName() {
        return Component.translatable("container.createmissiles.launch_pad_controller");
    }

    @Override
    protected @NotNull AbstractContainerMenu createMenu(int id, @NotNull Inventory playerInventory) {
        return new SchematicatorMenu(id, playerInventory, this);
    }

    @Override
    public boolean canPlaceItem(int i, @NotNull ItemStack itemStack) {
        if (i == 0) {
            return itemStack.is(MissileItems.WARHEAD_SCHEMATIC.get());
        } else if (i == 1) {
            return itemStack.is(MissileItems.CHASSIS_SCHEMATIC.get());
        } else if (i == 2) {
            return itemStack.is(MissileItems.THRUSTER_SCHEMATIC.get());
        }

        return false;
    }
}
