package net.woukie.createmissiles;

import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import com.simibubi.create.Create;
import com.simibubi.create.foundation.data.CreateRegistrate;
import dev.architectury.event.events.common.LifecycleEvent;
import dev.architectury.event.events.common.TickEvent;
import dev.architectury.registry.registries.RegistrarManager;
import net.woukie.createmissiles.missilemanager.Trajectories;
import net.woukie.createmissiles.registry.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CreateMissiles {
    public static final String MOD_ID = "createmissiles";
    public static final String NAME = "Create Missiles";
    public static final Logger LOGGER = LoggerFactory.getLogger(NAME);

    private static final CreateRegistrate REGISTRATE = CreateRegistrate.create(MOD_ID);
    public static final Supplier<RegistrarManager> MANAGER = Suppliers.memoize(() -> RegistrarManager.get(MOD_ID));

    public static void init() {
        LOGGER.info("{} initializing! Create version: {}}", NAME, Create.VERSION);

        LifecycleEvent.SERVER_STARTED.register(instance -> {
            Trajectories.get().init(instance);
        });

        TickEvent.SERVER_PRE.register(instance -> {
            Trajectories.get().serverTick(instance);
        });

//        ClientTickEvent.CLIENT_PRE.register(minecraft -> {
//            minecraft.level.putNonPlayerEntity(1000001, new RocketEntity());
//        });

        MissileBlocks.init();
        MissileBlockEntities.init();
        MissileParts.init();
        MissileItems.init();
        MissileCreativeMenu.init();
        MissilePackets.init();
        MissileMenus.init();
        MissileRecipeSerializers.init();
        MissileRecipeTypes.init();
        MissileSpriteShifts.init();
        MissileEntityTypes.init();
    }

    public static CreateRegistrate registrate() {
        return REGISTRATE;
    }
}
