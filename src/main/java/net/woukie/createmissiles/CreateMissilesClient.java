package net.woukie.createmissiles;

import com.simibubi.create.foundation.item.render.CustomRenderedItems;
import net.createmod.ponder.foundation.PonderIndex;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import net.neoforged.neoforge.client.event.RenderGuiEvent;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.common.NeoForge;
import net.woukie.createmissiles.client.AssemblyRenderer;
import net.woukie.createmissiles.client.FlashHandler;
import net.woukie.createmissiles.client.screens.AssemblyPanelScreen;
import net.woukie.createmissiles.client.screens.ControlPanelScreen;
import net.woukie.createmissiles.client.screens.DroneScreen;
import net.woukie.createmissiles.client.screens.NavigationPanelScreen;
import net.woukie.createmissiles.item.BiomeVialItem;
import net.woukie.createmissiles.particle.BuildShrapnel;
import net.woukie.createmissiles.registry.*;
import org.jetbrains.annotations.NotNull;

@Mod(value = CreateMissiles.MOD_ID, dist = Dist.CLIENT)
public class CreateMissilesClient {
    public CreateMissilesClient(IEventBus modEventBus, ModContainer container) {
        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);

        modEventBus.addListener(EntityRenderers::registerEntityRenderers);
        modEventBus.addListener(EntityRenderers::registerLayerDefinitions);
        modEventBus.addListener(CreateMissilesClient::registerScreens);
        modEventBus.addListener(CreateMissilesClient::clientSetup);
        modEventBus.addListener(CreateMissilesClient::onRegisterClientExtensions);
        modEventBus.addListener(CreateMissilesClient::registerParticles);

        NeoForge.EVENT_BUS.addListener(CreateMissilesClient::onClientTickPost);
        NeoForge.EVENT_BUS.addListener(CreateMissilesClient::onRenderGuiEvent);
    }

    public static void registerScreens(RegisterMenuScreensEvent event) {
        event.register(Menus.CONTROL_PANEL.get(), ControlPanelScreen::new);
        event.register(Menus.NAVIGATION_PANEL.get(), NavigationPanelScreen::new);
        event.register(Menus.ASSEMBLY_PANEL.get(), AssemblyPanelScreen::new);
        event.register(Menus.DRONE.get(), DroneScreen::new);
    }

    public static void clientSetup(FMLClientSetupEvent event) {
        CustomRenderedItems.register(Items.WARHEAD_ASSEMBLY.get());
        CustomRenderedItems.register(Items.CHASSIS_ASSEMBLY.get());
        CustomRenderedItems.register(Items.THRUSTER_ASSEMBLY.get());

        event.enqueueWork(() -> {
            ItemProperties.register(
                    Items.BIOME_VIAL.get(),
                    ResourceLocation.withDefaultNamespace("full"),
                    (itemStack, clientLevel, livingEntity, i) -> {
                        if (livingEntity == null) return 0.0F;
                        if (itemStack.getItem() instanceof BiomeVialItem item) {
                            return item.isFull(itemStack) ? 1.0F : 0.0F;
                        }
                        return 0.0F;
                    }
            );
        });

        PartModels.init();
        PonderIndex.addPlugin(new PonderPlugin());
    }

    public static void registerParticles(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(ParticleTypes.BUILD_SHRAPNEL.get(), BuildShrapnel.Provider::new);
    }

    public static void onRegisterClientExtensions(RegisterClientExtensionsEvent event) {
        event.registerItem(new IClientItemExtensions() {
            private final AssemblyRenderer renderer = new AssemblyRenderer();

            @Override
            public @NotNull BlockEntityWithoutLevelRenderer getCustomRenderer() {
                return renderer;
            }
        }, Items.WARHEAD_ASSEMBLY.get(), Items.CHASSIS_ASSEMBLY.get(), Items.THRUSTER_ASSEMBLY.get());
    }

    public static void onClientTickPost(ClientTickEvent.Post event) {
        FlashHandler.cleanUp();
    }

    public static void onRenderGuiEvent(RenderGuiEvent.Post event) {
        FlashHandler.handleHudRender(event.getGuiGraphics());
    }
}
