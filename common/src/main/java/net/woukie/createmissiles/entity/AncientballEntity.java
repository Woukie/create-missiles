package net.woukie.createmissiles.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.AbstractHurtingProjectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.SculkCatalystBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.HitResult;
import net.woukie.createmissiles.Util;
import org.jetbrains.annotations.NotNull;

public class AncientballEntity extends BallEntity {
    private boolean spent;

    public AncientballEntity(EntityType<? extends AbstractHurtingProjectile> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected void onHit(@NotNull HitResult hitResult) {
        if (level().getServer() == null || spent) return;
        super.onHit(hitResult);

        BlockPos spawnPos = Util.locateNearestMatchingBlock(hitResult.getLocation(), blockPos -> level().getBlockState(blockPos).canBeReplaced(Fluids.WATER), 20);
        spawnPos = spawnPos == null ? BlockPos.containing(hitResult.getLocation()) : spawnPos;
        if (!level().isEmptyBlock(spawnPos)) level().destroyBlock(spawnPos, false);

        BlockState catalystState = Blocks.SCULK_CATALYST.defaultBlockState();
        level().setBlock(spawnPos, catalystState, 3);

        SculkCatalystBlockEntity blockEntity = new SculkCatalystBlockEntity(spawnPos, catalystState);
        level().setBlockEntity(blockEntity);

        Util.locateNearestMatchingBlock(spawnPos.getCenter(), blockPos -> {
            if (level().getBlockState(blockPos).is(BlockTags.SCULK_REPLACEABLE)) {
                level().setBlock(blockPos, Blocks.SCULK.defaultBlockState(), 3);
            }
            return false;
        }, 27);
        blockEntity.getListener().getSculkSpreader().addCursors(spawnPos, 400);

        spent = true;
        this.discard();
    }

    @Override
    public ItemStack getItem() {
        return Items.SCULK.getDefaultInstance();
    }
}
