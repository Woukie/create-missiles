package net.woukie.createmissiles.mixin;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.woukie.createmissiles.entity.InfernalAreaEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Objects;

@Mixin(BucketItem.class)
public abstract class MixinBucketItem extends Item {
    @Shadow @Final private Fluid content;

    public MixinBucketItem(Properties properties) {
        super(properties);
    }

    @Inject(method = "use", at = @At("HEAD"), cancellable = true)
    public void use(Level level, Player player, InteractionHand interactionHand, CallbackInfoReturnable<InteractionResultHolder<ItemStack>> cir) {
        if (this.content.isSame(Fluids.WATER)) {
            List<InfernalAreaEntity> list = level.getEntitiesOfClass(InfernalAreaEntity.class, player.getBoundingBox().inflate(2.0F), Objects::nonNull);
            list.forEach(InfernalAreaEntity::extingish);
            if (!list.isEmpty()) cir.setReturnValue(InteractionResultHolder.sidedSuccess(player.getItemInHand(interactionHand), level.isClientSide()));
        }
    }
}
