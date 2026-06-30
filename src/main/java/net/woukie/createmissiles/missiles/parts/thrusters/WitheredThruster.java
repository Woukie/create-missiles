package net.woukie.createmissiles.missiles.parts.thrusters;

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
import net.woukie.createmissiles.block.navigationpanel.NavigationPanelBlockEntity;
import net.woukie.createmissiles.missiles.Trajectory;
import net.woukie.createmissiles.missiles.parts.ChassisType;
import net.woukie.createmissiles.missiles.parts.ThrusterType;
import net.woukie.createmissiles.missiles.parts.WarheadType;
import net.woukie.createmissiles.missiles.trajectories.BallisticTrajectory;
import org.joml.Vector3d;

public class WitheredThruster extends ThrusterType {
    @Override
    public Trajectory constructTrajectory(CompoundTag data, MinecraftServer server) {
        return new BallisticTrajectory(data, server);
    }

    @Override
    public void onTick(Trajectory trajectory, MinecraftServer server) {
        super.onTick(trajectory, server);
        var p = trajectory.getPosition();
        ServerLevel level = server.getLevel(trajectory.getLevelKey());
        if (level != null) level.sendParticles(ParticleTypes.SMOKE, p.x, p.y, p.z, 5, 0.1, 0, 0, 0);
    }

    @Override
    public Trajectory createTrajectory(Level level, Vector3d start, Vector3d target, WarheadType warheadType, ChassisType chassisType, ThrusterType thrusterType, Container container, NavigationPanelBlockEntity navPanel) {
        return new BallisticTrajectory(level, start, target, warheadType, chassisType, thrusterType, container, navPanel.getThrustDurationPercent());
    }

    @Override
    public float getThrust() {
        return 4500f;
    }

    @Override
    public float getBurnRate() {
        return 20f;
    }

    @Override
    public float getMass() {
        return 20f;
    }

    @Override
    public void onLaunch(Trajectory trajectory, ServerLevel level) {
        var p = trajectory.getPosition();
        level.playSound(null, p.x, p.y, p.z, SoundEvents.ENDER_DRAGON_FLAP, SoundSource.NEUTRAL, 1, 1);
    }

    @Override
    public ResourceLocation getResourceLocation() {
        return ResourceLocation.fromNamespaceAndPath(CreateMissiles.MOD_ID, "withered_thruster");
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("thrusters.createmissiles.withered_thruster");
    }
}
