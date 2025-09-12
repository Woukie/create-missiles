package net.woukie.createmissiles.missiles.parts.warheads;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.woukie.createmissiles.CreateMissiles;

public class TunnelerWarhead extends ExcavatorWarhead {
    @Override
    public float getMass() {
        return 28;
    }

    @Override
    public ResourceLocation getResourceLocation() {
        return new ResourceLocation(CreateMissiles.MOD_ID, "tunneler_warhead");
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("warheads.createmissiles.tunneler_warhead");
    }

    @Override
    protected double getExplosionPower() {
        return 8;
    }

    @Override
    protected float getStepSize() {
        return 2F;
    }

    @Override
    protected int getInitialCharges() {
        return 60;
    }
}
