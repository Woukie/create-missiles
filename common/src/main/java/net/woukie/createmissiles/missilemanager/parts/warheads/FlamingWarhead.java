package net.woukie.createmissiles.missilemanager.parts.warheads;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.woukie.createmissiles.CreateMissiles;
import net.woukie.createmissiles.client.MissilePartModel;
import net.woukie.createmissiles.client.models.warheads.FireworkWarheadModel;
import net.woukie.createmissiles.client.models.warheads.FlamingWarheadModel;
import net.woukie.createmissiles.missilemanager.Trajectory;
import net.woukie.createmissiles.missilemanager.parts.WarheadType;
import net.woukie.createmissiles.missilemanager.parts.warheads.messages.ExplodeFireworkMessage;
import net.woukie.createmissiles.registry.Packets;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3d;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class FlamingWarhead extends WarheadType {
    private final MissilePartModel model = new FlamingWarheadModel();

    @Override
    public float getWeight() {
        return 20;
    }

    @Override
    public void onDetonate(Trajectory trajectory, MinecraftServer server) {
        var level = server.getLevel(trajectory.getLevelKey());
        if (level == null) return;
        Vector3d impactPos = trajectory.getPosition();
        level.explode(null, impactPos.x, impactPos.y, impactPos.z, 5, Level.ExplosionInteraction.BLOCK);
        var random = Random.from(new Random());
        level.addParticle(ParticleTypes.FLAME, impactPos.x, impactPos.y, impactPos.z, random.nextGaussian() * 0.05, 0.005, random.nextGaussian() * 0.05);
    }

    @Override
    public @NotNull MissilePartModel getModel() {
        return model;
    }

    @Override
    public ResourceLocation getResourceLocation() {
        return new ResourceLocation(CreateMissiles.MOD_ID, "flaming_warhead");
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("warheads.createmissiles.flaming_warhead");
    }

    @Override
    public void onTick(Trajectory trajectory, MinecraftServer server) {
        ServerLevel level = server.getLevel(trajectory.getLevelKey());
        if (level != null && trajectory.getTick() > 100) {
            onDetonate(trajectory, server);
            trajectory.setSpent(true);
            return;
        }

        super.onTick(trajectory, server);
    }
}
