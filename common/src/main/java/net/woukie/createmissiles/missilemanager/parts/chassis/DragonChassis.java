package net.woukie.createmissiles.missilemanager.parts.chassis;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.woukie.createmissiles.CreateMissiles;
import net.woukie.createmissiles.client.MissilePartModel;
import net.woukie.createmissiles.client.models.chassis.DragonChassisModel;
import net.woukie.createmissiles.client.models.chassis.FireworkChassisModel;
import net.woukie.createmissiles.missilemanager.parts.ChassisType;
import org.jetbrains.annotations.NotNull;

public class DragonChassis extends ChassisType {
    MissilePartModel model = new DragonChassisModel();

    @Override
    public float getFuelCapacity() {
        return 40;
    }

    @Override
    public float getMass() {
        return 10;
    }

    @Override
    public @NotNull MissilePartModel getModel() {
        return model;
    }

    @Override
    public ResourceLocation getResourceLocation() {
        return new ResourceLocation(CreateMissiles.MOD_ID, "dragon_chassis");
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("chassis.createmissiles.dragon_chassis");
    }
}
