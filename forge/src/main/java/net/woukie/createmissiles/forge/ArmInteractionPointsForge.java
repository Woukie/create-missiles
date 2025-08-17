package net.woukie.createmissiles.forge;

import com.simibubi.create.content.kinetics.mechanicalArm.AllArmInteractionPointTypes;
import com.simibubi.create.content.kinetics.mechanicalArm.ArmInteractionPoint;
import com.simibubi.create.content.kinetics.mechanicalArm.ArmInteractionPointType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.SidedInvWrapper;
import net.woukie.createmissiles.CreateMissiles;
import net.woukie.createmissiles.MultiblockHelper;
import net.woukie.createmissiles.block.assemblypanel.AssemblyPanelBlock;
import net.woukie.createmissiles.block.assemblypanel.AssemblyPanelBlockEntity;
import net.woukie.createmissiles.block.launchpad.LaunchPadBlock;
import net.woukie.createmissiles.registry.BlockEntities;
import net.woukie.createmissiles.registry.Blocks;

import javax.annotation.Nullable;
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
            return state.is(Blocks.LAUNCH_PAD.get());
        }

        @Override
        public ArmInteractionPoint createPoint(Level level, BlockPos pos, BlockState state) {
            return new LaunchPadPoint(this, level, pos, state);
        }
    }

    public static class LaunchPadPoint extends AllArmInteractionPointTypes.DepositOnlyArmInteractionPoint {
        public LaunchPadPoint(ArmInteractionPointType type, Level level, BlockPos pos, BlockState state) {
            super(type, level, pos, state);
        }

        @Override
        public void updateCachedState() {
            BlockState oldState = cachedState;
            super.updateCachedState();
            if (oldState != cachedState)
                cachedHandler.invalidate();
        }

        @Nullable
        @Override
        protected IItemHandler getHandler() {
            return null;
        }

        protected WorldlyContainer getContainer() {
            LaunchPadBlock block = Blocks.LAUNCH_PAD.get();
            return block.getContainer(cachedState, level, pos);
        }

        @Override
        protected Vec3 getInteractionPositionVector() {
            BlockPos corner = MultiblockHelper.findCornerFromLaunchPad(getLevel(), getPos());
            if (corner != null)
                return corner.relative(Direction.NORTH, -1).relative(Direction.EAST, -1).getCenter();

            return super.getInteractionPositionVector();
        }

        @Override
        public ItemStack insert(ItemStack stack, boolean simulate) {
            ItemStack remainder = stack.copy();
            ItemStack toInsert = remainder.split(1);
            LaunchPadBlock block = Blocks.LAUNCH_PAD.get();
            WorldlyContainer container = block.getContainer(cachedState, level, pos);
                if (!container.canPlaceItemThroughFace(0, toInsert, Direction.UP)) {
                return stack;
            }

            IItemHandler handler = new SidedInvWrapper(getContainer(), Direction.UP);
            handler.insertItem(0, toInsert, simulate);
            return remainder;
        }
    }

    public static void init() {

    }
}
