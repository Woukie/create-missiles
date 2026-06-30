package net.woukie.createmissiles;

import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.item.render.CustomRenderedItems;
import net.createmod.ponder.foundation.PonderIndex;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.trading.ItemCost;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetComponentsFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import net.neoforged.neoforge.client.event.RenderGuiEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.LootTableLoadEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.server.ServerStartedEvent;
import net.neoforged.neoforge.event.server.ServerStoppingEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import net.neoforged.neoforge.event.village.WandererTradesEvent;
import net.neoforged.neoforge.registries.RegisterEvent;
import net.woukie.createmissiles.client.FlashHandler;
import net.woukie.createmissiles.client.screens.AssemblyPanelScreen;
import net.woukie.createmissiles.client.screens.ControlPanelScreen;
import net.woukie.createmissiles.client.screens.DroneScreen;
import net.woukie.createmissiles.client.screens.NavigationPanelScreen;
import net.woukie.createmissiles.entity.drone.Drone;
import net.woukie.createmissiles.entity.drone.DroneHandler;
import net.woukie.createmissiles.entity.drone.MapUtils;
import net.woukie.createmissiles.item.BiomeVialItem;
import net.woukie.createmissiles.item.assembly.AssemblyItem;
import net.woukie.createmissiles.missiles.Trajectories;
import net.woukie.createmissiles.missiles.asyncexplosionhandler.ExplosionHandler;
import net.woukie.createmissiles.particle.BuildShrapnel;
import net.woukie.createmissiles.registry.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CreateMissiles {
    public static final String MOD_ID = "createmissiles";
    public static final String NAME = "Create Missiles";
    public static final Logger LOGGER = LoggerFactory.getLogger(NAME);

    private static final CreateRegistrate REGISTRATE = CreateRegistrate.create(MOD_ID);

    public static void init() {
        LOGGER.info("Initializing!");

        CreateMissiles.registrate().registerEventListeners(NeoForge.EVENT_BUS);

        NeoForge.EVENT_BUS.addListener(CreateMissiles::onServerStarted);
        NeoForge.EVENT_BUS.addListener(CreateMissiles::onServerStopping);
        NeoForge.EVENT_BUS.addListener(CreateMissiles::onServerTick);
        NeoForge.EVENT_BUS.addListener(CreateMissiles::addWanderingTrades);
        NeoForge.EVENT_BUS.addListener(CreateMissiles::onLootTableLoad);
        NeoForge.EVENT_BUS.addListener(CreateMissiles::createDefaultAttributes);
        NeoForge.EVENT_BUS.addListener(CreateMissiles::onBuildCreativeModeTabContents);
        NeoForge.EVENT_BUS.addListener(EntityRenderers::registerEntityRenderers);
        NeoForge.EVENT_BUS.addListener(EntityRenderers::registerLayerDefinitions);
        NeoForge.EVENT_BUS.addListener(CreateMissiles::registerScreens);
        NeoForge.EVENT_BUS.addListener(CreateMissiles::clientSetup);
        NeoForge.EVENT_BUS.addListener(CreateMissiles::registerParticles);
        NeoForge.EVENT_BUS.addListener(CreateMissiles::onRegister);
        NeoForge.EVENT_BUS.addListener(CreateMissiles::onClientTickPost);
        NeoForge.EVENT_BUS.addListener(CreateMissiles::onRenderGuiEvent);
        NeoForge.EVENT_BUS.addListener(Packets::onRegisterPayloadHandlers);

        Blocks.init();
        BlockEntities.init();
        PartTypes.init();
        Items.init();
        CreativeMenus.init();
        Menus.init();
        RecipeSerializers.init();
        RecipeTypes.init();
        SpriteShifts.init();
        EntityTypes.init();
        SoundEvents.init();
        ParticleTypes.init();
        ExplosionResistanceOverrides.init();
    }

    public static void onLootTableLoad(LootTableLoadEvent event) {
        if (event.getName().equals(ResourceLocation.parse("minecraft:chests/abandoned_mineshaft"))) {
            LootPool.Builder poolBuilder = LootPool.lootPool();
            var warheadItem = LootItem.lootTableItem(Items.WARHEAD_ASSEMBLY.get());
            warheadItem.when(LootItemRandomChanceCondition.randomChance(0.1f));
            var data = new CompoundTag();
            data.putString("PartType", "createmissiles:excavator_warhead");
            warheadItem.apply(SetComponentsFunction.setComponent(DataComponents.CUSTOM_DATA, CustomData.of(data)));
            poolBuilder.add(warheadItem);
            event.getTable().addPool(poolBuilder.build());
        }

        if (event.getName().equals(ResourceLocation.parse("minecraft:entities/ender_dragon"))) {
            LootPool.Builder poolBuilder = LootPool.lootPool();
            var warheadItem = LootItem.lootTableItem(Items.WARHEAD_ASSEMBLY.get());
            var data = new CompoundTag();
            data.putString("PartType", "createmissiles:dragon_warhead");
            warheadItem.apply(SetComponentsFunction.setComponent(DataComponents.CUSTOM_DATA, CustomData.of(data)));
            poolBuilder.add(warheadItem);
            event.getTable().addPool(poolBuilder.build());
        }
    }

    private static void onServerStarted(ServerStartedEvent event) {
        var server = event.getServer();
        Trajectories.get().init(server);
        ExplosionHandler.get().init(server);
        DroneHandler.get().init(server);
    }

    private static void onServerStopping(ServerStoppingEvent event) {
        Trajectories.get().stop();
        ExplosionHandler.get().stop();
        DroneHandler.get().stop();
    }

    private static void onServerTick(ServerTickEvent event) {
        var server = event.getServer();
        Trajectories.get().serverTick(server);
        ExplosionHandler.get().serverTick(server);
        DroneHandler.get().serverTick(server);
        MapUtils.serverTick();
    }

    private static void addWanderingTrades(WandererTradesEvent event) {
        event.getGenericTrades().add(
                (entity, randomSource) -> new MerchantOffer(
                        new ItemCost(net.minecraft.world.item.Items.EMERALD),
                        AssemblyItem.createWith(ResourceLocation.fromNamespaceAndPath(CreateMissiles.MOD_ID, "annoying_warhead"), Items.WARHEAD_ASSEMBLY.get()),
                        1,
                        6,
                        4
                )
        );
    }

    private static void createDefaultAttributes(EntityAttributeCreationEvent event) {
        event.put(EntityTypes.BASIC_DRONE.get(), Drone.createMobAttributes().build());
        event.put(EntityTypes.REINFORCED_DRONE.get(), Drone.createMobAttributes().build());
    }

    private static void onBuildCreativeModeTabContents(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeMenus.ASSEMBLIES_TAB.getKey()) {
            CreativeMenus.addItems(event);
        }
    }

    public static CreateRegistrate registrate() {
        return REGISTRATE;
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

    public static void onRegister(RegisterEvent event) {
        ArmInteractionPointsForge.init();
    }

    public static void onClientTickPost(ClientTickEvent.Post event) {
        FlashHandler.cleanUp();
    }

    public static void onRenderGuiEvent(RenderGuiEvent event) {
        FlashHandler.handleHudRender(event.getGuiGraphics());
    }
}
