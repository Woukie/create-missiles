package net.woukie.createmissiles.missilemanager.parts.chassis;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.woukie.createmissiles.CreateMissiles;
import net.woukie.createmissiles.client.MissilePartModel;
import net.woukie.createmissiles.client.models.chassis.AncientChassisModel;
import net.woukie.createmissiles.missilemanager.parts.ChassisType;
import org.jetbrains.annotations.NotNull;

public class AncientChassis extends ChassisType {
    MissilePartModel model = new AncientChassisModel();

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
        return new ResourceLocation(CreateMissiles.MOD_ID, "ancient_chassis");
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("chassis.createmissiles.ancient_chassis");
    }
}
