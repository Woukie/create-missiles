package net.woukie.createmissiles.block.controlpanel;

import net.minecraft.Util;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.woukie.createmissiles.CreateMissiles;
import net.woukie.createmissiles.recipe.MissileIngredient;
import net.woukie.createmissiles.recipe.MissilePartRecipe;
import net.woukie.createmissiles.registry.PartTypes;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class ControlPanelScreen extends AbstractContainerScreen<ControlPanelMenu> {
    private static final ResourceLocation BACKGROUND = new ResourceLocation(CreateMissiles.MOD_ID, "textures/gui/container/control_panel.png");
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

    private double lineHeight = 9.0 / 2.0;
    private double currentOpenPercent = 0;
    private double currentScrollPosition = 0;

    public ControlPanelScreen(ControlPanelMenu abstractContainerMenu, Inventory inventory, Component component) {
        super(abstractContainerMenu, inventory, component);
    }

    @Override
    public void render(@NotNull GuiGraphics gui, int i, int j, float f) {
        super.render(gui, i, j, f);
        this.renderTooltip(gui, i, j);
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
        boolean assemblyPanelExists = getMenu().assemblyPanelExists();
        boolean navigationPanel = getMenu().navigationPanelExists();

        ItemStack warheadStack = getMenu().getWarhead();
        ItemStack chassisStack = getMenu().getChassis();
        ItemStack thrusterStack = getMenu().getThruster();

        boolean hasAssemblies = warheadStack != null && chassisStack != null && thrusterStack != null;
        boolean hasDestination = getMenu().hasDestination();

        List<FormattedText> text = new ArrayList<>();

//        Statuses
        text.addAll(formatStatus(Component.translatable("gui.createmissiles.navigation_panel.launch_pad_title").getString() + ": ", launchPad, launchPadPowered));
        text.addAll(formatStatus(Component.translatable("gui.createmissiles.navigation_panel.navigation_panel_title").getString() + ": ", navigationPanel, hasDestination));
        text.addAll(formatStatus(Component.translatable("gui.createmissiles.navigation_panel.assembly_panel_title").getString() + ": ", assemblyPanelExists, hasAssemblies));

//        Recipe
        int lineCount = 9;

        var warheadItemsLeft = MissilePartRecipe.getRemainingItems(PartTypes.get(warheadStack), minecraft.level, getMenu().getItems());
        var chassisItemsLeft = MissilePartRecipe.getRemainingItems(PartTypes.get(chassisStack), minecraft.level, getMenu().getItems());
        var thrusterItemsLeft = MissilePartRecipe.getRemainingItems(PartTypes.get(thrusterStack), minecraft.level, getMenu().getItems());

        int warheadPercent = MissilePartRecipe.getBuildPercentage(warheadItemsLeft);
        int chassisPercent = MissilePartRecipe.getBuildPercentage(chassisItemsLeft);
        int thrusterPercent = MissilePartRecipe.getBuildPercentage(thrusterItemsLeft);

        text.add(FormattedText.of("\n" + Component.translatable("gui.createmissiles.navigation_panel.warhead_title").getString() + ": ", Style.EMPTY.withColor(16777215)));
        text.add(FormattedText.of(warheadPercent + "%\n", Style.EMPTY.withColor(warheadPercent == 0 ? 16711680 : (warheadPercent == 100 ? 65280 : 16776960))));
        if (warheadItemsLeft != null) lineCount += writeIngredientStatus(text, warheadItemsLeft);

        text.add(FormattedText.of("\n" + Component.translatable("gui.createmissiles.navigation_panel.chassis_title").getString() + ": ", Style.EMPTY.withColor(16777215)));
        text.add(FormattedText.of(chassisPercent + "%\n", Style.EMPTY.withColor(chassisPercent == 0 ? 16711680 : (chassisPercent == 100 ? 65280 : 16776960))));
        if (chassisItemsLeft != null) lineCount += writeIngredientStatus(text, chassisItemsLeft);

        text.add(FormattedText.of("\n" + Component.translatable("gui.createmissiles.navigation_panel.thruster_title").getString() + ": ", Style.EMPTY.withColor(16777215)));
        text.add(FormattedText.of(thrusterPercent + "%\n", Style.EMPTY.withColor(thrusterPercent == 0 ? 16711680 : (thrusterPercent == 100 ? 65280 : 16776960))));
        if (thrusterItemsLeft != null) lineCount += writeIngredientStatus(text, thrusterItemsLeft);

//        Render
        gui.pose().pushPose();
        gui.pose().translate(consoleLeft, consoleTop, 0);

        this.currentScrollPosition = Math.max(-lineHeight * lineCount -1 +consoleHeight, this.currentScrollPosition);
        this.currentScrollPosition = Math.min(0, this.currentScrollPosition);

        gui.pose().translate(0, currentScrollPosition, 0);
        gui.pose().scale(0.5F, 0.5F, 1);
//        Scissor ignores pose transforms
        gui.enableScissor(consoleLeft + leftPos, consoleTop + topPos, consoleLeft + leftPos + consoleWidth, consoleTop + topPos + consoleHeight);
        gui.drawWordWrap(this.font, FormattedText.composite(text), 2, 2, consoleWidth * 2 - 4, consoleHeight * 2 - 4);
        gui.disableScissor();
        gui.pose().popPose();

        return launchPad && assemblyPanelExists && navigationPanel && hasAssemblies && hasDestination && chassisPercent == 100 && warheadPercent == 100 && thrusterPercent == 100;
    }

    private int writeIngredientStatus(List<FormattedText> text, Map<MissileIngredient, Integer> ingredients) {
        AtomicInteger increment = new AtomicInteger();
        ingredients.forEach((ingredient, left) -> {
            int required = ingredient.getCount();
            int have = required - left;
            List<ItemStack> items = new ArrayList<>(Arrays.stream(ingredient.getItems()).toList());

            for (var tag : ingredient.getTags()) {
                for(Holder<Item> holder : BuiltInRegistries.ITEM.getTagOrEmpty(tag)) {
                    items.add(new ItemStack(holder));
                }
            }

            Component[] names = items.stream().map(ItemStack::getDisplayName).toList().toArray(new Component[0]);
            String name = names[(int)(Util.getMillis() / 1000f) % names.length].getString();

            text.add(FormattedText.of("> " + name.substring(1, name.length() - 1) + " ", Style.EMPTY.withColor(16777215)));
            text.add(FormattedText.of(have + "/" + required + "\n", Style.EMPTY.withColor(have == required ? 65280 : (have == 0 ? 16711680 : 16776960))));
            increment.addAndGet(name.length() / 32 + 1); // Good enough fix for long names
        });

        return increment.get();
    }

    private List<FormattedText> formatStatus(String text, boolean online, boolean valid) {
        List<FormattedText> result = new ArrayList<>();
        result.add(FormattedText.of(text, Style.EMPTY.withColor(16777215)));
        MutableComponent status = !online ? Component.translatable("gui.createmissiles.navigation_panel.offline") : (valid ? Component.translatable("gui.createmissiles.navigation_panel.valid") : Component.translatable("gui.createmissiles.navigation_panel.incomplete"));
        result.add(FormattedText.of(status.getString() + "\n", !online ? Style.EMPTY.withColor(16711680) : (valid ? Style.EMPTY.withColor(65280) : Style.EMPTY.withColor(16776960))));
        return result;
    }

    @Override
    public boolean mouseScrolled(double d, double e, double f) {
        this.currentScrollPosition += f * 4f;
        return true;
    }

    @Override
    protected void renderBg(GuiGraphics gui, float f, int i, int j) {
        gui.pose().pushPose();
        gui.pose().translate(leftPos, topPos, 0);

        gui.blit(BACKGROUND, 0, 0, 0, 0, this.imageWidth, this.imageHeight);

        boolean buttonOpen = renderLogs(gui);
        renderButton(gui, buttonOpen, i, j);

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
