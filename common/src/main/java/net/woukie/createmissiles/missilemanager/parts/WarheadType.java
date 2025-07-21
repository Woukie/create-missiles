package net.woukie.createmissiles.missilemanager.parts;

import net.minecraft.core.BlockPos;
import net.minecraft.server.MinecraftServer;
import net.woukie.createmissiles.missilemanager.Trajectory;

public abstract class WarheadType extends MissilePartType {
    @Override
    public int getStartSlot() {
        return 0;
    }

    @Override
    public int getEndSlot() {
        return 32;
    }

    @Override
    public void onTick(Trajectory trajectory, MinecraftServer server) {
//        var p = trajectory.getPosition();
//        BlockPos blockPos = new BlockPos((int)p.x, (int)p.y, (int)p.z);
//        System.out.println(trajectory.getTick());
//        System.out.println(trajectory.getLevel());
//        if (trajectory.getTick() > 200 || (trajectory.getTick() > 15 && !trajectory.getLevel().getBlockState(blockPos).isAir())) {
//            onDetonate(trajectory, server);
//            trajectory.setSpent(true);
//        }
//
        if (trajectory.getTick() > 200) {
            onDetonate(trajectory, server);
            trajectory.setSpent(true);
        }
    }
}
