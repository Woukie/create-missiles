package net.woukie.createmissiles.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.Items;
import net.woukie.createmissiles.CreateMissiles;
import net.woukie.createmissiles.entity.DroneEntity;
import org.jetbrains.annotations.NotNull;

public class DroneRenderer extends EntityRenderer<DroneEntity> {

    public DroneRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull DroneEntity entity) {
        return new ResourceLocation(CreateMissiles.MOD_ID, "textures/entity/gunpowder_thruster.png");
    }

    @Override
    public void render(DroneEntity entity, float yaw, float pt, PoseStack ms, @NotNull MultiBufferSource buffer, int light) {
        ms.pushPose();
        ms.translate(0, entity.getBoundingBox()
                .getYsize() / 2 - 1 / 8f, 0);
        ms.scale(4, 4, 4);
        Minecraft.getInstance()
                .getItemRenderer()
                .renderStatic(Items.IRON_BLOCK.getDefaultInstance(), ItemDisplayContext.GROUND, light, OverlayTexture.NO_OVERLAY, ms, buffer, entity.level(),
                        0);
        ms.popPose();
    }
}
