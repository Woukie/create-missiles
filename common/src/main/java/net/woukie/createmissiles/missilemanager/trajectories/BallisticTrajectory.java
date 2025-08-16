package net.woukie.createmissiles.missilemanager.trajectories;

import net.fabricmc.loader.impl.lib.sat4j.core.Vec;
import net.minecraft.client.particle.SuspendedParticle;
import net.minecraft.core.Rotations;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.Container;
import net.minecraft.world.level.Level;
import net.woukie.createmissiles.entity.MissileEntity;
import net.woukie.createmissiles.missilemanager.Trajectory;
import net.woukie.createmissiles.missilemanager.parts.ChassisType;
import net.woukie.createmissiles.missilemanager.parts.ThrusterType;
import net.woukie.createmissiles.missilemanager.parts.WarheadType;
import org.joml.Vector2d;
import org.joml.Vector3d;

import static net.woukie.createmissiles.missilemanager.trajectories.TrajectoryHelper.findLaunchSolution;

// Salad notes
// Can track as many variables as you like so long as you implement serialization
// This class can be butchered, you might want to work in local space instead and then convert to global in getPosition() if it makes the maths easier
public class BallisticTrajectory extends Trajectory {
    public final Vector3d gravity = new Vector3d(0, -9.81, 0);
    public final double tickSpeed = 20;
    protected Vector3d globalPosition;
    protected Vector3d velocity;
    protected Vector3d acceleration;
    protected Vector3d rotation;

    private double initialThrust = 5;
    private double launchAngle = 90;
    private Vector3d launchDirection;


    @Override
    public void tick(MinecraftServer server) {
        super.tick(server);

        double tickLength = 0.05d;
        double elapsedTime = this.tick * tickLength;
//
//        double thrust = thrusterType.getThrust();
//        double weight = warheadType.getWeight() + chassisType.getWeight() + thrusterType.getWeight();
//        double fuel = chassisType.getFuelCapacity();
//        double timeSinceLastTick = 1.0f / tickSpeed;

        double forceHorizontal = initialThrust * Math.cos(Math.toRadians(launchAngle));

        acceleration = elapsedTime >= 2.5d ?
                new Vector3d(0, gravity.y, 0) :
                new Vector3d(
                        launchDirection.x * forceHorizontal,
                        initialThrust * Math.sin(Math.toRadians(launchAngle)) + gravity.y,
                        launchDirection.z * forceHorizontal
                );

        velocity.add(acceleration.mul(tickLength));
        globalPosition.add(velocity.x * tickLength, velocity.y * tickLength, velocity.z * tickLength);
    }

//    Called when launching a missile from the console panel
    public BallisticTrajectory(Level level, Vector3d start, Vector3d target, WarheadType warheadType, ChassisType chassisType, ThrusterType thrusterType, Container container) {
        super(level.dimension(), start, target, warheadType, chassisType, thrusterType, container);
        globalPosition = start;
        rotation = new Vector3d(0, 0, 0);
        velocity = new Vector3d(0, 0, 0);

        double targetDistance = Vector3d.distance(target.x, 0, target.z, start.x, 0, start.z);

        double thrustDuration = 2.5d;
        double minHeight = 50;
        launchDirection = new Vector3d(target.x - start.x, 0, target.z - start.z).normalize();

        TrajectoryHelper.LaunchSolution solution = findLaunchSolution(targetDistance, thrustDuration, minHeight, 80, 90);
        launchAngle = solution.angle;
        initialThrust = solution.thrust * 1.46d; //WTFFFFFFFFFFFFFF

        System.out.println(targetDistance);
        System.out.println(launchAngle);
        System.out.println(initialThrust);
    }

    //    Called when deserialising trajectories
    public BallisticTrajectory(CompoundTag data, MinecraftServer server) {
        super(data, server);
        this.globalPosition = new Vector3d(data.getDouble("PositionX"), data.getDouble("PositionY"), data.getDouble("PositionZ"));
        this.rotation = new Vector3d(data.getDouble("RotationX"), data.getDouble("RotationY"), data.getDouble("RotationZ"));
        this.velocity = new Vector3d(data.getDouble("VelocityX"), data.getDouble("VelocityY"), data.getDouble("VelocityZ"));
    }

//    Called when serializing a trajectory when exiting the world
    @Override
    public CompoundTag saveTo(CompoundTag data) {
        CompoundTag superData = super.saveTo(data);
        superData.putDouble("PositionX", globalPosition.x);
        superData.putDouble("PositionY", globalPosition.y);
        superData.putDouble("PositionZ", globalPosition.z);
        superData.putDouble("RotationX", rotation.x);
        superData.putDouble("RotationY", rotation.y);
        superData.putDouble("RotationZ", rotation.z);
        superData.putDouble("VelocityX", velocity.x);
        superData.putDouble("VelocityY", velocity.y);
        superData.putDouble("VelocityZ", velocity.z);
        return superData;
    }

    @Override
    public Vector3d getPosition() {
        return globalPosition;
    }

    @Override
    public void updateEntityModel(MissileEntity entity) {
        super.updateEntityModel(entity);
        if (entity == null) return;
        entity.setRotation(new Rotations((float) rotation.x, (float) rotation.y, (float) rotation.z));
    }
}
