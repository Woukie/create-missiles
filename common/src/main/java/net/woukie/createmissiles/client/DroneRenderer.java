package net.woukie.createmissiles.client;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.woukie.createmissiles.CreateMissiles;
import net.woukie.createmissiles.client.models.DroneModel;
import net.woukie.createmissiles.entity.DroneEntity;
import org.jetbrains.annotations.NotNull;

public class DroneRenderer extends MobRenderer<DroneEntity, DroneModel<DroneEntity>> {
    public DroneRenderer(EntityRendererProvider.Context context) {
        super(context, new DroneModel<>(context.bakeLayer(DroneModel.LAYER_LOCATION)), 0.75F);
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull DroneEntity entity) {
        return new ResourceLocation(CreateMissiles.MOD_ID, "textures/entity/drone.png");
    }
}
