package net.woukie.createmissiles.registry;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.woukie.createmissiles.CreateMissiles;
import net.woukie.createmissiles.entity.drone.Drone;
import net.woukie.createmissiles.entity.drone.ReinforcedDrone;
import net.woukie.createmissiles.item.BiomeVialItem;
import net.woukie.createmissiles.item.BoundEnderPearlItem;
import net.woukie.createmissiles.item.DroneBoxItem;
import net.woukie.createmissiles.item.assembly.AssemblyItem;

public abstract class Items {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(BuiltInRegistries.ITEM, CreateMissiles.MOD_ID);

    public static final DeferredHolder<Item, AssemblyItem> WARHEAD_ASSEMBLY = ITEMS.register("warhead_assembly", () -> new AssemblyItem(new Item.Properties()));
    public static final DeferredHolder<Item, AssemblyItem> CHASSIS_ASSEMBLY = ITEMS.register("chassis_assembly", () -> new AssemblyItem(new Item.Properties()));
    public static final DeferredHolder<Item, AssemblyItem> THRUSTER_ASSEMBLY = ITEMS.register("thruster_assembly", () -> new AssemblyItem(new Item.Properties()));

    public static final DeferredHolder<Item, BoundEnderPearlItem> BOUND_ENDER_PEARL = ITEMS.register(
            "bound_ender_pearl",
            () -> new BoundEnderPearlItem(new Item.Properties())
    );
    public static final DeferredHolder<Item, DroneBoxItem> DRONE_BOX_ITEM = ITEMS.register("basic_drone_box", () -> new DroneBoxItem(new Item.Properties(), level -> new Drone(EntityTypes.BASIC_DRONE.get(), level)));
    public static final DeferredHolder<Item, DroneBoxItem> REINFORCED_DRONE_BOX = ITEMS.register("reinforced_drone_box", () -> new DroneBoxItem(new Item.Properties(), level -> new ReinforcedDrone(EntityTypes.REINFORCED_DRONE.get(), level)));
    public static final DeferredHolder<Item, Item> DRAGON_EGG_SHELL = ITEMS.register(
            "dragon_egg_shell",
            () -> new Item(new Item.Properties())
    );
    public static final DeferredHolder<Item, BiomeVialItem> BIOME_VIAL = ITEMS.register(
            "biome_vial",
            () -> new BiomeVialItem(new BiomeVialItem.Properties())
    );
    public static final DeferredHolder<Item, Item> REINFORCED_DRAGON_EGG_SHELL = ITEMS.register(
            "reinforced_dragon_egg_shell",
            () -> new Item(new Item.Properties())
    );
    public static final DeferredHolder<Item, Item> INCOMPLETE_BASIC_DRONE_BOX = ITEMS.register(
            "incomplete_basic_drone_box",
            () -> new Item(new Item.Properties())
    );
    public static final DeferredHolder<Item, Item> INCOMPLETE_REINFORCED_DRONE_BOX = ITEMS.register(
            "incomplete_reinforced_drone_box",
            () -> new Item(new Item.Properties())
    );
    public static final DeferredHolder<Item, Item> FIREWORK_UPGRADE_CORE = ITEMS.register(
            "firework_upgrade_core",
            () -> new Item(new Item.Properties())
    );
    public static final DeferredHolder<Item, Item> FLAMING_UPGRADE_CORE = ITEMS.register(
            "flaming_upgrade_core",
            () -> new Item(new Item.Properties())
    );
    public static final DeferredHolder<Item, Item> EXCAVATOR_UPGRADE_CORE = ITEMS.register(
            "excavator_upgrade_core",
            () -> new Item(new Item.Properties())
    );
    public static final DeferredHolder<Item, Item> FROST_UPGRADE_CORE = ITEMS.register(
            "frost_upgrade_core",
            () -> new Item(new Item.Properties())
    );

    public static void init() {
        CreateMissiles.LOGGER.info("Registering items for " + CreateMissiles.NAME);
        ITEMS.register(NeoForge.EVENT_BUS);
    }
}
