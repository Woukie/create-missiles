package net.woukie.createmissiles.missilemanager.trajectories;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.Container;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.woukie.createmissiles.entity.MissileEntity;
import net.woukie.createmissiles.missilemanager.Trajectory;
import net.woukie.createmissiles.missilemanager.parts.ChassisType;
import net.woukie.createmissiles.missilemanager.parts.ThrusterType;
import net.woukie.createmissiles.missilemanager.parts.WarheadType;
import org.joml.Vector3d;

public class BallisticTrajectory extends Trajectory {
    public BallisticTrajectory(Level level, Vector3d start, Vector3d target, WarheadType warheadType, ChassisType chassisType, ThrusterType thrusterType, Container container) {
        super(level, start, target, warheadType, chassisType, thrusterType, container);
    }

    public BallisticTrajectory(CompoundTag data, MinecraftServer server) {
        loadFrom(data, server);
    }

    @Override
    public Vec3 getPosition() {
        return null;
    }

    @Override
    public void updateEntityModel(MissileEntity entity) {

    }

    @Override
    public boolean shouldRemove() {
        return false;
    }
}
