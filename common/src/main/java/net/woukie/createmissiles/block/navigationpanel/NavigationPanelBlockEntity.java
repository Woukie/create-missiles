package net.woukie.createmissiles.block.navigationpanel;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import net.woukie.createmissiles.MultiblockHelper;
import net.woukie.createmissiles.block.entity.AbstractBasicBlockEntity;
import net.woukie.createmissiles.block.assemblypanel.AssemblyPanelBlock;
import net.woukie.createmissiles.block.assemblypanel.AssemblyPanelBlockEntity;
import net.woukie.createmissiles.block.navigationpanel.messages.UpdateMapDataMessage;
import net.woukie.createmissiles.inventory.NavigationPanelMenu;
import net.woukie.createmissiles.missiles.parts.ChassisType;
import net.woukie.createmissiles.missiles.parts.ThrusterType;
import net.woukie.createmissiles.missiles.parts.WarheadType;
import net.woukie.createmissiles.missiles.trajectories.TrajectoryHelper;
import net.woukie.createmissiles.registry.BlockEntities;
import net.woukie.createmissiles.registry.Packets;
import net.woukie.createmissiles.registry.PartTypes;
import org.jetbrains.annotations.NotNull;

public class NavigationPanelBlockEntity extends AbstractBasicBlockEntity {
    public static final int SLOT_MAP = 0;

    private double mapCrosshairX, mapCrosshairZ, thrustDurationPercent;
    private boolean initialized;
    private BlockPos target;
    private final ContainerData dataAccess;
    private AssemblyPanelBlockEntity assemblyPanel;
    private int relativeMinThrustDuration = 0;
    private double maxThrustDuration = 0;

