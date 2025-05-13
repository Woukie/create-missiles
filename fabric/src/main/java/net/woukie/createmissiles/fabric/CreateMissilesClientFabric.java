package net.woukie.createmissiles.fabric;

import dev.architectury.registry.menu.MenuRegistry;
import net.fabricmc.api.ClientModInitializer;
import net.woukie.createmissiles.block.controller.ControllerScreen;
import net.woukie.createmissiles.registry.MissileMenus;

public class CreateMissilesClientFabric implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        MenuRegistry.registerScreenFactory(MissileMenus.LAUNCH_PAD_CONTROLLER.get(), ControllerScreen::new);
    }
}
