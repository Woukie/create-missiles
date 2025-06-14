package net.woukie.createmissiles.missilemanager.parts;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.woukie.createmissiles.client.MissilePartModel;

import javax.annotation.Nullable;
import java.util.List;

public class ChassisType extends MissilePartType {
    public final float fuelCapacity;

    @Override
    public int getStartSlot() {
        return 32;
    }

    @Override
    public int getEndSlot() {
        return 64;
    }

    public ChassisType(Component displayName, ResourceLocation resourceLocation, @Nullable WriteData writeData, float fuelCapacity, List<MissilePartModel> models) {
        super(displayName, resourceLocation, writeData, models);
        this.fuelCapacity = fuelCapacity;
    }
}