package net.woukie.createmissiles.entity.drone;

import com.google.common.collect.Iterables;
import com.google.common.collect.LinkedHashMultiset;
import com.google.common.collect.Multiset;
import com.google.common.collect.Multisets;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.SectionPos;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import net.minecraft.world.phys.Vec3;

import java.util.*;

public class MapUtils {
    private static final Set<SpawnMap> spawnMaps = new HashSet<>();

    public static void spawnMapAt(ServerLevel level, Vec3 position, BlockPos target) {
        spawnMaps.add(new SpawnMap(level, position, target));
    }

    public static void serverTick() {
        spawnMaps.removeIf(spawnMap -> {
            BlockPos target = spawnMap.target;
            ServerLevel level = spawnMap.level;
            List<ChunkPos> affectedChunks = getChunksForTarget(target);
            for (ChunkPos pos : affectedChunks) {
                if (!level.isLoaded(new BlockPos((pos.x << 4) + 8, 0, (pos.z << 4) + 8))) {
                    level.setChunkForced(pos.x, pos.z, true);
                    return false;
                }
            }

            ItemStack stack = MapUtils.createAndFillMap(level, target.getX(), target.getZ(), 1);
            DefaultDispenseItemBehavior.spawnItem(level, stack, 1, Direction.UP, spawnMap.position);
            level.playSound(null, spawnMap.position.x, spawnMap.position.y, spawnMap.position.z, SoundEvents.NOTE_BLOCK_CHIME.value(), SoundSource.PLAYERS, 1, 1);
            affectedChunks.forEach(chunkPos -> {
                level.setChunkForced(chunkPos.x, chunkPos.z, false);
            });
            return true;
        });
    }

    public static List<ChunkPos> getChunksForTarget(BlockPos target) {
        List<ChunkPos> chunks = new ArrayList<>();
        int radius = 128;

        int minChunkX = (target.getX() - radius) >> 4;
        int maxChunkX = (target.getX() + radius) >> 4;
        int minChunkZ = (target.getZ() - radius) >> 4;
        int maxChunkZ = (target.getZ() + radius) >> 4;

        for (int chunkX = minChunkX; chunkX <= maxChunkX; chunkX++) {
            for (int chunkZ = minChunkZ; chunkZ <= maxChunkZ; chunkZ++) {
                int closestX = Math.max(chunkX << 4, Math.min(target.getX(), (chunkX << 4) + 15));
                int closestZ = Math.max(chunkZ << 4, Math.min(target.getZ(), (chunkZ << 4) + 15));
                double distanceSquared = Math.pow(target.getX() - closestX, 2) + Math.pow(target.getZ() - closestZ, 2);
                if (distanceSquared <= radius * radius) {
                    chunks.add(new ChunkPos(chunkX, chunkZ));
                }
            }
        }

        return chunks;
    }

    public static ItemStack createAndFillMap(Level level, int centerX, int centerZ, int scale) {
        ItemStack map = MapItem.create(level, centerX, centerZ, (byte) scale, true, true);
        MapItemSavedData mapData = MapItem.getSavedData(map, level);

        if (mapData == null) {
            return map;
        }
        fillMapData(level, centerX, centerZ, Objects.requireNonNull(MapItem.getSavedData(map, level)));
        return map;
    }

    private static BlockState getCorrectStateForFluidBlock(Level level, BlockState blockState, BlockPos blockPos) {
        FluidState fluidState = blockState.getFluidState();
        return !fluidState.isEmpty() && !blockState.isFaceSturdy(level, blockPos, Direction.UP) ? fluidState.createLegacyBlock() : blockState;
    }

