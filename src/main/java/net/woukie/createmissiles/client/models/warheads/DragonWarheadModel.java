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

public class DragonWarheadModel implements MissilePartModel {
	private final List<Map<String, Vector3f>> attachments = List.of(
			new HashMap<>() {{
				put("bottom", new Vector3f(0, 0, 0));
				put("top", new Vector3f(0, 0, 0));
			}},
			new HashMap<>() {{
				put("bottom", new Vector3f(0, 0, 0));
				put("top", new Vector3f(0, 7, 0));
			}},
			new HashMap<>() {{
				put("bottom", new Vector3f(0, 0, 0));
				put("top", new Vector3f(0, 7, 0));
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
				bb_main.addOrReplaceChild("base1_r1", CubeListBuilder.create().texOffs(7, 7).addBox(-2.5F, -0.5F, -2.5F, 5.0F, 1.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.732F, 0.5F, 1.0F, 3.1416F, 1.0472F, 0.0F));
				bb_main.addOrReplaceChild("braces1_r1", CubeListBuilder.create().texOffs(0, 6).addBox(-2.5F, -1.0F, -0.5F, 5.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.1972F, 6.0575F, 0.6912F, -2.1642F, 1.0472F, 0.0F));
				bb_main.addOrReplaceChild("braces1_r2", CubeListBuilder.create().texOffs(0, 6).addBox(-2.5F, -2.0F, -0.5F, 5.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.9321F, 3.0112F, 1.1155F, -2.1642F, 1.0472F, 0.0F));
				bb_main.addOrReplaceChild("shard1_r1", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, -1.0F, -2.0F, 4.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.732F, 2.0F, 1.0F, 3.1416F, 1.0472F, 0.0F));
				bb_main.addOrReplaceChild("shard1_r2", CubeListBuilder.create().texOffs(16, 0).addBox(-2.0F, -2.5F, -1.0F, 4.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.5354F, 4.5134F, 0.8865F, 2.5482F, 1.0472F, 0.0F));
				return partDefinition;
			},
			partDefinition -> {
				PartDefinition bb_main = partDefinition.addOrReplaceChild("bb_main", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));
				bb_main.addOrReplaceChild("base2_r1", CubeListBuilder.create().texOffs(7, 7).addBox(-2.5F, -0.5F, -2.5F, 5.0F, 1.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.732F, 0.5F, 1.0F, 3.1416F, -1.0472F, 0.0F));
				bb_main.addOrReplaceChild("base1_r1", CubeListBuilder.create().texOffs(7, 7).addBox(-2.5F, -0.5F, -2.5F, 5.0F, 1.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.732F, 0.5F, 1.0F, 3.1416F, 1.0472F, 0.0F));
				bb_main.addOrReplaceChild("braces1_r1", CubeListBuilder.create().texOffs(0, 6).addBox(-2.5F, -1.0F, -0.5F, 5.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.1972F, 6.0575F, 0.6912F, -2.1642F, 1.0472F, 0.0F));
				bb_main.addOrReplaceChild("braces1_r2", CubeListBuilder.create().texOffs(0, 6).addBox(-2.5F, -2.0F, -0.5F, 5.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.9321F, 3.0112F, 1.1155F, -2.1642F, 1.0472F, 0.0F));
				bb_main.addOrReplaceChild("braces2_r1", CubeListBuilder.create().texOffs(0, 6).addBox(-2.5F, -1.0F, -0.5F, 5.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.1972F, 6.0575F, 0.6912F, -2.1642F, -1.0472F, 0.0F));
				bb_main.addOrReplaceChild("braces2_r2", CubeListBuilder.create().texOffs(0, 6).addBox(-2.5F, -2.0F, -0.5F, 5.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.9321F, 3.0112F, 1.1155F, -2.1642F, -1.0472F, 0.0F));
				bb_main.addOrReplaceChild("shard2_r1", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, -1.0F, -2.0F, 4.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.732F, 2.0F, 1.0F, 3.1416F, -1.0472F, 0.0F));
				bb_main.addOrReplaceChild("shard1_r1", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, -1.0F, -2.0F, 4.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.732F, 2.0F, 1.0F, 3.1416F, 1.0472F, 0.0F));
				bb_main.addOrReplaceChild("shard2_r2", CubeListBuilder.create().texOffs(16, 0).addBox(-2.0F, -2.5F, -1.0F, 4.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.5354F, 4.5134F, 0.8865F, 2.5482F, -1.0472F, 0.0F));
				bb_main.addOrReplaceChild("shard1_r2", CubeListBuilder.create().texOffs(16, 0).addBox(-2.0F, -2.5F, -1.0F, 4.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.5354F, 4.5134F, 0.8865F, 2.5482F, 1.0472F, 0.0F));
				return partDefinition;
			},
			partDefinition -> {
				PartDefinition bb_main = partDefinition.addOrReplaceChild("bb_main", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));
				bb_main.addOrReplaceChild("base3_r1", CubeListBuilder.create().texOffs(7, 7).addBox(-2.5F, -0.5F, -2.5F, 5.0F, 1.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.5F, -2.0F, 0.0F, 0.0F, 3.1416F));
				bb_main.addOrReplaceChild("base2_r1", CubeListBuilder.create().texOffs(7, 7).addBox(-2.5F, -0.5F, -2.5F, 5.0F, 1.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.732F, 0.5F, 1.0F, 3.1416F, -1.0472F, 0.0F));
				bb_main.addOrReplaceChild("base1_r1", CubeListBuilder.create().texOffs(7, 7).addBox(-2.5F, -0.5F, -2.5F, 5.0F, 1.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.732F, 0.5F, 1.0F, 3.1416F, 1.0472F, 0.0F));
				bb_main.addOrReplaceChild("braces1_r1", CubeListBuilder.create().texOffs(0, 6).addBox(-2.5F, -1.0F, -0.5F, 5.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.1972F, 6.0575F, 0.6912F, -2.1642F, 1.0472F, 0.0F));
				bb_main.addOrReplaceChild("braces1_r2", CubeListBuilder.create().texOffs(0, 6).addBox(-2.5F, -2.0F, -0.5F, 5.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.9321F, 3.0112F, 1.1155F, -2.1642F, 1.0472F, 0.0F));
				bb_main.addOrReplaceChild("braces2_r1", CubeListBuilder.create().texOffs(0, 6).addBox(-2.5F, -1.0F, -0.5F, 5.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.1972F, 6.0575F, 0.6912F, -2.1642F, -1.0472F, 0.0F));
				bb_main.addOrReplaceChild("braces2_r2", CubeListBuilder.create().texOffs(0, 6).addBox(-2.5F, -2.0F, -0.5F, 5.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.9321F, 3.0112F, 1.1155F, -2.1642F, -1.0472F, 0.0F));
				bb_main.addOrReplaceChild("braces3_r1", CubeListBuilder.create().texOffs(0, 6).addBox(-2.5F, -2.0F, -0.5F, 5.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 3.0112F, -2.231F, 0.9774F, 0.0F, -3.1416F));
				bb_main.addOrReplaceChild("braces3_r2", CubeListBuilder.create().texOffs(0, 6).addBox(-2.5F, -1.0F, -0.5F, 5.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 6.0575F, -1.3824F, 0.9774F, 0.0F, -3.1416F));
				bb_main.addOrReplaceChild("shard2_r1", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, -1.0F, -2.0F, 4.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.732F, 2.0F, 1.0F, 3.1416F, -1.0472F, 0.0F));
				bb_main.addOrReplaceChild("shard1_r1", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, -1.0F, -2.0F, 4.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.732F, 2.0F, 1.0F, 3.1416F, 1.0472F, 0.0F));
				bb_main.addOrReplaceChild("shard3_r1", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, -1.0F, -2.0F, 4.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 2.0F, -2.0F, 0.0F, 0.0F, 3.1416F));
				bb_main.addOrReplaceChild("shard2_r2", CubeListBuilder.create().texOffs(16, 0).addBox(-2.0F, -2.5F, -1.0F, 4.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.5354F, 4.5134F, 0.8865F, 2.5482F, -1.0472F, 0.0F));
				bb_main.addOrReplaceChild("shard3_r2", CubeListBuilder.create().texOffs(16, 0).addBox(-2.0F, -2.5F, -1.0F, 4.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 4.5134F, -1.773F, -0.5934F, 0.0F, 3.1416F));
				bb_main.addOrReplaceChild("shard1_r2", CubeListBuilder.create().texOffs(16, 0).addBox(-2.0F, -2.5F, -1.0F, 4.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.5354F, 4.5134F, 0.8865F, 2.5482F, 1.0472F, 0.0F));
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
		return LayerDefinition.create(meshdefinition, 28, 13);
	}

	@Override
	public ResourceLocation getTexture(int stage) {
		return ResourceLocation.fromNamespaceAndPath(CreateMissiles.MOD_ID,"textures/entity/dragon_warhead.png");
	}

	@Override
	public int getStageCount() {
		return layers.size();
	}
}