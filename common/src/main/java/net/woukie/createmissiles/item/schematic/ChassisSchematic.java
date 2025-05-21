package net.woukie.createmissiles.item.schematic;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.woukie.createmissiles.missilemanager.parts.ChassisType;
import net.woukie.createmissiles.missilemanager.parts.PartTypeRegistry;
import net.woukie.createmissiles.registry.MissileItems;
import org.jetbrains.annotations.NotNull;

public class ChassisSchematic extends Item {
    public ChassisSchematic(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull Component getName(ItemStack itemStack) {
        CompoundTag compoundTag = itemStack.getTag();
        if (compoundTag != null)
            return PartTypeRegistry.getChassis(new ResourceLocation(compoundTag.getString("Chassis"))).displayName;

        return super.getName(itemStack);
    }

    public static ItemStack createWith(ResourceLocation chassis) {
        ItemStack itemStack = new ItemStack(MissileItems.CHASSIS_SCHEMATIC.get());
        CompoundTag compoundTag = itemStack.getOrCreateTag();
        compoundTag.putString("Chassis", chassis.toString());
        itemStack.setTag(compoundTag);
        return itemStack;
    }

    public static ChassisType getChassis(ItemStack itemStack) {
        CompoundTag compoundTag = itemStack.getTag();
        if (compoundTag == null)
            return null;
        return PartTypeRegistry.getChassis(new ResourceLocation(compoundTag.getString("Chassis")));
    }
}
