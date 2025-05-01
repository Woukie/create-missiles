package net.woukie.createmissiles.registry;

import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.entry.ItemEntry;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.woukie.createmissiles.CreateMissiles;

public class MissileItems {
    private static final CreateRegistrate REGISTRATE = CreateMissiles.registrate();

    public static void init() {
        CreateMissiles.LOGGER.info("Registering items for " + CreateMissiles.NAME);
    }
}
