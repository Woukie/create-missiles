package net.woukie.createmissiles.missilemanager.asyncexplosionhandler;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.storage.DimensionDataStorage;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

public class ExplosionHandler extends SavedData {
    private List<Explosion> explosions = new ArrayList<>();
    private static MinecraftServer server;
    private static ExplosionHandler instance;
    private static boolean initialized = false;
    private static boolean destroyOnSave = false;

    private ExplosionHandler() {}

    public static ExplosionHandler get() {
        if (instance == null)
            instance = new ExplosionHandler();
        return instance;
    }

    public void createExplosion(Explosion explosion) {
        this.explosions.add(explosion);
        setDirty();
    }

    @Override
    public CompoundTag save(CompoundTag compoundTag) {
        var explosions = new ListTag();
        explosions.addAll(this.explosions.stream().map(Explosion::save).toList());
        compoundTag.put("Explosions", explosions);

        if (destroyOnSave) {
            destroyOnSave = false;
            initialized = false;
            server = null;
            this.explosions.clear();
            instance = null;
        }

        return compoundTag;
    }

    public void stop() {
        if (!initialized) return;
        initialized = false;

        setDirty();
        destroyOnSave = true;
    }

    public void serverTick(MinecraftServer server) {
        explosions.removeIf(explosion -> {
            explosion.serverTick(server);
            if (explosion.isComplete()) {
                setDirty();
                return true;
            }
            return false;
        });
    }

    public void init(MinecraftServer server) {
        if (initialized)
            return;
        initialized = true;

        ExplosionHandler.server = server;
        DimensionDataStorage storage = server.overworld().getDataStorage();
        storage.computeIfAbsent(this::load, () -> this, "explosions");
    }

    public ExplosionHandler load(CompoundTag nbt) {
//        this.explosions = new ArrayList<>(nbt.getList("Explosions", 10).stream().map(tag -> Explosion.loadFrom((CompoundTag) tag)).toList());
        return this;
    }
}
