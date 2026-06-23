package net.woukie.createmissiles.entity.drone;
import net.minecraft.core.*;

import net.minecraft.core.Direction;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.*;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.FlyingMob;
import net.minecraft.world.entity.ai.control.BodyRotationControl;
import net.minecraft.world.entity.ai.control.LookControl;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.woukie.createmissiles.inventory.DroneMenu;
import net.woukie.createmissiles.registry.EntityTypes;
import net.woukie.createmissiles.registry.Items;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector2i;

import java.util.Arrays;
import java.util.UUID;

public class Drone extends FlyingMob {
    private final ContainerData dataAccess;

    BlockPos storedMapPos;
    BlockPos targetBlock;
    BlockPos originBlock;

    protected SimpleContainer mapContainer;

    public Drone(EntityType<? extends Drone> entityType, Level level) {
        super(entityType, level);
        this.moveControl = new DroneMoveControl(this, this);
        this.lookControl = new EmptyLookControl(this, this);
        this.mapContainer = new SimpleContainer(1);
        this.dataAccess = new ContainerData() {
            @Override
            public int get(int i) {
                var uuidArray = uuidToShortArray(getUUID());
                return switch (i) {
                    case 0 -> uuidArray[0];
                    case 1 -> uuidArray[1];
                    case 2 -> uuidArray[2];
                    case 3 -> uuidArray[3];
                    case 4 -> uuidArray[4];
                    case 5 -> uuidArray[5];
                    case 6 -> uuidArray[6];
                    case 7 -> uuidArray[7];
                    case 8 -> blockPosition().getX();
                    case 9 -> blockPosition().getZ();
                    case 10 -> targetBlock != null || originBlock != null ? 1 : 0;
                    case 11 -> getType().equals(EntityTypes.BASIC_DRONE.get()) ? 0 : 1; // TODO: Find a better way of passing what the entity is
                    default -> 0;
                };
            }

            @Override
            public void set(int i, int j) {

            }

            @Override
            public int getCount() {
                return 12;
            }

            private static int[] uuidToShortArray(UUID uUID) {
                long mostSig = uUID.getMostSignificantBits();
                long leastSig = uUID.getLeastSignificantBits();
                return new int[]{
                        (int)((mostSig >> 48) & 0xFFFF),  // bits 63-48
                        (int)((mostSig >> 32) & 0xFFFF),  // bits 47-32
                        (int)((mostSig >> 16) & 0xFFFF),  // bits 31-16
                        (int)(mostSig & 0xFFFF),          // bits 15-0
                        (int)((leastSig >> 48) & 0xFFFF), // bits 63-48
                        (int)((leastSig >> 32) & 0xFFFF), // bits 47-32
                        (int)((leastSig >> 16) & 0xFFFF), // bits 31-16
                        (int)(leastSig & 0xFFFF)          // bits 15-0
                };
            }
        };
    }

    @Override
    public void checkDespawn() {
    }

    @Override
    public boolean isPersistenceRequired() {
        return true;
    }

