package net.woukie.createmissiles.block.launchpadcontroller;

import dev.architectury.networking.NetworkManager;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;

import java.util.function.Supplier;

public class SetControllerTargetMessage {
    public final BlockPos pos;
    public final int targetX, targetZ;

    public SetControllerTargetMessage(FriendlyByteBuf buf) {
        this(buf.readBlockPos(), buf.readInt(), buf.readInt());
    }

    public SetControllerTargetMessage(BlockPos pos, int targetX, int targetZ) {
        this.pos = pos;
        this.targetX = targetX;
        this.targetZ = targetZ;
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeBlockPos(pos);
        buf.writeInt(targetX);
        buf.writeInt(targetZ);
    }

    public void apply(Supplier<NetworkManager.PacketContext> contextSupplier) {
        Player player = contextSupplier.get().getPlayer();
        LaunchPadControllerBlockEntity controller = ControllerInstanceManager.get(player.level(), pos);

        if (controller == null)
            return;

        if (targetX >= 0 && targetZ >= 0 && targetX <= 128 && targetZ <= 128) {
            controller.updateTarget(targetX, targetZ);
        }
    }
}