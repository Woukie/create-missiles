package net.woukie.createmissiles.missilemanager;

import net.minecraft.server.MinecraftServer;
import net.minecraft.world.phys.Vec3;

/**
 * Calculates a flight path based on data from serializable TrajectoryData.
 * <br>
 * Constructed when in motion on the server. And when rendering a trajectory in the navigator on the client
 */
public abstract class Trajectory {
    public final TrajectoryData data;

    /**
     * Create trajectory from serializable data
     * @param data serializable trajectory data
     */
    public Trajectory(TrajectoryData data) {
        this.data = data;
    }

    /**
     * Increments the trajectories serialized tick number
     * <br>
     * Called every tick the trajectory is simulated on the server
     * @implNote if using a simulated approach, here is where you simulate path
     * @see Trajectories#serverTick(MinecraftServer)
     */
    public void incrementTick() {
        this.data.incrementTick();
    }

    /**
     * Gets the position of the projectile in world-space at a given time
     * @implNote For client-side GUI rendering
     * @param second time in seconds since launch
     * @return global position of the missile at the given time
     */
    public abstract Vec3 getPosition(float second);

    /**
     * Gets the position of the rocket in world-space at the current tick as given in the data
     * @return global position of the rocket at the current tick
     */
    public abstract Vec3 getPosition();

    /**
     * Gets how long the flight will take from launch to impact
     * @return flight time in seconds
     */
    public abstract double getFlightTime();

    /**
     * @implNote For client-side GUI rendering
     * @return True if the trajectory intersects with the target block
     */
    public abstract boolean canHitTarget();

    /**
     * @return True if the missile should detonate at the current tick
     */
    public abstract boolean shouldExplode();
}
