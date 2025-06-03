package net.woukie.createmissiles.missilemanager.parts;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nullable;

public class ThrusterType extends MissilePartType {
    public final float thrust;
    public final float burnRate;

    @Override
    public int getStartSlot() {
        return 64;
    }

    @Override
    public int getEndSlot() {
        return 96;
    }

    public ThrusterType(Component displayName, ResourceLocation resourceLocation, @Nullable WriteData writeData, float thrust, float burnRate) {
        super(displayName, resourceLocation, writeData);
        this.thrust = thrust;
        this.burnRate = burnRate;
    }
}