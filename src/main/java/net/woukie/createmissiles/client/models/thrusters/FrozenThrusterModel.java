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

public class FrozenThrusterModel implements MissilePartModel {
	private final List<Map<String, Vector3f>> attachments = List.of(
			new HashMap<>() {{
				put("bottom", new Vector3f(0, 0, 0));
				put("top", new Vector3f(0, 0, 0));
			}},
			new HashMap<>() {{
				put("bottom", new Vector3f(0, 0, 0));
				put("top", new Vector3f(0, 6.5f, 0));
			}},
			new HashMap<>() {{
				put("bottom", new Vector3f(0, 0, 0));
				put("top", new Vector3f(0, 6.5f, 0));
			}},
			new HashMap<>() {{
				put("bottom", new Vector3f(0, 0, 0));
				put("top", new Vector3f(0, 6.5f, 0));
			}},
			new HashMap<>() {{
				put("bottom", new Vector3f(0, 0, 0));
				put("top", new Vector3f(0, 6.5f, 0));
			}}
	);

	private final List<Function<PartDefinition, PartDefinition>> layers = List.of(
			partDefinition -> partDefinition.addOrReplaceChild("bb_main", CubeListBuilder.create(), PartPose.ZERO),
			partDefinition -> {
				PartDefinition thruster_top = partDefinition.addOrReplaceChild("thruster_top", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, -0.9F, -2.0F, 4.0F, 2.0F, 4.0F, new CubeDeformation(0.0F))
						.texOffs(0, 6).addBox(-2.5F, -0.4F, -2.5F, 5.0F, 1.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 2.9F, 0.0F));
				PartDefinition thruster_bottom = partDefinition.addOrReplaceChild("thruster_bottom", CubeListBuilder.create().texOffs(12, 0).addBox(-1.5F, 0.4F, -1.5F, 3.0F, 1.0F, 3.0F, new CubeDeformation(0.0F))
						.texOffs(21, 0).addBox(0.5F, -0.6F, -0.5F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
						.texOffs(21, 0).addBox(-1.5F, -0.6F, -1.5F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
						.texOffs(25, 0).addBox(-0.5F, -0.6F, -1.5F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
						.texOffs(25, 0).addBox(-1.5F, -0.6F, 0.5F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.6F, 0.0F));
				PartDefinition fins = partDefinition.addOrReplaceChild("fins", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, -0.1F, 2.5F, -2.8362F, 0.0F, 3.1416F));
				PartDefinition fins3 = partDefinition.addOrReplaceChild("fins3", CubeListBuilder.create(), PartPose.offsetAndRotation(-2.2F, -0.1F, -1.8F, 0.3054F, 1.0472F, 0.0F));
				PartDefinition fins2 = partDefinition.addOrReplaceChild("fins2", CubeListBuilder.create(), PartPose.offsetAndRotation(2.15F, -0.1F, -1.8134F, 0.3054F, -1.0472F, 0.0F));
				return partDefinition;
			},
			partDefinition -> {
				PartDefinition thruster_top = partDefinition.addOrReplaceChild("thruster_top", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, -0.9F, -2.0F, 4.0F, 2.0F, 4.0F, new CubeDeformation(0.0F))
						.texOffs(0, 6).addBox(-2.5F, -0.4F, -2.5F, 5.0F, 1.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 5.4F, 0.0F));
				PartDefinition thruster_bottom = partDefinition.addOrReplaceChild("thruster_bottom", CubeListBuilder.create().texOffs(12, 0).addBox(-1.5F, 0.4F, -1.5F, 3.0F, 1.0F, 3.0F, new CubeDeformation(0.0F))
						.texOffs(21, 0).addBox(0.5F, -0.6F, -0.5F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
						.texOffs(21, 0).addBox(-1.5F, -0.6F, -1.5F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
						.texOffs(25, 0).addBox(-0.5F, -0.6F, -1.5F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
						.texOffs(25, 0).addBox(-1.5F, -0.6F, 0.5F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 3.1F, 0.0F));
				PartDefinition fins = partDefinition.addOrReplaceChild("fins", CubeListBuilder.create().texOffs(20, 4).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F))
						.texOffs(24, 3).addBox(-1.0F, -2.5F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 2.4F, 2.5F, -2.8362F, 0.0F, 3.1416F));
				PartDefinition fins3 = partDefinition.addOrReplaceChild("fins3", CubeListBuilder.create(), PartPose.offsetAndRotation(-2.2F, 2.4F, -1.8F, 0.3054F, 1.0472F, 0.0F));
				PartDefinition fins2 = partDefinition.addOrReplaceChild("fins2", CubeListBuilder.create(), PartPose.offsetAndRotation(2.15F, 2.4F, -1.8134F, 0.3054F, -1.0472F, 0.0F));
				return partDefinition;
			},
			partDefinition -> {
				PartDefinition thruster_top = partDefinition.addOrReplaceChild("thruster_top", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, -0.9F, -2.0F, 4.0F, 2.0F, 4.0F, new CubeDeformation(0.0F))
						.texOffs(0, 6).addBox(-2.5F, -0.4F, -2.5F, 5.0F, 1.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 5.4F, 0.0F));
				PartDefinition thruster_bottom = partDefinition.addOrReplaceChild("thruster_bottom", CubeListBuilder.create().texOffs(12, 0).addBox(-1.5F, 0.4F, -1.5F, 3.0F, 1.0F, 3.0F, new CubeDeformation(0.0F))
						.texOffs(21, 0).addBox(0.5F, -0.6F, -0.5F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
						.texOffs(21, 0).addBox(-1.5F, -0.6F, -1.5F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
						.texOffs(25, 0).addBox(-0.5F, -0.6F, -1.5F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
						.texOffs(25, 0).addBox(-1.5F, -0.6F, 0.5F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 3.1F, 0.0F));
				PartDefinition fins = partDefinition.addOrReplaceChild("fins", CubeListBuilder.create().texOffs(20, 4).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F))
						.texOffs(24, 3).addBox(-1.0F, -2.5F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 2.4F, 2.5F, -2.8362F, 0.0F, 3.1416F));
				PartDefinition fins3 = partDefinition.addOrReplaceChild("fins3", CubeListBuilder.create().texOffs(20, 4).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F))
						.texOffs(24, 3).addBox(-1.0F, -2.5F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.2F, 2.4F, -1.8F, 0.3054F, 1.0472F, 0.0F));
				PartDefinition fins2 = partDefinition.addOrReplaceChild("fins2", CubeListBuilder.create(), PartPose.offsetAndRotation(2.15F, 2.4F, -1.8134F, 0.3054F, -1.0472F, 0.0F));
				return partDefinition;
			},
			partDefinition -> {
				PartDefinition thruster_top = partDefinition.addOrReplaceChild("thruster_top", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, -0.9F, -2.0F, 4.0F, 2.0F, 4.0F, new CubeDeformation(0.0F))
						.texOffs(0, 6).addBox(-2.5F, -0.4F, -2.5F, 5.0F, 1.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 5.4F, 0.0F));
				PartDefinition thruster_bottom = partDefinition.addOrReplaceChild("thruster_bottom", CubeListBuilder.create().texOffs(12, 0).addBox(-1.5F, 0.4F, -1.5F, 3.0F, 1.0F, 3.0F, new CubeDeformation(0.0F))
						.texOffs(21, 0).addBox(0.5F, -0.6F, -0.5F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
						.texOffs(21, 0).addBox(-1.5F, -0.6F, -1.5F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
						.texOffs(25, 0).addBox(-0.5F, -0.6F, -1.5F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
						.texOffs(25, 0).addBox(-1.5F, -0.6F, 0.5F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 3.1F, 0.0F));
				PartDefinition fins = partDefinition.addOrReplaceChild("fins", CubeListBuilder.create().texOffs(20, 4).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F))
						.texOffs(24, 3).addBox(-1.0F, -2.5F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 2.4F, 2.5F, -2.8362F, 0.0F, 3.1416F));
				PartDefinition fins3 = partDefinition.addOrReplaceChild("fins3", CubeListBuilder.create().texOffs(20, 4).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F))
						.texOffs(24, 3).addBox(-1.0F, -2.5F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.2F, 2.4F, -1.8F, 0.3054F, 1.0472F, 0.0F));
				PartDefinition fins2 = partDefinition.addOrReplaceChild("fins2", CubeListBuilder.create().texOffs(20, 4).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F))
						.texOffs(24, 3).addBox(-1.0F, -2.5F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.15F, 2.4F, -1.8134F, 0.3054F, -1.0472F, 0.0F));
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
		PartDefinition partDefinition = meshdefinition.getRoot();
		layers.get(stage).apply(partDefinition);
		return LayerDefinition.create(meshdefinition, 32, 12);
	}

	@Override
	public ResourceLocation getTexture(int stage) {
		return ResourceLocation.fromNamespaceAndPath(CreateMissiles.MOD_ID,"textures/entity/frozen_thruster.png");
	}

	@Override
	public int getStageCount() {
		return layers.size();
	}
}