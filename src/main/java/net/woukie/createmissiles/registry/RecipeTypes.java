package net.woukie.createmissiles.registry;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.*;
import net.woukie.createmissiles.CreateMissiles;
import net.woukie.createmissiles.recipe.MissilePartRecipe;

public class RecipeTypes {
    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(CreateMissiles.MOD_ID, Registries.RECIPE_TYPE);

    public static final RegistrySupplier<RecipeType<MissilePartRecipe>> MISSILE_PART = RECIPE_TYPES.register(
            new ResourceLocation(CreateMissiles.MOD_ID, "missile_part"), () -> new RecipeType<>() {
                @Override
                public String toString() {
                    return "missile_part";
                }
            });

    public static void init() {
        CreateMissiles.LOGGER.info("Registering custom recipe types for " + CreateMissiles.NAME);
        RECIPE_TYPES.register();
    }
}
