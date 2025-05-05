package net.woukie.createmissiles.registry;

import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.builders.MenuBuilder;
import com.tterrag.registrate.util.entry.MenuEntry;
import net.woukie.createmissiles.CreateMissiles;
import net.woukie.createmissiles.block.launchpadcontroller.LaunchPadControllerMenu;
import net.woukie.createmissiles.block.launchpadcontroller.LaunchPadControllerScreen;

public class MissileMenus {
    private static final CreateRegistrate REGISTRATE = CreateMissiles.registrate();

    public static final MenuEntry<LaunchPadControllerMenu> LAUNCH_PAD_CONTROLLER_MENU = REGISTRATE.menu("launch_pad_controller", (MenuBuilder.MenuFactory<LaunchPadControllerMenu>) LaunchPadControllerMenu::new, () -> LaunchPadControllerScreen::new).register();

    public static void init() {
        CreateMissiles.LOGGER.info("Registering menus for " + CreateMissiles.NAME);
    }
}
