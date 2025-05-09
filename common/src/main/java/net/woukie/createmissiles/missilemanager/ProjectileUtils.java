package net.woukie.createmissiles.missilemanager;

import net.minecraft.world.phys.Vec2;

//    AI generated for the time being, going to have a realistic physically-simulated trajectory in future, need to focus on other things
public class ProjectileUtils {
    /**
     * Class representing launch information including angle and time of impact
     */
    public static class LaunchInfo {
        private final double angle;    // Launch angle in radians
        private final double time;     // Time of impact in seconds
        private final boolean canHitTarget; // Whether the target is reachable

        public LaunchInfo(double angle, double time, boolean canHitTarget) {
            this.angle = angle;
            this.time = time;
            this.canHitTarget = canHitTarget;
        }

        public double getAngle() {
            return angle;
        }

        public double getTime() {
            return time;
        }

        public boolean canHitTarget() {
            return canHitTarget;
        }
    }

    public static LaunchInfo getLaunchInfo(double targetX, double targetY, double initialVelocity, double gravity) {
        double v2 = initialVelocity * initialVelocity;
        double discriminant = v2 * v2 - gravity * (gravity * targetX * targetX + 2 * targetY * v2);

        if (discriminant >= 0) {
            double term1 = v2 / (gravity * targetX);
            double term2 = Math.sqrt(discriminant) / (gravity * targetX);
            double angle = Math.atan(term1 + term2);

            double vx = initialVelocity * Math.cos(angle);
            double time = targetX / vx;

            return new LaunchInfo(angle, time, true);
        } else {
            double angleForMaxRange = Math.PI / 4; // 45 degrees if targetY = 0
            if (targetY != 0) {
                double heightFactor = Math.atan(targetY / (initialVelocity * initialVelocity / gravity));
                angleForMaxRange += heightFactor / 2;
            }

            double vy = initialVelocity * Math.sin(angleForMaxRange);

            double a = -0.5 * gravity;
            double c = -targetY;

            double timeDiscriminant = vy * vy - 4 * a * c;
            double time;

            if (timeDiscriminant >= 0) {
                double t1 = (-vy + Math.sqrt(timeDiscriminant)) / (2 * a);
                double t2 = (-vy - Math.sqrt(timeDiscriminant)) / (2 * a);
                time = (t1 > 0 && t2 > 0) ? Math.min(t1, t2) : Math.max(t1, t2);
            } else {
                time = vy / gravity;
            }

            return new LaunchInfo(angleForMaxRange, time, false);
        }
    }

    public static Vec2 getPositionAt(double initialVelocity, double angle, double gravity, double time) {
        double vx = initialVelocity * Math.cos(angle);
        double vy = initialVelocity * Math.sin(angle);

        double x = vx * time;
        double y = vy * time - (gravity * time * time) / 2;

        return new Vec2((float)x, (float)y);
    }
}
