package net.woukie.createmissiles.fabric;

import com.simibubi.create.content.kinetics.mechanicalArm.ArmInteractionPoint;
import com.simibubi.create.content.kinetics.mechanicalArm.ArmInteractionPointType;
import io.github.fabricators_of_create.porting_lib.transfer.callbacks.TransactionCallback;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.woukie.createmissiles.CreateMissiles;
import net.woukie.createmissiles.block.launchpad.LaunchPadBlock;
import net.woukie.createmissiles.registry.MissileBlocks;

import java.util.function.Function;

public class ArmInteractionPointsFabric {
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

        @Override
        public ItemStack insert(ItemStack stack, TransactionContext ctx) {
            ItemStack remainder = stack.copy();
            ItemStack toInsert = remainder.split(1);
            LaunchPadBlock block = MissileBlocks.LAUNCH_PAD.get();
            WorldlyContainer container = block.getContainer(cachedState, level, pos);
            if (!container.canPlaceItemThroughFace(0, toInsert, Direction.UP)) {
                return stack;
            }

            TransactionCallback.onSuccess(ctx, () -> container.setItem(0, toInsert));

            return remainder;
        }
    }

    public static void init() {

    }
}
