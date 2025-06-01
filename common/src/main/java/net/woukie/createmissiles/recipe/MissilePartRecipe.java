package net.woukie.createmissiles.recipe;

import com.google.gson.JsonObject;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.woukie.createmissiles.registry.MissileRecipeSerializers;
import net.woukie.createmissiles.registry.MissileRecipeTypes;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
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
        public MissilePartRecipe fromJson(ResourceLocation resourceLocation, JsonObject jsonObject) {
            return null;
        }

        @Override
        public MissilePartRecipe fromNetwork(ResourceLocation resourceLocation, FriendlyByteBuf friendlyByteBuf) {
            return null;
        }

        @Override
        public void toNetwork(FriendlyByteBuf friendlyByteBuf, MissilePartRecipe recipe) {

        }
    }
}
