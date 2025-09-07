package net.woukie.createmissiles;

import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import com.simibubi.create.Create;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.item.render.CustomRenderedItems;
import dev.architectury.event.events.common.LifecycleEvent;
import dev.architectury.event.events.common.LootEvent;
import dev.architectury.event.events.common.TickEvent;
import dev.architectury.registry.item.ItemPropertiesRegistry;
import dev.architectury.registry.level.entity.EntityAttributeRegistry;
import dev.architectury.registry.level.entity.trade.TradeRegistry;
import dev.architectury.registry.menu.MenuRegistry;
import dev.architectury.registry.registries.RegistrarManager;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetNbtFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.woukie.createmissiles.client.screens.AssemblyPanelScreen;
import net.woukie.createmissiles.client.screens.ControlPanelScreen;
import net.woukie.createmissiles.client.screens.DroneScreen;
import net.woukie.createmissiles.client.screens.NavigationPanelScreen;
import net.woukie.createmissiles.entity.drone.Drone;
import net.woukie.createmissiles.entity.drone.DroneHandler;
import net.woukie.createmissiles.item.BiomeVialItem;
import net.woukie.createmissiles.item.assembly.AssemblyItem;
import net.woukie.createmissiles.missilemanager.Trajectories;
import net.woukie.createmissiles.missilemanager.asyncexplosionhandler.ExplosionHandler;
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

        LifecycleEvent.SERVER_STARTED.register(server -> {
            Trajectories.get().init(server);
            ExplosionHandler.get().init(server);
            DroneHandler.get().init(server);
        });
        LifecycleEvent.SERVER_STOPPING.register(server -> {
            Trajectories.get().stop();
            ExplosionHandler.get().stop();
            DroneHandler.get().stop();
        });
        TickEvent.SERVER_PRE.register(server -> {
            Trajectories.get().serverTick(server);
            ExplosionHandler.get().serverTick(server);
            DroneHandler.get().serverTick(server);
        });

        TradeRegistry.registerTradeForWanderingTrader(true, (entity, randomSource) -> new MerchantOffer(
                net.minecraft.world.item.Items.EMERALD.getDefaultInstance(),
                AssemblyItem.createWith(new ResourceLocation(CreateMissiles.MOD_ID, "annoying_warhead"),
                        Items.WARHEAD_ASSEMBLY.get()),
                1,
                6,
                4
        ));

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

        EntityAttributeRegistry.register(EntityTypes.BASIC_DRONE, Drone::createMobAttributes);
        EntityAttributeRegistry.register(EntityTypes.REINFORCED_DRONE, Drone::createMobAttributes);
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

        ItemPropertiesRegistry.register(Items.BIOME_VIAL.get(), new ResourceLocation("full"), (itemStack, clientLevel, livingEntity, i) -> {
            if (livingEntity == null) return 0.0F;
            if (itemStack.getItem() instanceof BiomeVialItem item) {
                return item.isFull(itemStack) ? 1.0F : 0.0F;
            }
            return 0.0F;
        });

        PonderIndex.register();
    }
}
