package net.woukie.createmissiles.registry;

import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.util.entry.ItemEntry;
import dev.architectury.registry.CreativeTabRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.woukie.createmissiles.CreateMissiles;
import net.woukie.createmissiles.item.BoundEnderPearlItem;
import net.woukie.createmissiles.item.assembly.AssemblyItem;

public class Items {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(CreateMissiles.MOD_ID, Registries.ITEM);

    private static final CreateRegistrate REGISTRATE = CreateMissiles.registrate();

    public static final RegistrySupplier<Item> WARHEAD_ASSEMBLY = ITEMS.register(
            "warhead_assembly",
            () -> new AssemblyItem(new Item.Properties())
    );

    public static final RegistrySupplier<Item> CHASSIS_ASSEMBLY = ITEMS.register(
            "chassis_assembly",
            () -> new AssemblyItem(new Item.Properties())
    );

    public static final RegistrySupplier<Item> THRUSTER_ASSEMBLY = ITEMS.register(
            "thruster_assembly",
            () -> new AssemblyItem(new Item.Properties())
    );

    public static final RegistrySupplier<Item> BOUND_ENDER_PEARL = ITEMS.register(
            "bound_ender_pearl",
            () -> new BoundEnderPearlItem(new Item.Properties())
    );

    public static void init() {
        CreateMissiles.LOGGER.info("Registering items for " + CreateMissiles.NAME);
        ITEMS.register();
    }
}
