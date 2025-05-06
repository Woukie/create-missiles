package net.woukie.createmissiles.block.launchpad;

import com.simibubi.create.content.kinetics.base.SingleRotatingInstance;
import net.minecraft.core.Direction;
import com.jozufozu.flywheel.api.Instancer;
import com.jozufozu.flywheel.api.MaterialManager;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.content.kinetics.base.flwdata.RotatingData;

public class LaunchPadCogInstance extends SingleRotatingInstance<KineticBlockEntity> {
    public LaunchPadCogInstance(MaterialManager materialManager, KineticBlockEntity blockEntity) {
        super(materialManager, blockEntity);
    }

    @Override
    protected Instancer<RotatingData> getModel() {
        return getRotatingMaterial().getModel(AllPartialModels.SHAFTLESS_COGWHEEL, blockState, Direction.NORTH);
    }
}
