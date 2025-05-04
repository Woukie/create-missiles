package net.woukie.createmissiles.block.launchpadcontroller;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import net.woukie.createmissiles.CreateMissiles;

@Environment(EnvType.CLIENT)
public class LaunchPadControllerScreen extends AbstractContainerScreen<LaunchPadControllerMenu> {
    private static final int mapTop = 17;
    private static final int mapLeft = 60;
    private static final float mapScale = 0.4375F;
    private static final ResourceLocation BG_LOCATION = new ResourceLocation(CreateMissiles.MOD_ID, "textures/gui/container/launch_pad_controller.png");
    private ItemStack map;

    public LaunchPadControllerScreen(LaunchPadControllerMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
        this.titleLabelY -= 2;
    }

    public void render(GuiGraphics guiGraphics, int i, int j, float f) {
        super.render(guiGraphics, i, j, f);
        this.renderTooltip(guiGraphics, i, j);
    }

    protected void renderBg(GuiGraphics guiGraphics, float f, int i, int j) {
        this.renderBackground(guiGraphics);
        int left = this.leftPos;
        int top = this.topPos;
        guiGraphics.blit(BG_LOCATION, left, top, 0, 0, this.imageWidth, this.imageHeight);
        map = this.menu.getSlot(0).getItem();

        if (map.is(Items.FILLED_MAP)) {
            Integer mapId = MapItem.getMapId(map);
            MapItemSavedData mapData = MapItem.getSavedData(mapId, this.minecraft.level);

            if (mapData != null) {
                guiGraphics.pose().pushPose();
                guiGraphics.pose().translate(left + mapLeft, top + mapTop, 1.0F);
                guiGraphics.pose().scale(mapScale, mapScale, 1.0F);
                this.minecraft.gameRenderer.getMapRenderer().render(guiGraphics.pose(), guiGraphics.bufferSource(), mapId, mapData, true, 15728880);
                guiGraphics.flush();
                guiGraphics.pose().popPose();
            }
        }
    }

    @Override
    public boolean mouseClicked(double x, double z, int i) {
        Integer mapId = MapItem.getMapId(map);
        MapItemSavedData mapData = MapItem.getSavedData(mapId, this.minecraft.level);

        if (mapData != null) {
            int mapClickX = (int) ((x - this.leftPos - mapLeft) / mapScale);
            int mapClickZ = (int) ((z - this.topPos - mapTop) / mapScale);

            menu.clickMap(mapClickX, mapClickZ);
        }

        return super.mouseClicked(x, z, i);
    }
}
