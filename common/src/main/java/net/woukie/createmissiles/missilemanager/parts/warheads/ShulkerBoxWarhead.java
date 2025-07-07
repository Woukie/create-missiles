package net.woukie.createmissiles.missilemanager.parts.warheads;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.ShulkerBoxBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ShulkerBoxBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.woukie.createmissiles.CreateMissiles;
import net.woukie.createmissiles.client.MissilePartModel;
import net.woukie.createmissiles.client.models.warheads.ShulkerBoxWarheadModel;
import net.woukie.createmissiles.missilemanager.Trajectory;
import net.woukie.createmissiles.missilemanager.parts.WarheadType;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class ShulkerBoxWarhead extends WarheadType {
    private final MissilePartModel model = new ShulkerBoxWarheadModel();

    @Override
    public int getWeight() {
        return 5;
    }

    @Override
    public CompoundTag writeData(Container container, CompoundTag data) {
        ListTag boxes = new ListTag();

        for (int i = getStartSlot(); i < getEndSlot(); i++) {
            ItemStack stack = container.getItem(i);
            boolean isShulker = stack.is(Items.SHULKER_BOX) || Arrays.stream(DyeColor.values()).anyMatch(c -> stack.is(ShulkerBoxBlock.getBlockByColor(c).asItem()));
            if (isShulker)
                boxes.add(stack.save(new CompoundTag()));
        }
        data.put("ShulkerBoxes", boxes);
        return data;
    }

    @Override
    public void onDetonate(Trajectory trajectory, MinecraftServer server) {
        var level = (ServerLevel) trajectory.getData().level;
        if (level == null) return;
        var impactPos = trajectory.getData().target;

        level.explode(null, impactPos.getX(), impactPos.getY(), impactPos.getZ(), 1, Level.ExplosionInteraction.BLOCK);

        CompoundTag data = trajectory.getData().warheadData;
        if (data != null && !data.isEmpty()) {
            ListTag boxes = data.getList("ShulkerBoxes", 10);

            if (!boxes.isEmpty()) {
                for (int i = 0; i < boxes.size(); ++i) {
                    CompoundTag tag = boxes.getCompound(i);
                    BlockState blockState = BuiltInRegistries.BLOCK.get(new ResourceLocation(tag.getString("id"))).defaultBlockState();

                    level.setBlock(impactPos, blockState, 3);
                    level.gameEvent(null, GameEvent.BLOCK_PLACE, impactPos);

                    if (tag.contains("tag") && tag.getCompound("tag").contains("BlockEntityTag")) {
                        BlockEntity entity = ShulkerBoxBlockEntity.loadStatic(impactPos, blockState, tag.getCompound("tag").getCompound("BlockEntityTag"));
                        if (entity != null) {
                            level.setBlockEntity(entity);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void onLaunch(Trajectory trajectory) {
        var level = (ServerLevel) trajectory.getData().level;
        if (level != null) {
            var p = trajectory.getPosition(0);
            level.playSound(null, p.x, p.y, p.z, SoundEvents.FIREWORK_ROCKET_LAUNCH, SoundSource.NEUTRAL, 1, 1);
        }
    }

    @Override
    public @NotNull MissilePartModel getModel() {
        return model;
    }

    @Override
    public ResourceLocation getResourceLocation() {
        return new ResourceLocation(CreateMissiles.MOD_ID, "shulker_box_warhead");
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("warheads.createmissiles.shulker_box_warhead");
    }
}
