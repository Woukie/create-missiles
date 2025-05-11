package net.woukie.createmissiles.missilemanager.parts;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public abstract class MissilePart {
    public final Component displayName;
    public final ResourceLocation resourceLocation;

    protected MissilePart(Component displayName, ResourceLocation resourceLocation) {
        this.displayName = displayName;
        this.resourceLocation = resourceLocation;
    }
}
