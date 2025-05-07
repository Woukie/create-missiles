package net.woukie.createmissiles.missilemanager;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.Level;
import net.woukie.createmissiles.missilemanager.parts.*;

public class Trajectory {
    public Level level;
    public BlockPos source, target;
    public int ticks;

    public Warhead warhead;
    public Chassis chassis;
    public Thruster thruster;

    public static Trajectory loadFrom(CompoundTag tag, MinecraftServer server) {
        String dimension = tag.getString("dimension");
        BlockPos source = new BlockPos(tag.getInt("SourceX"), tag.getInt("SourceY"), tag.getInt("SourceZ"));
        BlockPos target = new BlockPos(tag.getInt("TargetX"), tag.getInt("TargetY"), tag.getInt("TargetZ"));
        int ticks = tag.getInt("Ticks");

        ResourceKey<Level> key = ResourceKey.create(Registries.DIMENSION, new ResourceLocation(dimension));

        Level level = server.getLevel(key);
        return new Trajectory(level, source, target, ticks);
    }

    public CompoundTag saveTo(CompoundTag tag) {
        tag.putString("dimension", this.level.dimension().location().getPath());
        tag.putInt("SourceX", this.source.getX());
        tag.putInt("SourceY", this.source.getY());
        tag.putInt("SourceZ", this.source.getZ());
        tag.putInt("TargetX", this.target.getX());
        tag.putInt("TargetY", this.target.getY());
        tag.putInt("TargetZ", this.target.getZ());
        tag.putInt("Ticks", this.ticks);
        return tag;
    }

    public Trajectory(Level level, BlockPos source, BlockPos destination) {
        this(level, source, destination, 0);
    }

    private Trajectory(Level level, BlockPos source, BlockPos destination, int ticks) {
        this.level = level;
        this.source = source;
        this.target = destination;
        this.ticks = ticks;
    }
}
