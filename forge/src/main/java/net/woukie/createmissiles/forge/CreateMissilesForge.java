package net.woukie.createmissiles.forge;

import dev.architectury.platform.forge.EventBuses;
import dev.architectury.registry.menu.MenuRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.woukie.createmissiles.CreateMissiles;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.woukie.createmissiles.block.controller.ControllerScreen;
import net.woukie.createmissiles.block.navigator.NavigatorScreen;
import net.woukie.createmissiles.block.schematicator.SchematicatorScreen;
import net.woukie.createmissiles.registry.MissileMenus;
import net.woukie.createmissiles.registry.MissilesEntityRenderers;

@Mod(CreateMissiles.MOD_ID)
public class CreateMissilesForge {
    public CreateMissilesForge() {
        // registrate must be given the mod event bus on forge before registration
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        eventBus.addListener(this::clientSetup);

        EventBuses.registerModEventBus(CreateMissiles.MOD_ID, eventBus);

        CreateMissiles.registrate().registerEventListeners(eventBus);
        CreateMissiles.init();
        ArmInteractionPointsForge.init();
    }

    private void clientSetup (final FMLClientSetupEvent event) {
        MenuRegistry.registerScreenFactory(MissileMenus.CONTROLLER.get(), ControllerScreen::new);
        MenuRegistry.registerScreenFactory(MissileMenus.NAVIGATOR.get(), NavigatorScreen::new);
        MenuRegistry.registerScreenFactory(MissileMenus.SCHEMATICATOR.get(), SchematicatorScreen::new);
        MissilesEntityRenderers.initClient();
    }
}