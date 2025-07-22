package net.woukie.createmissiles.missilemanager;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.storage.DimensionDataStorage;
import net.woukie.createmissiles.entity.MissileEntity;
import net.woukie.createmissiles.missilemanager.parts.ThrusterType;
import net.woukie.createmissiles.registry.EntityTypes;
import net.woukie.createmissiles.registry.PartTypes;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

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
            ServerLevel level = server.getLevel(trajectory.getLevelKey());
            if (level != null && trajectory.getEntityId() == null) {
                MissileEntity entity = new MissileEntity(EntityTypes.MISSILE.get(), level);
                trajectory.updateEntityModel(entity);
                level.addFreshEntity(entity);
                trajectory.setEntityId(entity.getUUID());
                setDirty();
                return;
            }

            trajectory.tick(server);
            trajectory.warheadType.onTick(trajectory, server);
            trajectory.chassisType.onTick(trajectory, server);
            trajectory.thrusterType.onTick(trajectory, server);

            if (level != null) {
                MissileEntity entity = (MissileEntity) level.getEntity(trajectory.getEntityId());
                trajectory.updateEntityModel(entity);
            }
        });

        activeTrajectories.removeIf(trajectory -> {
            if (!trajectory.getSpent()) return false;

            ServerLevel level = server.getLevel(trajectory.getLevelKey());
            if (level != null) {
                Entity entity = level.getEntity(trajectory.getEntityId());
                if (entity != null) {
                    entity.remove(Entity.RemovalReason.KILLED);
                }
            }
            setDirty();
            return true;
        });
    }

    public void launch(Trajectory trajectory) {
        activeTrajectories.add(trajectory);
    }

    public Trajectories load(CompoundTag nbt) {
        for (int i = 0; i < nbt.size(); i++) {
            CompoundTag savedData = nbt.getCompound("" + i);
            ThrusterType thrusterType = (ThrusterType) PartTypes.get(new ResourceLocation(savedData.getString("ThrusterType")));
            launch(thrusterType.serializeTrajectory(savedData, server));
        }

        return this;
    }

    @Override
    public @NotNull CompoundTag save(@NotNull CompoundTag compoundTag) {
        for (int i = 0; i < activeTrajectories.size(); i++) {
            Trajectory trajectory = activeTrajectories.get(i);
            compoundTag.put("" + i, trajectory.saveTo(new CompoundTag()));
        }

        return compoundTag;
    }
}
