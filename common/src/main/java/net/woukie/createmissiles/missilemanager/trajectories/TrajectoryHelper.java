package net.woukie.createmissiles.missilemanager.trajectories;

public class TrajectoryHelper {
    private static final double gravity = 9.81;
    private static final double mass = 1.0;
    private static final double deltaTime = 0.01;

    public static class LaunchSolution {
        public final double thrust;
        public final int angle;

        public LaunchSolution(double thrust, int angle) {
            this.thrust = thrust;
            this.angle = angle;
        }
    }

    private static boolean isValidTrajectory(double thrust, int angleDeg, double thrustDuration, double targetX, double minHeight) {
        double angleRad = Math.toRadians(angleDeg);
        double vx = thrust * Math.cos(angleRad) / mass;
        double vy = thrust * Math.sin(angleRad) / mass;

        double x = 0;
        double y = 2;
        double time = 0;
        double maxHeight = y;

        while (y >= 0 && time <= 30) {
            double ax = (time > thrustDuration) ? 0 : thrust * Math.cos(angleRad) / mass;
            double ay = (time > thrustDuration)
                    ? -gravity
                    : thrust * Math.sin(angleRad) / mass - gravity;

            vx += ax * deltaTime;
            vy += ay * deltaTime;
            x += vx * deltaTime;
            y += vy * deltaTime;
            maxHeight = Math.max(maxHeight, y);
            time += deltaTime;
        }

        return Math.abs(x - targetX) <= 1.0 && maxHeight >= minHeight;
    }

    public static LaunchSolution findLaunchSolution(double targetX, double thrustDuration, double minHeight, int angleStart, int angleEnd) {
        for (int angle = angleStart; angle <= angleEnd; angle++) {
            double low = 0;
            double high = 300;

            for (int i = 0; i < 20; i++) {
                double mid = (low + high) / 2;
                if (isValidTrajectory(mid, angle, thrustDuration, targetX, minHeight)) {
                    return new LaunchSolution(mid, angle);
                }

                // Guide binary search
                double angleRad = Math.toRadians(angle);
                double vx = mid * Math.cos(angleRad) / mass;
                double vy = mid * Math.sin(angleRad) / mass;
                double x = 0;
                double y = 0;
                double time = 0;

                while (y >= 0 && time <= 30) {
                    double ax = (time > thrustDuration) ? 0 : mid * Math.cos(angleRad) / mass;
                    double ay = (time > thrustDuration)
                            ? -gravity
                            : mid * Math.sin(angleRad) / mass - gravity;

                    vx += ax * deltaTime;
                    vy += ay * deltaTime;
                    x += vx * deltaTime;
                    y += vy * deltaTime;
                    time += deltaTime;
                }

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
