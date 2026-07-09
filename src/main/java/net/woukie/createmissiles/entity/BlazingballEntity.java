package net.woukie.createmissiles.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.projectile.AbstractHurtingProjectile;
import net.minecraft.world.level.Level;

public class BlazingballEntity extends FlamingballEntity {
    public BlazingballEntity(EntityType<? extends AbstractHurtingProjectile> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected void explode(BlockPos position) {
        int blazeCount = (int)(Math.random() * 3);
        for (int i = 0; i < blazeCount + 1; i++) {
            var entity = EntityType.BLAZE.spawn((ServerLevel) level(), position, MobSpawnType.MOB_SUMMONED);
            if (entity != null) entity.setPersistenceRequired();
        }

        super.explode(position);
    }
}
