package net.woukie.createmissiles.registry;

import dev.architectury.registry.CreativeTabRegistry;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.woukie.createmissiles.CreateMissiles;
import net.woukie.createmissiles.item.schematic.ChassisSchematic;
import net.woukie.createmissiles.item.schematic.ThrusterSchematic;
import net.woukie.createmissiles.item.schematic.WarheadSchematic;
import net.woukie.createmissiles.missilemanager.parts.*;

import java.util.List;

public class MissileParts {
    public static void init() {
        CreateMissiles.LOGGER.info("Registering missile parts for " + CreateMissiles.NAME);

        PartTypeRegistry.registerWarhead(new WarheadType(
                Component.translatable("warheads.createmissiles.tnt_warhead"),
                id("tnt_warhead"),
                null,
                List.of(
                        new Ingredient(List.of(Items.IRON_INGOT), null, null, 32, Component.literal("Iron Ingot")),
                        new Ingredient(List.of(Items.REDSTONE_BLOCK), null, null, 1, Component.literal("Redstone Block")),
                        new Ingredient(List.of(Items.TNT), null, null, 10, Component.literal("TNT"))
                ),
                10,
                null
        ));

        PartTypeRegistry.registerChassis(new ChassisType(
                Component.translatable("chassis.createmissiles.paper_chassis"),
                id("paper_chassis"),
                null,
                List.of(
                        new Ingredient(List.of(Items.IRON_INGOT), null, null, 64, Component.literal("Iron Ingot")),
                        new Ingredient(List.of(Items.PAPER), null, null, 64, Component.literal("Paper")),
                        new Ingredient(List.of(Items.COMPASS), null, null, 10, Component.literal("Compass"))
                ),
                1
        ));

        PartTypeRegistry.registerThruster(new ThrusterType(
                Component.translatable("thrusters.createmissiles.gunpowder_thruster"),
                id("gunpowder_thruster"),
                null,
                List.of(
                        new Ingredient(List.of(Items.BLAZE_ROD), null, null, 1, Component.literal("Blaze Rod"))
                ),
                1,
                10
        ));

        CreativeTabRegistry.appendStack(MissileCreativeMenu.SCHEMATICS_TAB, () -> WarheadSchematic.createWith(id("tnt_warhead")));
        CreativeTabRegistry.appendStack(MissileCreativeMenu.SCHEMATICS_TAB, () -> ChassisSchematic.createWith(id("paper_chassis")));
        CreativeTabRegistry.appendStack(MissileCreativeMenu.SCHEMATICS_TAB, () -> ThrusterSchematic.createWith(id("gunpowder_thruster")));
    }

    private static ResourceLocation id(String id) {
        return new ResourceLocation(CreateMissiles.MOD_ID, id);
    }
}
