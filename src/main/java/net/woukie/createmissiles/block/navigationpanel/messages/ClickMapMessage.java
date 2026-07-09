package net.woukie.createmissiles.block.navigationpanel.messages;

import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.woukie.createmissiles.CreateMissiles;
import net.woukie.createmissiles.block.navigationpanel.NavigationPanelBlockEntity;
import net.woukie.createmissiles.block.navigationpanel.NavigationPanelInstanceTracker;

public record ClickMapMessage(int sourceX, int sourceY, int sourceZ, double mapCrosshairX, double mapCrosshairZ) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<ClickMapMessage> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(CreateMissiles.MOD_ID, "navigation_panel_click_map"));

    public static final StreamCodec<ByteBuf, ClickMapMessage> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT,
            ClickMapMessage::sourceX,
            ByteBufCodecs.INT,
            ClickMapMessage::sourceY,
            ByteBufCodecs.INT,
            ClickMapMessage::sourceZ,
            ByteBufCodecs.DOUBLE,
            ClickMapMessage::mapCrosshairX,
            ByteBufCodecs.DOUBLE,
            ClickMapMessage::mapCrosshairZ,
            ClickMapMessage::new
    );

    public void apply(final IPayloadContext context) {
        Player player = context.player();
        NavigationPanelBlockEntity blockEntity = NavigationPanelInstanceTracker.get(player.level(), new BlockPos(sourceX, sourceY, sourceZ));

        if (blockEntity == null)
            return;

        if (mapCrosshairX >= 0 && mapCrosshairZ >= 0 && mapCrosshairX <= 128 && mapCrosshairZ <= 128) {
            blockEntity.mapClicked(mapCrosshairX, mapCrosshairZ);
        }
    }

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
