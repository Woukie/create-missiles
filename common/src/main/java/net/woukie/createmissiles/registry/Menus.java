package net.woukie.createmissiles.registry;

import dev.architectury.registry.registries.Registrar;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import net.woukie.createmissiles.CreateMissiles;
import net.woukie.createmissiles.block.controller.ControllerMenu;
import net.woukie.createmissiles.block.navigator.NavigatorMenu;
import net.woukie.createmissiles.block.schematicator.SchematicatorMenu;

import static net.woukie.createmissiles.CreateMissiles.MANAGER;
import static net.woukie.createmissiles.CreateMissiles.MOD_ID;

public class Menus {
    public static final Registrar<MenuType<?>> MENUS = MANAGER.get().get(Registries.MENU);

    public static final RegistrySupplier<MenuType<ControllerMenu>> CONTROLLER = MENUS.register(
            new ResourceLocation(MOD_ID, "controller"),
            () -> new MenuType<>(ControllerMenu::new, FeatureFlags.VANILLA_SET)
    );

    public static final RegistrySupplier<MenuType<SchematicatorMenu>> SCHEMATICATOR = MENUS.register(
            new ResourceLocation(MOD_ID, "schematicator"),
            () -> new MenuType<>(SchematicatorMenu::new, FeatureFlags.VANILLA_SET)
    );

    public static final RegistrySupplier<MenuType<NavigatorMenu>> NAVIGATOR = MENUS.register(
            new ResourceLocation(MOD_ID, "navigator"),
            () -> new MenuType<>(NavigatorMenu::new, FeatureFlags.VANILLA_SET)
    );

    public static void init() {
        CreateMissiles.LOGGER.info("Registering menus for " + CreateMissiles.NAME);
    }
}
