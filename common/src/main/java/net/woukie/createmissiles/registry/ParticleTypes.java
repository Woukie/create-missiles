package net.woukie.createmissiles.registry;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.Registries;
import net.woukie.createmissiles.CreateMissiles;

import static net.woukie.createmissiles.CreateMissiles.MOD_ID;

public class ParticleTypes {
    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES =
            DeferredRegister.create(MOD_ID, Registries.PARTICLE_TYPE);

    public static final RegistrySupplier<SimpleParticleType> WELD_SPARK = PARTICLE_TYPES.register("weld_spark", SimpleParticleType::new);

    public static void init() {
        CreateMissiles.LOGGER.info("Registering particles for " + CreateMissiles.NAME);
        PARTICLE_TYPES.register();
    }

    public static class SimpleParticleType extends net.minecraft.core.particles.SimpleParticleType {
        public SimpleParticleType() {
            super(true);
        }
    }
}
