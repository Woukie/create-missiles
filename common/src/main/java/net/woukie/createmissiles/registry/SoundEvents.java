package net.woukie.createmissiles.registry;


import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.woukie.createmissiles.CreateMissiles;

import static net.woukie.createmissiles.CreateMissiles.MOD_ID;

public class SoundEvents {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS =
            DeferredRegister.create(MOD_ID, Registries.SOUND_EVENT);

    public static final RegistrySupplier<SoundEvent> DING = register("ding") ;
    public static final RegistrySupplier<SoundEvent> BUILD = register("build") ;
    public static final RegistrySupplier<SoundEvent> BUILD_SPECIAL = register("build_special") ;
    public static final RegistrySupplier<SoundEvent> BUTTON = register("button") ;
    public static final RegistrySupplier<SoundEvent> CRUNCH = register("crunch") ;

    public static void init() {
        CreateMissiles.LOGGER.info("Registering custom recipe types for " + CreateMissiles.NAME);
        SOUND_EVENTS.register();
    }

    private static RegistrySupplier<SoundEvent> register(String name) {
        ResourceLocation location = new ResourceLocation(MOD_ID, name);
        return SOUND_EVENTS.register(
                location,
                () -> SoundEvent.createVariableRangeEvent(location)
        );
    }
}
