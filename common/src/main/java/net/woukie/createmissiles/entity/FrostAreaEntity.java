package net.woukie.createmissiles.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.TraceableEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SnowLayerBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3d;

import static net.minecraft.world.level.block.SnowLayerBlock.LAYERS;

public class FrostAreaEntity extends Entity {
    public final int radius = 5;
    public final int frostsPerTick = 5;

    public FrostAreaEntity(EntityType<? extends Entity> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public void tick() {
        super.tick();
        if (level().isClientSide()) {
            Vec3 pos = blockPosition().getCenter().add(random.nextGaussian() * 3, -0.5, random.nextGaussian() * 3);
            level().addParticle(ParticleTypes.SNOWFLAKE, pos.x, pos.y, pos.z, 0, random.nextFloat() * 0.2 + 0.1, 0);
            return;
        }

        for (int i = 0; i < frostsPerTick; i++) {
            double distance = level().random.nextGaussian() * radius;
            double yaw = level().random.nextFloat() * Math.PI * 2;
            double pitch = level().random.nextGaussian() * Math.PI / 4;
            Vector3d offset = new Vector3d(distance, 0, 0);
            offset.rotateZ(pitch);
            offset.rotateY(yaw);
            applyFrost(BlockPos.containing(position().add(offset.x, offset.y, offset.z)), level());
        }
    }

    public void flintAndSteeled() {
        discard();
        level().addParticle(ParticleTypes.FLASH, position().x, position().y, position().z, 0, 0, 0);
        level().playSound(null, BlockPos.containing(position()), SoundEvents.FLINTANDSTEEL_USE, SoundSource.NEUTRAL);
        level().playSound(null, BlockPos.containing(position()), SoundEvents.GENERIC_DEATH, SoundSource.NEUTRAL);
    }

    @Override
    protected void defineSynchedData() {

    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compoundTag) {

    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compoundTag) {

    }

//    pushEntitiesUp causes this to go up
    @Override
    public void teleportRelative(double d, double e, double f) {
    }

    public static void applyFrost(BlockPos blockPos, Level level) {
//        Can't use switch because Java 17 said so
        BlockState blockState = level.getBlockState(blockPos);
        BlockState blockStateBelow = level.getBlockState(blockPos.below());
        Block block = blockState.getBlock();

        if (block.equals(Blocks.SNOW)) {
            int currentLayer = blockState.getValue(SnowLayerBlock.LAYERS);
            if (currentLayer < 7) {
                BlockState newBlockState = blockState.setValue(SnowLayerBlock.LAYERS, currentLayer + 1);
                Block.pushEntitiesUp(blockState, newBlockState, level, blockPos);
                level.setBlockAndUpdate(blockPos, newBlockState);
            } else if (currentLayer == 7) {
                level.setBlock(blockPos, Blocks.POWDER_SNOW.defaultBlockState(), 3);
            }
        } else if (block.equals(Blocks.WATER)) {
            level.setBlock(blockPos, Blocks.ICE.defaultBlockState(), 3);
        } else if (block.equals(Blocks.ICE)) {
            level.setBlock(blockPos, Blocks.PACKED_ICE.defaultBlockState(), 3);
        } else if (block.equals(Blocks.PACKED_ICE)) {
            level.setBlock(blockPos, Blocks.BLUE_ICE.defaultBlockState(), 3);
        } else if (block.equals(Blocks.LAVA) && blockState.getFluidState().isSource()) {
            level.setBlock(blockPos, Blocks.OBSIDIAN.defaultBlockState(), 3);
        } else if (block.equals(Blocks.FIRE)) {
            level.setBlock(blockPos, Blocks.AIR.defaultBlockState(), 3);
        } else if (block.equals(Blocks.AIR) || (blockState.canBeReplaced(Fluids.WATER) && !block.equals(Blocks.POWDER_SNOW))) {
            boolean belowUnsurviable = blockStateBelow.is(BlockTags.SNOW_LAYER_CANNOT_SURVIVE_ON);
            boolean belowSurviable = blockStateBelow.is(BlockTags.SNOW_LAYER_CANNOT_SURVIVE_ON);
            boolean belowIsFullSnow = blockStateBelow.is(Blocks.SNOW) && blockStateBelow.getValue(LAYERS) == 8;
            boolean middleCase = Block.isFaceFull(blockStateBelow.getCollisionShape(level, blockPos.below()), Direction.UP) || belowIsFullSnow;
            boolean snowLayerSurvives = !belowUnsurviable && (belowSurviable || middleCase);
            if (snowLayerSurvives) {
                if (!block.equals(Blocks.AIR)) {
                    level.destroyBlock(blockPos, true);
                }
                level.setBlock(blockPos, Blocks.SNOW.defaultBlockState(), 3);
            }
        }
    }
}
