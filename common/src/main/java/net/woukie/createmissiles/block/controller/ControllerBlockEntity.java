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
import net.woukie.createmissiles.missilemanager.Trajectories;
import net.woukie.createmissiles.missilemanager.Trajectory;
import net.woukie.createmissiles.missilemanager.TrajectoryData;
import net.woukie.createmissiles.missilemanager.parts.ChassisType;
import net.woukie.createmissiles.missilemanager.parts.Ingredient;
import net.woukie.createmissiles.missilemanager.parts.ThrusterType;
import net.woukie.createmissiles.missilemanager.parts.WarheadType;
import net.woukie.createmissiles.registry.MissileBlockEntities;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;

// Inventory divided up into 32-slot areas representing thruster, chassis and warhead
public class ControllerBlockEntity extends MissileAbstractBlockEntity {
    private boolean initialized;

    private final ContainerData dataAccess;

    private boolean launching = false;

    public ControllerBlockEntity(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);
        this.items = NonNullList.withSize(128, ItemStack.EMPTY);
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
        if (level == null || itemStack.getCount() != 1) return;

        BlockEntity blockEntity = MultiblockHelper.findEdgeBlock(this, level, MissileBlockEntities.SCHEMATICATOR.get());
        if (blockEntity == null) return;

        SchematicatorBlockEntity schematicator = (SchematicatorBlockEntity)blockEntity;

        WarheadType warhead = schematicator.getWarhead();
        if (warhead != null) {
            var ingredientsLeft = warhead.getIngredientsLeft(items.subList(0, 32));
            if (itemFulfillsIngredients(ingredientsLeft, itemStack)) {
                addItemToPartOfInventory(itemStack, 0, 32);
                return;
            }
        }

        ChassisType chassis = schematicator.getChassis();
        if (chassis != null) {
            var ingredientsLeft = chassis.getIngredientsLeft(items.subList(32, 64));
            if (itemFulfillsIngredients(ingredientsLeft, itemStack)) {
                addItemToPartOfInventory(itemStack, 32, 64);
                return;
            }
        }

        ThrusterType thruster = schematicator.getThruster();
        if (thruster != null) {
            var ingredientsLeft = thruster.getIngredientsLeft(items.subList(64, 96));
            if (itemFulfillsIngredients(ingredientsLeft, itemStack)) {
                addItemToPartOfInventory(itemStack, 64, 96);
            }
        }
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

    public boolean canGiveItem(ItemStack itemStack) {
        if (level == null || itemStack.getCount() != 1) return false;

        BlockEntity blockEntity = MultiblockHelper.findEdgeBlock(this, level, MissileBlockEntities.SCHEMATICATOR.get());
        if (blockEntity == null) return false;

        SchematicatorBlockEntity schematicator = (SchematicatorBlockEntity)blockEntity;
        WarheadType warhead = schematicator.getWarhead();
        ChassisType chassis = schematicator.getChassis();
        ThrusterType thruster = schematicator.getThruster();

        if (warhead != null) {
            var ingredientsLeft = warhead.getIngredientsLeft(items.subList(0, 32));
            if (itemFulfillsIngredients(ingredientsLeft, itemStack))
                return true;
        }

        if (chassis != null) {
            var ingredientsLeft = chassis.getIngredientsLeft(items.subList(32, 64));
            if (itemFulfillsIngredients(ingredientsLeft, itemStack))
                return true;
        }

        if (thruster != null) {
            var ingredientsLeft = thruster.getIngredientsLeft(items.subList(64, 96));
            if (itemFulfillsIngredients(ingredientsLeft, itemStack))
                return true;
        }

        return false;
    }

    private boolean itemFulfillsIngredients(HashMap<Ingredient, Integer> ingredients, ItemStack itemStack) {
        return ingredients.entrySet().stream().anyMatch(entry -> entry.getKey().matches(itemStack) && entry.getValue() > 0);
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

        WarheadType warheadType = schematicator.getWarhead();
        ChassisType chassisType = schematicator.getChassis();
        ThrusterType thrusterType = schematicator.getThruster();

        if (warheadType == null || chassisType == null || thrusterType == null) return;

        for (var entry : warheadType.getIngredientsLeft(items.subList(0, 32)).entrySet())
            if (entry.getValue() > 0) return;

        for (var entry : chassisType.getIngredientsLeft(items.subList(32, 64)).entrySet())
            if (entry.getValue() > 0) return;

        for (var entry : thrusterType.getIngredientsLeft(items.subList(64, 96)).entrySet())
            if (entry.getValue() > 0) return;

        Trajectory trajectory = new Trajectory(new TrajectoryData(
                getLevel(),
                getBlockPos().relative(launchPadDirection, 2),
                navigator.getTarget(),
                navigator.getFuelPercent(),
                0,
                schematicator.getWarhead(),
                schematicator.getChassis(),
                schematicator.getThruster(),
                this
        ));

        Trajectories trajectories = Trajectories.get();
        trajectories.activeTrajectories.add(trajectory);
        trajectories.setDirty();

        clearContent();
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
