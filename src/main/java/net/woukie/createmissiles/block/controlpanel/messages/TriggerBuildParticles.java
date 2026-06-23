package net.woukie.createmissiles.block.controlpanel.messages;

import dev.architectury.networking.NetworkManager;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.woukie.createmissiles.registry.PartModels;
import net.woukie.createmissiles.registry.PartTypes;
import org.joml.Vector3f;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

// Must be packet as server does not know how high the build is due to models being on the client
public class TriggerBuildParticles {
    public final Vector3f pos;
    public final ResourceLocation warhead, chassis, thruster;
    public final int warheadBuildPercent, chassisBuildPercent, thrusterBuildPercent;

    public TriggerBuildParticles(FriendlyByteBuf buf) {
        this(buf.readVector3f(), buf.readResourceLocation(), buf.readResourceLocation(), buf.readResourceLocation(), buf.readInt(), buf.readInt(), buf.readInt());
    }

    public TriggerBuildParticles(Vector3f pos, ResourceLocation warhead, ResourceLocation chassis, ResourceLocation thruster, int warheadBuildPercent, int chassisBuildPercent, int thrusterBuildPercent) {
        this.pos = pos;
        this.warhead = warhead;
        this.chassis = chassis;
        this.thruster = thruster;
        this.warheadBuildPercent = warheadBuildPercent;
        this.chassisBuildPercent = chassisBuildPercent;
        this.thrusterBuildPercent = thrusterBuildPercent;
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeVector3f(pos);
        buf.writeResourceLocation(warhead);
        buf.writeResourceLocation(chassis);
        buf.writeResourceLocation(thruster);
        buf.writeInt(warheadBuildPercent);
        buf.writeInt(chassisBuildPercent);
        buf.writeInt(thrusterBuildPercent);
    }

    public void apply(Supplier<NetworkManager.PacketContext> contextSupplier) {
        var warheadType = PartTypes.get(warhead);
        var chassisType = PartTypes.get(chassis);
        var thrusterType = PartTypes.get(thruster);

        Map<String, Vector3f> thrusterAttachments = new HashMap<>();
        Map<String, Vector3f> chassisAttachments = new HashMap<>();
        Map<String, Vector3f> warheadAttachments = new HashMap<>();

        if (thrusterType != null) {
            var model = PartModels.getModel(thrusterType.getResourceLocation());
            thrusterAttachments = model.getAttachements(model.getStage(thrusterBuildPercent));
        }

        if (chassisType != null) {
            var model = PartModels.getModel(chassisType.getResourceLocation());
            chassisAttachments = model.getAttachements(model.getStage(chassisBuildPercent));
        }

        if (warheadType != null) {
            var model = PartModels.getModel(warheadType.getResourceLocation());
            warheadAttachments = model.getAttachements(model.getStage(thrusterBuildPercent));
        }

        Vector3f rocketTip = new Vector3f()
                .add(thrusterAttachments.getOrDefault("bottom", new Vector3f()))
                .add(thrusterAttachments.getOrDefault("top", new Vector3f()))
                .add(chassisAttachments.getOrDefault("bottom", new Vector3f()))
                .add(chassisAttachments.getOrDefault("top", new Vector3f()))
                .add(warheadAttachments.getOrDefault("bottom", new Vector3f()))
                .add(warheadAttachments.getOrDefault("top", new Vector3f()))
                .div(16);

        var r = Math.random();
        for (int i = 0; i < r * 2 + 1; i++) {
            var x = pos.x + rocketTip.x * Math.random();
            var y = pos.y + 0.5 + rocketTip.y * Math.random();
            var z = pos.z + rocketTip.z * Math.random();
            x += Math.random() * 0.1 - 0.05;
            z += Math.random() * 0.1 - 0.05;
            for (int j = 0; j < 10; j++) {
                var player = contextSupplier.get().getPlayer();
                if (player == null) return;
                player.level().addParticle(net.woukie.createmissiles.registry.ParticleTypes.BUILD_SHRAPNEL.get(), x, y, z, 0, 0, 0);
            }
        }
    }
}
