package net.woukie.createmissiles.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.AbstractHurtingProjectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class InfernalballEntity extends BlazingballEntity {
    public InfernalballEntity(EntityType<? extends AbstractHurtingProjectile> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected void explode(BlockPos position) {
        System.out.println("SPAWN INFERNAL_AREA HERE");

        super.explode(position);
    }

    @Override
    public @NotNull ItemStack getItem() {
        return Items.BLAZE_POWDER.getDefaultInstance();
    }
}
