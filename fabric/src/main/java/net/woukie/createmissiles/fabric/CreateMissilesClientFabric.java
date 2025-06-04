package net.woukie.createmissiles.fabric;

import dev.architectury.registry.menu.MenuRegistry;
import net.fabricmc.api.ClientModInitializer;
import net.woukie.createmissiles.block.controller.ControllerScreen;
import net.woukie.createmissiles.block.navigator.NavigatorScreen;
import net.woukie.createmissiles.block.schematicator.SchematicatorScreen;
import net.woukie.createmissiles.registry.MissileEntityRenderers;
import net.woukie.createmissiles.registry.MissileMenus;

public class CreateMissilesClientFabric implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        MenuRegistry.registerScreenFactory(MissileMenus.CONTROLLER.get(), ControllerScreen::new);
        MenuRegistry.registerScreenFactory(MissileMenus.SCHEMATICATOR.get(), SchematicatorScreen::new);
        MenuRegistry.registerScreenFactory(MissileMenus.NAVIGATOR.get(), NavigatorScreen::new);

        MissileEntityRenderers.init();
    }
}
