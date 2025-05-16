package net.woukie.createmissiles.registry;

import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.util.entry.BlockEntityEntry;
import net.woukie.createmissiles.CreateMissiles;
import net.woukie.createmissiles.block.launchpad.LaunchPadCogInstance;
import net.woukie.createmissiles.block.launchpad.LaunchPadBlockEntity;
import net.woukie.createmissiles.block.controller.ControllerBlockEntity;
import net.woukie.createmissiles.block.navigator.NavigatorBlockEntity;
import net.woukie.createmissiles.block.schematicator.SchematicatorBlockEntity;

public class MissileBlockEntities {
    private static final CreateRegistrate REGISTRATE = CreateMissiles.registrate();

    public static final BlockEntityEntry<LaunchPadBlockEntity> LAUNCH_PAD = REGISTRATE
            .blockEntity("launch_pad", LaunchPadBlockEntity::new)
            .instance(() -> LaunchPadCogInstance::new, false)
            .validBlock(MissileBlocks.LAUNCH_PAD)
            .register();

    public static final BlockEntityEntry<ControllerBlockEntity> CONTROLLER = REGISTRATE
            .blockEntity("controller", ControllerBlockEntity::new)
            .validBlock(MissileBlocks.CONTROLLER)
            .register();

    public static final BlockEntityEntry<SchematicatorBlockEntity> SCHEMATICATOR = REGISTRATE
            .blockEntity("schematicator", SchematicatorBlockEntity::new)
            .validBlock(MissileBlocks.SCHEMATICATOR)
            .register();

    public static final BlockEntityEntry<NavigatorBlockEntity> NAVIGATOR = REGISTRATE
            .blockEntity("navigator", NavigatorBlockEntity::new)
            .validBlock(MissileBlocks.NAVIGATOR)
            .register();

    public static void init() {
        CreateMissiles.LOGGER.info("Registering block entities for " + CreateMissiles.NAME);
    }
}
