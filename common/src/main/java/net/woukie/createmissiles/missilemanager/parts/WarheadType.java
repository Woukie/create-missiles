package net.woukie.createmissiles.missilemanager.parts;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.woukie.createmissiles.client.MissilePartModel;
import net.woukie.createmissiles.missilemanager.Trajectory;

import javax.annotation.Nullable;

public abstract class WarheadType extends MissilePartType {
    @Override
    public int getStartSlot() {
        return 0;
    }

    @Override
    public int getEndSlot() {
        return 32;
    }

    public abstract int getWeight();
}
