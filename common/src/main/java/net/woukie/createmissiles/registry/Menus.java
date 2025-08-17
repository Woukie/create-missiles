package net.woukie.createmissiles.registry;

import dev.architectury.registry.registries.Registrar;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import net.woukie.createmissiles.CreateMissiles;
import net.woukie.createmissiles.inventory.ControlPanelMenu;
import net.woukie.createmissiles.inventory.NavigationPanelMenu;
import net.woukie.createmissiles.inventory.AssemblyPanelMenu;

import static net.woukie.createmissiles.CreateMissiles.MANAGER;
import static net.woukie.createmissiles.CreateMissiles.MOD_ID;

public class Menus {
    public static final Registrar<MenuType<?>> MENUS = MANAGER.get().get(Registries.MENU);

    public static final RegistrySupplier<MenuType<ControlPanelMenu>> CONTROL_PANEL = MENUS.register(
            new ResourceLocation(MOD_ID, "control_panel"),
            () -> new MenuType<>(ControlPanelMenu::new, FeatureFlags.VANILLA_SET)
    );

    public static final RegistrySupplier<MenuType<AssemblyPanelMenu>> ASSEMBLY_PANEL = MENUS.register(
            new ResourceLocation(MOD_ID, "assembly_panel"),
            () -> new MenuType<>(AssemblyPanelMenu::new, FeatureFlags.VANILLA_SET)
    );

    public static final RegistrySupplier<MenuType<NavigationPanelMenu>> NAVIGATION_PANEL = MENUS.register(
            new ResourceLocation(MOD_ID, "navigation_panel"),
            () -> new MenuType<>(NavigationPanelMenu::new, FeatureFlags.VANILLA_SET)
    );

    public static void init() {
        CreateMissiles.LOGGER.info("Registering menus for " + CreateMissiles.NAME);
    }
}
