package net.woukie.createmissiles.missilemanager.parts;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.woukie.createmissiles.client.MissilePartModel;

import javax.annotation.Nullable;
import java.util.List;

public abstract class ChassisType extends MissilePartType {
    @Override
    public int getStartSlot() {
        return 32;
    }

    @Override
    public int getEndSlot() {
        return 64;
    }

    public abstract float getFuelCapacity();
}