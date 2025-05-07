package net.woukie.createmissiles.missilemanager.parts;

public class Thruster {
    public String name;
    public float speed;
    public float fuelEfficiency;

    public Thruster(String name, float speed, float fuelEfficiency) {
        this.name = name;
        this.speed = speed;
        this.fuelEfficiency = fuelEfficiency;
    }
}