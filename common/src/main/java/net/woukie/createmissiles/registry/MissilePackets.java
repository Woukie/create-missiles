package net.woukie.createmissiles.registry;

import dev.architectury.networking.NetworkChannel;
import net.minecraft.resources.ResourceLocation;
import net.woukie.createmissiles.CreateMissiles;
import net.woukie.createmissiles.block.navigator.messages.ClickFuelMessage;
import net.woukie.createmissiles.block.navigator.messages.ClickMapMessage;

public class MissilePackets {
    public static final NetworkChannel NAVIGATOR_CLICK_MAP = NetworkChannel.create(new ResourceLocation(CreateMissiles.MOD_ID, "navigator_map_click"));
    public static final NetworkChannel NAVIGATOR_CLICK_FUEL = NetworkChannel.create(new ResourceLocation(CreateMissiles.MOD_ID, "navigator_fuel_click"));

    public static void init() {
        CreateMissiles.LOGGER.info("Registering packet resource locations for " + CreateMissiles.NAME);

        NAVIGATOR_CLICK_MAP.register(ClickMapMessage.class, ClickMapMessage::encode, ClickMapMessage::new, ClickMapMessage::apply);
        NAVIGATOR_CLICK_FUEL.register(ClickFuelMessage.class, ClickFuelMessage::encode, ClickFuelMessage::new, ClickFuelMessage::apply);
    }
}
