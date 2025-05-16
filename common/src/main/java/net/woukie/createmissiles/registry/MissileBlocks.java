package net.woukie.createmissiles.registry;

import com.simibubi.create.content.kinetics.BlockStressDefaults;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.data.SharedProperties;
import com.tterrag.registrate.util.entry.BlockEntry;
import dev.architectury.registry.CreativeTabRegistry;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.woukie.createmissiles.CreateMissiles;
import net.woukie.createmissiles.block.launchpad.LaunchPadBlock;
import net.woukie.createmissiles.block.controller.ControllerBlock;
import net.woukie.createmissiles.block.navigator.NavigatorBlock;
import net.woukie.createmissiles.block.schematicator.SchematicatorBlock;

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

    public static final BlockEntry<ControllerBlock> CONTROLLER = REGISTRATE
            .block("controller", ControllerBlock::new)
            .initialProperties(SharedProperties::softMetal)
            .properties(p -> p.mapColor(MapColor.COLOR_BROWN))
            .properties(BlockBehaviour.Properties::noOcclusion)
            .simpleItem()
            .register();

    public static final BlockEntry<SchematicatorBlock> SCHEMATICATOR = REGISTRATE
            .block("schematicator", SchematicatorBlock::new)
            .initialProperties(SharedProperties::softMetal)
            .properties(p -> p.mapColor(MapColor.COLOR_BROWN))
            .properties(BlockBehaviour.Properties::noOcclusion)
            .simpleItem()
            .register();

    public static final BlockEntry<NavigatorBlock> NAVIGATOR = REGISTRATE
            .block("navigator", NavigatorBlock::new)
            .initialProperties(SharedProperties::softMetal)
            .properties(p -> p.mapColor(MapColor.COLOR_BROWN))
            .properties(BlockBehaviour.Properties::noOcclusion)
            .simpleItem()
            .register();

    public static void init() {
        CreateMissiles.LOGGER.info("Registering blocks for " + CreateMissiles.NAME);

        CreativeTabRegistry.appendStack(MissileCreativeMenu.SCHEMATICS_TAB, () -> new ItemStack(SCHEMATICATOR));
        CreativeTabRegistry.appendStack(MissileCreativeMenu.SCHEMATICS_TAB, () -> new ItemStack(LAUNCH_PAD));
        CreativeTabRegistry.appendStack(MissileCreativeMenu.SCHEMATICS_TAB, () -> new ItemStack(CONTROLLER));
        CreativeTabRegistry.appendStack(MissileCreativeMenu.SCHEMATICS_TAB, () -> new ItemStack(NAVIGATOR));
    }
}
