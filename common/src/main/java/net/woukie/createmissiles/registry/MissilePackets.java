package net.woukie.createmissiles.registry;

import dev.architectury.networking.NetworkChannel;
import net.minecraft.resources.ResourceLocation;
import net.woukie.createmissiles.CreateMissiles;
import net.woukie.createmissiles.block.launchpadcontroller.SetControllerTargetMessage;

public class MissilePackets {
    public static final NetworkChannel SET_CONTROLLER_TARGET = NetworkChannel.create(new ResourceLocation(CreateMissiles.MOD_ID, "set_controller_target"));

    public static void init() {
        CreateMissiles.LOGGER.info("Registering packet resource locations for " + CreateMissiles.NAME);

        SET_CONTROLLER_TARGET.register(SetControllerTargetMessage.class, SetControllerTargetMessage::encode, SetControllerTargetMessage::new, SetControllerTargetMessage::apply);
    }
}
