package net.woukie.createmissiles.block.launchpadcontroller;

import dev.architectury.networking.NetworkManager;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;

import java.util.function.Supplier;

public class SetControllerTargetMessage {
    public final BlockPos pos;
    public final double mapCrosshairX, mapCrosshairZ;

    public SetControllerTargetMessage(FriendlyByteBuf buf) {
        this(buf.readBlockPos(), buf.readDouble(), buf.readDouble());
    }

    public SetControllerTargetMessage(BlockPos pos, double mapCrosshairX, double mapCrosshairZ) {
        this.pos = pos;
        this.mapCrosshairX = mapCrosshairX;
        this.mapCrosshairZ = mapCrosshairZ;
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeBlockPos(pos);
        buf.writeDouble(mapCrosshairX);
        buf.writeDouble(mapCrosshairZ);
    }

    public void apply(Supplier<NetworkManager.PacketContext> contextSupplier) {
        Player player = contextSupplier.get().getPlayer();
        LaunchPadControllerBlockEntity controller = ControllerInstanceManager.get(player.level(), pos);

        if (controller == null)
            return;

        if (mapCrosshairX >= 0 && mapCrosshairZ >= 0 && mapCrosshairX <= 128 && mapCrosshairZ <= 128) {
            controller.updateTarget(mapCrosshairX, mapCrosshairZ);
        }
    }
}