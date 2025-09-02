package net.woukie.createmissiles.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.AbstractHurtingProjectile;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.woukie.createmissiles.registry.Blocks;
import org.jetbrains.annotations.NotNull;

public class FireballEntity extends AbstractHurtingProjectile implements ItemSupplier {
    private boolean spent;

    public FireballEntity(EntityType<? extends AbstractHurtingProjectile> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected boolean shouldBurn() {
        return true;
    }

    @Override
    protected void onHit(HitResult hitResult) {
        if (level().getServer() == null || spent) return;
        super.onHit(hitResult);

        int radius = 5;
        int height = 3;
        for (int x = -radius; x < radius; x++) {
            for (int z = -radius; z < radius; z++) {
                var chance = 1 - Math.sqrt(x * x + z * z) / radius;
                if (Math.random() > chance) continue;

                BlockPos selectedBlock = blockPosition().relative(Direction.NORTH, x).relative(Direction.EAST, z);
                var previouslySolid = false;
                for (int y = -height; y < height; y++) {
                    BlockPos testBlockPos = selectedBlock.relative(Direction.UP, y);
                    if (level().isEmptyBlock(testBlockPos)) {
                        if (previouslySolid) {
                            if (Math.random() > 0.5) {
                                level().setBlock(testBlockPos, Blocks.FLAMING_FIRE.get().defaultBlockState(), 3);
                            } else {
                                level().setBlock(testBlockPos, net.minecraft.world.level.block.Blocks.FIRE.defaultBlockState(), 3);
                            }
                            break;
                        }
                        continue;
                    }
                    previouslySolid = true;
                }
            }
        }
        spent = true;
        this.discard();
    }

    @Override
    public @NotNull ItemStack getItem() {
        return Items.BLAZE_POWDER.getDefaultInstance();
    }
}
