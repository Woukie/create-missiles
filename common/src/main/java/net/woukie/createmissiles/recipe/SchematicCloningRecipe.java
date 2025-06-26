package net.woukie.createmissiles.recipe;

import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import net.woukie.createmissiles.registry.Items;
import net.woukie.createmissiles.registry.RecipeSerializers;
import org.jetbrains.annotations.NotNull;

public class SchematicCloningRecipe extends CustomRecipe {
    public SchematicCloningRecipe(ResourceLocation resourceLocation, CraftingBookCategory craftingBookCategory) {
        super(resourceLocation, craftingBookCategory);
    }

    private boolean isSchematic(ItemStack stack) {
        return stack.is(Items.WARHEAD_SCHEMATIC.get()) || stack.is(Items.CHASSIS_SCHEMATIC.get()) || stack.is(Items.THRUSTER_SCHEMATIC.get());
    }

    @Override
    public boolean matches(CraftingContainer craftingContainer, @NotNull Level level) {
        int paperCount = 0;
        ItemStack schematic = ItemStack.EMPTY;

        for(int j = 0; j < craftingContainer.getContainerSize(); ++j) {
            ItemStack containerItem = craftingContainer.getItem(j);
            if (!containerItem.isEmpty()) {
                if (isSchematic(containerItem)) {
                    if (!schematic.isEmpty()) {
                        return false;
                    }

                    schematic = containerItem;
                } else {
                    if (!containerItem.is(net.minecraft.world.item.Items.PAPER)) {
                        return false;
                    }

                    ++paperCount;
                }
            }
        }

        return !schematic.isEmpty() && paperCount > 0;
    }

    @Override
    public @NotNull ItemStack assemble(CraftingContainer craftingContainer, @NotNull RegistryAccess registryAccess) {
        int paperCount = 0;
        ItemStack schematic = ItemStack.EMPTY;

        for(int j = 0; j < craftingContainer.getContainerSize(); ++j) {
            ItemStack containerItem = craftingContainer.getItem(j);
            if (!containerItem.isEmpty()) {
                if (isSchematic(containerItem)) {
                    if (!schematic.isEmpty()) {
                        return ItemStack.EMPTY;
                    }

                    schematic = containerItem;
                } else {
                    if (!containerItem.is(net.minecraft.world.item.Items.PAPER)) {
                        return ItemStack.EMPTY;
                    }

                    ++paperCount;
                }
            }
        }

        if (!schematic.isEmpty() && paperCount >= 1) {
            return schematic.copyWithCount(paperCount + 1);
        } else {
            return ItemStack.EMPTY;
        }
    }

    @Override
    public boolean canCraftInDimensions(int i, int j) {
        return i >= 3 && j >= 3;
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return RecipeSerializers.SCHEMATIC_CLONING.get();
    }
}
