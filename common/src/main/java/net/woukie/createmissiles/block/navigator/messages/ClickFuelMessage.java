package net.woukie.createmissiles.block.navigator.messages;

import dev.architectury.networking.NetworkManager;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.woukie.createmissiles.block.navigator.NavigatorBlockEntity;
import net.woukie.createmissiles.block.navigator.NavigatorInstanceTracker;

import java.util.function.Supplier;

public class ClickFuelMessage {
    public final BlockPos source;
    public final double fuelPercent;

    public ClickFuelMessage(FriendlyByteBuf buf) {
        this(buf.readBlockPos(), buf.readDouble());
    }

    public ClickFuelMessage(BlockPos source, double fuelPercent) {
        this.source = source;
        this.fuelPercent = fuelPercent;
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeBlockPos(source);
        buf.writeDouble(fuelPercent);
    }

    public void apply(Supplier<NetworkManager.PacketContext> contextSupplier) {
        Player player = contextSupplier.get().getPlayer();
        NavigatorBlockEntity blockEntity = NavigatorInstanceTracker.get(player.level(), source);

        if (blockEntity == null)
            return;

        if (fuelPercent >= 0 && fuelPercent <= 1) {
            blockEntity.fuelClicked(fuelPercent);
        }
    }
}
