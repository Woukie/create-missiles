package net.woukie.createmissiles.entity;

import net.minecraft.world.entity.Entity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.WeakHashMap;

public class MissileEntityManager {
    private static final List<UUID> ownerIDs = new ArrayList<>();
    private static final WeakHashMap<UUID, MissileEntity> entities = new WeakHashMap<>() {};

    public static MissileEntity getEntity(UUID trackingID) {
        return entities.get(trackingID);
    }

    public static void addEntity(MissileEntity be) {
        entities.put(be.getOwnerId().get(), be);
    }

    public static void removeEntity(UUID trackingID) {
        var entity = entities.get(trackingID);
        if (entity == null) return;

        entity.remove(Entity.RemovalReason.KILLED);
        entities.remove(trackingID);
    }

    public static void addOwner(UUID trackingId) {
        ownerIDs.add(trackingId);
    }

    public static void removeOwner(UUID missileEntityTrackingID) {
        ownerIDs.remove(missileEntityTrackingID);
        removeEntity(missileEntityTrackingID);
    }

    public static boolean hasOwner(UUID uuid) {
        return ownerIDs.contains(uuid);
    }
}
