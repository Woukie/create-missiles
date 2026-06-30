package net.woukie.createmissiles.client.models.chassis;

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

public class FrozenChassisModel implements MissilePartModel {
	private final List<Map<String, Vector3f>> attachments = List.of(
			new HashMap<>() {{
				put("bottom", new Vector3f(0, 0, 0));
				put("top", new Vector3f(0, 0, 0));
			}},
			new HashMap<>() {{
				put("bottom", new Vector3f(0, 0, 0));
				put("top", new Vector3f(0, 2, 0));
			}},
			new HashMap<>() {{
				put("bottom", new Vector3f(0, 0, 0));
				put("top", new Vector3f(0, 4, 0));
			}},
			new HashMap<>() {{
				put("bottom", new Vector3f(0, 0, 0));
				put("top", new Vector3f(0, 6, 0));
			}},
			new HashMap<>() {{
				put("bottom", new Vector3f(0, 0, 0));
				put("top", new Vector3f(0, 6, 0));
			}}
	);

	private final List<Function<PartDefinition, PartDefinition>> layers = List.of(
			partDefinition -> partDefinition.addOrReplaceChild("bb_main", CubeListBuilder.create(), PartPose.ZERO),
			partDefinition -> {
				PartDefinition chassis_bottom = partDefinition.addOrReplaceChild("chassis_bottom", CubeListBuilder.create().texOffs(0, 7).addBox(-2.0F, 1.0F, -4.0F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.5F, 0.0F, 0.5F));

				PartDefinition body_r1 = chassis_bottom.addOrReplaceChild("body_r1", CubeListBuilder.create().texOffs(0, 0).addBox(-2.5F, -1.0F, -2.5F, 5.0F, 2.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5F, 1.0F, -0.5F, 0.0F, 0.0F, -3.1416F));

				PartDefinition chassis_middle = partDefinition.addOrReplaceChild("chassis_middle", CubeListBuilder.create(), PartPose.offset(0.5F, 0.0F, 0.5F));

				PartDefinition chassis_top = partDefinition.addOrReplaceChild("chassis_top", CubeListBuilder.create(), PartPose.offset(0.5F, 0.0F, 0.5F));

				PartDefinition snow_big = partDefinition.addOrReplaceChild("snow_big", CubeListBuilder.create(), PartPose.offset(-2.0F, 2.0F, 2.0F));

				PartDefinition snow_small = partDefinition.addOrReplaceChild("snow_small", CubeListBuilder.create(), PartPose.offset(0.5F, 0.0F, 0.5F));
				return partDefinition;
			},
			partDefinition -> {
				PartDefinition chassis_bottom = partDefinition.addOrReplaceChild("chassis_bottom", CubeListBuilder.create().texOffs(0, 7).addBox(-2.0F, 1.025F, -4.0F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.5F, 0.0F, 0.5F));

				PartDefinition body_r1 = chassis_bottom.addOrReplaceChild("body_r1", CubeListBuilder.create().texOffs(0, 0).addBox(-2.5F, -1.0F, -2.5F, 5.0F, 2.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5F, 1.0F, -0.5F, 0.0F, 0.0F, -3.1416F));

				PartDefinition chassis_middle = partDefinition.addOrReplaceChild("chassis_middle", CubeListBuilder.create().texOffs(0, 9).addBox(-2.0F, 2.025F, -4.0F, 3.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.5F, 0.0F, 0.5F));

				PartDefinition body_r2 = chassis_middle.addOrReplaceChild("body_r2", CubeListBuilder.create().texOffs(2, 7).addBox(-3.0F, -1.0F, -3.0F, 6.0F, 2.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5F, 3.0F, -0.5F, 0.0F, 0.0F, -3.1416F));

				PartDefinition chassis_top = partDefinition.addOrReplaceChild("chassis_top", CubeListBuilder.create(), PartPose.offset(0.5F, 0.0F, 0.5F));

				PartDefinition snow_big = partDefinition.addOrReplaceChild("snow_big", CubeListBuilder.create(), PartPose.offset(-2.0F, 2.0F, 2.0F));

				PartDefinition snow_small = partDefinition.addOrReplaceChild("snow_small", CubeListBuilder.create(), PartPose.offset(0.5F, 0.0F, 0.5F));
				return partDefinition;
			},
			partDefinition -> {
				PartDefinition chassis_bottom = partDefinition.addOrReplaceChild("chassis_bottom", CubeListBuilder.create().texOffs(0, 7).addBox(-2.0F, 1.0F, -4.0F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.5F, 0.0F, 0.5F));

				PartDefinition body_r1 = chassis_bottom.addOrReplaceChild("body_r1", CubeListBuilder.create().texOffs(0, 0).addBox(-2.5F, -1.0F, -2.5F, 5.0F, 2.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5F, 1.0F, -0.5F, 0.0F, 0.0F, -3.1416F));

				PartDefinition chassis_middle = partDefinition.addOrReplaceChild("chassis_middle", CubeListBuilder.create().texOffs(0, 9).addBox(-2.0F, 2.0F, -4.0F, 3.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.5F, 0.0F, 0.5F));

				PartDefinition body_r2 = chassis_middle.addOrReplaceChild("body_r2", CubeListBuilder.create().texOffs(2, 7).addBox(-3.0F, -1.0F, -3.0F, 6.0F, 2.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5F, 3.0F, -0.5F, 0.0F, 0.0F, -3.1416F));

				PartDefinition chassis_top = partDefinition.addOrReplaceChild("chassis_top", CubeListBuilder.create().texOffs(0, 0).addBox(1.0F, 6.0F, 1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
						.texOffs(0, 0).addBox(0.0F, 6.0F, 0.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
						.texOffs(0, 0).addBox(0.0F, 6.0F, -2.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
						.texOffs(0, 0).addBox(1.0F, 6.0F, -3.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
						.texOffs(0, 0).addBox(-3.0F, 6.0F, -3.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
						.texOffs(0, 0).addBox(-2.0F, 6.0F, -2.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
						.texOffs(0, 0).addBox(-3.0F, 6.0F, 1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
						.texOffs(0, 0).addBox(-2.0F, 6.0F, 0.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.5F, 0.0F, 0.5F));

				PartDefinition body_r3 = chassis_top.addOrReplaceChild("body_r3", CubeListBuilder.create().texOffs(0, 15).addBox(-3.5F, -1.0F, -3.5F, 7.0F, 2.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5F, 5.0F, -0.5F, 0.0F, 0.0F, -3.1416F));

				PartDefinition snow_big = partDefinition.addOrReplaceChild("snow_big", CubeListBuilder.create(), PartPose.offset(-2.0F, 2.0F, 2.0F));

				PartDefinition snow_small = partDefinition.addOrReplaceChild("snow_small", CubeListBuilder.create(), PartPose.offset(0.5F, 0.0F, 0.5F));
				return partDefinition;
			},
			partDefinition -> {
				PartDefinition chassis_bottom = partDefinition.addOrReplaceChild("chassis_bottom", CubeListBuilder.create().texOffs(0, 7).addBox(-2.0F, 1.0F, -4.0F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.5F, 0.0F, 0.5F));

				PartDefinition body_r1 = chassis_bottom.addOrReplaceChild("body_r1", CubeListBuilder.create().texOffs(0, 0).addBox(-2.5F, -1.0F, -2.5F, 5.0F, 2.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5F, 1.0F, -0.5F, 0.0F, 0.0F, -3.1416F));

				PartDefinition chassis_middle = partDefinition.addOrReplaceChild("chassis_middle", CubeListBuilder.create().texOffs(0, 9).addBox(-2.0F, 2.0F, -4.0F, 3.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.5F, 0.0F, 0.5F));

				PartDefinition body_r2 = chassis_middle.addOrReplaceChild("body_r2", CubeListBuilder.create().texOffs(2, 7).addBox(-3.0F, -1.0F, -3.0F, 6.0F, 2.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5F, 3.0F, -0.5F, 0.0F, 0.0F, -3.1416F));

				PartDefinition chassis_top = partDefinition.addOrReplaceChild("chassis_top", CubeListBuilder.create().texOffs(0, 0).addBox(1.0F, 6.0F, 1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
						.texOffs(0, 0).addBox(0.0F, 6.0F, 0.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
						.texOffs(0, 0).addBox(0.0F, 6.0F, -2.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
						.texOffs(0, 0).addBox(1.0F, 6.0F, -3.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
						.texOffs(0, 0).addBox(-3.0F, 6.0F, -3.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
						.texOffs(0, 0).addBox(-2.0F, 6.0F, -2.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
						.texOffs(0, 0).addBox(-3.0F, 6.0F, 1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
						.texOffs(0, 0).addBox(-2.0F, 6.0F, 0.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.5F, 0.0F, 0.5F));

				PartDefinition body_r3 = chassis_top.addOrReplaceChild("body_r3", CubeListBuilder.create().texOffs(0, 15).addBox(-3.5F, -1.0F, -3.5F, 7.0F, 2.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5F, 5.0F, -0.5F, 0.0F, 0.0F, -3.1416F));

				PartDefinition snow_big = partDefinition.addOrReplaceChild("snow_big", CubeListBuilder.create(), PartPose.offset(-2.0F, 2.0F, 2.0F));

				PartDefinition cube_r1 = snow_big.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(24, 0).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.5803F, 4.6287F, 0.8702F, -0.6123F, -0.4318F, -0.5412F));

				PartDefinition cube_r2 = snow_big.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(24, 0).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.5803F, 4.6287F, 0.8702F, -0.176F, -0.4318F, -0.5412F));

				PartDefinition cube_r3 = snow_big.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(24, 0).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(5.0803F, 4.6287F, -0.1298F, -0.2912F, -0.0876F, -0.6014F));

				PartDefinition cube_r4 = snow_big.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(24, 0).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(5.0803F, 4.6287F, -2.6298F, 0.2887F, -0.7459F, -0.3306F));

				PartDefinition cube_r5 = snow_big.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(24, 0).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(5.0803F, 4.6287F, -4.6297F, -0.0539F, -0.4737F, 0.1542F));

				PartDefinition cube_r6 = snow_big.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(24, 0).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.8732F, 4.5929F, -4.5505F, -0.481F, -0.2391F, 0.0929F));

				PartDefinition cube_r7 = snow_big.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(24, 0).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.7351F, 4.5427F, -3.9214F, 0.4386F, 0.1247F, 0.4368F));

				PartDefinition cube_r8 = snow_big.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(24, 0).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.5096F, 4.5106F, -5.0856F, 0.632F, -0.1324F, -0.0845F));

				PartDefinition cube_r9 = snow_big.addOrReplaceChild("cube_r9", CubeListBuilder.create().texOffs(24, 0).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.1051F, 4.6333F, -2.8107F, -0.2022F, -0.341F, -0.2475F));

				PartDefinition cube_r10 = snow_big.addOrReplaceChild("cube_r10", CubeListBuilder.create().texOffs(24, 0).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.8283F, 4.6914F, -1.1274F, 0.1279F, -0.0415F, -0.0338F));

				PartDefinition cube_r11 = snow_big.addOrReplaceChild("cube_r11", CubeListBuilder.create().texOffs(24, 0).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.0999F, 4.587F, 0.5492F, 0.48F, 0.2182F, 0.0F));

