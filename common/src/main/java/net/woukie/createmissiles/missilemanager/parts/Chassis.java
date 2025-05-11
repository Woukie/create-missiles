package net.woukie.createmissiles.missilemanager.parts;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class Chassis extends MissilePart {
    public final float fuelCapacity;

    public Chassis(Component displayName, ResourceLocation resourceLocation, float fuelCapacity) {
        super(displayName, resourceLocation);
        this.fuelCapacity = fuelCapacity;
    }
}