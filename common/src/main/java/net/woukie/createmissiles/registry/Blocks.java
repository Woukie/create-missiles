package net.woukie.createmissiles.registry;

import com.simibubi.create.content.kinetics.BlockStressDefaults;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.data.SharedProperties;
import com.tterrag.registrate.util.entry.BlockEntry;
import dev.architectury.registry.CreativeTabRegistry;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.woukie.createmissiles.CreateMissiles;
import net.woukie.createmissiles.block.launchpad.LaunchPadBlock;
import net.woukie.createmissiles.block.controlpanel.ControlPanelBlock;
import net.woukie.createmissiles.block.launchpad.LaunchPadCTBehaviour;
import net.woukie.createmissiles.block.navigation_panel.NavigationPanelBlock;
import net.woukie.createmissiles.block.assemblypanel.AssemblyPanelBlock;

public class Blocks {
    private static final CreateRegistrate REGISTRATE = CreateMissiles.registrate();

    public static final BlockEntry<LaunchPadBlock> LAUNCH_PAD = REGISTRATE
            .block("launch_pad", LaunchPadBlock::new)
            .initialProperties(SharedProperties::softMetal)
            .properties(p -> p.mapColor(MapColor.COLOR_LIGHT_GRAY))
            .properties(BlockBehaviour.Properties::noOcclusion)
            .transform(BlockStressDefaults.setImpact(4))
            .onRegister(CreateRegistrate.connectedTextures(LaunchPadCTBehaviour::new))
            .addLayer(() -> RenderType::cutoutMipped)
            .simpleItem()
            .register();

    public static final BlockEntry<ControlPanelBlock> CONTROL_PANEL = REGISTRATE
            .block("control_panel", ControlPanelBlock::new)
            .initialProperties(SharedProperties::softMetal)
            .properties(p -> p.mapColor(MapColor.COLOR_BROWN))
            .properties(BlockBehaviour.Properties::noOcclusion)
            .simpleItem()
            .register();

    public static final BlockEntry<AssemblyPanelBlock> ASSEMBLY_PANEL = REGISTRATE
            .block("assembly_panel", AssemblyPanelBlock::new)
            .initialProperties(SharedProperties::softMetal)
            .properties(p -> p.mapColor(MapColor.COLOR_BROWN))
            .properties(BlockBehaviour.Properties::noOcclusion)
            .simpleItem()
            .register();

    public static final BlockEntry<NavigationPanelBlock> NAVIGATION_PANEL = REGISTRATE
            .block("navigation_panel", NavigationPanelBlock::new)
            .initialProperties(SharedProperties::softMetal)
            .properties(p -> p.mapColor(MapColor.COLOR_BROWN))
            .properties(BlockBehaviour.Properties::noOcclusion)
            .simpleItem()
            .register();

    public static void init() {
        CreateMissiles.LOGGER.info("Registering blocks for " + CreateMissiles.NAME);

        CreativeTabRegistry.appendStack(CreativeMenus.ASSEMBLIES_TAB, () -> new ItemStack(LAUNCH_PAD));
        CreativeTabRegistry.appendStack(CreativeMenus.ASSEMBLIES_TAB, () -> new ItemStack(ASSEMBLY_PANEL));
        CreativeTabRegistry.appendStack(CreativeMenus.ASSEMBLIES_TAB, () -> new ItemStack(CONTROL_PANEL));
        CreativeTabRegistry.appendStack(CreativeMenus.ASSEMBLIES_TAB, () -> new ItemStack(NAVIGATION_PANEL));
    }
}
