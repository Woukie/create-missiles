package net.woukie.createmissiles.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.AbstractHurtingProjectile;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.HitResult;
import net.woukie.createmissiles.Util;
import net.woukie.createmissiles.registry.Blocks;
import org.jetbrains.annotations.NotNull;

public class FlamingballEntity extends AbstractHurtingProjectile implements ItemSupplier {
    boolean spent;

    public FlamingballEntity(EntityType<? extends AbstractHurtingProjectile> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected boolean shouldBurn() {
        return true;
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
        spreadFire(5, 3, position, (ServerLevel) level());
        spent = true;
        this.discard();
    }

    private void spreadFire(int radius, int height, BlockPos position, ServerLevel level) {
        for (int x = -radius; x < radius; x++) {
            for (int z = -radius; z < radius; z++) {
                var chance = 1 - Math.sqrt(x * x + z * z) / radius;
                if (Math.random() > chance) continue;

                BlockPos selectedBlock = position.relative(Direction.NORTH, x).relative(Direction.EAST, z);
                var previouslySolid = false;
                for (int y = -height; y < height; y++) {
                    BlockPos testBlockPos = selectedBlock.relative(Direction.UP, y);
                    if (level.isEmptyBlock(testBlockPos)) {
                        if (previouslySolid) {
                            if (Math.random() > 0.5) {
                                level.setBlock(testBlockPos, Blocks.FLAMING_FIRE.get().defaultBlockState(), 3);
                            } else {
                                level.setBlock(testBlockPos, net.minecraft.world.level.block.Blocks.FIRE.defaultBlockState(), 3);
                            }
                            break;
                        }
                        continue;
                    }
                    previouslySolid = true;
                }
            }
        }
    }

    @Override
    public @NotNull ItemStack getItem() {
        return Items.BLAZE_POWDER.getDefaultInstance();
    }
}
