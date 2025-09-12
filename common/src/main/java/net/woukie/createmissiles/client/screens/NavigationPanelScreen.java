package net.woukie.createmissiles.client.screens;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import net.woukie.createmissiles.CreateMissiles;
import net.woukie.createmissiles.inventory.NavigationPanelMenu;
import net.woukie.createmissiles.missilemanager.parts.ChassisType;
import net.woukie.createmissiles.missilemanager.parts.MissilePartType;
import net.woukie.createmissiles.missilemanager.parts.ThrusterType;
import net.woukie.createmissiles.missilemanager.parts.WarheadType;
import net.woukie.createmissiles.missilemanager.trajectories.TrajectoryHelper;
import net.woukie.createmissiles.registry.PartTypes;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector2d;
import org.joml.Vector3d;

import java.util.ArrayList;
import java.util.List;

import static net.woukie.createmissiles.missilemanager.trajectories.TrajectoryHelper.findLaunchAngle;

public class NavigationPanelScreen extends AbstractContainerScreen<NavigationPanelMenu> {
    private static final ResourceLocation BACKGROUND = new ResourceLocation(CreateMissiles.MOD_ID, "textures/gui/container/navigation_panel.png");
    private static final ResourceLocation MAP_ERROR = new ResourceLocation(CreateMissiles.MOD_ID, "textures/gui/sprites/container/map_error.png");
    private static final ResourceLocation MAP_TARGET_HORIZONTAL = new ResourceLocation(CreateMissiles.MOD_ID, "textures/gui/sprites/container/target_horizontal.png");
    private static final ResourceLocation MAP_TARGET_VERTICAL = new ResourceLocation(CreateMissiles.MOD_ID, "textures/gui/sprites/container/target_vertical.png");
    private static final ResourceLocation MAP_TARGET = new ResourceLocation(CreateMissiles.MOD_ID, "textures/gui/sprites/container/target_marker.png");
    private static final ResourceLocation FUEL = new ResourceLocation(CreateMissiles.MOD_ID, "textures/gui/sprites/container/fuel.png");
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
    private static final int trajectoryWidth = 54;
    private static final int trajectoryHeight = 54;

    private double currentMapCrosshairX = 0;
    private double currentMapCrosshairZ = 0;
    private double currentFuel2 = 0;
    private double currentFuel1 = 0;
    private double currentMinThrustDuration1 = 0;
    private double currentMinThrustDuration2 = 0;

    private final ArrayList<String> errors = new ArrayList<>();
    private List<Vector2d> missilePositions;
    private double maxHeight = 0;
    private double lastFuelPercent = 0;
    private BlockPos lastTargetPos = new BlockPos(0, 0, 0);

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
        errors.clear();
        gui.pose().pushPose();
        gui.pose().translate(leftPos, topPos, -1);

        gui.blit(BACKGROUND, 0, 0, 0, 0, this.imageWidth, this.imageHeight);

