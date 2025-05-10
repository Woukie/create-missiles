package net.woukie.createmissiles.missilemanager.parts;

public interface Thruster extends MissilePart {
    float getThrust();
    float getBurnRate();
}