package net.woukie.createmissiles.registry;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.woukie.createmissiles.CreateMissiles;
import net.woukie.createmissiles.entity.MissileEntity;

public class EntityTypes {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(CreateMissiles.MOD_ID, Registries.ENTITY_TYPE);

    public static final RegistrySupplier<EntityType<MissileEntity>> MISSILE = ENTITY_TYPES.register(
            "rocket_entity",
            () -> EntityType.Builder.of(MissileEntity::new, MobCategory.MISC).noSummon().fireImmune().sized(0.5F, 0.5F).clientTrackingRange(4).updateInterval(20).build(new ResourceLocation(CreateMissiles.MOD_ID, "rocket_entity").toString())
    );

    public static void init() {
        CreateMissiles.LOGGER.info("Registering entity types for " + CreateMissiles.NAME);
        ENTITY_TYPES.register();
    }
}
