package net.woukie.createmissiles.item.assembly;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.woukie.createmissiles.registry.Items;
import net.woukie.createmissiles.registry.PartTypes;
import org.jetbrains.annotations.NotNull;

public class AssemblyItem extends Item {
    public AssemblyItem(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull Component getName(ItemStack itemStack) {
        CompoundTag compoundTag = itemStack.getTag();
        if (compoundTag != null) {
            var type = PartTypes.get(new ResourceLocation(compoundTag.getString("PartType")));
            if (type == null) return Component.translatable("item.createmissiles.assembly_invalid");
            return type.displayName;
        }

        return super.getName(itemStack);
    }

    public static ItemStack createWith(ResourceLocation chassis, ItemLike item) {
        ItemStack itemStack = new ItemStack(item);
        CompoundTag compoundTag = itemStack.getOrCreateTag();
        compoundTag.putString("PartType", chassis.toString());
        itemStack.setTag(compoundTag);
        return itemStack;
    }
}
