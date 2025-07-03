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

public class AssemblyCloningRecipe extends CustomRecipe {
    public AssemblyCloningRecipe(ResourceLocation resourceLocation, CraftingBookCategory craftingBookCategory) {
        super(resourceLocation, craftingBookCategory);
    }

    private boolean isAssembly(ItemStack stack) {
        return stack.is(Items.WARHEAD_ASSEMBLY.get()) || stack.is(Items.CHASSIS_ASSEMBLY.get()) || stack.is(Items.THRUSTER_ASSEMBLY.get());
    }

    @Override
    public boolean matches(CraftingContainer craftingContainer, @NotNull Level level) {
        int paperCount = 0;
        ItemStack assembly = ItemStack.EMPTY;

        for(int j = 0; j < craftingContainer.getContainerSize(); ++j) {
            ItemStack containerItem = craftingContainer.getItem(j);
            if (!containerItem.isEmpty()) {
                if (isAssembly(containerItem)) {
                    if (!assembly.isEmpty()) {
                        return false;
                    }

                    assembly = containerItem;
                } else {
                    if (!containerItem.is(net.minecraft.world.item.Items.PAPER)) {
                        return false;
                    }

                    ++paperCount;
                }
            }
        }

        return !assembly.isEmpty() && paperCount > 0;
    }

    @Override
    public @NotNull ItemStack assemble(CraftingContainer craftingContainer, @NotNull RegistryAccess registryAccess) {
        int paperCount = 0;
        ItemStack assembly = ItemStack.EMPTY;

        for(int j = 0; j < craftingContainer.getContainerSize(); ++j) {
            ItemStack containerItem = craftingContainer.getItem(j);
            if (!containerItem.isEmpty()) {
                if (isAssembly(containerItem)) {
                    if (!assembly.isEmpty()) {
                        return ItemStack.EMPTY;
                    }

                    assembly = containerItem;
                } else {
                    if (!containerItem.is(net.minecraft.world.item.Items.PAPER)) {
                        return ItemStack.EMPTY;
                    }

                    ++paperCount;
                }
            }
        }

        if (!assembly.isEmpty() && paperCount >= 1) {
            return assembly.copyWithCount(paperCount + 1);
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
        return RecipeSerializers.ASSEMBLY_CLONING.get();
    }
}
