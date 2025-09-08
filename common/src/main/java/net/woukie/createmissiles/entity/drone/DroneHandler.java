package net.woukie.createmissiles.entity.drone;

import com.simibubi.create.foundation.utility.WorldAttached;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.DoubleTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.storage.DimensionDataStorage;
import net.minecraft.world.phys.Vec3;
import net.woukie.createmissiles.CreateMissiles;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class DroneHandler extends SavedData {
    private static WorldAttached<HashMap<UUID, CompoundTag>> drones = new WorldAttached<>(l -> new HashMap<>()) {};
    private static HashMap<UUID, Entity> entityCache = new HashMap<>();
    private static Set<UUID> killEntityWhenever = new HashSet<>();
    private static Set<UUID> stopTrackingNextTick = new HashSet<>();

    private static MinecraftServer server;
    private static DroneHandler instance;
    private static boolean initialized = false;
    private static boolean destroyOnSave = false;

    private DroneHandler() {}

    public static DroneHandler get() {
        if (instance == null)
            instance = new DroneHandler();
        return instance;
    }

    public void trackDrone(Drone drone) {
        drones.get(drone.level()).put(drone.getUUID(), drone.saveWithoutId(new CompoundTag()));
        setDirty();
    }

    public void stopTrackingDrone(ServerLevel level, UUID uuid) {
        drones.get(level).remove(uuid);
    }

    public void stop() {
        if (!initialized) return;
        initialized = false;
        setDirty();
        destroyOnSave = true;
    }

    public void serverTick(MinecraftServer server) {
        server.getAllLevels().forEach(serverLevel -> {
            killEntityWhenever.removeIf(uuid -> {
                Entity e = serverLevel.getEntity(uuid);
                if (e != null) {
                    e.discard();
                    entityCache.remove(e.getUUID());
                    return true;
                }
                return false;
            });

            stopTrackingNextTick.forEach(uuid -> {
                stopTrackingDrone(serverLevel, uuid);
            });

            List<UUID> changedUUID = new ArrayList<>();
            drones.get(serverLevel).forEach((uuid, tag) -> {
                if (tickDrone(serverLevel, uuid, tag)) {
                    changedUUID.add(uuid);
                }
            });

            changedUUID.forEach(uuid -> {
                var levelData = drones.get(serverLevel);
                var data = levelData.get(uuid);
                levelData.remove(uuid);
                levelData.put(data.getUUID("UUID"), data);
            });
        });
    }

//    True if uuid changed
    private boolean tickDrone(ServerLevel level, UUID uuid, CompoundTag drone) {
        Entity entity = entityCache.computeIfAbsent(uuid, uuid1 -> level.getEntity(uuid));
        boolean entityLoaded = entity != null;
        boolean ticking = entityLoaded && level.isPositionEntityTicking(entity.blockPosition());

        if (ticking) {
            CompoundTag updatedData = entity.saveWithoutId(new CompoundTag());
            updatedData.putBoolean("Simulated", false);
            updatedData.putString("id", EntityType.getKey(entity.getType()).toString());
            drones.get(level).put(uuid, updatedData);
            return false;
        }

//        Not ticking, or suddenly became unloaded, i.e player teleported away
        if ((entityLoaded || !drone.contains("Simulated") || !drone.getBoolean("Simulated"))) {
            killEntityWhenever.add(drone.getUUID("UUID"));
            entityCache.remove(uuid);
            drone.putUUID("UUID", UUID.randomUUID());
            drone.putBoolean("Simulated", true);
            return true;
        }

        simulateDroneMovement(drone);
        if (shouldBeReal(level, drone)) {
            makeReal(level, drone);
        }

        return false;
    }

    private static boolean shouldBeReal(ServerLevel level, CompoundTag drone) {
        ListTag posList = drone.getList("Pos", 6);
        BlockPos currentBlockPosition = BlockPos.containing(new Vec3(posList.getDouble(0), posList.getDouble(1), posList.getDouble(2)));
        return drone.contains("Simulated") && drone.getBoolean("Simulated") && level.isLoaded(currentBlockPosition) && level.isPositionEntityTicking(currentBlockPosition);
    }

    private static void makeReal(ServerLevel level, CompoundTag drone) {
        ListTag posList = drone.getList("Pos", 6);
        Vec3 currentPosition = new Vec3(posList.getDouble(0), posList.getDouble(1), posList.getDouble(2));
        BlockPos target = null;
        if (drone.contains("TargetBlockX")) {
            target = new BlockPos(drone.getInt("TargetBlockX"), drone.getInt("TargetBlockY"), drone.getInt("TargetBlockZ"));
        } else if (drone.contains("OriginBlockX")) {
            target = new BlockPos(drone.getInt("OriginBlockX"), drone.getInt("OriginBlockY"), drone.getInt("OriginBlockZ"));
        }

        EntityType<?> entityType = EntityType.by(drone).get();
        Entity entity = entityType.create(level);
        entity.load(drone);
        entity.setUUID(drone.getUUID("UUID"));
        if (target != null) {
            double dX = target.getX() - currentPosition.x;
            double dZ = target.getZ() - currentPosition.z;
            float angle = (float)Mth.atan2(dZ, dX);
            float targetYRot = Mth.wrapDegrees(angle * (180F / (float)Math.PI));
            entity.setYRot(targetYRot - 90.0F);
            entity.setYBodyRot(entity.getYRot());
        }
        level.addFreshEntity(entity);
        entityCache.put(drone.getUUID("UUID"), entity);
        entity.setPos(getAdjustedLoadPosition(level, currentPosition));
    }

    private static void simulateDroneMovement(CompoundTag drone) {
        BlockPos originBlock = null;
        BlockPos targetBlock = null;
        if (drone.contains("OriginBlockX")) originBlock = new BlockPos(drone.getInt("OriginBlockX"), drone.getInt("OriginBlockY"), drone.getInt("OriginBlockZ"));
        if (drone.contains("TargetBlockX")) targetBlock = new BlockPos(drone.getInt("TargetBlockX"), drone.getInt("TargetBlockY"), drone.getInt("TargetBlockZ"));

        ListTag posList = drone.getList("Pos", 6);
        Vec3 currentPosition = new Vec3(posList.getDouble(0), posList.getDouble(1), posList.getDouble(2));
        BlockPos currentTarget = targetBlock != null ? targetBlock : originBlock;
        if (currentTarget == null) {
            stopTrackingNextTick.add(drone.getUUID("UUID"));
            return;
        };

        Vec3 flatTarget = currentTarget.getCenter().multiply(1, 0, 1);
        Vec3 flatPosition = currentPosition.multiply(1, 0, 1);

        float speed = 5f;
        var delta = flatTarget.subtract(flatPosition).normalize().scale(speed);
        currentPosition = currentPosition.add(delta);
        drone.put("Pos", newDoubleList(currentPosition.x, currentPosition.y, currentPosition.z));

        if (targetBlock != null && flatPosition.distanceTo(targetBlock.getCenter().multiply(1, 0, 1)) < 5) {
            drone.remove("TargetBlockX");
            drone.remove("TargetBlockY");
            drone.remove("TargetBlockZ");
        }
        if (originBlock != null && flatPosition.distanceTo(originBlock.getCenter().multiply(1, 0, 1)) < 5) {
            drone.remove("OriginBlockX");
            drone.remove("OriginBlockY");
            drone.remove("OriginBlockZ");
        }
    }

    private static ListTag newDoubleList(double... ds) {
        ListTag listTag = new ListTag();
        for(double d : ds) listTag.add(DoubleTag.valueOf(d));
        return listTag;
    }

    private static Vec3 getAdjustedLoadPosition(ServerLevel level, Vec3 position) {
        int upper = 0, lower = 0;
        boolean hitUpper = false, hitLower = false;
        for (int i = 0; i < 50; i++) {
            if (hitLower && hitUpper) break;
            if (!hitUpper) {
                if (level.isEmptyBlock(BlockPos.containing(position.relative(Direction.UP, i)))) {
                    upper = i;
                } else {
                    hitUpper = true;
                }
            }

            if (!hitLower) {
                if (level.isEmptyBlock(BlockPos.containing(position.relative(Direction.UP, -i)))) {
                    lower = -i;
                } else {
                    hitLower = true;
                }
            }
        }

        return position.relative(Direction.UP, (upper + lower) / 2f);
    }

    public void init(MinecraftServer server) {
        if (initialized)
            return;
        initialized = true;

        DroneHandler.server = server;
        DimensionDataStorage storage = server.overworld().getDataStorage();
        storage.computeIfAbsent(this::load, () -> this, "Drones");
    }

    public DroneHandler load(CompoundTag nbt) {
        CompoundTag levels = nbt.getCompound("Drones");
        levels.getAllKeys().forEach(dimensionKey -> {
            ListTag drones = levels.getList(dimensionKey, 10);
            Level level = server.getLevel(ResourceKey.create(Registries.DIMENSION, new ResourceLocation(dimensionKey)));
            drones.forEach(tag -> {
                CompoundTag drone = (CompoundTag) tag;
                DroneHandler.drones.get(level).put(drone.getUUID("UUID"), drone);
            });
        });
        CreateMissiles.LOGGER.info("Drones loaded");
        ListTag hitList = nbt.getList("HitList", 10);

        hitList.forEach(tag -> {
            killEntityWhenever.add(UUID.fromString(tag.getAsString()));
        });

        return this;
    }

    @Override
    public @NotNull CompoundTag save(@NotNull CompoundTag compoundTag) {
        CreateMissiles.LOGGER.info("Saving drones");
        var data = new CompoundTag();
        server.getAllLevels().forEach(serverLevel -> {
            var droneListData = new ListTag();
            droneListData.addAll(drones.get(serverLevel).values());
            data.put(serverLevel.dimension().location().getPath(), droneListData);
        });

        compoundTag.put("Drones", data);
        ListTag hitList = new ListTag();
        hitList.addAll(killEntityWhenever.stream().map(uuid -> StringTag.valueOf(uuid.toString())).toList());
        compoundTag.put("HitList", hitList);

        if (destroyOnSave) {
            destroyOnSave = false;
            initialized = false;
            drones = new WorldAttached<>(l -> new HashMap<>()) {};
            entityCache = new HashMap<>();
            killEntityWhenever = new HashSet<>();
            stopTrackingNextTick = new HashSet<>();
            server = null;
            instance = null;
        }

        return compoundTag;
    }
}
