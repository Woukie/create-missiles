package net.woukie.createmissiles.missiles.parts.warheads;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.Vec3;
import net.woukie.createmissiles.CreateMissiles;
import net.woukie.createmissiles.Util;
import net.woukie.createmissiles.entity.GuardianballEntity;
import net.woukie.createmissiles.missiles.Trajectory;
import net.woukie.createmissiles.missiles.asyncexplosionhandler.Explosion;
import net.woukie.createmissiles.missiles.asyncexplosionhandler.ExplosionHandler;
import net.woukie.createmissiles.missiles.parts.WarheadType;
import net.woukie.createmissiles.registry.EntityTypes;

import java.util.Random;

public class GuardianWarhead extends WarheadType {
    private static final float ballVelocity = 3;
    private static final float slowBallVelocity = 1.5f;

    @Override
    public float getMass() {
        return 25;
    }

    @Override
    public void onDetonate(Vec3 hitPosition, Trajectory trajectory, MinecraftServer server) {
        var level = server.getLevel(trajectory.getLevelKey());
        if (level == null) return;
        ExplosionHandler.get().createExplosion(new Explosion(level, hitPosition, 15));

        var random = new Random();
        for (int i = 0; i < 30; i++) {
            GuardianballEntity ball = new GuardianballEntity(EntityTypes.FROSTBALL.get(), level);
            ball.setNoGravity(false);
            ball.setPos(hitPosition.add(0, 1, 0));
            var velocity = ballVelocity;
            if (i >= 20) velocity = slowBallVelocity;
            ball.setDeltaMovement(
                    random.nextDouble() * velocity - velocity / 2,
                    (random.nextDouble() / 2 + 0.5) * velocity,
                    random.nextDouble() * velocity - velocity / 2
            );
            level.addFreshEntity(ball);
        }

        EntityType.ELDER_GUARDIAN.spawn(level, BlockPos.containing(hitPosition), MobSpawnType.MOB_SUMMONED);
        Util.locateNearestMatchingBlock(hitPosition, blockPos -> {
            if (level.getBlockState(blockPos).canBeReplaced(Fluids.WATER)) {
                level.destroyBlock(blockPos, false);
                level.setBlock(blockPos, Blocks.WATER.defaultBlockState(), 3);
            }
            return false;
        }, 100);
    }

    @Override
    public ResourceLocation getResourceLocation() {
        return new ResourceLocation(CreateMissiles.MOD_ID, "guardian_warhead");
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("warheads.createmissiles.guardian_warhead");
    }
}
