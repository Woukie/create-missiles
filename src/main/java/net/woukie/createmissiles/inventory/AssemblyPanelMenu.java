package net.woukie.createmissiles.inventory;

import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.woukie.createmissiles.registry.Items;
import org.jetbrains.annotations.NotNull;

import static net.woukie.createmissiles.registry.Menus.ASSEMBLY_PANEL;

public class AssemblyPanelMenu extends AbstractBasicMenu {
    public AssemblyPanelMenu(int id, Inventory playerInventory, Container container) {
        super(ASSEMBLY_PANEL.get(), id, container);
        checkContainerSize(container, 3);

        this.addSlot(new Slot(container, 0, 80, 17) {
            public int getMaxStackSize() {
                return 1;
            }
            public boolean mayPlace(@NotNull ItemStack itemStack) {
                return itemStack.is(Items.WARHEAD_ASSEMBLY.get());
            }
        });

        this.addSlot(new Slot(container, 1, 61, 53) {
            public int getMaxStackSize() {
                return 1;
            }
            public boolean mayPlace(@NotNull ItemStack itemStack) {
                return itemStack.is(Items.CHASSIS_ASSEMBLY.get());
            }
        });

        this.addSlot(new Slot(container, 2, 99, 53) {
            public int getMaxStackSize() {
                return 1;
            }
            public boolean mayPlace(@NotNull ItemStack itemStack) {
                return itemStack.is(Items.THRUSTER_ASSEMBLY.get());
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

    public AssemblyPanelMenu(int id, Inventory inventory) {
        this(id, inventory, new SimpleContainer(3));
    }
}
