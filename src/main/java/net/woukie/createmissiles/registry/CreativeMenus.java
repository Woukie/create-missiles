package net.woukie.createmissiles.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.woukie.createmissiles.CreateMissiles;
import net.woukie.createmissiles.item.assembly.AssemblyItem;

import static net.woukie.createmissiles.registry.Blocks.*;
import static net.woukie.createmissiles.registry.Items.*;

public class CreativeMenus {
    public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, CreateMissiles.MOD_ID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> ASSEMBLIES_TAB = TABS.register(
            "createmissiles",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("category.createmissiles"))
                    .icon(() -> new ItemStack(Blocks.CONTROL_PANEL.get()))
                    .build()
            );

    public static void init() {
        CreateMissiles.LOGGER.info("Registering creative menus for " + CreateMissiles.NAME);
        TABS.register(NeoForge.EVENT_BUS);
    }

    private static ResourceLocation id(String id) {
        return ResourceLocation.fromNamespaceAndPath(CreateMissiles.MOD_ID, id);
    }

    public static void addItems(BuildCreativeModeTabContentsEvent event) {
        event.accept(new ItemStack(BOUND_ENDER_PEARL.get()));
        event.accept(new ItemStack(DRAGON_EGG_SHELL.get()));
        event.accept(new ItemStack(REINFORCED_DRAGON_EGG_SHELL.get()));
        event.accept(new ItemStack(BIOME_VIAL.get()));
        event.accept(new ItemStack(DRONE_BOX_ITEM.get()));
        event.accept(new ItemStack(REINFORCED_DRONE_BOX.get()));
        event.accept(new ItemStack(FIREWORK_UPGRADE_CORE.get()));
        event.accept(new ItemStack(EXCAVATOR_UPGRADE_CORE.get()));
        event.accept(new ItemStack(FROST_UPGRADE_CORE.get()));
        event.accept(new ItemStack(FLAMING_UPGRADE_CORE.get()));
        event.accept(AssemblyItem.createWith(id("firework_warhead"), WARHEAD_ASSEMBLY.get()));
        event.accept(AssemblyItem.createWith(id("firework_chassis"), CHASSIS_ASSEMBLY.get()));
        event.accept(AssemblyItem.createWith(id("firework_thruster"), THRUSTER_ASSEMBLY.get()));
        event.accept(AssemblyItem.createWith(id("annoying_warhead"), WARHEAD_ASSEMBLY.get()));
        event.accept(AssemblyItem.createWith(id("biome_brush_warhead"), WARHEAD_ASSEMBLY.get()));
        event.accept(AssemblyItem.createWith(id("frost_warhead"), WARHEAD_ASSEMBLY.get()));
        event.accept(AssemblyItem.createWith(id("frost_chassis"), CHASSIS_ASSEMBLY.get()));
        event.accept(AssemblyItem.createWith(id("frost_thruster"), THRUSTER_ASSEMBLY.get()));
        event.accept(AssemblyItem.createWith(id("flaming_warhead"), WARHEAD_ASSEMBLY.get()));
        event.accept(AssemblyItem.createWith(id("flaming_chassis"), CHASSIS_ASSEMBLY.get()));
        event.accept(AssemblyItem.createWith(id("flaming_thruster"), THRUSTER_ASSEMBLY.get()));
        event.accept(AssemblyItem.createWith(id("direct_hit_warhead"), WARHEAD_ASSEMBLY.get()));
        event.accept(AssemblyItem.createWith(id("excavator_warhead"), WARHEAD_ASSEMBLY.get()));
        event.accept(AssemblyItem.createWith(id("excavator_chassis"), CHASSIS_ASSEMBLY.get()));
        event.accept(AssemblyItem.createWith(id("excavator_thruster"), THRUSTER_ASSEMBLY.get()));
        event.accept(AssemblyItem.createWith(id("blazing_warhead"), WARHEAD_ASSEMBLY.get()));
        event.accept(AssemblyItem.createWith(id("frozen_warhead"), WARHEAD_ASSEMBLY.get()));
        event.accept(AssemblyItem.createWith(id("frozen_chassis"), CHASSIS_ASSEMBLY.get()));
        event.accept(AssemblyItem.createWith(id("frozen_thruster"), THRUSTER_ASSEMBLY.get()));
        event.accept(AssemblyItem.createWith(id("guardian_warhead"), WARHEAD_ASSEMBLY.get()));
        event.accept(AssemblyItem.createWith(id("tunneler_warhead"), WARHEAD_ASSEMBLY.get()));
        event.accept(AssemblyItem.createWith(id("infernal_warhead"), WARHEAD_ASSEMBLY.get()));
        event.accept(AssemblyItem.createWith(id("withered_warhead"), WARHEAD_ASSEMBLY.get()));
        event.accept(AssemblyItem.createWith(id("withered_chassis"), CHASSIS_ASSEMBLY.get()));
        event.accept(AssemblyItem.createWith(id("withered_thruster"), THRUSTER_ASSEMBLY.get()));
        event.accept(AssemblyItem.createWith(id("ancient_warhead"), WARHEAD_ASSEMBLY.get()));
        event.accept(AssemblyItem.createWith(id("ancient_chassis"), CHASSIS_ASSEMBLY.get()));
        event.accept(AssemblyItem.createWith(id("ancient_thruster"), THRUSTER_ASSEMBLY.get()));
        event.accept(AssemblyItem.createWith(id("dragon_warhead"), WARHEAD_ASSEMBLY.get()));
        event.accept(AssemblyItem.createWith(id("dragon_chassis"), CHASSIS_ASSEMBLY.get()));
        event.accept(AssemblyItem.createWith(id("dragon_thruster"), THRUSTER_ASSEMBLY.get()));
        event.accept(AssemblyItem.createWith(id("shulker_box_warhead"), WARHEAD_ASSEMBLY.get()));
        event.accept(AssemblyItem.createWith(id("teleportation_warhead"), WARHEAD_ASSEMBLY.get()));
        event.accept(AssemblyItem.createWith(id("messy_warhead"), WARHEAD_ASSEMBLY.get()));
        event.accept(new ItemStack(LAUNCH_PAD));
        event.accept(new ItemStack(ASSEMBLY_PANEL));
        event.accept(new ItemStack(CONTROL_PANEL));
        event.accept(new ItemStack(NAVIGATION_PANEL));
        event.accept(new ItemStack(ANNOYING_JUKEBOX));
        event.accept(new ItemStack(INFERNAL_ASH));
        event.accept(new ItemStack(FROST_SNOW));
    }
}
