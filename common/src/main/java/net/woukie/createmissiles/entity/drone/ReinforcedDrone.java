package net.woukie.createmissiles.entity.drone;

import net.minecraft.core.Direction;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.woukie.createmissiles.registry.Items;

public class ReinforcedDrone extends Drone {
    public ReinforcedDrone(EntityType<? extends Drone> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected void dropItem() {
        DefaultDispenseItemBehavior.spawnItem(level(), new ItemStack(Items.REINFORCED_DRONE_BOX.get()), 1, Direction.UP, position());
    }
}
