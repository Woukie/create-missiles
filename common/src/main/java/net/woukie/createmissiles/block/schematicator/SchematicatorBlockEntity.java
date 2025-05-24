package net.woukie.createmissiles.block.schematicator;

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
import net.woukie.createmissiles.block.MissileAbstractBlockEntity;
import net.woukie.createmissiles.item.schematic.ChassisSchematic;
import net.woukie.createmissiles.item.schematic.ThrusterSchematic;
import net.woukie.createmissiles.item.schematic.WarheadSchematic;
import net.woukie.createmissiles.missilemanager.parts.ChassisType;
import net.woukie.createmissiles.missilemanager.parts.ThrusterType;
import net.woukie.createmissiles.missilemanager.parts.WarheadType;
import net.woukie.createmissiles.registry.MissileItems;
import org.jetbrains.annotations.NotNull;

public class SchematicatorBlockEntity extends MissileAbstractBlockEntity {
    public SchematicatorBlockEntity(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);
        items = NonNullList.withSize(3, ItemStack.EMPTY);
    }

    public WarheadType getWarhead() {
        ItemStack item = getItem(0);
        if (!item.is(MissileItems.WARHEAD_SCHEMATIC.get()))
            return null;
        return WarheadSchematic.getWarhead(item);
    }

    public ChassisType getChassis() {
        ItemStack item = getItem(1);
        if (!item.is(MissileItems.CHASSIS_SCHEMATIC.get()))
            return null;
        return ChassisSchematic.getChassis(item);
    }

    public ThrusterType getThruster() {
        ItemStack item = getItem(2);
        if (!item.is(MissileItems.THRUSTER_SCHEMATIC.get()))
            return null;
        return ThrusterSchematic.getThruster(item);
    }

    @Override
    protected @NotNull Component getDefaultName() {
        return Component.translatable("container.createmissiles.schematicator");
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
