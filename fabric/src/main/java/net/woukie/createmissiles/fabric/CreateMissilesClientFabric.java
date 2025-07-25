package net.woukie.createmissiles.fabric;

import com.simibubi.create.foundation.item.render.CustomRenderedItems;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.woukie.createmissiles.CreateMissiles;
import net.woukie.createmissiles.registry.EntityRenderers;
import net.woukie.createmissiles.registry.Items;

public class CreateMissilesClientFabric implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        CreateMissiles.initClient();
        EntityRenderers.init();

        BuiltinItemRendererRegistry.INSTANCE.register(Items.WARHEAD_ASSEMBLY.get(), new AssemblyRenderer());
        CustomRenderedItems.register(Items.WARHEAD_ASSEMBLY.get());

        BuiltinItemRendererRegistry.INSTANCE.register(Items.CHASSIS_ASSEMBLY.get(), new AssemblyRenderer());
        CustomRenderedItems.register(Items.CHASSIS_ASSEMBLY.get());

        BuiltinItemRendererRegistry.INSTANCE.register(Items.THRUSTER_ASSEMBLY.get(), new AssemblyRenderer());
        CustomRenderedItems.register(Items.THRUSTER_ASSEMBLY.get());
    }
}
