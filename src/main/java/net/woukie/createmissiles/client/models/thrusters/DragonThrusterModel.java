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

public class DragonThrusterModel implements MissilePartModel {
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
				PartDefinition shard2_r1 = bb_main.addOrReplaceChild("shard2_r1", CubeListBuilder.create().texOffs(16, 0).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.2007F, 6.0782F, 1.2706F, -0.3927F, -1.0472F, 3.1416F));
				PartDefinition base2_r1 = bb_main.addOrReplaceChild("base2_r1", CubeListBuilder.create().texOffs(12, 0).addBox(-2.5F, -0.5F, -0.5F, 5.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.7321F, 7.5F, 1.0F, 3.1416F, 1.0472F, 0.0F));
				PartDefinition shard1_r1 = bb_main.addOrReplaceChild("shard1_r1", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, -3.5F, -1.0F, 4.0F, 7.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.7036F, 3.6924F, 0.9836F, 0.3927F, -1.0472F, -3.1416F));
				PartDefinition base1_r1 = bb_main.addOrReplaceChild("base1_r1", CubeListBuilder.create().texOffs(12, 5).addBox(-2.5F, -0.5F, -2.5F, 5.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.7321F, 0.5F, 1.0F, 3.1416F, 1.0472F, 0.0F));
				return bb_main;
			},
			partDefinition -> {
				PartDefinition bb_main = partDefinition.addOrReplaceChild("bb_main", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));
				PartDefinition shard3_r1 = bb_main.addOrReplaceChild("shard3_r1", CubeListBuilder.create().texOffs(16, 0).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.2007F, 6.0782F, 1.2706F, -0.3927F, 1.0472F, 3.1416F));
				PartDefinition shard2_r1 = bb_main.addOrReplaceChild("shard2_r1", CubeListBuilder.create().texOffs(16, 0).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.2007F, 6.0782F, 1.2706F, -0.3927F, -1.0472F, 3.1416F));
				PartDefinition base2_r1 = bb_main.addOrReplaceChild("base2_r1", CubeListBuilder.create().texOffs(12, 0).addBox(-2.5F, -0.5F, -0.5F, 5.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.7321F, 7.5F, 1.0F, 3.1416F, 1.0472F, 0.0F));
				PartDefinition base3_r1 = bb_main.addOrReplaceChild("base3_r1", CubeListBuilder.create().texOffs(12, 0).addBox(-2.5F, -0.5F, -0.5F, 5.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.7321F, 7.5F, 1.0F, 3.1416F, -1.0472F, 0.0F));
				PartDefinition shard1_r1 = bb_main.addOrReplaceChild("shard1_r1", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, -3.5F, -1.0F, 4.0F, 7.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.7036F, 3.6924F, 0.9836F, 0.3927F, -1.0472F, -3.1416F));
				PartDefinition shard2_r2 = bb_main.addOrReplaceChild("shard2_r2", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, -3.5F, -1.0F, 4.0F, 7.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.7036F, 3.6924F, 0.9836F, 0.3927F, 1.0472F, -3.1416F));
				PartDefinition base2_r2 = bb_main.addOrReplaceChild("base2_r2", CubeListBuilder.create().texOffs(12, 5).addBox(-2.5F, -0.5F, -2.5F, 5.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.7321F, 0.5F, 1.0F, 3.1416F, -1.0472F, 0.0F));
				PartDefinition base1_r1 = bb_main.addOrReplaceChild("base1_r1", CubeListBuilder.create().texOffs(12, 5).addBox(-2.5F, -0.5F, -2.5F, 5.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.7321F, 0.5F, 1.0F, 3.1416F, 1.0472F, 0.0F));
				return bb_main;
			},
			partDefinition -> {
				PartDefinition bb_main = partDefinition.addOrReplaceChild("bb_main", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));
				PartDefinition shard4_r1 = bb_main.addOrReplaceChild("shard4_r1", CubeListBuilder.create().texOffs(16, 0).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 6.0782F, -2.5412F, 2.7489F, 0.0F, 0.0F));
				PartDefinition shard3_r1 = bb_main.addOrReplaceChild("shard3_r1", CubeListBuilder.create().texOffs(16, 0).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.2007F, 6.0782F, 1.2706F, -0.3927F, 1.0472F, 3.1416F));
				PartDefinition shard2_r1 = bb_main.addOrReplaceChild("shard2_r1", CubeListBuilder.create().texOffs(16, 0).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.2007F, 6.0782F, 1.2706F, -0.3927F, -1.0472F, 3.1416F));
				PartDefinition base2_r1 = bb_main.addOrReplaceChild("base2_r1", CubeListBuilder.create().texOffs(12, 0).addBox(-2.5F, -0.5F, -0.5F, 5.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.7321F, 7.5F, 1.0F, 3.1416F, 1.0472F, 0.0F));
				PartDefinition base3_r1 = bb_main.addOrReplaceChild("base3_r1", CubeListBuilder.create().texOffs(12, 0).addBox(-2.5F, -0.5F, -0.5F, 5.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.7321F, 7.5F, 1.0F, 3.1416F, -1.0472F, 0.0F));
				PartDefinition base4_r1 = bb_main.addOrReplaceChild("base4_r1", CubeListBuilder.create().texOffs(12, 0).addBox(-2.5F, -0.5F, -0.5F, 5.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 7.5F, -2.0F, 0.0F, 0.0F, 3.1416F));
				PartDefinition shard1_r1 = bb_main.addOrReplaceChild("shard1_r1", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, -3.5F, -1.0F, 4.0F, 7.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.7036F, 3.6924F, 0.9836F, 0.3927F, -1.0472F, -3.1416F));
				PartDefinition shard3_r2 = bb_main.addOrReplaceChild("shard3_r2", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, -3.5F, -1.0F, 4.0F, 7.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 3.6924F, -1.9672F, -2.7489F, 0.0F, 0.0F));
				PartDefinition shard2_r2 = bb_main.addOrReplaceChild("shard2_r2", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, -3.5F, -1.0F, 4.0F, 7.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.7036F, 3.6924F, 0.9836F, 0.3927F, 1.0472F, -3.1416F));
				PartDefinition base2_r2 = bb_main.addOrReplaceChild("base2_r2", CubeListBuilder.create().texOffs(12, 5).addBox(-2.5F, -0.5F, -2.5F, 5.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.7321F, 0.5F, 1.0F, 3.1416F, -1.0472F, 0.0F));
				PartDefinition base3_r2 = bb_main.addOrReplaceChild("base3_r2", CubeListBuilder.create().texOffs(12, 5).addBox(-2.5F, -0.5F, -2.5F, 5.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.5F, -2.0F, 0.0F, 0.0F, 3.1416F));
				PartDefinition base1_r1 = bb_main.addOrReplaceChild("base1_r1", CubeListBuilder.create().texOffs(12, 5).addBox(-2.5F, -0.5F, -2.5F, 5.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.7321F, 0.5F, 1.0F, 3.1416F, 1.0472F, 0.0F));
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
		return LayerDefinition.create(meshdefinition, 28, 9);
	}

	@Override
	public ResourceLocation getTexture(int stage) {
		return ResourceLocation.fromNamespaceAndPath(CreateMissiles.MOD_ID,"textures/entity/dragon_thruster.png");
	}

	@Override
	public int getStageCount() {
		return layers.size();
	}
}