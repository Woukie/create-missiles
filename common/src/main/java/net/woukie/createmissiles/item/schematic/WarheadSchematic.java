package net.woukie.createmissiles.item.schematic;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StringUtil;
import net.minecraft.world.item.*;
import net.woukie.createmissiles.missilemanager.parts.Warhead;
import net.woukie.createmissiles.missilemanager.parts.warheads.WarheadRegistry;
import org.jetbrains.annotations.NotNull;

public class WarheadSchematic extends Item {
    public WarheadSchematic(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull Component getName(ItemStack itemStack) {
        CompoundTag compoundTag = itemStack.getTag();
        if (compoundTag != null) {

            WarheadRegistry.get((new ResourceLocation(compoundTag.getString("warhead")))).getDisplayName();

            String string = compoundTag.getString("title");
            if (!StringUtil.isNullOrEmpty(string)) {
                return Component.literal(string);
            }
        }

        return super.getName(itemStack);
    }

    public static String getWarhead(ItemStack itemStack) {
        CompoundTag compoundTag = itemStack.getTag();
        return compoundTag != null ? compoundTag.getString("Warhead") : "";
    }
}
