package net.woukie.createmissiles.missilemanager.parts.warheads;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.phys.Vec3;
import net.woukie.createmissiles.CreateMissiles;
import net.woukie.createmissiles.client.MissilePartModel;
import net.woukie.createmissiles.client.models.warheads.WitheredWarheadModel;
import net.woukie.createmissiles.entity.WitheredballEntity;
import net.woukie.createmissiles.missilemanager.Trajectory;
import net.woukie.createmissiles.missilemanager.asyncexplosionhandler.Explosion;
import net.woukie.createmissiles.missilemanager.asyncexplosionhandler.ExplosionHandler;
import net.woukie.createmissiles.missilemanager.parts.WarheadType;
import net.woukie.createmissiles.registry.EntityTypes;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class WitheredWarhead extends WarheadType {
    private final MissilePartModel model = new WitheredWarheadModel();
    private static final float ballVelocity = 3;
    private static final float slowBallVelocity = 1.5f;

    @Override
    public float getMass() {
        return 10;
    }

    @Override
    public void onDetonate(Vec3 hitPosition, Trajectory trajectory, MinecraftServer server) {
        var level = server.getLevel(trajectory.getLevelKey());
        if (level == null) return;
        ExplosionHandler.get().createExplosion(new Explosion(level, hitPosition, 10));
        EntityType.WITHER.spawn(level, BlockPos.containing(hitPosition), MobSpawnType.MOB_SUMMONED);

        var random = new Random();
        for (int i = 0; i < 30; i++) {
            WitheredballEntity ball = new WitheredballEntity(EntityTypes.WITHEREDBALL.get(), level);
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
    }

    @Override
    public @NotNull MissilePartModel getModel() {
        return model;
    }

    @Override
    public ResourceLocation getResourceLocation() {
        return new ResourceLocation(CreateMissiles.MOD_ID, "withered_warhead");
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("warheads.createmissiles.withered_warhead");
    }
}
