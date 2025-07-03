package net.woukie.createmissiles.block.navigator;

import com.simibubi.create.foundation.utility.WorldAttached;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

import java.util.WeakHashMap;

public class NavigatorInstanceTracker {
    private static final WorldAttached<WeakHashMap<BlockPos, NavigatorBlockEntity>> navigators = new WorldAttached<>(world -> new WeakHashMap<>()) {};

    public static NavigatorBlockEntity get(Level world, BlockPos pos) {
        return navigators.get(world).get(pos);
    }

    public static void add(NavigatorBlockEntity be) {
        navigators.get(be.getLevel())
                .put(be.getBlockPos(), be);
    }

    public static void remove(NavigatorBlockEntity be) {
        navigators.get(be.getLevel())
                .remove(be.getBlockPos());
    }
}
