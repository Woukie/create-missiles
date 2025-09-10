package net.woukie.createmissiles.missilemanager.trajectories;
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

    public static double[] generateRange(double start, double end, int size) {
        double[] result = new double[size];
        double step = (end - start) / (size - 1);

        for (int i = 0; i < size; i++) {
            result[i] = start + i * step;
        }

        return result;
    }

    private static double getDistance(double angle, double thrust, double thrustDuration, double minHeight, double mass)
    {
        List<Vector2d> simulation = simulate(angle, thrust, thrustDuration, mass);

        double maxHeight = 0;
        for (int i = 0; i < simulation.size(); i++) {
            maxHeight = Math.max(maxHeight, simulation.get(i).y);
        }

        return (maxHeight < minHeight) ? 0 : simulation.get(simulation.size() - 1).x;
    }

    public static List<Vector2d> simulate(double angle, double thrust, double thrustDuration, double mass){
        Vector2d velocity = new Vector2d();
        Vector2d position = new Vector2d();
        List<Vector2d> missilePositions = new ArrayList<>();
        double time = 0;

        while (position.y >= 0) {

            double currentFlightAngle = Math.atan2(velocity.y, Math.sqrt(velocity.x * velocity.x));
            double approximateForceDueToAirResistance = 0.03d * (velocity.x * velocity.x + velocity.y * velocity.y); //0.031
            double horizontalForceDueToAirResistance = Math.cos(currentFlightAngle) * approximateForceDueToAirResistance;
            double verticalForceDueToAirResistance = -getSign(velocity.y) * Math.sin(currentFlightAngle) * approximateForceDueToAirResistance;
            double forceHorizontal = thrust * Math.cos(Math.toRadians(angle)) / mass;

            Vector2d acceleration = time >= thrustDuration ?
                    new Vector2d(
                            -getSign(velocity.x) * horizontalForceDueToAirResistance / mass,
                            verticalForceDueToAirResistance / mass + gravity.y
                    ) :
                    new Vector2d(
                            (-getSign(velocity.x) * horizontalForceDueToAirResistance / mass) + forceHorizontal,
                            (verticalForceDueToAirResistance / mass) + (thrust * Math.sin(Math.toRadians(angle)) / mass) + gravity.y
                    );

            velocity.add(acceleration.mul(deltaTime));
            position.add(velocity.x * deltaTime, velocity.y * deltaTime);
            missilePositions.add(new Vector2d(position.x, position.y));
            time += deltaTime;
        }
        return missilePositions;
    }

    private static boolean isValidTrajectory(double thrust, double angleDeg, double thrustDuration, double targetX, double minHeight, double mass) {

        double x = getDistance(angleDeg, thrust, thrustDuration, minHeight, mass);

        if(x != 0)
        {
            return (Math.abs(x - targetX) <= 1.0);
        }
        return false;
    }

    public static LaunchSolution findMinLaunchSolution(double targetX, double thrust, double minHeight, int angleStart, int angleEnd, double mass) {
        double[] angleRange = generateRange(angleStart, angleEnd, 100);
        for(double angle : angleRange) {
            double low = 0;
            double high = 30;

            for (int i = 0; i < 20; i++) {
                double mid = (low + high) / 2;
                if (isValidTrajectory(thrust, angle, mid, targetX, minHeight, mass)) {
                    return new LaunchSolution(mid, angle);
                }

                double x = getDistance(angle, thrust, mid, minHeight, mass);

                if (x < targetX) {
                    low = mid;
                } else {
                    high = mid;
                }
            }
        }
        return null;
    }

    public static double findLaunchAngle(double targetX, double thrust, double thrustDuration, double minHeight, int angleStart, int angleEnd, double mass) {
        double low = angleStart;
        double high = angleEnd;
        for (int i = 0; i < 50; i++) {
            double mid = (low + high) / 2;
            if (isValidTrajectory(thrust, mid, thrustDuration, targetX, minHeight, mass)) {
                return mid;
            }

            double x = getDistance(mid, thrust, thrustDuration, minHeight, mass);
            if (x > targetX) {
                low = mid;
            } else {
                high = mid;
            }
        }
        return angleStart;
    }

    private static int getSign(double val){
        return val >= 0 ? 1 : -1;
    }
}