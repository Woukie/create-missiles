package net.woukie.createmissiles.missilemanager.parts;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;

import javax.annotation.Nullable;

public abstract class MissilePartType {
    public final Component displayName;
    public final ResourceLocation resourceLocation;
    public final WriteData writeData;

    public abstract int getStartSlot();
    public abstract int getEndSlot();

    protected MissilePartType(Component displayName, ResourceLocation resourceLocation, @Nullable WriteData writeData) {
        this.displayName = displayName;
        this.resourceLocation = resourceLocation;
        this.writeData = writeData;
    }

    public interface WriteData {
        CompoundTag write(Container container, CompoundTag data);
    }
}
