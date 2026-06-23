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

public class FlamingThrusterModel implements MissilePartModel {
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
				put("top", new Vector3f(0, 4, 0));
			}},
			new HashMap<>() {{
				put("bottom", new Vector3f(0, 0, 0));
				put("top", new Vector3f(0, 8, 0));
			}},
			new HashMap<>() {{
				put("bottom", new Vector3f(0, 0, 0));
				put("top", new Vector3f(0, 8, 0));
			}}
	);

	private final List<Function<PartDefinition, PartDefinition>> layers = List.of(
			partDefinition -> partDefinition.addOrReplaceChild("bb_main", CubeListBuilder.create(), PartPose.ZERO),
			partDefinition -> {
				PartDefinition bb_main = partDefinition.addOrReplaceChild("bb_main", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));
				bb_main.addOrReplaceChild("cap_r1", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, -1.0F, -2.0F, 4.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 1.0F, 0.0F, 0.0F, -0.7854F, 3.1416F));
				return bb_main;
			},
			partDefinition -> {
				PartDefinition bb_main = partDefinition.addOrReplaceChild("bb_main", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));
				bb_main.addOrReplaceChild("metal_r1", CubeListBuilder.create().texOffs(8, 6).addBox(-2.0F, -1.5F, -0.5F, 4.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.9513F, 1.4896F, -1.9513F, 2.9974F, -0.7854F, 3.1416F));
				bb_main.addOrReplaceChild("metal_r2", CubeListBuilder.create().texOffs(8, 6).addBox(-2.0F, -1.5F, -0.5F, 4.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.9513F, 1.4896F, -1.9513F, 2.9974F, 0.7854F, 3.1416F));
				bb_main.addOrReplaceChild("metal_r3", CubeListBuilder.create().texOffs(8, 6).addBox(-2.0F, -1.5F, -0.5F, 4.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.9513F, 1.4896F, 1.9513F, -0.1442F, 0.7854F, 0.0F));
				bb_main.addOrReplaceChild("metal_r4", CubeListBuilder.create().texOffs(8, 6).addBox(-2.0F, -1.5F, -0.5F, 4.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.9513F, 1.4896F, 1.9513F, -0.1442F, -0.7854F, 0.0F));
				bb_main.addOrReplaceChild("cap_r1", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, -1.0F, -2.0F, 4.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 3.0F, 0.0F, 0.0F, -0.7854F, 3.1416F));
				return bb_main;
			},
			partDefinition -> {
				PartDefinition bb_main = partDefinition.addOrReplaceChild("bb_main", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));
				bb_main.addOrReplaceChild("metal_r1", CubeListBuilder.create().texOffs(8, 6).addBox(-2.0F, -3.5F, -0.5F, 4.0F, 7.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.7481F, 3.5104F, -1.7481F, 2.9974F, -0.7854F, 3.1416F));
				bb_main.addOrReplaceChild("metal_r2", CubeListBuilder.create().texOffs(8, 6).addBox(-2.0F, -3.5F, -0.5F, 4.0F, 7.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.7481F, 3.5104F, -1.7481F, 2.9974F, 0.7854F, 3.1416F));
				bb_main.addOrReplaceChild("metal_r3", CubeListBuilder.create().texOffs(8, 6).addBox(-2.0F, -3.5F, -0.5F, 4.0F, 7.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.7481F, 3.5104F, 1.7481F, -0.1442F, 0.7854F, 0.0F));
				bb_main.addOrReplaceChild("metal_r4", CubeListBuilder.create().texOffs(8, 6).addBox(-2.0F, -3.5F, -0.5F, 4.0F, 7.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.7481F, 3.5104F, 1.7481F, -0.1442F, -0.7854F, 0.0F));
				bb_main.addOrReplaceChild("cap_r1", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, -1.0F, -2.0F, 4.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 7.0F, 0.0F, 0.0F, -0.7854F, 3.1416F));
				return bb_main;
			},
			partDefinition -> {
				PartDefinition bb_main = partDefinition.addOrReplaceChild("bb_main", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));
				bb_main.addOrReplaceChild("metal_r1", CubeListBuilder.create().texOffs(8, 6).addBox(-2.0F, -3.5F, -0.5F, 4.0F, 7.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.7481F, 3.5104F, -1.7481F, 2.9974F, -0.7854F, 3.1416F));
				bb_main.addOrReplaceChild("metal_r2", CubeListBuilder.create().texOffs(8, 6).addBox(-2.0F, -3.5F, -0.5F, 4.0F, 7.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.7481F, 3.5104F, -1.7481F, 2.9974F, 0.7854F, 3.1416F));
				bb_main.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(0, 6).addBox(-1.0F, -4.0F, -1.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.0028F, 3.8737F, 0.0F, 0.0F, 0.0F, -0.252F));
				bb_main.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(0, 6).addBox(-1.0F, -4.0F, -1.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 3.8737F, 3.0028F, -2.8896F, 0.0F, 3.1416F));
				bb_main.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(0, 6).addBox(-1.0F, -4.0F, -1.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.0028F, 3.8737F, 0.0F, 0.0F, 0.0F, 0.252F));
				bb_main.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(0, 6).addBox(-1.0F, -4.0F, -1.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 3.8737F, -3.0028F, 2.8896F, 0.0F, -3.1416F));
				bb_main.addOrReplaceChild("metal_r3", CubeListBuilder.create().texOffs(8, 6).addBox(-2.0F, -3.5F, -0.5F, 4.0F, 7.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.7481F, 3.5104F, 1.7481F, -0.1442F, 0.7854F, 0.0F));
				bb_main.addOrReplaceChild("metal_r4", CubeListBuilder.create().texOffs(8, 6).addBox(-2.0F, -3.5F, -0.5F, 4.0F, 7.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.7481F, 3.5104F, 1.7481F, -0.1442F, -0.7854F, 0.0F));
				bb_main.addOrReplaceChild("cap_r1", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, -1.0F, -2.0F, 4.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 7.0F, 0.0F, 0.0F, -0.7854F, 3.1416F));
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
		return LayerDefinition.create(meshdefinition, 18, 16);
	}

	@Override
	public ResourceLocation getTexture(int stage) {
		return new ResourceLocation(CreateMissiles.MOD_ID,"textures/entity/flaming_thruster.png");
	}

	@Override
	public int getStageCount() {
		return layers.size();
	}
}