package net.woukie.createmissiles.missilemanager.parts.thrusters;

import net.minecraft.resources.ResourceLocation;
import net.woukie.createmissiles.missilemanager.parts.Thruster;

import java.util.HashMap;
import java.util.function.Supplier;

public class ThrusterRegistry {
    private static HashMap<ResourceLocation, Supplier<Thruster>> thrusters = new HashMap<>();

    public static void register(Supplier<Thruster> constructor) {
        ThrusterRegistry.thrusters.put(constructor.get().getResourceLocation(), constructor);
    }

    public static Supplier<Thruster> get(ResourceLocation location) {
        return thrusters.get(location);
    }
}
