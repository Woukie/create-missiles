package net.woukie.createmissiles.missiles.trajectories;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Rotations;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.Container;
import net.minecraft.world.level.Level;
import net.woukie.createmissiles.block.navigationpanel.NavigationPanelBlockEntity;
import net.woukie.createmissiles.entity.MissileEntity;
import net.woukie.createmissiles.missiles.Trajectory;
import net.woukie.createmissiles.missiles.parts.ChassisType;
import net.woukie.createmissiles.missiles.parts.ThrusterType;
import net.woukie.createmissiles.missiles.parts.WarheadType;
import org.joml.*;

import java.lang.Math;

import static net.woukie.createmissiles.missiles.trajectories.TrajectoryHelper.findLaunchAngle;

public class BallisticTrajectory extends Trajectory {
    public final Vector3d gravity = new Vector3d(0, -9.81, 0);
    public final double tickSpeed = 20;
    protected Vector3d velocity;
    protected Vector3d acceleration;
    protected Vector3d rotation;

    private double thrust;
    private double thrustDuration;
    private double launchAngle;
    private double mass;
    private Vector3d launchDirection;

    @Override
    public void tick(MinecraftServer server) {
        super.tick(server);

        double tickLength = 1 / tickSpeed;
        double elapsedTime = this.tick * tickLength;

        if (launchDirection == null)
        {
            launchDirection = new Vector3d(0, 0, 0);
        }

        double currentFlightAngle = Math.atan2(velocity.y, Math.sqrt(velocity.x * velocity.x + velocity.z * velocity.z));
        double approximateForceDueToAirResistance = 0.03d * (velocity.x * velocity.x + velocity.y * velocity.y + velocity.z * velocity.z); //0.031
        double horizontalForceDueToAirResistance = Math.cos(currentFlightAngle) * approximateForceDueToAirResistance;
        double verticalForceDueToAirResistance = -getSign(velocity.y) * Math.sin(currentFlightAngle) * approximateForceDueToAirResistance;
        double forceHorizontal = thrust * Math.cos(Math.toRadians(launchAngle)) / mass;

        acceleration = elapsedTime >= thrustDuration ?
                new Vector3d(
                        -getSign(velocity.x) * horizontalForceDueToAirResistance * launchDirection.x / mass,
                        verticalForceDueToAirResistance / mass + gravity.y,
                        -getSign(velocity.z) * horizontalForceDueToAirResistance * launchDirection.z / mass
                ) :
                new Vector3d(
                        (-getSign(velocity.x) * horizontalForceDueToAirResistance * launchDirection.x / mass) + (launchDirection.x * forceHorizontal),
                        (verticalForceDueToAirResistance / mass) + (thrust * Math.sin(Math.toRadians(launchAngle)) / mass) + gravity.y,
                        (-getSign(velocity.z) * horizontalForceDueToAirResistance * launchDirection.z / mass) + (launchDirection.z * forceHorizontal)
                );

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

    //    Called when launching a missile from the console panel
    public BallisticTrajectory(Level level, Vector3d start, Vector3d target, WarheadType warheadType, ChassisType chassisType, ThrusterType thrusterType, Container container, NavigationPanelBlockEntity navPanel) {
        super(level.dimension(), start, target, warheadType, chassisType, thrusterType, container);
        rotation = new Vector3d(0, 0, 0);
        velocity = new Vector3d(0, 0, 0);

        launchDirection = new Vector3d(target.x - start.x, 0, target.z - start.z).normalize();

        TrajectoryHelper.MissileConfig missileConfig = new TrajectoryHelper.MissileConfig(thrusterType, chassisType, warheadType);
        double[] launchAngleRange = {0, 90};
        TrajectoryHelper.LaunchConfig launchConfig = new TrajectoryHelper.LaunchConfig(
                new BlockPos((int) start.x, (int) start.y, (int) start.z),
                new BlockPos((int) target.x, (int) target.y, (int) target.z),
                launchAngleRange,
                (chassisType.getFuelCapacity() / thrusterType.getBurnRate()) * navPanel.getThrustDurationPercent(),
                missileConfig
        );
        thrustDuration = launchConfig.selectedThrustDuration;
        launchAngle = findLaunchAngle(launchConfig, thrustDuration);
        thrust = launchConfig.missileConfig.thrust;
        System.out.println(thrust);
        System.out.println(launchAngle);
        System.out.println(thrustDuration);
    }

    //    Called when deserialising trajectories
    public BallisticTrajectory(CompoundTag data, MinecraftServer server) {
        super(data, server);
        this.velocity = new Vector3d(data.getDouble("VelocityX"), data.getDouble("VelocityY"), data.getDouble("VelocityZ"));
        this.acceleration = new Vector3d(data.getDouble("AccelerationX"), data.getDouble("AccelerationY"), data.getDouble("AccelerationZ"));
        this.rotation = new Vector3d(data.getDouble("RotationX"), data.getDouble("RotationY"), data.getDouble("RotationZ"));
        this.thrust = data.getDouble("Thrust");
        this.thrustDuration = data.getDouble("ThrustDuration");
        this.launchAngle = data.getDouble("LaunchAngle");
        this.mass = data.getDouble("Mass");
        this.launchDirection = new Vector3d(data.getDouble("LaunchDirectionX"), data.getDouble("LaunchDirectionY"), data.getDouble("LaunchDirectionZ"));
    }

    //    Called when serializing a trajectory when exiting the world
    @Override
    public CompoundTag saveTo(CompoundTag data) {
        CompoundTag superData = super.saveTo(data);
        superData.putDouble("VelocityX", velocity.x);
        superData.putDouble("VelocityY", velocity.y);
        superData.putDouble("VelocityZ", velocity.z);
        superData.putDouble("AccelerationX", acceleration.x);
        superData.putDouble("AccelerationY", acceleration.y);
        superData.putDouble("AccelerationZ", acceleration.z);
        superData.putDouble("RotationX", rotation.x);
        superData.putDouble("RotationY", rotation.y);
        superData.putDouble("RotationZ", rotation.z);
        superData.putDouble("Thrust", thrust);
        superData.putDouble("ThrustDuration", thrustDuration);
        superData.putDouble("LaunchAngle", launchAngle);
        superData.putDouble("Mass", mass);
        superData.putDouble("LaunchDirectionX", launchDirection.x);
        superData.putDouble("LaunchDirectionY", launchDirection.y);
        superData.putDouble("LaunchDirectionZ", launchDirection.z);
        return superData;
    }

    @Override
    public void updateEntityModel(MissileEntity entity) {
        super.updateEntityModel(entity);
        if (entity == null) return;
        entity.setRotation(new Rotations((float) rotation.x, (float) rotation.y, (float) rotation.z));
    }

    private int getSign(double val){
        return val >= 0 ? 1 : -1;
    }
}