package net.woukie.createmissiles.missiles.parts.warheads;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.FastColor;
import net.minecraft.world.Container;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import net.woukie.createmissiles.CreateMissiles;
import net.woukie.createmissiles.missiles.Trajectory;
import net.woukie.createmissiles.missiles.asyncexplosionhandler.ExplosionHandler;
import net.woukie.createmissiles.missiles.asyncexplosionhandler.Explosion;
import net.woukie.createmissiles.missiles.parts.WarheadType;

public class DragonWarhead extends WarheadType {
    @Override
    public float getMass() {
        return 37.5f;
    }

    @Override
    public void onDetonate(Vec3 hitPosition, Trajectory trajectory, MinecraftServer server) {
        var level = server.getLevel(trajectory.getLevelKey());
        if (level == null) return;
        level.playSound(null, BlockPos.containing(hitPosition), SoundEvents.DRAGON_FIREBALL_EXPLODE, SoundSource.HOSTILE, 100, 1);
        level.playSound(null, BlockPos.containing(hitPosition), SoundEvents.GENERIC_EXPLODE, SoundSource.HOSTILE, 100, 1);
        ExplosionHandler.get().createExplosion(new Explosion(level, hitPosition, 90), FastColor.ARGB32.color(255, 234, 194, 255), 15000);
        level.setBlock(BlockPos.containing(hitPosition), Blocks.DRAGON_EGG.defaultBlockState(), 3);
    }

    @Override
    public ResourceLocation getResourceLocation() {
        return new ResourceLocation(CreateMissiles.MOD_ID, "dragon_warhead");
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("warheads.createmissiles.dragon_warhead");
    }
}
