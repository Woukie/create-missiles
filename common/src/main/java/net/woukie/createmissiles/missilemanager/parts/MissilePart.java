package net.woukie.createmissiles.missilemanager.parts;

import net.minecraft.nbt.CompoundTag;

public interface MissilePart {
    CompoundTag saveTo(CompoundTag tag);
    void loadFrom(CompoundTag tag);
}
