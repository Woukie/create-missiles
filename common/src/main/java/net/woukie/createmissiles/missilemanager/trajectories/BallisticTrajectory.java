package net.woukie.createmissiles.missilemanager.trajectories;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.phys.Vec3;
import net.woukie.createmissiles.missilemanager.Trajectory;
import net.woukie.createmissiles.missilemanager.TrajectoryData;

public class BallisticTrajectory extends Trajectory {
    private float tickLength = 0.05f; // Seconds

    private Vec3 globalPosition; // m
    private Vec3 velocity; // ms^-1

    private Vec3 gravity; // N

    private float mass; // g
    private Vec3 rocketAngle; // radians
    private float thrust; // N in direction of rocketAngle

    @Override
    public void tick() {
        Vec3 thrustForce = new Vec3(
                thrust * Math.cos(rocketAngle.y) * Math.cos(rocketAngle.x),
                thrust * Math.sin(rocketAngle.y),
                thrust * Math.cos(rocketAngle.y) * Math.sin(rocketAngle.x)
        );

        Vec3 totalForce = gravity.add(thrustForce);
        Vec3 acceleration = totalForce.scale(1.0f / mass);
        velocity = velocity.add(acceleration.scale(tickLength));
        globalPosition = globalPosition.add(velocity.scale(tickLength));
    }

    @Override
    public Vec3 getPosition() {
        return globalPosition;
    }

    @Override
    public boolean shouldExplode() {
        return false;
    }

    @Override
    public void saveTo(CompoundTag data) {

    }

    @Override
    public Trajectory loadFrom(CompoundTag data) {
        return null;
    }
}
