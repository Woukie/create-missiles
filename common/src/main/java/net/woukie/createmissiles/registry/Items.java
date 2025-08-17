package net.woukie.createmissiles.registry;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.woukie.createmissiles.CreateMissiles;
import net.woukie.createmissiles.item.BoundEnderPearlItem;
import net.woukie.createmissiles.item.DroneItem;

import java.util.Optional;
import java.util.ServiceLoader;

public abstract class Items {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(CreateMissiles.MOD_ID, Registries.ITEM);

    public static final RegistrySupplier<Item> WARHEAD_ASSEMBLY;
    public static final RegistrySupplier<Item> CHASSIS_ASSEMBLY;
    public static final RegistrySupplier<Item> THRUSTER_ASSEMBLY;

    public static final RegistrySupplier<Item> BOUND_ENDER_PEARL = ITEMS.register(
            "bound_ender_pearl",
            () -> new BoundEnderPearlItem(new Item.Properties())
    );

    public static final RegistrySupplier<Item> DRONE_ITEM = ITEMS.register("drone", () -> new DroneItem(new Item.Properties()));

    public static final RegistrySupplier<Item> DRAGON_EGG_SHELL = ITEMS.register(
            "dragon_egg_shell",
            () -> new Item(new Item.Properties())
    );

    public static final RegistrySupplier<Item> REINFORCED_DRAGON_EGG_SHELL = ITEMS.register(
            "reinforced_dragon_egg_shell",
            () -> new Item(new Item.Properties())
    );

    protected abstract RegistrySupplier<Item> getWarheadAssembly();
    protected abstract RegistrySupplier<Item> getChassisAssembly();
    protected abstract RegistrySupplier<Item> getThrusterAssembly();

    public static void init() {
        CreateMissiles.LOGGER.info("Registering items for " + CreateMissiles.NAME);
        ITEMS.register();
    }

    static {
        Optional<Items> items = ServiceLoader.load(Items.class).findFirst();
        if (items.isPresent()) {
            WARHEAD_ASSEMBLY = items.get().getWarheadAssembly();
            CHASSIS_ASSEMBLY = items.get().getChassisAssembly();
            THRUSTER_ASSEMBLY = items.get().getThrusterAssembly();
        } else {
            WARHEAD_ASSEMBLY = null;
            CHASSIS_ASSEMBLY = null;
            THRUSTER_ASSEMBLY = null;
        }
    }
}
