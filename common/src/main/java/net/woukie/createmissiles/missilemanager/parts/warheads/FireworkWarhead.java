package net.woukie.createmissiles.missilemanager.parts.warheads;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.woukie.createmissiles.CreateMissiles;
import net.woukie.createmissiles.client.MissilePartModel;
import net.woukie.createmissiles.client.models.warheads.FireworkWarheadModel;
import net.woukie.createmissiles.missilemanager.Trajectory;
import net.woukie.createmissiles.missilemanager.parts.WarheadType;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class FireworkWarhead extends WarheadType {
    private final MissilePartModel model = new FireworkWarheadModel();

    @Override
    public int getWeight() {
        return 10;
    }

    @Override
    public void onDetonate(Trajectory trajectory, MinecraftServer server) {
        var level = (ServerLevel) trajectory.getData().level;
        if (level == null) return;
        var impactPos = trajectory.getPosition((float)trajectory.getImpactTime());

        level.explode(null, impactPos.x, impactPos.y, impactPos.z, 2, Level.ExplosionInteraction.BLOCK);

        ListTag fireworks = (ListTag) trajectory.getData().warheadData.get("Fireworks");

        if (fireworks == null || fireworks.isEmpty()) {
            var random = Random.from(new Random());
            level.addParticle(ParticleTypes.POOF, impactPos.x, impactPos.y, impactPos.z, random.nextGaussian() * 0.05, 0.005, random.nextGaussian() * 0.05);
        } else {
            fireworks.forEach(tag -> {
                System.out.println(tag);
                Vec3 vel = Vec3.ZERO; // TODO: Replace with rocket velocity
                level.createFireworks(impactPos.x, impactPos.y, impactPos.z, vel.x, vel.y, vel.z, (CompoundTag) tag);
            });
        }
    }

    @Override
    public @NotNull MissilePartModel getModel() {
        return model;
    }

    @Override
    public CompoundTag writeData(Container container, CompoundTag data) {
        ListTag fireworks = new ListTag();

        for (int i = 0; i < container.getContainerSize(); i++) {
            ItemStack itemStack = container.getItem(i);
            if (itemStack.is(Items.FIREWORK_STAR) && itemStack.getTag() != null)
                fireworks.add(itemStack.getTag());
        }
        data.put("Fireworks", fireworks);
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
