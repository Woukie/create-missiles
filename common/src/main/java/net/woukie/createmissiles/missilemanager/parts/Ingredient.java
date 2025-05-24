package net.woukie.createmissiles.missilemanager.parts;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class Ingredient {
    private final List<Item> requiredItems;
    private final List<TagKey<Item>> allowTags;
    private final CompoundTag requiredNbt;
    private final int requiredCount;
    public final Component name;

    public Ingredient(List<Item> requireItems, List<TagKey<Item>> allowIfHasTag, CompoundTag requiredNbt, int requiredCount, Component name) {
        this.requiredItems = requireItems;
        this.allowTags = allowIfHasTag;
        this.requiredNbt = requiredNbt;
        this.requiredCount = requiredCount;
        this.name = name;
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
            return requiredItems.contains(stack.getItem());

        return true;
    }

    public int getRequiredCount() {
        return requiredCount;
    }
}
