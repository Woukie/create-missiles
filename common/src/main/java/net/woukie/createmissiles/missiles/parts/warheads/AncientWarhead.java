package net.woukie.createmissiles.missiles.parts.warheads;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.phys.Vec3;
import net.woukie.createmissiles.CreateMissiles;
import net.woukie.createmissiles.entity.AncientballEntity;
import net.woukie.createmissiles.missiles.Trajectory;
import net.woukie.createmissiles.missiles.asyncexplosionhandler.Explosion;
import net.woukie.createmissiles.missiles.asyncexplosionhandler.ExplosionHandler;
import net.woukie.createmissiles.missiles.parts.WarheadType;
import net.woukie.createmissiles.registry.EntityTypes;

import java.util.Random;

public class AncientWarhead extends WarheadType {
    private static final float snowballVelocity = 3;
    private static final float snowballSlowVelocity = 1.5f;

    @Override
    public float getMass() {
        return 5;
    }

    @Override
    public void onDetonate(Vec3 hitPosition, Trajectory trajectory, MinecraftServer server) {
        var level = server.getLevel(trajectory.getLevelKey());
        if (level == null) return;
        level.playSound(null, BlockPos.containing(hitPosition), SoundEvents.GENERIC_EXPLODE, SoundSource.HOSTILE);
        ExplosionHandler.get().createExplosion(new Explosion(level, hitPosition, 15));

        EntityType.WARDEN.spawn(level, BlockPos.containing(hitPosition), MobSpawnType.MOB_SUMMONED);

        var random = new Random();
        for (int i = 0; i < 30; i++) {
            AncientballEntity ball = new AncientballEntity(EntityTypes.ANCIENTBALL.get(), level);
            ball.setNoGravity(false);
            ball.setPos(hitPosition.add(0, 1, 0));
            var velocity = snowballVelocity;
            if (i >= 20) velocity = snowballSlowVelocity;
            ball.setDeltaMovement(
                    random.nextDouble() * velocity - velocity / 2,
                    (random.nextDouble() / 2 + 0.5) * velocity,
                    random.nextDouble() * velocity - velocity / 2
            );
            level.addFreshEntity(ball);
        }
    }

    @Override
    public ResourceLocation getResourceLocation() {
        return new ResourceLocation(CreateMissiles.MOD_ID, "ancient_warhead");
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("warheads.createmissiles.ancient_warhead");
    }
}
