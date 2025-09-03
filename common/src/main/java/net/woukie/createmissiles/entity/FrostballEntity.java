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

public class FrostballEntity extends BallEntity {
    private boolean spent;

    public FrostballEntity(EntityType<? extends AbstractHurtingProjectile> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected void onHit(@NotNull HitResult hitResult) {
        if (level().getServer() == null || spent) return;
        super.onHit(hitResult);

        int entityCount = (int)(Math.random() * 3);
        BlockPos spawnPos = Util.locateNearestMatchingBlock(hitResult.getLocation(), blockPos -> level().getBlockState(blockPos).canBeReplaced(Fluids.WATER), 20);
        spawnPos = spawnPos == null ? BlockPos.containing(hitResult.getLocation()) : spawnPos;
        for (int i = 0; i < entityCount; i++) {
            EntityType.SNOW_GOLEM.spawn((ServerLevel) level(), spawnPos, MobSpawnType.MOB_SUMMONED);
        }

        EntityTypes.FROST_AREA.get().spawn((ServerLevel) level(), spawnPos, MobSpawnType.MOB_SUMMONED);
        Util.locateNearestMatchingBlock(hitResult.getLocation(), blockPos -> {
            FrostAreaEntity.applyFrost(blockPos, level());
            return false;
        }, 300);
        spent = true;
        this.discard();
    }

    @Override
    public ItemStack getItem() {
        return Items.SNOWBALL.getDefaultInstance();
    }
}
