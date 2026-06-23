package net.woukie.createmissiles.inventory;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class InvisibleSlot extends Slot {
    public InvisibleSlot(Container container, int i) {
        super(container, i, 0, 0);
    }

    public boolean mayPlace(@NotNull ItemStack itemStack) {
        return false;
    }

    public boolean isActive() {
        return false;
    }

    public boolean mayPickup(@NotNull Player player) {
        return false;
    }

    public boolean isHighlightable() {
        return false;
    }
}

