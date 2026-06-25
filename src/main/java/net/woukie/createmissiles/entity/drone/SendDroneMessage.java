package net.woukie.createmissiles.entity.drone;

import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.woukie.createmissiles.CreateMissiles;
import net.woukie.createmissiles.registry.EntityTypes;

import java.util.UUID;

public record SendDroneMessage(byte[] entityUUID, int destinationX, int destinationY, int destinationZ) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<SendDroneMessage> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(CreateMissiles.MOD_ID, "send_drone"));

    public static final StreamCodec<ByteBuf, SendDroneMessage> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.BYTE_ARRAY,
            SendDroneMessage::entityUUID,
            ByteBufCodecs.INT,
            SendDroneMessage::destinationX,
            ByteBufCodecs.INT,
            SendDroneMessage::destinationY,
            ByteBufCodecs.INT,
            SendDroneMessage::destinationZ,
            SendDroneMessage::new
    );

    public void apply(final IPayloadContext context) {
        Player player = context.player();
        Entity entity = ((ServerLevel)player.level()).getEntity(UUID.nameUUIDFromBytes(entityUUID));
        if (entity != null && (entity.getType() == EntityTypes.BASIC_DRONE.get() || entity.getType() == EntityTypes.REINFORCED_DRONE.get())) {
            ((Drone) entity).startMission(new BlockPos(destinationX, destinationY, destinationZ));
        }
    }

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
