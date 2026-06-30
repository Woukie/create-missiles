package net.woukie.createmissiles.missiles.parts.chassis;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.woukie.createmissiles.CreateMissiles;
import net.woukie.createmissiles.missiles.parts.ChassisType;

public class FireworkChassis extends ChassisType {
    @Override
    public float getFuelCapacity() {
        return 15;
    }

    @Override
    public float getMass() {
        return 10;
    }

    @Override
    public ResourceLocation getResourceLocation() {
        return ResourceLocation.fromNamespaceAndPath(CreateMissiles.MOD_ID, "firework_chassis");
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("chassis.createmissiles.firework_chassis");
    }
}
