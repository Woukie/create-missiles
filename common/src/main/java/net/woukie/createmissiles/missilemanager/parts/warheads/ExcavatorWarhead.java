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
import net.woukie.createmissiles.client.models.warheads.ExcavatorWarheadModel;
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

public class ExcavatorWarhead extends WarheadType {
    private final MissilePartModel model = new ExcavatorWarheadModel();

    @Override
    public float getWeight() {
        return 10;
    }

    @Override
    public void onDetonate(Trajectory trajectory, MinecraftServer server) {
        var level = server.getLevel(trajectory.getLevelKey());
        if (level == null) return;
        Vector3d impactPos = trajectory.getPosition();

        level.explode(null, impactPos.x, impactPos.y, impactPos.z, 2, Level.ExplosionInteraction.BLOCK);

        CompoundTag explosions = trajectory.getWarheadData();

        if (explosions == null || explosions.isEmpty()) {
            var random = Random.from(new Random());
            level.addParticle(ParticleTypes.POOF, impactPos.x, impactPos.y, impactPos.z, random.nextGaussian() * 0.05, 0.005, random.nextGaussian() * 0.05);
        } else {
            List<ServerPlayer> players = new ArrayList<>();
            level.players().forEach(serverPlayer -> {
                BlockPos blockPos = serverPlayer.blockPosition();
                if (blockPos.closerToCenterThan(new Vec3(impactPos.x, impactPos.y, impactPos.z), 512.0)) {
                    players.add(serverPlayer);
                }
            });

            Vector3f vel = new Vector3f(0, 0, 0); // TODO: Replace with rocket velocity
            Vector3f impactPosFloat = new Vector3f((float) impactPos.x, (float) impactPos.y, (float) impactPos.z);
            Packets.EXPLODE_FIREWORK.sendToPlayers(players, new ExplodeFireworkMessage(impactPosFloat, vel, explosions));
        }
    }

    @Override
    public @NotNull MissilePartModel getModel() {
        return model;
    }

    @Override
    public CompoundTag saveTo(Container container, CompoundTag data) {
        ListTag explosions = new ListTag();

        for (int i = 0; i < container.getContainerSize(); i++) {
            ItemStack itemStack = container.getItem(i);
            if (itemStack.is(Items.FIREWORK_STAR) && itemStack.getTag() != null)
                explosions.add(itemStack.getTagElement("Explosion"));
        }
        data.put("Explosions", explosions);
        return data;
    }

    @Override
    public ResourceLocation getResourceLocation() {
        return new ResourceLocation(CreateMissiles.MOD_ID, "excavator_warhead");
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("warheads.createmissiles.excavator_warhead");
    }

    @Override
    public void onTick(Trajectory trajectory, MinecraftServer server) {
        ServerLevel level = server.getLevel(trajectory.getLevelKey());
//        if (level != null && trajectory.getTick() > 100) {
//            onDetonate(trajectory, server);
//            trajectory.setSpent(true);
//            return;
//        }

        super.onTick(trajectory, server);
    }
}
