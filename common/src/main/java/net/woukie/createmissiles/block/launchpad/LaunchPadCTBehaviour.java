package net.woukie.createmissiles.block.launchpad;

import net.woukie.createmissiles.registry.SpriteShifts;
import org.jetbrains.annotations.Nullable;

import com.simibubi.create.foundation.block.connected.CTSpriteShiftEntry;
import com.simibubi.create.foundation.block.connected.ConnectedTextureBehaviour;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;

public class LaunchPadCTBehaviour extends ConnectedTextureBehaviour.Base {
    @Override
    public CTSpriteShiftEntry getShift(BlockState state, Direction direction, @Nullable TextureAtlasSprite sprite) {
        boolean isUp = direction == Direction.UP;
        boolean isSide = direction.getAxis().isHorizontal();

        return isUp ? SpriteShifts.LAUNCH_PAD_TOP : isSide ? SpriteShifts.LAUNCH_PAD_SIDE : SpriteShifts.LAUNCH_PAD_BOTTOM;
    }
}