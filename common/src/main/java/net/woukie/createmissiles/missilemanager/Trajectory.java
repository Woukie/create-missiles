package net.woukie.createmissiles.missilemanager;

import net.minecraft.server.MinecraftServer;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.woukie.createmissiles.missilemanager.parts.WarheadType;

public class Trajectory {
//    Data computed at control panel
    private TrajectoryData data;

//    Data computed at run-time
    private double angle;
    private double launchVelocity;
    private double flightLength;
    private boolean canHitTarget;
    private Vec2 localTarget;

    public Trajectory(TrajectoryData data) {
        this.data = data;
        reCalculate();
    }

    public TrajectoryData getData() {
        return data;
    }

    public void setData(TrajectoryData data) {
        this.data = data;
        reCalculate();
    }

    public void incrementTick() {
        this.data.incrementTick();
    }

    private void reCalculate() {
        Vec2 distanceXZ = new Vec2(
                data.target.getX() - data.source.getX(),
                data.target.getZ() - data.source.getZ()
        );

        this.launchVelocity = 25F;
        this.localTarget = new Vec2(distanceXZ.length(), data.target.getY() - data.source.getY());
        ProjectileUtils.LaunchInfo info = ProjectileUtils.getLaunchInfo(localTarget.x, localTarget.y, launchVelocity, data.gravity);
        this.angle = info.getAngle();
        this.flightLength = info.getTime();
        this.canHitTarget = info.canHitTarget();
    }

//    Wraps Vec2 getPosition
    public Vec3 getPosition(float second) {
        Vec2 position = getLocalPosition(second);
        Vec2 distanceXZ = new Vec2(
                data.target.getX() - data.source.getX(),
                data.target.getZ() - data.source.getZ()
        );

        float horizontalProportionTravelled = position.x / localTarget.x;
        return new Vec3(
                data.source.getX() + horizontalProportionTravelled * distanceXZ.x,
                data.source.getY() + position.y,
                data.source.getZ() + horizontalProportionTravelled * distanceXZ.y
        );
    }

//    For da expert salad:
//    Use 'source' and 'target' block positions to calculate the position in the trajectory at time 'ticks' (1 tick = 0.05s)
//    This isn't static because we will be referencing instance data
    public Vec2 getLocalPosition(float second) {
        return ProjectileUtils.getPositionAt(launchVelocity, angle, data.gravity, second);
    }

//    Gets how long the flight will take from launch to impact
    public double getImpactTime() {
        return this.flightLength;
    }

    public boolean canHitTarget() {
        return this.canHitTarget;
    }

    public boolean shouldExplode() {
        return (data.getTick() / 20F) > flightLength;
    }
}
