package net.woukie.createmissiles.missilemanager.parts;

import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;

public class PartTypeRegistry {
    private static final HashMap<ResourceLocation, WarheadType> warheadTypes = new HashMap<>();
    private static final HashMap<ResourceLocation, ChassisType> chassisTypes = new HashMap<>();
    private static final HashMap<ResourceLocation, ThrusterType> thrusterTypes = new HashMap<>();

    public static void registerWarhead(WarheadType warheadType) {
        PartTypeRegistry.warheadTypes.put(warheadType.resourceLocation, warheadType);
    }

    public static WarheadType getWarhead(ResourceLocation location) {
        return warheadTypes.get(location);
    }

    public static void registerChassis(ChassisType chassisType) {
        PartTypeRegistry.chassisTypes.put(chassisType.resourceLocation, chassisType);
    }

    public static ChassisType getChassis(ResourceLocation location) {
        return chassisTypes.get(location);
    }

    public static void registerThruster(ThrusterType thrusterType) {
        PartTypeRegistry.thrusterTypes.put(thrusterType.resourceLocation, thrusterType);
    }

    public static ThrusterType getThruster(ResourceLocation location) {
        return thrusterTypes.get(location);
    }
}
