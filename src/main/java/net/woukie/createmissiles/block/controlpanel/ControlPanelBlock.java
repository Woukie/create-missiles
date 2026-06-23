package net.woukie.createmissiles.block.controlpanel;

import net.createmod.catnip.math.VoxelShaper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.woukie.createmissiles.block.AbstractBasicBlock;
import net.woukie.createmissiles.registry.BlockEntities;
import org.jetbrains.annotations.NotNull;

public class ControlPanelBlock extends AbstractBasicBlock<ControlPanelBlockEntity> {
    private static final VoxelShape leftPillarNorth = Shapes.box(12/16.0, 0/16.0, 15/16.0, 14/16.0, 15/16.0, 16/16.0);
    private static final VoxelShape rightPillarNorth = Shapes.box(2/16.0, 0/16.0, 15/16.0, 4/16.0, 15/16.0, 16/16.0);
    private static final VoxelShape displayNorth = Shapes.box(1/16.0, 9.325/16.0, 12.05/16.0, 15/16.0, 17.075/16.0, 1);
    private static final VoxelShape baseBoxNorth = Shapes.box(3/16.0, 1/16.0, 13/16.0, 13/16.0, 5/16.0, 15/16.0);
    private static final VoxelShape voxelShape = Shapes.or(leftPillarNorth, rightPillarNorth, displayNorth, baseBoxNorth);

    public ControlPanelBlock(Properties properties) {
        super(properties);
    }

    @Override
    public BlockEntityType<? extends ControlPanelBlockEntity> getBlockEntityType() {
        return BlockEntities.CONTROL_PANEL.get();
    }

    @Override
    @SuppressWarnings("deprecation")
    public @NotNull VoxelShape getShape(BlockState blockState, @NotNull BlockGetter blockGetter, @NotNull BlockPos blockPos, @NotNull CollisionContext collisionContext) {
        return VoxelShaper.forDirectional(voxelShape, Direction.NORTH).get(blockState.getValue(FACING));
    }

    @Override
    public <S extends BlockEntity> BlockEntityTicker<S> getTicker(Level level, BlockState blockState, BlockEntityType<S> type) {
        return (level1, blockPos, blockState1, blockEntity) -> {
            if (blockEntity instanceof ControlPanelBlockEntity navigationPanel) {
                navigationPanel.tick();
                if (level1.getServer() != null && !level1.isClientSide)
                    navigationPanel.serverTick();
            }
        };
    }

    @Override
    public Class<ControlPanelBlockEntity> getBlockEntityClass() {
        return ControlPanelBlockEntity.class;
    }

    @Override
    @SuppressWarnings("deprecation")
    public void neighborChanged(@NotNull BlockState blockState, Level level, @NotNull BlockPos blockPos, @NotNull Block block, @NotNull BlockPos blockPos2, boolean bl) {
        boolean powered = level.hasNeighborSignal(blockPos) || level.hasNeighborSignal(blockPos.above());
        if (powered) {
            ControlPanelBlockEntity blockEntity = (ControlPanelBlockEntity) level.getBlockEntity(blockPos);
            if (blockEntity != null) blockEntity.launch();
        }
    }
}
