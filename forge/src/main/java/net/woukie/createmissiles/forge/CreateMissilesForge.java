package net.woukie.createmissiles.forge;

import net.woukie.createmissiles.CreateMissiles;
import net.woukie.createmissiles.registry.MissileBlocks;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(CreateMissiles.MOD_ID)
public class CreateMissilesForge {
    public CreateMissilesForge() {
        // registrate must be given the mod event bus on forge before registration
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        CreateMissiles.registrate().registerEventListeners(eventBus);
        CreateMissiles.init();
    }
}
