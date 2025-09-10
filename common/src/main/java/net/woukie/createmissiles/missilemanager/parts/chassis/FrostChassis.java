package net.woukie.createmissiles.missilemanager.parts.chassis;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.woukie.createmissiles.CreateMissiles;
import net.woukie.createmissiles.client.MissilePartModel;
import net.woukie.createmissiles.client.models.chassis.AncientChassisModel;
import net.woukie.createmissiles.client.models.chassis.FrostChassisModel;
import net.woukie.createmissiles.missilemanager.parts.ChassisType;
import org.jetbrains.annotations.NotNull;

public class FrostChassis extends ChassisType {
    MissilePartModel model = new FrostChassisModel();

    @Override
    public float getFuelCapacity() {
        return 25;
    }

    @Override
    public float getMass() {
        return 35;
    }

    @Override
    public @NotNull MissilePartModel getModel() {
        return model;
    }

    @Override
    public ResourceLocation getResourceLocation() {
        return new ResourceLocation(CreateMissiles.MOD_ID, "frost_chassis");
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("chassis.createmissiles.frost_chassis");
    }
}
