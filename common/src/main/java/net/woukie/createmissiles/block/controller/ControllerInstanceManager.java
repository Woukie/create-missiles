package net.woukie.createmissiles.block.controller;

import com.simibubi.create.foundation.utility.WorldAttached;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

import java.util.WeakHashMap;

public class ControllerInstanceManager {
    private static final WorldAttached<WeakHashMap<BlockPos, ControllerBlockEntity>> controllers = new WorldAttached<>(world -> new WeakHashMap<>()) {};

    public static ControllerBlockEntity get(Level world, BlockPos pos) {
        return controllers.get(world).get(pos);
    }

    public static void add(ControllerBlockEntity be) {
        controllers.get(be.getLevel())
                .put(be.getBlockPos(), be);
    }

    public static void remove(ControllerBlockEntity be) {
        controllers.get(be.getLevel())
                .remove(be.getBlockPos());
    }
}
