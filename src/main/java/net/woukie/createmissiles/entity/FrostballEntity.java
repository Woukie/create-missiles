package net.woukie.createmissiles.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.projectile.AbstractHurtingProjectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.HitResult;
import net.woukie.createmissiles.Util;
import net.woukie.createmissiles.registry.EntityTypes;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3d;

import static net.woukie.createmissiles.entity.FrozenAreaEntity.applyFrost;

public class FrostballEntity extends BallEntity {
    private boolean spent;

    public FrostballEntity(EntityType<? extends AbstractHurtingProjectile> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected void onHit(@NotNull HitResult hitResult) {
        if (level().getServer() == null || spent) return;
        super.onHit(hitResult);

        BlockPos spawnPos = Util.locateNearestMatchingBlock(hitResult.getLocation(), blockPos -> level().getBlockState(blockPos).canBeReplaced(Fluids.WATER), 20);
        spawnPos = spawnPos == null ? BlockPos.containing(hitResult.getLocation()) : spawnPos;
        if (random.nextFloat() > 0.5) {
            var entity = EntityType.SNOW_GOLEM.spawn((ServerLevel) level(), spawnPos, MobSpawnType.MOB_SUMMONED);
            if (entity != null) entity.setPersistenceRequired();
        }
        for (int i = 0; i < 1400; i++) {
            double distance = level().random.nextGaussian() * 5;
            double yaw = level().random.nextFloat() * Math.PI * 2;
            double pitch = level().random.nextGaussian() * Math.PI / 4;
            Vector3d offset = new Vector3d(distance, 0, 0);
            offset.rotateZ(pitch);
            offset.rotateY(yaw);
            applyFrost(BlockPos.containing(position().add(offset.x, offset.y, offset.z)), level());
        }
        spent = true;
        this.discard();
    }

    @Override
    public ItemStack getItem() {
        return Items.SNOWBALL.getDefaultInstance();
    }
}
