package net.woukie.createmissiles.client.models.thrusters;

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

public class ExcavatorThrusterModel implements MissilePartModel {
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
				put("top", new Vector3f(0, 3, 0));
			}},
			new HashMap<>() {{
				put("bottom", new Vector3f(0, 0, 0));
				put("top", new Vector3f(0, 4, 0));
			}},
			new HashMap<>() {{
				put("bottom", new Vector3f(0, 0, 0));
				put("top", new Vector3f(0, 7, 0));
			}}
	);

	private final List<Function<PartDefinition, PartDefinition>> layers = List.of(
			partDefinition -> partDefinition.addOrReplaceChild("bb_main", CubeListBuilder.create(), PartPose.ZERO),
			partDefinition -> {
				PartDefinition bb_main = partDefinition.addOrReplaceChild("bb_main", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));
				bb_main.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -1.0F, -4.0F, 8.0F, 2.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 1.0F, 0.0F, 0.0F, 1.5708F, 0.0F));
				return bb_main;
			},
			partDefinition -> {
				PartDefinition bb_main = partDefinition.addOrReplaceChild("bb_main", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));
				bb_main.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(24, 20).addBox(-5.0F, -1.5F, -1.0F, 10.0F, 3.0F, 2.0F, new CubeDeformation(0.0F))
						.texOffs(0, 0).addBox(-4.0F, -0.5F, -4.0F, 8.0F, 2.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 1.5F, 0.0F, 0.0F, 1.5708F, 0.0F));
				return bb_main;
			},
			partDefinition -> {
				PartDefinition bb_main = partDefinition.addOrReplaceChild("bb_main", CubeListBuilder.create().texOffs(24, 25).addBox(-5.0F, 0.0F, -1.0F, 10.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
				bb_main.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(24, 20).addBox(-5.0F, -1.5F, -1.0F, 10.0F, 3.0F, 2.0F, new CubeDeformation(0.0F))
						.texOffs(0, 0).addBox(-4.0F, -0.5F, -4.0F, 8.0F, 2.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 1.5F, 0.0F, 0.0F, 1.5708F, 0.0F));
				return bb_main;
			},
			partDefinition -> {
				PartDefinition bb_main = partDefinition.addOrReplaceChild("bb_main", CubeListBuilder.create().texOffs(24, 25).addBox(-5.0F, 1.0F, -1.0F, 10.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
				bb_main.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(0, 20).addBox(-3.0F, -1.0F, -3.0F, 6.0F, 2.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 1.01F, 0.0F, -3.1416F, 0.7854F, 0.0F));
				bb_main.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(24, 20).addBox(-5.0F, -1.5F, -1.0F, 10.0F, 3.0F, 2.0F, new CubeDeformation(0.0F))
						.texOffs(0, 0).addBox(-4.0F, -0.5F, -4.0F, 8.0F, 2.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 2.5F, 0.0F, 0.0F, 1.5708F, 0.0F));
				return bb_main;
			},
			partDefinition -> {
				PartDefinition bb_main = partDefinition.addOrReplaceChild("bb_main", CubeListBuilder.create().texOffs(24, 25).addBox(-5.0F, 4.0F, -1.0F, 10.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
				bb_main.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(0, 10).addBox(-3.5F, -1.5F, -3.5F, 7.0F, 3.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 1.5F, 0.0F, -3.1416F, 0.0F, 0.0F));
				bb_main.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(0, 20).addBox(-3.0F, -1.0F, -3.0F, 6.0F, 2.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 4.01F, 0.0F, -3.1416F, 0.7854F, 0.0F));
				bb_main.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(24, 20).addBox(-5.0F, -1.5F, -1.0F, 10.0F, 3.0F, 2.0F, new CubeDeformation(0.0F))
						.texOffs(0, 0).addBox(-4.0F, -0.5F, -4.0F, 8.0F, 2.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 5.5F, 0.0F, 0.0F, 1.5708F, 0.0F));
				return bb_main;
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
		return LayerDefinition.create(meshdefinition, 48, 48);
	}

	@Override
	public ResourceLocation getTexture(int stage) {
		return new ResourceLocation(CreateMissiles.MOD_ID,"textures/entity/excavator_thruster.png");
	}

	@Override
	public int getStageCount() {
		return layers.size();
	}
}