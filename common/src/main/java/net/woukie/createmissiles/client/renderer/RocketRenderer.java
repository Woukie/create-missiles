package net.woukie.createmissiles.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.woukie.createmissiles.entity.RocketEntity;
import org.jetbrains.annotations.NotNull;

public class RocketRenderer extends EntityRenderer<RocketEntity> {
    public static final ResourceLocation TRIDENT_LOCATION = new ResourceLocation("textures/entity/trident.png");
//    private final GunpowderThrusterModel model;

    public RocketRenderer(EntityRendererProvider.Context context) {
        super(context);
//        this.model = new GunpowderThrusterModel();
    }

    public void render(RocketEntity entity, float f, float g, PoseStack poseStack, @NotNull MultiBufferSource multiBufferSource, int i) {
        poseStack.pushPose();
        poseStack.mulPose(Axis.YP.rotationDegrees(Mth.lerp(g, entity.yRotO, entity.getYRot()) - 90.0F));
        poseStack.mulPose(Axis.ZP.rotationDegrees(Mth.lerp(g, entity.xRotO, entity.getXRot()) + 90.0F));
//        VertexConsumer vertexConsumer = ItemRenderer.getFoilBufferDirect(multiBufferSource, this.model.renderType(this.getTextureLocation(entity)), false, true);
//        this.model.renderToBuffer(poseStack, vertexConsumer, i, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        poseStack.popPose();
        super.render(entity, f, g, poseStack, multiBufferSource, i);
    }

    public @NotNull ResourceLocation getTextureLocation(@NotNull RocketEntity entity) {
        return TRIDENT_LOCATION;
    }

}
