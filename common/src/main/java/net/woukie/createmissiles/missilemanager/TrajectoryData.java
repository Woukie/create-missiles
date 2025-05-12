package net.woukie.createmissiles.missilemanager;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.Level;
import net.woukie.createmissiles.missilemanager.parts.Chassis;
import net.woukie.createmissiles.missilemanager.parts.PartRegistry;
import net.woukie.createmissiles.missilemanager.parts.Thruster;
import net.woukie.createmissiles.missilemanager.parts.Warhead;

public class TrajectoryData {
    public final double gravity = 9.81F;

    public final Level level;
    public final BlockPos source, target;
    private int tick; // Ticks incremented since launch

    public final float fuel_percent = 100F;

    public final Warhead warhead;
    public final Chassis chassis;
    public final Thruster thruster;

    public TrajectoryData(Level level, BlockPos source, BlockPos target, int tick, Warhead warhead, Chassis chassis, Thruster thruster) {
        this.level = level;
        this.source = source;
        this.target = target;
        this.tick = tick;

        this.warhead = warhead;
        this.chassis = chassis;
        this.thruster = thruster;
    }

    public TrajectoryData(CompoundTag savedData, MinecraftServer server) {
        String dimension = savedData.getString("dimension");
        ResourceKey<Level> dimensionKey = ResourceKey.create(Registries.DIMENSION, new ResourceLocation(dimension));
        this.level = server.getLevel(dimensionKey);

        this.source = new BlockPos(savedData.getInt("SourceX"), savedData.getInt("SourceY"), savedData.getInt("SourceZ"));
        this.target = new BlockPos(savedData.getInt("TargetX"), savedData.getInt("TargetY"), savedData.getInt("TargetZ"));
        this.tick = savedData.getInt("Tick");

        this.warhead = PartRegistry.getWarhead(new ResourceLocation(savedData.getString("Warhead")));
        this.chassis = PartRegistry.getChassis(new ResourceLocation(savedData.getString("Chassis")));
        this.thruster = PartRegistry.getThruster(new ResourceLocation(savedData.getString("Thruster")));
    }

    public CompoundTag saveTo(CompoundTag tag) {
        tag.putString("dimension", this.level.dimension().location().getPath());
        tag.putInt("SourceX", this.source.getX());
        tag.putInt("SourceY", this.source.getY());
        tag.putInt("SourceZ", this.source.getZ());
        tag.putInt("TargetX", this.target.getX());
        tag.putInt("TargetY", this.target.getY());
        tag.putInt("TargetZ", this.target.getZ());
        tag.putInt("Tick", this.tick);

        tag.putString("Warhead",  warhead.resourceLocation.toString());
        tag.putString("Chassis",  chassis.resourceLocation.toString());
        tag.putString("Thruster",  thruster.resourceLocation.toString());

        return tag;
    }

    public int getTick() {
        return tick;
    }

    public void incrementTick() {
        tick++;
    }
}
