package net.woukie.createmissiles.registry;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.woukie.createmissiles.CreateMissiles;
import net.woukie.createmissiles.block.controlpanel.messages.ClickLaunchMessage;
import net.woukie.createmissiles.block.controlpanel.messages.TriggerBuildParticles;
import net.woukie.createmissiles.block.navigationpanel.messages.ClickFuelMessage;
import net.woukie.createmissiles.block.navigationpanel.messages.ClickMapMessage;
import net.woukie.createmissiles.block.navigationpanel.messages.UpdateMapDataMessage;
import net.woukie.createmissiles.client.CreateFlashMessage;
import net.woukie.createmissiles.entity.drone.SendDroneMessage;
import net.woukie.createmissiles.missiles.parts.warheads.messages.ExplodeFireworkMessage;

public class Packets {
    public static final NetworkChannel CREATE_FLASH = NetworkChannel.create(ResourceLocation.fromNamespaceAndPath(CreateMissiles.MOD_ID, "create_flash"));

    public static void onRegisterPayloadHandlers(RegisterPayloadHandlersEvent event) {
        CreateMissiles.LOGGER.info("Registering payload handlers for " + CreateMissiles.NAME);

        final PayloadRegistrar registrar = event.registrar("1");

        registrar.playToServer(ClickMapMessage.TYPE, ClickMapMessage.STREAM_CODEC, ClickMapMessage::apply);
        registrar.playToServer(ClickFuelMessage.TYPE, ClickFuelMessage.STREAM_CODEC, ClickFuelMessage::apply);
        registrar.playToServer(ClickLaunchMessage.TYPE, ClickLaunchMessage.STREAM_CODEC, ClickLaunchMessage::apply);
        registrar.playToServer(ExplodeFireworkMessage.TYPE, ExplodeFireworkMessage.STREAM_CODEC, ExplodeFireworkMessage::apply);
        registrar.playToServer(UpdateMapDataMessage.TYPE, UpdateMapDataMessage.STREAM_CODEC, UpdateMapDataMessage::apply);
        registrar.playToServer(SendDroneMessage.TYPE, SendDroneMessage.STREAM_CODEC, SendDroneMessage::apply);
        registrar.playToServer(TriggerBuildParticles.TYPE, TriggerBuildParticles.STREAM_CODEC, TriggerBuildParticles::apply);
        registrar.playToServer(CreateFlashMessage.TYPE, CreateFlashMessage.STREAM_CODEC, CreateFlashMessage::apply);
    }
}
