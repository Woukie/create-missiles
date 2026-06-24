package net.woukie.createmissiles.registry;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.woukie.createmissiles.CreateMissiles;
import net.woukie.createmissiles.inventory.ControlPanelMenu;
import net.woukie.createmissiles.inventory.DroneMenu;
import net.woukie.createmissiles.inventory.NavigationPanelMenu;
import net.woukie.createmissiles.inventory.AssemblyPanelMenu;

import java.util.function.Supplier;

public class Menus {
    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(BuiltInRegistries.MENU, CreateMissiles.MOD_ID);

    public static final Supplier<MenuType<ControlPanelMenu>> CONTROL_PANEL = MENUS.register("control_panel", () -> new MenuType<>(ControlPanelMenu::new, FeatureFlags.VANILLA_SET));

    public static final Supplier<MenuType<AssemblyPanelMenu>> ASSEMBLY_PANEL = MENUS.register(
            "assembly_panel",
            () -> new MenuType<>(AssemblyPanelMenu::new, FeatureFlags.VANILLA_SET)
    );

    public static final Supplier<MenuType<NavigationPanelMenu>> NAVIGATION_PANEL = MENUS.register(
            "navigation_panel",
            () -> new MenuType<>(NavigationPanelMenu::new, FeatureFlags.VANILLA_SET)
    );

    public static final Supplier<MenuType<DroneMenu>> DRONE = MENUS.register(
            "drone",
            () -> new MenuType<>(DroneMenu::new, FeatureFlags.VANILLA_SET)
    );

    public static void init() {
        CreateMissiles.LOGGER.info("Registering menus for " + CreateMissiles.NAME);
    }
}
