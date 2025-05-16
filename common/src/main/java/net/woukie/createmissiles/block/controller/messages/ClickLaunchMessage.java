package net.woukie.createmissiles.block.controller.messages;

import dev.architectury.networking.NetworkManager;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.woukie.createmissiles.block.controller.ControllerBlockEntity;
import net.woukie.createmissiles.block.controller.ControllerInstanceTracker;

import java.util.function.Supplier;

public class ClickLaunchMessage {
    public final BlockPos pos;

    public ClickLaunchMessage(FriendlyByteBuf buf) {
        this(buf.readBlockPos());
    }

    public ClickLaunchMessage(BlockPos pos) {
        this.pos = pos;
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeBlockPos(pos);
    }

    public void apply(Supplier<NetworkManager.PacketContext> contextSupplier) {
        Player player = contextSupplier.get().getPlayer();
        ControllerBlockEntity controller = ControllerInstanceTracker.get(player.level(), pos);

        if (controller == null)
            return;

        controller.clickLaunch();
    }
}
