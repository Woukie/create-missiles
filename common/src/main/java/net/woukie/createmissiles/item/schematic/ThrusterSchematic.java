package net.woukie.createmissiles.item.schematic;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.woukie.createmissiles.registry.MissilePartTypes;
import net.woukie.createmissiles.missilemanager.parts.ThrusterType;
import net.woukie.createmissiles.registry.MissileItems;
import org.jetbrains.annotations.NotNull;

public class ThrusterSchematic extends Item {
    public ThrusterSchematic(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull Component getName(ItemStack itemStack) {
        CompoundTag compoundTag = itemStack.getTag();
        if (compoundTag != null)
            return MissilePartTypes.get(new ResourceLocation(compoundTag.getString("Thruster"))).displayName;

        return super.getName(itemStack);
    }

    public static ItemStack createWith(ResourceLocation thruster) {
        ItemStack itemStack = new ItemStack(MissileItems.THRUSTER_SCHEMATIC.get());
        CompoundTag compoundTag = itemStack.getOrCreateTag();
        compoundTag.putString("Thruster", thruster.toString());
        itemStack.setTag(compoundTag);
        return itemStack;
    }

    public static ThrusterType getThruster(ItemStack itemStack) {
        if (itemStack == null) return null;
        CompoundTag compoundTag = itemStack.getTag();
        if (compoundTag == null)
            return null;
        return (ThrusterType) MissilePartTypes.get(new ResourceLocation(compoundTag.getString("Thruster")));
    }
}
