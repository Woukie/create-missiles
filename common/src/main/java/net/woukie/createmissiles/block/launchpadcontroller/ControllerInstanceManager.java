package net.woukie.createmissiles.block.launchpadcontroller;

import com.simibubi.create.foundation.utility.WorldAttached;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

import java.util.WeakHashMap;

public class ControllerInstanceManager {
    private static final WorldAttached<WeakHashMap<BlockPos, LaunchPadControllerBlockEntity>> controllers = new WorldAttached<>(world -> new WeakHashMap<>()) {};

    public static LaunchPadControllerBlockEntity get(Level world, BlockPos pos) {
        return controllers.get(world).get(pos);
    }

    public static void add(LaunchPadControllerBlockEntity be) {
        controllers.get(be.getLevel())
                .put(be.getBlockPos(), be);
    }

    public static void remove(LaunchPadControllerBlockEntity be) {
        controllers.get(be.getLevel())
                .remove(be.getBlockPos());
    }
}
