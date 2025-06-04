package net.woukie.createmissiles.registry;

import dev.architectury.registry.client.level.entity.EntityRendererRegistry;
import net.woukie.createmissiles.CreateMissiles;
import net.woukie.createmissiles.client.renderer.RocketRenderer;

public class MissileEntityRenderers {
    public static void init() {
        CreateMissiles.LOGGER.info("Registering entity renderers for " + CreateMissiles.NAME);
        EntityRendererRegistry.register(MissileEntityTypes.ROCKET, RocketRenderer::new);
    }
}
