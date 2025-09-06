package net.woukie.createmissiles.missilemanager.parts.warheads;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.phys.Vec3;
import net.woukie.createmissiles.CreateMissiles;
import net.woukie.createmissiles.client.MissilePartModel;
import net.woukie.createmissiles.client.models.warheads.InfernalWarheadModel;
import net.woukie.createmissiles.entity.InfernalballEntity;
import net.woukie.createmissiles.missilemanager.Trajectory;
import net.woukie.createmissiles.missilemanager.asyncexplosionhandler.Explosion;
import net.woukie.createmissiles.missilemanager.asyncexplosionhandler.ExplosionHandler;
import net.woukie.createmissiles.missilemanager.parts.WarheadType;
import net.woukie.createmissiles.registry.EntityTypes;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class InfernalWarhead extends WarheadType {
    private final MissilePartModel model = new InfernalWarheadModel();
    private static final float fireballVelocity = 3;
    private static final float fireballSlowVelocity = 1.5f;

    @Override
    public float getWeight() {
        return 20;
    }

    @Override
    public void onDetonate(Vec3 hitPosition, Trajectory trajectory, MinecraftServer server) {
        var level = server.getLevel(trajectory.getLevelKey());
        if (level == null) return;
        ExplosionHandler.get().createExplosion(new Explosion(level, hitPosition, 25));

        var random = new Random();
        for (int i = 0; i < 30; i++) {
            InfernalballEntity fireball = new InfernalballEntity(EntityTypes.INFERNALBALL.get(), level);
            fireball.setNoGravity(false);
            fireball.setPos(hitPosition.add(0, 0.6, 0));
            var velocity = fireballVelocity;
            if (i >= 20) velocity = fireballSlowVelocity;
            fireball.setDeltaMovement(
                   random.nextDouble() * velocity - velocity / 2,
                   random.nextDouble() * velocity,
                    random.nextDouble() * velocity - velocity / 2
            );
            level.addFreshEntity(fireball);
        }

        level.addParticle(ParticleTypes.LAVA, hitPosition.x, hitPosition.y, hitPosition.z, random.nextGaussian() * 0.05, 0.005, random.nextGaussian() * 0.05);
    }

    @Override
    public @NotNull MissilePartModel getModel() {
        return model;
    }

    @Override
    public ResourceLocation getResourceLocation() {
        return new ResourceLocation(CreateMissiles.MOD_ID, "infernal_warhead");
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("warheads.createmissiles.infernal_warhead");
    }
}
