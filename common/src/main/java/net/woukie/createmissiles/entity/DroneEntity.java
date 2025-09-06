package net.woukie.createmissiles.entity;
import com.google.common.collect.Iterables;
import com.google.common.collect.LinkedHashMultiset;
import com.google.common.collect.Multiset;
import com.google.common.collect.Multisets;
import net.minecraft.core.*;

import net.minecraft.core.Direction;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.*;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.FlyingMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import net.minecraft.world.phys.Vec3;
import net.woukie.createmissiles.inventory.DroneMenu;
import net.woukie.createmissiles.registry.Items;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class DroneEntity extends FlyingMob{
    private enum Stage { IDLE, ASCENDING, TO_TARGET, RETURNING, DESCENDING }
    private Stage stage = Stage.IDLE;
    private Vec3 homePos;
    private Vec3 targetPos;
    private final double cruiseAltitude = 120.0;
    private final double speed = 0.1;

    public DroneEntity(EntityType<? extends DroneEntity> entityType, Level level) {
        super(entityType, level);
        this.setNoGravity(true);
        this.noPhysics = true;
        startJourney();
    }

    protected float getStandingEyeHeight(@NotNull Pose pose, EntityDimensions entityDimensions) {
        return entityDimensions.height * 0.35F;
    }

    @Override
    public void tick() {
        super.tick();
        Vec3 current = position();

        switch (stage) {
            case ASCENDING -> {
                if (current.y < cruiseAltitude) {
                    setDeltaMovement(0, speed, 0);
                } else {
                    stage = Stage.TO_TARGET;
                }
            }

            case TO_TARGET -> {
                flyToward(targetPos);
                if (current.distanceTo(targetPos) < 2.0) stage = Stage.RETURNING;
            }

            case RETURNING -> {
                flyToward(homePos);
                if (current.distanceTo(homePos) < 2.0) stage = Stage.DESCENDING;
            }

            case DESCENDING -> {
                if (current.y > homePos.y) {
                    setDeltaMovement(0, -speed, 0);
                } else {
                    stage = Stage.IDLE;
                    setDeltaMovement(Vec3.ZERO);
                }
            }

            default -> setDeltaMovement(Vec3.ZERO);
        }

        move(MoverType.SELF, getDeltaMovement());

        //mapItemStack = createAndFillMap(this.level(), (int)this.getX(), (int)this.getZ(), 0);
    }

    private void flyToward(Vec3 dest) {
        Vec3 dir = new Vec3(dest.x - position().x, 0, dest.z - position().z).normalize();
        setDeltaMovement(dir.scale(speed));
    }

    public void startJourney() {
        this.homePos = this.position();
        this.targetPos = new Vec3(homePos.x + 50, cruiseAltitude, homePos.z + 50);
        this.stage = Stage.ASCENDING;
    }

    private static final EntityDataAccessor<Rotations> ROTATION =
            SynchedEntityData.defineId(DroneEntity.class, EntityDataSerializers.ROTATIONS);

    @Override
    protected void defineSynchedData() {
        this.entityData.define(ROTATION, new Rotations(0, 0, 0));
        super.defineSynchedData();
    }

    @Override
    public boolean isAttackable() {
        return false;
    }

    public void aiStep() {
        if (this.isAlive() && this.isSunBurnTick()) {
            this.setSecondsOnFire(8);
        }

        super.aiStep();
    }

    @Override
    public boolean hurt(DamageSource damageSource, float f) {
        Entity entity = damageSource.getEntity();
        System.out.println(entity);
        if (!level().isClientSide() && entity != null && entity.getType().equals(EntityType.PLAYER)) {
            level().playSound(null, blockPosition(), SoundEvents.SCAFFOLDING_BREAK, SoundSource.NEUTRAL);
            DefaultDispenseItemBehavior.spawnItem(level(), new ItemStack(Items.DRONE_BOX_ITEM.get()), 1, Direction.UP, position());
            this.discard();
        }
        return super.hurt(damageSource, f);
    }

    @Override
    public boolean isPickable() {
        return true;
    }

    @Override
    protected @NotNull InteractionResult mobInteract(@NotNull Player player, @NotNull InteractionHand interactionHand) {
        if (!this.level().isClientSide) {
            player.openMenu(new SimpleMenuProvider((ix, inventory, playerx) -> new DroneMenu(ix, inventory), Component.literal("")));
        }
        return super.mobInteract(player, interactionHand);
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
