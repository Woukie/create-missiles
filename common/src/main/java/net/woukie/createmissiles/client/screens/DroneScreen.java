package net.woukie.createmissiles.client.screens;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.woukie.createmissiles.CreateMissiles;
import net.woukie.createmissiles.entity.DroneEntity;
import net.woukie.createmissiles.inventory.DroneMenu;
import net.woukie.createmissiles.registry.Items;
import org.jetbrains.annotations.NotNull;

public class DroneScreen extends AbstractContainerScreen<DroneMenu> {

    private static final ResourceLocation BACKGROUND = new ResourceLocation(CreateMissiles.MOD_ID, "textures/gui/container/drone_panel.png");
    private EditBox tbXPos;
    private EditBox tbYPos;
    private ItemStack drone = new ItemStack(Items.DRONE_BOX_ITEM.get());

    public DroneScreen(DroneMenu abstractContainerMenu, Inventory inventory, Component component) {
        super(abstractContainerMenu, inventory, component);
    }

    @Override
    protected void init() {
        super.init();
        tbXPos = new EditBox(this.font, this.leftPos + 70, this.topPos + 8, 60, 20, Component.literal("X-Pos"));
        tbXPos.setMaxLength(50);
        tbXPos.setFocused(false);
        this.addRenderableWidget(tbXPos);

        tbYPos = new EditBox(this.font, this.leftPos + 70, this.topPos + 40, 60, 20, Component.literal("Y-Pos"));
        tbYPos.setMaxLength(50);
        tbYPos.setFocused(false);
        this.addRenderableWidget(tbYPos);
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int i, int j, float f) {
        super.render(guiGraphics, i, j, f);
        this.renderTooltip(guiGraphics, i, j);
        renderDroneBox(guiGraphics);
        tbXPos.render(guiGraphics, i, j, f);
        tbYPos.render(guiGraphics, i, j, f);
    }

    @Override
    public boolean mouseClicked(double d, double e, int i) {
        tbXPos.setEditable(tbXPos.isMouseOver(d, e));
        tbXPos.setFocused(tbXPos.isMouseOver(d, e));

        tbYPos.setEditable(tbYPos.isMouseOver(d, e));
        tbYPos.setFocused(tbYPos.isMouseOver(d, e));

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

        // Translate to desired screen position
        float x = this.leftPos + 34;
        float y = this.topPos + 34;
        poseStack.translate(x, y, 0); // Z-depth to avoid clipping

        // Scale by 2.5
        float scale = 2.5F;
        poseStack.scale(scale, scale, scale);

        // Offset to center the item (GUI items are 16x16)
        poseStack.translate(-8.0F, -8.0F, 0.0F);

        guiGraphics.renderItem(drone, 0, 0);

        poseStack.popPose();
    }
}
