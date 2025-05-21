package net.woukie.createmissiles.missilemanager.parts;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class Ingredient {
    private final List<Item> requiredItems;
    private final List<TagKey<Item>> allowTags;
    private final CompoundTag requiredNbt;
    private final int requiredCount;

    public Ingredient(List<Item> requireItems, List<TagKey<Item>> allowIfHasTag, CompoundTag requiredNbt, int requiredCount) {
        this.requiredItems = requireItems;
        this.allowTags = allowIfHasTag;
        this.requiredNbt = requiredNbt;
        this.requiredCount = requiredCount;
    }

    public boolean matches(ItemStack stack) {
        if (requiredNbt != null) {
            CompoundTag tags = stack.getTag();

            if (tags == null)
                return false;

            if (requiredNbt.getAllKeys().stream().anyMatch(key -> tags.get(key) != requiredNbt.get(key)))
                return false;
        }

        if (allowTags != null)
            if (allowTags.stream().anyMatch(requiredTag -> stack.getTags().anyMatch(tag -> tag == requiredTag)))
                return true;

        if (requiredItems != null)
            if (!requiredItems.contains(stack.getItem()))
                return false;

        return stack.getCount() >= requiredCount;
    }
}
