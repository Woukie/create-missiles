package net.woukie.createmissiles.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @param items Ingredient accepts any of these items
 * @param tags  Ingredient accepts any of these tags
 */
public record MissileIngredient(int count, ItemStack[] items, TagKey<Item>[] tags) {
    public static final MissileIngredient EMPTY = new MissileIngredient(0, new ItemStack[0], new TagKey[0]);

    private static final Codec<ItemStack> ITEM_CODEC = BuiltInRegistries.ITEM.byNameCodec()
            .xmap(ItemStack::new, ItemStack::getItem);

    private static final StreamCodec<RegistryFriendlyByteBuf, TagKey<Item>> TAG_STREAM_CODEC = StreamCodec.of(
            (buf, tag) -> buf.writeResourceLocation(tag.location()),
            buf -> TagKey.create(Registries.ITEM, buf.readResourceLocation())
    );

    public static final Codec<MissileIngredient> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.fieldOf("count").forGetter(MissileIngredient::count),
            ITEM_CODEC.listOf().optionalFieldOf("items", List.of())
                    .forGetter(i -> Arrays.asList(i.items())),
            TagKey.codec(Registries.ITEM).listOf().optionalFieldOf("tags", List.of())
                    .forGetter(i -> Arrays.asList(i.tags()))
    ).apply(instance, (count, items, tags) ->
            new MissileIngredient(count, items.toArray(new ItemStack[0]), tags.toArray(new TagKey[0]))
    ));

    public static final StreamCodec<RegistryFriendlyByteBuf, MissileIngredient> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, MissileIngredient::count,
            ItemStack.STREAM_CODEC.apply(ByteBufCodecs.list()), i -> Arrays.asList(i.items()),
            TAG_STREAM_CODEC.apply(ByteBufCodecs.list()), i -> Arrays.asList(i.tags()),
            (count, items, tags) -> new MissileIngredient(count, items.toArray(new ItemStack[0]), tags.toArray(new TagKey[0]))
    );

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

    // Used currently only for display purposes
    public List<ItemStack> getAllValidItems() {
        List<ItemStack> items = new ArrayList<>(Arrays.stream(items()).toList());
        for (var tag : tags()) {
            for(Holder<Item> holder : BuiltInRegistries.ITEM.getTagOrEmpty(tag)) {
                items.add(new ItemStack(holder));
            }
        }

        return items;
    }
}