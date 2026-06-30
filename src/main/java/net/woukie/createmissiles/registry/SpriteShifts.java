package net.woukie.createmissiles.registry;

import com.simibubi.create.foundation.block.connected.AllCTTypes;
import com.simibubi.create.foundation.block.connected.CTSpriteShiftEntry;
import com.simibubi.create.foundation.block.connected.CTSpriteShifter;
import com.simibubi.create.foundation.block.connected.CTType;
import net.minecraft.resources.ResourceLocation;
import net.woukie.createmissiles.CreateMissiles;

public class SpriteShifts {
    public static final CTSpriteShiftEntry LAUNCH_PAD_SIDE = horizontal("launch_pad_side"),
            LAUNCH_PAD_TOP = omni("launch_pad_top");

    private static CTSpriteShiftEntry horizontal(String name) {
        return getCT(AllCTTypes.HORIZONTAL, name);
    }

    private static CTSpriteShiftEntry omni(String name) {
        return getCT(AllCTTypes.OMNIDIRECTIONAL, name);
    }

    private static CTSpriteShiftEntry getCT(CTType type, String blockTextureName) {
        return getCT(type, blockTextureName, blockTextureName);
    }

    private static CTSpriteShiftEntry getCT(CTType type, String blockTextureName, String connectedTextureName) {
        return CTSpriteShifter.getCT(type, ResourceLocation.fromNamespaceAndPath(CreateMissiles.MOD_ID, "block/" + blockTextureName),
                ResourceLocation.fromNamespaceAndPath(CreateMissiles.MOD_ID, "block/" + connectedTextureName + "_connected"));
    }

    public static void init() {
        CreateMissiles.LOGGER.info("Registering sprite shifts for " + CreateMissiles.NAME);
    }
}
