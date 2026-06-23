package net.woukie.createmissiles.item;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.woukie.createmissiles.entity.drone.Drone;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Function;

public class DroneBoxItem extends Item {
    private final Function<Level, Drone> entityConstructor;

    public DroneBoxItem(Properties properties, Function<Level, Drone> entityConstructor) {
        super(properties);
        this.entityConstructor = entityConstructor;
    }

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> list, TooltipFlag tooltipFlag) {
        super.appendHoverText(itemStack, level, list, tooltipFlag);
        list.add(Component.translatable(this.getDescriptionId(itemStack) + ".tooltip"));
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, Player player, @NotNull InteractionHand interactionHand) {
        ItemStack itemStack = player.getItemInHand(interactionHand);
        BlockHitResult blockHitResult = getPlayerPOVHitResult(level, player, ClipContext.Fluid.SOURCE_ONLY);
        if (blockHitResult.getType() != HitResult.Type.BLOCK) {
            return InteractionResultHolder.pass(itemStack);
        } else if (!(level instanceof ServerLevel)) {
            return InteractionResultHolder.success(itemStack);
        } else {
            BlockPos blockPos = blockHitResult.getBlockPos();
            if (level.mayInteract(player, blockPos) && player.mayUseItemAt(blockPos, blockHitResult.getDirection(), itemStack)) {
                Drone droneEntity =  entityConstructor.apply(level);
                droneEntity.setPos(blockPos.getX() + 0.5f, blockPos.getY() + 1, blockPos.getZ() + 0.5f);
                droneEntity.setYRot(player.getYRot());
                droneEntity.setPersistenceRequired();
                level.addFreshEntity(droneEntity);

                if (!player.getAbilities().instabuild) {
                    itemStack.shrink(1);
                }

                player.awardStat(Stats.ITEM_USED.get(this));
                level.gameEvent(player, GameEvent.ENTITY_PLACE, droneEntity.position());

                return InteractionResultHolder.consume(itemStack);
            } else {
                return InteractionResultHolder.fail(itemStack);
            }
        }
    }
}
