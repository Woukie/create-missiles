package net.woukie.createmissiles.registry;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;
import net.woukie.createmissiles.CreateMissiles;
import net.woukie.createmissiles.recipe.MissilePartRecipe;
import net.woukie.createmissiles.recipe.AssemblyCloningRecipe;

public class RecipeSerializers {
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(CreateMissiles.MOD_ID, Registries.RECIPE_SERIALIZER);

    public static final RegistrySupplier<SimpleCraftingRecipeSerializer<?>> ASSEMBLY_CLONING = RECIPE_SERIALIZERS.register("assembly_cloning", () -> new SimpleCraftingRecipeSerializer<>(AssemblyCloningRecipe::new));
    public static final RegistrySupplier<MissilePartRecipe.Serializer> MISSILE_PART = RECIPE_SERIALIZERS.register("missile_part", MissilePartRecipe.Serializer::new);

    public static void init() {
        CreateMissiles.LOGGER.info("Registering custom recipe serializers for " + CreateMissiles.NAME);
        RECIPE_SERIALIZERS.register();
    }
}
