package net.woukie.createmissiles.missilemanager.parts;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public abstract class MissilePartType {
    public final Component displayName;
    public final ResourceLocation resourceLocation;
    public final WriteData writeData;
    public final List<Ingredient> ingredients;

    public HashMap<Ingredient, Integer> getIngredientsLeft(List<ItemStack> items) {
        HashMap<Ingredient, Integer> result = new HashMap<>();

        for (Ingredient ingredient : ingredients)
            result.put(ingredient, ingredient.getRequiredCount());

        for (ItemStack item : items)
            result = applyItemToIngredients(result, item);

        return result;
    }

    private HashMap<Ingredient, Integer> applyItemToIngredients(HashMap<Ingredient, Integer> ingredients, ItemStack stack) {
        int itemsLeft = stack.getCount();
        HashMap<Ingredient, Integer> ingredientsLeft = new HashMap<>(ingredients);

        for (var entry : ingredients.entrySet()) {
            Ingredient ingredient = entry.getKey();
            Integer quantity = entry.getValue();

            if (ingredient.matches(stack)) {
                int newQuantity = quantity - itemsLeft;
                ingredientsLeft.put(ingredient, Math.max(0, newQuantity));
                itemsLeft = -newQuantity;

                if (newQuantity >= 0)
                    break;
            }
        }

        return ingredientsLeft;
    }

    protected MissilePartType(Component displayName, ResourceLocation resourceLocation, @Nullable WriteData writeData, List<Ingredient> ingredients) {
        this.displayName = displayName;
        this.resourceLocation = resourceLocation;
        this.writeData = writeData;
        this.ingredients = Collections.unmodifiableList(ingredients);
    }

    public interface WriteData {
        CompoundTag write(Container container, CompoundTag data);
    }
}
