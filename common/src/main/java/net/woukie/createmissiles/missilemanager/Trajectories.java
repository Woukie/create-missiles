package net.woukie.createmissiles.missilemanager;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.storage.DimensionDataStorage;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class Trajectories extends SavedData {
    public final List<Trajectory> activeTrajectories = new ArrayList<>();

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
            server.overworld().sendParticles(ParticleTypes.CLOUD, p.x + 0.5, p.y + 0.5, p.z + 0.5, 5, 0, 0, 0, 0);
            if (trajectory.shouldExplode()) {
                trajectory.explode();
            }
        });

        activeTrajectories.removeIf(Trajectory::shouldExplode);
        setDirty();
    }

    public Trajectories load(CompoundTag nbt) {
        for (int i = 0; i < nbt.size(); i++) {
            CompoundTag trajectory = nbt.getCompound("" + i);
            activeTrajectories.add(new Trajectory(new TrajectoryData(trajectory, server)));
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
