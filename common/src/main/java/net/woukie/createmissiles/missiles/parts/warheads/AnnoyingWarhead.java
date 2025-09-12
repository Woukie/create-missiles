package net.woukie.createmissiles.missiles.parts.warheads;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.phys.Vec3;
import net.woukie.createmissiles.CreateMissiles;
import net.woukie.createmissiles.missiles.Trajectory;
import net.woukie.createmissiles.missiles.parts.WarheadType;
import net.woukie.createmissiles.registry.Blocks;

import static net.woukie.createmissiles.Util.locateAir;

public class AnnoyingWarhead extends WarheadType {
    @Override
    public float getMass() {
        return 10;
    }

    @Override
    public void onDetonate(Vec3 hitPosition, Trajectory trajectory, MinecraftServer server) {
        var level = server.getLevel(trajectory.getLevelKey());
        if (level == null) return;
        var emptyBlock = locateAir(hitPosition.add(0, 1, 0), level, 100);
        if (emptyBlock != null) {
            level.playSound(null, BlockPos.containing(hitPosition), SoundEvents.GENERIC_EXPLODE, SoundSource.HOSTILE);
            level.setBlock(emptyBlock, Blocks.ANNOYING_JUKEBOX.get().defaultBlockState(), 3);
        } else {
            DefaultDispenseItemBehavior.spawnItem(level, Blocks.ANNOYING_JUKEBOX.get().asItem().getDefaultInstance(), 1, Direction.UP, hitPosition.add(0, 1, 0));
        }

        level.playSound(null, BlockPos.containing(hitPosition), SoundEvents.GENERIC_EXPLODE, SoundSource.NEUTRAL);
        level.sendParticles(ParticleTypes.EXPLOSION, hitPosition.x, hitPosition.y, hitPosition.z, 1, 0, 0.5, 0.5, 0.5);
    }

    @Override
    public ResourceLocation getResourceLocation() {
        return new ResourceLocation(CreateMissiles.MOD_ID, "annoying_warhead");
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("warheads.createmissiles.annoying_warhead");
    }
}
