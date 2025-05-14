package net.woukie.createmissiles.block.navigator;

import net.minecraft.core.BlockPos;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.woukie.createmissiles.block.MissileAbstractMenu;
import org.jetbrains.annotations.Nullable;

public class NavigatorMenu extends MissileAbstractMenu {
    private final ContainerData containerData;
    private final Container schematicatorContainer;

    protected NavigatorMenu(@Nullable MenuType<?> menuType, int id, Container container, ContainerData containerData, Container schematicatorContainer) {
        super(menuType, id, container);
        this.schematicatorContainer = schematicatorContainer;
        this.containerData = containerData;

        checkContainerSize(schematicatorContainer, 3);
        checkContainerDataCount(containerData, 10);
    }

    public int getMapCrosshairX() {
        return containerData.get(0);
    }

    public int getMapCrosshairZ() {
        return containerData.get(1);
    }

    public BlockPos getTarget() {
        if (containerData.get(2) == 0) {
            return null;
        }

        return new BlockPos(
                containerData.get(3),
                containerData.get(4),
                containerData.get(5)
        );
    }

    public BlockPos getSource() {
        return new BlockPos(
                containerData.get(6),
                containerData.get(7),
                containerData.get(8)
        );
    }

    public double getFuelPercent() {
        return ((double) containerData.get(9)) / 100.0D;
    }

    public ItemStack getMap() {
        return getSlot(0).getItem();
    }

    public Container getSchematicatorContainer() {
        return schematicatorContainer;
    }
}