    public NavigationPanelBlockEntity(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);
        this.items = NonNullList.withSize(1, ItemStack.EMPTY);
        this.mapCrosshairX = 64;
        this.mapCrosshairZ = 64;
        this.thrustDurationPercent = 0;
        this.dataAccess = new ContainerData() {
            public int get(int i) {
                return switch (i) {
                    case 0 -> (int) mapCrosshairX;
                    case 1 -> (int) mapCrosshairZ;
                    case 2 -> target == null ? 0 : 1;
                    case 3 -> target == null ? 0 : target.getX();
                    case 4 -> target == null ? 0 : target.getY();
                    case 5 -> target == null ? 0 : target.getZ();
                    case 6 -> getBlockPos().getX();
                    case 7 -> getBlockPos().getY();
                    case 8 -> getBlockPos().getZ();
                    case 9 -> (int) (thrustDurationPercent * 100D);
                    case 10 -> MultiblockHelper.findCorner(
                            blockPos,
                            blockState.getValue(AssemblyPanelBlock.FACING).getOpposite(),
                            level
                    ) == null ? 0 : 1;
//                    TODO: Keep track of multiblock instead of searching for it every tick, but this isnt actually expensive. worst case 38 blocks searched
                    case 11 -> MultiblockHelper.findEdgeBlock(
                            NavigationPanelBlockEntity.this,
                            getLevel(),
                            BlockEntities.ASSEMBLY_PANEL.get()
                    ) == null ? 0 : 1;
                    case 12 -> relativeMinThrustDuration;
                    case 13 -> (int)maxThrustDuration;
                    default -> 0;
                };
            }

            public void set(int i, int j) {}

            public int getCount() {
                return 14;
            }
        };
    }

    public void serverTick() {
        if (!initialized && hasLevel()) {
            initialized = true;
            NavigationPanelInstanceTracker.add(this);
            recalculateTarget();
        }


        ItemStack itemStack = getItem(0);
        if (!itemStack.is(Items.FILLED_MAP) || itemStack.isEmpty() || level == null) return;

        assemblyPanel = (AssemblyPanelBlockEntity) MultiblockHelper.findEdgeBlock(
                this,
                getLevel(),
                BlockEntities.ASSEMBLY_PANEL.get()
        );
    }

    public void fuelClicked(double fuelPercent) {
        this.thrustDurationPercent = fuelPercent;
    }

    public void mapClicked(double mapCrosshairX, double mapCrosshairZ) {
        this.mapCrosshairX = mapCrosshairX;
        this.mapCrosshairZ = mapCrosshairZ;
        recalculateTarget();

        ItemStack warheadItem = assemblyPanel.getItem(0);
        ItemStack chassisItem = assemblyPanel.getItem(1);
        ItemStack thrusterItem = assemblyPanel.getItem(2);


        WarheadType warheadType = (WarheadType) PartTypes.get(warheadItem);
        ChassisType chassisType = (ChassisType) PartTypes.get(chassisItem);
        ThrusterType thrusterType = (ThrusterType) PartTypes.get(thrusterItem);

        if(warheadType == null || chassisType == null || thrusterType == null) return;

        TrajectoryHelper.MissileConfig missileConfig = new TrajectoryHelper.MissileConfig(thrusterType, chassisType, warheadType);
        double[] launchAngleRange = {0, 90};
        TrajectoryHelper.LaunchConfig launchConfig = new TrajectoryHelper.LaunchConfig(worldPosition, target, launchAngleRange, 0, missileConfig);
        TrajectoryHelper.LaunchSolution minSolution = TrajectoryHelper.findMinLaunchSolution(launchConfig);
        if(minSolution != null)
        {
            maxThrustDuration = launchConfig.missileConfig.maxThrustDuration;
            relativeMinThrustDuration = (int)(minSolution.thrustDuration / maxThrustDuration * 100);
        }else
        {
            System.out.println("No Solution");
        }
    }

    public BlockPos getTarget() {
        recalculateTarget();
        return this.target;
    }

    @SuppressWarnings("unused")
    public double getThrustDurationPercent() {
        return thrustDurationPercent;
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

    @Override
    public void setRemoved() {
        super.setRemoved();
        NavigationPanelInstanceTracker.remove(this);
    }

    @Override
    public void load(@NotNull CompoundTag compoundTag) {
        super.load(compoundTag);

        this.items = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        ContainerHelper.loadAllItems(compoundTag, this.items);

        this.mapCrosshairX = compoundTag.getDouble("MapCrosshairX");
        this.mapCrosshairZ = compoundTag.getDouble("MapCrosshairZ");
        this.thrustDurationPercent = compoundTag.getDouble("FuelPercent");
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag compoundTag) {
        super.saveAdditional(compoundTag);

        compoundTag.putDouble("MapCrosshairX", this.mapCrosshairX);
        compoundTag.putDouble("MapCrosshairZ", this.mapCrosshairZ);
        compoundTag.putDouble("FuelPercent", this.thrustDurationPercent);

        ContainerHelper.saveAllItems(compoundTag, this.items);
    }

    @Override
    protected @NotNull Component getDefaultName() {
        return Component.translatable("container.createmissiles.navigation_panel");
    }

    @Override
    protected @NotNull AbstractContainerMenu createMenu(int id, @NotNull Inventory playerInventory) {
        recalculateTarget();


        Direction facing = getBlockState().getValue(AssemblyPanelBlock.FACING).getOpposite();
        BlockPos corner = MultiblockHelper.findCorner(getBlockPos(), facing, level);
        BlockEntity assemblyPanel = MultiblockHelper.findEdgeBlock(corner, facing, getLevel(), BlockEntities.ASSEMBLY_PANEL.get());

        if (level != null && !level.isClientSide) {
            ItemStack itemStack = getItem(SLOT_MAP);
            if (itemStack.is(Items.FILLED_MAP) && !itemStack.isEmpty()) {
                MapItemSavedData data = MapItem.getSavedData(itemStack, level);
                Integer mapId = MapItem.getMapId(itemStack);
                if (data != null && mapId != null) {
                    Packets.UPDATE_MAP_DATA.sendToPlayer((ServerPlayer) playerInventory.player, new UpdateMapDataMessage(mapId, data.save(new CompoundTag())));
                }
            }
        }
        return new NavigationPanelMenu(id, playerInventory, this, this.dataAccess, assemblyPanel == null ? new SimpleContainer(3) : (AssemblyPanelBlockEntity)assemblyPanel);
    }

    @Override
    public boolean canPlaceItem(int i, @NotNull ItemStack itemStack) {
        return i == 0 && itemStack.is(Items.FILLED_MAP);
    }
}
