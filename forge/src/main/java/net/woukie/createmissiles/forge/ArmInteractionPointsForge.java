package net.woukie.createmissiles.forge;

import com.simibubi.create.content.kinetics.mechanicalArm.ArmInteractionPoint;
import com.simibubi.create.content.kinetics.mechanicalArm.ArmInteractionPointType;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.woukie.createmissiles.CreateMissiles;
import net.woukie.createmissiles.registry.MissileBlocks;

import java.util.function.Function;

public class ArmInteractionPointsForge {
    public static final LaunchPadType LAUNCH_PAD = register(LaunchPadType::new);

    private static <T extends ArmInteractionPointType> T register(Function<ResourceLocation, T> factory) {
        T type = factory.apply(new ResourceLocation(CreateMissiles.MOD_ID, "launch_pad"));
        ArmInteractionPointType.register(type);
        return type;
    }

    public static class LaunchPadType extends ArmInteractionPointType {
        public LaunchPadType(ResourceLocation id) {
            super(id);
        }

        @Override
        public boolean canCreatePoint(Level level, BlockPos pos, BlockState state) {
            return state.is(MissileBlocks.LAUNCH_PAD.get());
        }

        @Override
        public ArmInteractionPoint createPoint(Level level, BlockPos pos, BlockState state) {
            return new LaunchPadPoint(this, level, pos, state);
        }
    }

    public static class LaunchPadPoint extends ArmInteractionPoint {
        public LaunchPadPoint(ArmInteractionPointType type, Level level, BlockPos pos, BlockState state) {
            super(type, level, pos, state);
        }

    }

    public static void init() {

    }
}
