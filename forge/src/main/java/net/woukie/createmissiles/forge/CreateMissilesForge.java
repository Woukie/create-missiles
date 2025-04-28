package net.woukie.createmissiles.forge;

import dev.architectury.platform.forge.EventBuses;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import net.woukie.createmissiles.CreateMissiles;

@Mod(CreateMissiles.MOD_ID)
public final class CreateMissilesForge {
    public CreateMissilesForge() {
        // Submit our event bus to let Architectury API register our content on the right time.
        EventBuses.registerModEventBus(CreateMissiles.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());

        // Run our common setup.
        CreateMissiles.init();
    }
}
