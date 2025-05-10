package net.woukie.createmissiles.missilemanager.parts.thrusters;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.woukie.createmissiles.CreateMissiles;
import net.woukie.createmissiles.missilemanager.parts.Thruster;

public class GunpowderThruster implements Thruster {
    public static Component getDisplayName() {
        return Component.translatable("thrusters.createmissiles.gunpowder_thruster");
    }

    @Override
    public ResourceLocation getResourceLocation() {
        return new ResourceLocation(CreateMissiles.MOD_ID, "tnt_missile");
    }

    @Override
    public float getThrust() {
        return 0;
    }

    @Override
    public float getBurnRate() {
        return 0;
    }
}
