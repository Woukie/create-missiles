package net.woukie.createmissiles.client.screens;

import net.minecraft.Util;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.woukie.createmissiles.CreateMissiles;
import net.woukie.createmissiles.inventory.ControlPanelMenu;
import net.woukie.createmissiles.recipe.MissileIngredient;
import net.woukie.createmissiles.recipe.MissilePartRecipe;
import net.woukie.createmissiles.registry.PartTypes;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class ControlPanelScreen extends AbstractContainerScreen<ControlPanelMenu> {
    private static final ResourceLocation BACKGROUND = ResourceLocation.fromNamespaceAndPath(CreateMissiles.MOD_ID, "textures/gui/container/control_panel.png");
    private static final ResourceLocation COVER_LEFT = ResourceLocation.fromNamespaceAndPath(CreateMissiles.MOD_ID, "textures/gui/sprites/container/cover_l.png");
    private static final ResourceLocation COVER_RIGHT = ResourceLocation.fromNamespaceAndPath(CreateMissiles.MOD_ID, "textures/gui/sprites/container/cover_r.png");
    private static final ResourceLocation BUTTON = ResourceLocation.fromNamespaceAndPath(CreateMissiles.MOD_ID, "textures/gui/sprites/container/button.png");
    private static final ResourceLocation BUTTON_HOVER = ResourceLocation.fromNamespaceAndPath(CreateMissiles.MOD_ID, "textures/gui/sprites/container/button_hover.png");

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

    private static final List<Character> textAnimatePallette = List.of('█','▓','▒','░',' ');
    private static final double textAnimateWaveLenth = 3;

    private double currentOpenPercent = 0;
    private double currentScrollPosition = 0;
    private final long openedTime = System.currentTimeMillis();

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

        double adjusted = buttonOpen ? easeOutBounce(currentOpenPercent) : easeInBounce(currentOpenPercent);
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

    private double easeInBounce(double x) {
        return 1 - easeOutBounce(1 - x);
    }

    private boolean renderLogs(GuiGraphics gui) {
        List<Text> text = new ArrayList<>();

        boolean launchPadValid = addLaunchPadStatus(text);
        boolean navigationPanelValid = addNavigationPanelStatus(text);
        boolean assemblyPanelValid = addAssemblyPanelStatus(text);

//        Recipe
        int lineCount = 9;

        ClientLevel level = minecraft != null ? minecraft.level : null;
        var warheadItemsLeft = MissilePartRecipe.getRemainingItems(PartTypes.get(getMenu().getWarhead()), level, getMenu().getItems());
        var chassisItemsLeft = MissilePartRecipe.getRemainingItems(PartTypes.get(getMenu().getChassis()), level, getMenu().getItems());
        var thrusterItemsLeft = MissilePartRecipe.getRemainingItems(PartTypes.get(getMenu().getThruster()), level, getMenu().getItems());

        int warheadPercent = MissilePartRecipe.getBuildPercentage(warheadItemsLeft);
        int chassisPercent = MissilePartRecipe.getBuildPercentage(chassisItemsLeft);
        int thrusterPercent = MissilePartRecipe.getBuildPercentage(thrusterItemsLeft);

        text.add(new Text("\n" + Component.translatable("gui.createmissiles.control_panel.warhead_title").getString() + ": ", Color.WHITE));
        text.add(new Text(warheadPercent + "%\n", warheadPercent == 0 ? Color.RED : (warheadPercent == 100 ? Color.GREEN : Color.YELLOW)));
        if (warheadItemsLeft != null) lineCount += writeIngredientStatus(text, warheadItemsLeft);

        text.add(new Text("\n" + Component.translatable("gui.createmissiles.control_panel.chassis_title").getString() + ": ", Color.WHITE));
        text.add(new Text(chassisPercent + "%\n", chassisPercent == 0 ? Color.RED : (chassisPercent == 100 ? Color.GREEN : Color.YELLOW)));
        if (chassisItemsLeft != null) lineCount += writeIngredientStatus(text, chassisItemsLeft);

        text.add(new Text("\n" + Component.translatable("gui.createmissiles.control_panel.thruster_title").getString() + ": ", Color.WHITE));
        text.add(new Text(thrusterPercent + "%\n", thrusterPercent == 0 ? Color.RED : (thrusterPercent == 100 ? Color.GREEN : Color.YELLOW)));
        if (thrusterItemsLeft != null) lineCount += writeIngredientStatus(text, thrusterItemsLeft);

//        Render
        gui.pose().pushPose();
        gui.pose().translate(consoleLeft, consoleTop, 0);

        double lineHeight = 9.0 / 2.0;
        this.currentScrollPosition = Math.max(-lineHeight * lineCount -1 +consoleHeight, this.currentScrollPosition);
        this.currentScrollPosition = Math.min(0, this.currentScrollPosition);

        gui.pose().translate(0, currentScrollPosition, 0);
        gui.pose().scale(0.5F, 0.5F, 1);
//        Scissor ignores pose transforms
        gui.enableScissor(consoleLeft + leftPos, consoleTop + topPos, consoleLeft + leftPos + consoleWidth, consoleTop + topPos + consoleHeight);

        animateText(text);

        FormattedText formattedText = FormattedText.composite(text.stream().map(a -> FormattedText.of(a.text, a.getStyle())).toList());
        gui.drawWordWrap(this.font, formattedText, 2, 2, consoleWidth * 2 - 4, consoleHeight * 2 - 4);
        gui.disableScissor();
        gui.pose().popPose();

        return launchPadValid && navigationPanelValid && assemblyPanelValid && chassisPercent == 100 && warheadPercent == 100 && thrusterPercent == 100;
    }

    private void animateText(List<Text> text) {
        long timePassed = System.currentTimeMillis() - openedTime;
        int hideAfter = (int)(timePassed / 10L);

        int currentCharacter = 0;
        for (Text line : text) {
            StringBuilder obuscated = new StringBuilder();
            for (Character character : line.text.toCharArray()) {
                if (currentCharacter <= hideAfter) {
                    if (!character.equals('\n')) {
                        int distance = hideAfter - currentCharacter;
                        distance += (int)(Math.random() * 3 - 1.5);
                        double proportionThroughList = distance / (textAnimatePallette.size() * textAnimateWaveLenth);
                        proportionThroughList = Math.max(0, proportionThroughList);
                        if (proportionThroughList <= 1) {
                            int palletteIndex = (int)(proportionThroughList * (textAnimatePallette.size() - 1));
                            character = textAnimatePallette.get(palletteIndex);
                        }
                    }

                    obuscated.append(character);
                }
                currentCharacter++;
            }
            line.text = obuscated.toString();
        }
    }

    private boolean addLaunchPadStatus(List<Text> text) {
        String title = Component.translatable("gui.createmissiles.control_panel.launch_pad_title").getString();
        text.add(new Text(title + ": ", Color.WHITE));

        if (!getMenu().launchPadExists()) {
            String status = Component.translatable("gui.createmissiles.control_panel.no_launch_pad").getString() + "\n";
            text.add(new Text(status, Color.RED));
            return false;
        }

        if (!getMenu().launchPadPowered()) {
            String status = Component.translatable("gui.createmissiles.control_panel.launch_pad_no_power").getString() + "\n";
            text.add(new Text(status, Color.YELLOW));
            return false;
        }

        String status = Component.translatable("gui.createmissiles.control_panel.valid").getString() + "\n";
        text.add(new Text(status, Color.GREEN));

        return true;
    }

    private boolean addNavigationPanelStatus(List<Text> text) {
        String title = Component.translatable("gui.createmissiles.control_panel.navigation_panel_title").getString();
        text.add(new Text(title + ": ", Color.WHITE));

        if (!getMenu().navigationPanelExists()) {
            String status = Component.translatable("gui.createmissiles.control_panel.no_navigation_panel").getString() + "\n";
            text.add(new Text(status, Color.YELLOW));
            return false;
        }

        if (!getMenu().hasDestination()) {
            String status = Component.translatable("gui.createmissiles.control_panel.no_destination").getString() + "\n";
            text.add(new Text(status, Color.YELLOW));
            return false;
        }

        String status = Component.translatable("gui.createmissiles.control_panel.valid").getString() + "\n";
        text.add(new Text(status, Color.GREEN));

        return true;
    }

    private boolean addAssemblyPanelStatus(List<Text> text) {
        String title = Component.translatable("gui.createmissiles.control_panel.assembly_panel_title").getString();
        text.add(new Text(title + ": ", Color.WHITE));

        if (!getMenu().assemblyPanelExists()) {
            String status = Component.translatable("gui.createmissiles.control_panel.no_assembly_panel").getString() + "\n";
            text.add(new Text(status, Color.RED));
            return false;
        }

        ItemStack warheadStack = getMenu().getWarhead();
        ItemStack chassisStack = getMenu().getChassis();
        ItemStack thrusterStack = getMenu().getThruster();

        if (warheadStack == null) {
            String status = Component.translatable("gui.createmissiles.control_panel.no_warhead").getString() + "\n";
            text.add(new Text(status, Color.YELLOW));
            return false;
        }

        if (chassisStack == null) {
            String status = Component.translatable("gui.createmissiles.control_panel.no_chassis").getString() + "\n";
            text.add(new Text(status, Color.YELLOW));
            return false;
        }

        if (thrusterStack == null) {
            String status = Component.translatable("gui.createmissiles.control_panel.no_thruster").getString() + "\n";
            text.add(new Text(status, Color.YELLOW));
            return false;
        }

        String status = Component.translatable("gui.createmissiles.control_panel.valid").getString() + "\n";
        text.add(new Text(status, Color.GREEN));

        return true;
    }

    private int writeIngredientStatus(List<Text> text, Map<MissileIngredient, Integer> ingredients) {
        AtomicInteger increment = new AtomicInteger();
        ingredients.forEach((ingredient, left) -> {
            int required = ingredient.count();
            int have = required - left;

            List<ItemStack> items = ingredient.getAllValidItems();
            Component[] names = items.stream().map(ItemStack::getDisplayName).toList().toArray(new Component[0]);
            String name = names[(int)(Util.getMillis() / 1000f) % names.length].getString();

            text.add(new Text("> " + name.substring(1, name.length() - 1) + " ", Color.WHITE));
            text.add(new Text(have + "/" + required + "\n", have == required ? Color.GREEN : (have == 0 ? Color.RED : Color.YELLOW)));
            increment.addAndGet(name.length() / 32 + 1); // Good enough fix for long names
        });

        return increment.get();
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
        this.currentScrollPosition += scrollY * 4f;
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

    private static class Text {
        public String text;
        public Color color;

        Text(String text, Color color) {
            this.text = text;
            this.color = color;
        }

        public Style getStyle() {
            return switch (color) {
                case WHITE -> Style.EMPTY.withColor(16777215);
                case GREEN -> Style.EMPTY.withColor(65280);
                case YELLOW -> Style.EMPTY.withColor(16776960);
                case RED -> Style.EMPTY.withColor(16711680);
            };
        }
    }

    private enum Color {
        GREEN, YELLOW, RED, WHITE
    }
}
