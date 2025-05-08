package net.woukie.createmissiles.missilemanager.parts;

import net.woukie.createmissiles.missilemanager.Trajectory;

public class Warhead {
    public String name;
    public float weight;
    public Detonatable detonatable;

    public Warhead(String name, float weight, Detonatable detonatable) {
        this.name = name;
        this.weight = weight;
        this.detonatable = detonatable;
    }

    public interface Detonatable {
        void detonate(Trajectory trajectory);
    }
}