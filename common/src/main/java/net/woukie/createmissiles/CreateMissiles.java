package net.woukie.createmissiles;

import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import com.simibubi.create.Create;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.item.render.CustomRenderedItems;
import dev.architectury.event.events.common.LifecycleEvent;
import dev.architectury.event.events.common.TickEvent;
import dev.architectury.registry.menu.MenuRegistry;
import dev.architectury.registry.registries.RegistrarManager;
import net.woukie.createmissiles.client.screens.AssemblyPanelScreen;
import net.woukie.createmissiles.client.screens.ControlPanelScreen;
import net.woukie.createmissiles.client.screens.NavigationPanelScreen;
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

        LifecycleEvent.SERVER_STARTED.register(instance -> Trajectories.get().init(instance));
        LifecycleEvent.SERVER_STOPPING.register(instance -> Trajectories.get().stop());
        TickEvent.SERVER_PRE.register(instance -> Trajectories.get().serverTick(instance));

        Blocks.init();
        BlockEntities.init();
        PartTypes.init();
        Items.init();
        CreativeMenus.init();
        Packets.init();
        Menus.init();
        RecipeSerializers.init();
        RecipeTypes.init();
        SpriteShifts.init();
        EntityTypes.init();
        SoundEvents.init();
        ParticleTypes.init();
    }

    public static CreateRegistrate registrate() {
        return REGISTRATE;
    }

    public static void initClient() {
        MenuRegistry.registerScreenFactory(Menus.CONTROL_PANEL.get(), ControlPanelScreen::new);
        MenuRegistry.registerScreenFactory(Menus.NAVIGATION_PANEL.get(), NavigationPanelScreen::new);
        MenuRegistry.registerScreenFactory(Menus.ASSEMBLY_PANEL.get(), AssemblyPanelScreen::new);

        CustomRenderedItems.register(Items.WARHEAD_ASSEMBLY.get());
        CustomRenderedItems.register(Items.CHASSIS_ASSEMBLY.get());
        CustomRenderedItems.register(Items.THRUSTER_ASSEMBLY.get());
    }
}
