package net.woukie.createmissiles.block.controller;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.LoomScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.woukie.createmissiles.CreateMissiles;
import net.woukie.createmissiles.item.schematic.WarheadSchematic;
import net.woukie.createmissiles.missilemanager.parts.Ingredient;
import net.woukie.createmissiles.missilemanager.parts.WarheadType;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ControllerScreen extends AbstractContainerScreen<ControllerMenu> {
    private static final ResourceLocation BACKGROUND = new ResourceLocation(CreateMissiles.MOD_ID, "textures/gui/container/controller.png");
    private static final ResourceLocation COVER_LEFT = new ResourceLocation(CreateMissiles.MOD_ID, "textures/gui/sprites/container/cover_l.png");
    private static final ResourceLocation COVER_RIGHT = new ResourceLocation(CreateMissiles.MOD_ID, "textures/gui/sprites/container/cover_r.png");
    private static final ResourceLocation BUTTON = new ResourceLocation(CreateMissiles.MOD_ID, "textures/gui/sprites/container/button.png");
    private static final ResourceLocation BUTTON_HOVER = new ResourceLocation(CreateMissiles.MOD_ID, "textures/gui/sprites/container/button_hover.png");

    private static final int consoleLeft = 8;
    private static final int consoleTop = 18;
    private static final int consoleWidth = 105;
    private static final int consoleHeight = 52;

    private static final int buttonLeft = 116;
    private static final int buttonTop = 18;
    private static final int buttonWidth = 52;
    private static final int buttonHeight = 52;

    private static final int coverLeft = buttonLeft;
    private static final int coverTop = buttonTop;
    private static final int coverWidth = buttonWidth / 2;
    private static final int coverHeight = buttonHeight;

    private double currentOpenPercent = 0;
    private double currentScrollPosition = 0;

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
        renderButton(gui, buttonOpen, i, j);

        gui.pose().popPose();
    }

    private void renderButton(@NotNull GuiGraphics gui, boolean buttonOpen, double mouseX, double mouseY) {
        double speed = 0.01f;
        double targetOpenPercent = buttonOpen ? 1 : 0;
        currentOpenPercent += targetOpenPercent > currentOpenPercent ? speed : -speed;

        double adjusted = easeOutBounce(currentOpenPercent);
        int width = (int) (adjusted * coverWidth + 0.5);

        gui.pose().pushPose();
        gui.pose().translate(0, 0, 2);
        gui.blit(
                COVER_LEFT,
                coverLeft, coverTop,
                width, 0,
                coverWidth - width, coverHeight,
                coverWidth, coverHeight
        );

        gui.blit(
                COVER_RIGHT,
                coverLeft + coverWidth + width, coverTop,
                0, 0,
                coverWidth - width, coverHeight,
                coverWidth, coverHeight
        );
        gui.pose().popPose();

        gui.pose().pushPose();
        gui.pose().translate(0, 0, 1);

        ResourceLocation resource = buttonOpen && isHovering(buttonLeft, buttonTop, buttonWidth, buttonHeight, mouseX, mouseY) ? BUTTON_HOVER : BUTTON;

        gui.blit(
                resource,
                buttonLeft, buttonTop,
                0, 0,
                buttonWidth, buttonHeight,
                buttonWidth, buttonHeight
        );
        gui.pose().popPose();
    }

