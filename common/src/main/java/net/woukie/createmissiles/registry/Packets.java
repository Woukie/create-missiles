package net.woukie.createmissiles.registry;

import dev.architectury.networking.NetworkChannel;
import net.minecraft.resources.ResourceLocation;
import net.woukie.createmissiles.CreateMissiles;
import net.woukie.createmissiles.block.controlpanel.messages.ClickLaunchMessage;
import net.woukie.createmissiles.block.navigationpanel.messages.ClickFuelMessage;
import net.woukie.createmissiles.block.navigationpanel.messages.ClickMapMessage;
import net.woukie.createmissiles.missilemanager.parts.warheads.messages.ExplodeFireworkMessage;

public class Packets {
    public static final NetworkChannel NAVIGATION_PANEL_CLICK_MAP = NetworkChannel.create(new ResourceLocation(CreateMissiles.MOD_ID, "navigation_panel_click_map"));
    public static final NetworkChannel NAVIGATION_PANEL_CLICK_FUEL = NetworkChannel.create(new ResourceLocation(CreateMissiles.MOD_ID, "navigation_panel_click_fuel"));
    public static final NetworkChannel CONTROL_PANEL_CLICK_LAUNCH = NetworkChannel.create(new ResourceLocation(CreateMissiles.MOD_ID, "control_panel_click_launch"));
    public static final NetworkChannel EXPLODE_FIREWORK = NetworkChannel.create(new ResourceLocation(CreateMissiles.MOD_ID, "explode_firework"));

    public static void init() {
        CreateMissiles.LOGGER.info("Registering packet resource locations for " + CreateMissiles.NAME);

        NAVIGATION_PANEL_CLICK_MAP.register(ClickMapMessage.class, ClickMapMessage::encode, ClickMapMessage::new, ClickMapMessage::apply);
        NAVIGATION_PANEL_CLICK_FUEL.register(ClickFuelMessage.class, ClickFuelMessage::encode, ClickFuelMessage::new, ClickFuelMessage::apply);
        CONTROL_PANEL_CLICK_LAUNCH.register(ClickLaunchMessage.class, ClickLaunchMessage::encode, ClickLaunchMessage::new, ClickLaunchMessage::apply);
        EXPLODE_FIREWORK.register(ExplodeFireworkMessage.class, ExplodeFireworkMessage::encode, ExplodeFireworkMessage::new, ExplodeFireworkMessage::apply);
    }
}
