package net.woukie.createmissiles.entity;
import com.google.common.collect.Iterables;
import com.google.common.collect.LinkedHashMultiset;
import com.google.common.collect.Multiset;
import com.google.common.collect.Multisets;
import net.minecraft.core.*;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.*;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import net.minecraft.world.phys.Vec3;
import net.woukie.createmissiles.inventory.DroneMenu;
import org.jetbrains.annotations.NotNull;
import org.joml.Quaterniond;
import org.joml.Vector3d;

import java.util.Objects;

public class DroneEntity extends Entity {
    protected Vector3d acceleration;
    protected Vector3d velocity;
    public Vector3d destination = new Vector3d(-900, 0, -900);
    public Vector3d startPosition;
    public Vector3d forward;
    public enum DroneJourneyStage {TAXI, TAKEOFF, CRUISE, RETURN, LAND, COMPLETE}

    private DroneJourneyStage journeyStage;
    private ItemStack mapItemStack;
    private double flySpeed = 7;

    public DroneEntity(EntityType<?> entityType, Level level) {
        super(entityType, level);
        acceleration = new Vector3d();
        velocity = new Vector3d();
        journeyStage = DroneJourneyStage.TAXI;
    }

    @Override
    public void tick() {
        double tickLength = 0.05d;
        double elapsedTime = this.tickCount * tickLength;

        switch (journeyStage) {
            case TAXI -> handleTaxi();
            case TAKEOFF -> handleTakeoff();
            case CRUISE -> handleCruise();
            case RETURN -> handleReturn();
            case LAND -> handleLanding();
            case COMPLETE -> handleComplete();
        }

        velocity.add(acceleration.mul(tickLength));
        this.setPos(this.position().x + velocity.x * tickLength, this.position().y + velocity.y * tickLength, this.position().z + velocity.z * tickLength);

        super.tick();
    }

    private void handleTaxi() {
        this.startPosition = new Vector3d(position().x, position().y, position().z);
        journeyStage = DroneJourneyStage.TAKEOFF;
    }

    private void handleTakeoff() {
        if(position().y >= 80) journeyStage = DroneJourneyStage.CRUISE;
        acceleration = velocity.x > 3 ? new Vector3d(1f, 1f, 0) : new Vector3d(2f, 0f, 0);
    }

    private void handleCruise() {
        acceleration = new Vector3d(0, 0, 0);
        Vector3d moveDir = new Vector3d(velocity.x, velocity.y, velocity.z).normalize();
        Vector3d targetDir = new Vector3d(destination.x - this.position().x, 0, destination.z - this.position().z).normalize();

        velocity = moveDir.lerp(targetDir, 0.05f).mul(flySpeed);

        if(position().distanceTo(new Vec3(destination.x, position().y, destination.z)) < 5) {
            journeyStage = DroneJourneyStage.RETURN;
            mapItemStack = createAndFillMap(this.level(), (int)this.getX(), (int)this.getZ(), 0);
        };
    }

    private void handleReturn() {
        Vector3d moveDir = new Vector3d(velocity.x, velocity.y, velocity.z).normalize();
        Vector3d targetDir = new Vector3d(startPosition.x - this.position().x, 0, startPosition.z - this.position().z).normalize();

        velocity = moveDir.lerp(targetDir, 0.05f).mul(flySpeed);
        if(position().distanceTo(new Vec3(startPosition.x, position().y, startPosition.z)) < 5) journeyStage = DroneJourneyStage.LAND;
    }

    private void handleLanding() {
        velocity = new Vector3d(0, -0.5f, 0);
        if(position().y - startPosition.y < 1) journeyStage = DroneJourneyStage.COMPLETE;
    }

    private void handleComplete() {
        if(mapItemStack != null)
        {
            DefaultDispenseItemBehavior.spawnItem(this.level(), mapItemStack, 1, Direction.UP, this.position());
        }
        this.discard();
    }

    private static final EntityDataAccessor<Rotations> ROTATION =
            SynchedEntityData.defineId(DroneEntity.class, EntityDataSerializers.ROTATIONS);

    @Override
    protected void defineSynchedData() {
        this.entityData.define(ROTATION, new Rotations(0, 0, 0));
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compoundTag) {

    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compoundTag) {

    }

    @Override
    public boolean isAttackable() {
        return false;
    }

    @Override
    public boolean isPickable() {
        return true;
    }

