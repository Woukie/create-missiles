package net.woukie.createmissiles.entity.messages;

import dev.architectury.networking.NetworkManager;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.woukie.createmissiles.entity.Drone;
import net.woukie.createmissiles.registry.EntityTypes;

import java.util.UUID;
import java.util.function.Supplier;

public class SendDroneMessage {
    public final UUID entityUUID;
    public final BlockPos desination;

    public SendDroneMessage(FriendlyByteBuf buf) {
        this(buf.readUUID(), buf.readBlockPos());
    }

    public SendDroneMessage(UUID entityUUID, BlockPos desination) {
        this.entityUUID = entityUUID;
        this.desination = desination;
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeUUID(entityUUID);
        buf.writeBlockPos(desination);
    }

    public void apply(Supplier<NetworkManager.PacketContext> contextSupplier) {
        Player player = contextSupplier.get().getPlayer();
        Entity entity = ((ServerLevel)player.level()).getEntity(entityUUID);
        if (entity != null && (entity.getType() == (EntityTypes.BASIC_DRONE.get()) || entity.getType() == (EntityTypes.REINFORCED_DRONE.get()))) {
            ((Drone) entity).startMission(desination);
        }
    }
}
