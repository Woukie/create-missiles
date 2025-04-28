package net.woukie.createmissiles.fabric;

import net.fabricmc.api.ModInitializer;

import net.woukie.createmissiles.CreateMissiles;

public final class CreateMissilesFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        // This code runs as soon as Minecraft is in a mod-load-ready state.
        // However, some things (like resources) may still be uninitialized.
        // Proceed with mild caution.

        // Run our common setup.
        CreateMissiles.init();
    }
}
