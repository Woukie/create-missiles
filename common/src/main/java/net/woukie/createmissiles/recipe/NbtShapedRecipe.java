package net.woukie.createmissiles.recipe;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.*;
import net.minecraft.core.Holder;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.LingeringPotionItem;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.woukie.createmissiles.registry.RecipeSerializers;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

// Had to pull a lot from super because of private access
public class NbtShapedRecipe extends ShapedRecipe {
    public NbtShapedRecipe(ResourceLocation resourceLocation, String string, CraftingBookCategory craftingBookCategory, int i, int j, NonNullList<Ingredient> nonNullList, ItemStack itemStack, boolean bl) {
        super(resourceLocation, string, craftingBookCategory, i, j, nonNullList, itemStack, bl);
    }

    public NbtShapedRecipe(ResourceLocation resourceLocation, String string, CraftingBookCategory craftingBookCategory, int i, int j, NonNullList<Ingredient> nonNullList, ItemStack itemStack) {
        super(resourceLocation, string, craftingBookCategory, i, j, nonNullList, itemStack);
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return RecipeSerializers.NBT_CRAFTING_SHAPED.get();
    }

    private List<NbtIngredient> getNbtIngredients() {
        List<Ingredient> ingredients = getIngredients();
        return ingredients.stream().map(ingredient -> NbtIngredient.fromSuper(ingredient)).toList();
    }

    private boolean matches(CraftingContainer craftingContainer, int i, int j, boolean bl) {
        for(int k = 0; k < craftingContainer.getWidth(); ++k) {
            for(int l = 0; l < craftingContainer.getHeight(); ++l) {
                int m = k - i;
                int n = l - j;
                NbtIngredient ingredient = NbtIngredient.EMPTY;
                if (m >= 0 && n >= 0 && m < this.getWidth() && n < this.getHeight()) {
                    if (bl) {
                        ingredient = this.getNbtIngredients().get(this.getWidth() - m - 1 + n * this.getWidth());
                    } else {
                        ingredient = this.getNbtIngredients().get(m + n * this.getWidth());
                    }
                }

                if (!ingredient.test(craftingContainer.getItem(k + l * craftingContainer.getWidth()))) {
                    return false;
                }
            }
        }

        return true;
    }

    @Override
    public boolean matches(CraftingContainer craftingContainer, Level level) {
        for(int i = 0; i <= craftingContainer.getWidth() - this.getWidth(); ++i) {
            for(int j = 0; j <= craftingContainer.getHeight() - this.getHeight(); ++j) {
                if (this.matches(craftingContainer, i, j, true)) {
                    return true;
                }

                if (this.matches(craftingContainer, i, j, false)) {
                    return true;
                }
            }
        }

        return false;
    }

    static String[] patternFromJson(JsonArray jsonArray) {
        String[] strings = new String[jsonArray.size()];
        if (strings.length > 3) {
            throw new JsonSyntaxException("Invalid pattern: too many rows, 3 is maximum");
        } else if (strings.length == 0) {
            throw new JsonSyntaxException("Invalid pattern: empty pattern not allowed");
        } else {
            for(int i = 0; i < strings.length; ++i) {
                String string = GsonHelper.convertToString(jsonArray.get(i), "pattern[" + i + "]");
                if (string.length() > 3) {
                    throw new JsonSyntaxException("Invalid pattern: too many columns, 3 is maximum");
                }

                if (i > 0 && strings[0].length() != string.length()) {
                    throw new JsonSyntaxException("Invalid pattern: each row must be the same width");
                }

                strings[i] = string;
            }

            return strings;
        }
    }

    static NonNullList<Ingredient> dissolvePattern(String[] strings, Map<String, Ingredient> map, int i, int j) {
        NonNullList<Ingredient> nonNullList = NonNullList.withSize(i * j, Ingredient.EMPTY);
        Set<String> set = Sets.newHashSet(map.keySet());
        set.remove(" ");

        for(int k = 0; k < strings.length; ++k) {
            for(int l = 0; l < strings[k].length(); ++l) {
                String string = strings[k].substring(l, l + 1);
                Ingredient ingredient = map.get(string);
                if (ingredient == null) {
                    throw new JsonSyntaxException("Pattern references symbol '" + string + "' but it's not defined in the key");
                }

                set.remove(string);
                nonNullList.set(l + i * k, ingredient);
            }
        }

        if (!set.isEmpty()) {
            throw new JsonSyntaxException("Key defines symbols that aren't used in pattern: " + set);
        } else {
            return nonNullList;
        }
    }

