package net.woukie.createmissiles.missiles.trajectories;

import net.minecraft.core.Rotations;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.Container;
import net.minecraft.world.level.Level;
import net.woukie.createmissiles.entity.MissileEntity;
import net.woukie.createmissiles.missiles.Trajectory;
import net.woukie.createmissiles.missiles.parts.ChassisType;
import net.woukie.createmissiles.missiles.parts.ThrusterType;
import net.woukie.createmissiles.missiles.parts.WarheadType;
import org.jetbrains.annotations.Nullable;
import org.joml.*;
import oshi.util.tuples.Pair;

import java.lang.Math;
import java.util.ArrayList;
import java.util.List;

public class BallisticTrajectory extends Trajectory {
    public final Vector3d gravity = new Vector3d(0, -9.81, 0);
    public final double tickSpeed = 20;
    protected Vector3d velocity;
    protected Vector3d rotation;

    private double thrust;
    private double thrustDurationPercent;
    private double mass;
    private Vector3d launchDirection;

    protected Double upperLaunchAngle;
    protected Double upperDistanceToTarget;

    protected Double lowerLaunchAngle;
    protected Double lowerDistanceToTarget;

    protected boolean stopRefiningAngle = false;

    @Override
    public void tick() {
        super.tick();

        double tickLength = 1 / tickSpeed;
        double elapsedTime = this.tick * tickLength;

        if (launchDirection == null) {
            launchDirection = new Vector3d(0, 0, 0);
        }

        double currentFlightAngle = Math.atan2(velocity.y, Math.sqrt(velocity.x * velocity.x + velocity.z * velocity.z));
        Vector3d acceleration = getAcceleration(currentFlightAngle, elapsedTime);

        velocity.add(acceleration.mul(tickLength));
        lastPosition = new Vector3d(position);
        position.add(velocity.x * tickLength, velocity.y * tickLength, velocity.z * tickLength);

        Vector3d direction = new Vector3d(velocity.x, velocity.y, velocity.z);
        direction.normalize();
        Vector3d forward = new Vector3d(0, 1, 0);
        Quaterniond q = new Quaterniond().rotateTo(forward, direction);

        Vector3d euler = q.getEulerAnglesZYX(new Vector3d());

        rotation.set(euler.x, euler.y, euler.z);
    }

    private Vector3d getAcceleration(double currentFlightAngle, double elapsedTime) {
        double approximateForceDueToAirResistance = 0.03d * (velocity.x * velocity.x + velocity.y * velocity.y + velocity.z * velocity.z); //0.031
        double horizontalForceDueToAirResistance = Math.cos(currentFlightAngle) * approximateForceDueToAirResistance;
        double verticalForceDueToAirResistance = -getSign(velocity.y) * Math.sin(currentFlightAngle) * approximateForceDueToAirResistance;
        double launchAngle = (upperLaunchAngle + lowerLaunchAngle) / 2;
        double forceHorizontal = thrust * Math.cos(Math.toRadians(launchAngle)) / mass;

        if (elapsedTime >= thrustDurationPercent * chassisType.getFuelCapacity() / thrusterType.getBurnRate()) {
            return new Vector3d(
                    -getSign(velocity.x) * horizontalForceDueToAirResistance * launchDirection.x / mass,
                    verticalForceDueToAirResistance / mass + gravity.y,
                    -getSign(velocity.z) * horizontalForceDueToAirResistance * launchDirection.z / mass
            );
        } else {
            return new Vector3d(
                    (-getSign(velocity.x) * horizontalForceDueToAirResistance * launchDirection.x / mass) + (launchDirection.x * forceHorizontal),
                    (verticalForceDueToAirResistance / mass) + (thrust * Math.sin(Math.toRadians(launchAngle)) / mass) + gravity.y,
                    (-getSign(velocity.z) * horizontalForceDueToAirResistance * launchDirection.z / mass) + (launchDirection.z * forceHorizontal)
            );
        }
    }