    public static Vec3 getForwardVector(float yawDegrees, float pitchDegrees) {
        float yawRad = (float) Math.toRadians(-yawDegrees);
        float pitchRad = (float) Math.toRadians(-pitchDegrees);

        float x = Mth.sin(yawRad) * Mth.cos(pitchRad);
        float y = Mth.sin(pitchRad);
        float z = Mth.cos(yawRad) * Mth.cos(pitchRad);

        return new Vec3(x, y, z).normalize();
    }


    @Override
    public @NotNull InteractionResult interact(@NotNull Player player, @NotNull InteractionHand hand) {
        if (!this.level().isClientSide) {
            player.openMenu(new SimpleMenuProvider((ix, inventory, playerx) -> new DroneMenu(ix, inventory), Component.literal("")));
        }
        return InteractionResult.sidedSuccess(this.level().isClientSide);
    }

    public static ItemStack createAndFillMap(Level level, int centerX, int centerZ, int scale) {
        ItemStack map = MapItem.create(level, centerX, centerZ, (byte) scale, true, true);
        MapItemSavedData mapData = MapItem.getSavedData(map, level);

        if (mapData == null) { return map;}
        update(level, centerX, centerZ, Objects.requireNonNull(MapItem.getSavedData(map, level)));
        return map;
    }
    private static BlockState getCorrectStateForFluidBlock(Level level, BlockState blockState, BlockPos blockPos) {
        FluidState fluidState = blockState.getFluidState();
        return !fluidState.isEmpty() && !blockState.isFaceSturdy(level, blockPos, Direction.UP) ? fluidState.createLegacyBlock() : blockState;
    }

    public static void update(Level level, double X, double Z, MapItemSavedData mapItemSavedData) {
        if (level.dimension() == mapItemSavedData.dimension) {
            int i = 1 << mapItemSavedData.scale;
            int j = mapItemSavedData.centerX;
            int k = mapItemSavedData.centerZ;
            int l = Mth.floor(X - (double)j) / i + 64;
            int m = Mth.floor(Z - (double)k) / i + 64;
            int n = 128 / i;
            if (level.dimensionType().hasCeiling()) {
                n /= 2;
            }

            BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
            BlockPos.MutableBlockPos mutableBlockPos2 = new BlockPos.MutableBlockPos();
            boolean bl = false;

            for(int o = l - n + 1; o < l + n; ++o) {
                bl = false;
                double d = 0.0;
                for(int p = m - n - 1; p < m + n; ++p) {
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
                                for(u = 0; u < i; ++u) {
                                    for(int v = 0; v < i; ++v) {
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
                                            } while(blockState.getMapColor(level, mutableBlockPos) == MapColor.NONE && w > level.getMinBuildHeight());

                                            if (w > level.getMinBuildHeight() && !blockState.getFluidState().isEmpty()) {
                                                int x = w - 1;
                                                mutableBlockPos2.set(mutableBlockPos);

                                                BlockState blockState2;
                                                do {
                                                    mutableBlockPos2.setY(x--);
                                                    blockState2 = levelChunk.getBlockState(mutableBlockPos2);
                                                    ++t;
                                                } while(x > level.getMinBuildHeight() && !blockState2.getFluidState().isEmpty());

                                                blockState = getCorrectStateForFluidBlock(level, blockState, mutableBlockPos);
                                            }
                                        }

                                        mapItemSavedData.checkBanners(level, mutableBlockPos.getX(), mutableBlockPos.getZ());
                                        e += (double)w / (double)(i * i);
                                        multiset.add(blockState.getMapColor(level, mutableBlockPos));
                                    }
                                }
                            }

                            t /= i * i;
                            MapColor mapColor = (MapColor) Iterables.getFirst(Multisets.copyHighestCountFirst(multiset), MapColor.NONE);
                            MapColor.Brightness brightness;
                            double f;
                            if (mapColor == MapColor.WATER) {
                                f = (double)t * 0.1 + (double)(o + p & 1) * 0.2;
                                if (f < 0.5) {
                                    brightness = MapColor.Brightness.HIGH;
                                } else if (f > 0.9) {
                                    brightness = MapColor.Brightness.LOW;
                                } else {
                                    brightness = MapColor.Brightness.NORMAL;
                                }
                            } else {
                                f = (e - d) * 4.0 / (double)(i + 4) + ((double)(o + p & 1) - 0.5) * 0.4;
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
}
