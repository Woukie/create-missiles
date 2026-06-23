package net.woukie.createmissiles.missiles.parts;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.phys.Vec3;
import net.woukie.createmissiles.missiles.Trajectory;

public abstract class MissilePartType {
    public abstract int getStartSlot();
    public abstract int getEndSlot();
    public abstract ResourceLocation getResourceLocation();
    public abstract Component getDisplayName();

    public void onTick(Trajectory trajectory, MinecraftServer server) {

    }

    public void onLaunch(Trajectory trajectory, ServerLevel level) {

    }

    public void onDetonate(Vec3 hitPosition, Trajectory trajectory, MinecraftServer server) {

    }

    public CompoundTag saveTo(Container container, CompoundTag data) {
        return data;
    }

    public float getMass() {
        return 1;
    }

    public void registerJEIStats(MutableComponent component) {
        component.append(Component.translatable("description.jei.createmissiles.generic.mass", Float.toString(getMass())));
        component.append("\n");
    }
}
