package net.woukie.createmissiles.registry;

import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.woukie.createmissiles.CreateMissiles;

public class ParticleTypes {
    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES =
            DeferredRegister.create(Registries.PARTICLE_TYPE, CreateMissiles.MOD_ID);

    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> BUILD_SHRAPNEL = PARTICLE_TYPES.register("build_shrapnel", SimpleParticleType::new);

    public static void init() {
        CreateMissiles.LOGGER.info("Registering particles for " + CreateMissiles.NAME);
        PARTICLE_TYPES.register(NeoForge.EVENT_BUS);
    }

    public static class SimpleParticleType extends net.minecraft.core.particles.SimpleParticleType {
        public SimpleParticleType() {
            super(true);
        }
    }
}
