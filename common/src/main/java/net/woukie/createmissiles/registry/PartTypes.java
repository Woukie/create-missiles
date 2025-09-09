package net.woukie.createmissiles.registry;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.woukie.createmissiles.CreateMissiles;
import net.woukie.createmissiles.missilemanager.parts.MissilePartType;
import net.woukie.createmissiles.missilemanager.parts.chassis.DragonChassis;
import net.woukie.createmissiles.missilemanager.parts.chassis.ExcavatorChassis;
import net.woukie.createmissiles.missilemanager.parts.chassis.FireworkChassis;
import net.woukie.createmissiles.missilemanager.parts.chassis.FlamingChassis;
import net.woukie.createmissiles.missilemanager.parts.thrusters.DragonThruster;
import net.woukie.createmissiles.missilemanager.parts.thrusters.ExcavatorThruster;
import net.woukie.createmissiles.missilemanager.parts.thrusters.FireworkThruster;
import net.woukie.createmissiles.missilemanager.parts.thrusters.FlamingThruster;
import net.woukie.createmissiles.missilemanager.parts.warheads.*;

import java.util.Collection;
import java.util.HashMap;

public class PartTypes {
    private static final HashMap<ResourceLocation, MissilePartType> missilePartTypes = new HashMap<>();

    public static void register(MissilePartType missileType) {
        PartTypes.missilePartTypes.put(missileType.getResourceLocation(), missileType);
    }

    public static Collection<MissilePartType> getMissilePartTypes() {
        return missilePartTypes.values();
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

        PartTypes.register(new GuardianWarhead());
        PartTypes.register(new TunnelerWarhead());
        PartTypes.register(new BiomeBrushWarhead());
        PartTypes.register(new AncientWarhead());
        PartTypes.register(new MessyWarhead());
        PartTypes.register(new WitheredWarhead());
        PartTypes.register(new FrostWarhead());
        PartTypes.register(new FrozenWarhead());
        PartTypes.register(new AnnoyingWarhead());
        PartTypes.register(new DirectHitWarhead());
        PartTypes.register(new DragonWarhead());
        PartTypes.register(new FlamingWarhead());
        PartTypes.register(new BlazingWarhead());
        PartTypes.register(new InfernalWarhead());
        PartTypes.register(new FireworkWarhead());
        PartTypes.register(new ShulkerBoxWarhead());
        PartTypes.register(new TeleportationWarhead());
        PartTypes.register(new ExcavatorWarhead());
        PartTypes.register(new DragonChassis());
        PartTypes.register(new FireworkChassis());
        PartTypes.register(new FlamingChassis());
        PartTypes.register(new ExcavatorChassis());
        PartTypes.register(new DragonThruster());
        PartTypes.register(new FireworkThruster());
        PartTypes.register(new FlamingThruster());
        PartTypes.register(new ExcavatorThruster());
    }
}
