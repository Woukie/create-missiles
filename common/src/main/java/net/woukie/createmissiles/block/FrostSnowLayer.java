package net.woukie.createmissiles.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class FrostSnowLayer extends InfernalAshLayer {
    public FrostSnowLayer(Properties properties) {
        super(properties);
    }

    @Override
    public void stepOn(Level level, BlockPos blockPos, BlockState blockState, Entity entity) {
        if (!(entity.getType() == EntityType.SNOW_GOLEM)) {
            entity.hurt(level.damageSources().onFire(), 1);
        }
        super.stepOn(level, blockPos, blockState, entity);
    }
}
