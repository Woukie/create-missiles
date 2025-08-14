package net.woukie.createmissiles.missilemanager.parts.chassis;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.woukie.createmissiles.CreateMissiles;
import net.woukie.createmissiles.client.MissilePartModel;
import net.woukie.createmissiles.client.models.chassis.FireworkChassisModel;
import net.woukie.createmissiles.client.models.chassis.FlamingChassisModel;
import net.woukie.createmissiles.missilemanager.parts.ChassisType;
import org.jetbrains.annotations.NotNull;

public class FlamingChassis extends ChassisType {
    MissilePartModel model = new FlamingChassisModel();

    @Override
    public float getFuelCapacity() {
        return 2;
    }

    @Override
    public @NotNull MissilePartModel getModel() {
        return model;
    }

    @Override
    public ResourceLocation getResourceLocation() {
        return new ResourceLocation(CreateMissiles.MOD_ID, "flaming_chassis");
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("chassis.createmissiles.flaming_chassis");
    }
}
