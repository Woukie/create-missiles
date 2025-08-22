package net.woukie.createmissiles.missilemanager.asyncexplosionhandler;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.*;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3d;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

import static net.woukie.createmissiles.Util.traverseSupercover;

public class Explosion {
    private final HashMap<BlockPos, BlockData> blockData = new HashMap<>();
    private final PriorityQueue<BlockPos> blockQueue = new PriorityQueue<>((o1, o2) -> Double.compare(this.position.distanceTo(o1.getCenter()), this.position.distanceTo(o2.getCenter())));

    private Vec3 position;
    private double radius;
    private double radiusOfLastChange = 0;
    private double power;
    private ResourceKey<Level> levelKey;
    private boolean complete = false;

    public Explosion() {
    }

    public Explosion(Level level, Vec3 position, double radius, double power) {
        this.levelKey = level.dimension();
        this.position = position;
        this.radius = radius;
        this.power = power;
    }

    public void serverTick(MinecraftServer server) {
        var startTime = System.currentTimeMillis();
        var level = server.getLevel(levelKey);
        if (level == null) return;

        if (blockQueue.isEmpty() && blockData.isEmpty()) {
            var pos = new BlockPos((int) position.x, (int) position.y, (int) position.z);
            blockData.put(pos, new BlockData(level.getBlockState(pos).getBlock().getExplosionResistance()));
            blockQueue.add(pos);
        }

        while (System.currentTimeMillis() - startTime < 100) {
            if (blockQueue.isEmpty()) {
//                Radius exhausted
                System.out.println("Radius exhausted");
                this.complete = true;
                return;
            }

            BlockPos blockPos = this.blockQueue.poll();
            var radius = blockPos.getCenter().distanceTo(this.position);
            if (radius - radiusOfLastChange > 2) {
//                No more blocks being broken
                System.out.println("No more blocks being broken");
                this.complete = true;
                return;
            }

            if (damageBlock(blockPos, level)) {
                radiusOfLastChange = radius;
            }

            var directions = Arrays.asList(Direction.values());
            Collections.shuffle(directions);
            for (Direction dir : directions) {
                var newPos = blockPos.offset(dir.getStepX(), dir.getStepY(), dir.getStepZ());
                if (!blockQueue.contains(newPos) && !blockData.containsKey(newPos)) {
                    if (newPos.getCenter().distanceTo(this.position) < this.radius) {
                        this.blockQueue.add(newPos);
                        blockData.put(newPos, new BlockData(level.getBlockState(newPos).getBlock().getExplosionResistance()));
                    }
                }
            }
        }
    }

    private boolean damageBlock(BlockPos blockPos, Level level) {
        var from = new Vector3d(this.position.x, this.position.y, this.position.z);
        var to = new Vector3d(blockPos.getX(), blockPos.getY(), blockPos.getZ()).add(0.5, 0.5, 0.5);
        AtomicReference<Float> totalHardness = new AtomicReference<>(0f);
        AtomicReference<Integer> passedCount = new AtomicReference<>(0);
        traverseSupercover(from, to, pos -> {
            var traversedBlockPos = new BlockPos((int) pos.x, (int) pos.y, (int) pos.z);
            float hardness = blockData.containsKey(traversedBlockPos) ?
                    blockData.get(traversedBlockPos).hardness :
                    level.getBlockState(traversedBlockPos).getBlock().getExplosionResistance();
            totalHardness.getAndUpdate(a -> a + hardness);
            passedCount.getAndUpdate(a -> ++a);
            return false;
        });

        double distance = blockPos.getCenter().distanceTo(this.position);
        double averageHardness = totalHardness.get() / passedCount.get();
        double powerLeft = this.power - ((0.3 * averageHardness + 0.315) * distance);

        var hardness = level.getBlockState(blockPos).getBlock().getExplosionResistance();
        powerLeft -= 0.3 * hardness + 0.315;
        if (powerLeft > 0) {
            level.destroyBlock(blockPos, true);
            return true;
        }
        return false;
    }

    public boolean isComplete() {
        return this.complete;
    }

    public CompoundTag save() {
        var blockData = new CompoundTag();
        blockData.putLongArray("Positions", this.blockData.keySet().stream().map(BlockPos::asLong).toList());
        ListTag blockDataValues = new ListTag();
        blockDataValues.addAll(this.blockData.values().stream().map(BlockData::save).toList());
        blockData.put("Data", blockDataValues);

        var data = new CompoundTag();
        data.put("BlockData", blockData);
        data.putLongArray("BlockQueue", this.blockQueue.stream().map(BlockPos::asLong).toList());
        data.putString("Dimension", levelKey.location().getPath());
        data.putDouble("PositionX", position.x);
        data.putDouble("PositionY", position.y);
        data.putDouble("PositionZ", position.z);
        data.putDouble("Radius", radius);
        data.putDouble("RadiusOfLastChange", radiusOfLastChange);
        data.putDouble("Power", power);
        return data;
    }

    public static Explosion loadFrom(CompoundTag data) {
        var explosion = new Explosion();
        String dimension = data.getString("Dimension");
        explosion.levelKey = ResourceKey.create(Registries.DIMENSION, new ResourceLocation(dimension));
        explosion.position = new Vec3(data.getDouble("PositionX"), data.getDouble("PositionY"), data.getDouble("PositionZ"));
        explosion.radius = data.getDouble("Radius");
        explosion.blockQueue.clear();
        explosion.blockQueue.addAll(Arrays.stream(data.getLongArray("BlockQueue")).mapToObj(BlockPos::of).toList());
        explosion.radiusOfLastChange = data.getDouble("RadiusOfLastChange");

        CompoundTag blockDataTag = data.getCompound("BlockData");
        explosion.blockData.clear();
        List<BlockPos> blockDataKeys = Arrays.stream(blockDataTag.getLongArray("Positions")).mapToObj(BlockPos::of).toList();
        ListTag blockDataValues = blockDataTag.getList("Data", 10);
        for (int i = 0; i < blockDataValues.size(); i++) {
            explosion.blockData.put(blockDataKeys.get(i), BlockData.load(blockDataValues.get(i)));
        }

        return explosion;
    }

    public record BlockData(float hardness) {
        public CompoundTag save() {
            var data = new CompoundTag();
            data.putFloat("Hardness", hardness);
            return data;
        }

        public static BlockData load(Tag tag) {
            if (tag.getType() == CompoundTag.TYPE) {
                var data = (CompoundTag) tag;
                return new BlockData(data.getFloat("Hardness"));
            }
            return new BlockData(0);
        }
    }
}
