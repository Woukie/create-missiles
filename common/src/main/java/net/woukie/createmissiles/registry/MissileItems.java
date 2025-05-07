package net.woukie.createmissiles.registry;

import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.util.entry.ItemEntry;
import net.woukie.createmissiles.CreateMissiles;
import net.woukie.createmissiles.item.schematic.ChassisSchematic;
import net.woukie.createmissiles.item.schematic.ThrusterSchematic;
import net.woukie.createmissiles.item.schematic.WarheadSchematic;

public class MissileItems {
    private static final CreateRegistrate REGISTRATE = CreateMissiles.registrate();

    public static ItemEntry<WarheadSchematic> WARHEAD_SCHEMATIC = REGISTRATE
            .item(CreateMissiles.MOD_ID, "warhead_schematic", WarheadSchematic::new)
            .register();

    public static ItemEntry<ChassisSchematic> CHASSIS_SCHEMATIC = REGISTRATE
            .item(CreateMissiles.MOD_ID, "chassis_schematic", ChassisSchematic::new)
            .register();

    public static ItemEntry<ThrusterSchematic> THRUSTER_SCHEMATIC = REGISTRATE
            .item(CreateMissiles.MOD_ID, "thruster_schematic",ThrusterSchematic::new)
            .register();

    public static void init() {
        CreateMissiles.LOGGER.info("Registering items for " + CreateMissiles.NAME);
    }
}
