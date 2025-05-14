package net.woukie.createmissiles.block.schematicator;

import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.woukie.createmissiles.block.MissileAbstractMenu;
import net.woukie.createmissiles.registry.MissileItems;
import org.jetbrains.annotations.NotNull;

import static net.woukie.createmissiles.registry.MissileMenus.SCHEMATICATOR;

public class SchematicatorMenu extends MissileAbstractMenu {
    public SchematicatorMenu(int id, Inventory playerInventory, Container container) {
        super(SCHEMATICATOR.get(), id, container);
        checkContainerSize(container, 3);

        this.addSlot(new Slot(container, 0, 80, 17) {
            public boolean mayPlace(@NotNull ItemStack itemStack) {
                return itemStack.is(MissileItems.WARHEAD_SCHEMATIC.get());
            }
        });

        this.addSlot(new Slot(container, 1, 61, 53) {
            public boolean mayPlace(@NotNull ItemStack itemStack) {
                return itemStack.is(MissileItems.CHASSIS_SCHEMATIC.get());
            }
        });

        this.addSlot(new Slot(container, 2, 99, 53) {
            public boolean mayPlace(@NotNull ItemStack itemStack) {
                return itemStack.is(MissileItems.THRUSTER_SCHEMATIC.get());
            }
        });

        for(int j = 0; j < 3; ++j) {
            for(int k = 0; k < 9; ++k) {
                this.addSlot(new Slot(playerInventory, k + j * 9 + 9, 8 + k * 18, 84 + j * 18));
            }
        }

        for(int j = 0; j < 9; ++j) {
            this.addSlot(new Slot(playerInventory, j, 8 + j * 18, 142));
        }
    }

    public SchematicatorMenu(int id, Inventory inventory) {
        this(id, inventory, new SimpleContainer(3));
    }
}
