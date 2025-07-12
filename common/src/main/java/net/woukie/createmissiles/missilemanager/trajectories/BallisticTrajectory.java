package net.woukie.createmissiles.missilemanager.trajectories;

import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.woukie.createmissiles.missilemanager.Trajectory;
import net.woukie.createmissiles.missilemanager.TrajectoryData;

/**
 * Instant burn time trajectory, uses thruster burn <code>time * fuel</code> as initial velocity
 */
public class BallisticTrajectory extends Trajectory {
    private final CachedData cachedData = new CachedData();

    public BallisticTrajectory(TrajectoryData data) {
        super(data);
        calculateCache();
    }

    @Override
    public Vec3 getPosition(float second) {
        Vec2 position = getLocalPosition(second);
        Vec2 distanceXZ = new Vec2(
                data.target.getX() - data.source.getX(),
                data.target.getZ() - data.source.getZ()
        );

        float horizontalProportionTravelled = position.x / cachedData.localTarget.x;
        return new Vec3(
                data.source.getX() + horizontalProportionTravelled * distanceXZ.x,
                data.source.getY() + position.y,
                data.source.getZ() + horizontalProportionTravelled * distanceXZ.y
        );
    }

    @Override
    public Vec3 getPosition() {
        return null;
    }

    @Override
    public double getFlightTime() {
        return 0;
    }

    @Override
    public boolean canHitTarget() {
        return true;
    }

    @Override
    public boolean shouldExplode() {
        return (data.getTick() / 20F) > cachedData.flightLength;
    }

    private void calculateCache() {

    }

    private static class CachedData {
        public double angle;
        public double launchVelocity;
        public double flightLength;
        public boolean canHitTarget;
        public Vec2 localTarget;

//        And any more variables you need to make future operations faster
    }
}
