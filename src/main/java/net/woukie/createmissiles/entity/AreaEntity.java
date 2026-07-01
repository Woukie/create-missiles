package net.woukie.createmissiles.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3d;

public abstract class AreaEntity extends Entity {
    public AreaEntity(EntityType<? extends Entity> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public void tick() {
        super.tick();

        if (level().isClientSide()) {
            Vec3 pos = blockPosition().getCenter().add(random.nextGaussian() * 3, -0.5, random.nextGaussian() * 3);
            level().addParticle(getParticle(), pos.x, pos.y, pos.z, 0, random.nextFloat() * 0.2 + 0.1, 0);
            return;
        }

        if (onGround()) {
            setDeltaMovement(Vec3.ZERO);
        } else {
            addDeltaMovement(new Vec3(0, -0.05, 0));
        }

        move(MoverType.SELF, getDeltaMovement());

        for (int i = 0; i < getApplicationsPerTick(); i++) {
            double distance = level().random.nextGaussian() * getRadius();
            double yaw = level().random.nextFloat() * Math.PI * 2;
            double pitch = level().random.nextGaussian() * Math.PI / 4;
            Vector3d offset = new Vector3d(distance, 0, 0);
            offset.rotateZ(pitch);
            offset.rotateY(yaw);
            apply(BlockPos.containing(position().add(offset.x, offset.y, offset.z)), (ServerLevel) level());
        }
    }

    public abstract ParticleOptions getParticle();

    public int getRadius() {
        return 5;
    }
    public int getApplicationsPerTick() {
        return 5;
    }

    public abstract void apply(BlockPos blockPos, ServerLevel level);

    @Override
    protected void readAdditionalSaveData(CompoundTag compoundTag) {

    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compoundTag) {

    }
}
