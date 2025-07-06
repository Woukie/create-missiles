package net.woukie.createmissiles.registry;

import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.util.entry.ItemEntry;
import net.minecraft.world.item.CreativeModeTabs;
import net.woukie.createmissiles.CreateMissiles;
import net.woukie.createmissiles.item.assembly.AssemblyItem;

public class Items {
    private static final CreateRegistrate REGISTRATE = CreateMissiles.registrate();

    public static ItemEntry<AssemblyItem> WARHEAD_ASSEMBLY = REGISTRATE
            .item(CreateMissiles.MOD_ID, "warhead_assembly", AssemblyItem::new)
            .removeTab(CreativeModeTabs.SEARCH)
            .register();

    public static ItemEntry<AssemblyItem> CHASSIS_ASSEMBLY = REGISTRATE
            .item(CreateMissiles.MOD_ID, "chassis_assembly", AssemblyItem::new)
            .removeTab(CreativeModeTabs.SEARCH)
            .register();

    public static ItemEntry<AssemblyItem> THRUSTER_ASSEMBLY = REGISTRATE
            .item(CreateMissiles.MOD_ID, "thruster_assembly", AssemblyItem::new)
            .removeTab(CreativeModeTabs.SEARCH)
            .register();

    public static void init() {
        CreateMissiles.LOGGER.info("Registering items for " + CreateMissiles.NAME);
    }
}
