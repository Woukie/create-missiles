package net.woukie.createmissiles.missilemanager.parts.warheads;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.woukie.createmissiles.CreateMissiles;
import net.woukie.createmissiles.client.MissilePartModel;
import net.woukie.createmissiles.client.models.warheads.TeleportationWarheadModel;
import net.woukie.createmissiles.missilemanager.Trajectory;
import net.woukie.createmissiles.missilemanager.parts.WarheadType;
import net.woukie.createmissiles.registry.Items;
import org.jetbrains.annotations.NotNull;

public class TeleportationWarhead extends WarheadType {
    private final MissilePartModel model = new TeleportationWarheadModel();

    @Override
    public int getWeight() {
        return 1;
    }

    @Override
    public CompoundTag writeData(Container container, CompoundTag data) {

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
    public void onDetonate(Trajectory trajectory, MinecraftServer server) {
        var level = (ServerLevel) trajectory.getData().level;
        if (level == null) return;
        var impactPos = trajectory.getData().target;

        CompoundTag data = trajectory.getData().warheadData;
        if (data != null && !data.isEmpty()) {
            ServerPlayer serverPlayer = (ServerPlayer) level.getPlayerByUUID(data.getUUID("PlayerUUID"));
            if (serverPlayer != null && serverPlayer.connection.isAcceptingMessages()) {
                if (serverPlayer.isPassenger()) {
                    serverPlayer.dismountTo(impactPos.getX(), impactPos.getY(), impactPos.getZ());
                } else {
                    serverPlayer.teleportTo(impactPos.getX(), impactPos.getY(), impactPos.getZ());
                }

                level.playSound(null, impactPos.getX(), impactPos.getY(), impactPos.getZ(), SoundEvents.ENDERMAN_TELEPORT, SoundSource.NEUTRAL, 1, 1);

                serverPlayer.resetFallDistance();
                serverPlayer.hurt(serverPlayer.damageSources().fall(), 5.0F);
            }
        }
    }

    @Override
    public @NotNull MissilePartModel getModel() {
        return model;
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
