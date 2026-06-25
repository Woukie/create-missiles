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

public record ClickFuelMessage(int sourceX, int sourceY, int sourceZ, float fuelPercent) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<ClickFuelMessage> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(CreateMissiles.MOD_ID, "navigation_panel_click_fuel"));

    public static final StreamCodec<ByteBuf, ClickFuelMessage> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT,
            ClickFuelMessage::sourceX,
            ByteBufCodecs.INT,
            ClickFuelMessage::sourceY,
            ByteBufCodecs.INT,
            ClickFuelMessage::sourceZ,
            ByteBufCodecs.FLOAT,
            ClickFuelMessage::fuelPercent,
            ClickFuelMessage::new
    );

    public void apply(final IPayloadContext context) {
        Player player = context.player();
        NavigationPanelBlockEntity blockEntity = NavigationPanelInstanceTracker.get(player.level(), new BlockPos(sourceX, sourceY, sourceZ));

        if (blockEntity == null)
            return;

        if (fuelPercent >= 0 && fuelPercent <= 1) {
            blockEntity.fuelClicked(fuelPercent);
        }
    }

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
