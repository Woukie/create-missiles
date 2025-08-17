package net.woukie.createmissiles.missilemanager.parts;

public abstract class ChassisType extends MissilePartType {
    @Override
    public int getStartSlot() {
        return 32;
    }

    @Override
    public int getEndSlot() {
        return 64;
    }

    public abstract float getFuelCapacity();
}