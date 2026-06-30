package net.woukie.createmissiles.block.assemblypanel;

import com.mojang.serialization.MapCodec;
import net.createmod.catnip.math.VoxelShaper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.woukie.createmissiles.block.AbstractBasicBlock;
import net.woukie.createmissiles.registry.BlockEntities;
import org.jetbrains.annotations.NotNull;

public class AssemblyPanelBlock extends AbstractBasicBlock<AssemblyPanelBlockEntity> {
    public static final MapCodec<AssemblyPanelBlock> CODEC = simpleCodec(AssemblyPanelBlock::new);
    public MapCodec<AssemblyPanelBlock> codec() {
        return CODEC;
    }

    private static final VoxelShape leftPillarNorth = Shapes.box(12/16.0, 0/16.0, 15/16.0, 14/16.0, 15/16.0, 16/16.0);
    private static final VoxelShape rightPillarNorth = Shapes.box(2/16.0, 0/16.0, 15/16.0, 4/16.0, 15/16.0, 16/16.0);
    private static final VoxelShape displayNorth = Shapes.box(2/16.0, 5.45/16.0, 10.275/16.0, 14/16.0, 16.925/16.0, 15.8/16.0);
    private static final VoxelShape baseBoxNorth = Shapes.box(3/16.0, 1/16.0, 13/16.0, 13/16.0, 5/16.0, 15/16.0);
    private static final VoxelShape voxelShape = Shapes.or(leftPillarNorth, rightPillarNorth, displayNorth, baseBoxNorth);

    public AssemblyPanelBlock(Properties properties) {
        super(properties);
    }

    @Override
    public BlockEntityType<? extends AssemblyPanelBlockEntity> getBlockEntityType() {
        return BlockEntities.ASSEMBLY_PANEL.get();
    }

    @Override
    @SuppressWarnings("deprecation")
    public @NotNull VoxelShape getShape(BlockState blockState, @NotNull BlockGetter blockGetter, @NotNull BlockPos blockPos, @NotNull CollisionContext collisionContext) {
        return VoxelShaper.forDirectional(voxelShape, Direction.NORTH).get(blockState.getValue(FACING));
    }

    @Override
    public Class<AssemblyPanelBlockEntity> getBlockEntityClass() {
        return AssemblyPanelBlockEntity.class;
    }
}
