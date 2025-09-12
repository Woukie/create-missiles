package net.woukie.createmissiles.missiles.parts.warheads;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import net.woukie.createmissiles.CreateMissiles;
import net.woukie.createmissiles.Util;
import net.woukie.createmissiles.missiles.Trajectory;
import net.woukie.createmissiles.missiles.asyncexplosionhandler.Explosion;
import net.woukie.createmissiles.missiles.asyncexplosionhandler.ExplosionHandler;
import net.woukie.createmissiles.missiles.parts.WarheadType;
import net.woukie.createmissiles.registry.Items;

public class DirectHitWarhead extends WarheadType {
    @Override
    public float getMass() {
        return 50;
    }

    @Override
    public void onDetonate(Vec3 hitPosition, Trajectory trajectory, MinecraftServer server) {
        var level = server.getLevel(trajectory.getLevelKey());
        if (level == null) return;
        BlockPos dragonEgg = Util.locateNearestMatchingBlock(hitPosition, blockPos -> level.getBlockState(blockPos).is(Blocks.DRAGON_EGG), 20);
        if (dragonEgg != null) {
            level.destroyBlock(dragonEgg, false);
            DefaultDispenseItemBehavior.spawnItem(level, new ItemStack(Items.DRAGON_EGG_SHELL.get(), 3), 1, Direction.UP, dragonEgg.getCenter());
        }
        ExplosionHandler.get().createExplosion(new Explosion(level, hitPosition, 150, 30));
    }

    @Override
    public ResourceLocation getResourceLocation() {
        return new ResourceLocation(CreateMissiles.MOD_ID, "direct_hit_warhead");
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("warheads.createmissiles.direct_hit_warhead");
    }
}
