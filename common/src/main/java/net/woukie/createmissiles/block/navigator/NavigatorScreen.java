package net.woukie.createmissiles.block.navigator;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import net.woukie.createmissiles.CreateMissiles;
import net.woukie.createmissiles.item.schematic.ChassisSchematic;
import net.woukie.createmissiles.item.schematic.ThrusterSchematic;
import net.woukie.createmissiles.item.schematic.WarheadSchematic;
import net.woukie.createmissiles.missilemanager.Trajectory;
import net.woukie.createmissiles.missilemanager.TrajectoryData;
import net.woukie.createmissiles.registry.MissileItems;

public class NavigatorScreen extends AbstractContainerScreen<NavigatorMenu> {
    private static final ResourceLocation BACKGROUND = new ResourceLocation(CreateMissiles.MOD_ID, "textures/gui/container/navigator.png");
    private static final ResourceLocation MAP_ERROR = new ResourceLocation(CreateMissiles.MOD_ID, "textures/gui/sprites/container/map_error.png");
    private static final ResourceLocation MAP_TARGET_HORIZONTAL = new ResourceLocation(CreateMissiles.MOD_ID, "textures/gui/sprites/container/target_horizontal.png");
    private static final ResourceLocation MAP_TARGET_VERTICAL = new ResourceLocation(CreateMissiles.MOD_ID, "textures/gui/sprites/container/target_vertical.png");
    private static final ResourceLocation MAP_TARGET = new ResourceLocation(CreateMissiles.MOD_ID, "textures/gui/sprites/container/target_marker.png");
    private static final ResourceLocation FUEL = new ResourceLocation(CreateMissiles.MOD_ID, "textures/gui/sprites/container/fuel.png");
    private static final ResourceLocation FUEL_GAUGE = new ResourceLocation(CreateMissiles.MOD_ID, "textures/gui/sprites/container/fuel_gauge.png");

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

    private double currentMapCrosshairX = 64;
    private double currentMapCrosshairZ = 64;
    private double currentMapScale = 0;

    public NavigatorScreen(NavigatorMenu abstractContainerMenu, Inventory inventory, Component component) {
        super(abstractContainerMenu, inventory, component);
    }

    @Override
    protected void renderBg(GuiGraphics gui, float f, int i, int j) {
        gui.pose().pushPose();
        gui.pose().translate(leftPos, topPos, 0);

        gui.blit(BACKGROUND, leftPos, topPos, 0, 0, this.imageWidth, this.imageHeight);

        if (renderMap(gui))
            renderTrajectory(gui);

        renderFuel(gui);
    }

    @Override
    public boolean mouseClicked(double x, double z, int i) {
        if (isHovering(mapLeft, mapTop, mapWidth, mapHeight, x, z)) {
            double mapCrosshairX = x - leftPos - mapLeft;
            double mapCrosshairZ = z - topPos - mapTop;
            getMenu().clickMap(mapCrosshairX, mapCrosshairZ);
        }

        if (isHovering(fuelLeft, fuelTop, fuelWidth, fuelHeight, x, z)) {
            double fuelClickZ = z - topPos - fuelTop;
            getMenu().clickFuel(fuelClickZ / fuelHeight);
        }

        return super.mouseClicked(x, z, i);
    }

