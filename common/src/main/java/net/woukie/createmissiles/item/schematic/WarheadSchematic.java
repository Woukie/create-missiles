package net.woukie.createmissiles.item.schematic;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.MendingEnchantment;

public class WarheadSchematic extends AbstractSchematic {
    public WarheadSchematic(Properties properties) {
        super(properties);
    }

    public static ListTag getEnchantments(ItemStack itemStack) {
        CompoundTag compoundTag = itemStack.getTag();
        return compoundTag != null ? compoundTag.getList("StoredEnchantments", 10) : new ListTag();
    }
}
