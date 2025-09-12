package net.woukie.createmissiles.missilemanager.trajectories;
import net.minecraft.core.BlockPos;
import net.woukie.createmissiles.missilemanager.parts.ChassisType;
import net.woukie.createmissiles.missilemanager.parts.ThrusterType;
import net.woukie.createmissiles.missilemanager.parts.WarheadType;
import org.apache.commons.lang3.ArrayUtils;
import org.joml.Vector2d;
import org.joml.Vector3d;

import java.util.ArrayList;
import java.util.List;

public class TrajectoryHelper {
    private static final Vector2d gravity = new Vector2d(0, -9.81);
    private static final double deltaTime = 0.05;

    public static class LaunchSolution {
        public final double thrustDuration;
        public final double angle;

        public LaunchSolution(double thrustDuration, double angle) {
            this.thrustDuration = thrustDuration;
            this.angle = angle;
        }
    }

    public static class MissileConfig{
        public double thrust;
        public double mass;
        public double maxThrustDuration;

        public MissileConfig(ThrusterType thruster, ChassisType chassis, WarheadType warhead){
            this.thrust = thruster.getThrust();
            this.mass = thruster.getMass() + chassis.getMass() + warhead.getMass();
            this.maxThrustDuration = chassis.getFuelCapacity() / thruster.getBurnRate();
        }
    }

    public static class LaunchConfig{
        public BlockPos source;
        public BlockPos target;
        public double[] angleRange;
        public MissileConfig missileConfig;
        public double targetX;
        public double selectedThrustDuration;

        public LaunchConfig(BlockPos source, BlockPos target, double[] angleRange, double selectedThrustDuration, MissileConfig missileConfig){
            this.source = source;
            this.target = target;
            this.angleRange = angleRange;
            this.missileConfig = missileConfig;
            this.targetX = Vector3d.distance(target.getX(), 0, target.getZ(), source.getX(), 0, source.getZ());
            this.selectedThrustDuration = selectedThrustDuration;
        }
    }

    public static double[] generateRange(double start, double end, int size) {
        double[] result = new double[size];
        double step = (end - start) / (size - 1);

        for (int i = 0; i < size; i++) {
            result[i] = start + i * step;
        }

        return result;
    }

    public static List<Vector2d> simulate(LaunchConfig launchConfig, double newAngle, double newThrustDuration){
        Vector2d velocity = new Vector2d();
        Vector2d position = new Vector2d();
        List<Vector2d> missilePositions = new ArrayList<>();
        double time = 0;

        while (position.y >= launchConfig.target.getY() - launchConfig.source.getY() || velocity.y >= 0) {

            double currentFlightAngle = Math.atan2(velocity.y, Math.sqrt(velocity.x * velocity.x));
            double approximateForceDueToAirResistance = 0.03d * (velocity.x * velocity.x + velocity.y * velocity.y); //0.031
            double horizontalForceDueToAirResistance = Math.cos(currentFlightAngle) * approximateForceDueToAirResistance;
            double verticalForceDueToAirResistance = -getSign(velocity.y) * Math.sin(currentFlightAngle) * approximateForceDueToAirResistance;
            double forceHorizontal = launchConfig.missileConfig.thrust * Math.cos(Math.toRadians(newAngle)) / launchConfig.missileConfig.mass;

            Vector2d acceleration = time >= newThrustDuration ?
                    new Vector2d(
                            -getSign(velocity.x) * horizontalForceDueToAirResistance / launchConfig.missileConfig.mass,
                            verticalForceDueToAirResistance / launchConfig.missileConfig.mass + gravity.y
                    ) :
                    new Vector2d(
                            (-getSign(velocity.x) * horizontalForceDueToAirResistance / launchConfig.missileConfig.mass) + forceHorizontal,
                            (verticalForceDueToAirResistance / launchConfig.missileConfig.mass) + (launchConfig.missileConfig.thrust * Math.sin(Math.toRadians(newAngle)) / launchConfig.missileConfig.mass) + gravity.y
                    );

            velocity.add(acceleration.mul(deltaTime));
            position.add(velocity.x * deltaTime, velocity.y * deltaTime);
            missilePositions.add(new Vector2d(position.x, position.y));
            time += deltaTime;
        }
        return missilePositions;
    }

    private static boolean isValidTrajectory(LaunchConfig launchConfig, double newAngle, double newThrustDuration) {

        double x = simulate(launchConfig, newAngle, newThrustDuration).getLast().x;

        if(x != 0)
        {
            return (Math.abs(x - launchConfig.targetX) <= 1.0);
        }
        return false;
    }

    public static LaunchSolution findMinLaunchSolution(LaunchConfig launchConfig) {
        double[] angleRange = generateRange(launchConfig.angleRange[1], launchConfig.angleRange[0], 100);
        double[] thrustDurationRange = generateRange(0, launchConfig.missileConfig.maxThrustDuration, 100);

        for(int i = 1; i < thrustDurationRange.length - 1; i++){
            for(double angle : angleRange) {
                double low = thrustDurationRange[i-1];
                double high = thrustDurationRange[i+1];

                for (int j = 0; j < 5; j++) {
                    double mid = (low + high) / 2;
                    if (isValidTrajectory(launchConfig, angle, mid)) {
                        return new LaunchSolution(mid, angle);
                    }

                    double x = simulate(launchConfig, angle, mid).getLast().x;

                    if (x < launchConfig.targetX) {
                        low = mid;
                    } else {
                        high = mid;
                    }
                }
            }
        }
        return null;
    }


    public static double findLaunchAngle(LaunchConfig launchConfig, double thrustDuration) {
        double low = launchConfig.angleRange[0];
        double high = launchConfig.angleRange[1];
        for (int i = 0; i < 50; i++) {
            double mid = (low + high) / 2;
            if (isValidTrajectory(launchConfig, mid, thrustDuration)) {
                return mid;
            }

            double x = simulate(launchConfig, mid, thrustDuration).getLast().x;;
            if (x > launchConfig.targetX) {
                low = mid;
            } else {
                high = mid;
            }
        }
        return (low + high) / 2;
    }

    private static int getSign(double val){
        return val >= 0 ? 1 : -1;
    }
}