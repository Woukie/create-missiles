package net.woukie.createmissiles.block.controlpanel.messages;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.woukie.createmissiles.CreateMissiles;
import net.woukie.createmissiles.Util;
import net.woukie.createmissiles.registry.PartModels;
import net.woukie.createmissiles.registry.PartTypes;
import org.joml.Vector3f;

import java.util.HashMap;
import java.util.Map;

// Must be packet as server does not know how high the build is due to models being on the client
public record TriggerBuildParticles(Vector3f pos, String warhead, String chassis, String thruster, int warheadBuildPercent, int chassisBuildPercent, int thrusterBuildPercent) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<TriggerBuildParticles> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(CreateMissiles.MOD_ID, "trigger_build_particles"));


    public static final StreamCodec<ByteBuf, TriggerBuildParticles> STREAM_CODEC = Util.composite(
            ByteBufCodecs.VECTOR3F,
            TriggerBuildParticles::pos,
            ByteBufCodecs.STRING_UTF8,
            TriggerBuildParticles::warhead,
            ByteBufCodecs.STRING_UTF8,
            TriggerBuildParticles::chassis,
            ByteBufCodecs.STRING_UTF8,
            TriggerBuildParticles::thruster,
            ByteBufCodecs.INT,
            TriggerBuildParticles::warheadBuildPercent,
            ByteBufCodecs.INT,
            TriggerBuildParticles::chassisBuildPercent,
            ByteBufCodecs.INT,
            TriggerBuildParticles::thrusterBuildPercent,
            TriggerBuildParticles::new
    );

    public void apply(final IPayloadContext context) {
        var warheadType = PartTypes.get(ResourceLocation.parse(warhead));
        var chassisType = PartTypes.get(ResourceLocation.parse(chassis));
        var thrusterType = PartTypes.get(ResourceLocation.parse(thruster));

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
                var player = context.player();
                player.level().addParticle(net.woukie.createmissiles.registry.ParticleTypes.BUILD_SHRAPNEL.get(), x, y, z, 0, 0, 0);
            }
        }
    }

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
