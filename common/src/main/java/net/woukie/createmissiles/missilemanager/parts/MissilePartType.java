package net.woukie.createmissiles.missilemanager.parts;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.level.Level;
import net.woukie.createmissiles.client.MissilePartModel;
import net.woukie.createmissiles.missilemanager.Trajectory;
import org.jetbrains.annotations.NotNull;

public abstract class MissilePartType {
    public abstract int getStartSlot();
    public abstract int getEndSlot();
    public @NotNull abstract MissilePartModel getModel();
    public abstract ResourceLocation getResourceLocation();
    public abstract Component getDisplayName();

    public void onTick(Trajectory trajectory, MinecraftServer server) {

    }

    public void onLaunch(Trajectory trajectory) {

    }

    public void onDetonate(Trajectory trajectory, MinecraftServer server) {
        var level = (ServerLevel) trajectory.getData().level;
        if (level != null) {
            var impactPos = trajectory.getPosition((float)trajectory.getImpactTime());
            level.explode(null, impactPos.x, impactPos.y, impactPos.z, 5, Level.ExplosionInteraction.BLOCK);
        }
    }

    public CompoundTag writeData(Container container, CompoundTag data) {
        return data;
    }
}
