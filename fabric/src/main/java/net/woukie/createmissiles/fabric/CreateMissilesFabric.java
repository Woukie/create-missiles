package net.woukie.createmissiles.fabric;

import io.github.fabricators_of_create.porting_lib.util.EnvExecutor;
import net.woukie.createmissiles.CreateMissiles;
import net.woukie.createmissiles.registry.MissileBlocks;
import net.fabricmc.api.ModInitializer;

public class CreateMissilesFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        CreateMissiles.init();
        ArmInteractionPointsFabric.init();
        CreateMissiles.LOGGER.info(EnvExecutor.unsafeRunForDist(
                () -> () -> "{} is accessing Porting Lib on a Fabric client!",
                () -> () -> "{} is accessing Porting Lib on a Fabric server!"
                ), CreateMissiles.NAME);
        // on fabric, Registrates must be explicitly finalized and registered.
        CreateMissiles.registrate().register();
    }
}
