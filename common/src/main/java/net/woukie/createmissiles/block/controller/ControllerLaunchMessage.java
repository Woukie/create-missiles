package net.woukie.createmissiles.block.controller;

import dev.architectury.networking.NetworkManager;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;

import java.util.function.Supplier;

public class ControllerLaunchMessage {
    public final BlockPos pos;

    public ControllerLaunchMessage(FriendlyByteBuf buf) {
        this(buf.readBlockPos());
    }

    public ControllerLaunchMessage(BlockPos pos) {
        this.pos = pos;
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeBlockPos(pos);
    }

    public void apply(Supplier<NetworkManager.PacketContext> contextSupplier) {
        Player player = contextSupplier.get().getPlayer();
        ControllerBlockEntity controller = ControllerInstanceManager.get(player.level(), pos);

        if (controller == null)
            return;

        controller.launch();
    }
}