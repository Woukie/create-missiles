package net.woukie.createmissiles.missilemanager;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.storage.DimensionDataStorage;
import net.woukie.createmissiles.missilemanager.parts.ThrusterType;
import net.woukie.createmissiles.registry.PartTypes;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

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
        AtomicBoolean dirty = new AtomicBoolean(false);
        activeTrajectories.removeIf(trajectory -> {
            if (trajectory.tick(server)) {
                dirty.set(true);
            }

            if (trajectory.shouldRemove()) {
                dirty.set(true);
                return true;
            }

            return false;
        });

        if (dirty.get()) {
            setDirty();
        }
    }

    public void launch(Trajectory trajectory) {
        activeTrajectories.add(trajectory);
    }

    public Trajectories load(CompoundTag nbt) {
        for (int i = 0; i < nbt.size(); i++) {
            CompoundTag savedData = nbt.getCompound("" + i);
            ThrusterType thrusterType = (ThrusterType) PartTypes.get(new ResourceLocation(savedData.getString("Thruster")));
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
