package net.woukie.createmissiles.missilemanager.parts.warheads;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.DirectionalPlaceContext;
import net.minecraft.world.level.block.ShulkerBoxBlock;
import net.minecraft.world.phys.Vec3;
import net.woukie.createmissiles.CreateMissiles;
import net.woukie.createmissiles.client.MissilePartModel;
import net.woukie.createmissiles.client.models.warheads.ShulkerBoxWarheadModel;
import net.woukie.createmissiles.missilemanager.Trajectory;
import net.woukie.createmissiles.missilemanager.parts.WarheadType;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Function;

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
        CompoundTag data = trajectory.getWarheadData();
        if (data != null && !data.isEmpty()) {
            ListTag boxes = data.getList("ShulkerBoxes", 10);

            if (!boxes.isEmpty()) {
                for (int i = 0; i < boxes.size(); ++i) {
                    var itemStack = ItemStack.of(boxes.getCompound(i));
                    var emptyBlock = locateShulkerPlacement(hitPosition.add(0, 1, 0), level);
                    if (emptyBlock != null) {
                        var success = ((BlockItem)(itemStack.getItem())).place(new DirectionalPlaceContext(level, emptyBlock, Direction.UP, itemStack, Direction.UP)).consumesAction();
                        if (success) continue;
                    }
                    DefaultDispenseItemBehavior.spawnItem(level, itemStack, 1, Direction.UP, hitPosition);
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

    public static BlockPos locateShulkerPlacement(Vec3 origin, ServerLevel level) {
        return locateNearestMatchingBlock(origin, blockPos -> level.isEmptyBlock(blockPos) && !level.isEmptyBlock(blockPos.relative(Direction.DOWN)), 100);
    }

    public static BlockPos locateNearestMatchingBlock(Vec3 origin, Function<BlockPos, Boolean> condition, int limit) {
        class Neighbor {
            public final BlockPos position;
            public final double distance;

            public Neighbor(BlockPos position, double distance) {
                this.position = position;
                this.distance = distance;
            }
        }

        var invalidNeighbors = new ArrayList<BlockPos>();
        var neighbors = new PriorityQueue<Neighbor>(Comparator.comparingDouble(neighbour -> neighbour.distance));
        neighbors.add(new Neighbor(new BlockPos((int) origin.x, (int) origin.y, (int) origin.z), 0));

        int steps = 0;
        while (!neighbors.isEmpty() && steps < limit) {
            Neighbor neighbor = neighbors.poll();
            if (condition.apply(neighbor.position)) return neighbor.position;
            invalidNeighbors.add(neighbor.position);
            for (Direction dir : Direction.values()) {
                var newPos = neighbor.position.offset(dir.getStepX(), dir.getStepY(), dir.getStepZ());
                if (!invalidNeighbors.contains(newPos))
                    neighbors.add(new Neighbor(newPos, newPos.getCenter().distanceTo(origin)));
            }
            steps ++;
        }
        return null;
    }
}
