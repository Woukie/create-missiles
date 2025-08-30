package net.woukie.createmissiles.client.screens;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.woukie.createmissiles.CreateMissiles;
import net.woukie.createmissiles.inventory.DroneMenu;
import org.jetbrains.annotations.NotNull;

public class DroneScreen extends AbstractContainerScreen<DroneMenu> {

    private static final ResourceLocation BACKGROUND = new ResourceLocation(CreateMissiles.MOD_ID, "textures/gui/container/drone_panel.png");

    public DroneScreen(DroneMenu abstractContainerMenu, Inventory inventory, Component component) {
        super(abstractContainerMenu, inventory, component);
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int i, int j, float f) {
        super.render(guiGraphics, i, j, f);
        this.renderTooltip(guiGraphics, i, j);
    }

    @Override
    protected void renderBg(@NotNull GuiGraphics guiGraphics, float f, int i, int j) {
        guiGraphics.blit(BACKGROUND, leftPos, topPos, 0, 0, this.imageWidth, this.imageHeight);
    }
}
