package net.woukie.createmissiles;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import java.util.HashMap;

public class ExplosionResistanceOverrides {
    private static final HashMap<Block, Float> overrides = new HashMap<>();

    public static void registerBlock(Block block, float explosionResistance) {
        overrides.put(block, explosionResistance);
    }

    public static float getResistance(Block block) {
        return overrides.getOrDefault(block, block.getExplosionResistance());
    }

    public static void init() {
        registerBlock(Blocks.ANCIENT_DEBRIS, 20);
        registerBlock(Blocks.ANVIL, 20);
        registerBlock(Blocks.CHIPPED_ANVIL, 10);
        registerBlock(Blocks.DAMAGED_ANVIL, 5);
        registerBlock(Blocks.NETHERITE_BLOCK, 20);
        registerBlock(Blocks.CRYING_OBSIDIAN, 20);
        registerBlock(Blocks.RESPAWN_ANCHOR, 20);
        registerBlock(Blocks.ENCHANTING_TABLE, 20);
        registerBlock(Blocks.OBSIDIAN, 20);
        registerBlock(Blocks.REINFORCED_DEEPSLATE, 20);
        registerBlock(Blocks.ENDER_CHEST, 20);
        registerBlock(Blocks.BUBBLE_COLUMN, 5);
        registerBlock(Blocks.LAVA, 5);
        registerBlock(Blocks.WATER, 5);
    }
}
