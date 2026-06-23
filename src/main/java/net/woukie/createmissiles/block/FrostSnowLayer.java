package net.woukie.createmissiles.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class FrostSnowLayer extends InfernalAshLayer {
    public FrostSnowLayer(Properties properties) {
        super(properties);
    }

    @Override
    public void stepOn(Level level, BlockPos blockPos, BlockState blockState, Entity entity) {
        if (!entity.getType().equals(EntityType.SNOW_GOLEM)) {
            int damage = blockState.getValue(FrostSnowLayer.LAYERS) * 2 / MAX_HEIGHT;
            entity.hurt(level.damageSources().generic(), damage);
        }
    }
}
