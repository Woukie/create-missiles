package net.woukie.createmissiles.missilemanager.parts.thrusters;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.woukie.createmissiles.CreateMissiles;
import net.woukie.createmissiles.client.MissilePartModel;
import net.woukie.createmissiles.client.models.thrusters.FireworkThrusterModel;
import net.woukie.createmissiles.missilemanager.Trajectory;
import net.woukie.createmissiles.missilemanager.TrajectoryData;
import net.woukie.createmissiles.missilemanager.parts.ThrusterType;
import net.woukie.createmissiles.missilemanager.trajectories.BallisticTrajectory;
import org.jetbrains.annotations.NotNull;

public class FireworkThruster extends ThrusterType {
    MissilePartModel model = new FireworkThrusterModel();

    @Override
    public Trajectory createTrajectory(TrajectoryData data) {
        return new BallisticTrajectory(data);
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
    public void onLaunch(Trajectory trajectory) {
        var level = (ServerLevel) trajectory.data.level;
        if (level != null) {
            var p = trajectory.getPosition(0);
            level.playSound(null, p.x, p.y, p.z, SoundEvents.FIREWORK_ROCKET_LAUNCH, SoundSource.NEUTRAL, 1, 1);
        }
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
