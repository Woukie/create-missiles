package net.woukie.createmissiles.missilemanager.parts.warheads;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.Container;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import net.woukie.createmissiles.CreateMissiles;
import net.woukie.createmissiles.client.MissilePartModel;
import net.woukie.createmissiles.client.models.warheads.DragonWarheadModel;
import net.woukie.createmissiles.missilemanager.Trajectory;
import net.woukie.createmissiles.missilemanager.asyncexplosionhandler.ExplosionHandler;
import net.woukie.createmissiles.missilemanager.asyncexplosionhandler.Explosion;
import net.woukie.createmissiles.missilemanager.parts.WarheadType;
import org.jetbrains.annotations.NotNull;

public class DragonWarhead extends WarheadType {
    private final MissilePartModel model = new DragonWarheadModel();

    @Override
    public float getMass() {
        return 50;
    }

    @Override
    public void onDetonate(Vec3 hitPosition, Trajectory trajectory, MinecraftServer server) {
        var level = server.getLevel(trajectory.getLevelKey());
        if (level == null) return;
        ExplosionHandler.get().createExplosion(new Explosion(level, hitPosition, 90));
        level.setBlock(BlockPos.containing(hitPosition), Blocks.DRAGON_EGG.defaultBlockState(), 3);
    }

    @Override
    public @NotNull MissilePartModel getModel() {
        return model;
    }

    @Override
    public CompoundTag saveTo(Container container, CompoundTag data) {
        return data;
    }

    @Override
    public ResourceLocation getResourceLocation() {
        return new ResourceLocation(CreateMissiles.MOD_ID, "dragon_warhead");
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("warheads.createmissiles.dragon_warhead");
    }
}
