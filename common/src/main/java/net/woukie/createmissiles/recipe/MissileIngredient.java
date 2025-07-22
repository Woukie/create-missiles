package net.woukie.createmissiles.recipe;

import com.google.gson.JsonElement;
import com.google.gson.JsonSyntaxException;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.stream.StreamSupport;

/**
 * @param items Ingredient accepts any of these items
 * @param tags  Ingredient accepts any of these tags
 */
public record MissileIngredient(int count, ItemStack[] items, TagKey<Item>[] tags) {
    public static final MissileIngredient EMPTY = new MissileIngredient(0, new ItemStack[0], new TagKey[0]);

    public boolean isEmpty() {
        return (items.length == 0 && tags.length == 0) || count == 0;
    }

    public boolean test(@Nullable ItemStack itemStack) {
        if (itemStack == null)
            return false;

        if (this.isEmpty())
            return itemStack.isEmpty();

        for (ItemStack ingredientStack : this.items)
            if (ingredientStack.is(itemStack.getItem())) return true;

        for (TagKey<Item> tag : tags)
            if (itemStack.is(tag)) return true;

        return false;
    }

    public void toNetwork(FriendlyByteBuf friendlyByteBuf) {
        friendlyByteBuf.writeCollection(Arrays.asList(items()), FriendlyByteBuf::writeItem);
        friendlyByteBuf.writeCollection(Arrays.asList(tags()), (a, b) -> a.writeResourceLocation(b.location()));
        friendlyByteBuf.writeInt(count);
    }

    public static MissileIngredient fromJson(JsonElement rawJson, boolean b) {
        if (rawJson != null && !rawJson.isJsonNull()) {
            if (rawJson.isJsonObject()) {
                var ingredient = rawJson.getAsJsonObject();
                var count = GsonHelper.getAsInt(ingredient, "count");

                ItemStack[] items = new ItemStack[0];
                if (ingredient.has("items")) {
                    var ingredientArray = GsonHelper.getAsJsonArray(ingredient, "items");
                    items = StreamSupport.stream(ingredientArray.spliterator(), false).map(MissileIngredient::itemStackFromJson).toList().toArray(new ItemStack[0]);
                }

                TagKey<Item>[] tags = new TagKey[0];
                if (ingredient.has("tags")) {
                    var tagArray = GsonHelper.getAsJsonArray(ingredient, "tags");
                    tags = StreamSupport.stream(tagArray.spliterator(), false).map(MissileIngredient::tagFromJson).toList().toArray(new TagKey[0]);
                }

                return new MissileIngredient(count, items, tags);
            } else {
                throw new JsonSyntaxException("Value of key 'ingredients' must be an object");
            }
        } else {
            throw new JsonSyntaxException("Value of key 'ingredients' cannot be null");
        }
    }

    public static MissileIngredient fromNetwork(FriendlyByteBuf friendlyByteBuf) {
        var items = friendlyByteBuf.readList(FriendlyByteBuf::readItem).toArray(new ItemStack[0]);
        var tags = friendlyByteBuf.readList(buf -> TagKey.create(Registries.ITEM, buf.readResourceLocation())).toArray(new TagKey[0]);
        var count = friendlyByteBuf.readInt();
        return new MissileIngredient(count, items, tags);
    }

    private static ItemStack itemStackFromJson(JsonElement json) {
        var resourceLocation = new ResourceLocation(json.getAsString());
        return new ItemStack(BuiltInRegistries.ITEM.get(resourceLocation));
    }

    private static TagKey<Item> tagFromJson(JsonElement json) {
        var resourceLocation = new ResourceLocation(json.getAsString());
        return TagKey.create(Registries.ITEM, resourceLocation);
    }
}
