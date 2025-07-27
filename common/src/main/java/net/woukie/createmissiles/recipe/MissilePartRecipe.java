package net.woukie.createmissiles.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.woukie.createmissiles.missilemanager.parts.MissilePartType;
import net.woukie.createmissiles.registry.PartTypes;
import net.woukie.createmissiles.registry.RecipeSerializers;
import net.woukie.createmissiles.registry.RecipeTypes;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class MissilePartRecipe implements Recipe<Container> {
    private final ResourceLocation id;
    private final NonNullList<MissileIngredient> ingredients;
    private final ResourceLocation assembly;

    public MissilePartRecipe(ResourceLocation id, NonNullList<MissileIngredient> ingredients, ResourceLocation assembly) {
        this.id = id;
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

    public NonNullList<MissileIngredient> getMissileIngredients() {
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
        return missilePartRecipes.stream().filter(r -> r.getAssembly().equals(resourceLocation)).findFirst();
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
    public boolean matches(@NotNull Container container, @NotNull Level level) {
        List<ItemStack> containerStacks = new ArrayList<>();

        var partType = PartTypes.get(assembly);

        for (int i = partType.getStartSlot(); i < partType.getEndSlot(); i++)
            if (!container.getItem(i).isEmpty())
                containerStacks.add(container.getItem(i));

        var remainingItems = getRemainingItems(containerStacks);

        for (var remainingItem : remainingItems.entrySet())
            if (remainingItem.getValue() > 0)
                return false;

        return true;
    }

    @Override
    public @NotNull ItemStack assemble(@NotNull Container container, @NotNull RegistryAccess registryAccess) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canCraftInDimensions(int i, int j) {
        return i * j >= this.ingredients.size();
    }

    @Override
    public @NotNull ItemStack getResultItem(@NotNull RegistryAccess registryAccess) {
        return ItemStack.EMPTY;
    }

    @Override
    public @NotNull ResourceLocation getId() {
        return this.id;
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
        @Override
        public @NotNull MissilePartRecipe fromJson(@NotNull ResourceLocation resourceLocation, @NotNull JsonObject jsonObject) {
            NonNullList<MissileIngredient> ingredients = itemsFromJson(GsonHelper.getAsJsonArray(jsonObject, "ingredients"));
            ResourceLocation assembly = new ResourceLocation(GsonHelper.getAsString(jsonObject, "assembly"));

            return new MissilePartRecipe(resourceLocation, ingredients, assembly);
        }

        private static NonNullList<MissileIngredient> itemsFromJson(JsonArray jsonArray) {
            NonNullList<MissileIngredient> items = NonNullList.create();

            for(int i = 0; i < jsonArray.size(); ++i) {
                MissileIngredient ingredient = MissileIngredient.fromJson(jsonArray.get(i), false);
                if (!ingredient.isEmpty()) {
                    items.add(ingredient);
                }
            }

            return items;
        }

        @Override
        public @NotNull MissilePartRecipe fromNetwork(@NotNull ResourceLocation resourceLocation, FriendlyByteBuf friendlyByteBuf) {
            int ingredientCount = friendlyByteBuf.readVarInt();
            NonNullList<MissileIngredient> ingredients = NonNullList.withSize(ingredientCount, MissileIngredient.EMPTY);
            ingredients.replaceAll(ignored -> MissileIngredient.fromNetwork(friendlyByteBuf));
            ResourceLocation assembly = friendlyByteBuf.readResourceLocation();

            return new MissilePartRecipe(resourceLocation, ingredients, assembly);
        }

        @Override
        public void toNetwork(FriendlyByteBuf friendlyByteBuf, MissilePartRecipe recipe) {
            friendlyByteBuf.writeVarInt(recipe.ingredients.size());

            for(MissileIngredient ingredient : recipe.ingredients)
                ingredient.toNetwork(friendlyByteBuf);

            friendlyByteBuf.writeResourceLocation(recipe.assembly);
        }
    }
}
