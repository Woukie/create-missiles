package net.woukie.createmissiles.registry;

import dev.architectury.registry.client.level.entity.EntityRendererRegistry;
import net.woukie.createmissiles.entity.client.DroneRenderer;

public class MissilesEntityRenderers {
    public static void initClient()
    {
        EntityRendererRegistry.register(MissilesEntities.DRONE_ENTITY, DroneRenderer::new);
    }
}
