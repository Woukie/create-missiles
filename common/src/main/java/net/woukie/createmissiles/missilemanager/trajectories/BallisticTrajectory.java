package net.woukie.createmissiles.missilemanager.trajectories;

import net.minecraft.core.Rotations;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.Container;
import net.minecraft.world.level.Level;
import net.woukie.createmissiles.block.navigationpanel.NavigationPanelBlockEntity;
import net.woukie.createmissiles.entity.MissileEntity;
import net.woukie.createmissiles.missilemanager.Trajectory;
import net.woukie.createmissiles.missilemanager.parts.ChassisType;
import net.woukie.createmissiles.missilemanager.parts.ThrusterType;
import net.woukie.createmissiles.missilemanager.parts.WarheadType;
import org.joml.*;

import java.lang.Math;

import static net.woukie.createmissiles.missilemanager.trajectories.TrajectoryHelper.findLaunchAngle;
import static net.woukie.createmissiles.missilemanager.trajectories.TrajectoryHelper.findMinLaunchSolution;

public class BallisticTrajectory extends Trajectory {
    public final Vector3d gravity = new Vector3d(0, -9.81, 0);
    public final double tickSpeed = 20;
    protected Vector3d velocity;
    protected Vector3d acceleration;
    protected Vector3d rotation;

    private double thrust = 5;
    private double thrustDuration = 2.5d;
    private double launchAngle = 90;
    private double mass = 1;
    private Vector3d launchDirection;


    @Override
    public void tick(MinecraftServer server) {
        super.tick(server);

        double tickLength = 1 / tickSpeed;
        double elapsedTime = this.tick * tickLength;

        double forceHorizontal = thrust * Math.cos(Math.toRadians(launchAngle)) / mass;

        if(launchDirection == null)
        {
            launchDirection = new Vector3d(0, 0, 0);
        }

        acceleration = elapsedTime >= thrustDuration ?
                new Vector3d(0, gravity.y, 0) :
                new Vector3d(
                        launchDirection.x * forceHorizontal,
                        thrust * Math.sin(Math.toRadians(launchAngle)) / mass + gravity.y,
                        launchDirection.z * forceHorizontal
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

        double targetDistance = Vector3d.distance(target.x, 0, target.z, start.x, 0, start.z);
        double minHeight = 50;
        launchDirection = new Vector3d(target.x - start.x, 0, target.z - start.z).normalize();

        thrust = thrusterType.getThrust();
        mass = warheadType.getMass() + chassisType.getMass() + thrusterType.getMass();
        thrustDuration = (chassisType.getFuelCapacity() / thrusterType.getBurnRate()) * navPanel.getThrustDurationPercent();
        launchAngle = findLaunchAngle(targetDistance, thrust, thrustDuration, minHeight, 45, 90, mass);

        System.out.println(thrustDuration);
        System.out.println(launchAngle);
    }

    //    Called when deserialising trajectories
    public BallisticTrajectory(CompoundTag data, MinecraftServer server) {
        super(data, server);
        this.rotation = new Vector3d(data.getDouble("RotationX"), data.getDouble("RotationY"), data.getDouble("RotationZ"));
        this.velocity = new Vector3d(data.getDouble("VelocityX"), data.getDouble("VelocityY"), data.getDouble("VelocityZ"));
    }

    //    Called when serializing a trajectory when exiting the world
    @Override
    public CompoundTag saveTo(CompoundTag data) {
        CompoundTag superData = super.saveTo(data);
        superData.putDouble("RotationX", rotation.x);
        superData.putDouble("RotationY", rotation.y);
        superData.putDouble("RotationZ", rotation.z);
        superData.putDouble("VelocityX", velocity.x);
        superData.putDouble("VelocityY", velocity.y);
        superData.putDouble("VelocityZ", velocity.z);
        return superData;
    }

    @Override
    public void updateEntityModel(MissileEntity entity) {
        super.updateEntityModel(entity);
        if (entity == null) return;
        entity.setRotation(new Rotations((float) rotation.x, (float) rotation.y, (float) rotation.z));
    }
}