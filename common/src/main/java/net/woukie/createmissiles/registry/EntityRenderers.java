package net.woukie.createmissiles.registry;

import dev.architectury.registry.client.level.entity.EntityRendererRegistry;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.world.item.Items;
import net.woukie.createmissiles.CreateMissiles;
import net.woukie.createmissiles.client.MissileRenderer;
import net.woukie.createmissiles.entity.FireballEntity;

public class EntityRenderers {
    public static void init() {
        CreateMissiles.LOGGER.info("Registering entity renderers for " + CreateMissiles.NAME);
        EntityRendererRegistry.register(EntityTypes.MISSILE, MissileRenderer::new);
        EntityRendererRegistry.register(EntityTypes.FIREBALL, context -> new ThrownItemRenderer<FireballEntity>(context));
    }
}
