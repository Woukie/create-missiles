package net.woukie.createmissiles.forge;

import dev.architectury.platform.forge.EventBuses;
import dev.architectury.registry.menu.MenuRegistry;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.woukie.createmissiles.CreateMissiles;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.woukie.createmissiles.block.controlpanel.ControlPanelScreen;
import net.woukie.createmissiles.block.navigationpanel.NavigationPanelScreen;
import net.woukie.createmissiles.block.assemblypanel.AssemblyPanelScreen;
import net.woukie.createmissiles.registry.EntityRenderers;
import net.woukie.createmissiles.registry.Menus;

@Mod(CreateMissiles.MOD_ID)
public class CreateMissilesForge {
    public CreateMissilesForge() {
        // registrate must be given the mod event bus on forge before registration
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        eventBus.addListener(this::clientSetup);

        DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> EntityRenderers::init);

        EventBuses.registerModEventBus(CreateMissiles.MOD_ID, eventBus);

        CreateMissiles.registrate().registerEventListeners(eventBus);
        CreateMissiles.init();
        ArmInteractionPointsForge.init();
    }

    private void clientSetup (final FMLClientSetupEvent event) {
        MenuRegistry.registerScreenFactory(Menus.CONTROL_PANEL.get(), ControlPanelScreen::new);
        MenuRegistry.registerScreenFactory(Menus.NAVIGATION_PANEL.get(), NavigationPanelScreen::new);
        MenuRegistry.registerScreenFactory(Menus.ASSEMBLY_PANEL.get(), AssemblyPanelScreen::new);
    }
}