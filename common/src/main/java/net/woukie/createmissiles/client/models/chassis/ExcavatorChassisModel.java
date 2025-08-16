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

public class ExcavatorChassisModel implements MissilePartModel {
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
				put("top", new Vector3f(0, 10, 0));
			}},
			new HashMap<>() {{
				put("bottom", new Vector3f(0, 0, 0));
				put("top", new Vector3f(0, 18, 0));
			}},
			new HashMap<>() {{
				put("bottom", new Vector3f(0, 0, 0));
				put("top", new Vector3f(0, 18, 0));
			}},
			new HashMap<>() {{
				put("bottom", new Vector3f(0, 0, 0));
				put("top", new Vector3f(0, 18, 0));
			}},
			new HashMap<>() {{
				put("bottom", new Vector3f(0, 0, 0));
				put("top", new Vector3f(0, 18, 0));
			}},
			new HashMap<>() {{
				put("bottom", new Vector3f(0, 0, 0));
				put("top", new Vector3f(0, 18, 0));
			}},
			new HashMap<>() {{
				put("bottom", new Vector3f(0, 0, 0));
				put("top", new Vector3f(0, 18, 0));
			}}
	);

	private final List<Function<PartDefinition, PartDefinition>> layers = List.of(
			partDefinition -> partDefinition.addOrReplaceChild("bb_main", CubeListBuilder.create(), PartPose.ZERO),
			partDefinition -> {
				PartDefinition bb_main = partDefinition.addOrReplaceChild("bb_main", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));
				bb_main.addOrReplaceChild("metal_r1", CubeListBuilder.create().texOffs(22, 0).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.6464F, 1.0F, -2.6464F, 0.0F, 0.7854F, 0.0F));
				bb_main.addOrReplaceChild("metal_r2", CubeListBuilder.create().texOffs(22, 0).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.6464F, 1.0F, 2.6464F, 0.0F, 0.7854F, 0.0F));
				bb_main.addOrReplaceChild("metal_r3", CubeListBuilder.create().texOffs(22, 0).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.6464F, 1.0F, -2.6464F, 0.0F, -0.7854F, 0.0F));
				bb_main.addOrReplaceChild("metal_r4", CubeListBuilder.create().texOffs(22, 0).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.6464F, 1.0F, 2.6464F, 0.0F, 0.7854F, 0.0F));
				bb_main.addOrReplaceChild("metal_r5", CubeListBuilder.create().texOffs(23, 1).addBox(-1.0F, -2.5F, -0.5F, 2.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 1.0F, -3.0F, 0.0F, -1.5708F, 1.5708F));
				bb_main.addOrReplaceChild("metal_r6", CubeListBuilder.create().texOffs(23, 1).addBox(-1.0F, -2.5F, -0.5F, 2.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.0F, 1.0F, 0.0F, -1.5708F, 0.0F, -3.1416F));
				bb_main.addOrReplaceChild("metal_r7", CubeListBuilder.create().texOffs(23, 1).addBox(-1.0F, -2.5F, -0.5F, 2.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 1.0F, 3.0F, 0.0F, -1.5708F, 1.5708F));
				bb_main.addOrReplaceChild("metal_r8", CubeListBuilder.create().texOffs(23, 1).addBox(-1.0F, -2.5F, -0.5F, 2.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.0F, 1.0F, 0.0F, -1.5708F, 0.0F, -3.1416F));
				return bb_main;
			},
			partDefinition -> {
				PartDefinition bb_main = partDefinition.addOrReplaceChild("bb_main", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));
				bb_main.addOrReplaceChild("metal_r1", CubeListBuilder.create().texOffs(23, 1).addBox(-1.0F, -2.5F, -0.5F, 2.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 9.0F, -3.0F, 0.0F, -1.5708F, 1.5708F));
				bb_main.addOrReplaceChild("metal_r2", CubeListBuilder.create().texOffs(23, 1).addBox(-1.0F, -2.5F, -0.5F, 2.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 9.0F, 3.0F, 0.0F, -1.5708F, 1.5708F));
				bb_main.addOrReplaceChild("metal_r3", CubeListBuilder.create().texOffs(23, 1).addBox(-1.0F, -2.5F, -0.5F, 2.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.0F, 9.0F, 0.0F, -1.5708F, 0.0F, -3.1416F));
				bb_main.addOrReplaceChild("metal_r4", CubeListBuilder.create().texOffs(23, 1).addBox(-1.0F, -2.5F, -0.5F, 2.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.0F, 9.0F, 0.0F, -1.5708F, 0.0F, -3.1416F));
				bb_main.addOrReplaceChild("metal_r5", CubeListBuilder.create().texOffs(23, 1).addBox(-1.0F, -2.5F, -0.5F, 2.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.0F, 1.0F, 0.0F, -1.5708F, 0.0F, -3.1416F));
				bb_main.addOrReplaceChild("metal_r6", CubeListBuilder.create().texOffs(23, 1).addBox(-1.0F, -2.5F, -0.5F, 2.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 1.0F, 3.0F, 0.0F, -1.5708F, 1.5708F));
				bb_main.addOrReplaceChild("metal_r7", CubeListBuilder.create().texOffs(23, 1).addBox(-1.0F, -2.5F, -0.5F, 2.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.0F, 1.0F, 0.0F, -1.5708F, 0.0F, -3.1416F));
				bb_main.addOrReplaceChild("metal_r8", CubeListBuilder.create().texOffs(23, 1).addBox(-1.0F, -2.5F, -0.5F, 2.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 1.0F, -3.0F, 0.0F, -1.5708F, 1.5708F));
				bb_main.addOrReplaceChild("metal_r9", CubeListBuilder.create().texOffs(22, 0).addBox(-1.0F, -5.0F, -1.0F, 2.0F, 10.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.6464F, 5.0F, 2.6464F, 0.0F, 0.7854F, 0.0F));
				bb_main.addOrReplaceChild("metal_r10", CubeListBuilder.create().texOffs(22, 0).addBox(-1.0F, -5.0F, -1.0F, 2.0F, 10.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.6464F, 5.0F, -2.6464F, 0.0F, -0.7854F, 0.0F));
				bb_main.addOrReplaceChild("metal_r11", CubeListBuilder.create().texOffs(22, 0).addBox(-1.0F, -5.0F, -1.0F, 2.0F, 10.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.6464F, 5.0F, 2.6464F, 0.0F, 0.7854F, 0.0F));
				bb_main.addOrReplaceChild("metal_r12", CubeListBuilder.create().texOffs(22, 0).addBox(-1.0F, -5.0F, -1.0F, 2.0F, 10.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.6464F, 5.0F, -2.6464F, 0.0F, 0.7854F, 0.0F));
				return bb_main;
			},
			partDefinition -> {
				PartDefinition bb_main = partDefinition.addOrReplaceChild("bb_main", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));
				bb_main.addOrReplaceChild("metal_r1", CubeListBuilder.create().texOffs(22, 0).addBox(-1.0F, -9.0F, -1.0F, 2.0F, 18.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.6464F, 9.0F, -2.6464F, 0.0F, 0.7854F, 0.0F));
				bb_main.addOrReplaceChild("metal_r2", CubeListBuilder.create().texOffs(22, 0).addBox(-1.0F, -9.0F, -1.0F, 2.0F, 18.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.6464F, 9.0F, 2.6464F, 0.0F, 0.7854F, 0.0F));
				bb_main.addOrReplaceChild("metal_r3", CubeListBuilder.create().texOffs(22, 0).addBox(-1.0F, -9.0F, -1.0F, 2.0F, 18.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.6464F, 9.0F, -2.6464F, 0.0F, -0.7854F, 0.0F));
				bb_main.addOrReplaceChild("metal_r4", CubeListBuilder.create().texOffs(23, 1).addBox(-1.0F, -2.5F, -0.5F, 2.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.0F, 17.0F, 0.0F, -1.5708F, 0.0F, -3.1416F));
				bb_main.addOrReplaceChild("metal_r5", CubeListBuilder.create().texOffs(23, 1).addBox(-1.0F, -2.5F, -0.5F, 2.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.0F, 17.0F, 0.0F, -1.5708F, 0.0F, -3.1416F));
				bb_main.addOrReplaceChild("metal_r6", CubeListBuilder.create().texOffs(23, 1).addBox(-1.0F, -2.5F, -0.5F, 2.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 17.0F, -3.0F, 0.0F, -1.5708F, 1.5708F));
				bb_main.addOrReplaceChild("metal_r7", CubeListBuilder.create().texOffs(23, 1).addBox(-1.0F, -2.5F, -0.5F, 2.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 17.0F, 3.0F, 0.0F, -1.5708F, 1.5708F));
				bb_main.addOrReplaceChild("metal_r8", CubeListBuilder.create().texOffs(22, 0).addBox(-1.0F, -9.0F, -1.0F, 2.0F, 18.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.6464F, 9.0F, 2.6464F, 0.0F, 0.7854F, 0.0F));
				bb_main.addOrReplaceChild("metal_r9", CubeListBuilder.create().texOffs(23, 1).addBox(-1.0F, -2.5F, -0.5F, 2.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 1.0F, -3.0F, 0.0F, -1.5708F, 1.5708F));
				bb_main.addOrReplaceChild("metal_r10", CubeListBuilder.create().texOffs(23, 1).addBox(-1.0F, -2.5F, -0.5F, 2.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.0F, 1.0F, 0.0F, -1.5708F, 0.0F, -3.1416F));
				bb_main.addOrReplaceChild("metal_r11", CubeListBuilder.create().texOffs(23, 1).addBox(-1.0F, -2.5F, -0.5F, 2.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 1.0F, 3.0F, 0.0F, -1.5708F, 1.5708F));
				bb_main.addOrReplaceChild("metal_r12", CubeListBuilder.create().texOffs(23, 1).addBox(-1.0F, -2.5F, -0.5F, 2.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.0F, 1.0F, 0.0F, -1.5708F, 0.0F, -3.1416F));
				return bb_main;
			},
			partDefinition -> {
				PartDefinition bb_main = partDefinition.addOrReplaceChild("bb_main", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));
				bb_main.addOrReplaceChild("metal_r1", CubeListBuilder.create().texOffs(23, 1).addBox(-1.0F, -2.5F, -0.5F, 2.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.0F, 1.0F, 0.0F, -1.5708F, 0.0F, -3.1416F));
				bb_main.addOrReplaceChild("metal_r2", CubeListBuilder.create().texOffs(23, 1).addBox(-1.0F, -2.5F, -0.5F, 2.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 1.0F, 3.0F, 0.0F, -1.5708F, 1.5708F));
				bb_main.addOrReplaceChild("metal_r3", CubeListBuilder.create().texOffs(23, 1).addBox(-1.0F, -2.5F, -0.5F, 2.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.0F, 1.0F, 0.0F, -1.5708F, 0.0F, -3.1416F));
				bb_main.addOrReplaceChild("metal_r4", CubeListBuilder.create().texOffs(23, 1).addBox(-1.0F, -2.5F, -0.5F, 2.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 1.0F, -3.0F, 0.0F, -1.5708F, 1.5708F));
				bb_main.addOrReplaceChild("metal_r5", CubeListBuilder.create().texOffs(22, 0).addBox(-1.0F, -9.0F, -1.0F, 2.0F, 18.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.6464F, 9.0F, 2.6464F, 0.0F, 0.7854F, 0.0F));
				bb_main.addOrReplaceChild("metal_r6", CubeListBuilder.create().texOffs(23, 1).addBox(-1.0F, -2.5F, -0.5F, 2.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 17.0F, 3.0F, 0.0F, -1.5708F, 1.5708F));
				bb_main.addOrReplaceChild("metal_r7", CubeListBuilder.create().texOffs(23, 1).addBox(-1.0F, -2.5F, -0.5F, 2.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 17.0F, -3.0F, 0.0F, -1.5708F, 1.5708F));
				bb_main.addOrReplaceChild("metal_r8", CubeListBuilder.create().texOffs(23, 1).addBox(-1.0F, -2.5F, -0.5F, 2.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.0F, 17.0F, 0.0F, -1.5708F, 0.0F, -3.1416F));
				bb_main.addOrReplaceChild("metal_r9", CubeListBuilder.create().texOffs(23, 1).addBox(-1.0F, -2.5F, -0.5F, 2.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.0F, 17.0F, 0.0F, -1.5708F, 0.0F, -3.1416F));
				bb_main.addOrReplaceChild("wood_r1", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, -1.0F, -0.5F, 4.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.5F, 3.0F, 0.0F, 1.5708F, 1.4835F, 1.5708F));
				bb_main.addOrReplaceChild("wood_r2", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, -1.0F, -0.5F, 4.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 3.0F, -2.5F, 0.0F, 0.0F, -0.0873F));
				bb_main.addOrReplaceChild("wood_r3", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, -1.0F, -0.5F, 4.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 3.0F, 2.5F, 0.0F, 0.0F, 0.1309F));
				bb_main.addOrReplaceChild("wood_r4", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, -1.0F, -0.5F, 4.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.5F, 3.0F, 0.0F, 1.5708F, 1.5272F, 1.5708F));
				bb_main.addOrReplaceChild("metal_r10", CubeListBuilder.create().texOffs(22, 0).addBox(-1.0F, -9.0F, -1.0F, 2.0F, 18.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.6464F, 9.0F, -2.6464F, 0.0F, -0.7854F, 0.0F));
				bb_main.addOrReplaceChild("metal_r11", CubeListBuilder.create().texOffs(22, 0).addBox(-1.0F, -9.0F, -1.0F, 2.0F, 18.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.6464F, 9.0F, 2.6464F, 0.0F, 0.7854F, 0.0F));
				bb_main.addOrReplaceChild("metal_r12", CubeListBuilder.create().texOffs(22, 0).addBox(-1.0F, -9.0F, -1.0F, 2.0F, 18.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.6464F, 9.0F, -2.6464F, 0.0F, 0.7854F, 0.0F));
				return bb_main;
			},
			partDefinition -> {
				PartDefinition bb_main = partDefinition.addOrReplaceChild("bb_main", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, 6.0F, -3.0F, 4.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
				bb_main.addOrReplaceChild("wood_r1", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, -1.0F, -0.5F, 4.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.5F, 7.0F, 0.0F, -1.5708F, 1.4399F, -1.5708F));
				bb_main.addOrReplaceChild("metal_r1", CubeListBuilder.create().texOffs(22, 0).addBox(-1.0F, -9.0F, -1.0F, 2.0F, 18.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.6464F, 9.0F, -2.6464F, 0.0F, 0.7854F, 0.0F));
				bb_main.addOrReplaceChild("metal_r2", CubeListBuilder.create().texOffs(22, 0).addBox(-1.0F, -9.0F, -1.0F, 2.0F, 18.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.6464F, 9.0F, 2.6464F, 0.0F, 0.7854F, 0.0F));
				bb_main.addOrReplaceChild("metal_r3", CubeListBuilder.create().texOffs(22, 0).addBox(-1.0F, -9.0F, -1.0F, 2.0F, 18.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.6464F, 9.0F, -2.6464F, 0.0F, -0.7854F, 0.0F));
				bb_main.addOrReplaceChild("wood_r2", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, -1.0F, -0.5F, 4.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.5F, 3.0F, 0.0F, 1.5708F, 1.5272F, 1.5708F));
				bb_main.addOrReplaceChild("wood_r3", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, -1.0F, -0.5F, 4.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 3.0F, 2.5F, 0.0F, 0.0F, 0.1309F));
				bb_main.addOrReplaceChild("wood_r4", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, -1.0F, -0.5F, 4.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 7.0F, 2.5F, 0.0F, 0.0F, 0.0873F));
				bb_main.addOrReplaceChild("wood_r5", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, -1.0F, -0.5F, 4.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 3.0F, -2.5F, 0.0F, 0.0F, -0.0873F));
				bb_main.addOrReplaceChild("wood_r6", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, -1.0F, -0.5F, 4.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.5F, 3.0F, 0.0F, 1.5708F, 1.4835F, 1.5708F));
				bb_main.addOrReplaceChild("wood_r7", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, -1.0F, -0.5F, 4.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.5F, 7.0F, 0.0F, 1.5708F, 1.4835F, 1.5708F));
				bb_main.addOrReplaceChild("metal_r4", CubeListBuilder.create().texOffs(23, 1).addBox(-1.0F, -2.5F, -0.5F, 2.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.0F, 17.0F, 0.0F, -1.5708F, 0.0F, -3.1416F));
				bb_main.addOrReplaceChild("metal_r5", CubeListBuilder.create().texOffs(23, 1).addBox(-1.0F, -2.5F, -0.5F, 2.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.0F, 17.0F, 0.0F, -1.5708F, 0.0F, -3.1416F));
				bb_main.addOrReplaceChild("metal_r6", CubeListBuilder.create().texOffs(23, 1).addBox(-1.0F, -2.5F, -0.5F, 2.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 17.0F, -3.0F, 0.0F, -1.5708F, 1.5708F));
				bb_main.addOrReplaceChild("metal_r7", CubeListBuilder.create().texOffs(23, 1).addBox(-1.0F, -2.5F, -0.5F, 2.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 17.0F, 3.0F, 0.0F, -1.5708F, 1.5708F));
				bb_main.addOrReplaceChild("metal_r8", CubeListBuilder.create().texOffs(22, 0).addBox(-1.0F, -9.0F, -1.0F, 2.0F, 18.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.6464F, 9.0F, 2.6464F, 0.0F, 0.7854F, 0.0F));
				bb_main.addOrReplaceChild("metal_r9", CubeListBuilder.create().texOffs(23, 1).addBox(-1.0F, -2.5F, -0.5F, 2.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 1.0F, -3.0F, 0.0F, -1.5708F, 1.5708F));
				bb_main.addOrReplaceChild("metal_r10", CubeListBuilder.create().texOffs(23, 1).addBox(-1.0F, -2.5F, -0.5F, 2.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.0F, 1.0F, 0.0F, -1.5708F, 0.0F, -3.1416F));
				bb_main.addOrReplaceChild("metal_r11", CubeListBuilder.create().texOffs(23, 1).addBox(-1.0F, -2.5F, -0.5F, 2.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 1.0F, 3.0F, 0.0F, -1.5708F, 1.5708F));
				bb_main.addOrReplaceChild("metal_r12", CubeListBuilder.create().texOffs(23, 1).addBox(-1.0F, -2.5F, -0.5F, 2.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.0F, 1.0F, 0.0F, -1.5708F, 0.0F, -3.1416F));
				return bb_main;
			},
			partDefinition -> {
				PartDefinition bb_main = partDefinition.addOrReplaceChild("bb_main", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, 6.0F, -3.0F, 4.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
				bb_main.addOrReplaceChild("metal_r1", CubeListBuilder.create().texOffs(23, 1).addBox(-1.0F, -2.5F, -0.5F, 2.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.0F, 1.0F, 0.0F, -1.5708F, 0.0F, -3.1416F));
				bb_main.addOrReplaceChild("metal_r2", CubeListBuilder.create().texOffs(23, 1).addBox(-1.0F, -2.5F, -0.5F, 2.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 1.0F, 3.0F, 0.0F, -1.5708F, 1.5708F));
				bb_main.addOrReplaceChild("metal_r3", CubeListBuilder.create().texOffs(23, 1).addBox(-1.0F, -2.5F, -0.5F, 2.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.0F, 1.0F, 0.0F, -1.5708F, 0.0F, -3.1416F));
				bb_main.addOrReplaceChild("metal_r4", CubeListBuilder.create().texOffs(23, 1).addBox(-1.0F, -2.5F, -0.5F, 2.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 1.0F, -3.0F, 0.0F, -1.5708F, 1.5708F));
				bb_main.addOrReplaceChild("metal_r5", CubeListBuilder.create().texOffs(22, 0).addBox(-1.0F, -9.0F, -1.0F, 2.0F, 18.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.6464F, 9.0F, 2.6464F, 0.0F, 0.7854F, 0.0F));
				bb_main.addOrReplaceChild("metal_r6", CubeListBuilder.create().texOffs(23, 1).addBox(-1.0F, -2.5F, -0.5F, 2.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 17.0F, 3.0F, 0.0F, -1.5708F, 1.5708F));
				bb_main.addOrReplaceChild("metal_r7", CubeListBuilder.create().texOffs(23, 1).addBox(-1.0F, -2.5F, -0.5F, 2.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 17.0F, -3.0F, 0.0F, -1.5708F, 1.5708F));
				bb_main.addOrReplaceChild("metal_r8", CubeListBuilder.create().texOffs(23, 1).addBox(-1.0F, -2.5F, -0.5F, 2.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.0F, 17.0F, 0.0F, -1.5708F, 0.0F, -3.1416F));
				bb_main.addOrReplaceChild("metal_r9", CubeListBuilder.create().texOffs(23, 1).addBox(-1.0F, -2.5F, -0.5F, 2.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.0F, 17.0F, 0.0F, -1.5708F, 0.0F, -3.1416F));
				bb_main.addOrReplaceChild("wood_r1", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, -1.0F, -0.5F, 4.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.5F, 7.0F, 0.0F, 1.5708F, 1.4835F, 1.5708F));
				bb_main.addOrReplaceChild("wood_r2", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, -1.0F, -0.5F, 4.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.5F, 11.0F, 0.0F, -1.5708F, 1.4399F, -1.5708F));
				bb_main.addOrReplaceChild("wood_r3", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, -1.0F, -0.5F, 4.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.5F, 3.0F, 0.0F, 1.5708F, 1.4835F, 1.5708F));
				bb_main.addOrReplaceChild("wood_r4", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, -1.0F, -0.5F, 4.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 11.0F, -2.5F, 0.0F, 0.0F, 0.0873F));
				bb_main.addOrReplaceChild("wood_r5", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, -1.0F, -0.5F, 4.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 3.0F, -2.5F, 0.0F, 0.0F, -0.0873F));
				bb_main.addOrReplaceChild("wood_r6", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, -1.0F, -0.5F, 4.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 7.0F, 2.5F, 0.0F, 0.0F, 0.0873F));
				bb_main.addOrReplaceChild("wood_r7", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, -1.0F, -0.5F, 4.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 11.0F, 2.5F, 0.0F, 0.0F, -0.1309F));
				bb_main.addOrReplaceChild("wood_r8", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, -1.0F, -0.5F, 4.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 3.0F, 2.5F, 0.0F, 0.0F, 0.1309F));
				bb_main.addOrReplaceChild("wood_r9", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, -1.0F, -0.5F, 4.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.5F, 3.0F, 0.0F, 1.5708F, 1.5272F, 1.5708F));
				bb_main.addOrReplaceChild("metal_r10", CubeListBuilder.create().texOffs(22, 0).addBox(-1.0F, -9.0F, -1.0F, 2.0F, 18.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.6464F, 9.0F, -2.6464F, 0.0F, -0.7854F, 0.0F));
				bb_main.addOrReplaceChild("metal_r11", CubeListBuilder.create().texOffs(22, 0).addBox(-1.0F, -9.0F, -1.0F, 2.0F, 18.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.6464F, 9.0F, 2.6464F, 0.0F, 0.7854F, 0.0F));
				bb_main.addOrReplaceChild("metal_r12", CubeListBuilder.create().texOffs(22, 0).addBox(-1.0F, -9.0F, -1.0F, 2.0F, 18.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.6464F, 9.0F, -2.6464F, 0.0F, 0.7854F, 0.0F));
				bb_main.addOrReplaceChild("wood_r10", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, -1.0F, -0.5F, 4.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.5F, 11.0F, 0.0F, 1.5708F, 1.5272F, 1.5708F));
				bb_main.addOrReplaceChild("wood_r11", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, -1.0F, -0.5F, 4.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.5F, 7.0F, 0.0F, -1.5708F, 1.4399F, -1.5708F));
				return bb_main;
			},
			partDefinition -> {
				PartDefinition bb_main = partDefinition.addOrReplaceChild("bb_main", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, 6.0F, -3.0F, 4.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
				bb_main.addOrReplaceChild("wood_r1", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, -1.0F, -0.5F, 4.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.5F, 15.0F, 0.0F, -1.5708F, 1.4835F, -1.5708F));
				bb_main.addOrReplaceChild("wood_r2", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, -1.0F, -0.5F, 4.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.5F, 7.0F, 0.0F, -1.5708F, 1.4399F, -1.5708F));
				bb_main.addOrReplaceChild("wood_r3", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, -1.0F, -0.5F, 4.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.5F, 11.0F, 0.0F, 1.5708F, 1.5272F, 1.5708F));
				bb_main.addOrReplaceChild("metal_r1", CubeListBuilder.create().texOffs(22, 0).addBox(-1.0F, -9.0F, -1.0F, 2.0F, 18.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.6464F, 9.0F, -2.6464F, 0.0F, 0.7854F, 0.0F));
				bb_main.addOrReplaceChild("metal_r2", CubeListBuilder.create().texOffs(22, 0).addBox(-1.0F, -9.0F, -1.0F, 2.0F, 18.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.6464F, 9.0F, 2.6464F, 0.0F, 0.7854F, 0.0F));
				bb_main.addOrReplaceChild("metal_r3", CubeListBuilder.create().texOffs(22, 0).addBox(-1.0F, -9.0F, -1.0F, 2.0F, 18.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.6464F, 9.0F, -2.6464F, 0.0F, -0.7854F, 0.0F));
				bb_main.addOrReplaceChild("wood_r4", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, -1.0F, -0.5F, 4.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.5F, 3.0F, 0.0F, 1.5708F, 1.5272F, 1.5708F));
				bb_main.addOrReplaceChild("wood_r5", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, -1.0F, -0.5F, 4.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 3.0F, 2.5F, 0.0F, 0.0F, 0.1309F));
				bb_main.addOrReplaceChild("wood_r6", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, -1.0F, -0.5F, 4.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 11.0F, 2.5F, 0.0F, 0.0F, -0.1309F));
				bb_main.addOrReplaceChild("wood_r7", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, -1.0F, -0.5F, 4.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 7.0F, 2.5F, 0.0F, 0.0F, 0.0873F));
				bb_main.addOrReplaceChild("wood_r8", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, -1.0F, -0.5F, 4.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 15.0F, 2.5F, 0.0F, 0.0F, 0.0873F));
				bb_main.addOrReplaceChild("wood_r9", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, -1.0F, -0.5F, 4.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 3.0F, -2.5F, 0.0F, 0.0F, -0.0873F));
				bb_main.addOrReplaceChild("wood_r10", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, -1.0F, -0.5F, 4.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 11.0F, -2.5F, 0.0F, 0.0F, 0.0873F));
				bb_main.addOrReplaceChild("wood_r11", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, -1.0F, -0.5F, 4.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 15.0F, -2.5F, 0.0F, 0.0F, -0.0436F));
				bb_main.addOrReplaceChild("wood_r12", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, -1.0F, -0.5F, 4.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.5F, 3.0F, 0.0F, 1.5708F, 1.4835F, 1.5708F));
				bb_main.addOrReplaceChild("wood_r13", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, -1.0F, -0.5F, 4.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.5F, 11.0F, 0.0F, -1.5708F, 1.4399F, -1.5708F));
				bb_main.addOrReplaceChild("wood_r14", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, -1.0F, -0.5F, 4.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.5F, 7.0F, 0.0F, 1.5708F, 1.4835F, 1.5708F));
				bb_main.addOrReplaceChild("wood_r15", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, -1.0F, -0.5F, 4.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.5F, 15.0F, 0.0F, 1.5708F, 1.4835F, 1.5708F));
				bb_main.addOrReplaceChild("metal_r4", CubeListBuilder.create().texOffs(23, 1).addBox(-1.0F, -2.5F, -0.5F, 2.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.0F, 17.0F, 0.0F, -1.5708F, 0.0F, -3.1416F));
				bb_main.addOrReplaceChild("metal_r5", CubeListBuilder.create().texOffs(23, 1).addBox(-1.0F, -2.5F, -0.5F, 2.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.0F, 17.0F, 0.0F, -1.5708F, 0.0F, -3.1416F));
				bb_main.addOrReplaceChild("metal_r6", CubeListBuilder.create().texOffs(23, 1).addBox(-1.0F, -2.5F, -0.5F, 2.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 17.0F, -3.0F, 0.0F, -1.5708F, 1.5708F));
				bb_main.addOrReplaceChild("metal_r7", CubeListBuilder.create().texOffs(23, 1).addBox(-1.0F, -2.5F, -0.5F, 2.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 17.0F, 3.0F, 0.0F, -1.5708F, 1.5708F));
				bb_main.addOrReplaceChild("metal_r8", CubeListBuilder.create().texOffs(22, 0).addBox(-1.0F, -9.0F, -1.0F, 2.0F, 18.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.6464F, 9.0F, 2.6464F, 0.0F, 0.7854F, 0.0F));
				bb_main.addOrReplaceChild("metal_r9", CubeListBuilder.create().texOffs(23, 1).addBox(-1.0F, -2.5F, -0.5F, 2.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 1.0F, -3.0F, 0.0F, -1.5708F, 1.5708F));
				bb_main.addOrReplaceChild("metal_r10", CubeListBuilder.create().texOffs(23, 1).addBox(-1.0F, -2.5F, -0.5F, 2.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.0F, 1.0F, 0.0F, -1.5708F, 0.0F, -3.1416F));
				bb_main.addOrReplaceChild("metal_r11", CubeListBuilder.create().texOffs(23, 1).addBox(-1.0F, -2.5F, -0.5F, 2.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 1.0F, 3.0F, 0.0F, -1.5708F, 1.5708F));
				bb_main.addOrReplaceChild("metal_r12", CubeListBuilder.create().texOffs(23, 1).addBox(-1.0F, -2.5F, -0.5F, 2.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.0F, 1.0F, 0.0F, -1.5708F, 0.0F, -3.1416F));
				return bb_main;
			},
			partDefinition -> {
				PartDefinition bb_main = partDefinition.addOrReplaceChild("bb_main", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, 6.0F, -3.0F, 4.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
						.texOffs(6, 0).addBox(-2.0F, 1.0F, -2.0F, 4.0F, 16.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
				bb_main.addOrReplaceChild("metal_r1", CubeListBuilder.create().texOffs(23, 1).addBox(-1.0F, -2.5F, -0.5F, 2.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.0F, 1.0F, 0.0F, -1.5708F, 0.0F, -3.1416F));
				bb_main.addOrReplaceChild("metal_r2", CubeListBuilder.create().texOffs(23, 1).addBox(-1.0F, -2.5F, -0.5F, 2.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 1.0F, 3.0F, 0.0F, -1.5708F, 1.5708F));
				bb_main.addOrReplaceChild("metal_r3", CubeListBuilder.create().texOffs(23, 1).addBox(-1.0F, -2.5F, -0.5F, 2.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.0F, 1.0F, 0.0F, -1.5708F, 0.0F, -3.1416F));
				bb_main.addOrReplaceChild("metal_r4", CubeListBuilder.create().texOffs(23, 1).addBox(-1.0F, -2.5F, -0.5F, 2.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 1.0F, -3.0F, 0.0F, -1.5708F, 1.5708F));
				bb_main.addOrReplaceChild("metal_r5", CubeListBuilder.create().texOffs(22, 0).addBox(-1.0F, -9.0F, -1.0F, 2.0F, 18.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.6464F, 9.0F, 2.6464F, 0.0F, 0.7854F, 0.0F));
				bb_main.addOrReplaceChild("metal_r6", CubeListBuilder.create().texOffs(23, 1).addBox(-1.0F, -2.5F, -0.5F, 2.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 17.0F, 3.0F, 0.0F, -1.5708F, 1.5708F));
				bb_main.addOrReplaceChild("metal_r7", CubeListBuilder.create().texOffs(23, 1).addBox(-1.0F, -2.5F, -0.5F, 2.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 17.0F, -3.0F, 0.0F, -1.5708F, 1.5708F));
				bb_main.addOrReplaceChild("metal_r8", CubeListBuilder.create().texOffs(23, 1).addBox(-1.0F, -2.5F, -0.5F, 2.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.0F, 17.0F, 0.0F, -1.5708F, 0.0F, -3.1416F));
				bb_main.addOrReplaceChild("metal_r9", CubeListBuilder.create().texOffs(23, 1).addBox(-1.0F, -2.5F, -0.5F, 2.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.0F, 17.0F, 0.0F, -1.5708F, 0.0F, -3.1416F));
				bb_main.addOrReplaceChild("wood_r1", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, -1.0F, -0.5F, 4.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.5F, 15.0F, 0.0F, 1.5708F, 1.4835F, 1.5708F));
				bb_main.addOrReplaceChild("wood_r2", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, -1.0F, -0.5F, 4.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.5F, 7.0F, 0.0F, 1.5708F, 1.4835F, 1.5708F));
				bb_main.addOrReplaceChild("wood_r3", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, -1.0F, -0.5F, 4.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.5F, 11.0F, 0.0F, -1.5708F, 1.4399F, -1.5708F));
				bb_main.addOrReplaceChild("wood_r4", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, -1.0F, -0.5F, 4.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.5F, 3.0F, 0.0F, 1.5708F, 1.4835F, 1.5708F));
				bb_main.addOrReplaceChild("wood_r5", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, -1.0F, -0.5F, 4.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 15.0F, -2.5F, 0.0F, 0.0F, -0.0436F));
				bb_main.addOrReplaceChild("wood_r6", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, -1.0F, -0.5F, 4.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 11.0F, -2.5F, 0.0F, 0.0F, 0.0873F));
				bb_main.addOrReplaceChild("wood_r7", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, -1.0F, -0.5F, 4.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 3.0F, -2.5F, 0.0F, 0.0F, -0.0873F));
				bb_main.addOrReplaceChild("wood_r8", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, -1.0F, -0.5F, 4.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 15.0F, 2.5F, 0.0F, 0.0F, 0.0873F));
				bb_main.addOrReplaceChild("wood_r9", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, -1.0F, -0.5F, 4.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 7.0F, 2.5F, 0.0F, 0.0F, 0.0873F));
				bb_main.addOrReplaceChild("wood_r10", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, -1.0F, -0.5F, 4.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 11.0F, 2.5F, 0.0F, 0.0F, -0.1309F));
				bb_main.addOrReplaceChild("wood_r11", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, -1.0F, -0.5F, 4.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 3.0F, 2.5F, 0.0F, 0.0F, 0.1309F));
				bb_main.addOrReplaceChild("wood_r12", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, -1.0F, -0.5F, 4.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.5F, 3.0F, 0.0F, 1.5708F, 1.5272F, 1.5708F));
				bb_main.addOrReplaceChild("metal_r10", CubeListBuilder.create().texOffs(22, 0).addBox(-1.0F, -9.0F, -1.0F, 2.0F, 18.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.6464F, 9.0F, -2.6464F, 0.0F, -0.7854F, 0.0F));
				bb_main.addOrReplaceChild("metal_r11", CubeListBuilder.create().texOffs(22, 0).addBox(-1.0F, -9.0F, -1.0F, 2.0F, 18.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.6464F, 9.0F, 2.6464F, 0.0F, 0.7854F, 0.0F));
				bb_main.addOrReplaceChild("metal_r12", CubeListBuilder.create().texOffs(22, 0).addBox(-1.0F, -9.0F, -1.0F, 2.0F, 18.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.6464F, 9.0F, -2.6464F, 0.0F, 0.7854F, 0.0F));
				bb_main.addOrReplaceChild("wood_r13", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, -1.0F, -0.5F, 4.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.5F, 11.0F, 0.0F, 1.5708F, 1.5272F, 1.5708F));
				bb_main.addOrReplaceChild("wood_r14", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, -1.0F, -0.5F, 4.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.5F, 7.0F, 0.0F, -1.5708F, 1.4399F, -1.5708F));
				bb_main.addOrReplaceChild("wood_r15", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, -1.0F, -0.5F, 4.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.5F, 15.0F, 0.0F, -1.5708F, 1.4835F, -1.5708F));
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
		return LayerDefinition.create(meshdefinition, 48, 48);
	}

	@Override
	public ResourceLocation getTexture(int stage) {
		return new ResourceLocation(CreateMissiles.MOD_ID,"textures/entity/excavator_chassis.png");
	}

	@Override
	public int getStageCount() {
		return layers.size();
	}
}