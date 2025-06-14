package net.woukie.createmissiles.entity;

import net.minecraft.world.entity.Entity;

import java.util.UUID;
import java.util.WeakHashMap;

public class MissileEntityManager {
    private static final WeakHashMap<UUID, MissileEntity> controllerEntities = new WeakHashMap<>() {};

    public static MissileEntity get(UUID trackingID) {
        return controllerEntities.get(trackingID);
    }

    public static void add(MissileEntity be) {
        controllerEntities.put(be.getControllerID().get(), be);
    }

    public static void remove(UUID trackingID) {
        var entity = controllerEntities.get(trackingID);
        if (entity == null) return;

        entity.remove(Entity.RemovalReason.KILLED);
        controllerEntities.remove(trackingID);
    }
}
