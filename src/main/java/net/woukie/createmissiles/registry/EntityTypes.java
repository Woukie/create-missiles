package net.woukie.createmissiles.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.woukie.createmissiles.CreateMissiles;
import net.woukie.createmissiles.entity.*;
import net.woukie.createmissiles.entity.drone.Drone;
import net.woukie.createmissiles.entity.drone.ReinforcedDrone;

public class EntityTypes {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(Registries.ENTITY_TYPE, CreateMissiles.MOD_ID);

    public static final DeferredHolder<EntityType<?>, EntityType<MissileEntity>> MISSILE = ENTITY_TYPES.register(
            "missile_entity",
            () -> EntityType.Builder.of(MissileEntity::new, MobCategory.MISC).noSummon().fireImmune().sized(1.5F, 5F).clientTrackingRange(32).build(ResourceLocation.fromNamespaceAndPath(CreateMissiles.MOD_ID, "missile_entity").toString())
    );
    public static final DeferredHolder<EntityType<?>, EntityType<FlamingballEntity>> FLAMINGBALL = ENTITY_TYPES.register(
            "flamingball",
            () -> EntityType.Builder.of(FlamingballEntity::new, MobCategory.MISC).sized(0.5f, 0.5f).build(ResourceLocation.fromNamespaceAndPath(CreateMissiles.MOD_ID, "flamingball").toString())
    );
    public static final DeferredHolder<EntityType<?>, EntityType<BlazingballEntity>> BLAZINGBALL = ENTITY_TYPES.register(
            "blazingball",
            () -> EntityType.Builder.of(BlazingballEntity::new, MobCategory.MISC).sized(0.5f, 0.5f).build(ResourceLocation.fromNamespaceAndPath(CreateMissiles.MOD_ID, "blazingball").toString())
    );
    public static final DeferredHolder<EntityType<?>, EntityType<InfernalballEntity>> INFERNALBALL = ENTITY_TYPES.register(
            "infernalball",
            () -> EntityType.Builder.of(InfernalballEntity::new, MobCategory.MISC).sized(0.5f, 0.5f).build(ResourceLocation.fromNamespaceAndPath(CreateMissiles.MOD_ID, "infernalball").toString())
    );
    public static final DeferredHolder<EntityType<?>, EntityType<FrostballEntity>> FROSTBALL = ENTITY_TYPES.register(
            "frostball",
            () -> EntityType.Builder.of(FrostballEntity::new, MobCategory.MISC).sized(0.5f, 0.5f).build(ResourceLocation.fromNamespaceAndPath(CreateMissiles.MOD_ID, "frostball").toString())
    );
    public static final DeferredHolder<EntityType<?>, EntityType<FrozenballEntity>> FROZENBALL = ENTITY_TYPES.register(
            "frozenball",
            () -> EntityType.Builder.of(FrozenballEntity::new, MobCategory.MISC).sized(0.5f, 0.5f).build(ResourceLocation.fromNamespaceAndPath(CreateMissiles.MOD_ID, "frozenball").toString())
    );
    public static final DeferredHolder<EntityType<?>, EntityType<GuardianballEntity>> GUARDIANBALL = ENTITY_TYPES.register(
            "guardianball",
            () -> EntityType.Builder.of(GuardianballEntity::new, MobCategory.MISC).sized(0.5f, 0.5f).build(ResourceLocation.fromNamespaceAndPath(CreateMissiles.MOD_ID, "guardianball").toString())
    );
    public static final DeferredHolder<EntityType<?>, EntityType<AncientballEntity>> ANCIENTBALL = ENTITY_TYPES.register(
            "ancientball",
            () -> EntityType.Builder.of(AncientballEntity::new, MobCategory.MISC).sized(0.5f, 0.5f).build(ResourceLocation.fromNamespaceAndPath(CreateMissiles.MOD_ID, "ancientball").toString())
    );
    public static final DeferredHolder<EntityType<?>, EntityType<WitheredballEntity>> WITHEREDBALL = ENTITY_TYPES.register(
            "witheredball",
            () -> EntityType.Builder.of(WitheredballEntity::new, MobCategory.MISC).sized(0.5f, 0.5f).build(ResourceLocation.fromNamespaceAndPath(CreateMissiles.MOD_ID, "witheredball").toString())
    );
    public static final DeferredHolder<EntityType<?>, EntityType<FrozenAreaEntity>> FROZEN_AREA = ENTITY_TYPES.register(
            "frozen_area",
            () -> EntityType.Builder.of(FrozenAreaEntity::new, MobCategory.MISC).sized(6.0F, 1.2F).fireImmune().build(ResourceLocation.fromNamespaceAndPath(CreateMissiles.MOD_ID, "frozen_area").toString())
    );
    public static final DeferredHolder<EntityType<?>, EntityType<InfernalAreaEntity>> INFERNAL_AREA = ENTITY_TYPES.register(
            "infernal_area",
            () -> EntityType.Builder.of(InfernalAreaEntity::new, MobCategory.MISC).sized(6.0F, 1.2F).fireImmune().build(ResourceLocation.fromNamespaceAndPath(CreateMissiles.MOD_ID, "infernal_area").toString())
    );
    public static final DeferredHolder<EntityType<?>, EntityType<MessyEntity>> MESSY = ENTITY_TYPES.register(
            "messy",
            () -> EntityType.Builder.of(MessyEntity::new, MobCategory.MISC).sized(1.2F, 1.2F).fireImmune().build(ResourceLocation.fromNamespaceAndPath(CreateMissiles.MOD_ID, "messy").toString())
    );
    public static final DeferredHolder<EntityType<?>, EntityType<Drone>> BASIC_DRONE = ENTITY_TYPES.register(
            "basic_drone",
            () -> EntityType.Builder.of(Drone::new, MobCategory.MISC).sized(0.9F, 0.5F).clientTrackingRange(8).build(ResourceLocation.fromNamespaceAndPath(CreateMissiles.MOD_ID, "basic_drone").toString())
    );
    public static final DeferredHolder<EntityType<?>, EntityType<ReinforcedDrone>> REINFORCED_DRONE = ENTITY_TYPES.register(
            "reinforced_drone",
            () -> EntityType.Builder.of(ReinforcedDrone::new, MobCategory.MISC).sized(0.9F, 0.5F).clientTrackingRange(8).fireImmune().build(ResourceLocation.fromNamespaceAndPath(CreateMissiles.MOD_ID, "reinforced_drone").toString())
    );

    public static void init() {
        CreateMissiles.LOGGER.info("Registering entity types for " + CreateMissiles.NAME);
        ENTITY_TYPES.register(NeoForge.EVENT_BUS);
    }
}
