package net.woukie.createmissiles.missilemanager.parts.warheads;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.woukie.createmissiles.CreateMissiles;
import net.woukie.createmissiles.client.MissilePartModel;
import net.woukie.createmissiles.client.models.warheads.TunnelerWarheadModel;
import org.jetbrains.annotations.NotNull;

public class TunnelerWarhead extends ExcavatorWarhead {
    private final MissilePartModel model = new TunnelerWarheadModel();

    @Override
    public float getMass() {
        return 11;
    }

    @Override
    public @NotNull MissilePartModel getModel() {
        return model;
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
