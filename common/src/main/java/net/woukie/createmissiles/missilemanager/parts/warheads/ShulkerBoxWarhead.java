package net.woukie.createmissiles.missilemanager.parts.warheads;

import net.minecraft.core.BlockPos;
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
import net.minecraft.world.level.block.ShulkerBoxBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ShulkerBoxBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;
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
    public float getWeight() {
        return 5;
    }

    @Override
    public CompoundTag saveTo(Container container, CompoundTag data) {
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
    public void onDetonate(Vec3 hitPosition, Trajectory trajectory, MinecraftServer server) {
        var level = server.getLevel(trajectory.getLevelKey());
        if (level == null) return;
        var impactPos = hitPosition.add(0, 1, 0);
        CompoundTag data = trajectory.getWarheadData();
        if (data != null && !data.isEmpty()) {
            ListTag boxes = data.getList("ShulkerBoxes", 10);

            if (!boxes.isEmpty()) {
                for (int i = 0; i < boxes.size(); ++i) {
                    CompoundTag tag = boxes.getCompound(i);
                    BlockState blockState = BuiltInRegistries.BLOCK.get(new ResourceLocation(tag.getString("id"))).defaultBlockState();

                    var impactBlock = new BlockPos((int)impactPos.x, (int)impactPos.y, (int)impactPos.z);
                    level.setBlock(impactBlock, blockState, 3);
                    level.gameEvent(null, GameEvent.BLOCK_PLACE, impactBlock);

                    if (tag.contains("tag") && tag.getCompound("tag").contains("BlockEntityTag")) {
                        BlockEntity entity = ShulkerBoxBlockEntity.loadStatic(impactBlock, blockState, tag.getCompound("tag").getCompound("BlockEntityTag"));
                        if (entity != null) {
                            level.setBlockEntity(entity);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void onLaunch(Trajectory trajectory, ServerLevel level) {
        var p = trajectory.getPosition();
        level.playSound(null, p.x, p.y, p.z, SoundEvents.FIREWORK_ROCKET_LAUNCH, SoundSource.NEUTRAL, 1, 1);
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

    @Override
    public void onTick(Trajectory trajectory, MinecraftServer server) {

    }
}
