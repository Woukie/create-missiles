package net.woukie.createmissiles.missilemanager.parts;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.woukie.createmissiles.missilemanager.Trajectory;

import javax.annotation.Nullable;
import java.util.List;

public class WarheadType extends MissilePartType {
    public final float weight;
    public final Detonatable detonatable;
    public final int startSlot = 0;
    public final int endSlot = 32;

    public WarheadType(Component displayName, ResourceLocation resourceLocation, @Nullable WriteData writeData, float weight, @Nullable Detonatable detonatable) {
        super(displayName, resourceLocation, writeData);
        this.weight = weight;
        this.detonatable = detonatable;
    }

    public interface Detonatable {
        void detonate(Trajectory trajectory);
    }
}
