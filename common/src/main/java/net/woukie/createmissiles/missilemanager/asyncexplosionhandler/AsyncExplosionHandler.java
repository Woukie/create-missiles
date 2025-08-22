package net.woukie.createmissiles.missilemanager.asyncexplosionhandler;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.storage.DimensionDataStorage;

import java.util.ArrayList;
import java.util.List;

public class AsyncExplosionHandler extends SavedData {
    private List<Explosion> explosions = new ArrayList<>();
    private static MinecraftServer server;
    private static AsyncExplosionHandler instance;
    private static boolean initialized = false;
    private static boolean destroyOnSave = false;

    private AsyncExplosionHandler() {}

    public static AsyncExplosionHandler get() {
        if (instance == null)
            instance = new AsyncExplosionHandler();
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

        AsyncExplosionHandler.server = server;
        DimensionDataStorage storage = server.overworld().getDataStorage();
        storage.computeIfAbsent(this::load, () -> this, "explosions");
    }

    public AsyncExplosionHandler load(CompoundTag nbt) {
        this.explosions = new ArrayList<>(nbt.getList("Explosions", 10).stream().map(tag -> Explosion.loadFrom((CompoundTag) tag)).toList());
        return this;
    }
}
