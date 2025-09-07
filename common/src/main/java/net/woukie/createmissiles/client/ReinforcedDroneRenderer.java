package net.woukie.createmissiles.client;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.woukie.createmissiles.CreateMissiles;
import net.woukie.createmissiles.entity.drone.Drone;
import org.jetbrains.annotations.NotNull;

public class ReinforcedDroneRenderer extends DroneRenderer {
    public ReinforcedDroneRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull Drone entity) {
        return new ResourceLocation(CreateMissiles.MOD_ID, "textures/entity/reinforced_drone.png");
    }
}