//    Sourced from easings.net, licenced under GPL-v3
    private double easeOutBounce(double x) {
        double n1 = 7.5625;
        double d1 = 2.75;

        if (x < 1 / d1) {
            return n1 * x * x;
        } else if (x < 2 / d1) {
            return n1 * (x -= 1.5 / d1) * x + 0.75;
        } else if (x < 2.5 / d1) {
            return n1 * (x -= 2.25 / d1) * x + 0.9375;
        } else {
            return n1 * (x -= 2.625 / d1) * x + 0.984375;
        }
    }

    private boolean renderLogs(GuiGraphics gui) {
        boolean launchPad = getMenu().launchPadExists();
        boolean launchPadPowered = getMenu().launchPadPowered();
        boolean schematicator = getMenu().schematicatorExists();
        boolean navigator = getMenu().navigatorExists();

        ItemStack warheadStack = getMenu().getWarhead();
        ItemStack chassisStack = getMenu().getChassis();
        ItemStack thrusterStack = getMenu().getThruster();

        boolean hasSchematics = warheadStack != null && chassisStack != null && thrusterStack != null;
        boolean hasDestination = getMenu().hasDestination();

        List<FormattedText> text = new ArrayList<>();

//        Statuses
        text.addAll(formatStatus("Launch pad: ", launchPad, launchPadPowered));
        text.addAll(formatStatus("Navigator: ", navigator, hasDestination));
        text.addAll(formatStatus("Schematicator: ", schematicator, hasSchematics));

//        Recipies
        HashMap<Ingredient, Integer> chassisIngredients = getMenu().getChassisIngredientsLeft();
        HashMap<Ingredient, Integer> thrusterIngredients = getMenu().getThrusterIngredientsLeft();
        HashMap<Ingredient, Integer> warheadIngredients = getMenu().getWarheadIngredientsLeft();

        int warheadPercent = getBuildPercentage(warheadIngredients);
        int chassisPercent = getBuildPercentage(chassisIngredients);
        int thrusterPercent = getBuildPercentage(thrusterIngredients);

        text.add(FormattedText.of("\nWarhead: ", Style.EMPTY.withColor(16777215)));
        text.add(FormattedText.of(warheadPercent + "%\n", Style.EMPTY.withColor(warheadPercent == 0 ? 16711680 : (warheadPercent == 100 ? 65280 : 16776960))));
        if (warheadIngredients != null) writeIngredientStatus(text, warheadIngredients);

        text.add(FormattedText.of("\nChassis: ", Style.EMPTY.withColor(16777215)));
        text.add(FormattedText.of(chassisPercent + "%\n", Style.EMPTY.withColor(chassisPercent == 0 ? 16711680 : (chassisPercent == 100 ? 65280 : 16776960))));
        if (chassisIngredients != null) writeIngredientStatus(text, chassisIngredients);

        text.add(FormattedText.of("\nThruster: ", Style.EMPTY.withColor(16777215)));
        text.add(FormattedText.of(thrusterPercent + "%\n", Style.EMPTY.withColor(thrusterPercent == 0 ? 16711680 : (thrusterPercent == 100 ? 65280 : 16776960))));
        if (thrusterIngredients != null) writeIngredientStatus(text, thrusterIngredients);


        gui.pose().pushPose();
        gui.pose().translate(consoleLeft, consoleTop, 0);
        gui.pose().translate(0, currentScrollPosition * 4, 0);
        gui.pose().scale(0.5F, 0.5F, 1);
//        Scissor ignores pose transforms
        gui.enableScissor(consoleLeft + leftPos, consoleTop + topPos, consoleLeft + leftPos + consoleWidth, consoleTop + topPos + consoleHeight);
        gui.drawWordWrap(this.font, FormattedText.composite(text), 2, 2, consoleWidth * 2 - 4, consoleHeight * 2 - 4);
        gui.disableScissor();
        gui.pose().popPose();


        return launchPad && schematicator && navigator && hasSchematics && hasDestination && warheadPercent == 100 && chassisPercent == 100 && thrusterPercent == 100;
    }

    private void writeIngredientStatus(List<FormattedText> text, HashMap<Ingredient, Integer> thrusterIngredients) {
        thrusterIngredients.forEach((ingredient, left) -> {
            int required = ingredient.getRequiredCount();
            int have = required - left;
            text.add(FormattedText.of("> " + ingredient.name.getString() + " ", Style.EMPTY.withColor(16777215)));
            text.add(FormattedText.of(have + "/" + required + "\n", Style.EMPTY.withColor(have == required ? 65280 : (have == 0 ? 16711680 : 16776960))));
        });
    }

    private int getBuildPercentage(HashMap<Ingredient, Integer> warheadIngredients) {
        if (warheadIngredients == null) return 0;

        int warheadIngredientsTotal = 0;
        int warheadIngredientsFulfilled = 0;
        for (var entry : warheadIngredients.entrySet()) {
            int required = entry.getKey().getRequiredCount();
            warheadIngredientsTotal += entry.getKey().getRequiredCount();
            warheadIngredientsFulfilled += required - entry.getValue();
        }

        return (int)(((float)warheadIngredientsFulfilled / (float)warheadIngredientsTotal) * 100);
    }

    private List<FormattedText> formatStatus(String text, boolean online, boolean valid) {
        List<FormattedText> result = new ArrayList<>();
        result.add(FormattedText.of(text, Style.EMPTY.withColor(16777215)));
        result.add(FormattedText.of((!online ? "OFFLINE" : (valid ? "VALID" : "INCOMPLETE")) + "\n", !online ? Style.EMPTY.withColor(16711680) : (valid ? Style.EMPTY.withColor(65280) : Style.EMPTY.withColor(16776960))));
        return result;
    }

    @Override
    public boolean mouseScrolled(double d, double e, double f) {
        this.currentScrollPosition += f;
        return true;
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
