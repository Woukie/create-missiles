package net.woukie.createmissiles.block.navigationpanel;

import net.createmod.catnip.data.WorldAttached;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

import java.util.WeakHashMap;

public class NavigationPanelInstanceTracker {
    private static final WorldAttached<WeakHashMap<BlockPos, NavigationPanelBlockEntity>> navigationPanels = new WorldAttached<>(world -> new WeakHashMap<>()) {};

    public static NavigationPanelBlockEntity get(Level world, BlockPos pos) {
        return navigationPanels.get(world).get(pos);
    }

    public static void add(NavigationPanelBlockEntity be) {
        navigationPanels.get(be.getLevel())
                .put(be.getBlockPos(), be);
    }

    public static void remove(NavigationPanelBlockEntity be) {
        navigationPanels.get(be.getLevel())
                .remove(be.getBlockPos());
    }
}
