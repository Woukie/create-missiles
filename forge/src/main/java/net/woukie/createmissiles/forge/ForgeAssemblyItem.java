package net.woukie.createmissiles.forge;

import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.common.extensions.IForgeItem;
import net.woukie.createmissiles.client.AssemblyRenderer;
import net.woukie.createmissiles.item.assembly.AssemblyItem;

import java.util.function.Consumer;

public class ForgeAssemblyItem extends AssemblyItem implements IForgeItem {
    public ForgeAssemblyItem(Properties properties) {
        super(properties);
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                return new AssemblyRenderer();
            }
        });
    }
}
