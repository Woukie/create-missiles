package net.woukie.createmissiles;

import com.simibubi.create.Create;
import com.simibubi.create.foundation.data.CreateRegistrate;
import net.woukie.createmissiles.registry.MissileBlocks;
import net.woukie.createmissiles.registry.MissileItems;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CreateMissiles {
    public static final String MOD_ID = "createmissiles";
    public static final String NAME = "Create Missiles";
    public static final Logger LOGGER = LoggerFactory.getLogger(NAME);

    private static final CreateRegistrate REGISTRATE = CreateRegistrate.create(MOD_ID);

    public static void init() {
        LOGGER.info("{} initializing! Create version: {} on platform: {}", NAME, Create.VERSION, ExampleExpectPlatform.platformName());

        MissileBlocks.init();
        MissileItems.init();
    }

    public static CreateRegistrate registrate() {
        return REGISTRATE;
    }
}
