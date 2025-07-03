package net.woukie.createmissiles.registry;

import dev.architectury.registry.CreativeTabRegistry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.woukie.createmissiles.CreateMissiles;
import net.woukie.createmissiles.client.models.GunpowderThrusterModel;
import net.woukie.createmissiles.client.models.PaperChassisModel;
import net.woukie.createmissiles.client.models.TntWarheadModel;
import net.woukie.createmissiles.item.assembly.ChassisAssembly;
import net.woukie.createmissiles.item.assembly.ThrusterAssembly;
import net.woukie.createmissiles.item.assembly.WarheadAssembly;
import net.woukie.createmissiles.missilemanager.parts.ChassisType;
import net.woukie.createmissiles.missilemanager.parts.MissilePartType;
import net.woukie.createmissiles.missilemanager.parts.ThrusterType;
import net.woukie.createmissiles.missilemanager.parts.WarheadType;

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
        if (itemStack == null || itemStack.isEmpty()) return null;
        CompoundTag compoundTag = itemStack.getTag();
        if (compoundTag == null) return null;
        return PartTypes.get(new ResourceLocation(compoundTag.getString("PartType")));
    }

    public static void init() {
        CreateMissiles.LOGGER.info("Registering missile part types for " + CreateMissiles.NAME);

        PartTypes.register(new WarheadType(
                Component.translatable("warheads.createmissiles.tnt_warhead"),
                id("tnt_warhead"),
                null,
                10,
                (trajectory, server) -> {
                    var impactPos = trajectory.getPosition((float)trajectory.getImpactTime());
                    trajectory.getData().level.explode(null, impactPos.x, impactPos.y, impactPos.z, 10, Level.ExplosionInteraction.BLOCK);
                },
                new TntWarheadModel()
        ));

        PartTypes.register(new ChassisType(
                Component.translatable("chassis.createmissiles.paper_chassis"),
                id("paper_chassis"),
                null,
                1,
                new PaperChassisModel()
        ));

        PartTypes.register(new ThrusterType(
                Component.translatable("thrusters.createmissiles.gunpowder_thruster"),
                id("gunpowder_thruster"),
                null,
                1,
                10,
                new GunpowderThrusterModel()
        ));

        CreativeTabRegistry.appendStack(CreativeMenus.ASSEMBLIES_TAB, () -> WarheadAssembly.createWith(id("tnt_warhead")));
        CreativeTabRegistry.appendStack(CreativeMenus.ASSEMBLIES_TAB, () -> ChassisAssembly.createWith(id("paper_chassis")));
        CreativeTabRegistry.appendStack(CreativeMenus.ASSEMBLIES_TAB, () -> ThrusterAssembly.createWith(id("gunpowder_thruster")));
    }

    private static ResourceLocation id(String id) {
        return new ResourceLocation(CreateMissiles.MOD_ID, id);
    }

}
