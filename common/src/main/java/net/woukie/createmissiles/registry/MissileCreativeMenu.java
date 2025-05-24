package net.woukie.createmissiles.registry;

import dev.architectury.registry.CreativeTabRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.woukie.createmissiles.CreateMissiles;

public class MissileCreativeMenu {
    public static final DeferredRegister<CreativeModeTab> TABS =
            DeferredRegister.create(CreateMissiles.MOD_ID, Registries.CREATIVE_MODE_TAB);

    public static final RegistrySupplier<CreativeModeTab> SCHEMATICS_TAB = TABS.register(
            "createmissiles",
            () -> CreativeTabRegistry.create(
                    Component.translatable("category.createmissiles"),
                    () -> new ItemStack(MissileBlocks.CONTROLLER.get())
            )
    );

    public static void init() {
        CreateMissiles.LOGGER.info("Registering creative menus for " + CreateMissiles.NAME);

        TABS.register();
    }
}
