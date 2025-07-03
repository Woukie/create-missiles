package net.woukie.createmissiles.block.controlpanel;

import com.simibubi.create.foundation.utility.WorldAttached;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

import java.util.WeakHashMap;

public class ControlPanelInstanceTracker {
    private static final WorldAttached<WeakHashMap<BlockPos, ControlPanelBlockEntity>> controlPanels = new WorldAttached<>(world -> new WeakHashMap<>()) {};

    public static ControlPanelBlockEntity get(Level world, BlockPos pos) {
        return controlPanels.get(world).get(pos);
    }

    public static void add(ControlPanelBlockEntity be) {
        controlPanels.get(be.getLevel())
                .put(be.getBlockPos(), be);
    }

    public static void remove(ControlPanelBlockEntity be) {
        controlPanels.get(be.getLevel())
                .remove(be.getBlockPos());
    }
}
