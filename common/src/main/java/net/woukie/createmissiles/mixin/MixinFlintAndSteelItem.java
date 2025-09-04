package net.woukie.createmissiles.mixin;

import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.FlintAndSteelItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.woukie.createmissiles.entity.FrozenAreaEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Objects;

@Mixin(FlintAndSteelItem.class)
public abstract class MixinFlintAndSteelItem extends Item {
    public MixinFlintAndSteelItem(Properties properties) {
        super(properties);
    }

    @Inject(method = "useOn", at = @At("HEAD"), cancellable = true)
    public void useOn(UseOnContext useOnContext, CallbackInfoReturnable<InteractionResult> cir) {
        var level = useOnContext.getLevel();
        var player = useOnContext.getPlayer();
        if (player != null) {
            List<FrozenAreaEntity> list = level.getEntitiesOfClass(FrozenAreaEntity.class, player.getBoundingBox().inflate(2.0F), Objects::nonNull);
            list.forEach(FrozenAreaEntity::flintAndSteeled);
            if (!list.isEmpty()) cir.setReturnValue(InteractionResult.SUCCESS);
        }
    }
}
