package net.woukie.createmissiles.missilemanager.parts;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.woukie.createmissiles.client.MissilePartModel;

import javax.annotation.Nullable;
import java.util.List;

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

    public ThrusterType(Component displayName, ResourceLocation resourceLocation, @Nullable WriteData writeData, float thrust, float burnRate, MissilePartModel model) {
        super(displayName, resourceLocation, writeData, model);
        this.thrust = thrust;
        this.burnRate = burnRate;
    }
}