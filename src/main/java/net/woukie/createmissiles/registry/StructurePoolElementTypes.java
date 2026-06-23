package net.woukie.createmissiles.registry;

import com.mojang.serialization.Codec;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElementType;
import net.woukie.createmissiles.CreateMissiles;
import net.woukie.createmissiles.levelgen.structure.pools.LiquidlessSinglePoolElement;
import org.jetbrains.annotations.NotNull;

import static net.woukie.createmissiles.CreateMissiles.MOD_ID;

public class StructurePoolElementTypes {
    public static final DeferredRegister<StructurePoolElementType<?>> STRUCTURE_POOL_ELEMENT_TYPES =
            DeferredRegister.create(MOD_ID, Registries.STRUCTURE_POOL_ELEMENT);

    public static final RegistrySupplier<StructurePoolElementType<LiquidlessSinglePoolElement>> LIQUIDLESS_SINGLE_POOL_ELEMENT = STRUCTURE_POOL_ELEMENT_TYPES.register("liquidless_single_pool_element", LiquidlessStructurePoolElementType::new);

    @SuppressWarnings({"Experimental"})
    public static void init() {
        CreateMissiles.LOGGER.info("Registering structure pool element types for " + CreateMissiles.NAME);
        STRUCTURE_POOL_ELEMENT_TYPES.register();
    }

    public static class LiquidlessStructurePoolElementType implements StructurePoolElementType<LiquidlessSinglePoolElement> {
        @Override
        public @NotNull Codec<LiquidlessSinglePoolElement> codec() {
            return LiquidlessSinglePoolElement.CODEC;
        }
    }
}
