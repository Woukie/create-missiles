package net.woukie.createmissiles.block.launchpadcontroller;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class LaunchPadControllerBlockEntity extends BaseContainerBlockEntity {
    protected static final int SLOT_ROCKET = 0;
    protected static final int SLOT_MAP = 1;

    private static final int[] SLOTS_FOR_UP = new int[]{0};
    private static final int[] SLOTS_FOR_DOWN = new int[]{1, 0};
    private static final int[] SLOTS_FOR_SIDES = new int[]{1};

    private NonNullList<ItemStack> items;

    public LaunchPadControllerBlockEntity(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);
        this.items = NonNullList.withSize(5, ItemStack.EMPTY);
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("container.createmissiles.launch_pad_controller");
    }

    @Override
    protected AbstractContainerMenu createMenu(int i, Inventory inventory) {
        return new LaunchPadControllerMenu(i, inventory, this);
    }

    @Override
    public int getContainerSize() {
        return this.items.size();
    }

    @Override
    public boolean isEmpty() {
        for(ItemStack itemStack : this.items) {
            if (!itemStack.isEmpty()) {
                return false;
            }
        }

        return true;
    }

    public void load(CompoundTag compoundTag) {
        super.load(compoundTag);
        this.items = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        ContainerHelper.loadAllItems(compoundTag, this.items);
    }

    protected void saveAdditional(CompoundTag compoundTag) {
        super.saveAdditional(compoundTag);
        ContainerHelper.saveAllItems(compoundTag, this.items);
    }

    public ItemStack getItem(int i) {
        return i >= 0 && i < this.items.size() ? this.items.get(i) : ItemStack.EMPTY;
    }

    public ItemStack removeItem(int i, int j) {
        return ContainerHelper.removeItem(this.items, i, j);
    }

    public ItemStack removeItemNoUpdate(int i) {
        return ContainerHelper.takeItem(this.items, i);
    }

    public void setItem(int i, ItemStack itemStack) {
        if (i >= 0 && i < this.items.size()) {
            this.items.set(i, itemStack);
        }
    }

    public boolean canPlaceItem(int i, ItemStack itemStack) {
        if (i == SLOT_ROCKET) {
            return PotionBrewing.isIngredient(itemStack);
        } else if (i == SLOT_MAP) {
            return itemStack.is(Items.FILLED_MAP);
        }

        return false;
    }

    public boolean stillValid(Player player) {
        return Container.stillValidBlockEntity(this, player);
    }

    public int[] getSlotsForFace(Direction direction) {
        return switch (direction) {
            case UP -> SLOTS_FOR_UP;
            case DOWN -> SLOTS_FOR_DOWN;
            default -> SLOTS_FOR_SIDES;
        };
    }

    public boolean canPlaceItemThroughFace(int i, ItemStack itemStack, @Nullable Direction direction) {
        return this.canPlaceItem(i, itemStack);
    }

    public boolean canTakeItemThroughFace(int i, ItemStack itemStack, Direction direction) {
        return true;
    }

    public void clearContent() {
        this.items.clear();
    }
}
