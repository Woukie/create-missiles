package net.woukie.createmissiles;

import com.simibubi.create.AllShapes;
import com.simibubi.create.Create;
import com.simibubi.create.content.kinetics.mechanicalArm.ArmBlockEntity;
import com.simibubi.create.foundation.ponder.PonderPalette;
import com.simibubi.create.foundation.ponder.PonderRegistrationHelper;
import com.simibubi.create.foundation.ponder.Selection;
import com.simibubi.create.foundation.ponder.element.InputWindowElement;
import com.simibubi.create.foundation.utility.Pointing;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.woukie.createmissiles.item.assembly.AssemblyItem;
import net.woukie.createmissiles.registry.Blocks;

public class PonderIndex {
    static final PonderRegistrationHelper HELPER = new PonderRegistrationHelper(CreateMissiles.MOD_ID);

    public static void register() {
        HELPER.forComponents(Blocks.NAVIGATION_PANEL, Blocks.CONTROL_PANEL, Blocks.ASSEMBLY_PANEL, Blocks.LAUNCH_PAD)
                .addStoryBoard("missile_assembly", (scene, util) -> {
                    BlockPos launchPadClose = util.grid.at(2, 1, 2);
                    BlockPos launchPadFar = util.grid.at(4, 1, 4);
                    BlockPos launchPadMiddle = util.grid.at(3, 1, 4);
                    BlockPos launchPadArm = util.grid.at(2, 1, 3);
                    BlockPos powerClose = util.grid.at(5, 1, 3);
                    BlockPos powerFar = util.grid.at(6, 1, 4);
                    BlockPos powerSource = util.grid.at(7, 0, 3);
                    BlockPos navigationPanel = util.grid.at(4, 1, 1);
                    BlockPos controlPanel = util.grid.at(3, 1, 1);
                    BlockPos assemblyPanel = util.grid.at(2, 1, 1);
                    BlockPos arm = util.grid.at(1, 1, 3);
                    BlockPos depot = util.grid.at(0, 1, 3);

                    Selection launchPadSelection = util.select.fromTo(launchPadClose, launchPadFar);
                    Selection powerSelection = util.select.fromTo(powerClose, powerFar);
                    Selection powerSourceSelection = util.select.position(powerSource);
                    Selection navigationPanelSelection = util.select.position(navigationPanel);
                    Selection controlPanelSelection = util.select.position(controlPanel);
                    Selection assemblyPanelSelection = util.select.position(assemblyPanel);
                    Selection armSelection = util.select.fromTo(arm, depot);

                    scene.title("missile_assembly", "Building a Rocket");
                    scene.configureBasePlate(0, 0, 7);
                    scene.showBasePlate();

                    scene.idle(20);

                    scene.world.showSection(launchPadSelection, Direction.DOWN);
                    scene.world.showSection(powerSelection, Direction.DOWN);
                    scene.world.showSection(powerSourceSelection, Direction.WEST);
                    scene.idle(10);
                    scene.overlay.showSelectionWithText(launchPadSelection, 70)
                            .attachKeyFrame()
                            .colored(PonderPalette.RED)
                            .text("Launch pads must be placed in a 3x3 area and powered with a speed of at least 64")
                            .pointAt(launchPadMiddle.getCenter())
                            .placeNearTarget();
                    scene.idle(80);

                    scene.world.showIndependentSection(navigationPanelSelection, Direction.DOWN);
                    scene.idle(20);
                    scene.overlay.showControls(new InputWindowElement(navigationPanel.getCenter(), Pointing.RIGHT)
                            .withItem(Items.FILLED_MAP.getDefaultInstance()), 50);
                    scene.overlay.showText(70)
                            .attachKeyFrame()
                            .colored(PonderPalette.INPUT)
                            .text("Provide with a filled map and plot the missile target in this panel")
                            .pointAt(util.vector.blockSurface(navigationPanel, Direction.WEST))
                            .placeNearTarget();
                    scene.idle(80);

                    scene.world.showIndependentSection(controlPanelSelection, Direction.DOWN);
                    scene.idle(20);
                    scene.overlay.showText(70)
                            .attachKeyFrame()
                            .text("The Control Panel lets you view the status of the build and launch the rocket once built")
                            .pointAt(util.vector.blockSurface(controlPanel, Direction.WEST))
                            .placeNearTarget();
                    scene.idle(80);

                    scene.world.showIndependentSection(assemblyPanelSelection, Direction.DOWN);
                    scene.idle(20);
                    scene.overlay.showControls(new InputWindowElement(assemblyPanelSelection.getCenter(), Pointing.RIGHT)
                            .withItem(AssemblyItem.createWith(new ResourceLocation(CreateMissiles.MOD_ID, "firework_thruster"), net.woukie.createmissiles.registry.Items.THRUSTER_ASSEMBLY.get())), 50);
                    scene.overlay.showText(70)
                            .attachKeyFrame()
                            .colored(PonderPalette.INPUT)
                            .text("An Assembly Panel lets you supply three assemblies to specify a missile to build")
                            .pointAt(util.vector.blockSurface(assemblyPanel, Direction.WEST))
                            .placeNearTarget();
                    scene.idle(80);

                    scene.world.showSection(armSelection, Direction.DOWN);
                    scene.idle(20);
                    scene.overlay.chaseBoundingBoxOutline(PonderPalette.INPUT, new Object(), AllShapes.CASING_13PX.get(Direction.UP).bounds().move(depot), 70);
                    scene.overlay.chaseBoundingBoxOutline(PonderPalette.OUTPUT, new Object(), AllShapes.CASING_13PX.get(Direction.UP).bounds().move(launchPadArm), 70);
                    scene.overlay.showText(70)
                            .attachKeyFrame()
                            .text("Use arms to feed items into any slot of the launch pad")
                            .pointAt(util.vector.blockSurface(arm, Direction.WEST))
                            .placeNearTarget();
                    scene.idle(80);

//                    ItemStack paper = new ItemStack(Items.PAPER);
//                    scene.world.createItemOnBeltLike(depot, Direction.SOUTH, paper);
//                    scene.idle(10);
//                    scene.world.instructArm(arm, ArmBlockEntity.Phase.MOVE_TO_INPUT, ItemStack.EMPTY, 1);
//                    scene.idle(24);
//                    scene.world.removeItemsFromBelt(depot);
//                    scene.world.instructArm(arm, ArmBlockEntity.Phase.SEARCH_OUTPUTS, paper, -1);
//                    scene.idle(20);
//                    scene.world.instructArm(arm, ArmBlockEntity.Phase.MOVE_TO_OUTPUT, paper, 1);
//                    scene.idle(24);
//                    scene.world.instructArm(arm, ArmBlockEntity.Phase.SEARCH_INPUTS, ItemStack.EMPTY, -1);
//                    scene.idle(44);

                    scene.markAsFinished();
                });
    }
}
