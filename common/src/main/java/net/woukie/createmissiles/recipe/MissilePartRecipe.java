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
import net.woukie.createmissiles.registry.MissileRecipeSerializers;
import net.woukie.createmissiles.registry.MissileRecipeTypes;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class MissilePartRecipe implements Recipe<Container> {
    private final ResourceLocation id;
    private final NonNullList<Ingredient> ingredients;
    private final Ingredient schematic;
    private final int slotStart;
    private final int slotEnd;

    public MissilePartRecipe(ResourceLocation id, NonNullList<Ingredient> ingredients, Ingredient schematic, int slotStart, int slotEnd) {
        this.id = id;
        this.ingredients = ingredients;
        this.schematic = schematic;
        this.slotStart = slotStart;
        this.slotEnd = slotEnd;
    }

    public Ingredient getSchematic() {
        return this.schematic;
    }

    @Override
    public boolean matches(@NotNull Container container, @NotNull Level level) {
        List<ItemStack> containerStacks = new ArrayList<>();

        for (int i = slotStart; i < slotEnd; i++)
            if (!container.getItem(i).isEmpty())
                containerStacks.add(container.getItem(i));

        for (Ingredient ingredient : ingredients) {
            boolean fulfilled = false;

            for (int i = 0; i < containerStacks.size(); i++)
                if (ingredient.test(containerStacks.get(i))) {
                    containerStacks.remove(i);
                    fulfilled = true;
                    break;
                }

            if (!fulfilled)
                return false;
        }

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
        return MissileRecipeSerializers.MISSILE_PART.get();
    }

    @Override
    public @NotNull RecipeType<?> getType() {
        return MissileRecipeTypes.MISSILE_PART.get();
    }

    public static class Serializer implements RecipeSerializer<MissilePartRecipe> {
        @Override
        public @NotNull MissilePartRecipe fromJson(@NotNull ResourceLocation resourceLocation, @NotNull JsonObject jsonObject) {
            NonNullList<Ingredient> ingredients = itemsFromJson(GsonHelper.getAsJsonArray(jsonObject, "ingredients"));
            Ingredient schematic = Ingredient.fromJson(GsonHelper.getAsJsonObject(jsonObject, "schematic"), false);

            int slotStart = GsonHelper.getAsInt(jsonObject, "slotStart");
            int slotEnd = GsonHelper.getAsInt(jsonObject, "slotEnd");

            return new MissilePartRecipe(resourceLocation, ingredients, schematic, slotStart, slotEnd);
        }

        private static NonNullList<Ingredient> itemsFromJson(JsonArray jsonArray) {
            NonNullList<Ingredient> nonNullList = NonNullList.create();

            for(int i = 0; i < jsonArray.size(); ++i) {
                Ingredient ingredient = Ingredient.fromJson(jsonArray.get(i), false);
                if (!ingredient.isEmpty()) {
                    nonNullList.add(ingredient);
                }
            }

            return nonNullList;
        }

        @Override
        public @NotNull MissilePartRecipe fromNetwork(@NotNull ResourceLocation resourceLocation, FriendlyByteBuf friendlyByteBuf) {
            int ingredientCount = friendlyByteBuf.readVarInt();
            NonNullList<Ingredient> ingredients = NonNullList.withSize(ingredientCount, Ingredient.EMPTY);
            ingredients.replaceAll(ignored -> Ingredient.fromNetwork(friendlyByteBuf));
            Ingredient schematic = Ingredient.fromNetwork(friendlyByteBuf);
            int slotStart = friendlyByteBuf.readInt();
            int slotEnd = friendlyByteBuf.readInt();
            return new MissilePartRecipe(resourceLocation, ingredients, schematic, slotStart, slotEnd);
        }

        @Override
        public void toNetwork(FriendlyByteBuf friendlyByteBuf, MissilePartRecipe recipe) {
            friendlyByteBuf.writeVarInt(recipe.ingredients.size());

            for(Ingredient ingredient : recipe.ingredients)
                ingredient.toNetwork(friendlyByteBuf);

            recipe.schematic.toNetwork(friendlyByteBuf);
            friendlyByteBuf.writeInt(recipe.slotStart);
            friendlyByteBuf.writeInt(recipe.slotEnd);
        }
    }
}
