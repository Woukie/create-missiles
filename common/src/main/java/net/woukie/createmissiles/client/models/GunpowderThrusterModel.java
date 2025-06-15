package net.woukie.createmissiles.client.models;

import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.woukie.createmissiles.CreateMissiles;
import net.woukie.createmissiles.client.MissilePartModel;
import org.joml.Vector3f;

import java.util.*;
import java.util.function.Function;

public class GunpowderThrusterModel implements MissilePartModel {
	private final List<Map<String, Vector3f>> attachments = List.of(
			new HashMap<>() {{
				put("bottom", new Vector3f(0, 0, 0));
				put("top", new Vector3f(0, 0, 0));
			}},
			new HashMap<>() {{
				put("bottom", new Vector3f(0, 0, 0));
				put("top", new Vector3f(0, 8, 0));
			}},
			new HashMap<>() {{
				put("bottom", new Vector3f(0, 0, 0));
				put("top", new Vector3f(0, 12, 0));
			}},
			new HashMap<>() {{
				put("bottom", new Vector3f(0, 0, 0));
				put("top", new Vector3f(0, 14, 0));
			}},
			new HashMap<>() {{
				put("bottom", new Vector3f(0, 0, 0));
				put("top", new Vector3f(0, 17, 0));
			}},
			new HashMap<>() {{
				put("bottom", new Vector3f(0, 0, 0));
				put("top", new Vector3f(0, 17, 0));
			}}
	);

