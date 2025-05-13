package net.woukie.createmissiles.block.controller;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import net.woukie.createmissiles.missilemanager.Trajectories;
import net.woukie.createmissiles.missilemanager.Trajectory;
import net.woukie.createmissiles.missilemanager.TrajectoryData;
import net.woukie.createmissiles.missilemanager.parts.Chassis;
import net.woukie.createmissiles.missilemanager.parts.PartRegistry;
import net.woukie.createmissiles.missilemanager.parts.Thruster;
import net.woukie.createmissiles.missilemanager.parts.Warhead;
import net.woukie.createmissiles.registry.MissileItems;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ControllerBlockEntity extends BaseContainerBlockEntity implements WorldlyContainer {
    public static final int SLOT_MAP = 0;
    public static final int SLOT_WARHEAD = 1;
    public static final int SLOT_CHASSIS = 2;
    public static final int SLOT_THRUSTER = 3;
    private static final int[] SLOTS_FOR_UP = new int[]{0};
    private static final int[] SLOTS_FOR_DOWN = new int[]{0, 1, 2, 3};
    private static final int[] SLOTS_FOR_SIDES = new int[]{1, 2, 3};

    protected NonNullList<ItemStack> items;

    private double mapCrosshairX, mapCrosshairZ, fuelPercent;
    // Can only calculate this on the server
    private BlockPos impactPos;

    private final ContainerData dataAccess;

    boolean initialized;

    public ControllerBlockEntity(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);

        mapCrosshairX = 64;
        mapCrosshairZ = 64;
        impactPos = blockPos;

        this.items = NonNullList.withSize(4, ItemStack.EMPTY);
        this.dataAccess = new ContainerData() {
            public int get(int i) {
                switch (i) {
                    case 0 -> {
                        MapItemSavedData mapData = getMapData();
                        if (mapData == null)
                            return 0;
                        return mapData.centerX;
                    }
                    case 1 -> {
                        MapItemSavedData mapData = getMapData();
                        if (mapData == null)
                            return 0;
                        return mapData.centerZ;
                    }
                    case 2 -> {
                        return ControllerBlockEntity.this.getBlockPos().getX();
                    }
                    case 3 -> {
                        return ControllerBlockEntity.this.getBlockPos().getY();
                    }
                    case 4 -> {
                        return ControllerBlockEntity.this.getBlockPos().getZ();
                    }
                    case 5 -> {
                        BlockPos impact = ControllerBlockEntity.this.impactPos;
                        if (impact == null)
                            return 0;
                        return impact.getX();
                    }
                    case 6 -> {
                        BlockPos impact = ControllerBlockEntity.this.impactPos;
                        if (impact == null)
                            return 0;
                        return impact.getY();
                    }
                    case 7 -> {
                        BlockPos impact = ControllerBlockEntity.this.impactPos;
                        if (impact == null)
                            return 0;
                        return impact.getZ();
                    }
                    case 8 -> {
                        return (int)(fuelPercent * 100);
                    }
                    case 9 -> {
                        return (int) ControllerBlockEntity.this.mapCrosshairX;
                    }
                    case 10 -> {
                        return (int) ControllerBlockEntity.this.mapCrosshairZ;
                    }
                    default -> {
                        return 0;
                    }
                }
            }

            public void set(int i, int j) {}

            public int getCount() {
                return 11;
            }
        };
    }

    private MapItemSavedData getMapData() {
        ItemStack map = items.get(SLOT_MAP);

        if (map.is(Items.FILLED_MAP)) {
            Integer mapId = MapItem.getMapId(map);
            return MapItem.getSavedData(mapId, getLevel());
        }

        return null;
    }

    public void updateTarget(double mapCrosshairX, double mapCrosshairZ) {
        this.mapCrosshairX = mapCrosshairX;
        this.mapCrosshairZ = mapCrosshairZ;
        this.impactPos = getImpactFromMapCrosshair(this.mapCrosshairX, this.mapCrosshairZ);
    }

    public void tick() {
        if (!initialized && hasLevel()) {
            initialized = true;
            ControllerInstanceManager.add(this);
        }
    }

    public void launch() {
        MapItemSavedData mapData = getMapData();
        Warhead warhead = getWarhead();
        Chassis chassis = getChassis();
        Thruster thruster = getThruster();
        if (mapData == null || warhead == null || chassis == null || thruster == null)
            return;

        Trajectories trajectories = Trajectories.get();
        trajectories.activeTrajectories.add(new Trajectory(new TrajectoryData(
                this.level.getServer().getLevel(getMapData().dimension),
                getBlockPos(),
                getImpactFromMapCrosshair(mapCrosshairX, mapCrosshairZ),
                0,
                warhead,
                chassis,
                thruster
        )));
        trajectories.setDirty();
    }

//    Convert 0-128 map-corner-relative coordinates to global block impact position with valid Y position
    private BlockPos getImpactFromMapCrosshair(double mapCrosshairX, double mapCrosshairY) {
        MapItemSavedData mapData = getMapData();
        if (mapData == null)
            return null;

        int multiplier = 1 << mapData.scale;
        int blockX = (int)(mapData.centerX - 64 * multiplier + (multiplier * mapCrosshairX));
        int blockZ = (int)(mapData.centerZ - 64 * multiplier + (multiplier * mapCrosshairY));

        int scan = level.getMaxBuildHeight();
        BlockPos impactPos = new BlockPos(blockX, scan, blockZ);
        while (scan >= level.getMinBuildHeight()) {
            impactPos = new BlockPos(blockX, scan, blockZ);
            if (!level.getBlockState(impactPos).isAir())
                break;
            scan--;
        }

        return impactPos;
    }

    private Warhead getWarhead() {
        CompoundTag warheadTag = items.get(SLOT_WARHEAD).getTag();
        if (warheadTag == null)
            return null;
        return PartRegistry.getWarhead(new ResourceLocation(warheadTag.getString("Warhead")));
    }

    private Chassis getChassis() {
        CompoundTag chassisTag = items.get(SLOT_CHASSIS).getTag();
        if (chassisTag == null)
            return null;
        return PartRegistry.getChassis(new ResourceLocation(chassisTag.getString("Chassis")));
    }

    private Thruster getThruster() {
        CompoundTag thrusterTag = items.get(SLOT_THRUSTER).getTag();
        if (thrusterTag == null)
            return null;
        return PartRegistry.getThruster(new ResourceLocation(thrusterTag.getString("Thruster")));
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
        return new ControllerMenu(id, inv, this, this.dataAccess);
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

        this.mapCrosshairX = compoundTag.getDouble("MapCrosshairX");
        this.mapCrosshairZ = compoundTag.getDouble("MapCrosshairZ");
    }

    @Override
    protected void saveAdditional(CompoundTag compoundTag) {
        super.saveAdditional(compoundTag);
        compoundTag.putDouble("MapCrosshairX", this.mapCrosshairX);
        compoundTag.putDouble("MapCrosshairZ", this.mapCrosshairZ);
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
        if (i == SLOT_MAP) {
            return itemStack.is(Items.FILLED_MAP);
        } else if (i == SLOT_THRUSTER) {
            return itemStack.is(MissileItems.THRUSTER_SCHEMATIC.get());
        } else if (i == SLOT_WARHEAD) {
            return itemStack.is(MissileItems.WARHEAD_SCHEMATIC.get());
        } else if (i == SLOT_CHASSIS) {
            return itemStack.is(MissileItems.CHASSIS_SCHEMATIC.get());
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
