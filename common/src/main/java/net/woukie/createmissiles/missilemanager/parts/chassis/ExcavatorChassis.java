package net.woukie.createmissiles.missilemanager.parts.chassis;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.woukie.createmissiles.CreateMissiles;
import net.woukie.createmissiles.client.MissilePartModel;
import net.woukie.createmissiles.client.models.chassis.ExcavatorChassisModel;
import net.woukie.createmissiles.client.models.chassis.FireworkChassisModel;
import net.woukie.createmissiles.missilemanager.parts.ChassisType;
import org.jetbrains.annotations.NotNull;

public class ExcavatorChassis extends ChassisType {
    MissilePartModel model = new ExcavatorChassisModel();

    @Override
    public float getFuelCapacity() {
        return 45;
    }

    @Override
    public float getMass() {
        return 60;
    }

    @Override
    public @NotNull MissilePartModel getModel() {
        return model;
    }

    @Override
    public ResourceLocation getResourceLocation() {
        return new ResourceLocation(CreateMissiles.MOD_ID, "excavator_chassis");
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("chassis.createmissiles.excavator_chassis");
    }
}