	private final List<Function<PartDefinition, PartDefinition>> layers = List.of(
			partDefinition -> partDefinition.addOrReplaceChild("bb_main", CubeListBuilder.create(), PartPose.ZERO),
			partDefinition -> partDefinition.addOrReplaceChild("bb_main", CubeListBuilder.create().texOffs(-24, -12).addBox(-7.0F, 0.0F, -7.0F, 14.0F, 4.0F, 14.0F, new CubeDeformation(0.0F))
					.texOffs(-10, 0).addBox(-6.0F, 6.0F, -6.0F, 12.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
					.texOffs(-16, -6).addBox(-6.0F, 6.0F, -4.0F, 2.0F, 2.0F, 8.0F, new CubeDeformation(0.0F))
					.texOffs(-10, 0).addBox(-6.0F, 6.0F, 4.0F, 12.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
					.texOffs(-16, -6).addBox(4.0F, 6.0F, -4.0F, 2.0F, 2.0F, 8.0F, new CubeDeformation(0.0F))
					.texOffs(0, 0).addBox(4.0F, 4.0F, -6.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
					.texOffs(0, 0).addBox(4.0F, 4.0F, 4.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
					.texOffs(0, 0).addBox(-6.0F, 4.0F, 4.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
					.texOffs(0, 0).addBox(-6.0F, 4.0F, -6.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F)),
			partDefinition -> partDefinition.addOrReplaceChild("bb_main", CubeListBuilder.create().texOffs(-24, -12).addBox(-7.0F, 0.0F, -7.0F, 14.0F, 4.0F, 14.0F, new CubeDeformation(0.0F))
					.texOffs(-8, 0).addBox(-5.0F, 10.0F, -5.0F, 10.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
					.texOffs(-15, -5).addBox(-5.0F, 10.0F, -4.0F, 2.0F, 2.0F, 7.0F, new CubeDeformation(0.0F))
					.texOffs(-8, 0).addBox(-5.0F, 10.0F, 3.0F, 10.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
					.texOffs(-14, -4).addBox(3.0F, 10.0F, -3.0F, 2.0F, 2.0F, 6.0F, new CubeDeformation(0.0F))
					.texOffs(0, 0).addBox(3.0F, 8.0F, -5.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
					.texOffs(0, 0).addBox(3.0F, 8.0F, 3.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
					.texOffs(0, 0).addBox(-5.0F, 8.0F, 3.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
					.texOffs(0, 0).addBox(-5.0F, 8.0F, -5.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
					.texOffs(-20, -10).addBox(-6.0F, 4.0F, -6.0F, 12.0F, 4.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F)),
			partDefinition -> partDefinition.addOrReplaceChild("bb_main", CubeListBuilder.create().texOffs(-24, -12).addBox(-7.0F, 0.0F, -7.0F, 14.0F, 4.0F, 14.0F, new CubeDeformation(0.0F))
					.texOffs(-4, 1).addBox(-2.0F, 14.0F, -2.0F, 4.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
					.texOffs(-9, 0).addBox(-2.0F, 14.0F, -1.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
					.texOffs(-4, 1).addBox(-2.0F, 14.0F, 1.0F, 4.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
					.texOffs(-10, 0).addBox(1.0F, 14.0F, -1.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
					.texOffs(2, 1).addBox(1.0F, 12.0F, -2.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
					.texOffs(2, 1).addBox(1.0F, 12.0F, 1.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
					.texOffs(1, 1).addBox(-2.0F, 12.0F, 1.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
					.texOffs(1, 1).addBox(-2.0F, 12.0F, -2.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
					.texOffs(-20, -10).addBox(-6.0F, 4.0F, -6.0F, 12.0F, 4.0F, 12.0F, new CubeDeformation(0.0F))
					.texOffs(-16, -8).addBox(-5.0F, 8.0F, -5.0F, 10.0F, 4.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F)),
			partDefinition -> partDefinition.addOrReplaceChild("bb_main", CubeListBuilder.create().texOffs(-24, -12).addBox(-7.0F, 0.0F, -7.0F, 14.0F, 4.0F, 14.0F, new CubeDeformation(0.0F))
					.texOffs(-6, 1).addBox(-3.0F, 16.0F, -4.0F, 6.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
					.texOffs(-15, -6).addBox(-4.0F, 16.0F, -4.0F, 1.0F, 1.0F, 8.0F, new CubeDeformation(0.0F))
					.texOffs(-6, 1).addBox(-3.0F, 16.0F, 3.0F, 6.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
					.texOffs(-16, -6).addBox(3.0F, 16.0F, -4.0F, 1.0F, 1.0F, 8.0F, new CubeDeformation(0.0F))
					.texOffs(-20, -10).addBox(-6.0F, 4.0F, -6.0F, 12.0F, 4.0F, 12.0F, new CubeDeformation(0.0F))
					.texOffs(-16, -8).addBox(-5.0F, 8.0F, -5.0F, 10.0F, 4.0F, 10.0F, new CubeDeformation(0.0F))
					.texOffs(-7, -2).addBox(-2.0F, 12.0F, -2.0F, 4.0F, 3.0F, 4.0F, new CubeDeformation(0.0F))
					.texOffs(-1, -1).addBox(-2.0F, 15.0F, -4.0F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F))
					.texOffs(0, 1).addBox(1.0F, 15.0F, -2.0F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
					.texOffs(1, 1).addBox(-4.0F, 15.0F, 1.0F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
					.texOffs(0, -1).addBox(1.0F, 15.0F, 1.0F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F)),
			partDefinition -> partDefinition.addOrReplaceChild("bb_main", CubeListBuilder.create().texOffs(-24, -12).addBox(-7.0F, 0.0F, -7.0F, 14.0F, 4.0F, 14.0F, new CubeDeformation(0.0F))
					.texOffs(-20, -10).addBox(-6.0F, 4.0F, -6.0F, 12.0F, 4.0F, 12.0F, new CubeDeformation(0.0F))
					.texOffs(-16, -8).addBox(-5.0F, 8.0F, -5.0F, 10.0F, 4.0F, 10.0F, new CubeDeformation(0.0F))
					.texOffs(-7, -2).addBox(-2.0F, 12.0F, -2.0F, 4.0F, 3.0F, 4.0F, new CubeDeformation(0.0F))
					.texOffs(-15, -6).addBox(-4.0F, 15.0F, -4.0F, 8.0F, 2.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F))
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
		return new ResourceLocation(CreateMissiles.MOD_ID,"textures/entity/gunpowder_thruster.png");
	}

	@Override
	public int getStageCount() {
		return layers.size();
	}
}