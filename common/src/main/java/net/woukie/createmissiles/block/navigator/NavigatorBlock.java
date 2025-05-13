package net.woukie.createmissiles.block.navigator;

import com.simibubi.create.foundation.block.IBE;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.woukie.createmissiles.block.controller.ControllerBlockEntity;

public class NavigatorBlock extends HorizontalDirectionalBlock implements IBE<ControllerBlockEntity> {
    protected NavigatorBlock(Properties properties) {
        super(properties);
    }

    @Override
    public Class<ControllerBlockEntity> getBlockEntityClass() {
        return null;
    }

    @Override
    public BlockEntityType<? extends ControllerBlockEntity> getBlockEntityType() {
        return null;
    }
}
