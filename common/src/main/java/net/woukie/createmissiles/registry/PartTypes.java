package net.woukie.createmissiles.registry;

import dev.architectury.registry.CreativeTabRegistry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.woukie.createmissiles.CreateMissiles;
import net.woukie.createmissiles.client.models.GunpowderThrusterModel;
import net.woukie.createmissiles.client.models.PaperChassisModel;
import net.woukie.createmissiles.client.models.TntWarheadModel;
import net.woukie.createmissiles.item.schematic.ChassisSchematic;
import net.woukie.createmissiles.item.schematic.ThrusterSchematic;
import net.woukie.createmissiles.item.schematic.WarheadSchematic;
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
        if (itemStack == null) return null;
        CompoundTag compoundTag = itemStack.getTag();
        if (compoundTag == null)
            return null;
        return PartTypes.get(new ResourceLocation(compoundTag.getString("PartType")));
    }

    public static void init() {
        CreateMissiles.LOGGER.info("Registering missile part types for " + CreateMissiles.NAME);

        PartTypes.register(new WarheadType(
                Component.translatable("warheads.createmissiles.tnt_warhead"),
                id("tnt_warhead"),
                null,
                10,
                null,
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

        CreativeTabRegistry.appendStack(CreativeMenus.SCHEMATICS_TAB, () -> WarheadSchematic.createWith(id("tnt_warhead")));
        CreativeTabRegistry.appendStack(CreativeMenus.SCHEMATICS_TAB, () -> ChassisSchematic.createWith(id("paper_chassis")));
        CreativeTabRegistry.appendStack(CreativeMenus.SCHEMATICS_TAB, () -> ThrusterSchematic.createWith(id("gunpowder_thruster")));
    }

    private static ResourceLocation id(String id) {
        return new ResourceLocation(CreateMissiles.MOD_ID, id);
    }

}
