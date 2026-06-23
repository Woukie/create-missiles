package net.woukie.createmissiles;

import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.world.item.Item;
import net.woukie.createmissiles.registry.Items;

public class ForgeItems extends Items {
    @Override
    protected RegistrySupplier<Item> getWarheadAssembly() {
        return ITEMS.register(
                "warhead_assembly",
                () -> new ForgeAssemblyItem(new Item.Properties())
        );
    }

    @Override
    protected RegistrySupplier<Item> getChassisAssembly() {
        return ITEMS.register(
                "chassis_assembly",
                () -> new ForgeAssemblyItem(new Item.Properties())
        );
    }

    @Override
    protected RegistrySupplier<Item> getThrusterAssembly() {
        return ITEMS.register(
                "thruster_assembly",
                () -> new ForgeAssemblyItem(new Item.Properties())
        );
    }

    static {

    }
}
