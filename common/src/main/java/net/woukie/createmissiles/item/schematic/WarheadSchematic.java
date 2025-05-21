package net.woukie.createmissiles.item.schematic;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.woukie.createmissiles.missilemanager.parts.PartTypeRegistry;
import net.woukie.createmissiles.missilemanager.parts.WarheadType;
import net.woukie.createmissiles.registry.MissileItems;
import org.jetbrains.annotations.NotNull;

public class WarheadSchematic extends Item {
    public WarheadSchematic(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull Component getName(ItemStack itemStack) {
        CompoundTag compoundTag = itemStack.getTag();
        if (compoundTag != null)
            return PartTypeRegistry.getWarhead(new ResourceLocation(compoundTag.getString("Warhead"))).displayName;

        return super.getName(itemStack);
    }

    public static ItemStack createWith(ResourceLocation warhead) {
        ItemStack itemStack = new ItemStack(MissileItems.WARHEAD_SCHEMATIC.get());
        CompoundTag compoundTag = itemStack.getOrCreateTag();
        compoundTag.putString("Warhead", warhead.toString());
        itemStack.setTag(compoundTag);
        return itemStack;
    }

    public static WarheadType getWarhead(ItemStack itemStack) {
        CompoundTag compoundTag = itemStack.getTag();
        if (compoundTag == null)
            return null;
        return PartTypeRegistry.getWarhead(new ResourceLocation(compoundTag.getString("Warhead")));
    }
}
