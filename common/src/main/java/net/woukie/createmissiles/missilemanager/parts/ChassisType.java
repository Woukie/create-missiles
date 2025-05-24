package net.woukie.createmissiles.missilemanager.parts;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nullable;
import java.util.List;

public class ChassisType extends MissilePartType {
    public final float fuelCapacity;

    public ChassisType(Component displayName, ResourceLocation resourceLocation, @Nullable WriteData writeData, List<Ingredient> ingredients, float fuelCapacity) {
        super(displayName, resourceLocation, writeData, ingredients);
        this.fuelCapacity = fuelCapacity;
    }
}