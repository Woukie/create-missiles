package net.woukie.createmissiles.missilemanager.parts;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.Container;
import net.minecraft.world.level.Level;
import net.woukie.createmissiles.missilemanager.Trajectory;
import org.joml.Vector3d;

import java.util.List;

public abstract class ThrusterType extends MissilePartType {
    @Override
    public int getStartSlot() {
        return 64;
    }

    @Override
    public int getEndSlot() {
        return 96;
    }

    public abstract Trajectory serializeTrajectory(CompoundTag data, MinecraftServer server);

    public abstract Trajectory createTrajectory(Level level, Vector3d start, Vector3d target, WarheadType warheadType, ChassisType chassisType, ThrusterType thrusterType, Container container);

    public abstract float getThrust();
    public abstract float getBurnRate();
}