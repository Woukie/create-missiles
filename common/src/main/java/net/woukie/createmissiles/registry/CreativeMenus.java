package net.woukie.createmissiles.registry;

import dev.architectury.registry.CreativeTabRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.woukie.createmissiles.CreateMissiles;
import net.woukie.createmissiles.item.assembly.AssemblyItem;

import static net.woukie.createmissiles.registry.Items.*;

public class CreativeMenus {
    public static final DeferredRegister<CreativeModeTab> TABS =
            DeferredRegister.create(CreateMissiles.MOD_ID, Registries.CREATIVE_MODE_TAB);

    public static final RegistrySupplier<CreativeModeTab> ASSEMBLIES_TAB = TABS.register(
            "createmissiles",
            () -> CreativeTabRegistry.create(
                    Component.translatable("category.createmissiles"),
                    () -> new ItemStack(Blocks.CONTROL_PANEL.get())
            )
    );

    public static void init() {
        CreateMissiles.LOGGER.info("Registering creative menus for " + CreateMissiles.NAME);

        CreativeTabRegistry.appendStack(CreativeMenus.ASSEMBLIES_TAB, () -> AssemblyItem.createWith(id("bound_ender_pearl"), BOUND_ENDER_PEARL.get()));
        CreativeTabRegistry.appendStack(CreativeMenus.ASSEMBLIES_TAB, () -> AssemblyItem.createWith(id("drone"), DRONE_ITEM.get()));
        CreativeTabRegistry.appendStack(CreativeMenus.ASSEMBLIES_TAB, () -> AssemblyItem.createWith(id("dragon_egg_shell"), DRAGON_EGG_SHELL.get()));
        CreativeTabRegistry.appendStack(CreativeMenus.ASSEMBLIES_TAB, () -> AssemblyItem.createWith(id("reinforced_dragon_egg_shell"), REINFORCED_DRAGON_EGG_SHELL.get()));
        CreativeTabRegistry.appendStack(CreativeMenus.ASSEMBLIES_TAB, () -> AssemblyItem.createWith(id("drone"), DRONE_ITEM.get()));

        CreativeTabRegistry.appendStack(CreativeMenus.ASSEMBLIES_TAB, () -> AssemblyItem.createWith(id("dragon_warhead"), WARHEAD_ASSEMBLY.get()));
        CreativeTabRegistry.appendStack(CreativeMenus.ASSEMBLIES_TAB, () -> AssemblyItem.createWith(id("flaming_warhead"), WARHEAD_ASSEMBLY.get()));
        CreativeTabRegistry.appendStack(CreativeMenus.ASSEMBLIES_TAB, () -> AssemblyItem.createWith(id("firework_warhead"), WARHEAD_ASSEMBLY.get()));
        CreativeTabRegistry.appendStack(CreativeMenus.ASSEMBLIES_TAB, () -> AssemblyItem.createWith(id("shulker_box_warhead"), WARHEAD_ASSEMBLY.get()));
        CreativeTabRegistry.appendStack(CreativeMenus.ASSEMBLIES_TAB, () -> AssemblyItem.createWith(id("teleportation_warhead"), WARHEAD_ASSEMBLY.get()));
        CreativeTabRegistry.appendStack(CreativeMenus.ASSEMBLIES_TAB, () -> AssemblyItem.createWith(id("excavator_warhead"), WARHEAD_ASSEMBLY.get()));

        CreativeTabRegistry.appendStack(CreativeMenus.ASSEMBLIES_TAB, () -> AssemblyItem.createWith(id("flaming_chassis"), CHASSIS_ASSEMBLY.get()));
        CreativeTabRegistry.appendStack(CreativeMenus.ASSEMBLIES_TAB, () -> AssemblyItem.createWith(id("firework_chassis"), CHASSIS_ASSEMBLY.get()));
        CreativeTabRegistry.appendStack(CreativeMenus.ASSEMBLIES_TAB, () -> AssemblyItem.createWith(id("excavator_chassis"), CHASSIS_ASSEMBLY.get()));

        CreativeTabRegistry.appendStack(CreativeMenus.ASSEMBLIES_TAB, () -> AssemblyItem.createWith(id("flaming_thruster"), THRUSTER_ASSEMBLY.get()));
        CreativeTabRegistry.appendStack(CreativeMenus.ASSEMBLIES_TAB, () -> AssemblyItem.createWith(id("firework_thruster"), THRUSTER_ASSEMBLY.get()));
        CreativeTabRegistry.appendStack(CreativeMenus.ASSEMBLIES_TAB, () -> AssemblyItem.createWith(id("excavator_thruster"), THRUSTER_ASSEMBLY.get()));

        TABS.register();
    }

    private static ResourceLocation id(String id) {
        return new ResourceLocation(CreateMissiles.MOD_ID, id);
    }
}
