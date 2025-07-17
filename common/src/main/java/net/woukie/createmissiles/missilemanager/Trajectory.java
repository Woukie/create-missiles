package net.woukie.createmissiles.missilemanager;

import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.woukie.createmissiles.entity.MissileEntity;
import net.woukie.createmissiles.missilemanager.parts.ChassisType;
import net.woukie.createmissiles.missilemanager.parts.ThrusterType;
import net.woukie.createmissiles.missilemanager.parts.WarheadType;
import net.woukie.createmissiles.registry.EntityTypes;
import net.woukie.createmissiles.registry.PartTypes;
import org.joml.Vector3d;

import java.util.UUID;

/**
 * Applies logic to variables to represent a missile flight path, namely controlling velocity, global position and rotation.
 * <br>
 * Constructed when in motion on the server. And when rendering a trajectory in the navigator on the client.
 */
public abstract class Trajectory {
    protected Level level;
    protected int tick;
    protected UUID entityId;
    protected WarheadType warheadType;
    protected ChassisType chassisType;
    protected ThrusterType thrusterType;
    protected Vector3d initialPosition, targetPosition;
    protected CompoundTag warheadData;
    protected CompoundTag chassisData;
    protected CompoundTag thrusterData;

    public Trajectory() {
    }

    public Trajectory(Level level, Vector3d initialPosition, Vector3d targetPosition, WarheadType warheadType, ChassisType chassisType, ThrusterType thrusterType, Container container) {
        this.level = level;
        this.initialPosition = initialPosition;
        this.targetPosition = targetPosition;
        this.warheadType = warheadType;
        this.chassisType = chassisType;
        this.thrusterType = thrusterType;
        this.warheadData = warheadType.saveTo(container, new CompoundTag());
        this.chassisData = chassisType.saveTo(container, new CompoundTag());
        this.thrusterData = thrusterType.saveTo(container, new CompoundTag());
    }

    public Trajectory(CompoundTag data, MinecraftServer server) {
        loadFrom(data, server);
    }

    /**
     * Called every tick the trajectory is simulated on the server
     * @implNote in simulated approaches, here is where you tick the simulation
     * @see Trajectories#serverTick(MinecraftServer)
     * @return whether the trajectories should be saved to disk
     */
    public boolean tick(MinecraftServer server) {
        if (level != null) {
            tick++;
            Entity entity = ((ServerLevel) level).getEntity(getEntityId());
            if (entity == null || !entity.getType().equals(EntityTypes.MISSILE.get())) {
                entity = new MissileEntity(EntityTypes.MISSILE.get(), level);

                updateEntityModel((MissileEntity) entity);

                level.addFreshEntity(entity);

                setEntityId(entity.getUUID());
                return true;
            }
        }

        return false;
    }

    /**
     * Gets the position of the rocket in world-space at the current tick
     * @return global position of the rocket at the current tick
     */
    public abstract Vec3 getPosition();

    /**
     * Saves trajectory data to a compound tag in a way where the whole object can be reconstructed in <code>loadFrom()</code>
     * @param data tag to fill trajectory data with
     * @see Trajectory#loadFrom(CompoundTag, MinecraftServer)
     */
    public CompoundTag saveTo(CompoundTag data) {
        data.putString("Dimension", level.dimension().location().getPath());
        data.putUUID("EntityID", entityId);
        data.putInt("Tick", tick);
        data.putDouble("InitialPositionX", initialPosition.x);
        data.putDouble("InitialPositionY", initialPosition.y);
        data.putDouble("InitialPositionZ", initialPosition.z);
        data.putDouble("TargetPositionX", targetPosition.x);
        data.putDouble("TargetPositionY", targetPosition.y);
        data.putDouble("TargetPositionZ", targetPosition.z);
        data.putString("WarheadType", warheadType.getResourceLocation().toString());
        data.putString("ChassisType", chassisType.getResourceLocation().toString());
        data.putString("ThrusterType", thrusterType.getResourceLocation().toString());
        data.put("WarheadData", warheadData);
        data.put("ChassisData", chassisData);
        data.put("ThrusterData", thrusterData);
        return data;
    }

    /**
     * Fill the trajectory with data from the provided compound tag
     * @param data to load the trajectory with
     * @param server the calling server instance used to access the level
     * @see Trajectory#saveTo(CompoundTag)
     */
    public void loadFrom(CompoundTag data, MinecraftServer server) {
        String dimension = data.getString("Dimension");
        ResourceKey<Level> dimensionKey = ResourceKey.create(Registries.DIMENSION, new ResourceLocation(dimension));

        level = server.getLevel(dimensionKey);
        entityId = data.hasUUID("EntityID") ? data.getUUID("EntityID") : null;
        tick = data.getInt("Tick");
        warheadType = (WarheadType) PartTypes.get(new ResourceLocation(data.getString("WarheadType")));
        chassisType = (ChassisType) PartTypes.get(new ResourceLocation(data.getString("ChassisType")));
        thrusterType = (ThrusterType) PartTypes.get(new ResourceLocation(data.getString("ThrusterType")));
        initialPosition = new Vector3d(data.getDouble("InitialPositionX"), data.getDouble("InitialPositionY"), data.getDouble("InitialPositionZ"));
        targetPosition = new Vector3d(data.getDouble("TargetPositionX"), data.getDouble("TargetPositionY"), data.getDouble("TargetPositionZ"));
        warheadData = data.getCompound("WarheadData");
        chassisData = data.getCompound("ChassisData");
        thrusterData = data.getCompound("ThrusterData");
    }

    /**
     * Update the entity model with visual changes about the missile like setting position, build percent and part types
     * <br>
     * @implNote called every frame
     * @param entity the entity associated with this trajectory
     */
    public abstract void updateEntityModel(MissileEntity entity);

    public UUID getEntityId() {
        return entityId;
    }

    public Level getLevel() {
        return level;
    }

    public WarheadType getWarheadType() {
        return warheadType;
    }

    public ChassisType getChassisType() {
        return chassisType;
    }

    public ThrusterType getThrusterType() {
        return thrusterType;
    }

    public Vector3d getTargetPosition() {
        return targetPosition;
    }

    public Vector3d getInitialPosition() {
        return initialPosition;
    }

    public int getTick() {
        return tick;
    }

    public CompoundTag getWarheadData() {
        return warheadData;
    }

    public CompoundTag getChassisData() {
        return chassisData;
    }

    public CompoundTag getThrusterData() {
        return thrusterData;
    }

    public void setEntityId(UUID uuid) {
        entityId = uuid;
    }

    /**
     * Whether this trajectory should be unloaded and the trajectory list re-serialized
     * @return true if the trajectory should be destroyed
     */
    public abstract boolean shouldRemove();
}
