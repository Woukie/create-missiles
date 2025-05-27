package net.woukie.createmissiles.registry;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.woukie.createmissiles.CreateMissiles;
import net.woukie.createmissiles.entity.custom.DroneEntity;

public class MissilesEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPE = DeferredRegister.create(CreateMissiles.MOD_ID, Registries.ENTITY_TYPE);
    public static final RegistrySupplier<EntityType<DroneEntity>> DRONE_ENTITY = ENTITY_TYPE.register("drone", () -> EntityType.Builder.of(DroneEntity::new, MobCategory.MISC).sized(1f, 2f).build("drone"));

    public static void init() {
        CreateMissiles.LOGGER.info("Registering entities for " + CreateMissiles.NAME);

        ENTITY_TYPE.register();
    }
}
