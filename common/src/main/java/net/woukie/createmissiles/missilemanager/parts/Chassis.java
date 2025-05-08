package net.woukie.createmissiles.missilemanager.parts;

import net.minecraft.nbt.CompoundTag;

public class Chassis implements MissilePart {
    public String name;
    public float fuelCapacity;

    public Chassis(String name, float fuelCapacity) {
        this.name = name;
        this.fuelCapacity = fuelCapacity;
    }

    @Override
    public CompoundTag saveTo(CompoundTag tag) {
        tag.putString("name", name);
        tag.putFloat("fuelCapacity", fuelCapacity);
        return tag;
    }

    @Override
    public void loadFrom(CompoundTag tag) {
        this.name = tag.getString("Name");
        this.fuelCapacity = tag.getFloat("FuelCapacity");
    }
}