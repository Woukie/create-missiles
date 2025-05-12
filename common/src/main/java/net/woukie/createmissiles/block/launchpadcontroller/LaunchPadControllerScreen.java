package net.woukie.createmissiles.block.launchpadcontroller;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import net.minecraft.world.phys.Vec2;
import net.woukie.createmissiles.CreateMissiles;
import net.woukie.createmissiles.missilemanager.Trajectory;
import net.woukie.createmissiles.missilemanager.TrajectoryData;

import static net.woukie.createmissiles.block.launchpadcontroller.LaunchPadControllerBlockEntity.SLOT_MAP;

public class LaunchPadControllerScreen extends AbstractContainerScreen<LaunchPadControllerMenu> {
    private static final int mapLeft = 108;
    private static final int mapTop = 17;
    private static final int buttonLeft = 88;
    private static final int buttonTop = 14;
    private static final int buttonWidth = 16;
    private static final int buttonHeight = 16;
    private static final int buttonCoverWidth = 17;
    private static final int buttonCoverHeight = 16;
    private static final int fuelLeft = 27;
    private static final int fuelTop = 14;
    private static final int fuelWidth = 58;
    private static final int fuelHeight = 6;
    private static final int trajectoryLeft = 30;
    private static final int trajectoryTop = 26;
    private static final int trajectoryWidth = 52;
    private static final int trajectoryHeight = 37;
    private static final float mapScale = 0.4375F;
    private static final ResourceLocation BACKGROUND = new ResourceLocation(CreateMissiles.MOD_ID, "textures/gui/container/launch_pad_controller.png");
    private static final ResourceLocation BUTTON_COVER = new ResourceLocation(CreateMissiles.MOD_ID, "textures/gui/container/button_cover.png");
    private static final ResourceLocation BACKGROUND_HOVERING_BUTTON = new ResourceLocation(CreateMissiles.MOD_ID, "textures/gui/container/launch_pad_controller_hover.png");
    private static final ResourceLocation TARGET_MIDDLE = new ResourceLocation(CreateMissiles.MOD_ID, "textures/gui/sprites/container/target_marker.png");
    private static final ResourceLocation TARGET_HORIZONTAL = new ResourceLocation(CreateMissiles.MOD_ID, "textures/gui/sprites/container/target_horizontal.png");
    private static final ResourceLocation TARGET_VERTICAL = new ResourceLocation(CreateMissiles.MOD_ID, "textures/gui/sprites/container/target_vertical.png");
    private static final ResourceLocation FUEL = new ResourceLocation(CreateMissiles.MOD_ID, "textures/gui/sprites/container/fuel.png");
    private static final ResourceLocation FUEL_GLASS = new ResourceLocation(CreateMissiles.MOD_ID, "textures/gui/sprites/container/fuel_glass.png");
    private static final ResourceLocation FUEL_GAUGE = new ResourceLocation(CreateMissiles.MOD_ID, "textures/gui/sprites/container/fuel_gauge.png");
    private static final ResourceLocation MAP_ERROR = new ResourceLocation(CreateMissiles.MOD_ID, "textures/gui/sprites/container/map_error.png");
    private static final ResourceLocation GREEN_DOT = new ResourceLocation(CreateMissiles.MOD_ID, "textures/gui/sprites/container/green_dot.png");
    private static final ResourceLocation RED_DOT = new ResourceLocation(CreateMissiles.MOD_ID, "textures/gui/sprites/container/red_dot.png");

    private Trajectory trajectory;

    private boolean hoveringButton;

    private float displayScale = 0;
    private float displayTargetX = -1;
    private float displayTargetZ = -1;

    private float buttonCoverPercentage = 0F;

    public LaunchPadControllerScreen(LaunchPadControllerMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
        this.titleLabelY -= 2;
    }

