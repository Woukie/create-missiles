package net.woukie.createmissiles.block.controlpanel;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.woukie.createmissiles.MultiblockHelper;
import net.woukie.createmissiles.block.MissileAbstractBlockEntity;
import net.woukie.createmissiles.block.launchpad.LaunchPadBlockEntity;
import net.woukie.createmissiles.block.navigation_panel.NavigationPanelBlockEntity;
import net.woukie.createmissiles.block.assemblypanel.AssemblyPanelBlock;
import net.woukie.createmissiles.block.assemblypanel.AssemblyPanelBlockEntity;
import net.woukie.createmissiles.entity.MissileEntity;
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
public class ControlPanelBlockEntity extends MissileAbstractBlockEntity {
    private boolean initialized;
    private UUID entityId = null;

    private final ContainerData dataAccess;

    private boolean launching = false;

//    Cached variables, all updated on tick or serverTick
    private AssemblyPanelBlockEntity assemblyPanel;
    private BlockPos cornerLaunchPadPos;
    private int warheadBuildPercent;
    private int chassisBuildPercent;
    private int thrusterBuildPercent;

    public ControlPanelBlockEntity(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState) {
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
                    case 4 -> assemblyPanel == null ? 0 : 1;
                    case 5 -> MultiblockHelper.findEdgeBlock(
                            ControlPanelBlockEntity.this,
                            getLevel(),
                            BlockEntities.NAVIGATION_PANEL.get()
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
        var partType = PartTypes.get(recipe.getAssembly());
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

        if (assemblyPanel == null) return null;

        MissilePartType warheadType = PartTypes.get(assemblyPanel.getItem(0));
        MissilePartType chassisType = PartTypes.get(assemblyPanel.getItem(1));
        MissilePartType thrusterType = PartTypes.get(assemblyPanel.getItem(2));

        var missilePartRecipes = level.getRecipeManager().getAllRecipesFor(RecipeTypes.MISSILE_PART.get());
        for (var recipe : missilePartRecipes) {
            var assembly = recipe.getAssembly();
            if (((warheadType != null && assembly.equals(warheadType.resourceLocation)) ||
                    (chassisType != null && assembly.equals(chassisType.resourceLocation)) ||
                    (thrusterType != null && assembly.equals(thrusterType.resourceLocation)))
                    && recipe.itemComplements(itemStack, this))
                return recipe;
        }

        return null;
    }

    public void tick() {
        Direction facing = getBlockState().getValue(AssemblyPanelBlock.FACING).getOpposite();
        cornerLaunchPadPos = MultiblockHelper.findCorner(
                getBlockPos(),
                facing,
                level
        );

        assemblyPanel = (AssemblyPanelBlockEntity) MultiblockHelper.findEdgeBlock(
                cornerLaunchPadPos,
                facing,
                getLevel(),
                BlockEntities.ASSEMBLY_PANEL.get()
        );

        int oldChecksum = warheadBuildPercent + chassisBuildPercent + thrusterBuildPercent;

        warheadBuildPercent = 0;
        chassisBuildPercent = 0;
        thrusterBuildPercent = 0;

        if (assemblyPanel == null) return;

        var warheadType = PartTypes.get(assemblyPanel.getItem(0));
        var chassisType = PartTypes.get(assemblyPanel.getItem(1));
        var thrusterType = PartTypes.get(assemblyPanel.getItem(2));

        warheadBuildPercent = MissilePartRecipe.getBuildPercentage(warheadType, level, items);
        chassisBuildPercent = MissilePartRecipe.getBuildPercentage(chassisType, level, items);
        thrusterBuildPercent = MissilePartRecipe.getBuildPercentage(thrusterType, level, items);

        var soundOrigin = cornerLaunchPadPos.relative(facing).relative(facing.getClockWise());
        if (oldChecksum != warheadBuildPercent + chassisBuildPercent + thrusterBuildPercent && level != null) {
            level.playSound(null, soundOrigin, SoundEvents.METAL_PLACE, SoundSource.BLOCKS);
        }
    }

    public void serverTick() {
        if (!initialized && hasLevel()) {
            initialized = true;
            ControlPanelInstanceTracker.add(this);
        }

        ServerLevel level = (ServerLevel) getLevel();
        if (level == null) return;

        if (launching) launch();

        BlockPos entityPosition = getBlockPos();
        if (cornerLaunchPadPos != null) {
            var forward = getBlockState().getValue(AssemblyPanelBlock.FACING).getOpposite();
            entityPosition = new BlockPos(cornerLaunchPadPos).relative(forward).relative(forward.getClockWise());
        }

        Entity entity = level.getEntity(this.entityId);
        if (entity == null || !entity.getType().equals(EntityTypes.MISSILE.get())) {
            entity = new MissileEntity(EntityTypes.MISSILE.get(), level);
            entity.setPos(entityPosition.getX() + 0.5, entityPosition.getY() + 0.5, entityPosition.getZ() + 0.5);
            level.addFreshEntity(entity);
            this.entityId = entity.getUUID();
        }

        if (cornerLaunchPadPos == null) {
            ejectNotNeededItems(null, 0, 96);
            var missileEntity = (MissileEntity) entity;
            missileEntity.setWarheadBuildPercent(0);
            missileEntity.setChassisBuildPercent(0);
            missileEntity.setThrusterBuildPercent(0);
            level.playSound(null, entityPosition, SoundEvents.CHAIN_BREAK, SoundSource.BLOCKS);
        }

        if (assemblyPanel != null && entity.getType() != EntityTypes.MISSILE) {
            ItemStack warheadItem = assemblyPanel.getItem(0);
            ItemStack chassisItem = assemblyPanel.getItem(1);
            ItemStack thrusterItem = assemblyPanel.getItem(2);

            MissilePartType warheadType = PartTypes.get(warheadItem);
            MissilePartType chassisType = PartTypes.get(chassisItem);
            MissilePartType thrusterType = PartTypes.get(thrusterItem);

            var missileEntity = (MissileEntity) entity;

            missileEntity.setWarheadBuildPercent(warheadBuildPercent);
            missileEntity.setWarheadType(warheadType == null ? null : warheadType.resourceLocation);

            missileEntity.setChassisBuildPercent(chassisBuildPercent);
            missileEntity.setChassisType(chassisType == null ? null : chassisType.resourceLocation);

            missileEntity.setThrusterBuildPercent(thrusterBuildPercent);
            missileEntity.setThrusterType(thrusterType == null ? null : thrusterType.resourceLocation);

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
        if (assemblyPanel == null) return;

        NavigationPanelBlockEntity navigationPanel = (NavigationPanelBlockEntity) MultiblockHelper.findEdgeBlock(
                ControlPanelBlockEntity.this,
                getLevel(),
                BlockEntities.NAVIGATION_PANEL.get()
        );
        if (navigationPanel == null) return;

        MissilePartRecipe warheadRecipe = null;
        MissilePartRecipe chassisRecipe = null;
        MissilePartRecipe thrusterRecipe = null;

        MissilePartType warheadType = PartTypes.get(assemblyPanel.getItem(0));
        MissilePartType chassisType = PartTypes.get(assemblyPanel.getItem(1));
        MissilePartType thrusterType = PartTypes.get(assemblyPanel.getItem(2));

        if (warheadType == null || chassisType == null || thrusterType == null) return;

        var missilePartRecipes = getLevel().getRecipeManager().getAllRecipesFor(RecipeTypes.MISSILE_PART.get());
        for (var recipe : missilePartRecipes) {
            if (!(warheadRecipe == null || chassisRecipe == null || thrusterRecipe == null)) break;
            var assembly = recipe.getAssembly();
            if (assembly.equals(warheadType.resourceLocation)) {
                warheadRecipe = recipe;
                continue;
            }

            if (assembly.equals(chassisType.resourceLocation)) {
                chassisRecipe = recipe;
                continue;
            }

            if (assembly.equals(thrusterType.resourceLocation)) {
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
                navigationPanel.getTarget(),
                navigationPanel.getFuelPercent(),
                0,
                (WarheadType) warheadType,
                (ChassisType) chassisType,
                (ThrusterType) thrusterType,
                this
        ));

        Trajectories trajectories = Trajectories.get();
        trajectories.launch(trajectory);
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
        ControlPanelInstanceTracker.remove(this);
        if (getLevel() != null && !getLevel().isClientSide) {
            ServerLevel level = (ServerLevel) getLevel();
            Entity entity = level.getEntity(entityId);
            if (entity != null && entity.getType().equals(EntityTypes.MISSILE.get())) {
                entity.remove(Entity.RemovalReason.KILLED);
            }
        }
    }

    @Override
    public void load(@NotNull CompoundTag compoundTag) {
        super.load(compoundTag);
        this.items = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        ContainerHelper.loadAllItems(compoundTag, this.items);
        if (compoundTag.contains("EntityID")) {
            this.entityId = compoundTag.getUUID("EntityID");
        }
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag compoundTag) {
        super.saveAdditional(compoundTag);
        ContainerHelper.saveAllItems(compoundTag, this.items);
        if (this.entityId != null) {
            compoundTag.putUUID("EntityID", this.entityId);
        }
    }

    @Override
    public void saveToItem(@NotNull ItemStack itemStack) {
        var data = this.saveWithoutMetadata();
        data.remove("EntityID");
        data.remove("Items");
        BlockItem.setBlockEntityData(itemStack, this.getType(), data);
    }

    @Override
    protected @NotNull Component getDefaultName() {
        return Component.translatable("container.createmissiles.control_panel");
    }

    @Override
    protected @NotNull AbstractContainerMenu createMenu(int id, @NotNull Inventory playerInventory) {
        Direction facing = getBlockState().getValue(HorizontalDirectionalBlock.FACING).getOpposite();

        BlockEntity navigationPanel = MultiblockHelper.findEdgeBlock(cornerLaunchPadPos, facing, getLevel(), BlockEntities.NAVIGATION_PANEL.get());

        return new ControlPanelMenu(
                id,
                playerInventory,
                this,
                dataAccess,
                assemblyPanel == null ? new SimpleContainer(3) : assemblyPanel,
                navigationPanel == null ? new SimpleContainer(1) : (Container) navigationPanel
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
