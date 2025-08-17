package net.woukie.createmissiles.item.assembly;

import net.minecraft.Util;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.woukie.createmissiles.recipe.MissileIngredient;
import net.woukie.createmissiles.recipe.MissilePartRecipe;
import net.woukie.createmissiles.registry.PartTypes;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public class AssemblyItem extends Item {
    public AssemblyItem(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull Component getName(ItemStack itemStack) {
        CompoundTag compoundTag = itemStack.getTag();
        if (compoundTag != null) {
            var type = PartTypes.get(new ResourceLocation(compoundTag.getString("PartType")));
            if (type == null) return Component.translatable("item.createmissiles.assembly_invalid");
            return type.getDisplayName();
        }

        return super.getName(itemStack);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack itemStack, @Nullable Level level, @NotNull List<Component> list, @NotNull TooltipFlag tooltipFlag) {
        super.appendHoverText(itemStack, level, list, tooltipFlag);

        CompoundTag compoundTag = itemStack.getTag();
        if (compoundTag == null) return;

        Optional<MissilePartRecipe> recipe = MissilePartRecipe.fromResourceLocation(level, new ResourceLocation(compoundTag.getString("PartType")));
        if (recipe.isPresent()) {
            List<MissileIngredient> ingredients = recipe.get().getMissileIngredients();

            ingredients.forEach(ingredient -> {
                List<ItemStack> items = ingredient.getAllValidItems();

                Component[] names = items.stream().map(ItemStack::getDisplayName).toList().toArray(new Component[0]);
                String name = names[(int)(Util.getMillis() / 1000f) % names.length].getString();
                list.add(Component.literal(ingredient.count() + " " + name.substring(1, name.length() - 1)));
            });
        }
    }

    public static ItemStack createWith(ResourceLocation partTypeResourceLocation, ItemLike item) {
        ItemStack itemStack = new ItemStack(item);
        CompoundTag compoundTag = itemStack.getOrCreateTag();
        compoundTag.putString("PartType", partTypeResourceLocation.toString());
        itemStack.setTag(compoundTag);
        return itemStack;
    }
}
