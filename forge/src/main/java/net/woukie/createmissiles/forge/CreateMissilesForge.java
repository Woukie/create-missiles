package net.woukie.createmissiles.forge;

import dev.architectury.platform.forge.EventBuses;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.registries.RegisterEvent;
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
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        eventBus.addListener(this::clientSetup);
        eventBus.addListener(this::onRegister);

        DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> EntityRenderers::init);
        EventBuses.registerModEventBus(CreateMissiles.MOD_ID, eventBus);
        CreateMissiles.registrate().registerEventListeners(eventBus);
        CreateMissiles.init();
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> eventBus.addListener(this::registerParticles));
    }

    private void clientSetup(final FMLClientSetupEvent event) {
        CreateMissiles.initClient();
    }

    public void registerParticles(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(ParticleTypes.BUILD_SHRAPNEL.get(), BuildShrapnel.Provider::new);
    }

    private void onRegister(RegisterEvent evt) {
        ArmInteractionPointsForge.init();
    }
}