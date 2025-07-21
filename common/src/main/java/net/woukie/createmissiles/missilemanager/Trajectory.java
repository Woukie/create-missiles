package net.woukie.createmissiles.missilemanager;

import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.Container;
import net.minecraft.world.level.Level;
import net.woukie.createmissiles.entity.MissileEntity;
import net.woukie.createmissiles.missilemanager.parts.ChassisType;
import net.woukie.createmissiles.missilemanager.parts.ThrusterType;
import net.woukie.createmissiles.missilemanager.parts.WarheadType;
import net.woukie.createmissiles.registry.PartTypes;
import org.joml.Vector3d;

import java.util.UUID;

/**
 * Applies logic to variables to represent a missile flight path.
 * <br>
 * Implementations of this class support instantiation from either a container or CompoundTag.
 * <br>
 * Constructed on the server when launching/loading flight paths. And on the client when rendering a trajectory in the navigator.
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

    private boolean spent;

    public Trajectory(Level level, Vector3d initialPosition, Vector3d targetPosition, WarheadType warheadType, ChassisType chassisType, ThrusterType thrusterType, Container container) {
        this.level = level;
        this.initialPosition = initialPosition;
        this.targetPosition = targetPosition;
        this.tick = 0;
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
     * Called every tick the trajectory is simulated on the server.
     * <br>
     * This method is followed by MissilePartType ticks (and if spent, entity removal, then trajectory removal)
     * @implNote in simulated approaches, here is where you tick the simulation
     * @see Trajectories#serverTick(MinecraftServer)
     */
    public void tick(MinecraftServer server) {
        tick++;
    }

    /**
     * Gets the position of the rocket in world-space at the current tick
     * @return global position of the rocket at the current tick
     */
    public abstract Vector3d getPosition();

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
    private void loadFrom(CompoundTag data, MinecraftServer server) {
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
    public void updateEntityModel(MissileEntity entity) {
        var p = getPosition();
        entity.setPos(p.x, p.y, p.z);
        entity.setWarheadBuildPercent(100);
        entity.setChassisBuildPercent(100);
        entity.setThrusterBuildPercent(100);
        entity.setWarheadType(warheadType.getResourceLocation());
        entity.setChassisType(chassisType.getResourceLocation());
        entity.setThrusterType(thrusterType.getResourceLocation());
    }

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

    public void setSpent(boolean spent) {
        this.spent = spent;
    }

    /**
     * Whether this trajectory should be unloaded and the trajectory list re-serialized
     * @return true if the trajectory should be destroyed
     */
    public boolean getSpent() {
        return this.spent;
    }

    public void setEntityId(UUID uuid) {
        this.entityId = uuid;
    }
}
