package net.woukie.createmissiles.missilemanager.parts;

public class Thruster {
    public String name;
    public float thrust;
    public float burnRate;

    public Thruster(String name, float thrust, float burnRate) {
        this.name = name;
        this.thrust = thrust;
        this.burnRate = burnRate;
    }
}