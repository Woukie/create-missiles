package net.woukie.createmissiles.entity.drone;

import com.simibubi.create.foundation.utility.WorldAttached;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.storage.DimensionDataStorage;
import net.minecraft.world.phys.Vec3;
import net.woukie.createmissiles.CreateMissiles;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class DroneHandler extends SavedData {
    private static WorldAttached<HashSet<CompoundTag>> drones = new WorldAttached<>(l -> new HashSet<>()) {};
    Set<UUID> loadChunkAtLastPosition = new HashSet<>();
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
        System.out.println("TRACKING DRONE " + drone.getUUID());
        System.out.println(drone);
        setDirty();
    }

    public void stop() {
        if (!initialized) return;
        initialized = false;
        setDirty();
        destroyOnSave = true;
    }

    public void serverTick(MinecraftServer server) {
        server.getAllLevels().forEach(serverLevel -> {
            drones.get(serverLevel).forEach(drone -> {
                tickDrone(serverLevel, drone);
            });
        });
    }

    private void tickDrone(ServerLevel level, DroneTrackingData drone) {
        Entity entity = level.getEntity(drone.getUuid());

        boolean foundEntity = entity != null;
        boolean ticking = foundEntity && level.isPositionEntityTicking(entity.blockPosition());

        if (ticking) {

        }
    }

    private void tickDrone(ServerLevel level, DroneTrackingData drone, int a) {
        Entity entity = level.getEntity(drone.getUuid());
        boolean foundEntity = entity != null;
        boolean inStasis = foundEntity && !level.isPositionEntityTicking(entity.blockPosition());

        if (loadChunkAtLastPosition.contains(drone.getUuid()) && level.isLoaded(BlockPos.containing(drone.getPosition()))) {
            var chunk = new ChunkPos(BlockPos.containing(drone.getPosition()));
            level.setChunkForced(chunk.x, chunk.z, false);
            loadChunkAtLastPosition.remove(drone.getUuid());
            System.out.println("TELEPORTED!!!");
        }

        if (foundEntity && !inStasis) {
            var dronePos = entity.position();
            if (drone.isSimulatingPosition()) {
                dronePos = getAdjustedLoadPosition(level, drone.getSimulatedPosition());
                entity.setPos(dronePos);
                drone.setSimulatingPosition(false);
            }

            drone.setPosition(dronePos);
            drone.setSimulatedPosition(dronePos);

            System.out.println("REAL: " + entity.position());
        } else {
            drone.setSimulatingPosition(true);
            boolean teleportToSimulated = simulateDroneMovement(drone) || inStasis || level.isLoaded(BlockPos.containing(drone.getSimulatedPosition()));
            if (teleportToSimulated) {
                loadChunkAtLastPosition.add(drone.getUuid());
                var chunk = new ChunkPos(BlockPos.containing(drone.getPosition()));
                level.setChunkForced(chunk.x, chunk.z, true);
                System.out.println("needs teleporting (still?)");
            }
            System.out.println("SIMULATED: " + drone.getSimulatedPosition());
        }
    }

    private static boolean simulateDroneMovement(DroneTrackingData drone) {
        BlockPos target = drone.getTargetPosition() != null ? drone.getTargetPosition() : drone.getOriginPosition();
        if (target == null) return false;

        Vec3 flatTarget = target.getCenter().multiply(1, 0, 1);
        Vec3 flatPosition = new Vec3(drone.getSimulatedPosition().toVector3f()).multiply(1, 0, 1);

        float speed = 10f;
        var delta = flatTarget.subtract(flatPosition).normalize().scale(speed);
        drone.setSimulatedPosition(drone.getSimulatedPosition().add(delta));

        return flatPosition.distanceTo(flatTarget) < 10;
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
        nbt.getAllKeys().forEach(dimensionKey -> {
            Level level = server.getLevel(ResourceKey.create(Registries.DIMENSION, new ResourceLocation(dimensionKey)));
            ListTag droneData = (ListTag) nbt.get(dimensionKey);
            if (droneData != null) {
                droneData.forEach(tag -> {
                    drones.get(level).add(DroneTrackingData.load((CompoundTag)tag));
                });
            }
        });
        CreateMissiles.LOGGER.info("Drones loaded");
        return this;
    }

    @Override
    public @NotNull CompoundTag save(@NotNull CompoundTag compoundTag) {
        CreateMissiles.LOGGER.info("Saving drones");
        var data = new CompoundTag();
        server.getAllLevels().forEach(serverLevel -> {
            var droneListData = new ListTag();
            droneListData.addAll(drones.get(serverLevel).stream().map(DroneTrackingData::save).toList());
            data.put(serverLevel.dimension().toString(), droneListData);
        });

        if (destroyOnSave) {
            destroyOnSave = false;
            initialized = false;
            drones = new WorldAttached<>(l -> new HashSet<>()) {};
            loadChunkAtLastPosition = new HashSet<>();
            server = null;
            instance = null;
        }

        return compoundTag;
    }
}
