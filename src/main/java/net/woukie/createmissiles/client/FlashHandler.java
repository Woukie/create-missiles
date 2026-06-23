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
            double timeIntensity = (System.currentTimeMillis() - (double) flash.startTime) / (double) flash.length;
            if (timeIntensity >= 1) return;
            timeIntensity = Math.pow(1 - timeIntensity, 3);

            double distance = position.distanceTo(flash.origin.getCenter());
            distance = Math.min(distance / flash.radius, 1);
            double distanceIntensity = Math.pow(1 - distance, 3);

            int colour = FastColor.ARGB32.color((int) (255.0 * distanceIntensity * timeIntensity), FastColor.ARGB32.red(flash.colour), FastColor.ARGB32.green(flash.colour), FastColor.ARGB32.blue(flash.colour));
            guiGraphics.fill(0, 0, guiGraphics.guiWidth(), guiGraphics.guiHeight(), colour);
        });
    }

    public static void handleRenderLevel(PoseStack poseStack) {
        Player player = Minecraft.getInstance().player;
        if (player == null) return;
        Vec3 position = player.position();
        for (Flash flash : flashes) {
            double timeIntensity = (System.currentTimeMillis() - (double) flash.startTime) / (double) flash.length;
            double shakeIndex = timeIntensity * (flash.shakeSamples.size() - 2);
            if (timeIntensity >= 1) continue;
            timeIntensity = Math.pow(1 - timeIntensity, 3);

            Vec3 shake1 = flash.shakeSamples.get((int)shakeIndex);
            Vec3 shake2 = flash.shakeSamples.get((int)shakeIndex + 1);
            Vec3 shake = shake1.lerp(shake2, shakeIndex % 1);

            double distance = position.distanceTo(flash.origin.getCenter());
            distance = Math.min(distance / flash.radius, 1);
            double intensity = Math.pow(1 - distance, 3);
            double scaledIntensity = Math.pow(-(flash.intensity/4 + 1), -1) + 1; // Maps (0, +infinity) to (0, 1)
            shake = shake.scale(timeIntensity * intensity * scaledIntensity * 3);

            poseStack.translate(shake.x, shake.y, shake.z);
        }
    }

    public static class Flash {
        public BlockPos origin;
        public Integer colour;
        public Integer radius;
        public Double intensity;
        public long startTime, length;
        public List<Vec3> shakeSamples = new ArrayList<>();

        public Flash(Integer colour, BlockPos origin, Integer radius, Double intensity, long length) {
            this.colour = colour;
            this.origin = origin;
            this.radius = radius;
            this.intensity = intensity;
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
