package net.woukie.createmissiles.client.models.warheads;

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

public class DirectHitWarheadModel implements MissilePartModel {
	private final List<Map<String, Vector3f>> attachments = List.of(
			new HashMap<>() {{
				put("bottom", new Vector3f(0, 0, 0));
				put("top", new Vector3f(0, 0, 0));
			}},
			new HashMap<>() {{
				put("bottom", new Vector3f(0, 0, 0));
				put("top", new Vector3f(0, 2, 0));
			}},
			new HashMap<>() {{
				put("bottom", new Vector3f(0, 0, 0));
				put("top", new Vector3f(0, 3, 0));
			}},
			new HashMap<>() {{
				put("bottom", new Vector3f(0, 0, 0));
				put("top", new Vector3f(0, 5, 0));
			}},
			new HashMap<>() {{
				put("bottom", new Vector3f(0, 0, 0));
				put("top", new Vector3f(0, 9, 0));
			}}
	);

	private final List<Function<PartDefinition, PartDefinition>> layers = List.of(
			partDefinition -> partDefinition.addOrReplaceChild("bb_main", CubeListBuilder.create(), PartPose.ZERO),
			partDefinition -> {
				partDefinition.addOrReplaceChild("bb_main", CubeListBuilder.create().texOffs(0, 16).addBox(-4.0F, 0.0F, -4.0F, 8.0F, 2.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
				return partDefinition;
			},
			partDefinition -> {
				partDefinition.addOrReplaceChild("bb_main", CubeListBuilder.create().texOffs(0, 16).addBox(-4.0F, 0.0F, -4.0F, 8.0F, 2.0F, 8.0F, new CubeDeformation(0.0F))
						.texOffs(0, 26).addBox(-3.0F, 2.0F, -3.5F, 6.0F, 1.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
				return partDefinition;
			},
			partDefinition -> {
				partDefinition.addOrReplaceChild("bb_main", CubeListBuilder.create().texOffs(0, 16).addBox(-4.0F, 0.0F, -4.0F, 8.0F, 2.0F, 8.0F, new CubeDeformation(0.0F))
						.texOffs(0, 26).addBox(-3.0F, 2.0F, -3.5F, 6.0F, 1.0F, 7.0F, new CubeDeformation(0.0F))
						.texOffs(26, 26).addBox(-2.0F, 3.0F, -3.0F, 4.0F, 2.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
				return partDefinition;
			},
			partDefinition -> {
				partDefinition.addOrReplaceChild("bb_main", CubeListBuilder.create().texOffs(0, 16).addBox(-4.0F, 0.0F, -4.0F, 8.0F, 2.0F, 8.0F, new CubeDeformation(0.0F))
						.texOffs(0, 26).addBox(-3.0F, 2.0F, -3.5F, 6.0F, 1.0F, 7.0F, new CubeDeformation(0.0F))
						.texOffs(26, 26).addBox(-2.0F, 3.0F, -3.0F, 4.0F, 2.0F, 6.0F, new CubeDeformation(0.0F))
						.texOffs(0, 0).addBox(-3.5F, 5.0F, -6.0F, 7.0F, 4.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
				return partDefinition;
			}
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
		return LayerDefinition.create(meshdefinition, 46, 35);
	}

	@Override
	public ResourceLocation getTexture(int stage) {
		return new ResourceLocation(CreateMissiles.MOD_ID,"textures/entity/direct_hit_warhead.png");
	}

	@Override
	public int getStageCount() {
		return layers.size();
	}
}