    private boolean renderMap(GuiGraphics gui) {
        assert minecraft != null && minecraft.level != null;

        gui.pose().pushPose();
        gui.pose().translate(mapLeft, mapTop, 0);

        ItemStack mapItem = getMenu().getMap();
        double targetMapScale = 1;
        boolean success = true;

        if (mapItem == null) {
            targetMapScale = 0;
            success = false;
        } else {
            Integer mapId = MapItem.getMapId(mapItem);
            MapItemSavedData mapData = MapItem.getSavedData(mapId, minecraft.level);

            if(mapId == null || mapData == null) {
                gui.blit(MAP_ERROR, mapLeft, mapTop, 0, 0, mapWidth, mapHeight);
            } else {
                minecraft.gameRenderer.getMapRenderer().render(gui.pose(), gui.bufferSource(), mapId, mapData, true, 15728880);
            }
        }

        int targetMapCrosshairX = getMenu().getMapCrosshairX();
        int targetMapCrosshairZ = getMenu().getMapCrosshairZ();

        currentMapCrosshairX = currentMapCrosshairX + (targetMapCrosshairX - currentMapCrosshairX) * 0.1F;
        currentMapCrosshairZ = currentMapCrosshairZ + (targetMapCrosshairZ - currentMapCrosshairZ) * 0.1F;
        currentMapScale = currentMapScale + (targetMapScale - currentMapScale) * 0.1F;

//        Assuming k is depth
        gui.blit(MAP_TARGET, (int)currentMapCrosshairX - 4, (int)currentMapCrosshairZ - 4, 2, 0, 0, 9, 9, 9, 9);
        gui.blit(MAP_TARGET_VERTICAL, (int)currentMapCrosshairX - 2, -4, 1, 0, 0, 5, 64, 5 ,64);
        gui.blit(MAP_TARGET_HORIZONTAL, -4, (int)currentMapCrosshairZ - 2, 1, 0, 0, 64, 5, 64, 5);

        gui.pose().popPose();
        return success;
    }

    private void renderTrajectory(GuiGraphics gui) {
        assert minecraft != null;

        gui.pose().pushPose();
        gui.pose().translate(trajectoryLeft, trajectoryTop, 0);

        Container schematicatorSlots = getMenu().getSchematicatorContainer();
        BlockPos source = getMenu().getSource();
        BlockPos target = getMenu().getTarget();

        if (schematicatorSlots == null) {
            gui.drawWordWrap(this.font, FormattedText.of(Component.translatable("gui.createmissiles.navigator.no_schematicator").toString()), 0, 0, trajectoryWidth, trajectoryHeight);
            return;
        }

        if (source == null || target == null) {
            gui.drawWordWrap(this.font, FormattedText.of(Component.translatable("gui.createmissiles.navigator.no_target").toString()), 0, 0, trajectoryWidth, trajectoryHeight);
            return;
        }

        ItemStack warhead = schematicatorSlots.getItem(0);
        ItemStack chassis = schematicatorSlots.getItem(1);
        ItemStack thruster = schematicatorSlots.getItem(2);

        if (!(warhead.is(MissileItems.WARHEAD_SCHEMATIC.get()) && warhead.is(MissileItems.CHASSIS_SCHEMATIC.get()) && warhead.is(MissileItems.THRUSTER_SCHEMATIC.get()))) {
            gui.drawWordWrap(this.font, FormattedText.of(Component.translatable("gui.createmissiles.navigator.no_schematics").toString()), 0, 0, trajectoryWidth, trajectoryHeight);
            return;
        }

        TrajectoryData trajectoryData = new TrajectoryData(
                minecraft.level,
                source,
                target,
                getMenu().getFuelPercent(),
                0,
                WarheadSchematic.getWarhead(warhead),
                ChassisSchematic.getChassis(chassis),
                ThrusterSchematic.getThruster(thruster)
        );

        Trajectory trajectory = new Trajectory(trajectoryData);

//        Sample points and position between
//        minY = min(sourceY, impactY)
//        maxY = max height of trajectory if shot at same angle with full fuel level
//        minX = 0
//        maxX = (targetXZ - sourceXZ).length()

        gui.pose().popPose();
    }

    private void renderFuel(GuiGraphics gui) {
        gui.pose().pushPose();
        gui.pose().translate(trajectoryLeft, trajectoryTop, 0);

        int barHeight = (int)(getMenu().getFuelPercent() * fuelHeight);
        gui.blit(FUEL, fuelLeft, fuelTop, 1, 0, 0, fuelWidth, barHeight, fuelWidth, barHeight);
//        gui.blit(FUEL_GAUGE, fuelLeft, fuelTop, 2, 0, 0, fuelWidth, barHeight, fuelWidth, barHeight);

        gui.pose().popPose();
    }
}
