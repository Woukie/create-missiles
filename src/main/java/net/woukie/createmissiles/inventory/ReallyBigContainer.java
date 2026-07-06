package net.woukie.createmissiles.inventory;

import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;

public class ReallyBigContainer extends SimpleContainer {
    public ReallyBigContainer(int size) {
        super(size);
    }

    @Override
    public int getMaxStackSize() {
        return Integer.MAX_VALUE;
    }

    @Override
    public int getMaxStackSize(ItemStack stack) {
        return Integer.MAX_VALUE;
    }
}
