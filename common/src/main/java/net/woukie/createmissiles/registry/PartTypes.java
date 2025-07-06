package net.woukie.createmissiles.registry;

import dev.architectury.registry.CreativeTabRegistry;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.woukie.createmissiles.CreateMissiles;
import net.woukie.createmissiles.client.models.chassis.FireworkChassisModel;
import net.woukie.createmissiles.client.models.thrusters.FireworkThrusterModel;
import net.woukie.createmissiles.client.models.warheads.FireworkWarheadModel;
import net.woukie.createmissiles.item.assembly.AssemblyItem;
import net.woukie.createmissiles.missilemanager.parts.ChassisType;
import net.woukie.createmissiles.missilemanager.parts.MissilePartType;
import net.woukie.createmissiles.missilemanager.parts.ThrusterType;
import net.woukie.createmissiles.missilemanager.parts.WarheadType;

import java.util.HashMap;
import java.util.Random;

import static net.woukie.createmissiles.registry.Items.*;

public class PartTypes {
    private static final HashMap<ResourceLocation, MissilePartType> missilePartTypes = new HashMap<>();

    public static void register(MissilePartType missileType) {
        PartTypes.missilePartTypes.put(missileType.resourceLocation, missileType);
    }

    public static MissilePartType get(ResourceLocation location) {
        return missilePartTypes.get(location);
    }

    public static MissilePartType get(ItemStack itemStack) {
        if (itemStack == null || itemStack.isEmpty()) return null;
        CompoundTag compoundTag = itemStack.getTag();
        if (compoundTag == null) return null;
        return PartTypes.get(new ResourceLocation(compoundTag.getString("PartType")));
    }

    public static void init() {
        CreateMissiles.LOGGER.info("Registering missile part types for " + CreateMissiles.NAME);

//        Warheads

        PartTypes.register(new WarheadType(
                Component.translatable("warheads.createmissiles.firework_warhead"),
                id("firework_warhead"),
                (container, data) -> {
                    ListTag fireworks = new ListTag();

                    for (int i = 0; i < container.getContainerSize(); i++) {
                        ItemStack itemStack = container.getItem(i);
                        if (itemStack.is(Items.FIREWORK_STAR)) {
                            CompoundTag fireworkData = itemStack.getTagElement("Fireworks");
                            if (fireworkData != null) {
                                fireworks.add(fireworkData);
                            }
                        }
                    }
                    data.put("FireworksList", fireworks);
                    return data;
                },
                10,
                (trajectory, server) -> {
                    var level = trajectory.getData().level;
                    var impactPos = trajectory.getPosition((float)trajectory.getImpactTime());
                    level.explode(null, impactPos.x, impactPos.y, impactPos.z, 2, Level.ExplosionInteraction.BLOCK);

                    ListTag fireworks = (ListTag) trajectory.getData().warheadData.get("FireworksList");

                    if (fireworks == null || fireworks.isEmpty()) {
                        var random = Random.from(new Random());
                        level.addParticle(ParticleTypes.POOF, impactPos.x, impactPos.y, impactPos.z, random.nextGaussian() * 0.05, 0.005, random.nextGaussian() * 0.05);
                    } else {
                        fireworks.forEach(tag -> {
                            Vec3 vel = Vec3.ZERO; // TODO: Replace with rocket velocity
                            level.createFireworks(impactPos.x, impactPos.y, impactPos.z, vel.x, vel.y, vel.z, (CompoundTag) tag);
                        });
                    }
                },
                new FireworkWarheadModel()
        ));

//        Chassis

        PartTypes.register(new ChassisType(
                Component.translatable("chassis.createmissiles.firework_chassis"),
                id("firework_chassis"),
                null,
                1,
                new FireworkChassisModel()
        ));

//        Thrusters

        PartTypes.register(new ThrusterType(
                Component.translatable("thrusters.createmissiles.firework_thruster"),
                id("firework_thruster"),
                null,
                1,
                10,
                new FireworkThrusterModel()
        ));

        CreativeTabRegistry.appendStack(CreativeMenus.ASSEMBLIES_TAB, () -> AssemblyItem.createWith(id("firework_warhead"), WARHEAD_ASSEMBLY.get()));

        CreativeTabRegistry.appendStack(CreativeMenus.ASSEMBLIES_TAB, () -> AssemblyItem.createWith(id("firework_chassis"), CHASSIS_ASSEMBLY.get()));

        CreativeTabRegistry.appendStack(CreativeMenus.ASSEMBLIES_TAB, () -> AssemblyItem.createWith(id("firework_thruster"), THRUSTER_ASSEMBLY.get()));
    }

    private static ResourceLocation id(String id) {
        return new ResourceLocation(CreateMissiles.MOD_ID, id);
    }

}
