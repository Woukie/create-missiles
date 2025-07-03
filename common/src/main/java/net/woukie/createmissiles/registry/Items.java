package net.woukie.createmissiles.registry;

import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.util.entry.ItemEntry;
import net.minecraft.world.item.CreativeModeTabs;
import net.woukie.createmissiles.CreateMissiles;
import net.woukie.createmissiles.item.assembly.ChassisAssembly;
import net.woukie.createmissiles.item.assembly.ThrusterAssembly;
import net.woukie.createmissiles.item.assembly.WarheadAssembly;

public class Items {
    private static final CreateRegistrate REGISTRATE = CreateMissiles.registrate();

    public static ItemEntry<WarheadAssembly> WARHEAD_ASSEMBLY = REGISTRATE
            .item(CreateMissiles.MOD_ID, "warhead_assembly", WarheadAssembly::new)
            .removeTab(CreativeModeTabs.SEARCH)
            .register();

    public static ItemEntry<ChassisAssembly> CHASSIS_ASSEMBLY = REGISTRATE
            .item(CreateMissiles.MOD_ID, "chassis_assembly", ChassisAssembly::new)
            .removeTab(CreativeModeTabs.SEARCH)
            .register();

    public static ItemEntry<ThrusterAssembly> THRUSTER_ASSEMBLY = REGISTRATE
            .item(CreateMissiles.MOD_ID, "thruster_assembly", ThrusterAssembly::new)
            .removeTab(CreativeModeTabs.SEARCH)
            .register();

    public static void init() {
        CreateMissiles.LOGGER.info("Registering items for " + CreateMissiles.NAME);
    }
}
