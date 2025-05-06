package net.woukie.createmissiles.forge;

import dev.architectury.platform.forge.EventBuses;
import dev.architectury.registry.menu.MenuRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.woukie.createmissiles.CreateMissiles;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.woukie.createmissiles.block.launchpadcontroller.LaunchPadControllerScreen;
import net.woukie.createmissiles.registry.MissileMenus;

@Mod(CreateMissiles.MOD_ID)
public class CreateMissilesForge {
    public CreateMissilesForge() {
        // registrate must be given the mod event bus on forge before registration
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        eventBus.addListener(this::clientSetup);

        EventBuses.registerModEventBus(CreateMissiles.MOD_ID, eventBus);

        CreateMissiles.registrate().registerEventListeners(eventBus);
        CreateMissiles.init();
    }

    private void clientSetup (final FMLClientSetupEvent event) {
        MenuRegistry.registerScreenFactory(MissileMenus.LAUNCH_PAD_CONTROLLER.get(), LaunchPadControllerScreen::new);
    }
}