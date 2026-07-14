package net.woukie.createmissiles.item.assembly;

import net.minecraft.Util;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.woukie.createmissiles.recipe.MissileIngredient;
import net.woukie.createmissiles.recipe.MissilePartRecipe;
import net.woukie.createmissiles.registry.DataComponents;
import net.woukie.createmissiles.registry.PartTypes;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

public class AssemblyItem extends Item {
    private static final String LEGACY_NBT_KEY = "PartType";

    public AssemblyItem(Properties properties) {
        super(properties);
    }

    @Override
    public void verifyComponentsAfterLoad(ItemStack itemStack) {
        CustomData customData = itemStack.get(net.minecraft.core.component.DataComponents.CUSTOM_DATA);
        if (customData == null || !customData.contains(LEGACY_NBT_KEY)) return;

        if (!itemStack.has(DataComponents.PART_TYPE)) {
            String partType = customData.copyTag().getString(LEGACY_NBT_KEY);
            if (!partType.isEmpty()) itemStack.set(DataComponents.PART_TYPE, partType);
        }

        CustomData.update(net.minecraft.core.component.DataComponents.CUSTOM_DATA, itemStack, tag -> tag.remove(LEGACY_NBT_KEY));
    }

    @Override
    public @NotNull Component getName(ItemStack itemStack) {
        String partType = itemStack.get(DataComponents.PART_TYPE);
        if (partType != null) {
            var type = PartTypes.get(ResourceLocation.parse(partType));
            if (type == null) return Component.translatable("item.createmissiles.assembly_invalid");
            return type.getDisplayName();
        }

        return super.getName(itemStack);
    }

    @Override
    public void appendHoverText(ItemStack itemStack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        super.appendHoverText(itemStack, context, tooltipComponents, tooltipFlag);
        String partType = itemStack.get(DataComponents.PART_TYPE);
        if (partType == null) return;

        Level level = context.level();
        if (level == null) return;

        Optional<MissilePartRecipe> recipe = MissilePartRecipe.fromResourceLocation(level, ResourceLocation.parse(partType));
        if (recipe.isPresent()) {
            List<MissileIngredient> ingredients = recipe.get().getMissileIngredients();

            ingredients.forEach(ingredient -> {
                List<ItemStack> items = ingredient.getAllValidItems();

                Component[] names = items.stream().map(ItemStack::getDisplayName).toList().toArray(new Component[0]);
                String name = names[(int)(Util.getMillis() / 1000f) % names.length].getString();
                tooltipComponents.add(Component.literal(ingredient.count() + " " + name.substring(1, name.length() - 1)));
            });
        }
    }

    public static ItemStack createWith(ResourceLocation partTypeResourceLocation, ItemLike item) {
        ItemStack itemStack = new ItemStack(item);
        itemStack.set(DataComponents.PART_TYPE, partTypeResourceLocation.toString());
        return itemStack;
    }
}