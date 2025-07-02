package net.woukie.createmissiles.missilemanager;

import net.minecraft.core.Rotations;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.storage.DimensionDataStorage;
import net.minecraft.world.phys.Vec3;
import net.woukie.createmissiles.entity.MissileEntity;
import net.woukie.createmissiles.registry.EntityTypes;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Trajectories extends SavedData {
    private final List<Trajectory> activeTrajectories = new ArrayList<>();

    private static Trajectories instance;
    private static boolean initialized = false;
    private static MinecraftServer server;

    private Trajectories() {}

    public static Trajectories get() {
        if (instance == null)
            instance = new Trajectories();
        return instance;
    }

    public void init(MinecraftServer server) {
        if (initialized)
            return;
        initialized = true;

        Trajectories.server = server;
        DimensionDataStorage storage = server.overworld().getDataStorage();
        storage.computeIfAbsent(this::load, () -> this, "trajectory");
    }

    public void serverTick(MinecraftServer server) {
        activeTrajectories.forEach(trajectory -> {
            trajectory.incrementTick();
            Vec3 p = trajectory.getPosition(trajectory.getData().getTick() * 0.05F);

            ServerLevel level = (ServerLevel) trajectory.getData().level;
            if (level != null) {

                Entity entity = level.getEntity(trajectory.getData().getEntityId());
                if (entity == null || !entity.getType().equals(EntityTypes.MISSILE.get())) {
                    entity = new MissileEntity(EntityTypes.MISSILE.get(), level);
                    entity.setPos(p.x + 0.5, p.y + 0.5, p.z + 0.5);

                    MissileEntity missileEntity = (MissileEntity) entity;
                    missileEntity.setWarheadBuildPercent(100);
                    missileEntity.setChassisBuildPercent(100);
                    missileEntity.setThrusterBuildPercent(100);
                    missileEntity.setWarheadType(trajectory.getData().warheadType.resourceLocation);
                    missileEntity.setChassisType(trajectory.getData().chassisType.resourceLocation);
                    missileEntity.setThrusterType(trajectory.getData().thrusterType.resourceLocation);

                    level.addFreshEntity(entity);

                    trajectory.getData().setEntityId(entity.getUUID());
                    setDirty();
                }

                MissileEntity missileEntity = (MissileEntity) entity;

                missileEntity.setPos(p.x + 0.5, p.y + 0.5, p.z + 0.5);
                var rotation = missileEntity.getRotation();
                missileEntity.setRotation(new Rotations(rotation.getX() + 0.05f, rotation.getY() + 0.05f, rotation.getZ() + 0.05f));

                level.sendParticles(ParticleTypes.CLOUD, p.x + 0.5, p.y + 0.5, p.z + 0.5, 5, 0, 0, 0, 0);
                if (trajectory.shouldExplode()) {
                    trajectory.explode(server);
                }
            }
        });

        activeTrajectories.removeIf(trajectory -> {
            var remove = trajectory.shouldExplode();
            if (remove) {
                ServerLevel level = (ServerLevel) trajectory.getData().level;
                if (level == null) return true;
                var entity = level.getEntity(trajectory.getData().getEntityId());
                if (entity != null && entity.getType().equals(EntityTypes.MISSILE.get())) {
                    entity.remove(Entity.RemovalReason.KILLED);
                }
            }
            return remove;
        });

        setDirty();
    }

    public void launch(Trajectory trajectory) {
        activeTrajectories.add(trajectory);
    }

    public Trajectories load(CompoundTag nbt) {
        for (int i = 0; i < nbt.size(); i++) {
            CompoundTag trajectory = nbt.getCompound("" + i);
            launch(new Trajectory(new TrajectoryData(trajectory, server)));
        }

        return this;
    }

    @Override
    public @NotNull CompoundTag save(@NotNull CompoundTag compoundTag) {
        for (int i = 0; i < activeTrajectories.size(); i++) {
            Trajectory trajectory = activeTrajectories.get(i);
            compoundTag.put("" + i, trajectory.getData().saveTo(new CompoundTag()));
        }

        return compoundTag;
    }
}
