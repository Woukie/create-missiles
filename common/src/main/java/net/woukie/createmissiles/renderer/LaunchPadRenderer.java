package net.woukie.createmissiles.renderer;

import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import com.simibubi.create.foundation.render.CachedBufferer;
import com.simibubi.create.foundation.render.SuperByteBuffer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.block.state.BlockState;
import net.woukie.createmissiles.block.LaunchPadBlockEntity;

public class LaunchPadRenderer extends KineticBlockEntityRenderer<LaunchPadBlockEntity> {
    public LaunchPadRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    protected SuperByteBuffer getRotatedModel(LaunchPadBlockEntity be, BlockState state) {
        return CachedBufferer.partial(AllPartialModels.MILLSTONE_COG, state);
    }
}
