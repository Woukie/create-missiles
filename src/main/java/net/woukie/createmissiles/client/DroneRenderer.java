package net.woukie.createmissiles.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.woukie.createmissiles.CreateMissiles;
import net.woukie.createmissiles.client.models.DroneModel;
import net.woukie.createmissiles.entity.drone.Drone;
import org.jetbrains.annotations.NotNull;

public class DroneRenderer extends MobRenderer<Drone, DroneModel<Drone>> {
    public DroneRenderer(EntityRendererProvider.Context context) {
        super(context, new DroneModel<>(context.bakeLayer(DroneModel.LAYER_LOCATION)), 0.75F);
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull Drone entity) {
        return ResourceLocation.fromNamespaceAndPath(CreateMissiles.MOD_ID, "textures/entity/basic_drone.png");
    }

    @Override
    protected void setupRotations(Drone drone, PoseStack poseStack, float f, float g, float h) {
        super.setupRotations(drone, poseStack, f, g, h);
        poseStack.mulPose(Axis.XP.rotationDegrees(-drone.getXRot()));
    }
}
