package net.woukie.createmissiles.item;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrownEnderpearl;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

public class BoundEnderPearlItem extends Item {
    public BoundEnderPearlItem(Properties properties) {
        super(properties);
    }

    @Override
    public boolean isFoil(@NotNull ItemStack itemStack) {
        return itemStack.hasTag() && itemStack.getTag().hasUUID("PlayerUUID");
    }

    @Override
    public void appendHoverText(@NotNull ItemStack itemStack, @Nullable Level level, @NotNull List<Component> list, @NotNull TooltipFlag tooltipFlag) {
        super.appendHoverText(itemStack, level, list, tooltipFlag);

        CompoundTag tag = itemStack.getTag();
        if (level == null || tag == null || !tag.hasUUID("PlayerUUID")) {
            return;
        }

        UUID playerUUID = tag.getUUID("PlayerUUID");
        Player targetPlayer = level.getPlayerByUUID(playerUUID);
        if (targetPlayer == null) {
            list.add(Component.translatable("item.createmissiles.bound_ender_pearl_not_online"));
        }
    }

    @Override
    public void inventoryTick(ItemStack itemStack, @NotNull Level level, Entity entity, int i, boolean bl) {
        super.inventoryTick(itemStack, level, entity, i, bl);

        if (level.isClientSide) return;

        CompoundTag tag = itemStack.getOrCreateTag();
        if (tag.hasUUID("PlayerUUID")) return;

        tag.putUUID("PlayerUUID", entity.getUUID());
        tag.putString("PlayerName", entity.getName().getString());

        itemStack.resetHoverName();
    }

    @Override
    public @NotNull Component getName(@NotNull ItemStack itemStack) {
        CompoundTag tag = itemStack.getTag();
        if (tag == null || !tag.hasUUID("PlayerUUID")) {
            return Component.translatable("item.createmissiles.bound_ender_pearl");
        }

        String playerName = tag.getString("PlayerName");
        return Component.translatable("item.createmissiles.bound_ender_pearl_player", playerName.isEmpty() ? "Unknown" : playerName);
    }

    @Override
    public void onCraftedBy(ItemStack itemStack, @NotNull Level level, Player player) {
        var tag = itemStack.getOrCreateTag();
        tag.putUUID("PlayerUUID", player.getUUID());
        tag.putString("PlayerName", player.getName().getString());

        super.onCraftedBy(itemStack, level, player);
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, Player throwingPlayer, @NotNull InteractionHand interactionHand) {
        ItemStack itemStack = throwingPlayer.getItemInHand(interactionHand);

        CompoundTag tag = itemStack.getTag();
        if (tag != null && tag.hasUUID("PlayerUUID")) {
            UUID playerUUID = tag.getUUID("PlayerUUID");
            Player targetPlayer = level.getPlayerByUUID(playerUUID);
            if (targetPlayer == null) return InteractionResultHolder.fail(itemStack);

            throwingPlayer.getCooldowns().addCooldown(this, 20);
            level.playSound((Player)null, throwingPlayer.getX(), throwingPlayer.getY(), throwingPlayer.getZ(), SoundEvents.CHAIN_BREAK, SoundSource.NEUTRAL, 0.5F, 0.4F / (level.getRandom().nextFloat() * 0.4F + 1.2F));
            level.playSound((Player)null, throwingPlayer.getX(), throwingPlayer.getY(), throwingPlayer.getZ(), SoundEvents.ENDER_PEARL_THROW, SoundSource.NEUTRAL, 0.5F, 0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F));
            if (!level.isClientSide) {
                ThrownEnderpearl thrownEnderpearl = new ThrownEnderpearl(level, throwingPlayer);
                thrownEnderpearl.setOwner(targetPlayer);
                thrownEnderpearl.setItem(itemStack);
                thrownEnderpearl.shootFromRotation(throwingPlayer, throwingPlayer.getXRot(), throwingPlayer.getYRot(), 0.0F, 1.5F, 1.0F);
                level.addFreshEntity(thrownEnderpearl);
            }

            throwingPlayer.awardStat(Stats.ITEM_USED.get(this));
            if (!throwingPlayer.getAbilities().instabuild) {
                itemStack.shrink(1);
            }

            return InteractionResultHolder.sidedSuccess(itemStack, level.isClientSide());
        }

        return InteractionResultHolder.fail(itemStack);
    }
}
