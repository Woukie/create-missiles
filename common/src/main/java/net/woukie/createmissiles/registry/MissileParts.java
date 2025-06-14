package net.woukie.createmissiles.registry;

import dev.architectury.registry.CreativeTabRegistry;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.woukie.createmissiles.CreateMissiles;
import net.woukie.createmissiles.item.schematic.ChassisSchematic;
import net.woukie.createmissiles.item.schematic.ThrusterSchematic;
import net.woukie.createmissiles.item.schematic.WarheadSchematic;
import net.woukie.createmissiles.missilemanager.parts.*;

import java.util.ArrayList;

public class MissileParts {
    public static void init() {
        CreateMissiles.LOGGER.info("Registering missile parts for " + CreateMissiles.NAME);

        MissilePartTypes.register(new WarheadType(
                Component.translatable("warheads.createmissiles.tnt_warhead"),
                id("tnt_warhead"),
                null,
                10,
                null,
                new ArrayList<>()
        ));

        MissilePartTypes.register(new ChassisType(
                Component.translatable("chassis.createmissiles.paper_chassis"),
                id("paper_chassis"),
                null,
                1,
                new ArrayList<>()
        ));

        MissilePartTypes.register(new ThrusterType(
                Component.translatable("thrusters.createmissiles.gunpowder_thruster"),
                id("gunpowder_thruster"),
                null,
                1,
                10,
                new ArrayList<>()
        ));

        CreativeTabRegistry.appendStack(MissileCreativeMenu.SCHEMATICS_TAB, () -> WarheadSchematic.createWith(id("tnt_warhead")));
        CreativeTabRegistry.appendStack(MissileCreativeMenu.SCHEMATICS_TAB, () -> ChassisSchematic.createWith(id("paper_chassis")));
        CreativeTabRegistry.appendStack(MissileCreativeMenu.SCHEMATICS_TAB, () -> ThrusterSchematic.createWith(id("gunpowder_thruster")));
    }

    private static ResourceLocation id(String id) {
        return new ResourceLocation(CreateMissiles.MOD_ID, id);
    }
}