    @Override
    public boolean removeWhenFarAway(double d) {
        return false;
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level().isClientSide) {
            int i = 1;
            float h = Mth.cos(this.getYRot() * ((float)Math.PI / 180F)) * (1.3F + 0.21F * (float)i);
            float j = Mth.sin(this.getYRot() * ((float)Math.PI / 180F)) * (1.3F + 0.21F * (float)i);
            float k = (0.3F * 0.45F) * ((float)i * 0.2F + 1.0F);
            this.level().addParticle(ParticleTypes.MYCELIUM, this.getX() + (double)h, this.getY() + (double)k, this.getZ() + (double)j, (double)0.0F, (double)0.0F, (double)0.0F);
            this.level().addParticle(ParticleTypes.MYCELIUM, this.getX() - (double)h, this.getY() + (double)k, this.getZ() - (double)j, (double)0.0F, (double)0.0F, (double)0.0F);
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compoundTag) {
        super.readAdditionalSaveData(compoundTag);

        if (compoundTag.contains("StoredMapPosX")) {
            this.storedMapPos = new BlockPos(compoundTag.getInt("StoredMapPosX"), compoundTag.getInt("StoredMapPosY"), compoundTag.getInt("StoredMapPosZ"));
        }
        if (compoundTag.contains("TargetBlockX")) {
            this.targetBlock = new BlockPos(compoundTag.getInt("TargetBlockX"), compoundTag.getInt("TargetBlockY"), compoundTag.getInt("TargetBlockZ"));
        }
        if (compoundTag.contains("OriginBlockX")) {
            this.originBlock = new BlockPos(compoundTag.getInt("OriginBlockX"), compoundTag.getInt("OriginBlockY"), compoundTag.getInt("OriginBlockZ"));
        }

        if (compoundTag.contains("MapItem")) {
            mapContainer = new SimpleContainer(ItemStack.of(compoundTag.getCompound("MapItem")));
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compoundTag) {
        super.addAdditionalSaveData(compoundTag);
        if (this.storedMapPos != null) {
            compoundTag.putInt("StoredMapPosX", this.storedMapPos.getX());
            compoundTag.putInt("StoredMapPosY", this.storedMapPos.getY());
            compoundTag.putInt("StoredMapPosZ", this.storedMapPos.getZ());
        }
        if (this.targetBlock != null) {
            compoundTag.putInt("TargetBlockX", this.targetBlock.getX());
            compoundTag.putInt("TargetBlockY", this.targetBlock.getY());
            compoundTag.putInt("TargetBlockZ", this.targetBlock.getZ());
        }
        if (this.originBlock != null) {
            compoundTag.putInt("OriginBlockX", this.originBlock.getX());
            compoundTag.putInt("OriginBlockY", this.originBlock.getY());
            compoundTag.putInt("OriginBlockZ", this.originBlock.getZ());
        }

        compoundTag.put("MapItem", mapContainer.getItem(0).save(new CompoundTag()));
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return SoundEvents.PHANTOM_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.PHANTOM_DEATH;
    }

    @Override
    public SoundSource getSoundSource() {
        return SoundSource.PLAYERS;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.PHANTOM_AMBIENT;
    }

    @Override
    protected boolean shouldDespawnInPeaceful() {
        return false;
    }

    @Override
    protected float getStandingEyeHeight(@NotNull Pose pose, EntityDimensions entityDimensions) {
        return entityDimensions.height * 0.35F;
    }

    @Override
    public boolean isAttackable() {
        return true;
    }

    public void aiStep() {
        if (this.isAlive() && this.isSunBurnTick()) {
            this.setSecondsOnFire(8);
        }

        super.aiStep();
    }

    @Override
    public boolean isAlwaysTicking() {
        return true;
    }

    @Override
    public boolean hurt(DamageSource damageSource, float f) {
        Entity entity = damageSource.getEntity();
        if (!level().isClientSide() && entity != null && entity.getType().equals(EntityType.PLAYER)) {
            level().playSound(null, blockPosition(), SoundEvents.SCAFFOLDING_BREAK, SoundSource.NEUTRAL);
            popMap((ServerLevel) level());
            dropItem();
            this.discard();
            return true;
        }
        return super.hurt(damageSource, f);
    }

    @Override
    public void die(DamageSource damageSource) {
        if (!level().isClientSide) {
            popMap((ServerLevel) level());
            DroneHandler.get().stopTrackingDrone((ServerLevel) level(), getUUID());
        }
        super.die(damageSource);
    }

    public void popMap(ServerLevel level) {
        if (storedMapPos != null) {
            MapUtils.spawnMapAt(level, position().add(0, 0.5, 0), storedMapPos);
            level.playSound(null, position().x, position().y, position().z, SoundEvents.UI_CARTOGRAPHY_TABLE_TAKE_RESULT, SoundSource.PLAYERS, 1, 1);
            storedMapPos = null;
        } else {
            DefaultDispenseItemBehavior.spawnItem(level(), mapContainer.getItem(0), 1, Direction.UP, position());
        }
        mapContainer.clearContent();
    }

    protected void dropItem() {
        DefaultDispenseItemBehavior.spawnItem(level(), new ItemStack(Items.DRONE_BOX_ITEM.get()), 1, Direction.UP, position());
    }

    @Override
    public boolean isPickable() {
        return true;
    }

    @Override
    public @Nullable ItemStack getPickResult() {
        return Items.DRONE_BOX_ITEM.get().getDefaultInstance();
    }

    @Override
    protected @NotNull InteractionResult mobInteract(@NotNull Player player, @NotNull InteractionHand interactionHand) {
        if (storedMapPos != null) {
            popMap((ServerLevel) level());
            return InteractionResult.SUCCESS;
        }

        if (targetBlock != null || originBlock != null) {
            return InteractionResult.FAIL;
        }

        if (!this.level().isClientSide) {
            player.openMenu(new SimpleMenuProvider((ix, inventory, playerx) -> new DroneMenu(ix, inventory, this.dataAccess, mapContainer), getDisplayName()));
        }
        return InteractionResult.SUCCESS;
    }

    public void startMission(BlockPos destination) {
        if (mapContainer.getItem(0).isEmpty()) return;
        level().playSound(null, position().x, position().y, position().z, SoundEvents.NOTE_BLOCK_CHIME.value(), SoundSource.PLAYERS, 1, 1);
        this.targetBlock = destination;
        this.originBlock = blockPosition();
        DroneHandler.get().trackDrone(this);
    }

    @Override
    protected BodyRotationControl createBodyControl() {
        return new Drone.DroneBodyRotationControl(this, this);
    }

    class DroneBodyRotationControl extends BodyRotationControl {
        public DroneBodyRotationControl(Drone drone, Mob mob) {
            super(mob);
        }

        public void clientTick() {
            Drone.this.yHeadRot = Drone.this.yBodyRot;
            Drone.this.yBodyRot = Drone.this.getYRot();
        }
    }

    public static class EmptyLookControl extends LookControl {
        public EmptyLookControl(Drone drone, Mob mob) {
            super(mob);
        }

        public void tick() {
        }
    }

    public class DroneMoveControl extends MoveControl {
        public DroneMoveControl(Drone drone, Mob mob) {
            super(mob);
        }
        private boolean hasOigin, hasTarget;

        public void tick() {
            if (Drone.this.horizontalCollision) {
                setYRot(getYRot() + 180.0F);
            }
            updateTargetStatus();
            tickTravelOrWait();
            Vector2i currentPos = new Vector2i(blockPosition().getX(), blockPosition().getZ());
            if (hasTarget) {
                Vector2i targetPos = new Vector2i(targetBlock.getX(), targetBlock.getZ());
                if (targetPos.distance(currentPos) < 25) {
                    storedMapPos = targetBlock;
                    targetBlock = null;
                }
            }

            if (!hasTarget && hasOigin) {
                Vector2i originPos = new Vector2i(originBlock.getX(), originBlock.getZ());
                if (originPos.distance(currentPos) < 3) {
                    DroneHandler.get().stopTrackingDrone((ServerLevel) level(), uuid);
                    originBlock = null;
                }
            }
        }

        private void tickTravelOrWait() {
            if (hasTarget) {
                tickTravel(Drone.this.targetBlock);
            } else if (hasOigin) {
                tickTravel(Drone.this.originBlock);
            } else {
                tickWait();
            }

            updateTargetStatus();
        }

        private void updateTargetStatus() {
            hasOigin = Drone.this.originBlock != null;
            hasTarget = Drone.this.targetBlock != null;
            if (hasTarget && !hasOigin) {
                Drone.this.originBlock = Drone.this.targetBlock;
            }
        }

        private void tickWait() {
            if (!onGround()) {
                setDeltaMovement(getDeltaMovement().add(0, -0.24, 0));
                setXRot(Mth.approachDegrees(getXRot(), 70, 4F));
            } else {
                setXRot(0);
            }
        }

        private void tickTravel(BlockPos destination) {
            double dX = destination.getX() - getX();
            double dZ = destination.getZ() - getZ();
            double distance = Math.sqrt(dX * dX + dZ * dZ);
            if (Math.abs(distance) > (double)1.0E-5F) {
                float yTurnSpeed = 1.0F;
                if (distance < 30) yTurnSpeed = 10F;

                float angle = (float)Mth.atan2(dZ, dX);
                float currentYRot = Mth.wrapDegrees(getYRot() + 90.0F);
                float targetYRot = Mth.wrapDegrees(angle * (180F / (float)Math.PI));
                setYRot(Mth.approachDegrees(currentYRot, targetYRot, yTurnSpeed) - 90.0F);
                yBodyRot = getYRot();

                float targetXRot = 0;
                for (int i = 0; i < 50; i++) {
                    if (!level().isEmptyBlock(blockPosition().below(i))) {
                        targetXRot = -70;
                        break;
                    } else if (!level().isEmptyBlock(blockPosition().above(i))) {
                        targetXRot = 70;
                        break;
                    }
                }

                setXRot(Mth.approachDegrees(getXRot(), targetXRot, 1.0F));

                float pitch = getXRot() * (float)Math.PI / 180.0F;
                float yaw = getYRot() * (float)Math.PI / 180.0F;
                double x = -Math.sin(yaw) * Math.cos(pitch);
                double y = -Math.sin(pitch);
                double z = Math.cos(yaw) * Math.cos(pitch);
                Vec3 directionVector = new Vec3(x, y, z);

                Vec3 vec3 = getDeltaMovement();
                setDeltaMovement(vec3.add((directionVector.subtract(vec3).scale(0.2))));
            }
        }
    }
}
