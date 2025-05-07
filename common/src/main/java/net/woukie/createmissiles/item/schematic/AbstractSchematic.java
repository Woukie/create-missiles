package net.woukie.createmissiles.item.schematic;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public abstract class AbstractSchematic extends Item {
    public AbstractSchematic(Properties properties) {
        super(properties);
    }

    public boolean isFoil(ItemStack itemStack) {
        return true;
    }

    public boolean isEnchantable(ItemStack itemStack) {
        return false;
    }
}
