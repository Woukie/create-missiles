package net.woukie.createmissiles.missilemanager;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.UUID;

/**
 * Applies logic to variables to represent a missile flight path, namely controlling velocity, global position and rotation.
 * <br>
 * Constructed when in motion on the server. And when rendering a trajectory in the navigator on the client.
 */
public abstract class Trajectory {
    private Level level;
    private UUID entityId;

    /**
     * Construct a trajectory with the data in the tag provided
     * @param data to load the trajectory with
     * @return the constructed trajectory
     */
    public Trajectory(CompoundTag data, MinecraftServer server) {
        String dimension = data.getString("Dimension");
        ResourceKey<Level> dimensionKey = ResourceKey.create(Registries.DIMENSION, new ResourceLocation(dimension));

        this.level = server.getLevel(dimensionKey);
        this.entityId = data.hasUUID("EntityID") ? data.getUUID("EntityID") : null;
    }

    /**
     * Called every tick the trajectory is simulated on the server
     * @implNote in simulated approaches, here is where you simulate path
     * @see Trajectories#serverTick(MinecraftServer)
     */
    public abstract void tick();

    /**
     * Gets the position of the rocket in world-space at the current tick
     * @return global position of the rocket at the current tick
     */
    public abstract Vec3 getPosition();

    /**
     * @return True if the missile should detonate at the current tick
     */
    public abstract boolean shouldExplode();

    /**
     * Saves trajectory data to a compound tag in a way where the whole object can be reconstructed in the constructor
     * @param data tag to fill trajectory data with
     */
    public void saveTo(CompoundTag data, MinecraftServer server) {
        String dimension = data.getString("Dimension");
        ResourceKey<Level> dimensionKey = ResourceKey.create(Registries.DIMENSION, new ResourceLocation(dimension));
        this.level = server.getLevel(dimensionKey);
    }

    public UUID getEntityId() {
        return this.entityId;
    }

    public Level getLevel() {
        return this.level;
    }
}
