package net.woukie.createmissiles.recipe;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.woukie.createmissiles.missiles.parts.MissilePartType;
import net.woukie.createmissiles.registry.PartTypes;
import net.woukie.createmissiles.registry.RecipeSerializers;
import net.woukie.createmissiles.registry.RecipeTypes;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class MissilePartRecipe implements Recipe<RecipeInput> {
    private final List<MissileIngredient> ingredients;
    private final ResourceLocation assembly;

    public MissilePartRecipe(List<MissileIngredient> ingredients, ResourceLocation assembly) {
        this.ingredients = ingredients;
        this.assembly = assembly;
    }

    public ResourceLocation getAssembly() {
        return this.assembly;
    }

    public boolean itemComplements(ItemStack itemStack, Container container) {
        List<ItemStack> stacksLeft = new ArrayList<>();

        var partType = PartTypes.get(assembly);

        for (int i = partType.getStartSlot(); i < partType.getEndSlot(); i++)
            if (!container.getItem(i).isEmpty())
                stacksLeft.add(container.getItem(i));

        var remainingItems = this.getRemainingItems(stacksLeft);
        for (var item : remainingItems.entrySet())
            if (item.getKey().test(itemStack) && item.getValue() > 0) return true;

        return false;
    }

    public List<MissileIngredient> getMissileIngredients() {
        return this.ingredients;
    }

    public Map<MissileIngredient, Integer> getRemainingItems(List<ItemStack> items) {
        Map<MissileIngredient, Integer> ingredientStatus = getMissileIngredients().stream().collect(Collectors.toMap(a -> a, MissileIngredient::count));

        for (ItemStack item : items) {
            var itemsRemaining = item.getCount();
            for (var ingredient : ingredientStatus.keySet()) {
                var count = ingredientStatus.get(ingredient);
                if (ingredient.test(item)) {
                    var reducedTo = Math.max(count - itemsRemaining, 0);
                    ingredientStatus.put(ingredient, reducedTo);
                    itemsRemaining -= count - reducedTo;
                }

                if (itemsRemaining == 0) break;
            }
        }

        return ingredientStatus;
    }

    public static Optional<MissilePartRecipe> fromResourceLocation(Level level, ResourceLocation resourceLocation) {
        if (level == null) return Optional.empty();
        var missilePartRecipes = level.getRecipeManager().getAllRecipesFor(RecipeTypes.MISSILE_PART.get());
        return missilePartRecipes.stream()
                .map(RecipeHolder::value)
                .filter(r -> r.getAssembly().equals(resourceLocation))
                .findFirst();
    }

    public static Map<MissileIngredient, Integer> getRemainingItems(MissilePartType partType, Level level, List<ItemStack> items) {
        if (partType == null) return null;
        Optional<MissilePartRecipe> recipe = fromResourceLocation(level, partType.getResourceLocation());
        return recipe.map(missilePartRecipe -> missilePartRecipe.getRemainingItems(items.subList(partType.getStartSlot(), partType.getEndSlot()))).orElse(null);
    }

    public static int getBuildPercentage(Map<MissileIngredient, Integer> remainingItems) {
        if (remainingItems == null) return 0;

        int totalCount = 0;
        int fulfilled = 0;
        for (var entry : remainingItems.entrySet()) {
            int required = entry.getKey().count();
            totalCount += entry.getKey().count();
            fulfilled += required - entry.getValue();
        }

        return (int)(((float)fulfilled / (float)totalCount) * 100);
    }

    public static int getBuildPercentage(MissilePartType partType, Level level, List<ItemStack> items) {
        if (partType == null || level == null) return 0;
        var remainingItems = getRemainingItems(partType, level, items);
        return getBuildPercentage(remainingItems);
    }

    @Override
    public boolean matches(@NotNull RecipeInput input, @NotNull Level level) {
        List<ItemStack> containerStacks = new ArrayList<>();

        var partType = PartTypes.get(assembly);

        for (int i = partType.getStartSlot(); i < partType.getEndSlot(); i++)
            if (!input.getItem(i).isEmpty())
                containerStacks.add(input.getItem(i));

        var remainingItems = getRemainingItems(containerStacks);

        for (var remainingItem : remainingItems.entrySet())
            if (remainingItem.getValue() > 0)
                return false;

        return true;
    }

    @Override
    public @NotNull ItemStack assemble(@NotNull RecipeInput input, @NotNull HolderLookup.Provider registries) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canCraftInDimensions(int i, int j) {
        return i * j >= this.ingredients.size();
    }

    @Override
    public @NotNull ItemStack getResultItem(@NotNull HolderLookup.Provider registries) {
        return ItemStack.EMPTY;
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return RecipeSerializers.MISSILE_PART.get();
    }

    @Override
    public @NotNull RecipeType<?> getType() {
        return RecipeTypes.MISSILE_PART.get();
    }

    public static class Serializer implements RecipeSerializer<MissilePartRecipe> {
        public static final MapCodec<MissilePartRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                MissileIngredient.CODEC.listOf().fieldOf("ingredients").forGetter(MissilePartRecipe::getMissileIngredients),
                ResourceLocation.CODEC.fieldOf("assembly").forGetter(MissilePartRecipe::getAssembly)
        ).apply(instance, MissilePartRecipe::new));

        public static final StreamCodec<RegistryFriendlyByteBuf, MissilePartRecipe> STREAM_CODEC = StreamCodec.composite(
                MissileIngredient.STREAM_CODEC.apply(ByteBufCodecs.list()), MissilePartRecipe::getMissileIngredients,
                ResourceLocation.STREAM_CODEC, MissilePartRecipe::getAssembly,
                MissilePartRecipe::new
        );

        @Override
        public @NotNull MapCodec<MissilePartRecipe> codec() {
            return CODEC;
        }

        @Override
        public @NotNull StreamCodec<RegistryFriendlyByteBuf, MissilePartRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }
}