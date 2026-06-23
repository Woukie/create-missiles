package net.woukie.createmissiles.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

public class AnnoyingJukeboxBlock extends Block {
    public AnnoyingJukeboxBlock(Properties properties) {
        super(properties);
    }

    @Override
    public void tick(BlockState blockState, ServerLevel serverLevel, BlockPos blockPos, RandomSource randomSource) {
        serverLevel.scheduleTick(blockPos, this, 10 + randomSource.nextInt(10));
        var pos = blockPos.getCenter();
        serverLevel.sendParticles(ParticleTypes.NOTE, pos.x, pos.y - 3f/16f, pos.z, 5, 0.25, 0.25, 0.25, 0);
        var offset = new Vec3(randomSource.nextFloat() * 5 - 2.5, randomSource.nextFloat() * 5 - 2.5, randomSource.nextFloat() * 5 - 2.5);
        pos = pos.add(offset);
        serverLevel.playSound(null, BlockPos.containing(pos), SoundEvents.CAT_PURREOW, SoundSource.MASTER, 10, 1);

        var nearPlayer = serverLevel.getNearestPlayer(pos.x, pos.y, pos.z, 5, true);
        if (nearPlayer != null && randomSource.nextFloat() < 0.05) {
            if (!nearPlayer.swinging || nearPlayer.swingingArm != InteractionHand.MAIN_HAND) return;
            nearPlayer.hurt(serverLevel.damageSources().generic(),1);
            nearPlayer.setDeltaMovement(nearPlayer.getDeltaMovement().add(new Vec3(0, 0.5, 0)));
            serverLevel.playSound(null, nearPlayer.blockPosition(), SoundEvents.BAT_TAKEOFF, SoundSource.MASTER, 1, 1);
        }
    }

    @Override
    public @NotNull VoxelShape getShape(BlockState blockState, @NotNull BlockGetter blockGetter, @NotNull BlockPos blockPos, @NotNull CollisionContext collisionContext) {
        return Shapes.box(3f/16f, 0, 3f/16f, 13f/16f, 10f/16f, 13f/16f);
    }

    @Override
    public @NotNull RenderShape getRenderShape(@NotNull BlockState blockState) {
        return RenderShape.MODEL;
    }

    @Override
    public void onPlace(BlockState blockState, Level level, BlockPos blockPos, BlockState blockState2, boolean bl) {
        super.onPlace(blockState, level, blockPos, blockState2, bl);
        level.scheduleTick(blockPos, this, 0);
    }
}
