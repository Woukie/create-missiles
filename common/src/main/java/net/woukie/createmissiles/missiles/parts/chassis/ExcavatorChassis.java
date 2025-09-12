package net.woukie.createmissiles.missiles.parts.chassis;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.woukie.createmissiles.CreateMissiles;
import net.woukie.createmissiles.missiles.parts.ChassisType;

public class ExcavatorChassis extends ChassisType {
    @Override
    public float getFuelCapacity() {
        return 45;
    }

    @Override
    public float getMass() {
        return 60;
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
