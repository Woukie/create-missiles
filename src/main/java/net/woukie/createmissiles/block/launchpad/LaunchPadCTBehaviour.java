package net.woukie.createmissiles.block.launchpad;

import com.simibubi.create.AllSpriteShifts;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockAndTintGetter;
import net.woukie.createmissiles.registry.SpriteShifts;
import org.jetbrains.annotations.Nullable;

import com.simibubi.create.foundation.block.connected.CTSpriteShiftEntry;
import com.simibubi.create.foundation.block.connected.ConnectedTextureBehaviour;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;

public class LaunchPadCTBehaviour extends ConnectedTextureBehaviour.Base {
    @Override
    public boolean connectsTo(BlockState state, BlockState other, BlockAndTintGetter reader, BlockPos pos, BlockPos otherPos, Direction face) {
        if (state.getBlock() != other.getBlock())
            return false;
        if (pos.getY() != otherPos.getY())
            return false;
        return super.connectsTo(state, other, reader, pos, otherPos, face);
    }

    @Override
    public CTSpriteShiftEntry getShift(BlockState state, Direction direction, @Nullable TextureAtlasSprite sprite) {
        boolean isUp = direction == Direction.UP;
        boolean isSide = direction.getAxis().isHorizontal();

        return isUp ? SpriteShifts.LAUNCH_PAD_TOP : isSide ? SpriteShifts.LAUNCH_PAD_SIDE : AllSpriteShifts.BRASS_TUNNEL_TOP;
    }
}