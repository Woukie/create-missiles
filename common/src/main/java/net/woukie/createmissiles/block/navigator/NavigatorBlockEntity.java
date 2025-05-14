package net.woukie.createmissiles.block.navigator;

import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.woukie.createmissiles.block.MissileAbstractBlockEntity;
import net.woukie.createmissiles.block.schematicator.SchematicatorMenu;
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

    @Override
    protected @NotNull Component getDefaultName() {
        return Component.translatable("container.createmissiles.launch_pad_controller");
    }

    @Override
    protected @NotNull AbstractContainerMenu createMenu(int id, @NotNull Inventory playerInventory) {
        return new SchematicatorMenu(id, playerInventory, this);
    }

    @Override
    public boolean canPlaceItem(int i, @NotNull ItemStack itemStack) {
        return i == 0 && itemStack.is(Items.FILLED_MAP);
    }
}
