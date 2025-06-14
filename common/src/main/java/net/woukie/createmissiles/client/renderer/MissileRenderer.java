package net.woukie.createmissiles.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.woukie.createmissiles.client.MissilePartModel;
import net.woukie.createmissiles.entity.MissileEntity;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class MissileRenderer extends EntityRenderer<MissileEntity> {
    private List<MissilePartModel> warheadModels = new ArrayList<>();
    private List<MissilePartModel> chassisModels = new ArrayList<>();
    private List<MissilePartModel> thrusterModels = new ArrayList<>();

    private float warheadBuildPercent;
    private float chassisBuildPercent;
    private float thrusterBuildPercent;

    public void setWarheadModels(List<MissilePartModel> warheadModels) {
        this.warheadModels = warheadModels;
    }

    public void setChassisModels(List<MissilePartModel> chassisModels) {
        this.chassisModels = chassisModels;
    }

    public void setThrusterModels(List<MissilePartModel> thrusterModels) {
        this.thrusterModels = thrusterModels;
    }

    public void setWarheadBuildPercent(float buildPercent) {
        this.warheadBuildPercent = buildPercent;
    }

    public void setChassisBuildPercent(float buildPercent) {
        this.chassisBuildPercent = buildPercent;
    }

    public void setThrusterBuildPercent(float buildPercent) {
        this.thrusterBuildPercent = buildPercent;
    }

    public MissileRenderer(EntityRendererProvider.Context context) {
        super(context);
//        this.model = new GunpowderThrusterModel();
    }

    public void render(@NotNull MissileEntity entity, float f, float g, @NotNull PoseStack poseStack, @NotNull MultiBufferSource multiBufferSource, int i) {
        if (warheadModels.isEmpty() || chassisModels.isEmpty() || thrusterModels.isEmpty()) return;

        poseStack.pushPose();
        poseStack.mulPose(Axis.YP.rotationDegrees(Mth.lerp(g, entity.yRotO, entity.getYRot()) - 90.0F));
        poseStack.mulPose(Axis.ZP.rotationDegrees(Mth.lerp(g, entity.xRotO, entity.getXRot()) + 90.0F));

        MissilePartModel warheadModel = warheadModels.get((int) (warheadBuildPercent * warheadModels.size()));
        MissilePartModel chassisModel = chassisModels.get((int) (chassisBuildPercent * chassisModels.size()));
        MissilePartModel thrusterModel = thrusterModels.get((int) (thrusterBuildPercent * thrusterModels.size()));

        VertexConsumer warheadVertexConsumer = multiBufferSource.getBuffer(warheadModel.renderType(warheadModel.getTextureLocation()));
        VertexConsumer chassisVertexConsumer = multiBufferSource.getBuffer(chassisModel.renderType(chassisModel.getTextureLocation()));
        VertexConsumer thrusterVertexConsumer = multiBufferSource.getBuffer(thrusterModel.renderType(thrusterModel.getTextureLocation()));

        warheadModel.renderToBuffer(poseStack, warheadVertexConsumer, i, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        warheadModel.renderToBuffer(poseStack, chassisVertexConsumer, i, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        warheadModel.renderToBuffer(poseStack, thrusterVertexConsumer, i, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);

        poseStack.popPose();
        super.render(entity, f, g, poseStack, multiBufferSource, i);
    }

    public @NotNull ResourceLocation getTextureLocation(@NotNull MissileEntity entity) {
        return new ResourceLocation("textures/entity/trident.png");
    }
}
