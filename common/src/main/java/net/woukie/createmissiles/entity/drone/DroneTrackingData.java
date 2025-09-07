package net.woukie.createmissiles.entity.drone;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.phys.Vec3;

import java.util.UUID;

public class DroneTrackingData {
    private Vec3 position;
    private Vec3 simulatedPosition;
    private BlockPos targetPosition;
    private BlockPos originPosition;
    private boolean simulatingPosition;
    private UUID uuid;

    public DroneTrackingData(Vec3 position, BlockPos targetPosition, BlockPos originPosition, UUID uuid) {
        this.position = position;
        this.simulatedPosition = position;
        this.targetPosition = targetPosition;
        this.originPosition = originPosition;
        this.simulatingPosition = false;
        this.uuid = uuid;
    }

    private DroneTrackingData() {}

    public Vec3 getPosition() {
        return position;
    }

    public void setPosition(Vec3 position) {
        this.position = position;
    }

    public BlockPos getOriginPosition() {
        return originPosition;
    }

    public Vec3 getSimulatedPosition() {
        return simulatedPosition;
    }

    public void setSimulatedPosition(Vec3 simulatedPosition) {
        this.simulatedPosition = simulatedPosition;
    }

    public boolean isSimulatingPosition() {
        return simulatingPosition;
    }

    public void setSimulatingPosition(boolean simulatingPosition) {
        this.simulatingPosition = simulatingPosition;
    }

    public BlockPos getTargetPosition() {
        return targetPosition;
    }

    public UUID getUuid() {
        return uuid;
    }

    public CompoundTag save() {
        CompoundTag data = new CompoundTag();
        data.putDouble("PositionX", position.x);
        data.putDouble("PositionY", position.y);
        data.putDouble("PositionZ", position.z);
        data.putDouble("SimulatedPositionX", simulatedPosition.x);
        data.putDouble("SimulatedPositionY", simulatedPosition.y);
        data.putDouble("SimulatedPositionZ", simulatedPosition.z);
        if (targetPosition != null) {
            data.putInt("TargetPositionX", targetPosition.getX());
            data.putInt("TargetPositionY", targetPosition.getY());
            data.putInt("TargetPositionZ", targetPosition.getZ());
        }
        if (originPosition != null) {
            data.putInt("OriginPositionX", originPosition.getX());
            data.putInt("OriginPositionY", originPosition.getY());
            data.putInt("OriginPositionZ", originPosition.getZ());
        }
        data.putBoolean("SimulatingPosition", simulatingPosition);
        data.putUUID("UUID", uuid);
        return data;
    }

    public static DroneTrackingData load(CompoundTag data) {
        var drone = new DroneTrackingData();
        drone.position = new Vec3(data.getDouble("PositionX"), data.getDouble("PositionY"), data.getDouble("PositionZ"));
        drone.simulatedPosition = new Vec3(data.getDouble("SimulatedPositionX"), data.getDouble("SimulatedPositionY"), data.getDouble("SimulatedPositionZ"));
        drone.targetPosition = new BlockPos(data.getInt("TargetPositionX"), data.getInt("TargetPositionY"), data.getInt("TargetPositionZ"));
        drone.originPosition = new BlockPos(data.getInt("OriginPositionX"), data.getInt("OriginPositionY"), data.getInt("OriginPositionZ"));
        drone.simulatingPosition = data.getBoolean("SimulatingPosition");
        drone.uuid = data.getUUID("UUID");
        return drone;
    }
}
