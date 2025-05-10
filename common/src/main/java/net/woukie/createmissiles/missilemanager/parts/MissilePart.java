package net.woukie.createmissiles.missilemanager.parts;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.apache.commons.lang3.NotImplementedException;

// Missile parts are used by only in controllers and trajectories (created by controllers)
// Schematic items have a reference to the resource location, not a MissilePart instance
// Saving and loading is implemented because variance may be needed per instance, i.e. the amount of uranium loaded into a radioactive warhead
public interface MissilePart {
    static Component getDisplayName() {
        throw new NotImplementedException();
    };

    default ResourceLocation getResourceLocation() {
        return getResourceLocationStatic();
    }

    static ResourceLocation getResourceLocationStatic() {
        throw new NotImplementedException();
    }

    default void loadData(CompoundTag tag) {}

    default CompoundTag saveData(CompoundTag tag) {
        return tag;
    }
}
