package net.woukie.createmissiles.block.navigationpanel.messages;

import dev.architectury.networking.NetworkManager;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.woukie.createmissiles.block.navigationpanel.NavigationPanelBlockEntity;
import net.woukie.createmissiles.block.navigationpanel.NavigationPanelInstanceTracker;

import java.util.function.Supplier;

public class ClickMapMessage {
    public final BlockPos source;
    public final double mapCrosshairX, mapCrosshairZ;

    public ClickMapMessage(FriendlyByteBuf buf) {
        this(buf.readBlockPos(), buf.readDouble(), buf.readDouble());
    }

    public ClickMapMessage(BlockPos source, double mapCrosshairX, double mapCrosshairZ) {
        this.source = source;
        this.mapCrosshairX = mapCrosshairX;
        this.mapCrosshairZ = mapCrosshairZ;
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeBlockPos(source);
        buf.writeDouble(mapCrosshairX);
        buf.writeDouble(mapCrosshairZ);
    }

    public void apply(Supplier<NetworkManager.PacketContext> contextSupplier) {
        Player player = contextSupplier.get().getPlayer();
        NavigationPanelBlockEntity blockEntity = NavigationPanelInstanceTracker.get(player.level(), source);

        if (blockEntity == null)
            return;

        if (mapCrosshairX >= 0 && mapCrosshairZ >= 0 && mapCrosshairX <= 128 && mapCrosshairZ <= 128) {
            blockEntity.mapClicked(mapCrosshairX, mapCrosshairZ);
        }
    }
}
