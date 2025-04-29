package net.woukie.createmissiles;

import dev.architectury.registry.CreativeTabRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public final class CreateMissiles {
    public static final String MOD_ID = "create_missiles";

    public static final DeferredRegister<CreativeModeTab> TABS =
            DeferredRegister.create(MOD_ID, Registries.CREATIVE_MODE_TAB);

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(MOD_ID, Registries.ITEM);

    public static final RegistrySupplier<CreativeModeTab> MY_TAB = TABS.register(
            "test_tab",
            () -> CreativeTabRegistry.create(
                    Component.translatable("category.architectury_test"),
                    () -> new ItemStack(Items.GLOW_ITEM_FRAME)
            )
            );

    public static final RegistrySupplier<Item> EXAMPLE_ITEM = ITEMS.register("example_item", () -> new Item(new Item.Properties().arch$tab(MY_TAB)));

    public static void init() {
        System.out.println("Create Missiles");

        ITEMS.register();
        TABS.register();
    }
}
