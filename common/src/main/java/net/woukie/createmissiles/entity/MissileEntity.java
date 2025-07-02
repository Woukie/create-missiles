package net.woukie.createmissiles.entity;

import net.minecraft.core.Rotations;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class MissileEntity extends Entity {
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

    private static final EntityDataAccessor<Rotations> ROTATION =
            SynchedEntityData.defineId(MissileEntity.class, EntityDataSerializers.ROTATIONS);

    private boolean initialized;

    public MissileEntity(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(WARHEAD_TYPE, "");
        this.entityData.define(CHASSIS_TYPE, "");
        this.entityData.define(THRUSTER_TYPE, "");
        this.entityData.define(WARHEAD_BUILD_PERCENT, 0);
        this.entityData.define(CHASSIS_BUILD_PERCENT, 0);
        this.entityData.define(THRUSTER_BUILD_PERCENT, 0);
        this.entityData.define(ROTATION, new Rotations(0, 0, 0));
    }

    @Override
    public void tick() {
        super.tick();

        if (!initialized)
            initialized = true;
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
        entityData.set(WARHEAD_TYPE, type == null ? "" : type.toString());
    }

    public void setChassisType(ResourceLocation type) {
        entityData.set(CHASSIS_TYPE, type == null ? "" : type.toString());
    }

    public void setThrusterType(ResourceLocation type) {
        entityData.set(THRUSTER_TYPE, type == null ? "" : type.toString());
    }
}
