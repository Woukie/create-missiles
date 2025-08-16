package net.woukie.createmissiles.missilemanager.trajectories;
import org.joml.Vector2d;

public class TrajectoryHelper {
    private static final double gravity = 9.81;
    private static final double mass = 1.0;
    private static final double deltaTime = 0.05;

    public static class LaunchSolution {
        public final double thrust;
        public final double angle;

        public LaunchSolution(double thrust, double angle) {
            this.thrust = thrust;
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

    private static double getDistance(double angle, double thrust, double thrustDuration, double minHeight)
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

    private static boolean isValidTrajectory(double thrust, double angleDeg, double thrustDuration, double targetX, double minHeight) {

        double x = getDistance(angleDeg, thrust, thrustDuration, minHeight);

        if(x != 0)
        {
            return (Math.abs(x - targetX) <= 1.0);
        }
        return false;
    }

    public static LaunchSolution findLaunchSolution(double targetX, double thrustDuration, double minHeight, int angleStart, int angleEnd) {

        double[] angleRange = generateRange(angleStart, angleEnd, 100);
        for(double angle : angleRange) {
            double low = 0;
            double high = 300;

            for (int i = 0; i < 20; i++) {
                double mid = (low + high) / 2;
                if (isValidTrajectory(mid, angle, thrustDuration, targetX, minHeight)) {
                    return new LaunchSolution(mid, angle);
                }

                double x = getDistance(angle, mid, thrustDuration, minHeight);

                if (x < targetX) {
                    low = mid;
                } else {
                    high = mid;
                }
            }
        }
        return null;
    }
}
