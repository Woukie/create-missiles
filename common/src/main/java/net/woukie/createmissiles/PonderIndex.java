package net.woukie.createmissiles;

import com.simibubi.create.foundation.ponder.PonderRegistrationHelper;
import com.simibubi.create.infrastructure.ponder.AllPonderTags;
import com.simibubi.create.infrastructure.ponder.scenes.KineticsScenes;
import net.woukie.createmissiles.registry.Blocks;

public class PonderIndex {
    static final PonderRegistrationHelper HELPER = new PonderRegistrationHelper(CreateMissiles.MOD_ID);

    public static void register() {
        HELPER.forComponents(Blocks.NAVIGATION_PANEL, Blocks.CONTROL_PANEL, Blocks.ASSEMBLY_PANEL, Blocks.LAUNCH_PAD)
                .addStoryBoard("missile_assembly", KineticsScenes::shaftAsRelay, AllPonderTags.KINETIC_RELAYS);
    }
}
