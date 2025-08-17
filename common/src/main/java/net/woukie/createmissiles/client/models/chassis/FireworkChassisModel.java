package net.woukie.createmissiles.client.models.chassis;

import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.woukie.createmissiles.CreateMissiles;
import net.woukie.createmissiles.client.MissilePartModel;
import org.joml.Vector3f;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class FireworkChassisModel implements MissilePartModel {
	private final List<Map<String, Vector3f>> attachments = List.of(
			new HashMap<>() {{
				put("bottom", new Vector3f(0, 0, 0));
				put("top", new Vector3f(0, 0, 0));
			}},
			new HashMap<>() {{
				put("bottom", new Vector3f(0, 0, 0));
				put("top", new Vector3f(0, 4, 0));
			}},
			new HashMap<>() {{
				put("bottom", new Vector3f(0, 0, 0));
				put("top", new Vector3f(0, 7, 0));
			}},
			new HashMap<>() {{
				put("bottom", new Vector3f(0, 0, 0));
				put("top", new Vector3f(0, 10, 0));
			}},
			new HashMap<>() {{
				put("bottom", new Vector3f(0, 0, 0));
				put("top", new Vector3f(0, 13, 0));
			}},
			new HashMap<>() {{
				put("bottom", new Vector3f(0, 0, 0));
				put("top", new Vector3f(0, 16, 0));
			}}
	);

	private final List<Function<PartDefinition, PartDefinition>> layers = List.of(
			partDefinition -> partDefinition.addOrReplaceChild("bb_main", CubeListBuilder.create(), PartPose.ZERO),
			partDefinition -> partDefinition.addOrReplaceChild("bb_main", CubeListBuilder.create().texOffs(0, 0).addBox(-3.0F, 0.0F, -3.0F, 6.0F, 4.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F)),
			partDefinition -> partDefinition.addOrReplaceChild("bb_main", CubeListBuilder.create().texOffs(0, 0).addBox(-3.0F, 0.0F, -3.0F, 6.0F, 7.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F)),
			partDefinition -> partDefinition.addOrReplaceChild("bb_main", CubeListBuilder.create().texOffs(0, 0).addBox(-3.0F, 0.0F, -3.0F, 6.0F, 10.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F)),
			partDefinition -> partDefinition.addOrReplaceChild("bb_main", CubeListBuilder.create().texOffs(0, 0).addBox(-3.0F, 0.0F, -3.0F, 6.0F, 13.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F)),
			partDefinition -> partDefinition.addOrReplaceChild("bb_main", CubeListBuilder.create().texOffs(0, 0).addBox(-3.0F, 0.0F, -3.0F, 6.0F, 16.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F))
	);

	@Override
	public Map<String, Vector3f> getAttachements(int layer) {
		return attachments.get(layer);
	}

	@Override
	public LayerDefinition getLayerDefinition(int stage) {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();
		layers.get(stage).apply(partdefinition);
		return LayerDefinition.create(meshdefinition, 24, 22);
	}

	@Override
	public ResourceLocation getTexture(int stage) {
		return new ResourceLocation(CreateMissiles.MOD_ID,"textures/entity/firework_chassis.png");
	}

	@Override
	public int getStageCount() {
		return layers.size();
	}
}