    //    Called when launching a missile from the control panel, and when simulating (next constructor)
    public BallisticTrajectory(@Nullable Level level, Vector3d start, Vector3d target, WarheadType warheadType, ChassisType chassisType, ThrusterType thrusterType, @Nullable Container container, double thrustDurationPercent) {
        super(level != null ? level.dimension() : null, start, target, warheadType, chassisType, thrusterType, container);
        this.velocity = new Vector3d(0, 0, 0);
        this.rotation = new Vector3d(0, 0, 0);
        this.thrust = thrusterType.getThrust();
        this.thrustDurationPercent = thrustDurationPercent;
        this.mass = thrusterType.getMass() + chassisType.getMass() + warheadType.getMass();
        this.launchDirection = new Vector3d(target.x - start.x, 0, target.z - start.z).normalize();
        this.upperLaunchAngle = 90d;
        this.lowerLaunchAngle = 0d;
    }

//    Called when simulating without a world, not intended for serialisation, see getDistanceToTarget()
    public BallisticTrajectory(Vector3d start, Vector3d target, WarheadType warheadType, ChassisType chassisType, ThrusterType thrusterType, double launchAngle, double thrustDurationPercent) {
        this(null, start, target, warheadType, chassisType, thrusterType, null, thrustDurationPercent);
        this.upperLaunchAngle = launchAngle;
        this.lowerLaunchAngle = launchAngle;
    }

    //    Called when deserialising trajectories
    public BallisticTrajectory(CompoundTag data, MinecraftServer server) {
        super(data, server);
        this.velocity = new Vector3d(data.getDouble("VelocityX"), data.getDouble("VelocityY"), data.getDouble("VelocityZ"));
        this.rotation = new Vector3d(data.getDouble("RotationX"), data.getDouble("RotationY"), data.getDouble("RotationZ"));
        this.thrust = data.getDouble("Thrust");
        this.thrustDurationPercent = data.getDouble("ThrustDurationPercent");
        this.mass = data.getDouble("Mass");
        this.launchDirection = new Vector3d(data.getDouble("LaunchDirectionX"), data.getDouble("LaunchDirectionY"), data.getDouble("LaunchDirectionZ"));
        this.upperLaunchAngle = data.getDouble("UpperLaunchAngle");
        this.lowerLaunchAngle = data.getDouble("LowerLaunchAngle");
        this.upperDistanceToTarget = data.contains("UpperDistanceToTarget") ? data.getDouble("UpperDistanceToTarget") : null;
        this.lowerDistanceToTarget = data.contains("LowerDistanceToTarget") ? data.getDouble("LowerDistanceToTarget") : null;
        this.stopRefiningAngle = data.getBoolean("StopRefiningAngle");
    }

    //    Called when serializing a trajectory when exiting the world
    @Override
    public CompoundTag saveTo(CompoundTag data) {
        CompoundTag superData = super.saveTo(data);
        superData.putDouble("VelocityX", velocity.x);
        superData.putDouble("VelocityY", velocity.y);
        superData.putDouble("VelocityZ", velocity.z);
        superData.putDouble("RotationX", rotation.x);
        superData.putDouble("RotationY", rotation.y);
        superData.putDouble("RotationZ", rotation.z);
        superData.putDouble("Thrust", thrust);
        superData.putDouble("ThrustDurationPercent", thrustDurationPercent);
        superData.putDouble("Mass", mass);
        superData.putDouble("LaunchDirectionX", launchDirection.x);
        superData.putDouble("LaunchDirectionY", launchDirection.y);
        superData.putDouble("LaunchDirectionZ", launchDirection.z);
        superData.putDouble("UpperLaunchAngle", upperLaunchAngle);
        superData.putDouble("LowerLaunchAngle", lowerLaunchAngle);
        if (upperDistanceToTarget != null) superData.putDouble("UpperDistanceToTarget", upperDistanceToTarget);
        if (lowerDistanceToTarget != null) superData.putDouble("LowerDistanceToTarget", lowerDistanceToTarget);
        superData.putBoolean("StopRefiningAngle", stopRefiningAngle);
        return superData;
    }

