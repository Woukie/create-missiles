package net.woukie.createmissiles.fabric;

import com.simibubi.create.Create;
import com.simibubi.create.api.registry.CreateBuiltInRegistries;
import com.simibubi.create.content.kinetics.mechanicalArm.AllArmInteractionPointTypes;
import com.simibubi.create.content.kinetics.mechanicalArm.ArmInteractionPoint;
import com.simibubi.create.content.kinetics.mechanicalArm.ArmInteractionPointType;
import io.github.fabricators_of_create.porting_lib.transfer.callbacks.TransactionCallback;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.woukie.createmissiles.MultiblockHelper;
import net.woukie.createmissiles.block.launchpad.LaunchPadBlock;
import net.woukie.createmissiles.registry.Blocks;

public class ArmInteractionPointsFabric {
    private static <T extends ArmInteractionPointType> void register(String name, T type) {
        Registry.register(CreateBuiltInRegistries.ARM_INTERACTION_POINT_TYPE, Create.asResource(name), type);
    }

    public static class LaunchPadType extends ArmInteractionPointType {
        @Override
        public boolean canCreatePoint(Level level, BlockPos pos, BlockState state) {
            return state.is(Blocks.LAUNCH_PAD.get());
        }

        @Override
        public ArmInteractionPoint createPoint(Level level, BlockPos pos, BlockState state) {
            return new LaunchPadPoint(this, level, pos, state);
        }
    }

    public static class LaunchPadPoint extends AllArmInteractionPointTypes.DepositOnlyArmInteractionPoint {
        public BlockState state;
        public LaunchPadPoint(ArmInteractionPointType type, Level level, BlockPos pos, BlockState state) {
            super(type, level, pos, state);
            this.state = state;
        }

        @Override
        protected Vec3 getInteractionPositionVector() {
            BlockPos corner = MultiblockHelper.findCornerFromLaunchPad(getLevel(), getPos());
            if (corner != null)
                return corner.relative(Direction.NORTH, -1).relative(Direction.EAST, -1).getCenter();

            return super.getInteractionPositionVector();
        }

        @Override
        public ItemStack insert(ItemStack stack, TransactionContext ctx) {
            ItemStack remainder = stack.copy();
            ItemStack toInsert = remainder.split(1);
            LaunchPadBlock block = Blocks.LAUNCH_PAD.get();
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

    static {
        register("launch_pad", new LaunchPadType());
    }
}
