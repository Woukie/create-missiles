package net.woukie.createmissiles.block.controller;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.woukie.createmissiles.CreateMissiles;
import net.woukie.createmissiles.item.schematic.ChassisSchematic;
import net.woukie.createmissiles.item.schematic.ThrusterSchematic;
import net.woukie.createmissiles.item.schematic.WarheadSchematic;
import net.woukie.createmissiles.missilemanager.Trajectory;
import net.woukie.createmissiles.missilemanager.TrajectoryData;
import net.woukie.createmissiles.missilemanager.parts.Thruster;
import org.jetbrains.annotations.NotNull;

public class ControllerScreen extends AbstractContainerScreen<ControllerMenu> {
    private static final ResourceLocation BACKGROUND = new ResourceLocation(CreateMissiles.MOD_ID, "textures/gui/container/controller.png");
    private static final ResourceLocation COVER_LEFT = new ResourceLocation(CreateMissiles.MOD_ID, "textures/gui/sprites/container/cover_l.png");
    private static final ResourceLocation COVER_RIGHT = new ResourceLocation(CreateMissiles.MOD_ID, "textures/gui/sprites/container/cover_r.png");

    private static final int consoleLeft = 8;
    private static final int consoleTop = 18;
    private static final int consoleWidth = 105;
    private static final int consoleHeight = 52;

    private static final int buttonLeft = 116;
    private static final int buttonTop = 18;
    private static final int buttonWidth = 52;
    private static final int buttonHeight = 52;

    private static final int coverWidth = buttonWidth / 2;
    private static final int coverHeight = buttonHeight;

    private double currentOpenPercent = 0;

    public ControllerScreen(ControllerMenu abstractContainerMenu, Inventory inventory, Component component) {
        super(abstractContainerMenu, inventory, component);
    }

    @Override
    public void render(@NotNull GuiGraphics gui, int i, int j, float f) {
        super.render(gui, i, j, f);
        this.renderTooltip(gui, i, j);

        gui.pose().pushPose();
        gui.pose().translate(leftPos, topPos, 0);

        boolean buttonOpen = renderLogs(gui);

        currentOpenPercent += (buttonOpen ? 1 : 0 - currentOpenPercent) * 0.05F;
        gui.pose().popPose();
    }

    private boolean renderLogs(GuiGraphics gui) {
        boolean launchPad = getMenu().launchPadExists();
        boolean schematicator = getMenu().schematicatorExists();
        boolean navigator = getMenu().navigatorExists();

        ItemStack warhead = getMenu().getWarhead();
        ItemStack chassis = getMenu().getChassis();
        ItemStack thruster = getMenu().getThruster();

        boolean hasSchematics = warhead != null && chassis != null && thruster != null;
        boolean hasDestination = getMenu().hasDestination();

        String logs = "";
        logs += "Launch pad: " + (launchPad ? " VALID" : "OFFLINE") + "\n";
        logs += "Schematicator: " + (!schematicator ? "OFFLINE" : (hasSchematics ? "VALID" : "INCOMPLETE")) + "\n";
        logs += "Navigator: " + (!navigator ? "OFFLINE" : (hasDestination ? "VALID" : "INCOMPLETE")) + "\n";

        boolean open = launchPad && schematicator && navigator && hasSchematics && hasDestination;

        logs += "\nStatus: " + (open ? "ARMED" : "INCOMPLETE")+ "\n";


        gui.pose().pushPose();
        gui.pose().translate(consoleLeft, consoleTop, 0);
        gui.pose().scale(0.5F, 0.5F, 1);
        gui.drawWordWrap(this.font, FormattedText.of(logs, Style.EMPTY.withColor(16777215)), 2, 2, consoleWidth * 2 - 4, consoleHeight * 2 - 4);
        gui.pose().popPose();

        return launchPad && schematicator && navigator && hasSchematics && hasDestination;
    }

    @Override
    protected void renderBg(GuiGraphics gui, float f, int i, int j) {
        gui.pose().pushPose();
        gui.pose().translate(leftPos, topPos, 0);

        gui.blit(BACKGROUND, 0, 0, 0, 0, this.imageWidth, this.imageHeight);

        gui.pose().popPose();
    }

    @Override
    public boolean mouseClicked(double x, double y, int i) {
        if (isHovering(buttonLeft, buttonTop, buttonWidth, buttonHeight, x, y)) {
            getMenu().clickLaunch();
        }

        return super.mouseClicked(x, y, i);
    }
}
