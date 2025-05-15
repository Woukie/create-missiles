package net.woukie.createmissiles.block.navigator;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import net.woukie.createmissiles.block.MissileAbstractBlockEntity;
import net.woukie.createmissiles.block.launchpad.LaunchPadBlockEntity;
import net.woukie.createmissiles.block.schematicator.SchematicatorBlock;
import net.woukie.createmissiles.block.schematicator.SchematicatorBlockEntity;
import org.jetbrains.annotations.NotNull;

public class NavigatorBlockEntity extends MissileAbstractBlockEntity {
    public static final int SLOT_MAP = 0;

    private double mapCrosshairX, mapCrosshairZ, fuelPercent;
    private boolean initialized;
    private BlockPos target;

    private final ContainerData dataAccess;

    public NavigatorBlockEntity(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);
        this.items = NonNullList.withSize(1, ItemStack.EMPTY);
        this.dataAccess = new ContainerData() {
            @Override
            public int get(int i) {
                return switch (i) {
                    case 0 -> (int) mapCrosshairX;
                    case 1 -> (int) mapCrosshairZ;
                    case 2 -> target != null ? 1 : 0;
                    case 3 -> target.getX();
                    case 4 -> target.getY();
                    case 5 -> target.getZ();
                    case 6 -> getBlockPos().getX();
                    case 7 -> getBlockPos().getY();
                    case 8 -> getBlockPos().getZ();
                    case 9 -> (int) (fuelPercent * 100);
                    default -> 0;
                };
            }

            @Override
            public void set(int i, int j) {}

            @Override
            public int getCount() {
                return 10;
            }
        };
    }

    public void serverTick() {
        if (!initialized && hasLevel()) {
            initialized = true;
            NavigatorInstanceTracker.add(this);
        }
    }

    public void fuelClicked(double fuelPercent) {
        this.fuelPercent = fuelPercent;
    }

    public void mapClicked(double mapCrosshairX, double mapCrosshairZ) {
        this.mapCrosshairX = mapCrosshairX;
        this.mapCrosshairZ = mapCrosshairZ;
        recalculateTarget();
    }

    private void recalculateTarget() {
        ItemStack mapItem = getItem(SLOT_MAP);
        if (!mapItem.is(Items.FILLED_MAP) || level == null) {
            target = null;
            return;
        }

        MapItemSavedData mapData = MapItem.getSavedData(mapItem, level);
        if (mapData == null) {
            target = null;
            return;
        }

        int multiplier = 1 << mapData.scale;
        int blockX = (int)(mapData.centerX - 64 * multiplier + (multiplier * mapCrosshairX));
        int blockZ = (int)(mapData.centerZ - 64 * multiplier + (multiplier * mapCrosshairZ));

        int scan = level.getMaxBuildHeight();
        BlockPos impactPos = new BlockPos(blockX, scan, blockZ);
        while (scan >= level.getMinBuildHeight()) {
            impactPos = new BlockPos(blockX, scan, blockZ);
            if (!level.getBlockState(impactPos).isAir())
                break;
            scan--;
        }

        target = impactPos;
    }

    private Container findSchematicator() {
        Level world = getLevel();
        if (world == null)
            return null;

        Direction facing = getBlockState().getValue(SchematicatorBlock.FACING);
        Direction right = facing.getClockWise();

        for (int offset = 0; offset < 3; offset++) {
            BlockPos corner = getBlockPos().relative(facing).relative(right, offset);

            if (checkForLaunchPad(corner, right, facing)) {
                for (int i = 0; i < 3; i++) {
                    BlockPos leftEdge = corner.relative(right, -1).relative(facing, i);
                    if (world.getBlockEntity(leftEdge) instanceof SchematicatorBlockEntity schematicator)
                        return schematicator;

                    BlockPos farEdge = corner.relative(facing, 3).relative(right, i);
                    if (world.getBlockEntity(farEdge) instanceof SchematicatorBlockEntity schematicator)
                        return schematicator;

                    BlockPos backEdge = corner.relative(facing, -1).relative(right, i);
                    if (world.getBlockEntity(backEdge) instanceof SchematicatorBlockEntity schematicator)
                        return schematicator;

                    BlockPos rightEdge = corner.relative(right, 3).relative(facing, i);
                    if (world.getBlockEntity(rightEdge) instanceof SchematicatorBlockEntity schematicator)
                        return schematicator;
                }

                return null;
            }
        }

        return null;
    }

    private boolean checkForLaunchPad(BlockPos corner, Direction right, Direction facing) {
        for (int x = 0; x < 3; x++) {
            for (int z = 0; z < 3; z++) {
                BlockPos targetBlock = corner.relative(right, x).relative(facing, z);
                BlockEntity blockEntity = getLevel().getBlockEntity(targetBlock);
                if (!(blockEntity instanceof LaunchPadBlockEntity))
                    return false;
            }
        }

        return true;
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
        NavigatorInstanceTracker.remove(this);
    }

    @Override
    public void load(@NotNull CompoundTag compoundTag) {
        super.load(compoundTag);

        this.items = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        ContainerHelper.loadAllItems(compoundTag, this.items);

        this.mapCrosshairX = compoundTag.getDouble("MapCrosshairX");
        this.mapCrosshairZ = compoundTag.getDouble("MapCrosshairZ");
        this.fuelPercent = compoundTag.getDouble("FuelPercent");
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag compoundTag) {
        super.saveAdditional(compoundTag);

        compoundTag.putDouble("MapCrosshairX", this.mapCrosshairX);
        compoundTag.putDouble("MapCrosshairZ", this.mapCrosshairZ);
        compoundTag.putDouble("FuelPercent", this.fuelPercent);

        ContainerHelper.saveAllItems(compoundTag, this.items);
    }

    @Override
    protected @NotNull Component getDefaultName() {
        return Component.translatable("container.createmissiles.navigator");
    }

    @Override
    protected @NotNull AbstractContainerMenu createMenu(int id, @NotNull Inventory playerInventory) {
        return new NavigatorMenu(id, playerInventory, this, dataAccess, findSchematicator());
    }

    @Override
    public boolean canPlaceItem(int i, @NotNull ItemStack itemStack) {
        return i == 0 && itemStack.is(Items.FILLED_MAP);
    }
}
