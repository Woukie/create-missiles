package net.woukie.createmissiles.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.projectile.AbstractHurtingProjectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.woukie.createmissiles.registry.EntityTypes;
import org.jetbrains.annotations.NotNull;

public class InfernalballEntity extends BlazingballEntity {
    public InfernalballEntity(EntityType<? extends AbstractHurtingProjectile> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected void explode(BlockPos position) {
        EntityTypes.INFERNAL_AREA.get().spawn((ServerLevel) level(), position, MobSpawnType.MOB_SUMMONED);

        super.explode(position);
    }

    @Override
    public @NotNull ItemStack getItem() {
        return Items.BLAZE_POWDER.getDefaultInstance();
    }
}