    public void refineLaunchAngleOnce() {
//        Unlikely to actually do all of these unless there's no solution
//        TODO: Make no solution simulations shorter
        int angles = 40;

        if (stopRefiningAngle) return;

        if (upperDistanceToTarget == null) upperDistanceToTarget = getDistanceToTarget(upperLaunchAngle);
        if (lowerDistanceToTarget == null) lowerDistanceToTarget = getDistanceToTarget(lowerLaunchAngle);

        List<Pair<Double, Double>> distances = new ArrayList<>();
        double angleStep = (upperLaunchAngle - lowerLaunchAngle) / (angles + 1);
        distances.add(new Pair<>(upperLaunchAngle, upperDistanceToTarget));
        for (int i = angles - 1; i >= 0; i--) {
            distances.add(new Pair<>(lowerLaunchAngle + angleStep * (i + 1), null));
        }
        distances.add(new Pair<>(lowerLaunchAngle, lowerDistanceToTarget));

        Double bestUpperDistance = Double.POSITIVE_INFINITY;
        Double bestLowerDistance = Double.POSITIVE_INFINITY;
        Double bestUpperAngle = 0D;
        Double bestLowerAngle = 0D;
        for (int i = 0; i < distances.size() - 1; i++) {
            var upper = distances.get(i);
            var lower = distances.get(i + 1);
            Double upperAngle = upper.getA();
            Double upperDistance = upper.getB() != null ? upper.getB() : getDistanceToTarget(upperAngle);
            Double lowerAngle = lower.getA();
            Double lowerDistance = lower.getB() != null ? lower.getB() : getDistanceToTarget(lowerAngle);
            distances.set(i + 1, new Pair<>(lowerAngle, lowerDistance));
            double averageDistance = (upperDistance + lowerDistance) / 2;
            if (averageDistance < (bestUpperDistance + bestLowerDistance) / 2) {
                bestUpperDistance = upperDistance;
                bestLowerDistance = lowerDistance;
                bestUpperAngle = upperAngle;
                bestLowerAngle = lowerAngle;
            } else {
//                Get first solution, there are usually 2 solutions and 'near' solutions, highest is always real
                break;
            }
        }

        if ((lowerDistanceToTarget + upperDistanceToTarget) / 2 - (bestUpperDistance + bestLowerDistance) / 2 < 0.01) stopRefiningAngle = true;
        upperDistanceToTarget = bestUpperDistance;
        lowerDistanceToTarget = bestLowerDistance;
        upperLaunchAngle = bestUpperAngle;
        lowerLaunchAngle = bestLowerAngle;
    }

    //    Simulates a trajectory at this angle, gets the closest it is to the target before going away
    private double getDistanceToTarget(double angle) {
        BallisticTrajectory simulatedTrajectory = new BallisticTrajectory(
                new Vector3d(initialPosition),
                new Vector3d(targetPosition),
                warheadType,
                chassisType,
                thrusterType,
                angle,
                thrustDurationPercent
        );

        double minDistance = Double.POSITIVE_INFINITY;
        double previousDistance = Double.POSITIVE_INFINITY;
        while (true) {
            Vector3d currentPosition = new Vector3d(simulatedTrajectory.getPosition());
            double currentDistance = targetPosition.distanceSquared(currentPosition);
            minDistance = Math.min(minDistance, currentDistance);

            boolean descending = simulatedTrajectory.velocity.y < 0;
            if (descending && (currentDistance > previousDistance || currentPosition.y < targetPosition.y)) {
                break;
            }

            previousDistance = currentDistance;
            simulatedTrajectory.tick();
        }

        return minDistance;
    }

    @Override
    public void updateEntityModel(MissileEntity entity) {
        super.updateEntityModel(entity);
        if (entity == null) return;
        entity.setRotation(new Rotations((float) rotation.x, (float) rotation.y, (float) rotation.z));
    }

    public Double getUpperDistanceToTarget() {
        return upperDistanceToTarget;
    }

    public Double getLowerDistanceToTarget() {
        return lowerDistanceToTarget;
    }

    public Double getUpperLaunchAngle() {
        return upperLaunchAngle;
    }

    public Double getLowerLaunchAngle() {
        return lowerLaunchAngle;
    }

    public Vector3d getVelocity() {
        return new Vector3d(velocity);
    }

    public void setUpperLaunchAngle(Double upperLaunchAngle) {
        this.upperLaunchAngle = upperLaunchAngle;
    }

    public void setLowerLaunchAngle(Double lowerLaunchAngle) {
        this.lowerLaunchAngle = lowerLaunchAngle;
    }

    private int getSign(double val){
        return val >= 0 ? 1 : -1;
    }
}