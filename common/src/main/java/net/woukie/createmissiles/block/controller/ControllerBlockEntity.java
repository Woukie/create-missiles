package net.woukie.createmissiles.block.controller;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
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
import net.woukie.createmissiles.item.schematic.ChassisSchematic;
import net.woukie.createmissiles.item.schematic.ThrusterSchematic;
import net.woukie.createmissiles.item.schematic.WarheadSchematic;
import net.woukie.createmissiles.missilemanager.Trajectories;
import net.woukie.createmissiles.missilemanager.Trajectory;
import net.woukie.createmissiles.missilemanager.TrajectoryData;
import net.woukie.createmissiles.missilemanager.parts.ChassisType;
import net.woukie.createmissiles.missilemanager.parts.MissilePartType;
import net.woukie.createmissiles.missilemanager.parts.ThrusterType;
import net.woukie.createmissiles.missilemanager.parts.WarheadType;
import net.woukie.createmissiles.recipe.MissilePartRecipe;
import net.woukie.createmissiles.registry.MissileBlockEntities;
import net.woukie.createmissiles.registry.MissilePartTypes;
import net.woukie.createmissiles.registry.MissileRecipeTypes;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

// Inventory divided up into 32-slot areas representing thruster, chassis and warhead
public class ControllerBlockEntity extends MissileAbstractBlockEntity {
    private boolean initialized;

    private final ContainerData dataAccess;

    private boolean launching = false;

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
                    case 3 -> MultiblockHelper.findCorner(
                            blockPos,
                            blockState.getValue(SchematicatorBlock.FACING).getOpposite(),
                            level
                    ) == null ? 0 : 1;
                    case 4 -> MultiblockHelper.findEdgeBlock(
                            ControllerBlockEntity.this,
                            getLevel(),
                            MissileBlockEntities.SCHEMATICATOR.get()
                    ) == null ? 0 : 1;
                    case 5 -> MultiblockHelper.findEdgeBlock(
                            ControllerBlockEntity.this,
                            getLevel(),
                            MissileBlockEntities.NAVIGATOR.get()
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
        var partType = MissilePartTypes.get(recipe.getSchematic());
        addItemToPartOfInventory(itemStack, partType.startSlot, partType.endSlot);
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

        BlockEntity blockEntity = MultiblockHelper.findEdgeBlock(this, level, MissileBlockEntities.SCHEMATICATOR.get());
        if (blockEntity == null) return null;

        SchematicatorBlockEntity schematicator = (SchematicatorBlockEntity) blockEntity;

        MissilePartType warheadType = MissilePartTypes.get(schematicator.getItem(0));
        MissilePartType chassisType = MissilePartTypes.get(schematicator.getItem(1));
        MissilePartType thrusterType = MissilePartTypes.get(schematicator.getItem(2));

        var missilePartRecipes = level.getRecipeManager().getAllRecipesFor(MissileRecipeTypes.MISSILE_PART.get());
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

    public void serverTick() {
        if (!initialized && hasLevel()) {
            initialized = true;
            ControllerInstanceTracker.add(this);
        }

        if (launching) launch();
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

        BlockPos corner = MultiblockHelper.findCorner(
                worldPosition,
                launchPadDirection,
                getLevel()
        );
        if (corner == null) return;

        SchematicatorBlockEntity schematicator = (SchematicatorBlockEntity) MultiblockHelper.findEdgeBlock(
                ControllerBlockEntity.this,
                getLevel(),
                MissileBlockEntities.SCHEMATICATOR.get()
        );
        if (schematicator == null) return;

        NavigatorBlockEntity navigator = (NavigatorBlockEntity) MultiblockHelper.findEdgeBlock(
                ControllerBlockEntity.this,
                getLevel(),
                MissileBlockEntities.NAVIGATOR.get()
        );
        if (navigator == null) return;

        MissilePartRecipe warheadRecipe = null;
        MissilePartRecipe chassisRecipe = null;
        MissilePartRecipe thrusterRecipe = null;

        MissilePartType warheadType = MissilePartTypes.get(schematicator.getItem(0));
        MissilePartType chassisType = MissilePartTypes.get(schematicator.getItem(1));
        MissilePartType thrusterType = MissilePartTypes.get(schematicator.getItem(2));

        if (warheadType == null || chassisType == null || thrusterType == null) return;

        var missilePartRecipes = getLevel().getRecipeManager().getAllRecipesFor(MissileRecipeTypes.MISSILE_PART.get());
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

    @Override
    protected @NotNull Component getDefaultName() {
        return Component.translatable("container.createmissiles.controller");
    }

    @Override
    protected @NotNull AbstractContainerMenu createMenu(int id, @NotNull Inventory playerInventory) {
        Direction facing = getBlockState().getValue(HorizontalDirectionalBlock.FACING).getOpposite();
        BlockPos corner = MultiblockHelper.findCorner(getBlockPos(), facing, getLevel());

        BlockEntity navigator = MultiblockHelper.findEdgeBlock(corner, facing, getLevel(), MissileBlockEntities.NAVIGATOR.get());
        BlockEntity schematicator = MultiblockHelper.findEdgeBlock(corner, facing, getLevel(), MissileBlockEntities.SCHEMATICATOR.get());

        return new ControllerMenu(
                id,
                playerInventory,
                this,
                dataAccess,
                schematicator == null ? new SimpleContainer(3) : (Container) schematicator,
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
