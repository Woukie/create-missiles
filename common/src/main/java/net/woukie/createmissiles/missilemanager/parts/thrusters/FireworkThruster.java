package net.woukie.createmissiles.missilemanager.parts.thrusters;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.woukie.createmissiles.CreateMissiles;
import net.woukie.createmissiles.client.MissilePartModel;
import net.woukie.createmissiles.client.models.thrusters.FireworkThrusterModel;
import net.woukie.createmissiles.missilemanager.parts.ThrusterType;
import org.jetbrains.annotations.NotNull;

public class FireworkThruster extends ThrusterType {
    MissilePartModel model = new FireworkThrusterModel();

    @Override
    public float getThrust() {
        return 10;
    }

    @Override
    public float getBurnRate() {
        return 1;
    }

    @Override
    public @NotNull MissilePartModel getModel() {
        return model;
    }

    @Override
    public ResourceLocation getResourceLocation() {
        return new ResourceLocation(CreateMissiles.MOD_ID, "firework_thruster");
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("thrusters.createmissiles.firework_thruster");
    }
}
