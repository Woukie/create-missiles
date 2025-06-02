package net.woukie.createmissiles.missilemanager.parts;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nullable;
import java.util.List;

public class ThrusterType extends MissilePartType {
    public final float thrust;
    public final float burnRate;
    public final int startSlot = 64;
    public final int endSlot = 96;

    public ThrusterType(Component displayName, ResourceLocation resourceLocation, @Nullable WriteData writeData, float thrust, float burnRate) {
        super(displayName, resourceLocation, writeData);
        this.thrust = thrust;
        this.burnRate = burnRate;
    }
}