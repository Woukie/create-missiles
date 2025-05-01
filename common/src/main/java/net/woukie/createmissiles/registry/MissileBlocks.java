package net.woukie.createmissiles.registry;

import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.util.entry.BlockEntry;
import net.woukie.createmissiles.CreateMissiles;
import net.minecraft.world.level.block.Block;

public class MissileBlocks {
    private static final CreateRegistrate REGISTRATE = CreateMissiles.registrate();

    public static final BlockEntry<Block> LAUNCH_PAD = REGISTRATE.block("launch_pad", Block::new).register();

    public static void init() {
        CreateMissiles.LOGGER.info("Registering blocks for " + CreateMissiles.NAME);
    }
}
