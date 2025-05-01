package net.woukie.createmissiles.block.launchpadcontroller;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class LaunchPadControllerBlock extends Block {
    public LaunchPadControllerBlock(Properties properties) {
        super(properties);
    }

    @Override
    public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return Shapes.or(
                Shapes.box(12/16.0, 0/16.0, 15/16.0, 14/16.0, 15/16.0, 16/16.0),
                Shapes.box(2/16.0, 0/16.0, 15/16.0, 4/16.0, 2/16.0, 16/16.0),
                Shapes.box(3/16.0, 1/16.0, 13/16.0, 13/16.0, 5/16.0, 15/16.0),
                Shapes.box(1/16.0, 9.2/16.0, 13/16.0, 15/16.0, 17.2/16.0, 15/16.0)
        );
    }
}
