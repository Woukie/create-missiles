package net.woukie.createmissiles.missilemanager.parts;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nullable;
import java.util.List;

public class ChassisType extends MissilePartType {
    public final float fuelCapacity;
    public final int startSlot = 32;
    public final int endSlot = 64;

    public ChassisType(Component displayName, ResourceLocation resourceLocation, @Nullable WriteData writeData, float fuelCapacity) {
        super(displayName, resourceLocation, writeData);
        this.fuelCapacity = fuelCapacity;
    }
}