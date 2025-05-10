package net.woukie.createmissiles.missilemanager.parts.warheads;

import net.minecraft.resources.ResourceLocation;
import net.woukie.createmissiles.missilemanager.parts.Warhead;

import java.util.HashMap;
import java.util.function.Supplier;

public class WarheadRegistry {
    private static HashMap<ResourceLocation, Supplier<Warhead>> warheads = new HashMap<>();

    public static void register(Supplier<Warhead> constructor) {
        WarheadRegistry.warheads.put(constructor.get().getResourceLocation(), constructor);
    }

    public static Supplier<Warhead> get(ResourceLocation location) {
        return warheads.get(location);
    }
}
