package net.woukie.createmissiles.missilemanager.parts;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.Container;
import net.woukie.createmissiles.client.MissilePartModel;
import net.woukie.createmissiles.missilemanager.Trajectory;
import org.jetbrains.annotations.NotNull;

public abstract class MissilePartType {
    public abstract int getStartSlot();
    public abstract int getEndSlot();
    public @NotNull abstract MissilePartModel getModel();
    public abstract ResourceLocation getResourceLocation();
    public abstract Component getDisplayName();
    public CompoundTag warheadData;

    public void onTick(Trajectory trajectory, MinecraftServer server) {

    }

    public void onLaunch(Trajectory trajectory) {

    }

    public void onDetonate(Trajectory trajectory, MinecraftServer server) {

    }

    public CompoundTag saveTo(Container container, CompoundTag data) {
        return data;
    }

    public float getWeight() {
        return 1;
    }
}
