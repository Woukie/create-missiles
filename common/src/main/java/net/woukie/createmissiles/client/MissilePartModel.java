package net.woukie.createmissiles.client;

import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.resources.ResourceLocation;
import org.joml.Vector3f;

import java.util.Map;

public interface MissilePartModel {
    Map<String, Vector3f> getAttachements(int stage);
    LayerDefinition getLayerDefinition(int stage);
    ResourceLocation getTexture(int stage);
    int getStageCount();

    default int getStage(int buildPercent) {
        int stage = (int) ((this.getStageCount() - 2) * (buildPercent / 100F)) + 1;
        if (buildPercent == 0) stage = 0;
        return  stage;
    }
}
