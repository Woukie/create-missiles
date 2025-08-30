package net.woukie.createmissiles.registry;

import dev.architectury.registry.client.level.entity.EntityModelLayerRegistry;
import dev.architectury.registry.client.level.entity.EntityRendererRegistry;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.woukie.createmissiles.CreateMissiles;
import net.woukie.createmissiles.client.DroneRenderer;
import net.woukie.createmissiles.client.MissileRenderer;
import net.woukie.createmissiles.client.models.DroneModel;

public class EntityRenderers {
    public static void init() {
        CreateMissiles.LOGGER.info("Registering entity renderers for " + CreateMissiles.NAME);
        EntityRendererRegistry.register(EntityTypes.MISSILE, MissileRenderer::new);
        EntityRendererRegistry.register(EntityTypes.BASIC_DRONE, DroneRenderer::new);
        EntityRendererRegistry.register(EntityTypes.FIREBALL, ThrownItemRenderer::new);

        EntityModelLayerRegistry.register(DroneModel.LAYER_LOCATION, DroneModel::createBodyLayer);
    }
}
