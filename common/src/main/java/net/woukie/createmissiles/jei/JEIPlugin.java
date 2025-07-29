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
import net.woukie.createmissiles.registry.Items;
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

        registration.addItemStackInfo(makeStack("firework_thruster", Items.THRUSTER_ASSEMBLY.get()), Component.translatable("description.jei.firework_thruster"));
        registration.addItemStackInfo(makeStack("firework_chassis", Items.CHASSIS_ASSEMBLY.get()), Component.translatable("description.jei.firework_chassis"));
    }

    private ItemStack makeStack(String name, ItemLike item) {
        return AssemblyItem.createWith(new ResourceLocation(CreateMissiles.MOD_ID, name), item);
    }
}
