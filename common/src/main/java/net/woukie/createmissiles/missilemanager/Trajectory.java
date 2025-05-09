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

public class Trajectory {
    private static final float gravity = 9.81F;


    private float angle = -1;

    public boolean hit;
    public Level level;
    public BlockPos source, target;
    public int ticks;

    public Warhead warhead;
    public Chassis chassis;
    public Thruster thruster;

    public Trajectory(Level level, BlockPos source, BlockPos destination) {
        this(level, source, destination, 0);
    }

    private Trajectory(Level level, BlockPos source, BlockPos destination, int ticks) {
        this.level = level;
        this.source = source;
        this.target = destination;
        this.ticks = ticks;

        this.warhead = new Warhead("idk", 1, trajectory1 ->
                level.explode(null, target.getX(), target.getY(), target.getZ(), 5, Level.ExplosionInteraction.TNT)
        );
        this.chassis = new Chassis("idakl", 3);
        this.thruster = new Thruster("thruster", 5000, 2);
    }

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

    private float calculateAngle(float lowerAngle, float upperAngle, int sections, int iteration, int iterations) {
        iteration++;
        if (iteration > iterations)
            return (lowerAngle + upperAngle) / 2;

        float[] angles = new float[sections];
        float[] accuracies = new float[sections];

        for (int i = 0; i < sections; i++) {
            float angle = ((float)i / sections) * (upperAngle - lowerAngle) + lowerAngle;
            angles[i] = angle;
            accuracies[i] = getAccuracy(angle);
        }

        int bestPairId = 0;
        float bestPair = Float.MAX_VALUE;
        for (int i = 0; i < sections - 1; i++) {
            float accuracyLeft = Math.abs(accuracies[i]);
            float accuracyRight = Math.abs(accuracies[i + 1]);

            float averageAccuracy = (accuracyLeft + accuracyRight) / 2;
            if (averageAccuracy < bestPair) {
                bestPair = averageAccuracy;
                bestPairId = i;
            }
        }

        if (Math.abs(accuracies[bestPairId]) < 0.5F)
            return angles[bestPairId];

        return calculateAngle(angles[bestPairId], angles[bestPairId + 1], sections, iteration, iterations);
    }

//    Bigger number = how far off
    private float getAccuracy(float angle) {
        float initialVelocity = getIntialVelocity();

        Vec2 targetXZ = new Vec2(this.target.getX(), this.target.getZ());
        Vec2 sourceXZ = new Vec2(this.source.getX(), this.source.getZ());
        Vec2 distanceXZ = targetXZ.add(sourceXZ.scale(-1));

        float targetX = distanceXZ.length();
        float targetY = this.target.getY() - this.source.getY();

        float tan = (float) Math.tan(angle);
        float cos = (float) Math.cos(angle);

        float y = (targetX * tan) / (1 + (targetX / (2 * initialVelocity * initialVelocity * cos * cos)));
        return targetY - y;
    }

    //    For da expert salad:
//    Use 'source' and 'target' block positions to calculate the position in the trajectory at time 'ticks' (1 tick = 0.05s)
//
//    Feel free to define static constants if you want to model atmosphere, pressure or whatever. In the future I'll make a config thingy to fetch dimension-specific values
//    Feel free to add to the 'loadFrom' and 'saveTo' functions if you want to save calculated data (e.g. launch angle) to speed up future calls
    public Vec3 getPosition() {
        float second = getSeconds();

        if (angle == -1)
            angle = calculateAngle(0, (float) Math.PI / 2.0F, 5, 0, 20);

        Vec2 targetXZ = new Vec2(this.target.getX(), this.target.getZ());
        Vec2 sourceXZ = new Vec2(this.source.getX(), this.source.getZ());
        Vec2 distanceXZ = targetXZ.add(sourceXZ.scale(-1));

        Vec2 localTarget = new Vec2(distanceXZ.length(), this.target.getY() - this.source.getY());

        float initialVelocity = getIntialVelocity();
        float v0x = initialVelocity * (float) Math.cos(angle);
        float v0y = initialVelocity * (float) Math.sin(angle);

        float x = v0x * second;
        float y = v0y * second - 0.5f * gravity * second * second;

        Vec2 position = new Vec2(x, y);

        float horizontalProportionTravelled = position.x / localTarget.x;
        if (horizontalProportionTravelled > 1) {
            this.hit = true;
        }
        return new Vec3(
                this.source.getX() + horizontalProportionTravelled * distanceXZ.x,
                this.source.getY() + position.y,
                this.source.getZ() + horizontalProportionTravelled * distanceXZ.y
        );
    }

    private float getIntialVelocity() {
        return 20F;
    }

    private float getSeconds() {
        return ticks * 0.05F;
    }
}
