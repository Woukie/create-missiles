package net.woukie.createmissiles.jei;

import mezz.jei.api.ingredients.subtypes.ISubtypeInterpreter;
import mezz.jei.api.ingredients.subtypes.UidContext;
import net.minecraft.world.item.ItemStack;
import net.woukie.createmissiles.registry.DataComponents;
import org.jetbrains.annotations.Nullable;

public class AssemblySubtypeInterpreter implements ISubtypeInterpreter<ItemStack> {
    public static final AssemblySubtypeInterpreter INSTANCE = new AssemblySubtypeInterpreter();

    private AssemblySubtypeInterpreter() {}

    @Override
    public @Nullable Object getSubtypeData(ItemStack ingredient, UidContext context) {
        return ingredient.get(DataComponents.PART_TYPE);
    }

    @Override
    public String getLegacyStringSubtypeInfo(ItemStack itemStack, UidContext context) {
        String partType = itemStack.get(DataComponents.PART_TYPE);
        if (partType == null) {
            return "";
        }

        return partType;
    }
}
