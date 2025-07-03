package net.woukie.createmissiles.registry;

import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.util.entry.BlockEntityEntry;
import net.woukie.createmissiles.CreateMissiles;
import net.woukie.createmissiles.block.launchpad.LaunchPadCogInstance;
import net.woukie.createmissiles.block.launchpad.LaunchPadBlockEntity;
import net.woukie.createmissiles.block.controlpanel.ControlPanelBlockEntity;
import net.woukie.createmissiles.block.navigation_panel.NavigationPanelBlockEntity;
import net.woukie.createmissiles.block.assemblypanel.AssemblyPanelBlockEntity;

public class BlockEntities {
    private static final CreateRegistrate REGISTRATE = CreateMissiles.registrate();

    public static final BlockEntityEntry<LaunchPadBlockEntity> LAUNCH_PAD = REGISTRATE
            .blockEntity("launch_pad", LaunchPadBlockEntity::new)
            .instance(() -> LaunchPadCogInstance::new, false)
            .validBlock(Blocks.LAUNCH_PAD)
            .register();

    public static final BlockEntityEntry<ControlPanelBlockEntity> CONTROL_PANEL = REGISTRATE
            .blockEntity("control_panel", ControlPanelBlockEntity::new)
            .validBlock(Blocks.CONTROL_PANEL)
            .register();

    public static final BlockEntityEntry<AssemblyPanelBlockEntity> ASSEMBLY_PANEL = REGISTRATE
            .blockEntity("assembly_panel", AssemblyPanelBlockEntity::new)
            .validBlock(Blocks.ASSEMBLY_PANEL)
            .register();

    public static final BlockEntityEntry<NavigationPanelBlockEntity> NAVIGATION_PANEL = REGISTRATE
            .blockEntity("navigation_panel", NavigationPanelBlockEntity::new)
            .validBlock(Blocks.NAVIGATION_PANEL)
            .register();

    public static void init() {
        CreateMissiles.LOGGER.info("Registering block entities for " + CreateMissiles.NAME);
    }
}
