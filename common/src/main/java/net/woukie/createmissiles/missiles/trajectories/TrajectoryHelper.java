package net.woukie.createmissiles.missiles.trajectories;
import org.joml.Vector2d;

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

    private static double getDistance(double angle, double thrust, double thrustDuration, double minHeight, double mass, double targetY, double sourceY)
    {
        List<Vector2d> simulation = simulate(angle, thrust, thrustDuration, mass, targetY, sourceY);

        double maxHeight = 0;
        for (int i = 0; i < simulation.size(); i++) {
            maxHeight = Math.max(maxHeight, simulation.get(i).y);
        }

        return (maxHeight < minHeight) ? 0 : simulation.get(simulation.size() - 1).x;
    }

    public static List<Vector2d> simulate(double angle, double thrust, double thrustDuration, double mass, double targetY, double sourceY){
        Vector2d velocity = new Vector2d();
        Vector2d position = new Vector2d();
        List<Vector2d> missilePositions = new ArrayList<>();
        double time = 0;

        while (position.y >= targetY - sourceY || velocity.y >= 0) {

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

    private static boolean isValidTrajectory(double thrust, double angleDeg, double thrustDuration, double targetX, double minHeight, double mass, double targetY, double sourceY) {

        double x = getDistance(angleDeg, thrust, thrustDuration, minHeight, mass, targetY, sourceY);

        if(x != 0)
        {
            return (Math.abs(x - targetX) <= 1.0);
        }
        return false;
    }

    public static LaunchSolution findMinLaunchSolution(double targetX, double thrust, double minHeight, int angleStart, int angleEnd, double mass, double targetY, double sourceY) {
        double[] angleRange = generateRange(angleEnd, angleStart, 100);
        double[] thrustDurationRange = generateRange(0, 30, 100);

        for(int i = 1; i < thrustDurationRange.length - 1; i++){
            for(double angle : angleRange) {
                double low = thrustDurationRange[i-1];
                double high = thrustDurationRange[i+1];

                for (int j = 0; j < 5; j++) {
                    double mid = (low + high) / 2;
                    if (isValidTrajectory(thrust, angle, mid, targetX, minHeight, mass, targetY, sourceY)) {
                        System.out.println(angle);
                        System.out.println(mid);
                        return new LaunchSolution(mid, angle);
                    }

                    double x = getDistance(angle, thrust, mid, minHeight, mass, targetY, sourceY);

                    if (x < targetX) {
                        low = mid;
                    } else {
                        high = mid;
                    }
                }
            }
        }
        return null;
    }


    public static double findLaunchAngle(double targetX, double thrust, double thrustDuration, double minHeight, int angleStart, int angleEnd, double mass, double targetY, double sourceY) {
        double low = angleStart;
        double high = angleEnd;
        for (int i = 0; i < 50; i++) {
            double mid = (low + high) / 2;
            if (isValidTrajectory(thrust, mid, thrustDuration, targetX, minHeight, mass, targetY, sourceY)) {
                return mid;
            }

            double x = getDistance(mid, thrust, thrustDuration, minHeight, mass, targetY, sourceY);
            if (x > targetX) {
                low = mid;
            } else {
                high = mid;
            }
        }
        System.out.println(angleStart);
        return angleStart;
    }

    private static int getSign(double val){
        return val >= 0 ? 1 : -1;
    }
}