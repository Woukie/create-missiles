package net.woukie.createmissiles.missilemanager.parts.warheads;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.entity.projectile.SmallFireball;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.woukie.createmissiles.CreateMissiles;
import net.woukie.createmissiles.client.MissilePartModel;
import net.woukie.createmissiles.client.models.warheads.FlamingWarheadModel;
import net.woukie.createmissiles.missilemanager.Trajectory;
import net.woukie.createmissiles.missilemanager.parts.WarheadType;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3d;

import java.util.Random;

public class FlamingWarhead extends WarheadType {
    private final MissilePartModel model = new FlamingWarheadModel();

    @Override
    public float getWeight() {
        return 20;
    }

    @Override
    public void onDetonate(Vec3 hitPosition, Trajectory trajectory, MinecraftServer server) {
        var level = server.getLevel(trajectory.getLevelKey());
        if (level == null) return;
        Vector3d impactPos = trajectory.getPosition();
        level.explode(null, impactPos.x, impactPos.y, impactPos.z, 10, Level.ExplosionInteraction.BLOCK);

        var random = new Random();
        for (int i = 0; i < 40; i++) {
            Arrow arrow = new Arrow(level, hitPosition.x, hitPosition.y, hitPosition.z);
            arrow.setInvisible(true);
            arrow.setSecondsOnFire(200);
            arrow.setPierceLevel((byte)3);
            arrow.setSoundEvent(SoundEvents.FIRECHARGE_USE);
            arrow.setDeltaMovement(random.nextDouble() - 0.5, random.nextDouble(), random.nextDouble() - 0.5);
            level.addFreshEntity(arrow);
        }

        level.addParticle(ParticleTypes.LAVA, impactPos.x, impactPos.y, impactPos.z, random.nextGaussian() * 0.05, 0.005, random.nextGaussian() * 0.05);
    }

    @Override
    public @NotNull MissilePartModel getModel() {
        return model;
    }

    @Override
    public ResourceLocation getResourceLocation() {
        return new ResourceLocation(CreateMissiles.MOD_ID, "flaming_warhead");
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("warheads.createmissiles.flaming_warhead");
    }
}
