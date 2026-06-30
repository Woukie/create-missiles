package net.woukie.createmissiles.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.woukie.createmissiles.CreateMissiles;
import net.woukie.createmissiles.recipe.MissilePartRecipe;

public class RecipeTypes {
    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(Registries.RECIPE_TYPE, CreateMissiles.MOD_ID);

    public static final DeferredHolder<RecipeType<?>, RecipeType<MissilePartRecipe>> MISSILE_PART = RECIPE_TYPES.register(
            "missile_part", () -> new RecipeType<>() {
                @Override
                public String toString() {
                    return "missile_part";
                }
            });

    public static void init() {
        CreateMissiles.LOGGER.info("Registering custom recipe types for " + CreateMissiles.NAME);
        RECIPE_TYPES.register(NeoForge.EVENT_BUS);
    }
}
