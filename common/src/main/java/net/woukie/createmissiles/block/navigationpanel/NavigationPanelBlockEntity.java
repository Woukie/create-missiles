package net.woukie.createmissiles.block.navigationpanel;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.level.ChunkPos;
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
import net.woukie.createmissiles.missiles.Trajectory;
import net.woukie.createmissiles.missiles.parts.ChassisType;
import net.woukie.createmissiles.missiles.parts.ThrusterType;
import net.woukie.createmissiles.missiles.parts.WarheadType;
import net.woukie.createmissiles.missiles.trajectories.BallisticTrajectory;
import net.woukie.createmissiles.registry.BlockEntities;
import net.woukie.createmissiles.registry.Packets;
import net.woukie.createmissiles.registry.PartTypes;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3d;

public class NavigationPanelBlockEntity extends AbstractBasicBlockEntity {
    public static final int SLOT_MAP = 0;

    private double mapCrosshairX, mapCrosshairZ;
    private float thrustDurationPercent;
    private boolean initialized;
    private BlockPos target;
    private final ContainerData dataAccess;
    private Trajectory simulatedTrajectory;

    public NavigationPanelBlockEntity(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);
        this.items = NonNullList.withSize(1, ItemStack.EMPTY);
        this.mapCrosshairX = 64;
        this.mapCrosshairZ = 64;
        this.thrustDurationPercent = 0.5f;
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
                    case 9 -> Float.floatToIntBits(thrustDurationPercent);
                    case 10 -> MultiblockHelper.findCorner( // Whether launch pad exists
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
                    case 12 -> {
                        if (simulatedTrajectory instanceof BallisticTrajectory ballisticTrajectory) {
                            yield Float.floatToIntBits((float)((double)ballisticTrajectory.getUpperLaunchAngle()));
                        }
                        yield 90;
                    }
                    case 13 -> {
                        if (simulatedTrajectory instanceof BallisticTrajectory ballisticTrajectory) {
                            yield Float.floatToIntBits((float)((double)ballisticTrajectory.getLowerLaunchAngle()));
                        }
                        yield 0;
                    }
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
        }

        tickTarget();
        tickTrajectory();
    }

    public void fuelClicked(float fuelPercent) {
        this.thrustDurationPercent = fuelPercent;
        this.simulatedTrajectory = null;
    }

    public void mapClicked(double mapCrosshairX, double mapCrosshairZ) {
        this.mapCrosshairX = mapCrosshairX;
        this.mapCrosshairZ = mapCrosshairZ;
        this.target = null;
        this.simulatedTrajectory = null;
    }

    public BlockPos getTarget() {
        return this.target;
    }

    private void tickTarget() {
        ItemStack mapItem = getItem(SLOT_MAP);
        if (!mapItem.is(Items.FILLED_MAP) || level == null) {
            target = null;
            this.simulatedTrajectory = null;
            return;
        }

        MapItemSavedData mapData = MapItem.getSavedData(mapItem, level);
        if (mapData == null) {
            target = null;
            this.simulatedTrajectory = null;
            return;
        }

        int multiplier = 1 << mapData.scale;
        int blockX = (int)(mapData.centerX - 64 * multiplier + (multiplier * mapCrosshairX));
        int blockZ = (int)(mapData.centerZ - 64 * multiplier + (multiplier * mapCrosshairZ));

        ChunkPos chunkPos = new ChunkPos(new BlockPos(blockX, 0, blockZ));
        if (!level.isLoaded(new BlockPos(blockX, 0, blockZ))) {
            ((ServerLevel) level).setChunkForced(chunkPos.x,  chunkPos.z, true);
            return;
        }

        int scan = level.getMaxBuildHeight();
        BlockPos impactPos = new BlockPos(blockX, scan, blockZ);
        while (scan >= level.getMinBuildHeight()) {
            impactPos = new BlockPos(blockX, scan, blockZ);
            if (!level.getBlockState(impactPos).isAir())
                break;
            scan--;
        }
        target = impactPos;
        ((ServerLevel) level).setChunkForced(chunkPos.x,  chunkPos.z, false);
    }

    private void tickTrajectory() {
        if (target == null) return;
        Direction facing = getBlockState().getValue(AssemblyPanelBlock.FACING).getOpposite();
        BlockPos corner = MultiblockHelper.findCorner(getBlockPos(), facing, level);
        BlockEntity blockEntity = MultiblockHelper.findEdgeBlock(corner, facing, getLevel(), BlockEntities.ASSEMBLY_PANEL.get());
        if (blockEntity == null) return;
        AssemblyPanelBlockEntity assemblyPanel = (AssemblyPanelBlockEntity) blockEntity;
        if (!assemblyPanel.hasAllAssemblies()) return;

        if (simulatedTrajectory == null) {
            var warheadType = (WarheadType) PartTypes.get(assemblyPanel.getItem(0));
            var chassisType = (ChassisType) PartTypes.get(assemblyPanel.getItem(1));
            var thrusterType = (ThrusterType) PartTypes.get(assemblyPanel.getItem(2));
            Vector3d start = new Vector3d(getBlockPos().getX(), getBlockPos().getY(), getBlockPos().getZ()).add(.5d, .5d, .5d);
            Vector3d end = new Vector3d(target.getX(), target.getY(), target.getZ()).add(.5d, .5d, .5d);
            simulatedTrajectory = thrusterType.createTrajectory(level, new Vector3d(start), new Vector3d(end), warheadType, chassisType, thrusterType, null, this);
        }

        if (simulatedTrajectory instanceof BallisticTrajectory ballisticTrajectory) {
            var low = ballisticTrajectory.getLowerDistanceToTarget();
            var up = ballisticTrajectory.getUpperDistanceToTarget();
            if (low == null || up == null || low + up > 0.5F) {
                ballisticTrajectory.refineLaunchAngleOnce();
            }
        }
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
        this.thrustDurationPercent = compoundTag.getFloat("FuelPercent");
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag compoundTag) {
        super.saveAdditional(compoundTag);

        compoundTag.putDouble("MapCrosshairX", this.mapCrosshairX);
        compoundTag.putDouble("MapCrosshairZ", this.mapCrosshairZ);
        compoundTag.putFloat("FuelPercent", this.thrustDurationPercent);

        ContainerHelper.saveAllItems(compoundTag, this.items);
    }

    @Override
    protected @NotNull Component getDefaultName() {
        return Component.translatable("container.createmissiles.navigation_panel");
    }

    @Override
    protected @NotNull AbstractContainerMenu createMenu(int id, @NotNull Inventory playerInventory) {
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

    public double getUpperLaunchAngle() {
        if (simulatedTrajectory instanceof BallisticTrajectory ballisticTrajectory) {
            return ballisticTrajectory.getUpperLaunchAngle();
        }
        return 0;
    }

    public double getLowerLaunchAngle() {
        if (simulatedTrajectory instanceof BallisticTrajectory ballisticTrajectory) {
            return ballisticTrajectory.getLowerLaunchAngle();
        }
        return 0;
    }

    public float getThrustDurationPercent() {
        return thrustDurationPercent;
    }
}
