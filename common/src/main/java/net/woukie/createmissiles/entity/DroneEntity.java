package net.woukie.createmissiles.entity;

import net.minecraft.core.Direction;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.woukie.createmissiles.registry.Items;
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

    public void aiStep() {
        if (this.isAlive() && this.isSunBurnTick()) {
            this.setSecondsOnFire(8);
        }

        super.aiStep();
    }

    @Override
    public boolean hurt(DamageSource damageSource, float f) {
        Entity entity = damageSource.getEntity();
        System.out.println(entity);
        if (!level().isClientSide() && entity != null && entity.getType().equals(EntityType.PLAYER)) {
            level().playSound(null, blockPosition(), SoundEvents.SCAFFOLDING_BREAK, SoundSource.NEUTRAL);
            DefaultDispenseItemBehavior.spawnItem(level(), new ItemStack(Items.DRONE_BOX_ITEM.get()), 1, Direction.UP, position());
            this.discard();
        }
        return super.hurt(damageSource, f);
    }
}
