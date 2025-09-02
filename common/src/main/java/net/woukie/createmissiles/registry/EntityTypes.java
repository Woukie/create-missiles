package net.woukie.createmissiles.registry;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.woukie.createmissiles.CreateMissiles;
import net.woukie.createmissiles.entity.*;

public class EntityTypes {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(CreateMissiles.MOD_ID, Registries.ENTITY_TYPE);

    public static final RegistrySupplier<EntityType<MissileEntity>> MISSILE = ENTITY_TYPES.register(
            "missile_entity",
            () -> EntityType.Builder.of(MissileEntity::new, MobCategory.MISC).noSummon().fireImmune().sized(1.5F, 5F).clientTrackingRange(32).updateInterval(20).build(new ResourceLocation(CreateMissiles.MOD_ID, "missile_entity").toString())
    );
    public static final RegistrySupplier<EntityType<FlamingballEntity>> FLAMINGBALL = ENTITY_TYPES.register(
            "flamingball",
            () -> EntityType.Builder.of(FlamingballEntity::new, MobCategory.MISC).sized(0.5f, 0.5f).build(new ResourceLocation(CreateMissiles.MOD_ID, "flamingball").toString())
    );
    public static final RegistrySupplier<EntityType<BlazingballEntity>> BLAZINGBALL = ENTITY_TYPES.register(
            "blazingball",
            () -> EntityType.Builder.of(BlazingballEntity::new, MobCategory.MISC).sized(0.5f, 0.5f).build(new ResourceLocation(CreateMissiles.MOD_ID, "blazingball").toString())
    );
    public static final RegistrySupplier<EntityType<InfernalballEntity>> INFERNALBALL = ENTITY_TYPES.register(
            "infernalball",
            () -> EntityType.Builder.of(InfernalballEntity::new, MobCategory.MISC).sized(0.5f, 0.5f).build(new ResourceLocation(CreateMissiles.MOD_ID, "infernalball").toString())
    );
    public static final RegistrySupplier<EntityType<FrostballEntity>> FROSTBALL = ENTITY_TYPES.register(
            "frostball",
            () -> EntityType.Builder.of(FrostballEntity::new, MobCategory.MISC).sized(0.5f, 0.5f).build(new ResourceLocation(CreateMissiles.MOD_ID, "frostball").toString())
    );
    public static final RegistrySupplier<EntityType<FrostAreaEntity>> FROST_AREA = ENTITY_TYPES.register(
            "frost_area",
            () -> EntityType.Builder.of(FrostAreaEntity::new, MobCategory.MISC).sized(6.0F, 1.2F).fireImmune().clientTrackingRange(10).build(new ResourceLocation(CreateMissiles.MOD_ID, "frost_area").toString())
    );
    public static final RegistrySupplier<EntityType<DroneEntity>> BASIC_DRONE = ENTITY_TYPES.register(
            "basic_drone",
            () -> EntityType.Builder.of(DroneEntity::new, MobCategory.MISC).noSummon().fireImmune().sized(2f, 1f).clientTrackingRange(4).updateInterval(20).build(new ResourceLocation(CreateMissiles.MOD_ID, "basic_drone").toString())
    );

    public static void init() {
        CreateMissiles.LOGGER.info("Registering entity types for " + CreateMissiles.NAME);
        ENTITY_TYPES.register();
    }
}
