package net.woukie.createmissiles.registry;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.woukie.createmissiles.missilemanager.parts.MissilePartType;

import java.util.HashMap;

public class PartTypes {
    private static final HashMap<ResourceLocation, MissilePartType> missilePartTypes = new HashMap<>();

    public static void register(MissilePartType missileType) {
        PartTypes.missilePartTypes.put(missileType.resourceLocation, missileType);
    }

    public static MissilePartType get(ResourceLocation location) {
        return missilePartTypes.get(location);
    }

    public static MissilePartType get(ItemStack itemStack) {
        if (itemStack == null) return null;
        CompoundTag compoundTag = itemStack.getTag();
        if (compoundTag == null)
            return null;
        return PartTypes.get(new ResourceLocation(compoundTag.getString("PartType")));
    }
}
