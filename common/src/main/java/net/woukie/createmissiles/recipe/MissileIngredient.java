package net.woukie.createmissiles.recipe;

import com.google.common.collect.Lists;
import com.google.gson.JsonObject;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

public class MissileIngredient {
    public static final MissileIngredient EMPTY = new MissileIngredient(0, new ArrayList<>(), new ArrayList<>());
    public final int count;
    public final List<ItemStack> items; // Ingredient accepts any of these items
    public final List<TagKey<Item>> tags; // Ingredient accepts any of these tags

    public boolean test(@Nullable ItemStack itemStack) {
        if (itemStack == null)
            return false;

        if ((items.isEmpty() && tags.isEmpty()) || count == 0)
            return itemStack.isEmpty();

        for (ItemStack ingredientStack : this.items)
            if (ingredientStack.is(itemStack.getItem())) return true;

        for (TagKey<Item> tag : tags)
            if (itemStack.is(tag)) return true;

        return false;
    }

    public MissileIngredient(int count, List<ItemStack> items, List<TagKey<Item>> tags) {
        this.count = count;
        this.items = items;
        this.tags = tags;
    }
}
