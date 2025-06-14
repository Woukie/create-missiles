package net.woukie.createmissiles.entity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

import java.util.Optional;
import java.util.UUID;

public class MissileEntity extends Entity {
    private static final EntityDataAccessor<Optional<UUID>> CONTROLLER_ID =
            SynchedEntityData.defineId(MissileEntity.class, EntityDataSerializers.OPTIONAL_UUID);

    private static final EntityDataAccessor<String> WARHEAD_TYPE =
            SynchedEntityData.defineId(MissileEntity.class, EntityDataSerializers.STRING);
    private static final EntityDataAccessor<String> CHASSIS_TYPE =
            SynchedEntityData.defineId(MissileEntity.class, EntityDataSerializers.STRING);
    private static final EntityDataAccessor<String> THRUSTER_TYPE =
            SynchedEntityData.defineId(MissileEntity.class, EntityDataSerializers.STRING);

    private static final EntityDataAccessor<Integer> WARHEAD_BUILD_PERCENT =
            SynchedEntityData.defineId(MissileEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> CHASSIS_BUILD_PERCENT =
            SynchedEntityData.defineId(MissileEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> THRUSTER_BUILD_PERCENT =
            SynchedEntityData.defineId(MissileEntity.class, EntityDataSerializers.INT);

    private boolean initialized;

    public MissileEntity(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(CONTROLLER_ID, Optional.empty());
        this.entityData.define(WARHEAD_TYPE, "");
        this.entityData.define(CHASSIS_TYPE, "");
        this.entityData.define(THRUSTER_TYPE, "");
        this.entityData.define(WARHEAD_BUILD_PERCENT, 0);
        this.entityData.define(CHASSIS_BUILD_PERCENT, 0);
        this.entityData.define(THRUSTER_BUILD_PERCENT, 0);
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

    @Override
    public boolean isAttackable() {
        return false;
    }

    public void setControllerID(UUID controllerID) {
        entityData.set(CONTROLLER_ID, Optional.ofNullable(controllerID));
    }

    public Optional<UUID> getControllerID() {
        return entityData.get(CONTROLLER_ID);
    }

    public ResourceLocation getWarheadType() {
        return new ResourceLocation(entityData.get(WARHEAD_TYPE));
    }

    public ResourceLocation getChassisType() {
        return new ResourceLocation(entityData.get(CHASSIS_TYPE));
    }

    public ResourceLocation getThrusterType() {
        return new ResourceLocation(entityData.get(THRUSTER_TYPE));
    }

    public int getWarheadBuildPercent() {
        return entityData.get(WARHEAD_BUILD_PERCENT);
    }

    public int getThrusterBuildPercent() {
        return entityData.get(THRUSTER_BUILD_PERCENT);
    }

    public int getChassisBuildPercent() {
        return entityData.get(CHASSIS_BUILD_PERCENT);
    }

    public void setWarheadBuildPercent(int percent) {
        entityData.set(WARHEAD_BUILD_PERCENT, percent);
    }

    public void setChassisBuildPercent(int percent) {
        entityData.set(CHASSIS_BUILD_PERCENT, percent);
    }

    public void setThrusterBuildPercent(int percent) {
        entityData.set(THRUSTER_BUILD_PERCENT, percent);
    }

    public void setWarheadType(ResourceLocation type) {
        entityData.set(WARHEAD_TYPE, type == null ? "" : type.toString(), true);
    }

    public void setChassisType(ResourceLocation type) {
        entityData.set(CHASSIS_TYPE, type == null ? "" : type.toString(), true);
    }

    public void setThrusterType(ResourceLocation type) {
        entityData.set(THRUSTER_TYPE, type == null ? "" : type.toString());
    }
}
