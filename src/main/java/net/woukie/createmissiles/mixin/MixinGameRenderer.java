package net.woukie.createmissiles.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.GameRenderer;
import net.woukie.createmissiles.client.FlashHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public abstract class MixinGameRenderer {
    @Inject(method = "bobHurt", at = @At("HEAD"))
    public void bobHurt(PoseStack poseStack, float partialTicks, CallbackInfo ci) {
        FlashHandler.handleRenderLevel(poseStack);
    }
}