    public void render(GuiGraphics guiGraphics, int l, int t, float f) {
        super.render(guiGraphics, l, t, f);
        this.renderTooltip(guiGraphics, l, t);

//        TODO: only call if null or if data has changed, especially when proper physics is added
        updateTrajectory();

        int plots = 100;
        double totalTime = trajectory.getImpactTime();
        boolean canHitTarget = trajectory.canHitTarget();
        BlockPos target = trajectory.getData().target;
        BlockPos source = trajectory.getData().source;
        double totalDistanceX = new Vec2(target.getX() - source.getX(), target.getZ() - source.getZ()).length();
        for (int i = 0; i < plots; i++) {
            double time = ((double)i / (double)plots) * totalTime;
            Vec2 position = trajectory.getLocalPosition((float)time);

            int x = (int)(trajectoryWidth * (position.x / totalDistanceX));
            int maxY = minecraft.level.getMaxBuildHeight();
            int minY = minecraft.level.getMinBuildHeight();
            int y = (int)(trajectoryHeight * (1 - ((-minY + position.y) / (maxY - minY))));

            guiGraphics.blit(canHitTarget ? GREEN_DOT : RED_DOT, x + trajectoryLeft + this.leftPos, y + trajectoryTop + this.topPos, 10, 0, 0, 1, 1, 1, 1);
            guiGraphics.blit(RED_DOT, x + trajectoryLeft + this.leftPos, maxY + trajectoryTop + this.topPos, 10, 0, 0, 1, 1, 1, 1);
            guiGraphics.blit(RED_DOT, x + trajectoryLeft + this.leftPos, maxY + trajectoryTop + this.topPos, 10, 0, 0, 1, 1, 1, 1);
        }
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

        float targetButtonCoverPercentage = this.menu.armed() ? 1F : 0F;
        buttonCoverPercentage = buttonCoverPercentage + (targetButtonCoverPercentage - buttonCoverPercentage) * 0.1F;

        boolean isLastFrame = buttonCoverPercentage > 0.9F;
        if (isLastFrame && hoveringButton) {
            guiGraphics.blit(BACKGROUND_HOVERING_BUTTON, left, top, 0, 0, this.imageWidth, this.imageHeight);
        } else {
            guiGraphics.blit(BACKGROUND, left, top, 0, 0, this.imageWidth, this.imageHeight);
        }

        int offset = Math.round(buttonCoverPercentage * buttonCoverWidth);
        guiGraphics.blit(BUTTON_COVER, buttonLeft + left, buttonTop + top, 10, offset, 0, buttonCoverWidth - offset, buttonCoverHeight, buttonCoverWidth, buttonCoverHeight);

        ItemStack map = this.menu.getSlot(SLOT_MAP).getItem();

        int targetScale = 1;
        if (map.is(Items.FILLED_MAP)) {
            Integer mapId = MapItem.getMapId(map);
            MapItemSavedData mapData = MapItem.getSavedData(mapId, this.minecraft.level);

            guiGraphics.pose().pushPose();
            guiGraphics.pose().translate(left + mapLeft, top + mapTop, 1.0F);
            guiGraphics.pose().scale(mapScale, mapScale, 1.0F);
            if (mapData != null) {
                this.minecraft.gameRenderer.getMapRenderer().render(guiGraphics.pose(), guiGraphics.bufferSource(), mapId, mapData, true, 15728880);
            } else {
                guiGraphics.blit(MAP_ERROR, 0, 0, 0, 0, 0, 128, 128, 128, 128);
            }
            guiGraphics.flush();
            guiGraphics.pose().popPose();
        } else {
            targetScale = 0;
        }

//      Map crosshair is num from 0 to 128
//      Map is 56x56 (128 * mapScale)
        int mapCrosshairX = this.getMenu().getMapCrosshairX();
        int mapCrosshairZ = this.getMenu().getMapCrosshairZ();

        if (displayTargetX == -1) {
            displayTargetX = mapCrosshairX;
            displayTargetZ = mapCrosshairZ;
        }

        displayTargetX = displayTargetX + (mapCrosshairX - displayTargetX) * 0.1F;
        displayTargetZ = displayTargetZ + (mapCrosshairZ - displayTargetZ) * 0.1F;
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

//        Fuel gauge
        float fuelPercentage = 0.25F;
        int fuelOffset = (int)(fuelPercentage * fuelWidth);
        guiGraphics.blit(FUEL, this.leftPos + fuelLeft, this.topPos + fuelTop, 1, 0, 0, fuelWidth, fuelHeight, fuelWidth, fuelHeight);
        guiGraphics.blit(FUEL_GLASS, this.leftPos + fuelLeft, this.topPos + fuelTop, 1, 0, 0, fuelWidth, fuelHeight, fuelWidth, fuelHeight);
    }

    @Override
    public boolean mouseClicked(double x, double z, int i) {
        ItemStack map = this.menu.getSlot(SLOT_MAP).getItem();
        Integer mapId = MapItem.getMapId(map);
        MapItemSavedData mapData = MapItem.getSavedData(mapId, this.minecraft.level);

        if (mapData != null) {
            double mapCrosshairX = ((x - this.leftPos - mapLeft) / mapScale);
            double mapCrosshairZ = ((z - this.topPos - mapTop) / mapScale);

            if (mapCrosshairX >= 0 && mapCrosshairX <= 128 && mapCrosshairZ >= 0 && mapCrosshairZ <= 128) {
                menu.clickMap(mapCrosshairX, mapCrosshairZ);
            }
        }

        if (hoveringButton) {
            menu.clickLaunch();
        }

        return super.mouseClicked(x, z, i);
    }

    private void updateTrajectory() {
        Level level = this.minecraft.level;
        TrajectoryData newData = new TrajectoryData(
                level,
                menu.getPos(),
                menu.getImpactPos(),
                0,
                menu.getWarhead(),
                menu.getChassis(),
                menu.getThruster()
        );

        if (trajectory == null) {
            trajectory = new Trajectory(newData);
        } else {
            trajectory.setData(newData);
        }
    }
}
