package net.woukie.createmissiles.missilemanager.parts.warheads;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.woukie.createmissiles.CreateMissiles;
import net.woukie.createmissiles.client.MissilePartModel;
import net.woukie.createmissiles.client.models.warheads.ExcavatorWarheadModel;
import net.woukie.createmissiles.missilemanager.Trajectory;
import net.woukie.createmissiles.missilemanager.asyncexplosionhandler.Explosion;
import net.woukie.createmissiles.missilemanager.asyncexplosionhandler.ExplosionHandler;
import net.woukie.createmissiles.missilemanager.parts.WarheadType;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3d;

public class ExcavatorWarhead extends WarheadType {
    protected final MissilePartModel model = new ExcavatorWarheadModel();

    @Override
    public float getMass() {
        return 18;
    }

    @Override
    public @NotNull MissilePartModel getModel() {
        return model;
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
        if (level == null || trajectory.getTick() <= 40) return;

        Vector3d end = trajectory.getPosition();
        if (trajectory.getWarheadData().getInt("Charges") != getInitialCharges()) {
            Vector3d start = trajectory.getLastPosition();
            detonateLine(new Vec3(start.x, start.y, start.z), new Vec3(end.x, end.y, end.z), level, trajectory);
        } else {
            Vec3 hitPosition = hitPosition(trajectory, server);
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