    static Map<String, Ingredient> keyFromJson(JsonObject jsonObject) {
        Map<String, Ingredient> map = Maps.newHashMap();

        for(Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
            if ((entry.getKey()).length() != 1) {
                throw new JsonSyntaxException("Invalid key entry: '" + (String)entry.getKey() + "' is an invalid symbol (must be 1 character only).");
            }

            if (" ".equals(entry.getKey())) {
                throw new JsonSyntaxException("Invalid key entry: ' ' is a reserved symbol.");
            }

            Ingredient i = NbtIngredient.fromJson(entry.getValue(), false);
            map.put(entry.getKey(), i);
        }

        map.put(" ", Ingredient.EMPTY);
        return map;
    }

    static String[] shrink(String... strings) {
        int i = Integer.MAX_VALUE;
        int j = 0;
        int k = 0;
        int l = 0;

        for(int m = 0; m < strings.length; ++m) {
            String string = strings[m];
            i = Math.min(i, firstNonSpace(string));
            int n = lastNonSpace(string);
            j = Math.max(j, n);
            if (n < 0) {
                if (k == m) {
                    ++k;
                }

                ++l;
            } else {
                l = 0;
            }
        }

        if (strings.length == l) {
            return new String[0];
        } else {
            String[] strings2 = new String[strings.length - l - k];

            for(int o = 0; o < strings2.length; ++o) {
                strings2[o] = strings[o + k].substring(i, j + 1);
            }

            return strings2;
        }
    }

    private static int firstNonSpace(String string) {
        int i;
        for(i = 0; i < string.length() && string.charAt(i) == ' '; ++i) {
        }
        return i;
    }

    private static int lastNonSpace(String string) {
        int i;
        for(i = string.length() - 1; i >= 0 && string.charAt(i) == ' '; --i) {
        }
        return i;
    }

    public static class Serializer implements RecipeSerializer<NbtShapedRecipe> {
        public NbtShapedRecipe fromJson(ResourceLocation resourceLocation, JsonObject jsonObject) {
            String group = GsonHelper.getAsString(jsonObject, "group", "");
            CraftingBookCategory craftingBookCategory = CraftingBookCategory.CODEC.byName(GsonHelper.getAsString(jsonObject, "category", null), CraftingBookCategory.MISC);
            Map<String, Ingredient> keys = NbtShapedRecipe.keyFromJson(GsonHelper.getAsJsonObject(jsonObject, "key"));
            String[] strings = NbtShapedRecipe.shrink(NbtShapedRecipe.patternFromJson(GsonHelper.getAsJsonArray(jsonObject, "pattern")));
            int width = strings[0].length();
            int height = strings.length;
            NonNullList<Ingredient> ingredients = NbtShapedRecipe.dissolvePattern(strings, keys, width, height);
            ItemStack result = NbtShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(jsonObject, "result"));
            boolean notify = GsonHelper.getAsBoolean(jsonObject, "show_notification", true);
            return new NbtShapedRecipe(resourceLocation, group, craftingBookCategory, width, height, ingredients, result, notify);
        }

        public NbtShapedRecipe fromNetwork(ResourceLocation resourceLocation, FriendlyByteBuf friendlyByteBuf) {
            int i = friendlyByteBuf.readVarInt();
            int j = friendlyByteBuf.readVarInt();
            String string = friendlyByteBuf.readUtf();
            CraftingBookCategory craftingBookCategory = friendlyByteBuf.readEnum(CraftingBookCategory.class);
            NonNullList<Ingredient> nonNullList = NonNullList.withSize(i * j, Ingredient.EMPTY);
            nonNullList.replaceAll(ignored -> Ingredient.fromNetwork(friendlyByteBuf));

            ItemStack itemStack = friendlyByteBuf.readItem();
            boolean bl = friendlyByteBuf.readBoolean();
            return new NbtShapedRecipe(resourceLocation, string, craftingBookCategory, i, j, nonNullList, itemStack, bl);
        }

