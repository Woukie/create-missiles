package net.woukie.createmissiles.client;

import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.woukie.createmissiles.CreateMissiles;
import net.woukie.createmissiles.Util;

public record CreateFlashMessage(int colour, int originX, int originY, int originZ, int radius, double intensity, int length) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<CreateFlashMessage> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(CreateMissiles.MOD_ID, "create_flash"));

    public static final StreamCodec<ByteBuf, CreateFlashMessage> STREAM_CODEC = Util.composite(
            ByteBufCodecs.INT,
            CreateFlashMessage::colour,
            ByteBufCodecs.INT,
            CreateFlashMessage::originX,
            ByteBufCodecs.INT,
            CreateFlashMessage::originY,
            ByteBufCodecs.INT,
            CreateFlashMessage::originZ,
            ByteBufCodecs.INT,
            CreateFlashMessage::radius,
            ByteBufCodecs.DOUBLE,
            CreateFlashMessage::intensity,
            ByteBufCodecs.INT,
            CreateFlashMessage::length,
            CreateFlashMessage::new
    );


    public void apply(final IPayloadContext context) {
        FlashHandler.addFlash(new FlashHandler.Flash(
                colour,
                new BlockPos(originX, originY, originZ),
                radius,
                intensity,
                length
        ));
    }

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
