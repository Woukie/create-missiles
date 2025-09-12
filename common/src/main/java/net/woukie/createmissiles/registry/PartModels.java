package net.woukie.createmissiles.registry;

import net.minecraft.resources.ResourceLocation;
import net.woukie.createmissiles.CreateMissiles;
import net.woukie.createmissiles.client.MissilePartModel;
import net.woukie.createmissiles.client.models.chassis.*;
import net.woukie.createmissiles.client.models.thrusters.*;
import net.woukie.createmissiles.client.models.warheads.*;

import java.util.HashMap;

// Can't store references to the model on server environments, so must have model getting be its own independent system
public class PartModels {
    private static final HashMap<ResourceLocation, MissilePartModel> models = new HashMap<>();

    public static MissilePartModel getModel(ResourceLocation location) {
        return models.get(location);
    }

    public static void registerModel(ResourceLocation location, MissilePartModel model) {
        models.putIfAbsent(location, model);
    }

    public static void init() {
        registerModel(id("guardian_warhead"), new GuardianWarheadModel());
        registerModel(id("tunneler_warhead"), new TunnelerWarheadModel());
        registerModel(id("biome_brush_warhead"), new BiomeBrushWarheadModel());
        registerModel(id("ancient_warhead"), new AncientWarheadModel());
        registerModel(id("messy_warhead"), new MessyWarheadModel());
        registerModel(id("withered_warhead"), new WitheredWarheadModel());
        registerModel(id("frost_warhead"), new FrostWarheadModel());
        registerModel(id("frozen_warhead"), new FrozenWarheadModel());
        registerModel(id("annoying_warhead"), new AnnoyingWarheadModel());
        registerModel(id("direct_hit_warhead"), new DirectHitWarheadModel());
        registerModel(id("dragon_warhead"), new DragonWarheadModel());
        registerModel(id("flaming_warhead"), new FlamingWarheadModel());
        registerModel(id("blazing_warhead"), new BlazingWarheadModel());
        registerModel(id("infernal_warhead"), new InfernalWarheadModel());
        registerModel(id("firework_warhead"), new FireworkWarheadModel());
        registerModel(id("shulker_box_warhead"), new ShulkerBoxWarheadModel());
        registerModel(id("teleportation_warhead"), new TeleportationWarheadModel());
        registerModel(id("excavator_warhead"), new ExcavatorWarheadModel());
        registerModel(id("frost_chassis"), new FrostChassisModel());
        registerModel(id("frozen_chassis"), new FrozenChassisModel());
        registerModel(id("withered_chassis"), new WitheredChassisModel());
        registerModel(id("ancient_chassis"), new AncientChassisModel());
        registerModel(id("dragon_chassis"), new DragonChassisModel());
        registerModel(id("firework_chassis"), new FireworkChassisModel());
        registerModel(id("flaming_chassis"), new FlamingChassisModel());
        registerModel(id("excavator_chassis"), new ExcavatorChassisModel());
        registerModel(id("frost_thruster"), new FrostThrusterModel());
        registerModel(id("frozen_thruster"), new FrozenThrusterModel());
        registerModel(id("withered_thruster"), new WitheredThrusterModel());
        registerModel(id("ancient_thruster"), new AncientThrusterModel());
        registerModel(id("dragon_thruster"), new DragonThrusterModel());
        registerModel(id("firework_thruster"), new FireworkThrusterModel());
        registerModel(id("flaming_thruster"), new FlamingThrusterModel());
        registerModel(id("excavator_thruster"), new ExcavatorThrusterModel());
    }

    private static ResourceLocation id(String id) {
        return new ResourceLocation(CreateMissiles.MOD_ID, id);
    }
}
