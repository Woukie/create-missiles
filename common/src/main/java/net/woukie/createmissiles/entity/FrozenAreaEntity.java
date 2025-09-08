package net.woukie.createmissiles.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.Vec3;
import net.woukie.createmissiles.block.FrostSnowLayer;

import static net.minecraft.world.level.block.SnowLayerBlock.LAYERS;

public class FrozenAreaEntity extends AreaEntity {
    public FrozenAreaEntity(EntityType<? extends Entity> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public ParticleOptions getParticle() {
        return ParticleTypes.SNOWFLAKE;
    }

    @Override
    public void apply(BlockPos blockPos, ServerLevel level) {
        applyFrost(blockPos, level);
    }

    public void flintAndSteeled() {
        discard();
        for (int i = 0; i < 25; i++) {
            Vec3 pos = blockPosition().getCenter().add(random.nextGaussian() * 3, -0.5, random.nextGaussian() * 3);
            level().addParticle(ParticleTypes.SMOKE, pos.x, pos.y, pos.z, 0, random.nextFloat() * 0.2 + 0.1, 0);
        }
        level().playSound(null, BlockPos.containing(position()), SoundEvents.FLINTANDSTEEL_USE, SoundSource.NEUTRAL);
        level().playSound(null, BlockPos.containing(position()), SoundEvents.GENERIC_EXTINGUISH_FIRE, SoundSource.NEUTRAL);
    }

    public static void applyFrost(BlockPos blockPos, Level level) {
        BlockState blockState = level.getBlockState(blockPos);
        BlockState blockStateBelow = level.getBlockState(blockPos.below());
        Block block = blockState.getBlock();

        if (block.equals(Blocks.SNOW)) {
            int currentLayer = blockState.getValue(FrostSnowLayer.LAYERS);
            BlockState frostSnowState = net.woukie.createmissiles.registry.Blocks.FROST_SNOW.getDefaultState();
            frostSnowState.setValue(FrostSnowLayer.LAYERS, currentLayer);
            level.setBlock(blockPos, frostSnowState, 3);
        } else if (block.equals(net.woukie.createmissiles.registry.Blocks.FROST_SNOW.get())) {
            int currentLayer = blockState.getValue(FrostSnowLayer.LAYERS);
            if (currentLayer < 8) {
                BlockState newBlockState = blockState.setValue(FrostSnowLayer.LAYERS, currentLayer + 1);
                Block.pushEntitiesUp(blockState, newBlockState, level, blockPos);
                level.setBlockAndUpdate(blockPos, newBlockState);
            } else {
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
