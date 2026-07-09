package net.woukie.createmissiles.block.controlpanel.messages;

import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.woukie.createmissiles.CreateMissiles;
import net.woukie.createmissiles.block.controlpanel.ControlPanelBlockEntity;
import net.woukie.createmissiles.block.controlpanel.ControlPanelInstanceTracker;

public record ClickLaunchMessage(int sourceX, int sourceY, int sourceZ) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<ClickLaunchMessage> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(CreateMissiles.MOD_ID, "control_panel_click_launch"));

    public static final StreamCodec<ByteBuf, ClickLaunchMessage> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT,
            ClickLaunchMessage::sourceX,
            ByteBufCodecs.INT,
            ClickLaunchMessage::sourceY,
            ByteBufCodecs.INT,
            ClickLaunchMessage::sourceZ,
            ClickLaunchMessage::new
    );

    public void apply(final IPayloadContext context) {
        Player player = context.player();
        ControlPanelBlockEntity controlPanel = ControlPanelInstanceTracker.get(player.level(), new BlockPos(sourceX, sourceY, sourceZ));

        if (controlPanel == null)
            return;

        controlPanel.launch();
    }

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
