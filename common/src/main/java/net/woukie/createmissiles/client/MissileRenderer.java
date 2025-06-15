package net.woukie.createmissiles.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.woukie.createmissiles.CreateMissiles;
import net.woukie.createmissiles.entity.MissileEntity;
import net.woukie.createmissiles.missilemanager.parts.ChassisType;
import net.woukie.createmissiles.missilemanager.parts.ThrusterType;
import net.woukie.createmissiles.missilemanager.parts.WarheadType;
import net.woukie.createmissiles.registry.MissilePartTypes;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;

public class MissileRenderer extends EntityRenderer<MissileEntity> {
    public MissileRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    public void render(@NotNull MissileEntity entity, float f, float g, @NotNull PoseStack poseStack, @NotNull MultiBufferSource multiBufferSource, int i) {
        WarheadType warheadType = (WarheadType) MissilePartTypes.get(entity.getWarheadType());
        ChassisType chassisType = (ChassisType) MissilePartTypes.get(entity.getChassisType());
        ThrusterType thrusterType = (ThrusterType) MissilePartTypes.get(entity.getThrusterType());

        poseStack.pushPose();

        Vector3f offset = new Vector3f(0, 5, 0);

        if (thrusterType != null) {
            MissilePartModel model = thrusterType.model;
            int stage = (int) ((model.getStageCount() - 1) * (entity.getThrusterBuildPercent() / 100F));
            var attachments = model.getAttachements(stage);

            offset.add(attachments.get("bottom"));
            System.out.println(offset);
            renderPart(poseStack, multiBufferSource, i, model, stage, offset);
            offset.add(attachments.get("top"));
        }

        if (chassisType != null) {
            MissilePartModel model = chassisType.model;
            int stage = (int) ((model.getStageCount() - 1) * (entity.getChassisBuildPercent() / 100F));
            var attachments = model.getAttachements(stage);

            offset.add(attachments.get("bottom"));
            System.out.println(offset);
            renderPart(poseStack, multiBufferSource, i, model, stage, offset);
            offset.add(attachments.get("top"));
        }

        if (warheadType != null) {
            MissilePartModel model = warheadType.model;
            int stage = (int) ((model.getStageCount() - 1) * (entity.getWarheadBuildPercent() / 100F));
            var attachments = model.getAttachements(stage);

            offset.add(attachments.get("bottom"));
            System.out.println(offset);
            renderPart(poseStack, multiBufferSource, i, model, stage, offset);
        }

        poseStack.popPose();

        super.render(entity, f, g, poseStack, multiBufferSource, i);
    }

    private static void renderPart(@NotNull PoseStack poseStack, @NotNull MultiBufferSource multiBufferSource, int i, MissilePartModel model, int stage, Vector3f offset) {
        LayerDefinition warheadModelLayerDefinition = model.getLayerDefinition(stage);
        ModelPart warheadPart = warheadModelLayerDefinition.bakeRoot();
        warheadPart.offsetPos(offset);
        VertexConsumer warheadVertexConsumer = multiBufferSource.getBuffer(RenderType.entityTranslucent(model.getTexture(stage)));
        warheadPart.render(poseStack, warheadVertexConsumer, i, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
    }

    public @NotNull ResourceLocation getTextureLocation(@NotNull MissileEntity entity) {
        return new ResourceLocation(CreateMissiles.MOD_ID, "textures/entity/gunpowder_thruster.png");
    }
}
