package net.woukie.createmissiles.missilemanager.parts;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.phys.Vec3;
import net.woukie.createmissiles.missilemanager.Trajectory;
import org.joml.Vector3d;

import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;

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
     * Interpolates from 'current position - velocity' to 'current possition' and returns any blocks along that path
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

    //    Supercover line algorithm
    //    Stops when consumer returns true
    public static void traverseSupercover(Vector3d start, Vector3d end, Function<Vector3d, Boolean> callback) {
        Vector3d startFloor = new Vector3d(Math.floor(start.x), Math.floor(start.y), Math.floor(start.z));
        Vector3d endFloor = new Vector3d(Math.floor(end.x), Math.floor(end.y), Math.floor(end.z));

        Vector3d delta = new Vector3d(
                Math.abs(endFloor.x - startFloor.x),
                Math.abs(endFloor.y - startFloor.y),
                Math.abs(endFloor.z - startFloor.z)
        );
        Vector3d step = new Vector3d(
                Integer.compare((int) endFloor.x, (int) startFloor.x),
                Integer.compare((int) endFloor.y, (int) startFloor.y),
                Integer.compare((int) endFloor.z, (int) startFloor.z)
        );
        Vector3d current = new Vector3d(startFloor);
        Vector3d deltaDist = new Vector3d(
                delta.x == 0 ? Double.POSITIVE_INFINITY : 1.0 / Math.abs(end.x - start.x),
                delta.y == 0 ? Double.POSITIVE_INFINITY : 1.0 / Math.abs(end.y - start.y),
                delta.z == 0 ? Double.POSITIVE_INFINITY : 1.0 / Math.abs(end.z - start.z)
        );
        Vector3d maxDist = new Vector3d(
                delta.x == 0 ? Double.POSITIVE_INFINITY : deltaDist.x * ((step.x > 0 ? (startFloor.x + 1) - start.x : start.x - startFloor.x)),
                delta.y == 0 ? Double.POSITIVE_INFINITY : deltaDist.y * ((step.y > 0 ? (startFloor.y + 1) - start.y : start.y - startFloor.y)),
                delta.z == 0 ? Double.POSITIVE_INFINITY : deltaDist.z * ((step.z > 0 ? (startFloor.z + 1) - start.z : start.z - startFloor.z))
        );

        for (int i = 0; i < 1 + (int) delta.x + (int) delta.y + (int) delta.z; i++) {
            Vector3d center = new Vector3d(current).add(0.5, 0.5, 0.5);
            if (callback.apply(center)) return;

            if (maxDist.x < maxDist.y && maxDist.x < maxDist.z) {
                current.x += step.x;
                maxDist.x += deltaDist.x;
            } else if (maxDist.y < maxDist.z) {
                current.y += step.y;
                maxDist.y += deltaDist.y;
            } else {
                current.z += step.z;
                maxDist.z += deltaDist.z;
            }
        }
    }
}
