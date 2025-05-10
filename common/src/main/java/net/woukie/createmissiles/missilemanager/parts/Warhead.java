package net.woukie.createmissiles.missilemanager.parts;

import net.minecraft.network.chat.Component;
import net.woukie.createmissiles.missilemanager.Trajectory;

public interface Warhead extends MissilePart {
    float getWeight();
    Detonatable getDetonatable();

    interface Detonatable {
        void detonate(Trajectory trajectory);
    }
}
