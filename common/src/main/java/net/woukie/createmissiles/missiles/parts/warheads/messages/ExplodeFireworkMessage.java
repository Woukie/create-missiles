package net.woukie.createmissiles.missiles.parts.warheads.messages;

import dev.architectury.networking.NetworkManager;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import org.joml.Vector3f;

import java.util.function.Supplier;

public class ExplodeFireworkMessage {
    public final Vector3f pos;
    public final Vector3f vel;
    public final CompoundTag tag;

    public ExplodeFireworkMessage(FriendlyByteBuf buf) {
        this(buf.readVector3f(), buf.readVector3f(), buf.readNbt());
    }

    public ExplodeFireworkMessage(Vector3f pos, Vector3f vel, CompoundTag tag) {
        this.pos = pos;
        this.vel = vel;
        this.tag = tag;
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeVector3f(pos);
        buf.writeVector3f(vel);
        buf.writeNbt(tag);
    }

    public void apply(Supplier<NetworkManager.PacketContext> contextSupplier) {
        contextSupplier.get().getPlayer().level().createFireworks(pos.x, pos.y, pos.z, vel.x, vel.y, vel.z, tag);
    }
}
