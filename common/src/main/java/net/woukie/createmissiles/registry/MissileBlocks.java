package net.woukie.createmissiles.registry;

import com.simibubi.create.content.kinetics.BlockStressDefaults;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.data.SharedProperties;
import com.tterrag.registrate.util.entry.BlockEntry;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.woukie.createmissiles.CreateMissiles;
import net.woukie.createmissiles.block.LaunchPadBlock;

public class MissileBlocks {
    private static final CreateRegistrate REGISTRATE = CreateMissiles.registrate();

    public static final BlockEntry<LaunchPadBlock> LAUNCH_PAD = REGISTRATE
            .block("launch_pad", LaunchPadBlock::new)
            .initialProperties(SharedProperties::softMetal)
            .properties(p -> p.mapColor(MapColor.COLOR_LIGHT_GRAY))
            .properties(BlockBehaviour.Properties::noOcclusion)
            .transform(BlockStressDefaults.setImpact(4))
            .simpleItem()
            .register();

    public static void init() {
        CreateMissiles.LOGGER.info("Registering blocks for " + CreateMissiles.NAME);
    }
}
