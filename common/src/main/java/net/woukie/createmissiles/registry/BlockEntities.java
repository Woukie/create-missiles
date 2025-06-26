package net.woukie.createmissiles.registry;

import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.util.entry.BlockEntityEntry;
import net.woukie.createmissiles.CreateMissiles;
import net.woukie.createmissiles.block.launchpad.LaunchPadCogInstance;
import net.woukie.createmissiles.block.launchpad.LaunchPadBlockEntity;
import net.woukie.createmissiles.block.controller.ControllerBlockEntity;
import net.woukie.createmissiles.block.navigator.NavigatorBlockEntity;
import net.woukie.createmissiles.block.schematicator.SchematicatorBlockEntity;

public class BlockEntities {
    private static final CreateRegistrate REGISTRATE = CreateMissiles.registrate();

    public static final BlockEntityEntry<LaunchPadBlockEntity> LAUNCH_PAD = REGISTRATE
            .blockEntity("launch_pad", LaunchPadBlockEntity::new)
            .instance(() -> LaunchPadCogInstance::new, false)
            .validBlock(Blocks.LAUNCH_PAD)
            .register();

    public static final BlockEntityEntry<ControllerBlockEntity> CONTROLLER = REGISTRATE
            .blockEntity("controller", ControllerBlockEntity::new)
            .validBlock(Blocks.CONTROLLER)
            .register();

    public static final BlockEntityEntry<SchematicatorBlockEntity> SCHEMATICATOR = REGISTRATE
            .blockEntity("schematicator", SchematicatorBlockEntity::new)
            .validBlock(Blocks.SCHEMATICATOR)
            .register();

    public static final BlockEntityEntry<NavigatorBlockEntity> NAVIGATOR = REGISTRATE
            .blockEntity("navigator", NavigatorBlockEntity::new)
            .validBlock(Blocks.NAVIGATOR)
            .register();

    public static void init() {
        CreateMissiles.LOGGER.info("Registering block entities for " + CreateMissiles.NAME);
    }
}
