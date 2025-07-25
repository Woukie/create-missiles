package net.woukie.createmissiles.fabric;

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
    }
}
