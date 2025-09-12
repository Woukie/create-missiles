package net.woukie.createmissiles.missiles.parts.warheads;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.woukie.createmissiles.CreateMissiles;
import net.woukie.createmissiles.client.MissilePartModel;
import net.woukie.createmissiles.client.models.warheads.TeleportationWarheadModel;
import net.woukie.createmissiles.missiles.Trajectory;
import net.woukie.createmissiles.missiles.parts.WarheadType;
import net.woukie.createmissiles.registry.Items;
import org.jetbrains.annotations.NotNull;

public class TeleportationWarhead extends WarheadType {
    @Override
    public CompoundTag saveTo(Container container, CompoundTag data) {
        for (int i = getStartSlot(); i < getEndSlot(); i++) {
            ItemStack stack = container.getItem(i);
            if (stack.is(Items.BOUND_ENDER_PEARL.get())) {
                CompoundTag tag = stack.getTag();
                if (tag != null && tag.hasUUID("PlayerUUID")) {
                    data.putUUID("PlayerUUID", tag.getUUID("PlayerUUID"));
                }
                return data;
            }
        }

        return data;
    }

    @Override
    public float getMass() {
        return 10;
    }

    @Override
    public void onDetonate(Vec3 hitPosition, Trajectory trajectory, MinecraftServer server) {
        var level = server.getLevel(trajectory.getLevelKey());
        if (level == null) return;
        var impactPos = hitPosition.add(0, 1, 0);

        CompoundTag data = trajectory.getWarheadData();
        if (data != null && !data.isEmpty()) {
            ServerPlayer serverPlayer = (ServerPlayer) level.getPlayerByUUID(data.getUUID("PlayerUUID"));
            if (serverPlayer != null && serverPlayer.connection.isAcceptingMessages()) {
                if (serverPlayer.isPassenger()) {
                    serverPlayer.dismountTo(impactPos.x(), impactPos.y(), impactPos.z());
                } else {
                    serverPlayer.teleportTo(impactPos.x(), impactPos.y(), impactPos.z());
                }

                level.playSound(null, BlockPos.containing(hitPosition), SoundEvents.GENERIC_EXPLODE, SoundSource.HOSTILE);
                level.playSound(null, impactPos.x(), impactPos.y(), impactPos.z(), SoundEvents.ENDERMAN_TELEPORT, SoundSource.NEUTRAL, 1, 1);

                serverPlayer.resetFallDistance();
                serverPlayer.hurt(serverPlayer.damageSources().fall(), 5.0F);
            }
        }
    }

    @Override
    public ResourceLocation getResourceLocation() {
        return new ResourceLocation(CreateMissiles.MOD_ID, "teleportation_warhead");
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("warheads.createmissiles.teleportation_warhead");
    }
}
