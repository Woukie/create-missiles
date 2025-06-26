package net.woukie.createmissiles.fabric;

import dev.architectury.registry.menu.MenuRegistry;
import net.fabricmc.api.ClientModInitializer;
import net.woukie.createmissiles.block.controller.ControllerScreen;
import net.woukie.createmissiles.block.navigator.NavigatorScreen;
import net.woukie.createmissiles.block.schematicator.SchematicatorScreen;
import net.woukie.createmissiles.registry.EntityRenderers;
import net.woukie.createmissiles.registry.Menus;

public class CreateMissilesClientFabric implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        MenuRegistry.registerScreenFactory(Menus.CONTROLLER.get(), ControllerScreen::new);
        MenuRegistry.registerScreenFactory(Menus.SCHEMATICATOR.get(), SchematicatorScreen::new);
        MenuRegistry.registerScreenFactory(Menus.NAVIGATOR.get(), NavigatorScreen::new);

        EntityRenderers.init();
    }
}
