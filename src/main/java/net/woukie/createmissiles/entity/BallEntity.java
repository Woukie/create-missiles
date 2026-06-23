package net.woukie.createmissiles.entity;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.AbstractHurtingProjectile;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public abstract class BallEntity extends AbstractHurtingProjectile implements ItemSupplier {
    private boolean spent;

    public BallEntity(EntityType<? extends AbstractHurtingProjectile> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected boolean shouldBurn() {
        return false;
    }

    @Override
    public void tick() {
        Entity entity = this.getOwner();
        if (this.level().isClientSide || (entity == null || !entity.isRemoved()) && this.level().hasChunkAt(this.blockPosition())) {
            super.tick();
            if (this.shouldBurn()) {
                this.setSecondsOnFire(1);
            }

            HitResult hitResult = ProjectileUtil.getHitResultOnMoveVector(this, this::canHitEntity);
            if (hitResult.getType() != HitResult.Type.MISS) {
                this.onHit(hitResult);
            }

            this.checkInsideBlocks();
            Vec3 vec3 = this.getDeltaMovement();
            double d = this.getX() + vec3.x;
            double e = this.getY() + vec3.y;
            double f = this.getZ() + vec3.z;

            if (!isNoGravity()) {
                vec3 = vec3.add(0, -0.1, 0);
            }

            ProjectileUtil.rotateTowardsMovement(this, 0.2F);
            float g = this.getInertia();
            this.setDeltaMovement(vec3.add(this.xPower, this.yPower, this.zPower).scale((double)g));
            this.level().addParticle(this.getTrailParticle(), d, e + (double)0.5F, f, (double)0.0F, (double)0.0F, (double)0.0F);
            this.setPos(d, e, f);
        } else {
            this.discard();
        }
    }

    @Override
    public boolean canBeCollidedWith() {
        return false;
    }

    @Override
    public boolean canBeHitByProjectile() {
        return false;
    }

    @Override
    protected void defineSynchedData() {
    }
}
