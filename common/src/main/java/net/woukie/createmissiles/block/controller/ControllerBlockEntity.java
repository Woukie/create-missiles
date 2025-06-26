package net.woukie.createmissiles.block.controller;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.woukie.createmissiles.MultiblockHelper;
import net.woukie.createmissiles.block.MissileAbstractBlockEntity;
import net.woukie.createmissiles.block.launchpad.LaunchPadBlockEntity;
import net.woukie.createmissiles.block.navigator.NavigatorBlockEntity;
import net.woukie.createmissiles.block.schematicator.SchematicatorBlock;
import net.woukie.createmissiles.block.schematicator.SchematicatorBlockEntity;
import net.woukie.createmissiles.entity.MissileEntity;
import net.woukie.createmissiles.entity.MissileEntityManager;
import net.woukie.createmissiles.missilemanager.Trajectories;
import net.woukie.createmissiles.missilemanager.Trajectory;
import net.woukie.createmissiles.missilemanager.TrajectoryData;
import net.woukie.createmissiles.missilemanager.parts.ChassisType;
import net.woukie.createmissiles.missilemanager.parts.MissilePartType;
import net.woukie.createmissiles.missilemanager.parts.ThrusterType;
import net.woukie.createmissiles.missilemanager.parts.WarheadType;
import net.woukie.createmissiles.recipe.MissileIngredient;
import net.woukie.createmissiles.recipe.MissilePartRecipe;
import net.woukie.createmissiles.registry.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

// Inventory divided up into 32-slot areas representing thruster, chassis and warhead
public class ControllerBlockEntity extends MissileAbstractBlockEntity {
    private boolean initialized;
    private UUID missileEntityTrackingID = UUID.randomUUID();

    private final ContainerData dataAccess;

    private boolean launching = false;

//    Cached variables, all updated on tick or serverTick
    private SchematicatorBlockEntity schematicator;
    private BlockPos cornerLaunchPadPos;
    private int warheadBuildPercent;
    private int chassisBuildPercent;
    private int thrusterBuildPercent;

