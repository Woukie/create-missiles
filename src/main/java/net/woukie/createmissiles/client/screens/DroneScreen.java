package net.woukie.createmissiles.client.screens;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.woukie.createmissiles.CreateMissiles;
import net.woukie.createmissiles.inventory.DroneMenu;
import net.woukie.createmissiles.registry.Items;
import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;

public class DroneScreen extends AbstractContainerScreen<DroneMenu> {
    private static final ResourceLocation BACKGROUND = ResourceLocation.fromNamespaceAndPath(CreateMissiles.MOD_ID, "textures/gui/container/drone_panel.png");
    private static final ResourceLocation LAUNCH_BUTTON = ResourceLocation.fromNamespaceAndPath(CreateMissiles.MOD_ID, "textures/gui/sprites/container/launch_drone_button.png");
    private static final ResourceLocation LAUNCH_BUTTON_HOVER = ResourceLocation.fromNamespaceAndPath(CreateMissiles.MOD_ID, "textures/gui/sprites/container/launch_drone_button_hover.png");

    private EditBox tbXPos;
    private EditBox tbYPos;
    private boolean sendClickable, initialised;

    private int sendButtonLeft = 151;
    private int sendButtonTop = 53;
    private int sendButtonWidth = 18;
    private int sendButtonHeight = 18;

    private final ItemStack droneStack = new ItemStack(Items.DRONE_BOX_ITEM.get());
    private final ItemStack reinforcedDroneStack = new ItemStack(Items.REINFORCED_DRONE_BOX.get());

    public DroneScreen(DroneMenu abstractContainerMenu, Inventory inventory, Component component) {
        super(abstractContainerMenu, inventory, component);
    }

    @Override
    protected void init() {
        super.init();
        tbXPos = new EditBox(this.font, this.leftPos + 80, this.topPos + 16, 88, 14, Component.literal("X-Pos"));
        tbXPos.setFilter(integerFilter());
        tbXPos.setMaxLength(50);
        tbXPos.setFocused(false);
        this.addRenderableWidget(tbXPos);

        tbYPos = new EditBox(this.font, this.leftPos + 80, this.topPos + 35, 88, 14, Component.literal("Y-Pos"));
        tbYPos.setFilter(integerFilter());
        tbYPos.setMaxLength(50);
        tbYPos.setFocused(false);
        this.addRenderableWidget(tbYPos);
    }

    private static @NotNull Predicate<String> integerFilter() {
        return s -> {
            char[] chars = s.toCharArray();
            for (int i = 0; i < chars.length; i++) {
                if (i == 0 && chars[i] == '-') {
                    continue;
                }

                try {
                    Integer.parseInt("" + chars[i]);
                } catch (NumberFormatException e) {
                    return false;
                }
            }

            return true;
        };
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int i, int j, float f) {
        super.render(guiGraphics, i, j, f);
        this.renderTooltip(guiGraphics, i, j);
        renderDroneBox(guiGraphics);
        tbXPos.render(guiGraphics, i, j, f);
        tbYPos.render(guiGraphics, i, j, f);

        if (!initialised) {
            tbXPos.insertText("" + getMenu().getInitialX());
            tbYPos.insertText("" + getMenu().getInitialZ());
            initialised = true;
        }

        try {
            Integer.parseInt(tbXPos.getValue());
            Integer.parseInt(tbYPos.getValue());
            sendClickable = true;
        } catch (NumberFormatException e) {
            sendClickable = false;
        }

        if (sendClickable) {
            if (!getMenu().hasEmptyMap()) {
                sendClickable = false;
            }
        }

        renderButton(guiGraphics, i, j, f);

        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(this.leftPos, this.topPos, 0);
        guiGraphics.drawWordWrap(this.font, FormattedText.of("X", Style.EMPTY.withColor(16777215)), 68, 20, 12, 16);
        guiGraphics.drawWordWrap(this.font, FormattedText.of("Z", Style.EMPTY.withColor(16777215)), 68, 39, 12, 16);
        guiGraphics.pose().popPose();
    }

    @Override
    public boolean mouseClicked(double d, double e, int i) {
        tbXPos.setEditable(tbXPos.isMouseOver(d, e));
        tbXPos.setFocused(tbXPos.isMouseOver(d, e));

        tbYPos.setEditable(tbYPos.isMouseOver(d, e));
        tbYPos.setFocused(tbYPos.isMouseOver(d, e));

        if (sendClickable && isHovering(sendButtonLeft, sendButtonTop, sendButtonWidth, sendButtonHeight, d, e)) {
            getMenu().clickLaunch(new BlockPos(Integer.parseInt(tbXPos.getValue()), 0, Integer.parseInt(tbYPos.getValue())));
        }

        return super.mouseClicked(d, e, i);
    }

    @Override
    protected void renderBg(@NotNull GuiGraphics guiGraphics, float f, int i, int j) {
        guiGraphics.blit(BACKGROUND, leftPos, topPos, 0, 0, this.imageWidth, this.imageHeight);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (tbXPos.isFocused()) {
            if (tbXPos.keyPressed(keyCode, scanCode, modifiers) || tbXPos.canConsumeInput()) {
                return true;
            }
        }
        if (tbYPos.isFocused()) {
            if (tbYPos.keyPressed(keyCode, scanCode, modifiers) || tbYPos.canConsumeInput()) {
                return true;
            }
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean charTyped(char codePoint, int modifiers) {
        if (tbXPos.isFocused()) {
            return tbXPos.charTyped(codePoint, modifiers);
        }
        if (tbYPos.isFocused()) {
            return tbYPos.charTyped(codePoint, modifiers);
        }
        return false;
    }

    private void renderDroneBox(GuiGraphics guiGraphics) {
        PoseStack poseStack = guiGraphics.pose();

        poseStack.pushPose();

        float x = this.leftPos + 8 + 27;
        float y = this.topPos + 16 + 27;
        poseStack.translate(x, y, 0); // Center of target box

        float scale = 3F;
        poseStack.scale(scale, scale, scale);
        poseStack.translate(-8.0F, -8.0F, 0.0F);
        guiGraphics.renderItem(getMenu().isBasic() ? droneStack : reinforcedDroneStack, 0, 0);
        poseStack.popPose();
    }

    private void renderButton(GuiGraphics guiGraphics, int i, int j, float f) {
        if (sendClickable) {
            PoseStack poseStack = guiGraphics.pose();
            poseStack.pushPose();
            poseStack.translate(this.leftPos, this.topPos, 0);
            ResourceLocation resource = isHovering(sendButtonLeft, sendButtonTop, sendButtonWidth, sendButtonHeight, i, j) ? LAUNCH_BUTTON_HOVER : LAUNCH_BUTTON;
            guiGraphics.blit(
                    resource,
                    sendButtonLeft, sendButtonTop,
                    0, 0,
                    sendButtonWidth, sendButtonHeight,
                    sendButtonWidth, sendButtonHeight
            );
            poseStack.popPose();
        }
    }
}
