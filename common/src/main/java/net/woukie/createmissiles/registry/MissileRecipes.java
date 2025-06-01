package net.woukie.createmissiles.registry;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;
import net.woukie.createmissiles.CreateMissiles;
import net.woukie.createmissiles.recipe.MissilePartRecipe;
import net.woukie.createmissiles.recipe.SchematicCloningRecipe;

public class MissileRecipes {
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_TYPES = DeferredRegister.create(CreateMissiles.MOD_ID, Registries.RECIPE_SERIALIZER);

    public static final RegistrySupplier<SimpleCraftingRecipeSerializer<?>> SCHEMATIC_CLONING = RECIPE_TYPES.register("schematic_cloning", () -> new SimpleCraftingRecipeSerializer<>(SchematicCloningRecipe::new));
    public static final RegistrySupplier<MissilePartRecipe.Serializer> MISSILE_PART = RECIPE_TYPES.register("schematic_cloning", MissilePartRecipe.Serializer::new);

    public static void init() {
        CreateMissiles.LOGGER.info("Registering custom recipes for " + CreateMissiles.NAME);
        RECIPE_TYPES.register();
    }
}
