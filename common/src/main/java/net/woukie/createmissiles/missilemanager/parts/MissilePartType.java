package net.woukie.createmissiles.missilemanager.parts;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.woukie.createmissiles.client.MissilePartModel;

import javax.annotation.Nullable;

public abstract class MissilePartType {
    public final Component displayName;
    public final ResourceLocation resourceLocation;
    public final WriteData writeData;
    public final MissilePartModel model;

    public abstract int getStartSlot();
    public abstract int getEndSlot();

    protected MissilePartType(Component displayName, ResourceLocation resourceLocation, @Nullable WriteData writeData, MissilePartModel model) {
        this.displayName = displayName;
        this.resourceLocation = resourceLocation;
        this.writeData = writeData;
        this.model = model;
    }

    public interface WriteData {
        CompoundTag write(Container container, CompoundTag data);
    }
}
