package net.woukie.createmissiles.missilemanager.parts;

import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;

public class PartRegistry {
    private static final HashMap<ResourceLocation, Warhead> warheads = new HashMap<>();
    private static final HashMap<ResourceLocation, Chassis> chassis = new HashMap<>();
    private static final HashMap<ResourceLocation, Thruster> thrusters = new HashMap<>();

    public static void registerWarhead(Warhead warhead) {
        PartRegistry.warheads.put(warhead.resourceLocation, warhead);
    }

    public static Warhead getWarhead(ResourceLocation location) {
        return warheads.get(location);
    }

    public static void registerChassis(Chassis chassis) {
        PartRegistry.chassis.put(chassis.resourceLocation, chassis);
    }

    public static Chassis getChassis(ResourceLocation location) {
        return chassis.get(location);
    }

    public static void registerThruster(Thruster thruster) {
        PartRegistry.thrusters.put(thruster.resourceLocation, thruster);
    }

    public static Thruster getThruster(ResourceLocation location) {
        return thrusters.get(location);
    }
}
