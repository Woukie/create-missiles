package net.woukie.createmissiles.client.models;

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

public class TntWarheadModel implements MissilePartModel {
	private final List<Function<PartDefinition, PartDefinition>> layers = List.of(
			partDefinition -> partDefinition.addOrReplaceChild("bb_main", CubeListBuilder.create(), PartPose.ZERO),
			partDefinition -> {
				PartDefinition bb_main = partDefinition.addOrReplaceChild("bb_main", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));
				bb_main.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(-8, -4).addBox(-3.0F, 0.0F, -3.0F, 6.0F, 6.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 1.5708F, 0.0F));
				return partDefinition;
			},
			partDefinition -> {
				PartDefinition bb_main = partDefinition.addOrReplaceChild("bb_main", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));
				bb_main.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(-3, 1).addBox(-3.0F, 0.0F, -1.0F, 6.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -7.0F, 2.7925F, -0.0F, -3.1416F));
				bb_main.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(-3, 1).addBox(-3.0F, 0.0F, -1.0F, 6.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-7.0F, 0.0F, 0.0F, -0.0F, 1.5708F, -0.3491F));
				bb_main.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(-3, 1).addBox(-3.0F, 0.0F, -1.0F, 6.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(7.0F, 0.0F, 0.0F, -0.0F, -1.5708F, 0.3491F));
				bb_main.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(-3, 1).addBox(-3.0F, 0.0F, -1.0F, 6.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 7.0F, -0.3491F, -0.0F, -0.0F));
				bb_main.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(-8, -4).addBox(-3.0F, 0.0F, -3.0F, 6.0F, 6.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 1.5708F, 0.0F));
				return partDefinition;
			},
			partDefinition -> {
				PartDefinition bb_main = partDefinition.addOrReplaceChild("bb_main", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));
				bb_main.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(-3, 1).addBox(-3.0F, 0.0F, -1.0F, 6.0F, 10.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -7.0F, 2.7925F, -0.0F, -3.1416F));
				bb_main.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(-3, 1).addBox(-3.0F, 0.0F, -1.0F, 6.0F, 10.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-7.0F, 0.0F, 0.0F, -0.0F, 1.5708F, -0.3491F));
				bb_main.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(-3, 1).addBox(-3.0F, 0.0F, -1.0F, 6.0F, 10.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(7.0F, 0.0F, 0.0F, -0.0F, -1.5708F, 0.3491F));
				bb_main.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(-3, 1).addBox(-3.0F, 0.0F, -1.0F, 6.0F, 10.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 7.0F, -0.3491F, -0.0F, -0.0F));
				bb_main.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(-8, -4).addBox(-3.0F, 0.0F, -3.0F, 6.0F, 6.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 1.5708F, 0.0F));
				return partDefinition;
			},
			partDefinition -> {
				PartDefinition bb_main = partDefinition.addOrReplaceChild("bb_main", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));
				bb_main.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(-5, -1).addBox(-3.0F, 0.0F, -3.0F, 3.0F, 10.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-7.0F, 0.0F, 7.0F, -1.9199F, -1.2217F, 1.6143F));
				bb_main.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(-5, -1).addBox(-3.0F, 0.0F, -3.0F, 3.0F, 10.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-7.0F, 0.0F, -7.0F, 2.812F, 0.1172F, 2.812F));
				bb_main.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(-5, -1).addBox(-3.0F, 0.0F, -3.0F, 3.0F, 10.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(7.0F, 0.0F, -7.0F, 1.2217F, 1.2217F, 1.6143F));
				bb_main.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(-5, -1).addBox(-3.0F, 0.0F, -3.0F, 3.0F, 10.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(7.0F, 0.0F, 7.0F, -0.3295F, -0.1172F, 0.2859F));
				bb_main.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(-3, 1).addBox(-3.0F, 0.0F, -1.0F, 6.0F, 10.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -7.0F, 2.7925F, -0.0F, -3.1416F));
				bb_main.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(-3, 1).addBox(-3.0F, 0.0F, -1.0F, 6.0F, 10.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-7.0F, 0.0F, 0.0F, -0.0F, 1.5708F, -0.3491F));
				bb_main.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(-3, 1).addBox(-3.0F, 0.0F, -1.0F, 6.0F, 10.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(7.0F, 0.0F, 0.0F, -0.0F, -1.5708F, 0.3491F));
				bb_main.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(-3, 1).addBox(-3.0F, 0.0F, -1.0F, 6.0F, 10.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 7.0F, -0.3491F, -0.0F, -0.0F));
				bb_main.addOrReplaceChild("cube_r9", CubeListBuilder.create().texOffs(-8, -4).addBox(-3.0F, 0.0F, -3.0F, 6.0F, 6.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.0F, -1.5708F, -0.0F));
				return partDefinition;
			},
			partDefinition -> {
				PartDefinition bb_main = partDefinition.addOrReplaceChild("bb_main", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));
				bb_main.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(-5, -1).addBox(-3.0F, 0.0F, -3.0F, 3.0F, 15.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-7.0F, 0.0F, 7.0F, -1.9199F, -1.2217F, 1.6143F));
				bb_main.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(-5, -1).addBox(-3.0F, 0.0F, -3.0F, 3.0F, 15.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-7.0F, 0.0F, -7.0F, 2.812F, 0.1172F, 2.812F));
				bb_main.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(-5, -1).addBox(-3.0F, 0.0F, -3.0F, 3.0F, 15.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(7.0F, 0.0F, -7.0F, 1.2217F, 1.2217F, 1.6143F));
				bb_main.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(-5, -1).addBox(-3.0F, 0.0F, -3.0F, 3.0F, 15.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(7.0F, 0.0F, 7.0F, -0.3295F, -0.1172F, 0.2859F));
				bb_main.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(-3, 1).addBox(-3.0F, 0.0F, -1.0F, 6.0F, 12.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -7.0F, 2.7925F, -0.0F, -3.1416F));
				bb_main.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(-3, 1).addBox(-3.0F, 0.0F, -1.0F, 6.0F, 12.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-7.0F, 0.0F, 0.0F, -0.0F, 1.5708F, -0.3491F));
				bb_main.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(-3, 1).addBox(-3.0F, 0.0F, -1.0F, 6.0F, 12.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(7.0F, 0.0F, 0.0F, -0.0F, -1.5708F, 0.3491F));
				bb_main.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(-3, 1).addBox(-3.0F, 0.0F, -1.0F, 6.0F, 12.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 7.0F, -0.3491F, -0.0F, -0.0F));
				bb_main.addOrReplaceChild("cube_r9", CubeListBuilder.create().texOffs(-8, -4).addBox(-3.0F, 0.0F, -3.0F, 6.0F, 6.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.0F, -1.5708F, -0.0F));
				return partDefinition;
			}
	);

	@Override
	public Map<String, Vector3f> getAttachements(int layer) {
		return new HashMap<>() {{
			put("bottom", new Vector3f(0, 0, 0));
		}};
	}

	@Override
	public LayerDefinition getLayerDefinition(int stage) {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();
		layers.get(stage).apply(partdefinition);
		return LayerDefinition.create(meshdefinition, 16, 16);
	}

	@Override
	public ResourceLocation getTexture(int stage) {
		return new ResourceLocation(CreateMissiles.MOD_ID,"textures/entity/tnt_warhead.png");
	}

	@Override
	public int getStageCount() {
		return layers.size();
	}
}