package net.woukie.createmissiles.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.registration.ISubtypeRegistration;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.woukie.createmissiles.CreateMissiles;
import net.woukie.createmissiles.item.assembly.AssemblyItem;
import net.woukie.createmissiles.missilemanager.parts.ChassisType;
import net.woukie.createmissiles.missilemanager.parts.ThrusterType;
import net.woukie.createmissiles.registry.Items;
import net.woukie.createmissiles.registry.PartTypes;
import org.jetbrains.annotations.NotNull;

@JeiPlugin
public class JEIPlugin implements IModPlugin {
    @Override
    public @NotNull ResourceLocation getPluginUid() {
        return new ResourceLocation(CreateMissiles.MOD_ID, "main");
    }

    @Override
    public void registerItemSubtypes(@NotNull ISubtypeRegistration registration) {
        IModPlugin.super.registerItemSubtypes(registration);
        registration.useNbtForSubtypes(Items.WARHEAD_ASSEMBLY.get(), Items.CHASSIS_ASSEMBLY.get(), Items.THRUSTER_ASSEMBLY.get());
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        IModPlugin.super.registerRecipes(registration);

        PartTypes.getMissilePartTypes().forEach(partType -> {
            ResourceLocation location = partType.getResourceLocation();
            ItemLike item = Items.WARHEAD_ASSEMBLY.get();
            if (partType instanceof ThrusterType) {
                item = Items.THRUSTER_ASSEMBLY.get();
            } else if (partType instanceof ChassisType) {
                item = Items.CHASSIS_ASSEMBLY.get();
            }
            ItemStack stack = AssemblyItem.createWith(location, item);

            var description = Component.empty();
            partType.registerJEIStats(description);
            description.append("\n");
            description.append(Component.translatable("description.jei." + location.getNamespace() + "." + location.getPath()));
            registration.addItemStackInfo(stack, description);
        });

        registration.addItemStackInfo(Items.EXCAVATOR_UPGRADE_CORE.get().getDefaultInstance(), Component.translatable("description.jei.createmissiles.generic.upgrade_core"));
        registration.addItemStackInfo(Items.FIREWORK_UPGRADE_CORE.get().getDefaultInstance(), Component.translatable("description.jei.createmissiles.generic.upgrade_core"));
        registration.addItemStackInfo(Items.FLAMING_UPGRADE_CORE.get().getDefaultInstance(), Component.translatable("description.jei.createmissiles.generic.upgrade_core"));
        registration.addItemStackInfo(Items.FROST_UPGRADE_CORE.get().getDefaultInstance(), Component.translatable("description.jei.createmissiles.generic.upgrade_core"));
    }
}
