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
import net.woukie.createmissiles.registry.PartTypes;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;

public class MissileRenderer extends EntityRenderer<MissileEntity> {
    public MissileRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

//    private int lastTick = 0;
//    private long lastTickTime = System.currentTimeMillis();

    public void render(@NotNull MissileEntity entity, float f, float g, @NotNull PoseStack poseStack, @NotNull MultiBufferSource multiBufferSource, int i) {
        WarheadType warheadType = (WarheadType) PartTypes.get(entity.getWarheadType());
        ChassisType chassisType = (ChassisType) PartTypes.get(entity.getChassisType());
        ThrusterType thrusterType = (ThrusterType) PartTypes.get(entity.getThrusterType());

        poseStack.pushPose();

        Vector3f offset = new Vector3f(0, 5, 0);

        if (thrusterType != null)
            renderPart(entity, poseStack, multiBufferSource, i, thrusterType.getModel(), entity.getThrusterBuildPercent(), offset);

        if (chassisType != null)
            renderPart(entity, poseStack, multiBufferSource, i, chassisType.getModel(), entity.getChassisBuildPercent(), offset);

        if (warheadType != null)
            renderPart(entity, poseStack, multiBufferSource, i, warheadType.getModel(), entity.getWarheadBuildPercent(), offset);

//        if (lastTick != entity.tickCount) {
//            lastTick = entity.tickCount;
//            lastTickTime = System.currentTimeMillis();
//        }

        poseStack.popPose();

        super.render(entity, f, g, poseStack, multiBufferSource, i);
    }

    private static void renderPart(@NotNull MissileEntity entity, @NotNull PoseStack poseStack, @NotNull MultiBufferSource multiBufferSource, int i, MissilePartModel model, int buildPercent, Vector3f offset) {
        int stage = model.getStage(buildPercent);

        var attachments = model.getAttachements(stage);
        offset.add(attachments.get("bottom"));

        LayerDefinition warheadModelLayerDefinition = model.getLayerDefinition(stage);
        ModelPart part = warheadModelLayerDefinition.bakeRoot();
        var rotation = entity.getRotation();
        part.offsetRotation(new Vector3f(rotation.getX(), rotation.getY(), rotation.getZ()));
        Vector3f vec = new Vector3f(offset);
        part.offsetPos(vec.rotateX(rotation.getX()).rotateY(rotation.getY()).rotateZ(rotation.getZ()));
        VertexConsumer warheadVertexConsumer = multiBufferSource.getBuffer(RenderType.entityTranslucent(model.getTexture(stage)));
        part.render(poseStack, warheadVertexConsumer, i, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);

        if (attachments.containsKey("top")) offset.add(attachments.get("top"));
    }

    public @NotNull ResourceLocation getTextureLocation(@NotNull MissileEntity entity) {
        return new ResourceLocation(CreateMissiles.MOD_ID, "textures/entity/gunpowder_thruster.png");
    }
}
