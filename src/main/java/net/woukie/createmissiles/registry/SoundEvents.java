package net.woukie.createmissiles.registry;


import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.woukie.createmissiles.CreateMissiles;

import static net.woukie.createmissiles.CreateMissiles.MOD_ID;

public class SoundEvents {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(Registries.SOUND_EVENT, MOD_ID);

    public static final DeferredHolder<SoundEvent, SoundEvent> DING = register("ding") ;
    public static final DeferredHolder<SoundEvent, SoundEvent> BUILD = register("build") ;
    public static final DeferredHolder<SoundEvent, SoundEvent> BUILD_SPECIAL = register("build_special") ;
    public static final DeferredHolder<SoundEvent, SoundEvent> BUTTON = register("button") ;
    public static final DeferredHolder<SoundEvent, SoundEvent> CRUNCH = register("crunch") ;

    public static void init() {
        CreateMissiles.LOGGER.info("Registering sound events for " + CreateMissiles.NAME);
        SOUND_EVENTS.register(NeoForge.EVENT_BUS);
    }

    private static DeferredHolder<SoundEvent, SoundEvent> register(String name) {
        return SOUND_EVENTS.register(
                name,
                () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(MOD_ID, name))
        );
    }
}