    public static void fillMapData(Level level, double X, double Z, MapItemSavedData mapItemSavedData) {
        if (level.dimension() == mapItemSavedData.dimension) {
            int i = 1 << mapItemSavedData.scale;
            int j = mapItemSavedData.centerX;
            int k = mapItemSavedData.centerZ;
            int l = Mth.floor(X - (double) j) / i + 64;
            int m = Mth.floor(Z - (double) k) / i + 64;
            int n = 128 / i;
            if (level.dimensionType().hasCeiling()) {
                n /= 2;
            }

            BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
            BlockPos.MutableBlockPos mutableBlockPos2 = new BlockPos.MutableBlockPos();
            boolean bl = false;

            for (int o = l - n + 1; o < l + n; ++o) {
                bl = false;
                double d = 0.0;
                for (int p = m - n - 1; p < m + n; ++p) {
                    if (o >= 0 && p >= -1 && o < 128 && p < 128) {
                        int q = Mth.square(o - l) + Mth.square(p - m);
                        boolean bl2 = q > (n - 2) * (n - 2);
                        int r = (j / i + o - 64) * i;
                        int s = (k / i + p - 64) * i;
                        Multiset<MapColor> multiset = LinkedHashMultiset.create();
                        LevelChunk levelChunk = level.getChunk(SectionPos.blockToSectionCoord(r), SectionPos.blockToSectionCoord(s));
                        if (!levelChunk.isEmpty()) {
                            int t = 0;
                            double e = 0.0;
                            int u;
                            if (level.dimensionType().hasCeiling()) {
                                u = r + s * 231871;
                                u = u * u * 31287121 + u * 11;
                                if ((u >> 20 & 1) == 0) {
                                    multiset.add(Blocks.DIRT.defaultBlockState().getMapColor(level, BlockPos.ZERO), 10);
                                } else {
                                    multiset.add(Blocks.STONE.defaultBlockState().getMapColor(level, BlockPos.ZERO), 100);
                                }

                                e = 100.0;
                            } else {
                                for (u = 0; u < i; ++u) {
                                    for (int v = 0; v < i; ++v) {
                                        mutableBlockPos.set(r + u, 0, s + v);
                                        int w = levelChunk.getHeight(Heightmap.Types.WORLD_SURFACE, mutableBlockPos.getX(), mutableBlockPos.getZ()) + 1;
                                        BlockState blockState;
                                        if (w <= level.getMinBuildHeight() + 1) {
                                            blockState = Blocks.BEDROCK.defaultBlockState();
                                        } else {
                                            do {
                                                --w;
                                                mutableBlockPos.setY(w);
                                                blockState = levelChunk.getBlockState(mutableBlockPos);
                                            } while (blockState.getMapColor(level, mutableBlockPos) == MapColor.NONE && w > level.getMinBuildHeight());

                                            if (w > level.getMinBuildHeight() && !blockState.getFluidState().isEmpty()) {
                                                int x = w - 1;
                                                mutableBlockPos2.set(mutableBlockPos);

                                                BlockState blockState2;
                                                do {
                                                    mutableBlockPos2.setY(x--);
                                                    blockState2 = levelChunk.getBlockState(mutableBlockPos2);
                                                    ++t;
                                                } while (x > level.getMinBuildHeight() && !blockState2.getFluidState().isEmpty());

                                                blockState = getCorrectStateForFluidBlock(level, blockState, mutableBlockPos);
                                            }
                                        }

                                        mapItemSavedData.checkBanners(level, mutableBlockPos.getX(), mutableBlockPos.getZ());
                                        e += (double) w / (double) (i * i);
                                        multiset.add(blockState.getMapColor(level, mutableBlockPos));
                                    }
                                }
                            }

                            t /= i * i;
                            MapColor mapColor = Iterables.getFirst(Multisets.copyHighestCountFirst(multiset), MapColor.NONE);
                            MapColor.Brightness brightness;
                            double f;
                            if (mapColor == MapColor.WATER) {
                                f = (double) t * 0.1 + (double) (o + p & 1) * 0.2;
                                if (f < 0.5) {
                                    brightness = MapColor.Brightness.HIGH;
                                } else if (f > 0.9) {
                                    brightness = MapColor.Brightness.LOW;
                                } else {
                                    brightness = MapColor.Brightness.NORMAL;
                                }
                            } else {
                                f = (e - d) * 4.0 / (double) (i + 4) + ((double) (o + p & 1) - 0.5) * 0.4;
                                if (f > 0.6) {
                                    brightness = MapColor.Brightness.HIGH;
                                } else if (f < -0.6) {
                                    brightness = MapColor.Brightness.LOW;
                                } else {
                                    brightness = MapColor.Brightness.NORMAL;
                                }
                            }

                            d = e;
                            if (p >= 0 && q < n * n && (!bl2 || (o + p & 1) != 0)) {
                                bl |= mapItemSavedData.updateColor(o, p, mapColor.getPackedId(brightness));
                            }
                        }
                    }
                }
            }
        }
    }

    static class SpawnMap {
        private ServerLevel level;
        private Vec3 position;
        private BlockPos target;

        public SpawnMap(ServerLevel level, Vec3 position, BlockPos target) {
            this.level = level;
            this.position = position;
            this.target = target;
        }
    }
}
