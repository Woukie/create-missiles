package net.woukie.createmissiles.missilemanager.parts;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class Thruster extends MissilePart {
    public final float thrust;
    public final float burnRate;

    public Thruster(Component displayName, ResourceLocation resourceLocation, float thrust, float burnRate) {
        super(displayName, resourceLocation);
        this.thrust = thrust;
        this.burnRate = burnRate;
    }
}