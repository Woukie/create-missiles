package net.woukie.createmissiles.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.joml.Vector3d;

public class MessyEntity extends Entity {
    private static final EntityDataAccessor<Integer> BLOCKS_LEFT;
    public final int radius = 35;
    public final int spreadRadius = 25;
    public final int applysPerTick = 100;

    public MessyEntity(EntityType<? extends Entity> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(BLOCKS_LEFT, 50000);
    }

    @Override
    public void tick() {
        super.tick();

        int blocksLeft = entityData.get(BLOCKS_LEFT);

        for (int i = 0; i < applysPerTick; i++) {
            if (blocksLeft - i < 0)
                discard();
            double distance = level().random.nextFloat() * radius;
            double yaw = level().random.nextFloat() * Math.PI * 2;
            double pitch = (level().random.nextFloat() - 0.5) * Math.PI;
            Vector3d offset = new Vector3d(distance, 0, 0);
            offset.rotateZ(pitch);
            offset.rotateY(yaw);

            double spreadDistance = level().random.nextFloat() * spreadRadius;
            double spreadYaw = level().random.nextFloat() * Math.PI * 2;
            double spreadPitch = (level().random.nextFloat() - 0.5) * Math.PI;
            Vector3d spreadOffset = new Vector3d(spreadDistance, 0, 0);
            spreadOffset.rotateZ(spreadPitch);
            spreadOffset.rotateY(spreadYaw);

            BlockPos oldPosition = new BlockPos((int) (offset.x), (int) (offset.y), (int) (offset.z)).offset(blockPosition());
            BlockPos newPosition = new BlockPos((int) (offset.x + spreadOffset.x), (int) (offset.y + spreadOffset.y), (int) (offset.z + spreadOffset.z)).offset(blockPosition());
            if (level().isEmptyBlock(newPosition)) {
                BlockState state = level().getBlockState(oldPosition);
                level().setBlock(newPosition, state, 3);
                level().removeBlock(oldPosition, true);
            }

            if (random.nextFloat() > 0.25 / applysPerTick) {
                level().playSound(null, oldPosition, SoundEvents.ENDERMAN_TELEPORT, SoundSource.NEUTRAL);
                level().playSound(null, newPosition, SoundEvents.ENDERMAN_TELEPORT, SoundSource.NEUTRAL);
                level().addParticle(ParticleTypes.FLASH, oldPosition.getX(), oldPosition.getY(), oldPosition.getZ(), 0, 0, 0);
            }
        }
        entityData.set(BLOCKS_LEFT, blocksLeft - applysPerTick);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compoundTag) {

    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compoundTag) {

    }

    static {
        BLOCKS_LEFT = SynchedEntityData.defineId(MessyEntity.class, EntityDataSerializers.INT);
    }
}
