package net.woukie.createmissiles.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;
import net.minecraft.util.FastColor;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

public class FlashHandler {
    private static final ConcurrentLinkedQueue<Flash> flashes = new ConcurrentLinkedQueue<>();

    public static void addFlash(Flash flash) {
        flashes.add(flash);
    }

    public static void cleanUp() {
        flashes.removeIf(flash -> ((System.currentTimeMillis() - (double) flash.startTime) / (double) flash.length) > 1);
    }

    public static void handleHudRender(GuiGraphics guiGraphics) {
        Player player = Minecraft.getInstance().player;
        if (player == null) return;
        Vec3 position = player.position();
        flashes.forEach(flash -> {
            double distance = position.distanceTo(flash.origin.getCenter()) / 4;
            double intensity = Math.clamp(1 - distance / flash.radius, 0, 1);
            double x = (System.currentTimeMillis() - (double) flash.startTime) / (double) flash.length;
            if (x >= 1) return;
            double easing = Math.pow(1 - x, 3);
            int colour = FastColor.ARGB32.color((int) (255.0 * intensity * easing), FastColor.ARGB32.red(flash.colour), FastColor.ARGB32.green(flash.colour), FastColor.ARGB32.blue(flash.colour));
            guiGraphics.fill(0, 0, guiGraphics.guiWidth(), guiGraphics.guiHeight(), colour);
        });
    }

    public static void handleRenderLevel(PoseStack poseStack) {
        Player player = Minecraft.getInstance().player;
        if (player == null) return;
        Vec3 position = player.position();
        for (Flash flash : flashes) {
            double x = (System.currentTimeMillis() - (double) flash.startTime) / (double) flash.length;
            if (x >= 1) continue;

            double shakeIndex = x * (flash.shakeSamples.size() - 2);
            Vec3 shake1 = flash.shakeSamples.get((int)shakeIndex);
            Vec3 shake2 = flash.shakeSamples.get((int)shakeIndex + 1);
            Vec3 shake = shake1.lerp(shake2, shakeIndex % 1);

            double scale = 0.8;
            scale *= 1 - x;
            double distance = position.distanceTo(flash.origin.getCenter()) / 4;
            double intensity = Math.clamp(1 - distance / flash.radius, 0, 1);
            scale *= intensity;
            shake = shake.scale(scale);

            poseStack.translate(shake.x, shake.y, shake.z);
        }
    }

    public static class Flash {
        public BlockPos origin;
        public Integer colour;
        public Integer radius;
        public long startTime, length;
        public List<Vec3> shakeSamples = new ArrayList<>();

        public Flash(Integer colour, BlockPos origin, Integer radius, long length) {
            this.colour = colour;
            this.origin = origin;
            this.radius = radius;
            this.startTime = System.currentTimeMillis();
            this.length = length;

            for (int i = 0; i < 20 * length / 1000; i++) {
                shakeSamples.add(new Vec3(
                        Math.random() * 2 - 0.5,
                        Math.random() * 2 - 0.5,
                        Math.random() * 2 - 0.5
                ));
            }
        }
    }
}
