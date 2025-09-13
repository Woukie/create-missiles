package net.woukie.createmissiles.missiles.asyncexplosionhandler;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.FastColor;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.storage.DimensionDataStorage;
import net.woukie.createmissiles.CreateMissiles;
import net.woukie.createmissiles.client.CreateFlashMessage;
import net.woukie.createmissiles.registry.Packets;
import org.jetbrains.annotations.NotNull;

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
        this.createExplosion(explosion, FastColor.ARGB32.color(255, 255, 255, 251), 1000);
    }

    public void createExplosion(Explosion explosion, Integer colour, Integer length) {
        this.explosions.add(explosion);
        explosion.damageEntities();
        explosion.startCrunching();
        explosion.getLevel().players().forEach(player -> {
            double distance = player.position().distanceTo(explosion.getOrigin().getCenter());
            if (distance < explosion.getMaxRadius() * 4) {
                Packets.CREATE_FLASH.sendToPlayer((ServerPlayer) player, new CreateFlashMessage(colour, explosion.getOrigin(), explosion.getMaxRadius(), length));
            }
        });
        setDirty();
    }

    @Override
    public @NotNull CompoundTag save(CompoundTag compoundTag) {
        CreateMissiles.LOGGER.info("Saving explosions");
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

        this.explosions.forEach(Explosion::stopCrunching);
        setDirty();
        destroyOnSave = true;
    }

    public void serverTick(MinecraftServer server) {
        explosions.removeIf(explosion -> {
            explosion.serverTick(server);
            if (explosion.isComplete()) {
                CreateMissiles.LOGGER.info("Explosion at {} is complete", explosion.getOrigin().toShortString());
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
        storage.computeIfAbsent(this::load, () -> this, "Explosions");
    }

    public ExplosionHandler load(CompoundTag nbt) {
        this.explosions = new ArrayList<>(nbt.getList("Explosions", 10).stream().map(tag -> Explosion.load((CompoundTag) tag, server)).toList());
        CreateMissiles.LOGGER.info("Explosions loaded");
        this.explosions.forEach(Explosion::startCrunching);
        return this;
    }
}
