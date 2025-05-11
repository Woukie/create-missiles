package net.woukie.createmissiles.missilemanager.parts;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.woukie.createmissiles.missilemanager.Trajectory;

public class Warhead extends MissilePart {
    public final float weight;
    public final Detonatable detonatable;

    public Warhead(Component displayName, ResourceLocation resourceLocation, float weight, Detonatable detonatable) {
        super(displayName, resourceLocation);
        this.weight = weight;
        this.detonatable = detonatable;
    }

    public interface Detonatable {
        void detonate(Trajectory trajectory);
    }
}
