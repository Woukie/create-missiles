package net.woukie.createmissiles.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.projectile.AbstractHurtingProjectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.HitResult;
import net.woukie.createmissiles.Util;
import net.woukie.createmissiles.missiles.asyncexplosionhandler.Explosion;
import net.woukie.createmissiles.missiles.asyncexplosionhandler.ExplosionHandler;
import org.jetbrains.annotations.NotNull;

public class WitheredballEntity extends BallEntity {
    private boolean spent;

    public WitheredballEntity(EntityType<? extends AbstractHurtingProjectile> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected void onHit(@NotNull HitResult hitResult) {
        if (level().getServer() == null || spent) return;
        super.onHit(hitResult);

        BlockPos spawnPos = Util.locateNearestMatchingBlock(hitResult.getLocation(), blockPos -> level().getBlockState(blockPos).canBeReplaced(Fluids.WATER), 20);
        spawnPos = spawnPos == null ? BlockPos.containing(hitResult.getLocation()) : spawnPos;

        level().playSound(null, spawnPos, SoundEvents.GENERIC_EXPLODE, SoundSource.HOSTILE);
        if (random.nextFloat() > 0.5) {
            var e = EntityType.WITHER_SKELETON.spawn((ServerLevel) level(), spawnPos, MobSpawnType.MOB_SUMMONED);
            if (e != null) e.setPersistenceRequired();
        }
        ExplosionHandler.get().createExplosion(new Explosion(level(), spawnPos.getCenter(), 2));

        spreadRoses(6, 2, spawnPos, (ServerLevel) level());
        spent = true;
        this.discard();
    }

    private void spreadRoses(int radius, int height, BlockPos position, ServerLevel level) {
        for (int x = -radius; x < radius; x++) {
            for (int z = -radius; z < radius; z++) {
                var chance = 1 - Math.sqrt(x * x + z * z) / radius;
                if (Math.random() > chance) continue;

                BlockPos selectedBlock = position.relative(Direction.NORTH, x).relative(Direction.EAST, z);
                for (int y = height; y > -height; y--) {
                    BlockPos testPos = selectedBlock.relative(Direction.UP, y);
                    BlockState testState = level.getBlockState(testPos);
                    if (level.isEmptyBlock(testPos.above()) || level.getBlockState(testPos.above()).canBeReplaced(Fluids.WATER)) {
                        level.destroyBlock(testPos.above(), false);
                    } else {
                        continue;
                    }
                    if (testState.is(
                            BlockTags.DIRT) ||
                            testState.is(Blocks.FARMLAND) ||
                            testState.is(Blocks.NETHERRACK) ||
                            testState.is(Blocks.SOUL_SAND) ||
                            testState.is(Blocks.SOUL_SOIL)
                    ) {
                        if (Math.random() > 0.5) {
                            level.setBlock(testPos, Blocks.WITHER_ROSE.defaultBlockState(), 3);
                        } else {
                            level.setBlock(testPos, Blocks.FIRE.defaultBlockState(), 3);
                        }
                    }
                }
            }
        }
    }

    @Override
    public ItemStack getItem() {
        return Items.WITHER_SKELETON_SKULL.getDefaultInstance();
    }
}
