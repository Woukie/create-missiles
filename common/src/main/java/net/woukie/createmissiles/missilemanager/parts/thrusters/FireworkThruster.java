package net.woukie.createmissiles.missilemanager.parts.thrusters;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.level.Level;
import net.woukie.createmissiles.CreateMissiles;
import net.woukie.createmissiles.client.MissilePartModel;
import net.woukie.createmissiles.client.models.thrusters.FireworkThrusterModel;
import net.woukie.createmissiles.missilemanager.Trajectory;
import net.woukie.createmissiles.missilemanager.parts.ChassisType;
import net.woukie.createmissiles.missilemanager.parts.ThrusterType;
import net.woukie.createmissiles.missilemanager.parts.WarheadType;
import net.woukie.createmissiles.missilemanager.trajectories.BallisticTrajectory;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3d;

public class FireworkThruster extends ThrusterType {
    MissilePartModel model = new FireworkThrusterModel();

    @Override
    public Trajectory serializeTrajectory(CompoundTag data, MinecraftServer server) {
        return new BallisticTrajectory(data, server);
    }

    @Override
    public void onTick(Trajectory trajectory, MinecraftServer server) {
        super.onTick(trajectory, server);
        var p = trajectory.getPosition();
        ServerLevel level = server.getLevel(trajectory.getLevelKey());
        if (level != null) level.sendParticles(ParticleTypes.CLOUD, p.x, p.y, p.z, 5, 0.1, 0, 0, 0);
    }

    @Override
    public Trajectory createTrajectory(Level level, Vector3d start, Vector3d target, WarheadType warheadType, ChassisType chassisType, ThrusterType thrusterType, Container container) {
        return new BallisticTrajectory(level, start, target, warheadType, chassisType, thrusterType, container);
    }

    @Override
    public float getThrust() {
        return 10;
    }

    @Override
    public float getBurnRate() {
        return 1;
    }

    @Override
    public void onLaunch(Trajectory trajectory, ServerLevel level) {
        var p = trajectory.getPosition();
        level.playSound(null, p.x, p.y, p.z, SoundEvents.FIREWORK_ROCKET_LAUNCH, SoundSource.NEUTRAL, 1, 1);
    }

    @Override
    public @NotNull MissilePartModel getModel() {
        return model;
    }

    @Override
    public ResourceLocation getResourceLocation() {
        return new ResourceLocation(CreateMissiles.MOD_ID, "firework_thruster");
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("thrusters.createmissiles.firework_thruster");
    }
}
