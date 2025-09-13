package net.woukie.createmissiles.missiles;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.storage.DimensionDataStorage;
import net.woukie.createmissiles.CreateMissiles;
import net.woukie.createmissiles.entity.MissileEntity;
import net.woukie.createmissiles.missiles.parts.ThrusterType;
import net.woukie.createmissiles.registry.EntityTypes;
import net.woukie.createmissiles.registry.PartTypes;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class Trajectories extends SavedData {
    private final List<Trajectory> activeTrajectories = new ArrayList<>();
    private final HashMap<UUID, Entity> entityCache = new HashMap<>();
    private final List<UUID> killEntityWhenever = new ArrayList<>();

    private static Trajectories instance;
    private static boolean initialized = false;
    private static boolean destroyOnSave = false;
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

    public void stop() {
        if (!initialized) return;
        initialized = false;

        setDirty();
        destroyOnSave = true;
    }

    public void serverTick(MinecraftServer server) {
        killEntityWhenever.removeIf(uuid -> {
            var levels = server.getAllLevels();
            for (ServerLevel level : levels) {
                Entity e = level.getEntity(uuid);
                if (e == null) return false;
                e.discard();
                entityCache.remove(e.getUUID());
                return true;
            }
            return false;
        });

        activeTrajectories.forEach(trajectory -> {
            ServerLevel level = server.getLevel(trajectory.getLevelKey());
            if (level == null) return;

            if (trajectory.getEntityId() == null) {
                MissileEntity entity = new MissileEntity(EntityTypes.MISSILE.get(), level);
                var uuid = UUID.randomUUID();
                entity.setUUID(uuid);
                trajectory.updateEntityModel(entity);
                trajectory.setEntityId(uuid);
                level.addFreshEntity(entity);
                entityCache.put(uuid, entity);
                setDirty();
                return;
            }

            UUID uuid = trajectory.getEntityId();
            Entity entity = entityCache.computeIfAbsent(uuid, uuid1 -> level.getEntity(uuid));

//            Entity has likely been serialised TODO: unless it's been /killed
            if (entity == null || entity.isRemoved() || !entity.isAlive()) {
                killEntityWhenever.add(uuid);
                entityCache.remove(uuid);
                trajectory.setEntityId(null); // To create a new entity for it next tick
            }

            trajectory.tick(server);
            trajectory.warheadType.onTick(trajectory, server);
            trajectory.chassisType.onTick(trajectory, server);
            trajectory.thrusterType.onTick(trajectory, server);

            trajectory.updateEntityModel((MissileEntity)entity);
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
        ListTag trajectories = nbt.getList("Trajectories", 10);
        trajectories.forEach(tag -> {
            CompoundTag savedData = (CompoundTag) tag;
            System.out.println(savedData);
            ThrusterType thrusterType = (ThrusterType) PartTypes.get(new ResourceLocation(savedData.getString("ThrusterType")));
            launch(thrusterType.constructTrajectory(savedData, server));
        });

        ListTag hitList = nbt.getList("HitList", 10);
        killEntityWhenever.addAll(hitList.stream().map(tag -> UUID.fromString(tag.toString())).toList());
        CreateMissiles.LOGGER.info("Trajectories loaded");

        return this;
    }

    @Override
    public @NotNull CompoundTag save(@NotNull CompoundTag compoundTag) {
        ListTag trajectories = new ListTag();
        for (Trajectory trajectory : activeTrajectories) {
            CreateMissiles.LOGGER.info("Saving trajectory at {}", trajectory.position);
            trajectories.add(trajectory.saveTo(new CompoundTag()));
        }

        ListTag hitList = new ListTag();
        hitList.addAll(killEntityWhenever.stream().map(uuid -> StringTag.valueOf(uuid.toString())).toList());

        compoundTag.put("Trajectories", trajectories);
        compoundTag.put("HitList", hitList);

        if (destroyOnSave) {
            destroyOnSave = false;
            initialized = false;
            server = null;
            activeTrajectories.clear();
            entityCache.clear();
            killEntityWhenever.clear();
            instance = null;
        }

        return compoundTag;
    }
}
