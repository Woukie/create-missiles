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
				put("top", new Vector3f(0, 6, 0));
			}},
			new HashMap<>() {{
				put("bottom", new Vector3f(0, 0, 0));
				put("top", new Vector3f(0, 6, 0));
			}},
			new HashMap<>() {{
				put("bottom", new Vector3f(0, 0, 0));
				put("top", new Vector3f(0, 13, 0));
			}}
	);

	private final List<Function<PartDefinition, PartDefinition>> layers = List.of(
			partDefinition -> partDefinition.addOrReplaceChild("bb_main", CubeListBuilder.create(), PartPose.ZERO),
			partDefinition -> {
				PartDefinition bb_main = partDefinition.addOrReplaceChild("bb_main", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));
				bb_main.addOrReplaceChild("head_r1", CubeListBuilder.create().texOffs(24, 0).addBox(-3.0F, -3.0F, -3.0F, 6.0F, 6.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 3.0F, 0.0F, 3.1416F, 0.7854F, 0.0F));
				return bb_main;
			},
			partDefinition -> {
				PartDefinition bb_main = partDefinition.addOrReplaceChild("bb_main", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));
				bb_main.addOrReplaceChild("insides_r1", CubeListBuilder.create().texOffs(0, 20).addBox(-2.5F, -2.5F, -2.5F, 5.0F, 5.0F, 5.0F, new CubeDeformation(0.0F))
						.texOffs(48, 0).addBox(-3.0F, -3.0F, -3.0F, 6.0F, 6.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 3.0F, 0.0F, 3.1416F, 0.7854F, 0.0F));
				return bb_main;
			},
			partDefinition -> {
				PartDefinition Snowman = partDefinition.addOrReplaceChild("Snowman", CubeListBuilder.create().texOffs(44, 43).addBox(-0.6351F, 9.9925F, -2.3078F, 5.0F, 4.0F, 5.0F, new CubeDeformation(0.0F))
						.texOffs(48, 52).addBox(-0.1351F, 13.9925F, -1.8078F, 4.0F, 3.0F, 4.0F, new CubeDeformation(0.0F))
						.texOffs(48, 59).addBox(-0.1351F, 19.9925F, -1.8078F, 4.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
						.texOffs(44, 52).addBox(0.8649F, 20.9925F, -0.8078F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
						.texOffs(46, 61).addBox(-1.6351F, 17.9925F, -0.3078F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.9F, -10.0F, -0.2F));
				PartDefinition arm_right_r1 = Snowman.addOrReplaceChild("arm_right_r1", CubeListBuilder.create().texOffs(32, 60).addBox(-0.5F, -0.5F, -1.5F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.8649F, 15.0221F, 2.7734F, 2.2689F, 0.0F, -3.1416F));
				PartDefinition arm_left_r1 = Snowman.addOrReplaceChild("arm_left_r1", CubeListBuilder.create().texOffs(40, 60).addBox(-0.5F, -0.5F, -1.5F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.8649F, 15.0221F, -2.3889F, 0.8727F, 0.0F, -3.1416F));
				PartDefinition head_r1 = Snowman.addOrReplaceChild("head_r1", CubeListBuilder.create().texOffs(35, 53).addBox(-1.5F, -1.5F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.8649F, 18.4925F, 0.1922F, -3.1416F, 0.0F, 0.0F));
				PartDefinition ice_cube = partDefinition.addOrReplaceChild("ice_cube", CubeListBuilder.create().texOffs(8, 4).addBox(-6.0F, -6.0F, -6.0F, 12.0F, 12.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.2307F, 5.1502F, -0.3254F, -0.7854F, 0.0F, 0.6154F));
				return Snowman;
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
		return new ResourceLocation(CreateMissiles.MOD_ID,"textures/entity/frozen_warhead.png");
	}

	@Override
	public int getStageCount() {
		return layers.size();
	}
}