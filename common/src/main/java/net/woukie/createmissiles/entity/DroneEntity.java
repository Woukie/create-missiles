package net.woukie.createmissiles.entity;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.FlyingMob;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class DroneEntity extends FlyingMob {
    public DroneEntity(EntityType<? extends FlyingMob> entityType, Level level) {
        super(entityType, level);
    }

    protected float getStandingEyeHeight(@NotNull Pose pose, EntityDimensions entityDimensions) {
        return entityDimensions.height * 0.35F;
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level().isClientSide) {
            float h = Mth.cos(this.getYRot() * ((float)Math.PI / 180F)) * 1.51F;
            float j = Mth.sin(this.getYRot() * ((float)Math.PI / 180F)) * 1.51F;
            this.level().addParticle(ParticleTypes.MYCELIUM, this.getX() + h, this.getY(), this.getZ() + j, 0, 0, 0);
            this.level().addParticle(ParticleTypes.MYCELIUM, this.getX() - h, this.getY(), this.getZ() - j, 0, 0, 0);
        }
    }

    @Override
    protected InteractionResult mobInteract(Player player, InteractionHand interactionHand) {
        if (interactionHand == InteractionHand.OFF_HAND) {
            kill();
            level().playSound(null, blockPosition(), SoundEvents.SCAFFOLDING_BREAK, SoundSource.NEUTRAL);
            return InteractionResult.SUCCESS;
        }

        return super.mobInteract(player, interactionHand);
    }
}
