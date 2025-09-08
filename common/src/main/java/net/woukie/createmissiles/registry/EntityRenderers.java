package net.woukie.createmissiles.registry;

import dev.architectury.registry.client.level.entity.EntityModelLayerRegistry;
import dev.architectury.registry.client.level.entity.EntityRendererRegistry;
import net.minecraft.client.renderer.entity.NoopRenderer;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.woukie.createmissiles.CreateMissiles;
import net.woukie.createmissiles.client.DroneRenderer;
import net.woukie.createmissiles.client.MissileRenderer;
import net.woukie.createmissiles.client.ReinforcedDroneRenderer;
import net.woukie.createmissiles.client.models.DroneModel;

public class EntityRenderers {
    public static void init() {
        CreateMissiles.LOGGER.info("Registering entity renderers for " + CreateMissiles.NAME);
        EntityRendererRegistry.register(EntityTypes.MISSILE, MissileRenderer::new);
        EntityRendererRegistry.register(EntityTypes.BASIC_DRONE, DroneRenderer::new);
        EntityRendererRegistry.register(EntityTypes.REINFORCED_DRONE, ReinforcedDroneRenderer::new);
        EntityRendererRegistry.register(EntityTypes.FLAMINGBALL, ThrownItemRenderer::new);
        EntityRendererRegistry.register(EntityTypes.BLAZINGBALL, ThrownItemRenderer::new);
        EntityRendererRegistry.register(EntityTypes.INFERNALBALL, ThrownItemRenderer::new);
        EntityRendererRegistry.register(EntityTypes.FROSTBALL, ThrownItemRenderer::new);
        EntityRendererRegistry.register(EntityTypes.FROZENBALL, ThrownItemRenderer::new);
        EntityRendererRegistry.register(EntityTypes.GUARDIANBALL, ThrownItemRenderer::new);
        EntityRendererRegistry.register(EntityTypes.ANCIENTBALL, ThrownItemRenderer::new);
        EntityRendererRegistry.register(EntityTypes.WITHEREDBALL, ThrownItemRenderer::new);
        EntityRendererRegistry.register(EntityTypes.FROZEN_AREA, NoopRenderer::new);
        EntityRendererRegistry.register(EntityTypes.INFERNAL_AREA, NoopRenderer::new);
        EntityRendererRegistry.register(EntityTypes.MESSY, NoopRenderer::new);

        EntityModelLayerRegistry.register(DroneModel.LAYER_LOCATION, DroneModel::createBodyLayer);
    }
}
