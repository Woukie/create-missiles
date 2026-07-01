package net.woukie.createmissiles.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.Vec3;
import net.woukie.createmissiles.block.InfernalAshLayer;

public class InfernalAreaEntity extends AreaEntity {
    public InfernalAreaEntity(EntityType<? extends Entity> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {

    }

    @Override
    public ParticleOptions getParticle() {
        return ParticleTypes.FLAME;
    }

    public void extingish() {
        discard();
        for (int i = 0; i < 25; i++) {
            Vec3 pos = blockPosition().getCenter().add(random.nextGaussian() * 3, -0.5, random.nextGaussian() * 3);
            level().addParticle(ParticleTypes.SMOKE, pos.x, pos.y, pos.z, 0, random.nextFloat() * 0.2 + 0.1, 0);
        }
        level().playSound(null, BlockPos.containing(position()), SoundEvents.FLINTANDSTEEL_USE, SoundSource.NEUTRAL);
        level().playSound(null, BlockPos.containing(position()), SoundEvents.GENERIC_EXTINGUISH_FIRE, SoundSource.NEUTRAL);
    }

    @Override
    public void apply(BlockPos blockPos, ServerLevel level) {
//        Can't use switch because Java 17 said so
        BlockState blockState = level.getBlockState(blockPos);
        BlockState blockStateBelow = level.getBlockState(blockPos.below());
        Block block = blockState.getBlock();
        if (block.equals(net.woukie.createmissiles.registry.Blocks.INFERNAL_ASH.get())) {
            int currentLayer = blockState.getValue(InfernalAshLayer.LAYERS);
            if (currentLayer < 8) {
                BlockState newBlockState = blockState.setValue(InfernalAshLayer.LAYERS, currentLayer + 1);
                Block.pushEntitiesUp(blockState, newBlockState, level, blockPos);
                level.setBlockAndUpdate(blockPos, newBlockState);
            } else {
                int validAshBlocks = 0;
                BlockState northBlockState = level.getBlockState(blockPos.north());
                BlockState westBlockState = level.getBlockState(blockPos.west());
                BlockState eastBlockState = level.getBlockState(blockPos.east());
                BlockState southBlockState = level.getBlockState(blockPos.south());
                Block northBlock = southBlockState.getBlock();
                Block westBlock =southBlockState.getBlock();
                Block eastBlock =southBlockState.getBlock();
                Block southBlock = southBlockState.getBlock();
                var layersNorth = northBlockState.getOptionalValue(InfernalAshLayer.LAYERS);
                var layersEast = eastBlockState.getOptionalValue(InfernalAshLayer.LAYERS);
                var layersSouth = southBlockState.getOptionalValue(InfernalAshLayer.LAYERS);
                var layersWest = westBlockState.getOptionalValue(InfernalAshLayer.LAYERS);
                if (northBlock.equals(Blocks.LAVA) || (layersNorth.isPresent() && layersNorth.get() == InfernalAshLayer.MAX_HEIGHT)) validAshBlocks++;
                if (eastBlock.equals(Blocks.LAVA) || (layersEast.isPresent() && layersEast.get() == InfernalAshLayer.MAX_HEIGHT)) validAshBlocks++;
                if (southBlock.equals(Blocks.LAVA) || (layersSouth.isPresent() && layersSouth.get() == InfernalAshLayer.MAX_HEIGHT)) validAshBlocks++;
                if (westBlock.equals(Blocks.LAVA) || (layersWest.isPresent() && layersWest.get() == InfernalAshLayer.MAX_HEIGHT)) validAshBlocks++;
                if (validAshBlocks >= 3) level.setBlockAndUpdate(blockPos, Blocks.LAVA.defaultBlockState());
            }
        } else if (block.equals(Blocks.WATER)) {
            level.setBlock(blockPos, Blocks.COBBLESTONE.defaultBlockState(), 3);
        } else if (block.equals(Blocks.ICE)) {
            level.setBlock(blockPos, Blocks.WATER.defaultBlockState(), 3);
        } else if (block.equals(Blocks.PACKED_ICE)) {
            level.setBlock(blockPos, Blocks.ICE.defaultBlockState(), 3);
        } else if (block.equals(Blocks.BLUE_ICE)) {
            level.setBlock(blockPos, Blocks.PACKED_ICE.defaultBlockState(), 3);
        } else if (block.equals(Blocks.AIR) || (blockState.canBeReplaced(Fluids.WATER)) && !block.equals(Blocks.LAVA)) {
            boolean belowUnsurviable = blockStateBelow.is(BlockTags.SNOW_LAYER_CANNOT_SURVIVE_ON);
            boolean belowSurviable = blockStateBelow.is(BlockTags.SNOW_LAYER_CANNOT_SURVIVE_ON);
            boolean middleCase = Block.isFaceFull(blockStateBelow.getCollisionShape(level, blockPos.below()), Direction.UP);
            boolean snowLayerSurvives = !belowUnsurviable && (belowSurviable || middleCase);
            if (!block.equals(Blocks.AIR)) {
                level.destroyBlock(blockPos, true);
            }
            if (snowLayerSurvives) {
                level.setBlock(blockPos, net.woukie.createmissiles.registry.Blocks.INFERNAL_ASH.get().defaultBlockState(), 3);
            } else {
                level.setBlock(blockPos, Blocks.FIRE.defaultBlockState(), 3);
            }
        }
    }
}