				PartDefinition snow_small = partDefinition.addOrReplaceChild("snow_small", CubeListBuilder.create().texOffs(26, 0).addBox(1.0F, 7.0F, 1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.5F, 0.0F, 0.5F));

				PartDefinition cube_r12 = snow_small.addOrReplaceChild("cube_r12", CubeListBuilder.create().texOffs(26, 0).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.2638F, 7.8128F, 1.817F, -0.5236F, 0.0F, -0.3491F));

				PartDefinition cube_r13 = snow_small.addOrReplaceChild("cube_r13", CubeListBuilder.create().texOffs(26, 0).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.3964F, 6.1124F, -3.75F, -0.764F, -0.6613F, -1.0985F));

				PartDefinition cube_r14 = snow_small.addOrReplaceChild("cube_r14", CubeListBuilder.create().texOffs(26, 0).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.6464F, 6.1124F, 3.25F, -0.764F, -0.6613F, -1.0985F));

				PartDefinition cube_r15 = snow_small.addOrReplaceChild("cube_r15", CubeListBuilder.create().texOffs(26, 0).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.6465F, 6.1124F, 1.75F, 0.1243F, -0.8273F, -2.2494F));

				PartDefinition cube_r16 = snow_small.addOrReplaceChild("cube_r16", CubeListBuilder.create().texOffs(26, 0).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.6465F, 6.1124F, -3.25F, -0.898F, 0.1869F, 0.227F));

				PartDefinition cube_r17 = snow_small.addOrReplaceChild("cube_r17", CubeListBuilder.create().texOffs(26, 0).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.1465F, 7.1124F, -2.75F, -0.2438F, -0.1945F, 0.3212F));

				PartDefinition cube_r18 = snow_small.addOrReplaceChild("cube_r18", CubeListBuilder.create().texOffs(26, 0).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.1464F, 7.6124F, -2.75F, 0.1004F, -0.4434F, 0.5647F));

				PartDefinition cube_r19 = snow_small.addOrReplaceChild("cube_r19", CubeListBuilder.create().texOffs(26, 0).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.8535F, 7.6124F, -1.75F, 0.4219F, -0.1728F, -0.4097F));

				PartDefinition cube_r20 = snow_small.addOrReplaceChild("cube_r20", CubeListBuilder.create().texOffs(26, 0).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.1035F, 5.6124F, -2.25F, 0.9054F, -0.8714F, -1.193F));

				PartDefinition cube_r21 = snow_small.addOrReplaceChild("cube_r21", CubeListBuilder.create().texOffs(26, 0).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.1035F, 5.8624F, 0.25F, 0.0308F, -0.233F, 0.2115F));

				PartDefinition cube_r22 = snow_small.addOrReplaceChild("cube_r22", CubeListBuilder.create().texOffs(26, 0).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.6035F, 6.6124F, 0.25F, 0.4235F, -0.233F, 0.2115F));
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
		return LayerDefinition.create(meshdefinition, 32, 32);
	}

	@Override
	public ResourceLocation getTexture(int stage) {
		return ResourceLocation.fromNamespaceAndPath(CreateMissiles.MOD_ID,"textures/entity/frozen_chassis.png");
	}

	@Override
	public int getStageCount() {
		return layers.size();
	}
}