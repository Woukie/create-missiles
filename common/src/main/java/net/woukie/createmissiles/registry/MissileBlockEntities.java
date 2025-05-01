package net.woukie.createmissiles.registry;

import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.util.entry.BlockEntityEntry;
import net.woukie.createmissiles.CreateMissiles;
import net.woukie.createmissiles.block.launchpad.LaunchPadCogInstance;
import net.woukie.createmissiles.block.launchpad.LaunchPadBlockEntity;

public class MissileBlockEntities {
    private static final CreateRegistrate REGISTRATE = CreateMissiles.registrate();

    public static final BlockEntityEntry<LaunchPadBlockEntity> LAUNCH_PAD = REGISTRATE
            .blockEntity("launch_pad", LaunchPadBlockEntity::new)
            .instance(() -> LaunchPadCogInstance::new, false)
            .validBlock(MissileBlocks.LAUNCH_PAD)
            .register();

    public static void init() {
        CreateMissiles.LOGGER.info("Registering block entities for " + CreateMissiles.NAME);
    }
}
