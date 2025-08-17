package net.woukie.createmissiles.missilemanager.parts;

import net.minecraft.core.BlockPos;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.woukie.createmissiles.missilemanager.Trajectory;
import org.joml.Vector3d;

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
        BlockHitResult result = level.clip(new ClipContext(new Vec3(from.x, from.y, from.z), new Vec3(to.x, to.y, to.z), ClipContext.Block.OUTLINE, ClipContext.Fluid.ANY, null));
        if (result.getType() != HitResult.Type.MISS) {
            return result.getLocation();
        }
        return null;
    }
}
