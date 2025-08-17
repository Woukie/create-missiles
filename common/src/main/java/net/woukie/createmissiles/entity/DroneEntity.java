package net.woukie.createmissiles.entity;
import com.google.common.base.Suppliers;
import net.minecraft.core.Rotations;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

import java.util.function.Supplier;

public class DroneEntity extends Entity {

    public DroneEntity(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    private static final EntityDataAccessor<Rotations> ROTATION =
            SynchedEntityData.defineId(DroneEntity.class, EntityDataSerializers.ROTATIONS);

    @Override
    protected void defineSynchedData() {
        this.entityData.define(ROTATION, new Rotations(0, 0, 0));
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compoundTag) {

    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compoundTag) {

    }

    @Override
    public boolean isAttackable() {
        return false;
    }

    public Rotations getRotation() {
        return entityData.get(ROTATION);
    }

    public void setRotation(Rotations rotation) {
        entityData.set(ROTATION, rotation);
    }
}
