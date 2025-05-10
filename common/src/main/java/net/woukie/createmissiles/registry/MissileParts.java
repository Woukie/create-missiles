package net.woukie.createmissiles.registry;

import net.woukie.createmissiles.CreateMissiles;
import net.woukie.createmissiles.missilemanager.parts.chassis.ChassisRegistry;
import net.woukie.createmissiles.missilemanager.parts.chassis.PaperChassis;
import net.woukie.createmissiles.missilemanager.parts.thrusters.GunpowderThruster;
import net.woukie.createmissiles.missilemanager.parts.thrusters.ThrusterRegistry;
import net.woukie.createmissiles.missilemanager.parts.warheads.TntWarhead;
import net.woukie.createmissiles.missilemanager.parts.warheads.WarheadRegistry;

public class MissileParts {
    public static void init() {
        CreateMissiles.LOGGER.info("Registering missile parts for " + CreateMissiles.NAME);

        WarheadRegistry.register(TntWarhead::new);
        ChassisRegistry.register(PaperChassis::new);
        ThrusterRegistry.register(GunpowderThruster::new);
    }
}
