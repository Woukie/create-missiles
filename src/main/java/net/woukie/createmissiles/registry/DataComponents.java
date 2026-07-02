package net.woukie.createmissiles.registry;

import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.util.ExtraCodecs;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.woukie.createmissiles.CreateMissiles;

import java.util.function.UnaryOperator;

public class DataComponents {
    public static final DeferredRegister.DataComponents DATA_COMPONENTS = DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, CreateMissiles.MOD_ID);

    public static final DataComponentType<String> PART_TYPE = register(
            "part_type",
            builder -> builder.persistent(ExtraCodecs.NON_EMPTY_STRING).networkSynchronized(ByteBufCodecs.STRING_UTF8)
    );

    private static <T> DataComponentType<T> register(String name, UnaryOperator<DataComponentType.Builder<T>> builder) {
        DataComponentType<T> type = builder.apply(DataComponentType.builder()).build();
        DATA_COMPONENTS.register(name, () -> type);
        return type;
    }

    public static void init() {
        CreateMissiles.LOGGER.info("Registering components for " + CreateMissiles.NAME);
        DATA_COMPONENTS.register(NeoForge.EVENT_BUS);
    }
}
