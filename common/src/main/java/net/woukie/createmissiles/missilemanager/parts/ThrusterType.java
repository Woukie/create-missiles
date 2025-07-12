package net.woukie.createmissiles.missilemanager.parts;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.woukie.createmissiles.client.MissilePartModel;
import net.woukie.createmissiles.missilemanager.Trajectory;
import net.woukie.createmissiles.missilemanager.TrajectoryData;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Supplier;

public abstract class ThrusterType extends MissilePartType {
    @Override
    public int getStartSlot() {
        return 64;
    }

    @Override
    public int getEndSlot() {
        return 96;
    }

    public abstract Trajectory createTrajectory(TrajectoryData data);

    public abstract float getThrust();
    public abstract float getBurnRate();
}