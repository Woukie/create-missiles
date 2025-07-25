package net.woukie.createmissiles.fabric;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.simibubi.create.foundation.item.render.CustomRenderedItemModel;
import com.simibubi.create.foundation.item.render.CustomRenderedItemModelRenderer;
import com.simibubi.create.foundation.item.render.PartialItemModelRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.woukie.createmissiles.client.MissilePartModel;
import net.woukie.createmissiles.missilemanager.parts.MissilePartType;
import net.woukie.createmissiles.registry.PartTypes;
import org.joml.Vector3f;

import java.util.Map;

public class AssemblyRenderer extends CustomRenderedItemModelRenderer {
    private static final ModelResourceLocation ASSEMBLY_MODEL = new ModelResourceLocation("createmissiles", "chassis_assembly_item", "inventory");

    @Override
    protected void render(ItemStack stack, CustomRenderedItemModel model, PartialItemModelRenderer renderer, ItemDisplayContext transformType, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        ms.pushPose();

        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        itemRenderer.render(stack, ItemDisplayContext.NONE, false, ms, buffer, light, overlay, model.getOriginalModel());

        if (transformType == ItemDisplayContext.GUI) {
            ms.translate(0, 0, 1F);
        }

        MissilePartType partType = PartTypes.get(stack);
        if (partType != null) {
            ms.pushPose();
            ms.mulPose(Axis.XP.rotationDegrees(30));
            ms.scale(0.65F, 0.65F, 0.65F);
            long time = (System.currentTimeMillis() / 10L) % 360;
            double a = Math.sin(Math.toRadians(time));
            ms.mulPose(Axis.ZP.rotationDegrees((float) a * 20F));
            renderMissilePart(ms, buffer, overlay, partType);
            ms.popPose();
        }

        ms.popPose();
    }

    private static void renderMissilePart(PoseStack poseStack, MultiBufferSource multiBufferSource, int overlay, MissilePartType partType) {
        MissilePartModel model = partType.getModel();

        poseStack.pushPose();
        long deg = ((System.currentTimeMillis() / 10L) % 360);
        poseStack.mulPose(Axis.YP.rotationDegrees(deg));

        int stage = (int)((System.currentTimeMillis() / 1000L) % (model.getStageCount() - 1)) + 1;

        Map<String, Vector3f> attachments = model.getAttachements(stage);
        Vector3f top = new Vector3f(attachments.get("top"));
        Vector3f offset = new Vector3f(attachments.get("bottom"));
        offset.sub(top.sub(offset).div(2));

        LayerDefinition warheadModelLayerDefinition = model.getLayerDefinition(stage);
        ModelPart modelPart = warheadModelLayerDefinition.bakeRoot();
        modelPart.offsetPos(offset);
        VertexConsumer warheadVertexConsumer = multiBufferSource.getBuffer(RenderType.entityTranslucent(model.getTexture(stage)));
        modelPart.render(poseStack, warheadVertexConsumer, 255, overlay);

        poseStack.popPose();
    }
}
