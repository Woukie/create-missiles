package net.woukie.createmissiles.registry;

import net.minecraft.resources.ResourceLocation;
import net.woukie.createmissiles.missilemanager.parts.MissilePartType;

import java.util.HashMap;

public class MissilePartTypes {
    private static final HashMap<ResourceLocation, MissilePartType> missilePartTypes = new HashMap<>();

    public static void register(MissilePartType missileType) {
        MissilePartTypes.missilePartTypes.put(missileType.resourceLocation, missileType);
    }

    public static MissilePartType get(ResourceLocation location) {
        return missilePartTypes.get(location);
    }
}
