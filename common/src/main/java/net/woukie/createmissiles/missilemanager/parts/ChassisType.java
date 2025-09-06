package net.woukie.createmissiles.missilemanager.parts;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public abstract class ChassisType extends MissilePartType {
    @Override
    public int getStartSlot() {
        return 32;
    }

    @Override
    public int getEndSlot() {
        return 64;
    }

    public abstract float getFuelCapacity();

    @Override
    public void registerJEIStats(MutableComponent component) {
        super.registerJEIStats(component);
        component.append(Component.translatable("description.jei.createmissiles.generic.capacity", Float.toString(getFuelCapacity())));
        component.append("\n");
    }
}