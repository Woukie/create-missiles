package net.woukie.createmissiles.missilemanager.parts.warheads;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.phys.Vec3;
import net.woukie.createmissiles.CreateMissiles;
import net.woukie.createmissiles.client.MissilePartModel;
import net.woukie.createmissiles.client.models.warheads.FrostWarheadModel;
import net.woukie.createmissiles.entity.FrostballEntity;
import net.woukie.createmissiles.missilemanager.Trajectory;
import net.woukie.createmissiles.missilemanager.asyncexplosionhandler.Explosion;
import net.woukie.createmissiles.missilemanager.asyncexplosionhandler.ExplosionHandler;
import net.woukie.createmissiles.missilemanager.parts.WarheadType;
import net.woukie.createmissiles.registry.EntityTypes;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class FrostWarhead extends WarheadType {
    private final MissilePartModel model = new FrostWarheadModel();
    private static final float snowballVelocity = 3;
    private static final float snowballSlowVelocity = 1.5f;

    @Override
    public float getMass() {
        return 10;
    }

    @Override
    public void onDetonate(Vec3 hitPosition, Trajectory trajectory, MinecraftServer server) {
        var level = server.getLevel(trajectory.getLevelKey());
        if (level == null) return;
        ExplosionHandler.get().createExplosion(new Explosion(level, hitPosition, 10));

        var random = new Random();
        for (int i = 0; i < 30; i++) {
            FrostballEntity ball = new FrostballEntity(EntityTypes.FROSTBALL.get(), level);
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
    public @NotNull MissilePartModel getModel() {
        return model;
    }

    @Override
    public ResourceLocation getResourceLocation() {
        return new ResourceLocation(CreateMissiles.MOD_ID, "frost_warhead");
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("warheads.createmissiles.frost_warhead");
    }
}
