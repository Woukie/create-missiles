package net.woukie.createmissiles.missiles.parts.warheads;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.phys.Vec3;
import net.woukie.createmissiles.CreateMissiles;
import net.woukie.createmissiles.missiles.Trajectory;
import net.woukie.createmissiles.missiles.asyncexplosionhandler.Explosion;
import net.woukie.createmissiles.missiles.asyncexplosionhandler.ExplosionHandler;
import net.woukie.createmissiles.missiles.parts.WarheadType;
import org.joml.Vector3d;

public class ExcavatorWarhead extends WarheadType {
    @Override
    public float getMass() {
        return 18;
    }

    @Override
    public CompoundTag saveTo(Container container, CompoundTag data) {
        data.putInt("Charges", getInitialCharges());
        data.putDouble("DetonationGap", 0);
        return data;
    }

    @Override
    public ResourceLocation getResourceLocation() {
        return new ResourceLocation(CreateMissiles.MOD_ID, "excavator_warhead");
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("warheads.createmissiles.excavator_warhead");
    }

    @Override
    public void onTick(Trajectory trajectory, MinecraftServer server) {
        ServerLevel level = server.getLevel(trajectory.getLevelKey());
        if (level == null || trajectory.getTick() <= 20) return;

        Vector3d end = trajectory.getPosition();
        if (trajectory.getWarheadData().getInt("Charges") != getInitialCharges()) {
            Vector3d start = trajectory.getLastPosition();
            detonateLine(new Vec3(start.x, start.y, start.z), new Vec3(end.x, end.y, end.z), level, trajectory);
        } else {
            Vec3 hitPosition = hitPosition(trajectory, server);
            if (trajectory.getPosition().y < level.getMinBuildHeight()) {
                hitPosition = new Vec3(trajectory.getPosition().x, trajectory.getPosition().y, trajectory.getPosition().z);
            }
            if (hitPosition != null) {
                detonateLine(hitPosition, new Vec3(end.x, end.y, end.z), level, trajectory);
            }
        }

        if (trajectory.getWarheadData().getInt("Charges") <= 0) {
            trajectory.setSpent(true);
        }
    }

    private void detonateLine(Vec3 start, Vec3 end, ServerLevel level, Trajectory trajectory) {
        int charges = trajectory.getWarheadData().getInt("Charges");
        double detonationGap = trajectory.getWarheadData().getDouble("DetonationGap");
        Vec3 totalDistance = end.subtract(start);
        Vec3 stepOffset = totalDistance.normalize().scale(getStepSize());

        Vec3 currentDistance = stepOffset.normalize().scale(detonationGap);
        while (currentDistance.length() < totalDistance.length() && charges > 0) {
            Vec3 globalPosition = currentDistance.add(start);
            level.playSound(null, BlockPos.containing(globalPosition), SoundEvents.GENERIC_EXPLODE, SoundSource.HOSTILE, 10, 1);
            ExplosionHandler.get().createExplosion(new Explosion(level, globalPosition, getExplosionPower()));
            charges--;
            trajectory.getWarheadData().putInt("Charges", charges);

            currentDistance = currentDistance.add(stepOffset);
        }

//        Offset the next to keep a constant step size across line detonations
        detonationGap = totalDistance.subtract(currentDistance).length();
        trajectory.getWarheadData().putDouble("DetonationGap", detonationGap);
    }

    protected int getInitialCharges() {
        return 30;
    }

    protected float getStepSize() {
        return 1.5f;
    }

    protected double getExplosionPower() {
        return 5;
    }
}
