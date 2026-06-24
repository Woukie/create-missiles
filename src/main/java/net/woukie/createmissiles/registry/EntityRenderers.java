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
        event.registerEntityRenderer(EntityTypes.MISSILE.get(), MissileRenderer::new);
        event.registerEntityRenderer(EntityTypes.BASIC_DRONE.get(), DroneRenderer::new);
        event.registerEntityRenderer(EntityTypes.REINFORCED_DRONE.get(), ReinforcedDroneRenderer::new);
        event.registerEntityRenderer(EntityTypes.FLAMINGBALL.get(), ThrownItemRenderer::new);
        event.registerEntityRenderer(EntityTypes.BLAZINGBALL.get(), ThrownItemRenderer::new);
        event.registerEntityRenderer(EntityTypes.INFERNALBALL.get(), ThrownItemRenderer::new);
        event.registerEntityRenderer(EntityTypes.FROSTBALL.get(), ThrownItemRenderer::new);
        event.registerEntityRenderer(EntityTypes.FROZENBALL.get(), ThrownItemRenderer::new);
        event.registerEntityRenderer(EntityTypes.GUARDIANBALL.get(), ThrownItemRenderer::new);
        event.registerEntityRenderer(EntityTypes.ANCIENTBALL.get(), ThrownItemRenderer::new);
        event.registerEntityRenderer(EntityTypes.WITHEREDBALL.get(), ThrownItemRenderer::new);
        event.registerEntityRenderer(EntityTypes.FROZEN_AREA.get(), NoopRenderer::new);
        event.registerEntityRenderer(EntityTypes.INFERNAL_AREA.get(), NoopRenderer::new);
        event.registerEntityRenderer(EntityTypes.MESSY.get(), NoopRenderer::new);
    }

    public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        CreateMissiles.LOGGER.info("Registering layer definitions for " + CreateMissiles.NAME);
        event.registerLayerDefinition(DroneModel.LAYER_LOCATION, DroneModel::createBodyLayer);
    }
}
