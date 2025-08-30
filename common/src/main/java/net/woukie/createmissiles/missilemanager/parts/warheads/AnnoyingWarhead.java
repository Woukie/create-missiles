package net.woukie.createmissiles.missilemanager.parts.warheads;

import net.minecraft.core.Direction;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.phys.Vec3;
import net.woukie.createmissiles.CreateMissiles;
import net.woukie.createmissiles.client.MissilePartModel;
import net.woukie.createmissiles.client.models.warheads.AnnoyingWarheadModel;
import net.woukie.createmissiles.missilemanager.Trajectory;
import net.woukie.createmissiles.missilemanager.asyncexplosionhandler.Explosion;
import net.woukie.createmissiles.missilemanager.asyncexplosionhandler.ExplosionHandler;
import net.woukie.createmissiles.missilemanager.parts.WarheadType;
import net.woukie.createmissiles.registry.Blocks;
import org.jetbrains.annotations.NotNull;

import static net.woukie.createmissiles.Util.locateAir;

public class AnnoyingWarhead extends WarheadType {
    private final MissilePartModel model = new AnnoyingWarheadModel();

    @Override
    public float getWeight() {
        return 5;
    }

    @Override
    public void onDetonate(Vec3 hitPosition, Trajectory trajectory, MinecraftServer server) {
        var level = server.getLevel(trajectory.getLevelKey());
        if (level == null) return;
        var emptyBlock = locateAir(hitPosition.add(0, 1, 0), level, 100);
        if (emptyBlock != null) {
            level.setBlock(emptyBlock, Blocks.ANNOYING_JUKEBOX.get().defaultBlockState(), 3);
        } else {
            DefaultDispenseItemBehavior.spawnItem(level, Blocks.ANNOYING_JUKEBOX.get().asItem().getDefaultInstance(), 1, Direction.UP, hitPosition.add(0, 1, 0));
        }

        ExplosionHandler.get().createExplosion(new Explosion(level, hitPosition, 5));
    }

    @Override
    public @NotNull MissilePartModel getModel() {
        return model;
    }

    @Override
    public ResourceLocation getResourceLocation() {
        return new ResourceLocation(CreateMissiles.MOD_ID, "annoying_warhead");
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("warheads.createmissiles.annoying_warhead");
    }
}
