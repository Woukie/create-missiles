package net.woukie.createmissiles.item.schematic;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.woukie.createmissiles.registry.PartTypes;
import net.woukie.createmissiles.registry.Items;
import org.jetbrains.annotations.NotNull;

public class ThrusterSchematic extends Item {
    public ThrusterSchematic(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull Component getName(ItemStack itemStack) {
        CompoundTag compoundTag = itemStack.getTag();
        if (compoundTag != null)
            return PartTypes.get(new ResourceLocation(compoundTag.getString("PartType"))).displayName;

        return super.getName(itemStack);
    }

    public static ItemStack createWith(ResourceLocation thruster) {
        ItemStack itemStack = new ItemStack(Items.THRUSTER_SCHEMATIC.get());
        CompoundTag compoundTag = itemStack.getOrCreateTag();
        compoundTag.putString("PartType", thruster.toString());
        itemStack.setTag(compoundTag);
        return itemStack;
    }
}
