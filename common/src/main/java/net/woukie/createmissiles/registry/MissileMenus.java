package net.woukie.createmissiles.registry;

import dev.architectury.registry.registries.Registrar;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import net.woukie.createmissiles.CreateMissiles;
import net.woukie.createmissiles.block.launchpadcontroller.LaunchPadControllerMenu;

import static net.woukie.createmissiles.CreateMissiles.MANAGER;
import static net.woukie.createmissiles.CreateMissiles.MOD_ID;

public class MissileMenus {
    public static final Registrar<MenuType<?>> MENUS = MANAGER.get().get(Registries.MENU);

    public static final RegistrySupplier<MenuType<LaunchPadControllerMenu>> LAUNCH_PAD_CONTROLLER = MENUS.register(
            new ResourceLocation(MOD_ID, "launch_pad_controller"),
            () -> new MenuType<>(LaunchPadControllerMenu::new, FeatureFlags.VANILLA_SET)
    );

    public static void init() {
        CreateMissiles.LOGGER.info("Registering menus for " + CreateMissiles.NAME);
    }
}
