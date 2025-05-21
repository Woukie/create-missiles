package net.woukie.createmissiles.missilemanager.parts;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nullable;
import java.awt.*;
import java.util.Collections;
import java.util.List;

public abstract class MissilePartType {
    public final Component displayName;
    public final ResourceLocation resourceLocation;
    public final WriteData writeData;
    public final List<Ingredient> ingredients;

    protected MissilePartType(Component displayName, ResourceLocation resourceLocation, @Nullable WriteData writeData, List<Ingredient> ingredients) {
        this.displayName = displayName;
        this.resourceLocation = resourceLocation;
        this.writeData = writeData;
        this.ingredients = Collections.unmodifiableList(ingredients);
    }

    public interface WriteData {
        CompoundTag write(Container container, CompoundTag data);
    }
}
