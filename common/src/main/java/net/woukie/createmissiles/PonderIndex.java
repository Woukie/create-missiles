package net.woukie.createmissiles;

import com.simibubi.create.foundation.ponder.PonderRegistrationHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.woukie.createmissiles.registry.Blocks;

public class PonderIndex {
    static final PonderRegistrationHelper HELPER = new PonderRegistrationHelper(CreateMissiles.MOD_ID);

    public static void register() {
        HELPER.forComponents(Blocks.NAVIGATION_PANEL, Blocks.CONTROL_PANEL, Blocks.ASSEMBLY_PANEL, Blocks.LAUNCH_PAD)
                .addStoryBoard("missile_assembly", (scene, util) -> {
                    scene.title("missile_assembly", "Building a Rocket");
                    scene.configureBasePlate(0, 0, 7);

                    scene.world.setKineticSpeed(util.select.everywhere(), 64);
                    scene.world.showSection(util.select.layer(0), Direction.UP);
                    scene.world.showSection(util.select.layer(2), Direction.UP);
                    scene.idle(5);

                    BlockPos launchPadClose = util.grid.at(2, 1, 2);
                    BlockPos launchPadFar = util.grid.at(4, 1, 4);
                    scene.world.showSection(util.select.fromTo(launchPadClose, launchPadFar), Direction.DOWN);

                    scene.idle(5);

                    BlockPos powerClose = util.grid.at(5, 1, 3);
                    BlockPos powerFar = util.grid.at(6, 1, 4);
                    scene.world.showSection(util.select.fromTo(powerClose, powerFar), Direction.DOWN);

                    BlockPos launchPadMiddle = util.grid.at(3, 1, 4);
                    scene.idle(20);
                    scene.overlay.showText(70)
                            .text("Place launch pads in a 3x3 area")
                            .placeNearTarget()
                            .pointAt(launchPadMiddle.getCenter());
                    scene.idle(70);

                    BlockPos navigationPanel = util.grid.at(4, 1, 1);
                    scene.world.showIndependentSection(util.select.position(navigationPanel), Direction.DOWN);
                    scene.idle(20);
                    scene.overlay.showText(70)
                            .text("Provide a filled map to plot a target")
                            .placeNearTarget()
                            .pointAt(navigationPanel.getCenter());
                    scene.idle(70);

                    BlockPos controlPanel = util.grid.at(3, 1, 1);
                    scene.world.showIndependentSection(util.select.position(controlPanel), Direction.DOWN);
                    scene.idle(20);
                    scene.overlay.showText(70)
                            .text("Displays the build status of the missile")
                            .placeNearTarget()
                            .pointAt(controlPanel.getCenter());
                    scene.idle(70);

                    BlockPos assemblyPanel = util.grid.at(2, 1, 1);
                    scene.world.showIndependentSection(util.select.position(assemblyPanel), Direction.DOWN);
                    scene.idle(20);
                    scene.overlay.showText(70)
                            .text("Supply with three assemblies to specify a missile")
                            .placeNearTarget()
                            .pointAt(assemblyPanel.getCenter());
                    scene.idle(70);



                    BlockPos arm = util.grid.at(1, 1, 3);
                    BlockPos depot = util.grid.at(0, 1, 3);
                    scene.world.showSection(util.select.fromTo(depot, arm), Direction.DOWN);
                    scene.idle(20);
                    scene.overlay.showText(70)
                            .text("Arms feed items into any slot of the launch pad")
                            .placeNearTarget()
                            .pointAt(arm.getCenter());

                    scene.idle(70);

                    scene.markAsFinished();
                });
    }
}
