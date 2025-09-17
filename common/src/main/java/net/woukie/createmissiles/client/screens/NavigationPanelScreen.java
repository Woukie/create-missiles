package net.woukie.createmissiles.client.screens;

import net.fabricmc.loader.impl.lib.sat4j.core.Vec;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import net.woukie.createmissiles.CreateMissiles;
import net.woukie.createmissiles.inventory.NavigationPanelMenu;
import net.woukie.createmissiles.missiles.parts.ChassisType;
import net.woukie.createmissiles.missiles.parts.ThrusterType;
import net.woukie.createmissiles.missiles.parts.WarheadType;
import net.woukie.createmissiles.missiles.trajectories.BallisticTrajectory;
import net.woukie.createmissiles.registry.PartTypes;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector2d;
import org.joml.Vector2i;
import org.joml.Vector3d;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class NavigationPanelScreen extends AbstractContainerScreen<NavigationPanelMenu> {
    private static final ResourceLocation BACKGROUND = new ResourceLocation(CreateMissiles.MOD_ID, "textures/gui/container/navigation_panel.png");
    private static final ResourceLocation MAP_ERROR = new ResourceLocation(CreateMissiles.MOD_ID, "textures/gui/sprites/container/map_error.png");
    private static final ResourceLocation NO_MAP = new ResourceLocation(CreateMissiles.MOD_ID, "textures/gui/sprites/container/no_map.png");
    private static final ResourceLocation MAP_TARGET_HORIZONTAL = new ResourceLocation(CreateMissiles.MOD_ID, "textures/gui/sprites/container/target_horizontal.png");
    private static final ResourceLocation MAP_TARGET_VERTICAL = new ResourceLocation(CreateMissiles.MOD_ID, "textures/gui/sprites/container/target_vertical.png");
    private static final ResourceLocation MAP_TARGET = new ResourceLocation(CreateMissiles.MOD_ID, "textures/gui/sprites/container/target_marker.png");
    private static final ResourceLocation TRAJECTORY_NO_MAP = new ResourceLocation(CreateMissiles.MOD_ID, "textures/gui/sprites/container/trajectory_no_map.png");
    private static final ResourceLocation TRAJECTORY_TARGET_LOADING = new ResourceLocation(CreateMissiles.MOD_ID, "textures/gui/sprites/container/trajectory_target_loading.png");
    private static final ResourceLocation INVALID_SETUP = new ResourceLocation(CreateMissiles.MOD_ID, "textures/gui/sprites/container/invalid_setup.png");
    private static final ResourceLocation FUEL = new ResourceLocation(CreateMissiles.MOD_ID, "textures/gui/sprites/container/fuel.png");
    private static final ResourceLocation WHITE_DOT = new ResourceLocation(CreateMissiles.MOD_ID, "textures/gui/sprites/container/white_dot.png");
    private static final ResourceLocation FUEL_GAUGE = new ResourceLocation(CreateMissiles.MOD_ID, "textures/gui/sprites/container/fuel_gauge.png");
    private static final ResourceLocation MIN_THRUST_DURATION = new ResourceLocation(CreateMissiles.MOD_ID, "textures/gui/sprites/container/min_thrust_duration.png");

    private static final int mapLeft = 8;
    private static final int mapTop = 16;
    private static final int mapWidth = 54;
    private static final int mapHeight = 54;

    private static final int fuelLeft = 106;
    private static final int fuelTop = 16;
    private static final int fuelWidth = 5;
    private static final int fuelHeight = 54;

    private static final int trajectoryLeft = 114;
    private static final int trajectoryTop = 16;
    private static final int trajectoryWidth = 53;
    private static final int trajectoryHeight = 53;
    private static final int trajectoryPadding = 2;

    private double currentMapCrosshairX = 0;
    private double currentMapCrosshairZ = 0;
    private double currentFuel1 = 0;
    private double currentFuel2 = 0;
    private double currentMinThrustDuration1 = 0;
    private double currentMinThrustDuration2 = 0;
    private double maxThrustDuration = 0;
    private double minThrustDuration = 0;

    private final List<Vector2d> positions = new ArrayList<>();
    private double maxHeight = 256;
    private double lastFuelPercent = 0;
    private double distanceToTarget = 0;
    private BlockPos lastTargetPos = new BlockPos(0, 0, 0);
    private Float lastUpperLaunchAngle = null;
    private Float lastLowerLaunchAngle = null;

    public NavigationPanelScreen(NavigationPanelMenu abstractContainerMenu, Inventory inventory, Component component) {
        super(abstractContainerMenu, inventory, component);
    }

    @Override
    public void render(@NotNull GuiGraphics gui, int i, int j, float f) {
        super.render(gui, i, j, f);
        this.renderTooltip(gui, i, j);

        renderCrosshair(gui);
    }

    @Override
    protected void renderBg(GuiGraphics gui, float f, int i, int j) {
        gui.pose().pushPose();
        gui.pose().translate(leftPos, topPos, -1);

        gui.blit(BACKGROUND, 0, 0, 0, 0, this.imageWidth, this.imageHeight);

        simulateTrajectory();
        renderMap(gui);
        renderFuel(gui);
        renderTrajectory(gui);
        renderText(gui);
        gui.pose().popPose();
    }

    @Override
    public boolean mouseClicked(double x, double z, int i) {
        if (isHovering(mapLeft, mapTop, mapWidth, mapHeight, x, z)) {
            double mapCrosshairX = (x - leftPos - mapLeft) * (128D / mapWidth);
            double mapCrosshairZ = (z - topPos - mapTop) * (128D / mapHeight);

            ItemStack map = getMenu().getMap();
            if (minecraft == null ||
                    minecraft.level == null ||
                    map == null ||
                    MapItem.getSavedData(map, minecraft.level) == null
            )
                return super.mouseClicked(x, z, i);

            getMenu().clickMap(mapCrosshairX, mapCrosshairZ);
        }

        if (isHovering(fuelLeft, fuelTop, fuelWidth, fuelHeight, x, z)) {
            double fuelClickZ = z - topPos - fuelTop;
            getMenu().clickFuel((float) (1 - fuelClickZ / fuelHeight));
        }

        return super.mouseClicked(x, z, i);
    }

    private void renderMap(GuiGraphics gui) {
        assert minecraft != null && minecraft.level != null;

        ItemStack mapItem = getMenu().getMap();
        if (mapItem == null){
            gui.blit(NO_MAP, mapLeft, mapTop, 5, 0, 0, mapWidth, mapHeight, mapWidth, mapHeight);
            return;
        }

        Integer mapId = MapItem.getMapId(mapItem);
        MapItemSavedData mapData = MapItem.getSavedData(mapId, minecraft.level);
        if(mapId == null || mapData == null) {
            gui.blit(MAP_ERROR, mapLeft, mapTop, 5, 0, 0, mapWidth, mapHeight, mapWidth, mapHeight);
            return;
        }

        gui.pose().pushPose();
        gui.pose().translate(mapLeft, mapTop, 1);
        gui.pose().scale((float) mapWidth / 128, (float) mapHeight / 128, 1);
        this.minecraft.gameRenderer.getMapRenderer().render(gui.pose(), gui.bufferSource(), mapId, mapData, true, 15728880);
        gui.pose().popPose();
    }

    private void renderCrosshair(GuiGraphics gui) {
        int targetMapCrosshairX = getMenu().getMapCrosshairX();
        int targetMapCrosshairZ = getMenu().getMapCrosshairZ();

        currentMapCrosshairX += (targetMapCrosshairX - currentMapCrosshairX) * 0.1F;
        currentMapCrosshairZ += (targetMapCrosshairZ - currentMapCrosshairZ) * 0.1F;

        int scaledX = (int) (currentMapCrosshairX * mapWidth / 128);
        int scaledY = (int) (currentMapCrosshairZ * mapHeight / 128);

        gui.pose().pushPose();
        gui.pose().translate(mapLeft + leftPos, mapTop + topPos, 10);

        // Scissor ignores poses
        int scissorLeft = leftPos + mapLeft;
        int scissorTop = topPos + mapTop;
        gui.enableScissor(scissorLeft - 1, scissorTop - 1, scissorLeft + mapWidth + 1, scissorTop + mapHeight + 1);

        gui.blit(MAP_TARGET, scaledX - 4, scaledY - 4, 2, 0, 0, 9, 9, 9, 9);

        gui.pose().pushPose();
        gui.pose().translate(0, 0, 1);
        gui.blit(MAP_TARGET_VERTICAL, scaledX - 2, -4, 1, 0, 0, 5, 62, 5 ,62);
        gui.blit(MAP_TARGET_HORIZONTAL, -4, scaledY - 2, 1, 0, 0, 62, 5, 62, 5);
        gui.pose().popPose();

        gui.disableScissor();
        gui.pose().popPose();
    }

    private void renderTrajectory(GuiGraphics gui) {
        assert minecraft != null;

        if (!getMenu().launchPadExists() || getMenu().assemblyPanelAbsent()){
            gui.blit(INVALID_SETUP, trajectoryLeft, trajectoryTop, 5, 0, 0, trajectoryWidth, trajectoryHeight, trajectoryWidth, trajectoryHeight);
            return;
        }

        ItemStack mapItem = getMenu().getMap();
        if (mapItem == null){
            gui.blit(TRAJECTORY_NO_MAP, trajectoryLeft, trajectoryTop, 5, 0, 0, trajectoryWidth, trajectoryHeight, trajectoryWidth, trajectoryHeight);
            return;
        }

        BlockPos source = getMenu().getSource();
        BlockPos target = getMenu().getTarget();

        if (source == null || target == null) {
            gui.blit(TRAJECTORY_TARGET_LOADING, trajectoryLeft, trajectoryTop, 5, 0, 0, trajectoryWidth, trajectoryHeight, trajectoryWidth, trajectoryHeight);
            return;
        }

        ItemStack warhead = getMenu().getWarhead();
        ItemStack chassis = getMenu().getChassis();
        ItemStack thruster = getMenu().getThruster();

        if (warhead == null || chassis == null || thruster == null) {
            gui.blit(INVALID_SETUP, trajectoryLeft, trajectoryTop, 5, 0, 0, trajectoryWidth, trajectoryHeight, trajectoryWidth, trajectoryHeight);
            return;
        }

        if (positions.isEmpty()) return;

        gui.pose().pushPose();
        gui.pose().translate(trajectoryLeft + trajectoryPadding, trajectoryTop + trajectoryPadding, 0);

        double yBottom = Math.min(target.getY(), source.getY());
        double yTop = maxHeight;
        double xStart = 0;
        double xEnd = Vector3d.distance(target.getX(), 0, target.getZ(), source.getX(), 0, source.getZ());

        int width = trajectoryWidth - trajectoryPadding * 2;
        int height = trajectoryHeight - trajectoryPadding * 2;

        int xLine = Math.max((int) mapRange(source.getY(), yBottom, yTop, 0, height), 0);
        if (xLine < height) {
            gui.hLine(0, width, height - xLine, 0x77FFFFFF);
        }
        gui.vLine(0, 0, height, 0x77FFFFFF);


        Vector2i lastPixelPos = null;
        for (int i = 0; i < width; i++) {
            Vector2d currentPos = positions.get(i * positions.size() / width);
            Vector2i currentPixelPos = new Vector2i(
                    (int) mapRange(currentPos.x, xStart, xEnd, 0, width),
                    (int) mapRange(currentPos.y, yBottom, yTop, 0, height)
            );

            if (lastPixelPos != null) {
                traverseLine(currentPixelPos, lastPixelPos, interpPos -> {
                    Vector2i p = new Vector2i((int) interpPos.x, (int) interpPos.y);
                    if (p.y < 0 || p.y > height || p.x < 0 || p.x > width) return;
                    gui.hLine(p.x, p.x, height - p.y, 0xFFFFFFFF);
                });
            }

            lastPixelPos = currentPixelPos;
        }

        gui.pose().pushPose();
        gui.pose().scale(0.5f, 0.5f, 1);
        float redness = -(float) Math.pow(-(distanceToTarget / 10 + 1), -1);
        int colour = FastColor.ARGB32.lerp(redness, 0xFF0000, 0xFFFFFF);
        gui.drawString(
                this.font,
                new DecimalFormat("#0.00").format(distanceToTarget) + "m",
                0,
                0,
                colour
        );
        gui.pose().popPose();
        gui.pose().popPose();
    }

    private void renderText(GuiGraphics gui) {
        gui.pose().pushPose();
        float scale = 0.5f;
        gui.pose().scale(scale, scale, scale);
        String textTime = Double.toString(maxThrustDuration);
        int paddingX = 3;
        gui.drawString(
                this.font,
                textTime,
                (int)((fuelLeft - this.font.width(textTime) - paddingX) / scale),
                (int)((fuelTop - 2) / scale),
                0x000000,
                false
        );
        String textDeltaT = "Δt";
        gui.drawString(
                this.font,
                textDeltaT,
                (int)((fuelLeft - this.font.width(textDeltaT) - paddingX) / scale),
                (int)((fuelTop + fuelHeight - 2) / scale),
                0x000000,
                false);
        gui.pose().popPose();
    }

    private void renderFuel(GuiGraphics gui) {
        currentFuel1 += (getMenu().getFuelPercent() - currentFuel2) * 0.1;
        currentFuel2 += (currentFuel1 - currentFuel2) * 0.1;

//        currentMinThrustDuration1 += (minThrustDuration - currentMinThrustDuration2) * 0.1;
//        currentMinThrustDuration2 += (currentMinThrustDuration1 - currentMinThrustDuration2) * 0.1;

        int barHeight = fuelHeight - (int)(currentFuel2 * fuelHeight);
        barHeight = Math.min(Math.max(barHeight, 1), fuelHeight);

//        int barHeightThrust = fuelHeight - (int)(currentMinThrustDuration2 * fuelHeight);
//        barHeightThrust = Math.min(Math.max(barHeightThrust, 0), fuelHeight);

        gui.pose().pushPose();
        gui.pose().translate(fuelLeft, fuelTop, 0);
        gui.blit(FUEL, 0, barHeight, 0, 0, barHeight, fuelWidth, fuelHeight - barHeight, fuelWidth, barHeight);
        gui.blit(FUEL_GAUGE, -2, barHeight - 3, 1, 0, 0, 9, 5, 9, 5);
//        gui.blit(MIN_THRUST_DURATION, -7, barHeightThrust - 2, 1, 0, 0, 5, 6, 5, 6);
        gui.pose().popPose();
    }

    private void simulateTrajectory() {
        BlockPos target = getMenu().getTarget();
        if (target == null) {
            distanceToTarget = 0;
            positions.clear();
            return;
        }

        if (target.equals(lastTargetPos) &&
                lastFuelPercent == getMenu().getFuelPercent() &&
                lastUpperLaunchAngle == getMenu().getUpperLaunchAngle() &&
                lastLowerLaunchAngle == getMenu().getLowerLaunchAngle()
        ) return;
        lastTargetPos = getMenu().getTarget();
        lastFuelPercent = getMenu().getFuelPercent();
        lastUpperLaunchAngle = getMenu().getUpperLaunchAngle();
        lastLowerLaunchAngle = getMenu().getLowerLaunchAngle();

        WarheadType warheadType = (WarheadType) PartTypes.get(getMenu().getWarhead());
        ChassisType chassisType = (ChassisType) PartTypes.get(getMenu().getChassis());
        ThrusterType thrusterType = (ThrusterType) PartTypes.get(getMenu().getThruster());
        if (warheadType == null || chassisType == null || thrusterType == null) return;

        maxThrustDuration = chassisType.getFuelCapacity() / thrusterType.getBurnRate();

        BlockPos source = getMenu().getSource();
        Vector3d start = new Vector3d(source.getX(), source.getY(), source.getZ()).add(.5, .5, .5);
        Vector3d end = new Vector3d(target.getX(), target.getY(), target.getZ()).add(.5, .5, .5);
        float launchAngle = (getMenu().getLowerLaunchAngle() + getMenu().getUpperLaunchAngle()) / 2;

        positions.clear();
        BallisticTrajectory simulatedTrajectory = new BallisticTrajectory(
                new Vector3d(start),
                new Vector3d(end),
                warheadType,
                chassisType,
                thrusterType,
                launchAngle,
                getMenu().getFuelPercent()
        );

        double minDistance = Double.POSITIVE_INFINITY;
        double previousDistance = Double.POSITIVE_INFINITY;
        while (true) {
            Vector3d currentPosition = new Vector3d(simulatedTrajectory.getPosition());
            Vector2d currentPosition2D = new Vector2d(new Vector2d(currentPosition.x, currentPosition.z).distance(new Vector2d(start.x, start.z)), currentPosition.y);
            positions.add(currentPosition2D);

            double currentDistance = end.distance(currentPosition);
            minDistance = Math.min(minDistance, currentDistance);
            boolean descending = simulatedTrajectory.getVelocity().y < 0;
            if (descending && currentDistance > previousDistance) break;
            previousDistance = currentDistance;
            simulatedTrajectory.tick();
        }
        distanceToTarget = minDistance;

        BallisticTrajectory maxFuelSimulatedTrajectory = new BallisticTrajectory(
                new Vector3d(start),
                new Vector3d(end),
                warheadType,
                chassisType,
                thrusterType,
                launchAngle,
                1
        );

        while (!(maxFuelSimulatedTrajectory.getVelocity().y < 0)) {
            maxHeight = maxFuelSimulatedTrajectory.getPosition().y;
            maxFuelSimulatedTrajectory.tick();
        }
    }

    private void traverseLine(Vector2i startI, Vector2i endI, Consumer<Vector2d> callback) {
        Vector2d start = new Vector2d(startI.x, startI.y);
        Vector2d end = new Vector2d(endI.x, endI.y);
        Vector2d currentPos = new Vector2d(start);
        Vector2d increment = new Vector2d(end).sub(start).normalize();
        double distanceSqr = currentPos.distanceSquared(end);
        while (currentPos.distanceSquared(start) < distanceSqr) {
            callback.accept(new Vector2d(currentPos));
            currentPos.add(increment);
        }
    }

    private double mapRange(double value, double oldMin, double oldMax, double newMin, double nawMax) {
        return newMin + (value - oldMin) * (nawMax - newMin) / (oldMax - oldMin);
    }
}
