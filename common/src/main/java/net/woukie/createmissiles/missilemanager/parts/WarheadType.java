package net.woukie.createmissiles.missilemanager.parts;

import net.minecraft.core.BlockPos;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.phys.Vec3;
import net.woukie.createmissiles.missilemanager.Trajectory;
import org.joml.Vector3d;

import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;

import static net.woukie.createmissiles.Util.traverseSupercover;

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
        ServerLevel level = server.getLevel(trajectory.getLevelKey());
        Vec3 hitPosition = hitPosition(trajectory, server);
        if (level != null && trajectory.getTick() > 40 && hitPosition != null) {
            onDetonate(hitPosition, trajectory, server);
            trajectory.setSpent(true);
        }
    }

    /**
     * Interpolates from 'current position - velocity' to 'current position' and returns the position of the first block that isn't empty
     * @return BlockPos if a block is intercepted
     */
    protected Vec3 hitPosition(Trajectory trajectory, MinecraftServer server) {
        ServerLevel level = server.getLevel(trajectory.getLevelKey());
        if (level == null) return null;
        Vector3d from = trajectory.getLastPosition();
        Vector3d to = trajectory.getPosition();

        AtomicReference<Vec3> solidLocation = new AtomicReference<>();
        traverseSupercover(from, to, blockPos -> {
            if (!level.isEmptyBlock(new BlockPos((int) blockPos.x, (int) blockPos.y, (int) blockPos.z))) {
                solidLocation.set(new Vec3(blockPos.x, blockPos.y, blockPos.z));
                return true;
            }
            return false;
        });

        return solidLocation.get();
    }
}
