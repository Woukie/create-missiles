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
				put("top", new Vector3f(0, 3, 0));
			}},
			new HashMap<>() {{
				put("bottom", new Vector3f(0, 0, 0));
				put("top", new Vector3f(0, 5, 0));
			}},
			new HashMap<>() {{
				put("bottom", new Vector3f(0, 0, 0));
				put("top", new Vector3f(0, 5, 0));
			}},
			new HashMap<>() {{
				put("bottom", new Vector3f(0, 0, 0));
				put("top", new Vector3f(0, 5, 0));
			}},
			new HashMap<>() {{
				put("bottom", new Vector3f(0, 0, 0));
				put("top", new Vector3f(0, 5, 0));
			}},
			new HashMap<>() {{
				put("bottom", new Vector3f(0, 0, 0));
				put("top", new Vector3f(0, 5, 0));
			}}
	);

	private final List<Function<PartDefinition, PartDefinition>> layers = List.of(
			partDefinition -> partDefinition.addOrReplaceChild("bb_main", CubeListBuilder.create(), PartPose.ZERO),
			partDefinition -> {
				PartDefinition bb_main = partDefinition.addOrReplaceChild("bb_main", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));
				bb_main.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(2, 51).addBox(-2.5F, -1.5F, -2.5F, 5.0F, 3.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 1.5F, 0.0F, 3.1416F, 0.0F, 0.0F));
				return bb_main;
			},
			partDefinition -> {
				PartDefinition bb_main = partDefinition.addOrReplaceChild("bb_main", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));
				bb_main.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(2, 51).addBox(-2.5F, -1.5F, -2.5F, 5.0F, 3.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 1.5F, 0.0F, 3.1416F, 0.0F, 0.0F));
				bb_main.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(4, 45).addBox(-2.0F, -1.0F, -2.0F, 4.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 4.0F, 0.0F, 3.1416F, 0.0F, 0.0F));
				return bb_main;
			},
			partDefinition -> {
				PartDefinition bb_main = partDefinition.addOrReplaceChild("bb_main", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));
				bb_main.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(4, 42).addBox(-3.0F, -0.5F, -1.0F, 6.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 4.5F, 0.0F, 0.0F, 1.5708F, -3.1416F));
				bb_main.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(2, 51).addBox(-2.5F, -1.5F, -2.5F, 5.0F, 3.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 1.5F, 0.0F, 3.1416F, 0.0F, 0.0F));
				bb_main.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(4, 45).addBox(-2.0F, -1.0F, -2.0F, 4.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 4.0F, 0.0F, 3.1416F, 0.0F, 0.0F));
				return bb_main;
			},
			partDefinition -> {
				PartDefinition bb_main = partDefinition.addOrReplaceChild("bb_main", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));
				bb_main.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(4, 42).addBox(-3.0F, -0.5F, -1.0F, 6.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 4.5F, 0.0F, 0.0F, 1.5708F, -3.1416F));
				bb_main.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(4, 42).addBox(-3.0F, -0.5F, -1.0F, 6.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 4.5F, 0.0F, 3.1416F, 0.0F, 0.0F));
				bb_main.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(2, 51).addBox(-2.5F, -1.5F, -2.5F, 5.0F, 3.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 1.5F, 0.0F, 3.1416F, 0.0F, 0.0F));
				bb_main.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(4, 45).addBox(-2.0F, -1.0F, -2.0F, 4.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 4.0F, 0.0F, 3.1416F, 0.0F, 0.0F));
				return bb_main;
			},
			partDefinition -> {
				PartDefinition bb_main = partDefinition.addOrReplaceChild("bb_main", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));
				bb_main.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(4, 42).addBox(-3.0F, -0.5F, -1.0F, 6.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 4.5F, 0.0F, 0.0F, 1.5708F, -3.1416F));
				bb_main.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(4, 42).addBox(-3.0F, -0.5F, -1.0F, 6.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 4.5F, 0.0F, 3.1416F, 0.0F, 0.0F));
				bb_main.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(22, 53).addBox(-0.5F, -2.5F, -1.0F, 1.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.926F, 1.8858F, 0.0F, 0.0F, 1.5708F, -2.7489F));
				bb_main.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(22, 53).addBox(-0.5F, -2.5F, -1.0F, 1.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.926F, 1.8858F, 0.0F, 0.0F, 1.5708F, 2.7489F));
				bb_main.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(2, 51).addBox(-2.5F, -1.5F, -2.5F, 5.0F, 3.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 1.5F, 0.0F, 3.1416F, 0.0F, 0.0F));
				bb_main.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(4, 45).addBox(-2.0F, -1.0F, -2.0F, 4.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 4.0F, 0.0F, 3.1416F, 0.0F, 0.0F));
				return bb_main;
			},
			partDefinition -> {
				PartDefinition bb_main = partDefinition.addOrReplaceChild("bb_main", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));
				bb_main.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(4, 42).addBox(-3.0F, -0.5F, -1.0F, 6.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 4.5F, 0.0F, 0.0F, 1.5708F, 3.1416F));
				bb_main.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(4, 42).addBox(-3.0F, -0.5F, -1.0F, 6.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 4.5F, 0.0F, 3.1416F, 0.0F, 0.0F));
				bb_main.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(22, 53).addBox(-0.5F, -2.5F, -1.0F, 1.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.926F, 1.8858F, 0.0F, 0.0F, 1.5708F, -2.7489F));
				bb_main.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(22, 53).addBox(-0.5F, -2.5F, -1.0F, 1.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 1.8858F, -2.926F, -0.3927F, 0.0F, -3.1416F));
				bb_main.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(22, 53).addBox(-0.5F, -2.5F, -1.0F, 1.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.926F, 1.8858F, 0.0F, 0.0F, 1.5708F, 2.7489F));
				bb_main.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(22, 53).addBox(-0.5F, -2.5F, -1.0F, 1.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 1.8858F, 2.926F, 0.3927F, 0.0F, 3.1416F));
				bb_main.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(2, 51).addBox(-2.5F, -1.5F, -2.5F, 5.0F, 3.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 1.5F, 0.0F, 3.1416F, 0.0F, 0.0F));
				bb_main.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(4, 45).addBox(-2.0F, -1.0F, -2.0F, 4.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 4.0F, 0.0F, 3.1416F, 0.0F, 0.0F));
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
		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public ResourceLocation getTexture(int stage) {
		return new ResourceLocation(CreateMissiles.MOD_ID,"textures/entity/excavator.png");
	}

	@Override
	public int getStageCount() {
		return layers.size();
	}
}