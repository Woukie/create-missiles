package net.woukie.createmissiles.block.schematicator;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.woukie.createmissiles.CreateMissiles;

public class SchematicatorScreen extends AbstractContainerScreen<SchematicatorMenu>  {
    private static final ResourceLocation BACKGROUND = new ResourceLocation(CreateMissiles.MOD_ID, "textures/gui/container/schematicator.png");

    public SchematicatorScreen(SchematicatorMenu abstractContainerMenu, Inventory inventory, Component component) {
        super(abstractContainerMenu, inventory, component);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float f, int i, int j) {
        guiGraphics.blit(BACKGROUND, leftPos, topPos, 0, 0, this.imageWidth, this.imageHeight);
    }
}
