package net.woukie.createmissiles.missilemanager.parts;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.woukie.createmissiles.client.MissilePartModel;

import javax.annotation.Nullable;
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

    public abstract float getThrust();
    public abstract float getBurnRate();
}