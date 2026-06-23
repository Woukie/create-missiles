package net.woukie.createmissiles.missiles.parts.chassis;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.woukie.createmissiles.CreateMissiles;
import net.woukie.createmissiles.missiles.parts.ChassisType;

public class FlamingChassis extends ChassisType {
    @Override
    public float getFuelCapacity() {
        return 25f;
    }

    @Override
    public float getMass() {
        return 20f;
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
