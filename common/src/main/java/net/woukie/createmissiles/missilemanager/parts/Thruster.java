package net.woukie.createmissiles.missilemanager.parts;

import net.minecraft.nbt.CompoundTag;

public class Thruster implements MissilePart {
    public String name;
    public float thrust;
    public float burnRate;

    public Thruster(String name, float thrust, float burnRate) {
        this.name = name;
        this.thrust = thrust;
        this.burnRate = burnRate;
    }

    @Override
    public CompoundTag saveTo(CompoundTag tag) {
        tag.putString("name", name);
        tag.putFloat("thrust", thrust);
        tag.putFloat("burnRate", burnRate);
        return tag;
    }

    @Override
    public void loadFrom(CompoundTag tag) {
        this.name = tag.getString("name");
        this.thrust = tag.getFloat("thrust");
        this.burnRate = tag.getFloat("burnRate");
    }
}