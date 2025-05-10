package net.woukie.createmissiles.missilemanager.parts.chassis;

import net.minecraft.resources.ResourceLocation;
import net.woukie.createmissiles.missilemanager.parts.Chassis;

import java.util.HashMap;
import java.util.function.Supplier;

public class ChassisRegistry {
    private static HashMap<ResourceLocation, Supplier<Chassis>> chassis = new HashMap<>();

    public static void register(Supplier<Chassis> constructor) {
        ChassisRegistry.chassis.put(constructor.get().getResourceLocation(), constructor);
    }

    public static Supplier<Chassis> get(ResourceLocation location) {
        return chassis.get(location);
    }
}
