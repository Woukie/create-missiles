package net.woukie.createmissiles.missilemanager.parts;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.woukie.createmissiles.client.MissilePartModel;
import net.woukie.createmissiles.missilemanager.Trajectory;

import javax.annotation.Nullable;

public class WarheadType extends MissilePartType {
    public final float weight;
    public final Detonatable detonatable;

    @Override
    public int getStartSlot() {
        return 0;
    }

    @Override
    public int getEndSlot() {
        return 32;
    }

    public WarheadType(Component displayName, ResourceLocation resourceLocation, @Nullable WriteData writeData, float weight, @Nullable Detonatable detonatable, MissilePartModel model) {
        super(displayName, resourceLocation, writeData, model);
        this.weight = weight;
        this.detonatable = detonatable;
    }

    public interface Detonatable {
        void detonate(Trajectory trajectory, MinecraftServer server);
    }
}
