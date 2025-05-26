package net.woukie.createmissiles.missilemanager;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.Container;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.level.Level;
import net.woukie.createmissiles.missilemanager.parts.ChassisType;
import net.woukie.createmissiles.missilemanager.parts.PartTypeRegistry;
import net.woukie.createmissiles.missilemanager.parts.ThrusterType;
import net.woukie.createmissiles.missilemanager.parts.WarheadType;

public class TrajectoryData {
    public final double gravity = 9.81F;

    public final Level level;
    public final BlockPos source, target;
    public final double fuelPercentage;
    private int tick; // Ticks incremented since launch

    public final WarheadType warheadType;
    public final ChassisType chassisType;
    public final ThrusterType thrusterType;

    public final Tag warheadData;
    public final Tag chassisData;
    public final Tag thrusterData;

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

//    Construct by loading from disk
    public TrajectoryData(CompoundTag savedData, MinecraftServer server) {
        String dimension = savedData.getString("Dimension");
        ResourceKey<Level> dimensionKey = ResourceKey.create(Registries.DIMENSION, new ResourceLocation(dimension));
        this.level = server.getLevel(dimensionKey);

        this.source = new BlockPos(savedData.getInt("SourceX"), savedData.getInt("SourceY"), savedData.getInt("SourceZ"));
        this.target = new BlockPos(savedData.getInt("TargetX"), savedData.getInt("TargetY"), savedData.getInt("TargetZ"));
        this.fuelPercentage = savedData.getDouble("FuelPercentage");
        this.tick = savedData.getInt("Tick");

        this.warheadType = PartTypeRegistry.getWarhead(new ResourceLocation(savedData.getString("Warhead")));
        this.chassisType = PartTypeRegistry.getChassis(new ResourceLocation(savedData.getString("Chassis")));
        this.thrusterType = PartTypeRegistry.getThruster(new ResourceLocation(savedData.getString("Thruster")));

        this.warheadData = savedData.get("WarheadData");
        this.chassisData = savedData.get("ChassisData");
        this.thrusterData = savedData.get("ThrusterData");
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

        tag.put("WarheadData", warheadData);
        tag.put("ChassisData", chassisData);
        tag.put("ThrusterData", thrusterData);

        return tag;
    }

    public int getTick() {
        return tick;
    }

    public void incrementTick() {
        tick++;
    }
}