    public ControllerBlockEntity(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);
        this.items = NonNullList.withSize(96, ItemStack.EMPTY);
        this.dataAccess = new ContainerData() {
            @Override
            public int get(int i) {
                return switch (i) {
                    case 0 -> getBlockPos().getX();
                    case 1 -> getBlockPos().getY();
                    case 2 -> getBlockPos().getZ();
                    case 3 -> cornerLaunchPadPos == null ? 0 : 1;
                    case 4 -> schematicator == null ? 0 : 1;
                    case 5 -> MultiblockHelper.findEdgeBlock(
                            ControllerBlockEntity.this,
                            getLevel(),
                            BlockEntities.NAVIGATOR.get()
                    ) == null ? 0 : 1;
                    case 6 -> {
                        Direction launchPadDirection = getBlockState().getValue(HorizontalDirectionalBlock.FACING).getOpposite();
                        BlockPos launchPad = getBlockPos().relative(launchPadDirection, 1);
                        BlockEntity blockEntity = getLevel().getBlockEntity(launchPad);

                        if (blockEntity instanceof LaunchPadBlockEntity launchPadBlockEntity) {
                            yield (int)launchPadBlockEntity.getSpeed();
                        }

                        yield 0;
                    }
                    default -> 0;
                };
            }

            @Override
            public void set(int i, int j) {}

            @Override
            public int getCount() {
                return 7;
            }
        };
    }

    public void giveItem(@NotNull ItemStack itemStack) {
        MissilePartRecipe recipe = findAcceptingRecipe(itemStack);
        if (recipe == null) return;
        var partType = PartTypes.get(recipe.getSchematic());
        addItemToPartOfInventory(itemStack, partType.getStartSlot(), partType.getEndSlot());
    }

    private void addItemToPartOfInventory(ItemStack itemStack, int fromIndex, int toIndex) {
        for (int i = fromIndex; i < toIndex; i++) {
            ItemStack stack = getItem(i);
            if (ItemStack.isSameItemSameTags(stack, itemStack)) {
                itemStack.setCount(0);
                stack.grow(1);
                return;
            }
        }

        for (int i = fromIndex; i < toIndex; i++) {
            ItemStack stack = getItem(i);
            if (stack.isEmpty()) {
                setItem(i, itemStack.copyAndClear());
                return;
            }
        }
    }

    public MissilePartRecipe findAcceptingRecipe(ItemStack itemStack) {
        if (level == null || itemStack.getCount() != 1) return null;

        if (schematicator == null) return null;

        MissilePartType warheadType = PartTypes.get(schematicator.getItem(0));
        MissilePartType chassisType = PartTypes.get(schematicator.getItem(1));
        MissilePartType thrusterType = PartTypes.get(schematicator.getItem(2));

        var missilePartRecipes = level.getRecipeManager().getAllRecipesFor(RecipeTypes.MISSILE_PART.get());
        for (var recipe : missilePartRecipes) {
            var schematic = recipe.getSchematic();
            if (((warheadType != null && schematic.equals(warheadType.resourceLocation)) ||
                    (chassisType != null && schematic.equals(chassisType.resourceLocation)) ||
                    (thrusterType != null && schematic.equals(thrusterType.resourceLocation)))
                    && recipe.itemComplements(itemStack, this))
                return recipe;
        }

        return null;
    }

    public void tick() {
        Direction facing = getBlockState().getValue(SchematicatorBlock.FACING).getOpposite();
        cornerLaunchPadPos = MultiblockHelper.findCorner(
                getBlockPos(),
                facing,
                level
        );

        schematicator = (SchematicatorBlockEntity) MultiblockHelper.findEdgeBlock(
                cornerLaunchPadPos,
                facing,
                getLevel(),
                BlockEntities.SCHEMATICATOR.get()
        );

        warheadBuildPercent = 0;
        chassisBuildPercent = 0;
        thrusterBuildPercent = 0;

        if (schematicator == null) return;

        var warheadType = PartTypes.get(schematicator.getItem(0));
        var chassisType = PartTypes.get(schematicator.getItem(1));
        var thrusterType = PartTypes.get(schematicator.getItem(2));

        warheadBuildPercent = MissilePartRecipe.getBuildPercentage(warheadType, level, items);
        chassisBuildPercent = MissilePartRecipe.getBuildPercentage(chassisType, level, items);
        thrusterBuildPercent = MissilePartRecipe.getBuildPercentage(thrusterType, level, items);
    }

    public void serverTick() {
        if (!initialized && hasLevel()) {
            initialized = true;
            ControllerInstanceTracker.add(this);
        }

        if (launching) launch();

        BlockPos entityPosition = getBlockPos();
        if (cornerLaunchPadPos != null) {
            var forward = getBlockState().getValue(SchematicatorBlock.FACING).getOpposite();
            entityPosition = new BlockPos(cornerLaunchPadPos).relative(forward).relative(forward.getClockWise());
        }

        MissileEntity entity = MissileEntityManager.get(missileEntityTrackingID);
        if (entity == null) {
            entity = new MissileEntity(EntityTypes.MISSILE.get(), getLevel());
            entity.setControllerID(missileEntityTrackingID);
            entity.setPos(entityPosition.getX() + 0.5, entityPosition.getY() + 0.5, entityPosition.getZ() + 0.5);
            getLevel().addFreshEntity(entity);
        }

        if (schematicator != null) {
            ItemStack warheadItem = schematicator.getItem(0);
            ItemStack chassisItem = schematicator.getItem(1);
            ItemStack thrusterItem = schematicator.getItem(2);

            MissilePartType warheadType = PartTypes.get(warheadItem);
            MissilePartType chassisType = PartTypes.get(chassisItem);
            MissilePartType thrusterType = PartTypes.get(thrusterItem);

            entity.setWarheadBuildPercent(warheadBuildPercent);
            entity.setWarheadType(warheadType == null ? null : warheadType.resourceLocation);

            entity.setChassisBuildPercent(chassisBuildPercent);
            entity.setChassisType(chassisType == null ? null : chassisType.resourceLocation);

            entity.setThrusterBuildPercent(thrusterBuildPercent);
            entity.setThrusterType(thrusterType == null ? null : thrusterType.resourceLocation);

            ejectNotNeededItems(warheadType, 0, 32);
            ejectNotNeededItems(chassisType, 32, 64);
            ejectNotNeededItems(thrusterType, 64, 96);
        }

        entity.setPos(entityPosition.getX() + 0.5, entityPosition.getY() + 0.5, entityPosition.getZ() + 0.5);
    }

    private void ejectNotNeededItems(@Nullable MissilePartType partType, int backupStartSlot, int backupEndSlot) {
        if (level == null) return;
        var recipe = MissilePartRecipe.fromResourceLocation(level, partType == null ? null : partType.resourceLocation);

        Map<MissileIngredient, Integer> ingredientStatus = recipe.map(a -> a.getMissileIngredients().stream().collect(Collectors.toMap(b -> b, MissileIngredient::getCount))).orElseGet(Map::of);

        int start = partType != null ? partType.getStartSlot() : backupStartSlot;
        int end = partType != null ? partType.getEndSlot() : backupEndSlot;

        for (int i = start; i < end; i++) {
            ItemStack item = getItem(i);
            if (item.isEmpty()) continue;

            Optional<MissileIngredient> matchingIngredient = ingredientStatus.keySet().stream().filter(a -> a.test(item)).findFirst();
            if (matchingIngredient.isPresent()) {
                int itemCount = item.getCount();
                int requiredCount = ingredientStatus.get(matchingIngredient.get());

                if (itemCount < requiredCount) {
                    ingredientStatus.put(matchingIngredient.get(), requiredCount - itemCount);
                } else {
                    if (itemCount != requiredCount) {
                        ItemStack ejected = item.split(requiredCount - itemCount);
                        DefaultDispenseItemBehavior.spawnItem(level, ejected, 1, Direction.UP, getBlockPos().getCenter());
                    }

                    ingredientStatus.remove(matchingIngredient.get());
                }
            } else {

                DefaultDispenseItemBehavior.spawnItem(level, item.copyAndClear(), 1, Direction.UP, getBlockPos().getCenter());
            }
        }
    }

    public void launch() {
//        Initially called on network thread, need to get multiblock structure which requires server thread
        if(getLevel() == null ||
                getLevel().getServer() == null ||
                Thread.currentThread() != getLevel().getServer().getRunningThread()
        ) {
            this.launching = true;
            return;
        }

        this.launching = false;

        Direction launchPadDirection = this.getBlockState().getValue(HorizontalDirectionalBlock.FACING).getOpposite();

        if (cornerLaunchPadPos == null) return;
        if (schematicator == null) return;

        NavigatorBlockEntity navigator = (NavigatorBlockEntity) MultiblockHelper.findEdgeBlock(
                ControllerBlockEntity.this,
                getLevel(),
                BlockEntities.NAVIGATOR.get()
        );
        if (navigator == null) return;

        MissilePartRecipe warheadRecipe = null;
        MissilePartRecipe chassisRecipe = null;
        MissilePartRecipe thrusterRecipe = null;

        MissilePartType warheadType = PartTypes.get(schematicator.getItem(0));
        MissilePartType chassisType = PartTypes.get(schematicator.getItem(1));
        MissilePartType thrusterType = PartTypes.get(schematicator.getItem(2));

        if (warheadType == null || chassisType == null || thrusterType == null) return;

        var missilePartRecipes = getLevel().getRecipeManager().getAllRecipesFor(RecipeTypes.MISSILE_PART.get());
        for (var recipe : missilePartRecipes) {
            if (!(warheadRecipe == null || chassisRecipe == null || thrusterRecipe == null)) break;
            var schematic = recipe.getSchematic();
            if (schematic.equals(warheadType.resourceLocation)) {
                warheadRecipe = recipe;
                continue;
            }

            if (schematic.equals(chassisType.resourceLocation)) {
                chassisRecipe = recipe;
                continue;
            }

            if (schematic.equals(thrusterType.resourceLocation)) {
                thrusterRecipe = recipe;
            }
        }

        if (warheadRecipe == null || chassisRecipe == null || thrusterRecipe == null) return;
        if (!warheadRecipe.matches(this, getLevel())) return;
        if (!chassisRecipe.matches(this, getLevel())) return;
        if (!thrusterRecipe.matches(this, getLevel())) return;

        Trajectory trajectory = new Trajectory(new TrajectoryData(
                getLevel(),
                getBlockPos().relative(launchPadDirection, 2),
                navigator.getTarget(),
                navigator.getFuelPercent(),
                0,
                (WarheadType) warheadType,
                (ChassisType) chassisType,
                (ThrusterType) thrusterType,
                this
        ));

        Trajectories trajectories = Trajectories.get();
        trajectories.activeTrajectories.add(trajectory);
        trajectories.setDirty();

        clearContent();
    }

    @Override
    public int getMaxStackSize() {
        return Integer.MAX_VALUE;
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
        ControllerInstanceTracker.remove(this);
        MissileEntityManager.remove(missileEntityTrackingID);
    }

    @Override
    public void load(@NotNull CompoundTag compoundTag) {
        super.load(compoundTag);
        this.items = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        ContainerHelper.loadAllItems(compoundTag, this.items);
        if (compoundTag.hasUUID("TrackingID")) {
            this.missileEntityTrackingID = compoundTag.getUUID("TrackingID");
        }
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag compoundTag) {
        super.saveAdditional(compoundTag);
        ContainerHelper.saveAllItems(compoundTag, this.items);
        if (missileEntityTrackingID != null) {
            compoundTag.putUUID("TrackingID", missileEntityTrackingID);
        }
    }

    @Override
    protected @NotNull Component getDefaultName() {
        return Component.translatable("container.createmissiles.controller");
    }

    @Override
    protected @NotNull AbstractContainerMenu createMenu(int id, @NotNull Inventory playerInventory) {
        Direction facing = getBlockState().getValue(HorizontalDirectionalBlock.FACING).getOpposite();

        BlockEntity navigator = MultiblockHelper.findEdgeBlock(cornerLaunchPadPos, facing, getLevel(), BlockEntities.NAVIGATOR.get());

        return new ControllerMenu(
                id,
                playerInventory,
                this,
                dataAccess,
                schematicator == null ? new SimpleContainer(3) : schematicator,
                navigator == null ? new SimpleContainer(1) : (Container) navigator
        );
    }

    @Override
    public boolean canPlaceItemThroughFace(int i, @NotNull ItemStack itemStack, @Nullable Direction direction) {
        return false;
    }

    @Override
    public boolean canTakeItemThroughFace(int i, @NotNull ItemStack itemStack, @NotNull Direction direction) {
        return false;
    }
}