        public void toNetwork(FriendlyByteBuf friendlyByteBuf, NbtShapedRecipe recipe) {
            friendlyByteBuf.writeVarInt(recipe.getWidth());
            friendlyByteBuf.writeVarInt(recipe.getHeight());
            friendlyByteBuf.writeUtf(recipe.getGroup());
            friendlyByteBuf.writeEnum(recipe.category());

            for(Ingredient ingredient : recipe.getIngredients()) {
                ingredient.toNetwork(friendlyByteBuf);
            }

            friendlyByteBuf.writeItem(recipe.getResultItem(null)); // parameter never used?
            friendlyByteBuf.writeBoolean(recipe.showNotification());
        }
    }

    static class NbtIngredient extends Ingredient {
        public static final NbtIngredient EMPTY = new NbtIngredient(Stream.empty());

        public static NbtIngredient fromSuper(Ingredient ingredient) {
            return new NbtIngredient(Arrays.stream(ingredient.values));
        }

        public static class TagValue implements Value {
            private final TagKey<Item> tag;

            TagValue(TagKey<Item> tagKey) {
                this.tag = tagKey;
            }

            public Collection<ItemStack> getItems() {
                List<ItemStack> list = Lists.newArrayList();

                for(Holder<Item> holder : BuiltInRegistries.ITEM.getTagOrEmpty(this.tag)) {
                    list.add(new ItemStack(holder));
                }

                return list;
            }

            public JsonObject serialize() {
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("tag", this.tag.location().toString());
                return jsonObject;
            }
        }

        @Override
        public boolean test(@Nullable ItemStack itemStack) {
            if (itemStack == null) {
                return false;
            } else if (this.isEmpty()) {
                return itemStack.isEmpty();
            } else {
                for(ItemStack requiredItem : this.getItems()) {
                    boolean requireTag = requiredItem.hasTag();
                    if (requiredItem.is(itemStack.getItem()) && (!requireTag || requiredItem.getTag().equals(itemStack.getTag()))) {
                        return true;
                    }
                }

                return false;
            }
        }

        public NbtIngredient(Stream<? extends Value> stream) {
            super(stream);
        }

        public static Value valueFromJson(JsonObject jsonObject) {
            if (jsonObject.has("item") && jsonObject.has("tag")) {
                throw new JsonParseException("An ingredient entry is either a tag or an item, not both");
            } else if (jsonObject.has("item")) {
                return new ItemValue(NbtShapedRecipe.itemStackFromJson(jsonObject));
            } else if (jsonObject.has("tag")) {
                ResourceLocation resourceLocation = new ResourceLocation(GsonHelper.getAsString(jsonObject, "tag"));
                TagKey<Item> tagKey = TagKey.create(Registries.ITEM, resourceLocation);
                return new TagValue(tagKey);
            } else {
                throw new JsonParseException("An ingredient entry needs either a tag or an item");
            }
        }

        public static Ingredient fromJson(@Nullable JsonElement jsonElement, boolean bl) {
            if (jsonElement != null && !jsonElement.isJsonNull()) {
                if (jsonElement.isJsonObject()) {
                    return fromValues(Stream.of(valueFromJson(jsonElement.getAsJsonObject())));
                } else if (jsonElement.isJsonArray()) {
                    JsonArray jsonArray = jsonElement.getAsJsonArray();
                    if (jsonArray.size() == 0 && !bl) {
                        throw new JsonSyntaxException("Item array cannot be empty, at least one item must be defined");
                    } else {
                        return fromValues(StreamSupport.stream(jsonArray.spliterator(), false).map((jsonElementx) -> valueFromJson(GsonHelper.convertToJsonObject(jsonElementx, "item"))));
                    }
                } else {
                    throw new JsonSyntaxException("Expected item to be object or array of objects");
                }
            } else {
                throw new JsonSyntaxException("Item cannot be null");
            }
        }
    }
}
