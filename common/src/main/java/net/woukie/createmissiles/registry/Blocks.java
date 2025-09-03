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
import net.woukie.createmissiles.block.AnnoyingJukeboxBlock;
import net.woukie.createmissiles.block.InfernalAshLayer;
import net.woukie.createmissiles.block.FlamingFireBlock;
import net.woukie.createmissiles.block.assemblypanel.AssemblyPanelBlock;
import net.woukie.createmissiles.block.controlpanel.ControlPanelBlock;
import net.woukie.createmissiles.block.launchpad.LaunchPadBlock;
import net.woukie.createmissiles.block.launchpad.LaunchPadCTBehaviour;
import net.woukie.createmissiles.block.navigationpanel.NavigationPanelBlock;

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

    public static final BlockEntry<FlamingFireBlock> FLAMING_FIRE = REGISTRATE
            .block("flaming_fire", FlamingFireBlock::new)
            .initialProperties(() -> net.minecraft.world.level.block.Blocks.FIRE)
            .addLayer(() -> RenderType::cutoutMipped)
            .register();

    public static final BlockEntry<AnnoyingJukeboxBlock> ANNOYING_JUKEBOX = REGISTRATE
            .block("annoying_jukebox", AnnoyingJukeboxBlock::new)
            .initialProperties(() -> net.minecraft.world.level.block.Blocks.JUKEBOX)
            .properties(BlockBehaviour.Properties::noOcclusion)
            .properties(properties -> properties.destroyTime(150))
            .properties(properties -> properties.explosionResistance(60))
            .addLayer(() -> RenderType::cutoutMipped)
            .simpleItem()
            .register();

    public static final BlockEntry<InfernalAshLayer> INFERNAL_ASH = REGISTRATE
            .block("infernal_ash", InfernalAshLayer::new)
            .initialProperties(() -> net.minecraft.world.level.block.Blocks.SNOW)
            .simpleItem()
            .register();

    @SuppressWarnings({"Experimental", "UnstableApiUsage"})
    public static void init() {
        CreateMissiles.LOGGER.info("Registering blocks for " + CreateMissiles.NAME);

        CreativeTabRegistry.appendStack(CreativeMenus.ASSEMBLIES_TAB, () -> new ItemStack(LAUNCH_PAD));
        CreativeTabRegistry.appendStack(CreativeMenus.ASSEMBLIES_TAB, () -> new ItemStack(ASSEMBLY_PANEL));
        CreativeTabRegistry.appendStack(CreativeMenus.ASSEMBLIES_TAB, () -> new ItemStack(CONTROL_PANEL));
        CreativeTabRegistry.appendStack(CreativeMenus.ASSEMBLIES_TAB, () -> new ItemStack(NAVIGATION_PANEL));
        CreativeTabRegistry.appendStack(CreativeMenus.ASSEMBLIES_TAB, () -> new ItemStack(ANNOYING_JUKEBOX));
        CreativeTabRegistry.appendStack(CreativeMenus.ASSEMBLIES_TAB, () -> new ItemStack(INFERNAL_ASH));
    }
}
