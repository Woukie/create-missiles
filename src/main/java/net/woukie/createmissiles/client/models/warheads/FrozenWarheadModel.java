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

public class FrozenWarheadModel implements MissilePartModel {
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
				put("top", new Vector3f(0, 13, 0));
			}}
	);

	private final List<Function<PartDefinition, PartDefinition>> layers = List.of(
			partDefinition -> partDefinition.addOrReplaceChild("bb_main", CubeListBuilder.create(), PartPose.ZERO),
			partDefinition -> {
				PartDefinition bb_main = partDefinition.addOrReplaceChild("Snowman", CubeListBuilder.create(), PartPose.offset(-0.3476F, 7.1249F, -0.0078F));
				PartDefinition body_bottom_r1 = bb_main.addOrReplaceChild("body_bottom_r1", CubeListBuilder.create().texOffs(44, 43).addBox(-2.5F, -2.0F, -2.5F, 5.0F, 4.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.3125F, -5.1324F, 0.0F, 0.0F, -1.5708F, 0.0F));
				PartDefinition ice_cube = partDefinition.addOrReplaceChild("ice_cube", CubeListBuilder.create(), PartPose.offsetAndRotation(-0.2307F, 5.1502F, -0.3254F, -0.7854F, 0.0F, 0.6154F));
				return bb_main;
			},
			partDefinition -> {
				PartDefinition bb_main = partDefinition.addOrReplaceChild("Snowman", CubeListBuilder.create(), PartPose.offset(-0.3476F, 7.1249F, -0.0078F));
				PartDefinition body_top_r1 = bb_main.addOrReplaceChild("body_top_r1", CubeListBuilder.create().texOffs(48, 52).addBox(-2.0F, -1.5F, -2.0F, 4.0F, 3.0F, 4.0F, new CubeDeformation(0.0F))
						.texOffs(44, 43).addBox(-2.5F, -5.5F, -2.5F, 5.0F, 4.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.3125F, -1.6324F, 0.0F, 0.0F, -1.5708F, 0.0F));
				PartDefinition ice_cube = partDefinition.addOrReplaceChild("ice_cube", CubeListBuilder.create(), PartPose.offsetAndRotation(-0.2307F, 5.1502F, -0.3254F, -0.7854F, 0.0F, 0.6154F));
				return bb_main;
			},
			partDefinition -> {
				PartDefinition bb_main = partDefinition.addOrReplaceChild("Snowman", CubeListBuilder.create(), PartPose.offset(-0.3476F, 7.1249F, -0.0078F));
				PartDefinition head_r1 = bb_main.addOrReplaceChild("head_r1", CubeListBuilder.create().texOffs(35, 53).addBox(-1.5F, -1.5F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.3125F, 1.3676F, 0.0F, 0.0F, -1.5708F, -3.1416F));
				PartDefinition body_top_r1 = bb_main.addOrReplaceChild("body_top_r1", CubeListBuilder.create().texOffs(48, 52).addBox(-2.0F, -1.5F, -2.0F, 4.0F, 3.0F, 4.0F, new CubeDeformation(0.0F))
						.texOffs(44, 43).addBox(-2.5F, -5.5F, -2.5F, 5.0F, 4.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.3125F, -1.6324F, 0.0F, 0.0F, -1.5708F, 0.0F));
				PartDefinition ice_cube = partDefinition.addOrReplaceChild("ice_cube", CubeListBuilder.create(), PartPose.offsetAndRotation(-0.2307F, 5.1502F, -0.3254F, -0.7854F, 0.0F, 0.6154F));
				return bb_main;
			},
			partDefinition -> {
				PartDefinition bb_main = partDefinition.addOrReplaceChild("Snowman", CubeListBuilder.create(), PartPose.offset(-0.3476F, 7.1249F, -0.0078F));
				PartDefinition arm_right_r1 = bb_main.addOrReplaceChild("arm_right_r1", CubeListBuilder.create().texOffs(32, 60).addBox(-0.5F, -0.5F, -1.5F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.2687F, -2.1028F, 0.0F, 0.0F, 1.5708F, 0.8727F));
				PartDefinition arm_left_r1 = bb_main.addOrReplaceChild("arm_left_r1", CubeListBuilder.create().texOffs(40, 60).addBox(-0.5F, -0.5F, -1.5F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.8937F, -2.1028F, 0.0F, 0.0F, 1.5708F, 2.2689F));
				PartDefinition nose_r1 = bb_main.addOrReplaceChild("nose_r1", CubeListBuilder.create().texOffs(46, 61).addBox(-1.0F, -0.5F, -0.5F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.3125F, 1.3676F, -2.5F, 0.0F, -1.5708F, 0.0F));
				PartDefinition hat_top_r1 = bb_main.addOrReplaceChild("hat_top_r1", CubeListBuilder.create().texOffs(44, 52).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
						.texOffs(48, 59).addBox(-2.0F, -2.0F, -2.0F, 4.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
						.texOffs(48, 52).addBox(-2.0F, -8.0F, -2.0F, 4.0F, 3.0F, 4.0F, new CubeDeformation(0.0F))
						.texOffs(44, 43).addBox(-2.5F, -12.0F, -2.5F, 5.0F, 4.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.3125F, 4.8676F, 0.0F, 0.0F, -1.5708F, 0.0F));
				PartDefinition head_r1 = bb_main.addOrReplaceChild("head_r1", CubeListBuilder.create().texOffs(35, 53).addBox(-1.5F, -1.5F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.3125F, 1.3676F, 0.0F, 0.0F, -1.5708F, -3.1416F));
				PartDefinition ice_cube = partDefinition.addOrReplaceChild("ice_cube", CubeListBuilder.create(), PartPose.offsetAndRotation(-0.2307F, 5.1502F, -0.3254F, -0.7854F, 0.0F, 0.6154F));
				return bb_main;
			},
			partDefinition -> {
				PartDefinition bb_main = partDefinition.addOrReplaceChild("Snowman", CubeListBuilder.create(), PartPose.offset(-0.3476F, 7.1249F, -0.0078F));
				PartDefinition arm_right_r1 = bb_main.addOrReplaceChild("arm_right_r1", CubeListBuilder.create().texOffs(32, 60).addBox(-0.5F, -0.5F, -1.5F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.2687F, -2.1028F, 0.0F, 0.0F, 1.5708F, 0.8727F));
				PartDefinition arm_left_r1 = bb_main.addOrReplaceChild("arm_left_r1", CubeListBuilder.create().texOffs(40, 60).addBox(-0.5F, -0.5F, -1.5F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.8937F, -2.1028F, 0.0F, 0.0F, 1.5708F, 2.2689F));
				PartDefinition nose_r1 = bb_main.addOrReplaceChild("nose_r1", CubeListBuilder.create().texOffs(46, 61).addBox(-1.0F, -0.5F, -0.5F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.3125F, 1.3676F, -2.5F, 0.0F, -1.5708F, 0.0F));
				PartDefinition hat_top_r1 = bb_main.addOrReplaceChild("hat_top_r1", CubeListBuilder.create().texOffs(44, 52).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
						.texOffs(48, 59).addBox(-2.0F, -2.0F, -2.0F, 4.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
						.texOffs(48, 52).addBox(-2.0F, -8.0F, -2.0F, 4.0F, 3.0F, 4.0F, new CubeDeformation(0.0F))
						.texOffs(44, 43).addBox(-2.5F, -12.0F, -2.5F, 5.0F, 4.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.3125F, 4.8676F, 0.0F, 0.0F, -1.5708F, 0.0F));
				PartDefinition head_r1 = bb_main.addOrReplaceChild("head_r1", CubeListBuilder.create().texOffs(35, 53).addBox(-1.5F, -1.5F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.3125F, 1.3676F, 0.0F, 0.0F, -1.5708F, -3.1416F));
				PartDefinition ice_cube = partDefinition.addOrReplaceChild("ice_cube", CubeListBuilder.create().texOffs(8, 4).addBox(-6.0F, -6.0F, -6.0F, 12.0F, 12.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.2307F, 5.1502F, -0.3254F, -0.7854F, 0.0F, 0.6154F));
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
		return ResourceLocation.fromNamespaceAndPath(CreateMissiles.MOD_ID,"textures/entity/frozen_warhead.png");
	}

	@Override
	public int getStageCount() {
		return layers.size();
	}
}