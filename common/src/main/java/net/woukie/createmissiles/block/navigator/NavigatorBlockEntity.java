package net.woukie.createmissiles.block.navigator;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.woukie.createmissiles.block.MissileAbstractBlockEntity;
import net.woukie.createmissiles.block.launchpad.LaunchPadBlockEntity;
import net.woukie.createmissiles.block.schematicator.SchematicatorBlock;
import net.woukie.createmissiles.block.schematicator.SchematicatorBlockEntity;
import org.jetbrains.annotations.NotNull;

public class NavigatorBlockEntity extends MissileAbstractBlockEntity {
    public static final int SLOT_MAP = 0;

    protected NonNullList<ItemStack> items;

    private double mapCrosshairX, mapCrosshairZ, fuelPercent;
    // Can only calculate this on the server
    private BlockPos target;

    private final ContainerData dataAccess;

    public NavigatorBlockEntity(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);
        this.items = NonNullList.withSize(3, ItemStack.EMPTY);
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
    protected @NotNull Component getDefaultName() {
        return Component.translatable("container.createmissiles.launch_pad_controller");
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
