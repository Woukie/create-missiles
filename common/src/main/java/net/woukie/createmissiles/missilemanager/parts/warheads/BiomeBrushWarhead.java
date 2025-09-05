package net.woukie.createmissiles.missilemanager.parts.warheads;

import net.minecraft.core.*;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeResolver;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkStatus;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.phys.Vec3;
import net.woukie.createmissiles.CreateMissiles;
import net.woukie.createmissiles.client.MissilePartModel;
import net.woukie.createmissiles.client.models.warheads.FlamingWarheadModel;
import net.woukie.createmissiles.item.BiomeVialItem;
import net.woukie.createmissiles.missilemanager.Trajectory;
import net.woukie.createmissiles.missilemanager.parts.WarheadType;
import net.woukie.createmissiles.registry.Items;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

// A lot of cross-over with net.minecraft.server.commands.FillBiomeCommand
public class BiomeBrushWarhead extends WarheadType {
    private final MissilePartModel model = new FlamingWarheadModel();
    private final int radius = 16;

    @Override
    public float getWeight() {
        return 20;
    }

    @Override
    public void onDetonate(Vec3 hitPosition, Trajectory trajectory, MinecraftServer server) {
        var level = server.getLevel(trajectory.getLevelKey());
        if (level == null) return;

        Vec3i center = new Vec3i((int)hitPosition.x, (int)hitPosition.y, (int)hitPosition.z);
        BoundingBox boundingBox = BoundingBox.fromCorners(center.offset(radius, radius, radius), center.offset(-radius, -radius, -radius));

        List<ChunkAccess> list = new ArrayList<>();
        for(int k = SectionPos.blockToSectionCoord(boundingBox.minZ()); k <= SectionPos.blockToSectionCoord(boundingBox.maxZ()); ++k) {
            for(int l = SectionPos.blockToSectionCoord(boundingBox.minX()); l <= SectionPos.blockToSectionCoord(boundingBox.maxX()); ++l) {
                ChunkAccess chunkAccess = level.getChunk(l, k, ChunkStatus.FULL, false);
                if (chunkAccess == null)
                    return;

                list.add(chunkAccess);
            }
        }

        ResourceLocation reference = new ResourceLocation(trajectory.getWarheadData().getString("biome"));
        var registryOptional = level.registryAccess().registry(Registries.BIOME);
        if (registryOptional.isEmpty()) return;
        var biomeHolderOptional = registryOptional.get().getHolder(ResourceKey.create(Registries.BIOME, reference));
        if (biomeHolderOptional.isEmpty()) return;

        for(ChunkAccess chunkAccess : list) {
            chunkAccess.fillBiomesFromNoise(makeResolver(chunkAccess, boundingBox, biomeHolderOptional.get(), (holder) -> true), level.getChunkSource().randomState().sampler());
            chunkAccess.setUnsaved(true);
        }

        level.getChunkSource().chunkMap.resendBiomesForChunks(list);

        level.playSound(null, BlockPos.containing(hitPosition), SoundEvents.GENERIC_EXPLODE, SoundSource.NEUTRAL);
        level.playSound(null, BlockPos.containing(hitPosition), SoundEvents.FIREWORK_ROCKET_TWINKLE, SoundSource.NEUTRAL);

        for (int i = 0; i < 20; i++) {
            Vec3 pos = new Vec3(
                    Math.random() * radius * 2,
                    Math.random() * radius * 2,
                    Math.random() * radius * 2
            ).add(hitPosition).subtract(radius, radius, radius);

            level.sendParticles(ParticleTypes.TOTEM_OF_UNDYING, pos.x, pos.y, pos.z, 3, 0, 0, 0, 2);
        }
    }

    private static BiomeResolver makeResolver(ChunkAccess chunkAccess, BoundingBox boundingBox, Holder<Biome> holder, Predicate<Holder<Biome>> predicate) {
        return (i, j, k, sampler) -> {
            int l = QuartPos.toBlock(i);
            int m = QuartPos.toBlock(j);
            int n = QuartPos.toBlock(k);
            Holder<Biome> holder2 = chunkAccess.getNoiseBiome(i, j, k);
            if (boundingBox.isInside(l, m, n) && predicate.test(holder2)) {
                return holder;
            } else {
                return holder2;
            }
        };
    }

    @Override
    public CompoundTag saveTo(Container container, CompoundTag data) {
        for (int i = 0; i < container.getContainerSize(); i++) {
            ItemStack itemStack = container.getItem(i);
            if (itemStack.is(Items.BIOME_VIAL.get())) {
                BiomeVialItem item = (BiomeVialItem) itemStack.getItem();
                if (item.isFull(itemStack)) {
                    data.putString("biome", itemStack.getTag().getString("biome"));
                    return data;
                }
            }
        }

        return super.saveTo(container, data);
    }

    @Override
    public @NotNull MissilePartModel getModel() {
        return model;
    }

    @Override
    public ResourceLocation getResourceLocation() {
        return new ResourceLocation(CreateMissiles.MOD_ID, "biome_brush_warhead");
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("warheads.createmissiles.biome_brush_warhead");
    }
}
