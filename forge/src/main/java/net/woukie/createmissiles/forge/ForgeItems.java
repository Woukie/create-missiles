package net.woukie.createmissiles.forge;

import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.world.item.Item;
import net.woukie.createmissiles.item.assembly.AssemblyItem;
import net.woukie.createmissiles.registry.Items;

public class ForgeItems extends Items {
    @Override
    protected RegistrySupplier<Item> getWarheadAssembly() {
        return ITEMS.register(
                "warhead_assembly",
                () -> new AssemblyItem(new Item.Properties())
        );
    }

    @Override
    protected RegistrySupplier<Item> getChassisAssembly() {
        return ITEMS.register(
                "chassis_assembly",
                () -> new AssemblyItem(new Item.Properties())
        );
    }

    @Override
    protected RegistrySupplier<Item> getThrusterAssembly() {
        return ITEMS.register(
                "thruster_assembly",
                () -> new AssemblyItem(new Item.Properties())
        );
    }
}
