package net.woukie.createmissiles.missilemanager.parts.chassis;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.woukie.createmissiles.CreateMissiles;
import net.woukie.createmissiles.missilemanager.parts.Chassis;

public class PaperChassis implements Chassis {
    @Override
    public float getFuelCapacity() {
        return 2;
    }

    public static Component getDisplayName() {
        return Component.translatable("paper.createmissiles.paper_chassis");
    }

    @Override
    public ResourceLocation getResourceLocation() {
        return new ResourceLocation(CreateMissiles.MOD_ID, "paper_chassis");
    }
}
