package net.woukie.createmissiles.registry;

import net.minecraft.client.renderer.entity.NoopRenderer;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.woukie.createmissiles.CreateMissiles;
import net.woukie.createmissiles.client.DroneRenderer;
import net.woukie.createmissiles.client.MissileRenderer;
import net.woukie.createmissiles.client.ReinforcedDroneRenderer;
import net.woukie.createmissiles.client.models.DroneModel;

public class EntityRenderers {
    public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        CreateMissiles.LOGGER.info("Registering entity renderers for " + CreateMissiles.NAME);
        event.registerEntityRenderer(EntityTypes.MISSILE, MissileRenderer::new);
        event.registerEntityRenderer(EntityTypes.BASIC_DRONE, DroneRenderer::new);
        event.registerEntityRenderer(EntityTypes.REINFORCED_DRONE, ReinforcedDroneRenderer::new);
        event.registerEntityRenderer(EntityTypes.FLAMINGBALL, ThrownItemRenderer::new);
        event.registerEntityRenderer(EntityTypes.BLAZINGBALL, ThrownItemRenderer::new);
        event.registerEntityRenderer(EntityTypes.INFERNALBALL, ThrownItemRenderer::new);
        event.registerEntityRenderer(EntityTypes.FROSTBALL, ThrownItemRenderer::new);
        event.registerEntityRenderer(EntityTypes.FROZENBALL, ThrownItemRenderer::new);
        event.registerEntityRenderer(EntityTypes.GUARDIANBALL, ThrownItemRenderer::new);
        event.registerEntityRenderer(EntityTypes.ANCIENTBALL, ThrownItemRenderer::new);
        event.registerEntityRenderer(EntityTypes.WITHEREDBALL, ThrownItemRenderer::new);
        event.registerEntityRenderer(EntityTypes.FROZEN_AREA, NoopRenderer::new);
        event.registerEntityRenderer(EntityTypes.INFERNAL_AREA, NoopRenderer::new);
        event.registerEntityRenderer(EntityTypes.MESSY, NoopRenderer::new);
    }

    public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        CreateMissiles.LOGGER.info("Registering layer definitions for " + CreateMissiles.NAME);
        event.registerLayerDefinition(DroneModel.LAYER_LOCATION, DroneModel::createBodyLayer);
    }
}
