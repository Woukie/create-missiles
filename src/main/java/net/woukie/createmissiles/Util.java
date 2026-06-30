package net.woukie.createmissiles;

import com.mojang.datafixers.util.Function7;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3d;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.function.Function;

public class Util {
    //    Supercover line algorithm
    //    Stops when consumer returns true
    public static void traverseSupercover(Vector3d start, Vector3d end, Function<Vector3d, Boolean> callback) {
        Vector3d startFloor = new Vector3d(Math.floor(start.x), Math.floor(start.y), Math.floor(start.z));
        Vector3d endFloor = new Vector3d(Math.floor(end.x), Math.floor(end.y), Math.floor(end.z));

        Vector3d delta = new Vector3d(
                Math.abs(endFloor.x - startFloor.x),
                Math.abs(endFloor.y - startFloor.y),
                Math.abs(endFloor.z - startFloor.z)
        );
        Vector3d step = new Vector3d(
                Integer.compare((int) endFloor.x, (int) startFloor.x),
                Integer.compare((int) endFloor.y, (int) startFloor.y),
                Integer.compare((int) endFloor.z, (int) startFloor.z)
        );
        Vector3d current = new Vector3d(startFloor);
        Vector3d deltaDist = new Vector3d(
                delta.x == 0 ? Double.POSITIVE_INFINITY : 1.0 / Math.abs(end.x - start.x),
                delta.y == 0 ? Double.POSITIVE_INFINITY : 1.0 / Math.abs(end.y - start.y),
                delta.z == 0 ? Double.POSITIVE_INFINITY : 1.0 / Math.abs(end.z - start.z)
        );
        Vector3d maxDist = new Vector3d(
                delta.x == 0 ? Double.POSITIVE_INFINITY : deltaDist.x * ((step.x > 0 ? (startFloor.x + 1) - start.x : start.x - startFloor.x)),
                delta.y == 0 ? Double.POSITIVE_INFINITY : deltaDist.y * ((step.y > 0 ? (startFloor.y + 1) - start.y : start.y - startFloor.y)),
                delta.z == 0 ? Double.POSITIVE_INFINITY : deltaDist.z * ((step.z > 0 ? (startFloor.z + 1) - start.z : start.z - startFloor.z))
        );

        for (int i = 0; i < 1 + (int) delta.x + (int) delta.y + (int) delta.z; i++) {
            Vector3d center = new Vector3d(current).add(0.5, 0.5, 0.5);
            if (callback.apply(center)) return;

            if (maxDist.x < maxDist.y && maxDist.x < maxDist.z) {
                current.x += step.x;
                maxDist.x += deltaDist.x;
            } else if (maxDist.y < maxDist.z) {
                current.y += step.y;
                maxDist.y += deltaDist.y;
            } else {
                current.z += step.z;
                maxDist.z += deltaDist.z;
            }
        }
    }

    public static BlockPos locateAir(Vec3 origin, ServerLevel level, int limit) {
        return locateNearestMatchingBlock(origin, blockPos -> level.isEmptyBlock(blockPos) && !level.isEmptyBlock(blockPos.relative(Direction.DOWN)), limit);
    }

    public static BlockPos locateNearestMatchingBlock(Vec3 origin, Function<BlockPos, Boolean> condition, int limit) {
        class Neighbor {
            public final BlockPos position;
            public final double distance;

            public Neighbor(BlockPos position, double distance) {
                this.position = position;
                this.distance = distance;
            }
        }

        var processedNeighbors = new ArrayList<BlockPos>();
        var seenNeighbors = new ArrayList<BlockPos>();
        var neighbors = new PriorityQueue<Neighbor>(Comparator.comparingDouble(neighbour -> neighbour.distance));
        neighbors.add(new Neighbor(new BlockPos((int) origin.x, (int) origin.y, (int) origin.z), 0));

        int steps = 0;
        while (!neighbors.isEmpty() && steps < limit) {
            Neighbor neighbor = neighbors.poll();
            if (condition.apply(neighbor.position)) return neighbor.position;
            processedNeighbors.add(neighbor.position);
            for (Direction dir : Direction.values()) {
                var newPos = neighbor.position.offset(dir.getStepX(), dir.getStepY(), dir.getStepZ());
                if (!processedNeighbors.contains(newPos) && !seenNeighbors.contains(newPos)) {
                    seenNeighbors.add(newPos);
                    neighbors.add(new Neighbor(newPos, newPos.getCenter().distanceTo(origin)));
                }
            }
            steps ++;
        }
        return null;
    }

    public static <B, C, T1, T2, T3, T4, T5, T6, T7> StreamCodec<B, C> composite(final StreamCodec<? super B, T1> codec1, final Function<C, T1> getter1, final StreamCodec<? super B, T2> codec2, final Function<C, T2> getter2, final StreamCodec<? super B, T3> codec3, final Function<C, T3> getter3, final StreamCodec<? super B, T4> codec4, final Function<C, T4> getter4, final StreamCodec<? super B, T5> codec5, final Function<C, T5> getter5, final StreamCodec<? super B, T6> codec6, final Function<C, T6> getter6, final StreamCodec<? super B, T7> codec7, final Function<C, T7> getter7, final Function7<T1, T2, T3, T4, T5, T6, T7, C> factory) {
        return new StreamCodec<B, C>() {
            public C decode(B p_330310_) {
                T1 t1 = (T1)codec1.decode(p_330310_);
                T2 t2 = (T2)codec2.decode(p_330310_);
                T3 t3 = (T3)codec3.decode(p_330310_);
                T4 t4 = (T4)codec4.decode(p_330310_);
                T5 t5 = (T5)codec5.decode(p_330310_);
                T6 t6 = (T6)codec6.decode(p_330310_);
                T7 t7 = (T7)codec7.decode(p_330310_);
                return (C)factory.apply(t1, t2, t3, t4, t5, t6, t7);
            }

            public void encode(B p_332052_, C p_331912_) {
                codec1.encode(p_332052_, getter1.apply(p_331912_));
                codec2.encode(p_332052_, getter2.apply(p_331912_));
                codec3.encode(p_332052_, getter3.apply(p_331912_));
                codec4.encode(p_332052_, getter4.apply(p_331912_));
                codec5.encode(p_332052_, getter5.apply(p_331912_));
                codec6.encode(p_332052_, getter6.apply(p_331912_));
                codec7.encode(p_332052_, getter7.apply(p_331912_));
            }
        };
    }
}
