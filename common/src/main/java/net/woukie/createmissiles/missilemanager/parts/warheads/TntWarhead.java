package net.woukie.createmissiles.missilemanager.parts.warheads;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.woukie.createmissiles.CreateMissiles;
import net.woukie.createmissiles.missilemanager.parts.Warhead;

public class TntWarhead implements Warhead {
    public static Component getDisplayName() {
        return Component.translatable("warheads.createmissiles.tnt_warhead");
    }

    public static ResourceLocation getResourceLocationStatic() {
        return new ResourceLocation(CreateMissiles.MOD_ID, "tnt_missile");
    }

    @Override
    public float getWeight() {
        return 5.0F;
    }

    @Override
    public Detonatable getDetonatable() {
        return trajectory -> {
            Vec3 positon = trajectory.getPosition();
            trajectory.level.explode(null, positon.x, positon.y, positon.z, 3, Level.ExplosionInteraction.TNT);
        };
    }
}
