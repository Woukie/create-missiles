package net.woukie.createmissiles.entity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

import java.util.Optional;
import java.util.UUID;

public class MissileEntity extends Entity {
    private static final EntityDataAccessor<Optional<UUID>> CONTROLLER_ID =
            SynchedEntityData.defineId(MissileEntity.class, EntityDataSerializers.OPTIONAL_UUID);

    private boolean initialized;

    public MissileEntity(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(CONTROLLER_ID, Optional.empty());
    }

    public void setControllerID(UUID controllerID) {
        entityData.set(CONTROLLER_ID, Optional.ofNullable(controllerID));
    }

    public Optional<UUID> getControllerID() {
        return entityData.get(CONTROLLER_ID);
    }

    @Override
    public void tick() {
        super.tick();

        if (!initialized) {
            initialized = true;

            if (level().isClientSide)
                return;

            var controllerID = entityData.get(CONTROLLER_ID);
            if (controllerID.isEmpty() || MissileEntityManager.get(controllerID.get()) != null) {
                remove(RemovalReason.KILLED);
                return;
            }

            MissileEntityManager.add(this);
        }
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compoundTag) {

    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compoundTag) {

    }

//    @Override
//    protected void readAdditionalSaveData(CompoundTag compoundTag) {
//        this.trackingID = compoundTag.getUUID("TrackingID");
//    }
//
//    @Override
//    protected void addAdditionalSaveData(CompoundTag compoundTag) {
//        if (trackingID != null) {
//            compoundTag.putUUID("TrackingID", trackingID);
//        }
//    }

    @Override
    public boolean isAttackable() {
        return false;
    }
}
