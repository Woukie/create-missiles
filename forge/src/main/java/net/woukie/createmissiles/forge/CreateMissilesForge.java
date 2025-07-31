package net.woukie.createmissiles.forge;

import dev.architectury.platform.forge.EventBuses;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.woukie.createmissiles.CreateMissiles;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.woukie.createmissiles.particle.BuildShrapnel;
import net.woukie.createmissiles.registry.EntityRenderers;
import net.woukie.createmissiles.registry.ParticleTypes;

@Mod(CreateMissiles.MOD_ID)
public class CreateMissilesForge {
    public CreateMissilesForge() {
        // registrate must be given the mod event bus on forge before registration
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        eventBus.addListener(this::clientSetup);
        eventBus.addListener(this::registerParticles);

        DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> EntityRenderers::init);

        EventBuses.registerModEventBus(CreateMissiles.MOD_ID, eventBus);

        CreateMissiles.registrate().registerEventListeners(eventBus);
        CreateMissiles.init();
        ArmInteractionPointsForge.init();
    }

    private void clientSetup (final FMLClientSetupEvent event) {
        CreateMissiles.initClient();
    }

    private void registerParticles(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(ParticleTypes.BUILD_SHRAPNEL.get(), BuildShrapnel.Provider::new);
    }
}