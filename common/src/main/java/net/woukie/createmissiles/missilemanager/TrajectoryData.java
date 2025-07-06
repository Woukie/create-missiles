package net.woukie.createmissiles.missilemanager;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.Container;
import net.minecraft.world.level.Level;
import net.woukie.createmissiles.missilemanager.parts.ChassisType;
import net.woukie.createmissiles.registry.PartTypes;
import net.woukie.createmissiles.missilemanager.parts.ThrusterType;
import net.woukie.createmissiles.missilemanager.parts.WarheadType;

import java.util.UUID;

public class TrajectoryData {
    public final double gravity = 9.81F;

    public final Level level;
    public final BlockPos source, target;
    public final double fuelPercentage;
    private UUID entityId;
    private int tick; // Ticks incremented since launch

    public final WarheadType warheadType;
    public final ChassisType chassisType;
    public final ThrusterType thrusterType;

    public final CompoundTag warheadData;
    public final CompoundTag chassisData;
    public final CompoundTag thrusterData;

//    Construct with data from container (intended for warhead with custom payload derrived from recipe ingredient, but we also support data from thruster and chassis)
    public TrajectoryData(Level level, BlockPos source, BlockPos target, double fuelPercentage, int tick, WarheadType warheadType, ChassisType chassisType, ThrusterType thrusterType, Container container) {
        this.level = level;
        this.source = source;
        this.target = target;
        this.fuelPercentage = fuelPercentage;
        this.tick = tick;

        this.warheadType = warheadType;
        this.chassisType = chassisType;
        this.thrusterType = thrusterType;

        this.warheadData = warheadType.writeData != null ? warheadType.writeData.write(container, new CompoundTag()) : null;
        this.chassisData = chassisType.writeData != null ? chassisType.writeData.write(container, new CompoundTag()) : null;
        this.thrusterData = thrusterType.writeData != null ? thrusterType.writeData.write(container, new CompoundTag()) : null;
    }

//    Construct without data from container
    public TrajectoryData(Level level, BlockPos source, BlockPos target, double fuelPercentage, int tick, WarheadType warheadType, ChassisType chassisType, ThrusterType thrusterType) {
        this.level = level;
        this.source = source;
        this.target = target;
        this.fuelPercentage = fuelPercentage;
        this.tick = tick;

        this.warheadType = warheadType;
        this.chassisType = chassisType;
        this.thrusterType = thrusterType;

        this.warheadData = new CompoundTag();
        this.chassisData = new CompoundTag();
        this.thrusterData = new CompoundTag();
    }

    public UUID getEntityId() {
        return this.entityId;
    }

    public void setEntityId(UUID uuid) {
        this.entityId = uuid;
    }

//    Construct by loading from disk
    public TrajectoryData(CompoundTag savedData, MinecraftServer server) {
        String dimension = savedData.getString("Dimension");
        ResourceKey<Level> dimensionKey = ResourceKey.create(Registries.DIMENSION, new ResourceLocation(dimension));
        this.level = server.getLevel(dimensionKey);

        this.source = new BlockPos(savedData.getInt("SourceX"), savedData.getInt("SourceY"), savedData.getInt("SourceZ"));
        this.target = new BlockPos(savedData.getInt("TargetX"), savedData.getInt("TargetY"), savedData.getInt("TargetZ"));
        this.fuelPercentage = savedData.getDouble("FuelPercentage");
        this.tick = savedData.getInt("Tick");

        this.warheadType = (WarheadType) PartTypes.get(new ResourceLocation(savedData.getString("Warhead")));
        this.chassisType = (ChassisType) PartTypes.get(new ResourceLocation(savedData.getString("Chassis")));
        this.thrusterType = (ThrusterType) PartTypes.get(new ResourceLocation(savedData.getString("Thruster")));

        this.warheadData =  savedData.getCompound("WarheadData");
        this.chassisData = savedData.getCompound("ChassisData");
        this.thrusterData = savedData.getCompound("ThrusterData");

        this.entityId = savedData.contains("EntityID") ? savedData.getUUID("EntityID") : null;
    }

    public CompoundTag saveTo(CompoundTag tag) {
        tag.putString("Dimension", this.level.dimension().location().getPath());
        tag.putInt("SourceX", this.source.getX());
        tag.putInt("SourceY", this.source.getY());
        tag.putInt("SourceZ", this.source.getZ());
        tag.putInt("TargetX", this.target.getX());
        tag.putInt("TargetY", this.target.getY());
        tag.putInt("TargetZ", this.target.getZ());
        tag.putDouble("FuelPercentage", this.fuelPercentage);
        tag.putInt("Tick", this.tick);

        tag.putString("Warhead", warheadType.resourceLocation.toString());
        tag.putString("Chassis", chassisType.resourceLocation.toString());
        tag.putString("Thruster", thrusterType.resourceLocation.toString());

        if (warheadData != null) tag.put("WarheadData", warheadData);
        if (chassisData != null) tag.put("ChassisData", chassisData);
        if (thrusterData != null) tag.put("ThrusterData", thrusterData);

        tag.putUUID("EntityID", entityId);

        return tag;
    }

    public int getTick() {
        return tick;
    }

    public void incrementTick() {
        tick++;
    }
}
