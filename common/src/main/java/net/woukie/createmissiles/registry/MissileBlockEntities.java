package net.woukie.createmissiles.registry;

import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.util.entry.BlockEntityEntry;
import net.woukie.createmissiles.CreateMissiles;
import net.woukie.createmissiles.block.LaunchPadCogInstance;
import net.woukie.createmissiles.block.LaunchPadBlockEntity;
import net.woukie.createmissiles.renderer.LaunchPadRenderer;

public class MissileBlockEntities {
    private static final CreateRegistrate REGISTRATE = CreateMissiles.registrate();

    public static final BlockEntityEntry<LaunchPadBlockEntity> LAUNCH_PAD = REGISTRATE
            .blockEntity("launch_pad", LaunchPadBlockEntity::new)
            .instance(() -> LaunchPadCogInstance::new, false)
            .validBlock(MissileBlocks.LAUNCH_PAD)
            .renderer(() -> LaunchPadRenderer::new)
            .register();

    public static void init() {
        CreateMissiles.LOGGER.info("Registering block entities for " + CreateMissiles.NAME);
    }
}
