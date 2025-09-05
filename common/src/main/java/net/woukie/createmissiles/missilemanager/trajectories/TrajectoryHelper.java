package net.woukie.createmissiles.missilemanager.trajectories;
import org.joml.Vector2d;

public class TrajectoryHelper {
    private static final double gravity = 9.81;
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
        double angleRad = Math.toRadians(angle);
        Vector2d velocity = new Vector2d(Math.cos(angleRad), Math.sin(angleRad)).mul(thrust).div(mass);
        Vector2d position = new Vector2d();

        double time = 0;
        double maxHeight = position.y;

        while (position.y >= 0) {
            Vector2d acceleration = new Vector2d(
                    (time >= thrustDuration) ? 0 : thrust * Math.cos(angleRad) / mass,
                    (time >= thrustDuration) ? -gravity : thrust * Math.sin(angleRad) / mass - gravity
            );

            velocity.add(acceleration.mul(deltaTime));
            position.add(velocity.x * deltaTime, velocity.y * deltaTime);
            maxHeight = Math.max(maxHeight, position.y);
            time += deltaTime;
        }

        if(maxHeight < minHeight)
        {
            return 0;
        }
        return position.x;
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
}