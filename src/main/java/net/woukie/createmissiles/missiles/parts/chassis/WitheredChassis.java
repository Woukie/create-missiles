package net.woukie.createmissiles.missiles.parts.chassis;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.woukie.createmissiles.CreateMissiles;
import net.woukie.createmissiles.missiles.parts.ChassisType;

public class WitheredChassis extends ChassisType {
    @Override
    public float getFuelCapacity() {
        return 80f;
    }

    @Override
    public float getMass() {
        return 40f;
    }

    @Override
    public ResourceLocation getResourceLocation() {
        return new ResourceLocation(CreateMissiles.MOD_ID, "withered_chassis");
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("chassis.createmissiles.withered_chassis");
    }
}
