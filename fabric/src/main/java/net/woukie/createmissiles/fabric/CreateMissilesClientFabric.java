package net.woukie.createmissiles.fabric;

import dev.architectury.registry.menu.MenuRegistry;
import net.fabricmc.api.ClientModInitializer;
import net.woukie.createmissiles.block.controlpanel.ControlPanelScreen;
import net.woukie.createmissiles.block.navigationpanel.NavigationPanelScreen;
import net.woukie.createmissiles.block.assemblypanel.AssemblyPanelScreen;
import net.woukie.createmissiles.registry.EntityRenderers;
import net.woukie.createmissiles.registry.Menus;

public class CreateMissilesClientFabric implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        MenuRegistry.registerScreenFactory(Menus.CONTROL_PANEL.get(), ControlPanelScreen::new);
        MenuRegistry.registerScreenFactory(Menus.ASSEMBLY_PANEL.get(), AssemblyPanelScreen::new);
        MenuRegistry.registerScreenFactory(Menus.NAVIGATION_PANEL.get(), NavigationPanelScreen::new);

        EntityRenderers.init();
    }
}
