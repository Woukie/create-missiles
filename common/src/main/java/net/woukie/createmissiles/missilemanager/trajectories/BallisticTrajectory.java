package net.woukie.createmissiles.missilemanager.trajectories;

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
import org.joml.Vector3d;

// Salad notes
// Can track as many variables as you like so long as you implement serialization
// This class can be butchered, you might want to work in local space instead and then convert to global in getPosition() if it makes the maths easier
public class BallisticTrajectory extends Trajectory {
    public final Vector3d gravity = new Vector3d(0, -9.81, 0);
    public final double tickSpeed = 20;
    public final double turnSpeed = 0.5;

    protected Vector3d globalPosition;
    protected Vector3d velocity;
    protected Vector3d rotation;
    protected boolean spent;

    @Override
    public void tick(MinecraftServer server) {
        super.tick(server);

        float tickLength = 0.05f;
        float elapsedTime = this.tick * tickLength;

        double thrust = thrusterType.getThrust();
        double weight = warheadType.getWeight() + chassisType.getWeight() + thrusterType.getWeight();
        double fuel = chassisType.getFuelCapacity();
        double timeSinceLastTick = 1.0f / tickSpeed;


        if(elapsedTime >= 2.5f)
        {
            velocity.add(0, -9.81 * 0.01, 0);
        }
        else
        {
            velocity.add(12.16 * Math.cos(Math.toRadians(80)) * 0.01, 12.16 * 0.01 * Math.sin(Math.toRadians(80)) - 9.81 * 0.01, 0);
        }
        //thruster on
        //add velocity based on thrust and gravity

        //thruster off
        //add velocity based on gravity



        //velocity.add(0, 0.01F, 0);
        globalPosition.add(velocity);
        rotation.add(0, 0.1f, 0);
    }

    //    Called when launching a missile from the console panel
    public BallisticTrajectory(Level level, Vector3d start, Vector3d target, WarheadType warheadType, ChassisType chassisType, ThrusterType thrusterType, Container container) {
        super(level.dimension(), start, target, warheadType, chassisType, thrusterType, container);
        globalPosition = start;
        rotation = new Vector3d(0, 0, 0);
        velocity = new Vector3d(0, 0, 0);
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
