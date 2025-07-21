package net.woukie.createmissiles.missilemanager.parts;

import net.minecraft.core.BlockPos;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
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
        var p = trajectory.getPosition();
        BlockPos blockPos = new BlockPos((int)p.x, (int)p.y, (int)p.z);

        ServerLevel level = server.getLevel(trajectory.getLevelKey());
        if (level != null && trajectory.getTick() > 200 || (trajectory.getTick() > 15 && !level.getBlockState(blockPos).isAir())) {
            onDetonate(trajectory, server);
            trajectory.setSpent(true);
        }
    }
}
