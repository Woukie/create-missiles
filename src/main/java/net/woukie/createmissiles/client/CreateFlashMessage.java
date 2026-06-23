package net.woukie.createmissiles.client;

import dev.architectury.networking.NetworkManager;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;

import java.util.function.Supplier;

public class CreateFlashMessage {
    public final Integer  colour;
    public final BlockPos origin;
    public final Integer radius;
    public final Double intentsity;
    public final Integer length;

    public CreateFlashMessage(FriendlyByteBuf buf) {
        this(buf.readInt(), buf.readBlockPos(), buf.readInt(), buf.readDouble(), buf.readInt());
    }

    public CreateFlashMessage(Integer colour, BlockPos origin, Integer radius, Double intentsity, Integer length) {
        this.colour = colour;
        this.origin = origin;
        this.radius = radius;
        this.intentsity = intentsity;
        this.length = length;
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeInt(colour);
        buf.writeBlockPos(origin);
        buf.writeInt(radius);
        buf.writeDouble(intentsity);
        buf.writeInt(length);
    }

    public void apply(Supplier<NetworkManager.PacketContext> contextSupplier) {
        FlashHandler.addFlash(new FlashHandler.Flash(
                colour,
                origin,
                radius,
                intentsity,
                length
        ));
    }
}
