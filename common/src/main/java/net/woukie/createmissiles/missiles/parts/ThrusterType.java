package net.woukie.createmissiles.missiles.parts;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.Container;
import net.minecraft.world.level.Level;
import net.woukie.createmissiles.block.navigationpanel.NavigationPanelBlockEntity;
import net.woukie.createmissiles.missiles.Trajectory;
import org.joml.Vector3d;

public abstract class ThrusterType extends MissilePartType {
    @Override
    public int getStartSlot() {
        return 64;
    }

    @Override
    public int getEndSlot() {
        return 96;
    }

    public abstract Trajectory constructTrajectory(CompoundTag data, MinecraftServer server);

    public abstract Trajectory createTrajectory(Level level, Vector3d start, Vector3d target, WarheadType warheadType, ChassisType chassisType, ThrusterType thrusterType, Container container, NavigationPanelBlockEntity navPanel);

    public abstract float getThrust();
    public abstract float getBurnRate();

    @Override
    public void registerJEIStats(MutableComponent component) {
        super.registerJEIStats(component);
        component.append(Component.translatable("description.jei.createmissiles.generic.thrust", Float.toString(getThrust())));
        component.append("\n");
        component.append(Component.translatable("description.jei.createmissiles.generic.burn_rate", Float.toString(getBurnRate())));
        component.append("\n");
    }
}