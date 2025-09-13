package net.woukie.createmissiles.missiles.parts.warheads;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import net.woukie.createmissiles.CreateMissiles;
import net.woukie.createmissiles.entity.BlazingballEntity;
import net.woukie.createmissiles.missiles.Trajectory;
import net.woukie.createmissiles.missiles.asyncexplosionhandler.Explosion;
import net.woukie.createmissiles.missiles.asyncexplosionhandler.ExplosionHandler;
import net.woukie.createmissiles.missiles.parts.WarheadType;
import net.woukie.createmissiles.registry.EntityTypes;

import java.util.Random;

public class BlazingWarhead extends WarheadType {
    private static final float fireballVelocity = 3;
    private static final float fireballSlowVelocity = 1.5f;

    @Override
    public float getMass() {
        return 20;
    }

    @Override
    public void onDetonate(Vec3 hitPosition, Trajectory trajectory, MinecraftServer server) {
        var level = server.getLevel(trajectory.getLevelKey());
        if (level == null) return;
        level.playSound(null, BlockPos.containing(hitPosition), SoundEvents.GENERIC_EXPLODE, SoundSource.HOSTILE, 10, 1);
        ExplosionHandler.get().createExplosion(new Explosion(level, hitPosition, 15));

        var random = new Random();
        for (int i = 0; i < 30; i++) {
            Entity entity = new BlazingballEntity(EntityTypes.BLAZINGBALL.get(), level);
            entity.setNoGravity(false);
            entity.setPos(hitPosition.add(0, 1.5, 0));
            var velocity = fireballVelocity;
            if (i >= 20) velocity = fireballSlowVelocity;
            entity.setDeltaMovement(
                   random.nextDouble() * velocity - velocity / 2,
                   random.nextDouble() * velocity,
                    random.nextDouble() * velocity - velocity / 2
            );
            level.addFreshEntity(entity);
        }

        level.addParticle(ParticleTypes.LAVA, hitPosition.x, hitPosition.y, hitPosition.z, random.nextGaussian() * 0.05, 0.005, random.nextGaussian() * 0.05);
    }

    @Override
    public ResourceLocation getResourceLocation() {
        return new ResourceLocation(CreateMissiles.MOD_ID, "blazing_warhead");
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("warheads.createmissiles.blazing_warhead");
    }
}
