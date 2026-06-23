package net.woukie.createmissiles.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.projectile.AbstractHurtingProjectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.HitResult;
import net.woukie.createmissiles.Util;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3d;

public class GuardianballEntity extends BallEntity {
    boolean spent;
    final float averageRadius = 6;

    public GuardianballEntity(EntityType<? extends AbstractHurtingProjectile> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected void onHit(@NotNull HitResult hitResult) {
        if (level().getServer() == null || spent) return;
        super.onHit(hitResult);
        BlockPos spawnPos = Util.locateNearestMatchingBlock(hitResult.getLocation(), blockPos -> level().getBlockState(blockPos).canBeReplaced(Fluids.WATER), 20);
        spawnPos = spawnPos == null ? BlockPos.containing(hitResult.getLocation()) : spawnPos;
        explode(spawnPos);
    }

    protected void explode(BlockPos position) {
        int entityCount = (int)(Math.random() * 2);
        for (int i = 0; i < entityCount + 1; i++) {
            var entity = EntityType.GUARDIAN.spawn((ServerLevel) level(), position, MobSpawnType.MOB_SUMMONED);
            if (entity != null) entity.setPersistenceRequired();
        }

        for (int i = 0; i < 100; i++) {
            double distance = level().random.nextGaussian() * averageRadius;
            double yaw = level().random.nextFloat() * Math.PI * 2;
            double pitch = level().random.nextGaussian() * Math.PI / 4;
            Vector3d offset = new Vector3d(distance, 0, 0);
            offset.rotateZ(pitch);
            offset.rotateY(yaw);
            BlockPos target = BlockPos.containing(position.getCenter().add(offset.x, offset.y, offset.z));
            if (level().getBlockState(target).canBeReplaced(Fluids.WATER)) {
                level().destroyBlock(target, false);
                level().setBlock(target, Blocks.WATER.defaultBlockState(), 3);
            }
        }

        spent = true;
        this.discard();
    }

    @Override
    public @NotNull ItemStack getItem() {
        return Items.HEART_OF_THE_SEA.getDefaultInstance();
    }
}
