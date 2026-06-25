package net.woukie.createmissiles.missiles.parts.warheads.messages;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.woukie.createmissiles.CreateMissiles;
import org.joml.Vector3f;

public record ExplodeFireworkMessage(Vector3f pos, Vector3f vel, CompoundTag tag) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<ExplodeFireworkMessage> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(CreateMissiles.MOD_ID, "explode_firework"));

    public static final StreamCodec<ByteBuf, ExplodeFireworkMessage> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VECTOR3F,
            ExplodeFireworkMessage::pos,
            ByteBufCodecs.VECTOR3F,
            ExplodeFireworkMessage::vel,
            ByteBufCodecs.COMPOUND_TAG,
            ExplodeFireworkMessage::tag,
            ExplodeFireworkMessage::new
    );

    public void apply(final IPayloadContext context) {
        var player = context.player();
        player.level().createFireworks(pos.x, pos.y, pos.z, vel.x, vel.y, vel.z, tag);
    }

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
