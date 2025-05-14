package net.woukie.createmissiles.block.schematicator;

import com.simibubi.create.foundation.utility.VoxelShaper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.woukie.createmissiles.block.MissileAbstractBlock;
import net.woukie.createmissiles.registry.MissileBlockEntities;
import org.jetbrains.annotations.NotNull;

public class SchematicatorBlock extends MissileAbstractBlock<SchematicatorBlockEntity> {
    private static final VoxelShape leftPillarNorth = Shapes.box(12/16.0, 0/16.0, 15/16.0, 14/16.0, 15/16.0, 16/16.0);
    private static final VoxelShape rightPillarNorth = Shapes.box(2/16.0, 0/16.0, 15/16.0, 4/16.0, 15/16.0, 16/16.0);
    private static final VoxelShape displayNorth = Shapes.box(2/16.0, 5.45/16.0, 10.275/16.0, 14/16.0, 16.925/16.0, 15.8);
    private static final VoxelShape baseBoxNorth = Shapes.box(3/16.0, 1/16.0, 13/16.0, 13/16.0, 5/16.0, 15/16.0);
    private static final VoxelShape voxelShape = Shapes.or(leftPillarNorth, rightPillarNorth, displayNorth, baseBoxNorth);

    public SchematicatorBlock(Properties properties) {
        super(properties);
    }

    @Override
    public BlockEntityType<? extends SchematicatorBlockEntity> getBlockEntityType() {
        return MissileBlockEntities.SCHEMATICATOR.get();
    }

    @Override
    public @NotNull VoxelShape getShape(BlockState blockState, @NotNull BlockGetter blockGetter, @NotNull BlockPos blockPos, @NotNull CollisionContext collisionContext) {
        return VoxelShaper.forDirectional(voxelShape, Direction.NORTH).get(blockState.getValue(FACING));
    }

    @Override
    public Class<SchematicatorBlockEntity> getBlockEntityClass() {
        return SchematicatorBlockEntity.class;
    }
}
