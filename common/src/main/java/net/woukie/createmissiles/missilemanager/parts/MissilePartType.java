package net.woukie.createmissiles.missilemanager.parts;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public abstract class MissilePartType {
    public final Component displayName;
    public final ResourceLocation resourceLocation;
    public final WriteData writeData;
    public final int startSlot = 0;
    public final int endSlot = 96;

    protected MissilePartType(Component displayName, ResourceLocation resourceLocation, @Nullable WriteData writeData) {
        this.displayName = displayName;
        this.resourceLocation = resourceLocation;
        this.writeData = writeData;
    }

    public interface WriteData {
        CompoundTag write(Container container, CompoundTag data);
    }
}
