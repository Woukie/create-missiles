package net.woukie.createmissiles.fabric;

import dev.architectury.registry.client.particle.ParticleProviderRegistry;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.woukie.createmissiles.CreateMissiles;
import net.woukie.createmissiles.client.AssemblyRenderer;
import net.woukie.createmissiles.particle.WeldSpark;
import net.woukie.createmissiles.registry.EntityRenderers;
import net.woukie.createmissiles.registry.Items;
import net.woukie.createmissiles.registry.ParticleTypes;

public class CreateMissilesClientFabric implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        CreateMissiles.initClient();
        EntityRenderers.init();

        BuiltinItemRendererRegistry.INSTANCE.register(Items.WARHEAD_ASSEMBLY.get(), new AssemblyRenderer());
        BuiltinItemRendererRegistry.INSTANCE.register(Items.CHASSIS_ASSEMBLY.get(), new AssemblyRenderer());
        BuiltinItemRendererRegistry.INSTANCE.register(Items.THRUSTER_ASSEMBLY.get(), new AssemblyRenderer());

        ParticleProviderRegistry.register(ParticleTypes.WELD_SPARK.get(), WeldSpark.Provider::new);
    }
}
