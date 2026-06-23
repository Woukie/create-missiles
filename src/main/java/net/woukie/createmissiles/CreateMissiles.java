package net.woukie.createmissiles;

import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.item.render.CustomRenderedItems;
import net.createmod.ponder.foundation.PonderIndex;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.trading.MerchantOffer;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartedEvent;
import net.neoforged.neoforge.event.server.ServerStoppingEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import net.neoforged.neoforge.event.village.WandererTradesEvent;
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
import net.woukie.createmissiles.registry.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CreateMissiles {
    public static final String MOD_ID = "createmissiles";
    public static final String NAME = "Create Missiles";
    public static final Logger LOGGER = LoggerFactory.getLogger(NAME);

    private static final CreateRegistrate REGISTRATE = CreateRegistrate.create(MOD_ID);
//    public static final Supplier<RegistrarManager> MANAGER = Suppliers.memoize(() -> RegistrarManager.get(MOD_ID));

    public static void init() {
        LOGGER.info("Initializing!");

        NeoForge.EVENT_BUS.addListener(CreateMissiles::onServerStarted);
        NeoForge.EVENT_BUS.addListener(CreateMissiles::onServerStopping);
        NeoForge.EVENT_BUS.addListener(CreateMissiles::onServerTick);
        NeoForge.EVENT_BUS.addListener(CreateMissiles::addWanderingTrades);

        LootEvent.MODIFY_LOOT_TABLE.register((lootDataManager, id, context, builtin) -> {
            if (!builtin) return;
            if (id.equals(new ResourceLocation("minecraft:chests/abandoned_mineshaft"))) {
                LootPool.Builder poolBuilder = LootPool.lootPool();
                var warheadItem = LootItem.lootTableItem(Items.WARHEAD_ASSEMBLY.get());
                warheadItem.when(LootItemRandomChanceCondition.randomChance(0.1f));
                var data = new CompoundTag();
                data.putString("PartType", "createmissiles:excavator_warhead");
                warheadItem.apply(SetNbtFunction.setTag(data));

                poolBuilder.add(warheadItem);

                context.addPool(poolBuilder);
            }

            if (id.equals(new ResourceLocation("minecraft:entities/ender_dragon"))) {
                LootPool.Builder poolBuilder = LootPool.lootPool();
                var warheadItem = LootItem.lootTableItem(Items.WARHEAD_ASSEMBLY.get());
                var data = new CompoundTag();
                data.putString("PartType", "createmissiles:dragon_warhead");
                warheadItem.apply(SetNbtFunction.setTag(data));
                poolBuilder.add(warheadItem);
                context.addPool(poolBuilder);
            }
        });

        StructurePoolElementTypes.init();
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
        ExplosionResistanceOverrides.init();

        EntityAttributeRegistry.register(EntityTypes.BASIC_DRONE, Drone::createMobAttributes);
        EntityAttributeRegistry.register(EntityTypes.REINFORCED_DRONE, Drone::createMobAttributes);
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
                        net.minecraft.world.item.Items.EMERALD.getDefaultInstance(),
                        AssemblyItem.createWith(ResourceLocation.fromNamespaceAndPath(CreateMissiles.MOD_ID, "annoying_warhead"), Items.WARHEAD_ASSEMBLY.get()),
                        1,
                        6,
                        4
                )
        );
    }

    public static CreateRegistrate registrate() {
        return REGISTRATE;
    }

    public static void initClient() {
        MenuRegistry.registerScreenFactory(Menus.CONTROL_PANEL.get(), ControlPanelScreen::new);
        MenuRegistry.registerScreenFactory(Menus.NAVIGATION_PANEL.get(), NavigationPanelScreen::new);
        MenuRegistry.registerScreenFactory(Menus.ASSEMBLY_PANEL.get(), AssemblyPanelScreen::new);
        MenuRegistry.registerScreenFactory(Menus.DRONE.get(), DroneScreen::new);

        CustomRenderedItems.register(Items.WARHEAD_ASSEMBLY.get());
        CustomRenderedItems.register(Items.CHASSIS_ASSEMBLY.get());
        CustomRenderedItems.register(Items.THRUSTER_ASSEMBLY.get());

        ClientGuiEvent.RENDER_HUD.register((guiGraphics, v) -> {
            FlashHandler.handleHudRender(guiGraphics);
        });

        ClientTickEvent.CLIENT_POST.register(instance -> {
            FlashHandler.cleanUp();
        });

        ItemPropertiesRegistry.register(Items.BIOME_VIAL.get(), new ResourceLocation("full"), (itemStack, clientLevel, livingEntity, i) -> {
            if (livingEntity == null) return 0.0F;
            if (itemStack.getItem() instanceof BiomeVialItem item) {
                return item.isFull(itemStack) ? 1.0F : 0.0F;
            }
            return 0.0F;
        });

        PartModels.init();
        PonderIndex.addPlugin(new PonderPlugin());
    }
}
