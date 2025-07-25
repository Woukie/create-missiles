package net.woukie.createmissiles.registry;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.woukie.createmissiles.CreateMissiles;
import net.woukie.createmissiles.missilemanager.parts.MissilePartType;
import net.woukie.createmissiles.missilemanager.parts.chassis.FireworkChassis;
import net.woukie.createmissiles.missilemanager.parts.thrusters.FireworkThruster;
import net.woukie.createmissiles.missilemanager.parts.warheads.FireworkWarhead;
import net.woukie.createmissiles.missilemanager.parts.warheads.ShulkerBoxWarhead;
import net.woukie.createmissiles.missilemanager.parts.warheads.TeleportationWarhead;

import java.util.HashMap;

public class PartTypes {
    private static final HashMap<ResourceLocation, MissilePartType> missilePartTypes = new HashMap<>();

    public static void register(MissilePartType missileType) {
        PartTypes.missilePartTypes.put(missileType.getResourceLocation(), missileType);
    }

    public static MissilePartType get(ResourceLocation location) {
        return missilePartTypes.get(location);
    }

    public static MissilePartType get(ItemStack itemStack) {
        if (itemStack == null || itemStack.isEmpty()) return null;
        CompoundTag compoundTag = itemStack.getTag();
        if (compoundTag == null) return null;
        return PartTypes.get(new ResourceLocation(compoundTag.getString("PartType")));
    }

    public static void init() {
        CreateMissiles.LOGGER.info("Registering missile part types for " + CreateMissiles.NAME);

        PartTypes.register(new FireworkWarhead());
        PartTypes.register(new ShulkerBoxWarhead());
        PartTypes.register(new TeleportationWarhead());
        PartTypes.register(new FireworkChassis());
        PartTypes.register(new FireworkThruster());

    }
}
