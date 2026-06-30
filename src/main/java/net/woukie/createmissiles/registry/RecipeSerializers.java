package net.woukie.createmissiles.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.woukie.createmissiles.CreateMissiles;
import net.woukie.createmissiles.recipe.AssemblyCloningRecipe;
import net.woukie.createmissiles.recipe.MissilePartRecipe;
import net.woukie.createmissiles.recipe.NbtShapedRecipe;

public class RecipeSerializers {
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(Registries.RECIPE_SERIALIZER, CreateMissiles.MOD_ID);

    public static final DeferredHolder<RecipeSerializer<?>, SimpleCraftingRecipeSerializer<?>> ASSEMBLY_CLONING = RECIPE_SERIALIZERS.register("assembly_cloning", () -> new SimpleCraftingRecipeSerializer<>(AssemblyCloningRecipe::new));
    public static final DeferredHolder<RecipeSerializer<?>, NbtShapedRecipe.Serializer> NBT_CRAFTING_SHAPED = RECIPE_SERIALIZERS.register("nbt_crafting_shaped", NbtShapedRecipe.Serializer::new);
    public static final DeferredHolder<RecipeSerializer<?>, MissilePartRecipe.Serializer> MISSILE_PART = RECIPE_SERIALIZERS.register("missile_part", MissilePartRecipe.Serializer::new);

    public static void init() {
        CreateMissiles.LOGGER.info("Registering custom recipe serializers for " + CreateMissiles.NAME);
        RECIPE_SERIALIZERS.register(NeoForge.EVENT_BUS);
    }
}
