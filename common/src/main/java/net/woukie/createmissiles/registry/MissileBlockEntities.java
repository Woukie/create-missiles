package net.woukie.createmissiles.registry;

import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.util.entry.BlockEntityEntry;
import net.woukie.createmissiles.CreateMissiles;
import net.woukie.createmissiles.block.launchpad.LaunchPadCogInstance;
import net.woukie.createmissiles.block.launchpad.LaunchPadBlockEntity;
import net.woukie.createmissiles.block.launchpadcontroller.LaunchPadControllerBlockEntity;

public class MissileBlockEntities {
    private static final CreateRegistrate REGISTRATE = CreateMissiles.registrate();

    public static final BlockEntityEntry<LaunchPadBlockEntity> LAUNCH_PAD = REGISTRATE
            .blockEntity("launch_pad", LaunchPadBlockEntity::new)
            .instance(() -> LaunchPadCogInstance::new, false)
            .validBlock(MissileBlocks.LAUNCH_PAD)
            .register();

    public static final BlockEntityEntry<LaunchPadControllerBlockEntity> LAUNCH_PAD_CONTROLLER = REGISTRATE
            .blockEntity("launch_pad_controller", LaunchPadControllerBlockEntity::new)
            .validBlock(MissileBlocks.LAUNCH_PAD_CONTROLLER)
            .register();

    public static void init() {
        CreateMissiles.LOGGER.info("Registering block entities for " + CreateMissiles.NAME);
    }
}