        renderMap(gui);
        renderFuel(gui);
        renderTrajectory(gui);
        renderErrors(gui);
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
            getMenu().clickFuel(1 - fuelClickZ / fuelHeight);
        }

        return super.mouseClicked(x, z, i);
    }

    private void renderMap(GuiGraphics gui) {
        assert minecraft != null && minecraft.level != null;

        ItemStack mapItem = getMenu().getMap();
        if (mapItem == null){
            errors.add(Component.translatable("gui.createmissiles.navigation_panel.no_map").getString());
            return;
        }

        Integer mapId = MapItem.getMapId(mapItem);
        MapItemSavedData mapData = MapItem.getSavedData(mapId, minecraft.level);
        if(mapId == null || mapData == null) {
            gui.blit(MAP_ERROR, mapLeft, mapTop, 5, 0, 0, mapWidth, mapHeight, mapWidth, mapHeight);
            errors.add(Component.translatable("gui.createmissiles.navigation_panel.no_map_data").getString());
            return;
        }

        gui.pose().pushPose();
        gui.pose().translate(mapLeft, mapTop, 1);
        gui.pose().scale((float) mapWidth / 128, (float) mapHeight / 128, 1);
        this.minecraft.gameRenderer.getMapRenderer().render(gui.pose(), gui.bufferSource(), mapId, mapData, true, 15728880);
        gui.pose().popPose();
    }

    private void renderErrors(GuiGraphics gui) {
        if (errors.isEmpty())
            return;
        String errorLog = String.join("\n", errors);
        gui.pose().pushPose();
        gui.pose().translate(trajectoryLeft, trajectoryTop, 0);
        gui.pose().scale(0.5F, 0.5F, 1);
        gui.drawWordWrap(this.font, FormattedText.of(errorLog, Style.EMPTY.withColor(16777215)), 2, 2, trajectoryWidth * 2 - 4, trajectoryHeight * 2 - 4);
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

        if (!errors.isEmpty())
            return;

        if (!getMenu().launchPadExists()){
            errors.add(Component.translatable("gui.createmissiles.navigation_panel.no_launch_pad").getString());
            return;
        }

        if (getMenu().assemblyPanelAbsent()) {
            errors.add(Component.translatable("gui.createmissiles.navigation_panel.no_assembly_panel").getString());
            return;
        }

        BlockPos source = getMenu().getSource();
        BlockPos target = getMenu().getTarget();

        if (source == null || target == null) {
            errors.add(Component.translatable("gui.createmissiles.navigation_panel.no_target").getString());
            return;
        }

        ItemStack warhead = getMenu().getWarhead();
        ItemStack chassis = getMenu().getChassis();
        ItemStack thruster = getMenu().getThruster();

        if (warhead == null || chassis == null || thruster == null) {
            errors.add(Component.translatable("gui.createmissiles.navigation_panel.no_assemblies").getString());
            return;
        }

        if(lastFuelPercent != getMenu().getFuelPercent() || !lastTargetPos.equals(getMenu().getTarget())){
            reCalculateTrajectory(gui, target, source, warhead, chassis, thruster);
            lastTargetPos = getMenu().getTarget();
            lastFuelPercent = getMenu().getFuelPercent();
        }

        if(missilePositions == null) return;
        gui.pose().pushPose();
        gui.pose().translate(trajectoryLeft, trajectoryTop, 0);
        double targetDistance = Vector3d.distance(target.getX(), 0, target.getZ(), source.getX(), 0, source.getZ());
        double xScale = trajectoryWidth / Math.max(targetDistance, missilePositions.get(missilePositions.size() - 1).x);
        double yScale = trajectoryHeight / maxHeight;

        Vector2d prevPixel = new Vector2d(0, 0);
        for (int i = 0; i < trajectoryWidth; i ++) {
            int index = i * missilePositions.size() / trajectoryWidth;
            int x = (int) (missilePositions.get(index).x * xScale);
            int y = (int) (missilePositions.get(index).y * yScale);
            int color = 0xFFFFFFFF;
            gui.vLine(x, (int) -prevPixel.y + trajectoryHeight, -y + trajectoryHeight, color);
            prevPixel = new Vector2d(x, y);
        }
        gui.pose().popPose();
    }

    private void renderText(GuiGraphics gui) {
        gui.pose().pushPose();
        float scale = 0.6f;
        gui.pose().scale(scale, scale, scale);
        String textTime = Integer.toString(getMenu().getMaxThrustDuration());
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
        currentFuel1 += (getMenu().getFuelPercent() - currentFuel2) * 0.1F;
        currentFuel2 += (currentFuel1 - currentFuel2) * 0.1F;

        currentMinThrustDuration1 += (getMenu().getMinThrustDuration() - currentMinThrustDuration2) * 0.1F;
        currentMinThrustDuration2 += (currentMinThrustDuration1 - currentMinThrustDuration2) * 0.1F;

        int barHeight = fuelHeight - (int)(currentFuel2 * fuelHeight);
        barHeight = Math.min(Math.max(barHeight, 0), fuelHeight);

        int barHeightThrust = fuelHeight - (int)(currentMinThrustDuration2 * fuelHeight);
        barHeightThrust = Math.min(Math.max(barHeightThrust, 0), fuelHeight);

        gui.pose().pushPose();
        gui.pose().translate(fuelLeft, fuelTop, 0);
        gui.blit(FUEL, 0, barHeight, 0, 0, barHeight, fuelWidth, fuelHeight - barHeight, fuelWidth, barHeight);
        gui.blit(FUEL_GAUGE, -2, barHeight - 2, 1, 0, 0, 9, 5, 9, 5);
        gui.blit(MIN_THRUST_DURATION, -7, barHeightThrust - 2, 1, 0, 0, 5, 6, 5, 6);
        gui.pose().popPose();
    }

    private void reCalculateTrajectory(GuiGraphics gui, BlockPos target, BlockPos source, ItemStack warhead, ItemStack chassis, ItemStack thruster){
        WarheadType warheadType = (WarheadType) PartTypes.get(warhead);
        ChassisType chassisType = (ChassisType) PartTypes.get(chassis);
        ThrusterType thrusterType = (ThrusterType) PartTypes.get(thruster);

        TrajectoryHelper.MissileConfig missileConfig = new TrajectoryHelper.MissileConfig(thrusterType, chassisType, warheadType);
        double[] launchAngleRange = {0, 90};
        TrajectoryHelper.LaunchConfig launchConfig = new TrajectoryHelper.LaunchConfig(source, target, launchAngleRange, missileConfig.maxThrustDuration * menu.getFuelPercent(), missileConfig);

        double angle = findLaunchAngle(launchConfig, launchConfig.selectedThrustDuration);
        missilePositions = TrajectoryHelper.simulate(launchConfig, angle, launchConfig.selectedThrustDuration);
        List<Vector2d> maxData = TrajectoryHelper.simulate(launchConfig, angle, launchConfig.selectedThrustDuration);

        for (int g = 0; g < maxData.size(); g++) {
            maxHeight = Math.max(maxHeight, maxData.get(g).y);
        }
    }
}
