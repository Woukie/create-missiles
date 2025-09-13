package net.woukie.createmissiles.missiles.parts.warheads;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.Vec3;
import net.woukie.createmissiles.CreateMissiles;
import net.woukie.createmissiles.missiles.Trajectory;
import net.woukie.createmissiles.missiles.asyncexplosionhandler.Explosion;
import net.woukie.createmissiles.missiles.asyncexplosionhandler.ExplosionHandler;
import net.woukie.createmissiles.missiles.parts.WarheadType;
import net.woukie.createmissiles.missiles.parts.warheads.messages.ExplodeFireworkMessage;
import net.woukie.createmissiles.registry.Packets;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

public class FireworkWarhead extends WarheadType {
    @Override
    public float getMass() {
        return 5;
    }

    @Override
    public void onDetonate(Vec3 hitPosition, Trajectory trajectory, MinecraftServer server) {
        var level = server.getLevel(trajectory.getLevelKey());
        if (level == null) return;

        level.playSound(null, BlockPos.containing(hitPosition), SoundEvents.GENERIC_EXPLODE, SoundSource.HOSTILE, 10, 1);
        ExplosionHandler.get().createExplosion(new Explosion(level, hitPosition, 4));

        CompoundTag explosions = trajectory.getWarheadData();
        if (explosions == null || explosions.isEmpty()) {
            level.addParticle(ParticleTypes.POOF, hitPosition.x, hitPosition.y, hitPosition.z, Math.random() * 0.05, 0.005, Math.random() * 0.05);
        } else {
            List<ServerPlayer> players = new ArrayList<>();
            level.players().forEach(serverPlayer -> {
                BlockPos blockPos = serverPlayer.blockPosition();
                if (blockPos.closerToCenterThan(new Vec3(hitPosition.x, hitPosition.y, hitPosition.z), 512.0)) {
                    players.add(serverPlayer);
                }
            });

            Vector3f vel = new Vector3f(0, 0, 0); // TODO: Replace with rocket velocity
            Vector3f impactPosFloat = new Vector3f((float) hitPosition.x, (float) hitPosition.y, (float) hitPosition.z);
            Packets.EXPLODE_FIREWORK.sendToPlayers(players, new ExplodeFireworkMessage(impactPosFloat, vel, explosions));
        }
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
        return new ResourceLocation(CreateMissiles.MOD_ID, "firework_warhead");
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("warheads.createmissiles.firework_warhead");
    }
}
