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

public class ExcavatorWarheadModel implements MissilePartModel {
	private final List<Map<String, Vector3f>> attachments = List.of(
			new HashMap<>() {{
				put("bottom", new Vector3f(0, 0, 0));
				put("top", new Vector3f(0, 0, 0));
			}},
			new HashMap<>() {{
				put("bottom", new Vector3f(0, 0, 0));
				put("top", new Vector3f(0, 3.5f, 0));
			}},
			new HashMap<>() {{
				put("bottom", new Vector3f(0, 0, 0));
				put("top", new Vector3f(0, 6.5f, 0));
			}},
			new HashMap<>() {{
				put("bottom", new Vector3f(0, 0, 0));
				put("top", new Vector3f(0, 8.5f, 0));
			}},
			new HashMap<>() {{
				put("bottom", new Vector3f(0, 0, 0));
				put("top", new Vector3f(0, 8.5f, 0));
			}}
	);

	private final List<Function<PartDefinition, PartDefinition>> layers = List.of(
			partDefinition -> partDefinition.addOrReplaceChild("bb_main", CubeListBuilder.create(), PartPose.ZERO),
			partDefinition -> {
				PartDefinition bb_main = partDefinition.addOrReplaceChild("bb_main", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));
				bb_main.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(0, 12).addBox(-3.0F, -1.5F, -3.0F, 6.0F, 3.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 2.0F, 0.0F, -3.1416F, -1.1781F, 0.0F));
				bb_main.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(4, 21).addBox(-2.0F, -0.5F, -2.0F, 4.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.5F, 0.0F, -3.1416F, -0.7854F, 0.0F));
				return partDefinition;
			},
			partDefinition -> {
				PartDefinition bb_main = partDefinition.addOrReplaceChild("bb_main", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));
				bb_main.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(4, 5).addBox(-2.0F, -1.5F, -2.0F, 4.0F, 3.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 5.0F, 0.0F, -3.1416F, 0.0F, 0.0F));
				bb_main.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(0, 12).addBox(-3.0F, -1.5F, -3.0F, 6.0F, 3.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 2.0F, 0.0F, -3.1416F, -1.1781F, 0.0F));
				bb_main.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(4, 21).addBox(-2.0F, -0.5F, -2.0F, 4.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.5F, 0.0F, -3.1416F, -0.7854F, 0.0F));
				return partDefinition;
			},
			partDefinition -> {
				PartDefinition bb_main = partDefinition.addOrReplaceChild("bb_main", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));
				bb_main.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(8, 0).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 7.5F, 0.0F, 3.1416F, -0.3927F, 0.0F));
				bb_main.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(4, 5).addBox(-2.0F, -1.5F, -2.0F, 4.0F, 3.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 5.0F, 0.0F, -3.1416F, 0.0F, 0.0F));
				bb_main.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(0, 12).addBox(-3.0F, -1.5F, -3.0F, 6.0F, 3.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 2.0F, 0.0F, -3.1416F, -1.1781F, 0.0F));
				bb_main.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(4, 21).addBox(-2.0F, -0.5F, -2.0F, 4.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.5F, 0.0F, -3.1416F, -0.7854F, 0.0F));
				return partDefinition;
			},
			partDefinition -> {
				PartDefinition bb_main = partDefinition.addOrReplaceChild("bb_main", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));
				bb_main.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(0, 12).addBox(-3.0F, -1.5F, -3.0F, 6.0F, 3.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 2.0F, 0.0F, 0.0F, -0.3927F, -3.1416F));
				bb_main.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(20, 10).addBox(-2.5F, -0.5F, -0.5F, 5.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, 4.5F, 0.0F, 0.0F, -1.5708F, 3.1416F));
				bb_main.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(22, 8).addBox(-1.5F, -0.5F, -0.5F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
						.texOffs(8, 0).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 7.5F, 0.0F, 3.1416F, -0.3927F, 0.0F));
				bb_main.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(20, 10).addBox(-2.5F, -0.5F, -0.5F, 5.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 5.5F, -1.0F, -3.1416F, 0.0F, 0.0F));
				bb_main.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(18, 12).addBox(-3.5F, -0.5F, -0.5F, 7.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.8478F, 1.5F, 0.7654F, 0.0F, 1.1781F, -3.1416F));
				bb_main.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(18, 12).addBox(-3.5F, -0.5F, -0.5F, 7.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.8478F, 2.5F, -0.7654F, 0.0F, 1.1781F, -3.1416F));
				bb_main.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(18, 12).addBox(-3.5F, -0.5F, -0.5F, 7.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.7654F, 2.5F, 1.8478F, 0.0F, -0.3927F, -3.1416F));
				bb_main.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(18, 12).addBox(-3.5F, -0.5F, -0.5F, 7.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.7654F, 1.5F, -1.8478F, 0.0F, -0.3927F, -3.1416F));
				bb_main.addOrReplaceChild("cube_r9", CubeListBuilder.create().texOffs(4, 5).addBox(-2.0F, -1.5F, -2.0F, 4.0F, 3.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 5.0F, 0.0F, -3.1416F, 0.0F, 0.0F));
				bb_main.addOrReplaceChild("cube_r10", CubeListBuilder.create().texOffs(4, 21).addBox(-2.0F, -0.5F, -2.0F, 4.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.5F, 0.0F, -3.1416F, -0.7854F, 0.0F));
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