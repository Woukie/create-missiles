package net.woukie.createmissiles.item.assembly;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.woukie.createmissiles.registry.PartTypes;
import net.woukie.createmissiles.registry.Items;
import org.jetbrains.annotations.NotNull;

public class ChassisAssembly extends Item {
    public ChassisAssembly(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull Component getName(ItemStack itemStack) {
        CompoundTag compoundTag = itemStack.getTag();
        if (compoundTag != null)
            return PartTypes.get(new ResourceLocation(compoundTag.getString("PartType"))).displayName;

        return super.getName(itemStack);
    }

    public static ItemStack createWith(ResourceLocation chassis) {
        ItemStack itemStack = new ItemStack(Items.CHASSIS_ASSEMBLY.get());
        CompoundTag compoundTag = itemStack.getOrCreateTag();
        compoundTag.putString("PartType", chassis.toString());
        itemStack.setTag(compoundTag);
        return itemStack;
    }
}
