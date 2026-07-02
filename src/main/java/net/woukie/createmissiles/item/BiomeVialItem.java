package net.woukie.createmissiles.item;

import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import net.woukie.createmissiles.registry.Items;
import org.jetbrains.annotations.NotNull;

public class BiomeVialItem extends Item {
    public BiomeVialItem(Properties properties) {
        super(properties);
    }

    public boolean isFull(@NotNull ItemStack itemStack) {
        CustomData customData = itemStack.get(DataComponents.CUSTOM_DATA);
        return customData != null && customData.copyTag().contains("biome");
    }

    @Override
    public Component getName(ItemStack itemStack) {
        CustomData customData = itemStack.get(DataComponents.CUSTOM_DATA);
        if (customData != null && customData.copyTag().contains("biome")) {
            String biome = customData.copyTag().getString("biome");
            return Component.translatable("biome." + biome.replace(":", "."));
        }
        return super.getName(itemStack);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {
        ItemStack itemStack = player.getItemInHand(interactionHand);
        if (isFull(itemStack)) return InteractionResultHolder.pass(itemStack);

        BlockPos position = player.blockPosition();
        var biomeKeyOptional = level.getBiome(position).unwrapKey();
        if (biomeKeyOptional.isEmpty()) return InteractionResultHolder.pass(itemStack);

        itemStack.shrink(1);
        var newItem = new ItemStack(Items.BIOME_VIAL.get(), 1);
        CompoundTag tag = new CompoundTag();
        tag.putString("biome", biomeKeyOptional.get().location().toString());
        newItem.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
        player.addItem(newItem);
        level.playSound(null, player.position().x, player.position().y, player.position().z, SoundEvents.BOTTLE_FILL, SoundSource.PLAYERS, 1, 1);

        return InteractionResultHolder.success(itemStack);
    }
}