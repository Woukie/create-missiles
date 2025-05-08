package net.woukie.createmissiles.missilemanager.parts;

import net.minecraft.nbt.CompoundTag;
import net.woukie.createmissiles.missilemanager.Trajectory;

public class Warhead implements MissilePart {
    public String name;
    public float weight;
    public Detonatable detonatable;

    public Warhead(String name, float weight, Detonatable detonatable) {
        this.name = name;
        this.weight = weight;
        this.detonatable = detonatable;
    }

    @Override
    public CompoundTag saveTo(CompoundTag tag) {
        tag.putString("name", name);
        tag.putFloat("weight", weight);
        return tag;
    }

    @Override
    public void loadFrom(CompoundTag tag) {
        name = tag.getString("name");
        weight = tag.getFloat("weight");
    }

    public interface Detonatable {
        void detonate(Trajectory trajectory);
    }
}