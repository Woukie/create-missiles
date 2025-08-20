package net.woukie.createmissiles.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.AbstractHurtingProjectile;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.woukie.createmissiles.registry.Blocks;

public class FireballEntity extends AbstractHurtingProjectile implements ItemSupplier {
    private boolean spent;

    public FireballEntity(EntityType<? extends AbstractHurtingProjectile> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public void tick() {
        Entity entity = this.getOwner();
        if (!this.isInWater() && (this.level().isClientSide || (entity == null || !entity.isRemoved()) && this.level().hasChunkAt(this.blockPosition()))) {
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
    protected void onHit(HitResult hitResult) {
        if (level().getServer() == null || spent) return;
        super.onHit(hitResult);

        int radius = 5;
        int height = 3;
        for (int x = -radius; x < radius; x++) {
            for (int z = -radius; z < radius; z++) {
                var chance = 1 - Math.sqrt(x * x + z * z) / radius;
                if (Math.random() > chance) continue;

                BlockPos selectedBlock = blockPosition().relative(Direction.NORTH, x).relative(Direction.EAST, z);
                var previouslySolid = false;
                for (int y = -height; y < height; y++) {
                    BlockPos testBlockPos = selectedBlock.relative(Direction.UP, y);
                    if (level().isEmptyBlock(testBlockPos)) {
                        if (previouslySolid) {
                            if (Math.random() > 0.5) {
                                level().setBlock(testBlockPos, Blocks.FLAMING_FIRE.get().defaultBlockState(), 3);
                            } else {
                                level().setBlock(testBlockPos, net.minecraft.world.level.block.Blocks.FIRE.defaultBlockState(), 3);
                            }
                            break;
                        }
                        continue;
                    }
                    previouslySolid = true;
                }
            }
        }
        spent = true;
        this.discard();
    }

    @Override
    protected void defineSynchedData() {

    }

    @Override
    public ItemStack getItem() {
        return Items.BLAZE_POWDER.getDefaultInstance();
    }
}
