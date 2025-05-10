package net.woukie.createmissiles.missilemanager;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.woukie.createmissiles.missilemanager.parts.*;
import net.woukie.createmissiles.missilemanager.parts.chassis.ChassisRegistry;
import net.woukie.createmissiles.missilemanager.parts.thrusters.ThrusterRegistry;
import net.woukie.createmissiles.missilemanager.parts.warheads.WarheadRegistry;

public class Trajectory {
    private static final double gravity = 9.81F;

    private double angle = -1;
    private double launchVelocity;
    private double flightLength;
    private Vec2 localTarget;

    public Level level;
    public BlockPos source, target;
    public int ticks;

    public Warhead warhead;
    public Chassis chassis;
    public Thruster thruster;

    public Trajectory(Level level, BlockPos source, BlockPos destination, Warhead warhead, Chassis chassis, Thruster thruster) {
        this(level, source, destination, 0, warhead, chassis, thruster);
    }

    private Trajectory(Level level, BlockPos source, BlockPos destination, int ticks, Warhead warhead, Chassis chassis, Thruster thruster) {
        this.level = level;
        this.source = source;
        this.target = destination;
        this.ticks = ticks;

        this.warhead = warhead;
        this.chassis = chassis;
        this.thruster = thruster;

        preCalculate();
    }

    public static Trajectory loadFrom(CompoundTag tag, MinecraftServer server) {
        String dimension = tag.getString("dimension");
        BlockPos source = new BlockPos(tag.getInt("SourceX"), tag.getInt("SourceY"), tag.getInt("SourceZ"));
        BlockPos target = new BlockPos(tag.getInt("TargetX"), tag.getInt("TargetY"), tag.getInt("TargetZ"));
        int ticks = tag.getInt("Ticks");

        ResourceKey<Level> dimensionKey = ResourceKey.create(Registries.DIMENSION, new ResourceLocation(dimension));

        Warhead warhead = WarheadRegistry.get(new ResourceLocation(tag.getString("WarheadType"))).get();
        warhead.loadData(tag.getCompound("WarheadData"));

        Chassis chassis = ChassisRegistry.get(new ResourceLocation(tag.getString("ChassisType"))).get();
        chassis.loadData(tag.getCompound("ChassisData"));

        Thruster thruster = ThrusterRegistry.get(new ResourceLocation(tag.getString("ThrusterType"))).get();
        thruster.loadData(tag.getCompound("ThrusterData"));

        Level level = server.getLevel(dimensionKey);
        return new Trajectory(level, source, target, ticks, warhead, chassis, thruster);
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

        tag.putString("WarheadType",  warhead.getResourceLocation().getPath());
        tag.put("WarheadData",  warhead.saveData(new CompoundTag()));

        tag.putString("ChassisType",  chassis.getResourceLocation().getPath());
        tag.put("ChassisData",  chassis.saveData(new CompoundTag()));

        tag.putString("ThrusterType",  thruster.getResourceLocation().getPath());
        tag.put("ThrusterData",  thruster.saveData(new CompoundTag()));

        return tag;
    }

    private void preCalculate() {
        Vec2 targetXZ = new Vec2(this.target.getX(), this.target.getZ());
        Vec2 sourceXZ = new Vec2(this.source.getX(), this.source.getZ());
        Vec2 distanceXZ = targetXZ.add(sourceXZ.scale(-1));

        this.launchVelocity = 25F;

        this.localTarget = new Vec2(distanceXZ.length(), this.target.getY() - this.source.getY());
        ProjectileUtils.LaunchInfo info = ProjectileUtils.getLaunchInfo(localTarget.x, localTarget.y, launchVelocity, gravity);
        this.angle = info.getAngle();
        this.flightLength = info.getTime();
    }

    //    For da expert salad:
//    Use 'source' and 'target' block positions to calculate the position in the trajectory at time 'ticks' (1 tick = 0.05s)
    public Vec3 getPosition() {
        float second = ticks / 20F;

        Vec2 position = ProjectileUtils.getPositionAt(launchVelocity, angle, gravity, second);
        Vec2 distanceXZ = new Vec2(
                target.getX() - source.getX(),
                target.getZ() - source.getZ()
        );

        float horizontalProportionTravelled = position.x / localTarget.x;
        return new Vec3(
                this.source.getX() + horizontalProportionTravelled * distanceXZ.x,
                this.source.getY() + position.y,
                this.source.getZ() + horizontalProportionTravelled * distanceXZ.y
        );
    }

    public boolean shouldExplode() {
        return (ticks / 20F) > flightLength;
    }
}
