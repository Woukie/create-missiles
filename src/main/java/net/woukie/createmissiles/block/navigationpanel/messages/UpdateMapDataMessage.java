package net.woukie.createmissiles.block.navigationpanel.messages;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.saveddata.maps.MapId;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.woukie.createmissiles.CreateMissiles;

public record UpdateMapDataMessage(int mapId, CompoundTag data) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<UpdateMapDataMessage> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(CreateMissiles.MOD_ID, "update_map_data"));

    public static final StreamCodec<ByteBuf, UpdateMapDataMessage> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT,
            UpdateMapDataMessage::mapId,
            ByteBufCodecs.COMPOUND_TAG,
            UpdateMapDataMessage::data,
            UpdateMapDataMessage::new
    );

    public void apply(final IPayloadContext context) {
        ClientLevel level = Minecraft.getInstance().level;
        if (level == null) return;
        level.overrideMapData(new MapId(mapId), MapItemSavedData.load(data, level.registryAccess()));
    }

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
