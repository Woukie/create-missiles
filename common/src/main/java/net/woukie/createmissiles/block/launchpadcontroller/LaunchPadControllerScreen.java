package net.woukie.createmissiles.block.launchpadcontroller;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import net.woukie.createmissiles.CreateMissiles;

@Environment(EnvType.CLIENT)
public class LaunchPadControllerScreen extends AbstractContainerScreen<LaunchPadControllerMenu> {
    private static final int backgroundFrames = 19;
    private static final int mapTop = 17;
    private static final int mapLeft = 108;
    private static final int buttonTop = 14;
    private static final int buttonLeft = 88;
    private static final int buttonWidth = 16;
    private static final int buttonHeight = 16;
    private static final float mapScale = 0.4375F;
    private static final ResourceLocation BACKGROUND_HOVERING_BUTTON = new ResourceLocation(CreateMissiles.MOD_ID, "textures/gui/container/launch_pad_controller_hover.png");
    private static final ResourceLocation TARGET_MIDDLE = new ResourceLocation(CreateMissiles.MOD_ID, "textures/gui/sprites/container/target_marker.png");
    private static final ResourceLocation TARGET_HORIZONTAL = new ResourceLocation(CreateMissiles.MOD_ID, "textures/gui/sprites/container/target_horizontal.png");
    private static final ResourceLocation TARGET_VERTICAL = new ResourceLocation(CreateMissiles.MOD_ID, "textures/gui/sprites/container/target_vertical.png");
    private ItemStack map;

    private boolean hoveringButton;

    private float displayScale = 0;
    private float displayTargetX = -1;
    private float displayTargetZ = -1;

    private float backgroundPercentage = 0F;

    public LaunchPadControllerScreen(LaunchPadControllerMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
        this.titleLabelY -= 2;
    }

    public void render(GuiGraphics guiGraphics, int i, int j, float f) {
        super.render(guiGraphics, i, j, f);
        this.renderTooltip(guiGraphics, i, j);
    }

    @Override
    public void mouseMoved(double d, double e) {
        hoveringButton = isHovering(buttonLeft, buttonTop, buttonWidth, buttonHeight, d, e);
        super.mouseMoved(d, e);
    }

    protected void renderBg(GuiGraphics guiGraphics, float f, int i, int j) {
        this.renderBackground(guiGraphics);
        int left = this.leftPos;
        int top = this.topPos;

        float targetBackgroundPercentage = this.menu.armed() ? 1F : 0F;
        backgroundPercentage = backgroundPercentage + (targetBackgroundPercentage - backgroundPercentage) * 0.1F;
        int backgroundFrame = (int) Math.clamp(backgroundPercentage * backgroundFrames, 0, backgroundFrames - 1);

        boolean isLastFrame = backgroundFrame == backgroundFrames - 1;
        if (isLastFrame && hoveringButton) {
            guiGraphics.blit(BACKGROUND_HOVERING_BUTTON, left, top, 0, 0, this.imageWidth, this.imageHeight);
        } else {
            ResourceLocation backgroundLocation = new ResourceLocation(CreateMissiles.MOD_ID, "textures/gui/container/launch_pad_controller_" + backgroundFrame + ".png");
            guiGraphics.blit(backgroundLocation, left, top, 0, 0, this.imageWidth, this.imageHeight);
        }

        map = this.menu.getSlot(0).getItem();

        int targetScale = 1;
        if (map.is(Items.FILLED_MAP)) {
            Integer mapId = MapItem.getMapId(map);
            MapItemSavedData mapData = MapItem.getSavedData(mapId, this.minecraft.level);

            if (mapData != null) {
                guiGraphics.pose().pushPose();
                guiGraphics.pose().translate(left + mapLeft, top + mapTop, 1.0F);
                guiGraphics.pose().scale(mapScale, mapScale, 1.0F);
                this.minecraft.gameRenderer.getMapRenderer().render(guiGraphics.pose(), guiGraphics.bufferSource(), mapId, mapData, true, 15728880);
                guiGraphics.flush();
                guiGraphics.pose().popPose();
            }
        } else {
            targetScale = 0;
        }

//      Target is num from 0 to 128
//      Map is 56x56 (a number from (0 to 128) * mapScale)
        int targetX = this.menu.getTargetX();
        int targetZ = this.menu.getTargetZ();

        if (targetX == -1 || targetZ == -1) {
            displayTargetX = -1;
            displayTargetZ = -1;
            targetScale = 0;
        }

        if (displayTargetX == -1) {
            displayTargetX = targetX;
            displayTargetZ = targetZ;
        }

        displayTargetX = displayTargetX + (targetX - displayTargetX) * 0.1F;
        displayTargetZ = displayTargetZ + (targetZ - displayTargetZ) * 0.1F;
        displayScale = displayScale + (targetScale - displayScale) * 0.1F;

        int clickX = (int)(displayTargetX * mapScale);
        int clickZ = (int)(displayTargetZ * mapScale);

        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(left + mapLeft, top + mapTop, 10.0F);

//        Ensures scaling looks like its origin is middle of board
        float scaleOffset = 64 * mapScale * (1 - displayScale);
        guiGraphics.pose().translate(scaleOffset, scaleOffset, 1);
        guiGraphics.pose().scale(displayScale, displayScale, 1);

        guiGraphics.blit(TARGET_HORIZONTAL, - 4, clickZ - 2, 0, 0, 64, 5, 64, 5);
        guiGraphics.blit(TARGET_VERTICAL, clickX - 2, - 4, 0, 0, 5, 64, 5, 64);

        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(0, 0, 20.0F);
        guiGraphics.blit(TARGET_MIDDLE, clickX - 4, clickZ - 4, 0, 0, 9, 9, 9, 9);
        guiGraphics.pose().popPose();
        guiGraphics.pose().popPose();
    }

    @Override
    public boolean mouseClicked(double x, double z, int i) {
        Integer mapId = MapItem.getMapId(map);
        MapItemSavedData mapData = MapItem.getSavedData(mapId, this.minecraft.level);

        if (mapData != null) {
            int targetX = (int) ((x - this.leftPos - mapLeft) / mapScale);
            int targetZ = (int) ((z - this.topPos - mapTop) / mapScale);

            if (targetX >= 0 && targetX <= 128 && targetZ >= 0 && targetZ <= 128) {
                menu.clickMap(targetX, targetZ);
            }
        }

        if (hoveringButton) {
            menu.clickLaunch();
        }

        return super.mouseClicked(x, z, i);
    }
}
