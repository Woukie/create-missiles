package net.woukie.createmissiles.block.launchpadcontroller;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class LaunchPadControllerBlockEntity extends BaseContainerBlockEntity implements WorldlyContainer {
    protected static final int SLOT_ROCKET = 0;
    protected static final int SLOT_MAP = 1;
    private static final int[] SLOTS_FOR_UP = new int[]{0};
    private static final int[] SLOTS_FOR_DOWN = new int[]{1, 0};
    private static final int[] SLOTS_FOR_SIDES = new int[]{1};

    protected NonNullList<ItemStack> items;
    int targetX;
    int targetZ;
    private final ContainerData dataAccess;

    boolean initialized;

    public LaunchPadControllerBlockEntity(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);

        this.targetX = -1;
        this.targetZ = -1;

        this.items = NonNullList.withSize(2, ItemStack.EMPTY);
        this.dataAccess = new ContainerData() {
            public int get(int i) {
                switch (i) {
                    case 0 -> {
                        return LaunchPadControllerBlockEntity.this.targetX;
                    }
                    case 1 -> {
                        return LaunchPadControllerBlockEntity.this.targetZ;
                    }
                    case 2 -> {
                        return blockPos.getX();
                    }
                    case 3 -> {
                        return blockPos.getY();
                    }
                    case 4 -> {
                        return blockPos.getZ();
                    }
                    default -> {
                        return 0;
                    }
                }
            }

            public void set(int i, int j) {
                switch (i) {
                    case 0 -> LaunchPadControllerBlockEntity.this.targetX = j;
                    case 1 -> LaunchPadControllerBlockEntity.this.targetZ = j;
                }
            }

            public int getCount() {
                return 5;
            }
        };
    }

    public void updateTarget(int targetX, int targetZ) {
        this.targetX = targetX;
        this.targetZ = targetZ;
    }

    public void tick() {
        if (!initialized && hasLevel()) {
            initialized = true;
            ControllerInstanceManager.add(this);
        }
    }

    public void launch() {
        ItemStack map = items.get(SLOT_MAP);

        if (map.is(Items.FILLED_MAP)) {
            Integer mapId = MapItem.getMapId(map);
            MapItemSavedData mapData = MapItem.getSavedData(mapId, this.getLevel());

            if (mapData != null) {
                items.set(0, new ItemStack(ItemStack.EMPTY.getItem()));

                int multiplier = 1 << mapData.scale;

                int blockX = multiplier * targetX;
                int blockZ = multiplier * targetZ;

                blockX = mapData.centerX - 64 * multiplier + blockX;
                blockZ = mapData.centerZ - 64 * multiplier + blockZ;

                int scan = level.getMaxBuildHeight();
                BlockPos impactPos = new BlockPos(blockX, scan, blockZ);
                while (scan >= level.getMinBuildHeight()) {
                    impactPos = new BlockPos(blockX, scan, blockZ);
                    if (!level.getBlockState(impactPos).isAir())
                        break;
                    scan--;
                }

                level.explode(null, impactPos.getX(), impactPos.getY(), impactPos.getZ(), 100, Level.ExplosionInteraction.BLOCK);
            }
        }
    }

    @Override
    public void setRemoved() {
        ControllerInstanceManager.remove(this);
    }

    @Override
    protected @NotNull Component getDefaultName() {
        return Component.translatable("container.createmissiles.launch_pad_controller");
    }

    @Override
    protected @NotNull AbstractContainerMenu createMenu(int id, @NotNull Inventory inv) {
        return new LaunchPadControllerMenu(id, inv, this, this.dataAccess);
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

    @Override
    public void load(@NotNull CompoundTag compoundTag) {
        super.load(compoundTag);

        this.items = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        ContainerHelper.loadAllItems(compoundTag, this.items);

        this.targetX = compoundTag.getInt("TargetX");
        this.targetZ = compoundTag.getInt("TargetZ");
    }

    @Override
    protected void saveAdditional(CompoundTag compoundTag) {
        super.saveAdditional(compoundTag);
        compoundTag.putInt("TargetX", this.targetX);
        compoundTag.putInt("TargetZ", this.targetZ);
        ContainerHelper.saveAllItems(compoundTag, this.items);
    }

    @Override
    public ItemStack getItem(int i) {
        return i >= 0 && i < this.items.size() ? this.items.get(i) : ItemStack.EMPTY;
    }

    @Override
    public ItemStack removeItem(int i, int j) {
        return ContainerHelper.removeItem(this.items, i, j);
    }

    @Override
    public ItemStack removeItemNoUpdate(int i) {
        return ContainerHelper.takeItem(this.items, i);
    }

    @Override
    public void setItem(int i, ItemStack itemStack) {
        if (i >= 0 && i < this.items.size()) {
            this.items.set(i, itemStack);
        }
    }

    @Override
    public boolean canPlaceItem(int i, ItemStack itemStack) {
        if (i == SLOT_ROCKET) {
            return itemStack.is(Items.FIREWORK_ROCKET);
        } else if (i == SLOT_MAP) {
            return itemStack.is(Items.FILLED_MAP);
        }

        return false;
    }

    @Override
    public boolean stillValid(Player player) {
        return Container.stillValidBlockEntity(this, player);
    }

    @Override
    public int[] getSlotsForFace(Direction direction) {
        return switch (direction) {
            case UP -> SLOTS_FOR_UP;
            case DOWN -> SLOTS_FOR_DOWN;
            default -> SLOTS_FOR_SIDES;
        };
    }

    @Override
    public boolean canPlaceItemThroughFace(int i, ItemStack itemStack, @Nullable Direction direction) {
        return this.canPlaceItem(i, itemStack);
    }

    @Override
    public boolean canTakeItemThroughFace(int i, ItemStack itemStack, Direction direction) {
        return true;
    }

    @Override
    public void clearContent() {
        this.items.clear();
    }
}
