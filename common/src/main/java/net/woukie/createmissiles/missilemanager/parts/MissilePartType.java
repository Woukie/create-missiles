package net.woukie.createmissiles.missilemanager.parts;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.woukie.createmissiles.client.MissilePartModel;

import javax.annotation.Nullable;
import java.util.List;

public abstract class MissilePartType {
    public final Component displayName;
    public final ResourceLocation resourceLocation;
    public final WriteData writeData;
    public final List<MissilePartModel> models;

    public abstract int getStartSlot();
    public abstract int getEndSlot();

    protected MissilePartType(Component displayName, ResourceLocation resourceLocation, @Nullable WriteData writeData, List<MissilePartModel> models) {
        this.displayName = displayName;
        this.resourceLocation = resourceLocation;
        this.writeData = writeData;
        this.models = models;
    }

    public interface WriteData {
        CompoundTag write(Container container, CompoundTag data);
    }
}
