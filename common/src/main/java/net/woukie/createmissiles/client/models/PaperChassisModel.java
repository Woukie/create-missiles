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

public class PaperChassisModel implements MissilePartModel {
	private final List<Map<String, Vector3f>> attachments = List.of(
			new HashMap<>() {{
				put("bottom", new Vector3f(0, 0, 0));
				put("top", new Vector3f(0, 0, 0));
			}},
			new HashMap<>() {{
				put("bottom", new Vector3f(0, 0, 0));
				put("top", new Vector3f(0, 15, 0));
			}},
			new HashMap<>() {{
				put("bottom", new Vector3f(0, 0, 0));
				put("top", new Vector3f(0, 26, 0));
			}},
			new HashMap<>() {{
				put("bottom", new Vector3f(0, 0, 0));
				put("top", new Vector3f(0, 26, 0));
			}},
			new HashMap<>() {{
				put("bottom", new Vector3f(0, 0, 0));
				put("top", new Vector3f(0, 26, 0));
			}},
			new HashMap<>() {{
				put("bottom", new Vector3f(0, 0, 0));
				put("top", new Vector3f(0, 26, 0));
			}}
	);

	private final List<Function<PartDefinition, PartDefinition>> layers = List.of(
			partDefinition -> partDefinition.addOrReplaceChild("bb_main", CubeListBuilder.create(), PartPose.ZERO),
			partDefinition -> partDefinition.addOrReplaceChild("bb_main", CubeListBuilder.create().texOffs(-14, 0).addBox(-8.0F, 0.0F, 6.0F, 16.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
					.texOffs(-14, 0).addBox(-8.0F, 0.0F, -8.0F, 16.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
					.texOffs(-14, 0).addBox(-8.0F, 13.0F, -8.0F, 16.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
					.texOffs(-14, 0).addBox(-8.0F, 13.0F, 6.0F, 16.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
					.texOffs(0, -8).addBox(-8.0F, 2.0F, -8.0F, 2.0F, 11.0F, 2.0F, new CubeDeformation(0.0F))
					.texOffs(5, 0).addBox(6.0F, 2.0F, -8.0F, 2.0F, 11.0F, 2.0F, new CubeDeformation(0.0F))
					.texOffs(0, 0).addBox(-8.0F, 2.0F, 6.0F, 2.0F, 11.0F, 2.0F, new CubeDeformation(0.0F))
					.texOffs(8, -8).addBox(6.0F, 2.0F, 6.0F, 2.0F, 11.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F))
					.addOrReplaceChild("cube_r1", CubeListBuilder.create()
					.texOffs(-10, 0).addBox(-6.0F, 11.0F, -8.0F, 12.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
					.texOffs(-10, 0).addBox(-6.0F, 11.0F, 6.0F, 12.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
					.texOffs(-10, 0).addBox(-6.0F, -2.0F, 6.0F, 12.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
					.texOffs(-10, 0).addBox(-6.0F, -2.0F, -8.0F, 12.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 2.0F, 0.0F, 0.0F, 1.5708F, 0.0F)),
			partDefinition -> partDefinition.addOrReplaceChild("bb_main", CubeListBuilder.create()
					.texOffs(-14, 0).addBox(-8.0F, 0.0F, 6.0F, 16.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
					.texOffs(-14, 0).addBox(-8.0F, 0.0F, -8.0F, 16.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
					.texOffs(-14, 0).addBox(-8.0F, 24.0F, -8.0F, 16.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
					.texOffs(-14, 0).addBox(-8.0F, 24.0F, 6.0F, 16.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
					.texOffs(0, -8).addBox(-8.0F, 2.0F, -8.0F, 2.0F, 22.0F, 2.0F, new CubeDeformation(0.0F))
					.texOffs(5, 0).addBox(6.0F, 2.0F, -8.0F, 2.0F, 22.0F, 2.0F, new CubeDeformation(0.0F))
					.texOffs(0, 0).addBox(-8.0F, 2.0F, 6.0F, 2.0F, 22.0F, 2.0F, new CubeDeformation(0.0F))
					.texOffs(8, -8).addBox(6.0F, 2.0F, 6.0F, 2.0F, 22.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F))
					.addOrReplaceChild("cube_r1", CubeListBuilder.create()
					.texOffs(-10, 0).addBox(-6.0F, 11.0F, -8.0F, 12.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
					.texOffs(-10, 0).addBox(-6.0F, 11.0F, 6.0F, 12.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
					.texOffs(-10, 0).addBox(-6.0F, -13.0F, 6.0F, 12.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
					.texOffs(-10, 0).addBox(-6.0F, -13.0F, -8.0F, 12.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 13.0F, 0.0F, 0.0F, 1.5708F, 0.0F)),
			partDefinition -> {
				PartDefinition bb_main = partDefinition.addOrReplaceChild("bb_main", CubeListBuilder.create()
						.texOffs(-14, 0).addBox(-8.0F, 0.0F, 6.0F, 16.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
						.texOffs(-14, 0).addBox(-8.0F, 0.0F, -8.0F, 16.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
						.texOffs(-14, 0).addBox(-8.0F, 24.0F, -8.0F, 16.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
						.texOffs(-14, 0).addBox(-8.0F, 24.0F, 6.0F, 16.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
						.texOffs(0, -8).addBox(-8.0F, 2.0F, -8.0F, 2.0F, 22.0F, 2.0F, new CubeDeformation(0.0F))
						.texOffs(5, 0).addBox(6.0F, 2.0F, -8.0F, 2.0F, 22.0F, 2.0F, new CubeDeformation(0.0F))
						.texOffs(0, 0).addBox(-8.0F, 2.0F, 6.0F, 2.0F, 22.0F, 2.0F, new CubeDeformation(0.0F))
						.texOffs(8, -8).addBox(6.0F, 2.0F, 6.0F, 2.0F, 22.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

				bb_main.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(1, -7).addBox(-1.0F, -12.0F, -7.0F, 2.0F, 24.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 13.0F, 0.0F, 1.5708F, -1.1781F, -1.5708F));
				bb_main.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(1, -7).addBox(-1.0F, -12.0F, 6.0F, 2.0F, 24.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 13.0F, 0.0F, 0.0F, 0.0F, 0.3927F));
				bb_main.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(1, -7).addBox(-1.0F, -12.0F, -7.0F, 2.0F, 24.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 13.0F, 0.0F, 0.0F, 0.0F, -0.3927F));
				bb_main.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(1, -7).addBox(-1.0F, -12.0F, 6.0F, 2.0F, 24.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 13.0F, 0.0F, -1.5708F, -1.1781F, 1.5708F));
				bb_main.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(-10, 0).addBox(-6.0F, 11.0F, -8.0F, 12.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
						.texOffs(-10, 0).addBox(-6.0F, 11.0F, 6.0F, 12.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
						.texOffs(-10, 0).addBox(-6.0F, -13.0F, 6.0F, 12.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
						.texOffs(-10, 0).addBox(-6.0F, -13.0F, -8.0F, 12.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 13.0F, 0.0F, 0.0F, 1.5708F, 0.0F));

				return partDefinition;
			},
			partDefinition -> {
				PartDefinition bb_main = partDefinition.addOrReplaceChild("bb_main", CubeListBuilder.create().texOffs(-14, 0).addBox(-8.0F, 0.0F, 6.0F, 16.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
						.texOffs(-14, 0).addBox(-8.0F, 0.0F, -8.0F, 16.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
						.texOffs(-14, 0).addBox(-8.0F, 24.0F, -8.0F, 16.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
						.texOffs(-14, 0).addBox(-8.0F, 24.0F, 6.0F, 16.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
						.texOffs(0, -8).addBox(-8.0F, 2.0F, -8.0F, 2.0F, 22.0F, 2.0F, new CubeDeformation(0.0F))
						.texOffs(5, 0).addBox(6.0F, 2.0F, -8.0F, 2.0F, 22.0F, 2.0F, new CubeDeformation(0.0F))
						.texOffs(0, 0).addBox(-8.0F, 2.0F, 6.0F, 2.0F, 22.0F, 2.0F, new CubeDeformation(0.0F))
						.texOffs(8, -8).addBox(6.0F, 2.0F, 6.0F, 2.0F, 22.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

				bb_main.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(1, -7).addBox(-1.0F, -12.0F, 0.0F, 2.0F, 24.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(7.0F, 13.0F, 0.0F, -1.5708F, -1.1781F, 1.5708F));
				bb_main.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(1, -7).addBox(-1.0F, -12.0F, 0.0F, 2.0F, 24.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(7.0F, 13.0F, 0.0F, 1.5708F, -1.1781F, -1.5708F));
				bb_main.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(1, -7).addBox(-1.0F, -12.0F, 0.0F, 2.0F, 24.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-6.0F, 13.0F, 0.0F, -1.5708F, -1.1781F, 1.5708F));
				bb_main.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(1, -7).addBox(-1.0F, -12.0F, 0.0F, 2.0F, 24.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-6.0F, 13.0F, 0.0F, 1.5708F, -1.1781F, -1.5708F));
				bb_main.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(1, -7).addBox(-1.0F, -12.0F, 0.0F, 2.0F, 24.0F, 1.0F, new CubeDeformation(0.0F))
						.texOffs(1, -7).addBox(-1.0F, -12.0F, -13.0F, 2.0F, 24.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 13.0F, 6.0F, 0.0F, 0.0F, 0.3927F));
				bb_main.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(1, -7).addBox(-1.0F, -12.0F, 0.0F, 2.0F, 24.0F, 1.0F, new CubeDeformation(0.0F))
						.texOffs(1, -7).addBox(-1.0F, -12.0F, -13.0F, 2.0F, 24.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 13.0F, 6.0F, 0.0F, 0.0F, -0.3927F));
				bb_main.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(-10, 0).addBox(-6.0F, 11.0F, -8.0F, 12.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
						.texOffs(-10, 0).addBox(-6.0F, 11.0F, 6.0F, 12.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
						.texOffs(-10, 0).addBox(-6.0F, -13.0F, 6.0F, 12.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
						.texOffs(-10, 0).addBox(-6.0F, -13.0F, -8.0F, 12.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 13.0F, 0.0F, 0.0F, 1.5708F, 0.0F));

				return partDefinition;
			},
			partDefinition -> partDefinition.addOrReplaceChild("bb_main", CubeListBuilder.create()
					.texOffs(-48, -26).addBox(-8.0F, 0.0F, -8.0F, 16.0F, 26.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F))
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
		return LayerDefinition.create(meshdefinition, 16, 16);
	}

	@Override
	public ResourceLocation getTexture(int stage) {
		return new ResourceLocation(CreateMissiles.MOD_ID,"textures/entity/paper_chassis.png");
	}

	@Override
	public int getStageCount() {
		return layers.size();
	}
}