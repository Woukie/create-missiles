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

public class FlamingChassisModel implements MissilePartModel {
	private final List<Map<String, Vector3f>> attachments = List.of(
			new HashMap<>() {{
				put("bottom", new Vector3f(0, 0, 0));
				put("top", new Vector3f(0, 0, 0));
			}},
			new HashMap<>() {{
				put("bottom", new Vector3f(0, 0, 0));
				put("top", new Vector3f(0, 9, 0));
			}},
			new HashMap<>() {{
				put("bottom", new Vector3f(0, 0, 0));
				put("top", new Vector3f(0, 9, 0));
			}},
			new HashMap<>() {{
				put("bottom", new Vector3f(0, 0, 0));
				put("top", new Vector3f(0, 17, 0));
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
			partDefinition -> {
				PartDefinition bb_main = partDefinition.addOrReplaceChild("bb_main", CubeListBuilder.create().texOffs(8, 1).addBox(2.1213F, 0.0F, -1.0F, 1.0F, 9.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
				bb_main.addOrReplaceChild("body_r1", CubeListBuilder.create().texOffs(8, 1).addBox(2.1213F, -1.0F, -1.0F, 1.0F, 9.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 1.0F, 0.0F, 0.0F, 1.5708F, 0.0F));
				bb_main.addOrReplaceChild("body_r2", CubeListBuilder.create().texOffs(8, 1).addBox(2.1213F, -1.0F, -1.0F, 1.0F, 9.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 1.0F, 0.0F, -3.1416F, 0.0F, 3.1416F));
				bb_main.addOrReplaceChild("body_r3", CubeListBuilder.create().texOffs(8, 1).addBox(2.1213F, -1.0F, -1.0F, 1.0F, 9.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 1.0F, 0.0F, 0.0F, -1.5708F, 0.0F));
				bb_main.addOrReplaceChild("body_r4", CubeListBuilder.create().texOffs(0, 0).addBox(1.9142F, -1.0F, -1.5F, 1.0F, 9.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 1.0F, 0.0F, 0.0F, 0.7854F, 0.0F));
				bb_main.addOrReplaceChild("body_r5", CubeListBuilder.create().texOffs(0, 0).addBox(1.9142F, -1.0F, -1.5F, 1.0F, 9.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 1.0F, 0.0F, -3.1416F, 0.7854F, 3.1416F));
				bb_main.addOrReplaceChild("body_r6", CubeListBuilder.create().texOffs(0, 0).addBox(1.9142F, -1.0F, -1.5F, 1.0F, 9.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 1.0F, 0.0F, -3.1416F, -0.7854F, 3.1416F));
				bb_main.addOrReplaceChild("body_r7", CubeListBuilder.create().texOffs(0, 0).addBox(1.9142F, -1.0F, -1.5F, 1.0F, 9.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 1.0F, 0.0F, 0.0F, -0.7854F, 0.0F));
				return bb_main;
			},
			partDefinition -> {
				PartDefinition bb_main = partDefinition.addOrReplaceChild("bb_main", CubeListBuilder.create().texOffs(8, 1).addBox(2.1213F, 0.0F, -1.0F, 1.0F, 9.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
				bb_main.addOrReplaceChild("rods_low_r1", CubeListBuilder.create().texOffs(14, 10).addBox(-1.0F, -4.0F, -1.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.1213F, 4.5F, -1.0F, 0.0F, 0.7854F, -3.1416F));
				bb_main.addOrReplaceChild("rods_low_r2", CubeListBuilder.create().texOffs(14, 10).addBox(-1.0F, -4.0F, -1.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, 4.5F, 1.1213F, 0.0F, 0.7854F, -3.1416F));
				bb_main.addOrReplaceChild("rods_low_r3", CubeListBuilder.create().texOffs(14, 10).addBox(-1.0F, -4.0F, -1.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.1213F, 4.5F, 1.1213F, 0.0F, -0.7854F, -3.1416F));
				bb_main.addOrReplaceChild("rods_low_r4", CubeListBuilder.create().texOffs(14, 10).addBox(-1.0F, -4.0F, -1.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, 4.5F, -1.0F, 0.0F, -0.7854F, -3.1416F));
				bb_main.addOrReplaceChild("body_r1", CubeListBuilder.create().texOffs(8, 1).addBox(2.1213F, -1.0F, -1.0F, 1.0F, 9.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 1.0F, 0.0F, 0.0F, 1.5708F, 0.0F));
				bb_main.addOrReplaceChild("body_r2", CubeListBuilder.create().texOffs(8, 1).addBox(2.1213F, -1.0F, -1.0F, 1.0F, 9.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 1.0F, 0.0F, -3.1416F, 0.0F, 3.1416F));
				bb_main.addOrReplaceChild("body_r3", CubeListBuilder.create().texOffs(8, 1).addBox(2.1213F, -1.0F, -1.0F, 1.0F, 9.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 1.0F, 0.0F, 0.0F, -1.5708F, 0.0F));
				bb_main.addOrReplaceChild("body_r4", CubeListBuilder.create().texOffs(0, 0).addBox(1.9142F, -1.0F, -1.5F, 1.0F, 9.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 1.0F, 0.0F, 0.0F, 0.7854F, 0.0F));
				bb_main.addOrReplaceChild("body_r5", CubeListBuilder.create().texOffs(0, 0).addBox(1.9142F, -1.0F, -1.5F, 1.0F, 9.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 1.0F, 0.0F, -3.1416F, 0.7854F, 3.1416F));
				bb_main.addOrReplaceChild("body_r6", CubeListBuilder.create().texOffs(0, 0).addBox(1.9142F, -1.0F, -1.5F, 1.0F, 9.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 1.0F, 0.0F, -3.1416F, -0.7854F, 3.1416F));
				bb_main.addOrReplaceChild("body_r7", CubeListBuilder.create().texOffs(0, 0).addBox(1.9142F, -1.0F, -1.5F, 1.0F, 9.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 1.0F, 0.0F, 0.0F, -0.7854F, 0.0F));
				return bb_main;
			},
			partDefinition -> {
				PartDefinition bb_main = partDefinition.addOrReplaceChild("bb_main", CubeListBuilder.create().texOffs(8, 1).addBox(2.1213F, 0.0F, -1.0F, 1.0F, 17.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
				bb_main.addOrReplaceChild("rods_low_r1", CubeListBuilder.create().texOffs(14, 10).addBox(-1.0F, -4.0F, -1.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.1213F, 4.5F, -1.0F, 0.0F, 0.7854F, -3.1416F));
				bb_main.addOrReplaceChild("rods_low_r2", CubeListBuilder.create().texOffs(14, 10).addBox(-1.0F, -4.0F, -1.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, 4.5F, 1.1213F, 0.0F, 0.7854F, -3.1416F));
				bb_main.addOrReplaceChild("rods_low_r3", CubeListBuilder.create().texOffs(14, 10).addBox(-1.0F, -4.0F, -1.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.1213F, 4.5F, 1.1213F, 0.0F, -0.7854F, -3.1416F));
				bb_main.addOrReplaceChild("rods_low_r4", CubeListBuilder.create().texOffs(14, 10).addBox(-1.0F, -4.0F, -1.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, 4.5F, -1.0F, 0.0F, -0.7854F, -3.1416F));
				bb_main.addOrReplaceChild("body_r1", CubeListBuilder.create().texOffs(8, 1).addBox(2.1213F, -1.0F, -1.0F, 1.0F, 17.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 1.0F, 0.0F, 0.0F, 1.5708F, 0.0F));
				bb_main.addOrReplaceChild("body_r2", CubeListBuilder.create().texOffs(8, 1).addBox(2.1213F, -1.0F, -1.0F, 1.0F, 17.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 1.0F, 0.0F, -3.1416F, 0.0F, 3.1416F));
				bb_main.addOrReplaceChild("body_r3", CubeListBuilder.create().texOffs(8, 1).addBox(2.1213F, -1.0F, -1.0F, 1.0F, 17.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 1.0F, 0.0F, 0.0F, -1.5708F, 0.0F));
				bb_main.addOrReplaceChild("body_r4", CubeListBuilder.create().texOffs(0, 0).addBox(1.9142F, -1.0F, -1.5F, 1.0F, 17.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 1.0F, 0.0F, 0.0F, 0.7854F, 0.0F));
				bb_main.addOrReplaceChild("body_r5", CubeListBuilder.create().texOffs(0, 0).addBox(1.9142F, -1.0F, -1.5F, 1.0F, 17.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 1.0F, 0.0F, -3.1416F, 0.7854F, 3.1416F));
				bb_main.addOrReplaceChild("body_r6", CubeListBuilder.create().texOffs(0, 0).addBox(1.9142F, -1.0F, -1.5F, 1.0F, 17.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 1.0F, 0.0F, -3.1416F, -0.7854F, 3.1416F));
				bb_main.addOrReplaceChild("body_r7", CubeListBuilder.create().texOffs(0, 0).addBox(1.9142F, -1.0F, -1.5F, 1.0F, 17.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 1.0F, 0.0F, 0.0F, -0.7854F, 0.0F));
				return bb_main;
			},
			partDefinition -> {
				PartDefinition bb_main = partDefinition.addOrReplaceChild("bb_main", CubeListBuilder.create().texOffs(8, 1).addBox(2.1213F, 0.0F, -1.0F, 1.0F, 17.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
				bb_main.addOrReplaceChild("rods_high_r1", CubeListBuilder.create().texOffs(14, 10).addBox(-1.0F, -4.0F, -1.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.5607F, 12.5F, 0.0607F, 0.0F, 0.0F, 3.1416F));
				bb_main.addOrReplaceChild("rods_high_r2", CubeListBuilder.create().texOffs(14, 10).addBox(-1.0F, -4.0F, -1.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0607F, 12.5F, 1.5607F, 0.0F, -1.5708F, -3.1416F));
				bb_main.addOrReplaceChild("rods_high_r3", CubeListBuilder.create().texOffs(14, 10).addBox(-1.0F, -4.0F, -1.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.4393F, 12.5F, 0.0607F, 0.0F, 0.0F, 3.1416F));
				bb_main.addOrReplaceChild("rods_high_r4", CubeListBuilder.create().texOffs(14, 10).addBox(-1.0F, -4.0F, -1.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0607F, 12.5F, -1.4393F, 0.0F, -1.5708F, -3.1416F));
				bb_main.addOrReplaceChild("rods_low_r1", CubeListBuilder.create().texOffs(14, 10).addBox(-1.0F, -4.0F, -1.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.1213F, 4.5F, -1.0F, 0.0F, 0.7854F, -3.1416F));
				bb_main.addOrReplaceChild("rods_low_r2", CubeListBuilder.create().texOffs(14, 10).addBox(-1.0F, -4.0F, -1.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, 4.5F, 1.1213F, 0.0F, 0.7854F, -3.1416F));
				bb_main.addOrReplaceChild("rods_low_r3", CubeListBuilder.create().texOffs(14, 10).addBox(-1.0F, -4.0F, -1.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.1213F, 4.5F, 1.1213F, 0.0F, -0.7854F, -3.1416F));
				bb_main.addOrReplaceChild("rods_low_r4", CubeListBuilder.create().texOffs(14, 10).addBox(-1.0F, -4.0F, -1.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, 4.5F, -1.0F, 0.0F, -0.7854F, -3.1416F));
				bb_main.addOrReplaceChild("body_r1", CubeListBuilder.create().texOffs(8, 1).addBox(2.1213F, -1.0F, -1.0F, 1.0F, 17.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 1.0F, 0.0F, 0.0F, 1.5708F, 0.0F));
				bb_main.addOrReplaceChild("body_r2", CubeListBuilder.create().texOffs(8, 1).addBox(2.1213F, -1.0F, -1.0F, 1.0F, 17.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 1.0F, 0.0F, -3.1416F, 0.0F, 3.1416F));
				bb_main.addOrReplaceChild("body_r3", CubeListBuilder.create().texOffs(8, 1).addBox(2.1213F, -1.0F, -1.0F, 1.0F, 17.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 1.0F, 0.0F, 0.0F, -1.5708F, 0.0F));
				bb_main.addOrReplaceChild("body_r4", CubeListBuilder.create().texOffs(0, 0).addBox(1.9142F, -1.0F, -1.5F, 1.0F, 17.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 1.0F, 0.0F, 0.0F, 0.7854F, 0.0F));
				bb_main.addOrReplaceChild("body_r5", CubeListBuilder.create().texOffs(0, 0).addBox(1.9142F, -1.0F, -1.5F, 1.0F, 17.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 1.0F, 0.0F, -3.1416F, 0.7854F, 3.1416F));
				bb_main.addOrReplaceChild("body_r6", CubeListBuilder.create().texOffs(0, 0).addBox(1.9142F, -1.0F, -1.5F, 1.0F, 17.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 1.0F, 0.0F, -3.1416F, -0.7854F, 3.1416F));
				bb_main.addOrReplaceChild("body_r7", CubeListBuilder.create().texOffs(0, 0).addBox(1.9142F, -1.0F, -1.5F, 1.0F, 17.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 1.0F, 0.0F, 0.0F, -0.7854F, 0.0F));
				return bb_main;
			},
			partDefinition -> {
				PartDefinition bb_main = partDefinition.addOrReplaceChild("bb_main", CubeListBuilder.create().texOffs(30, 1).addBox(2.1213F, 0.0F, -1.0F, 1.0F, 17.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
				bb_main.addOrReplaceChild("rods_high_r1", CubeListBuilder.create().texOffs(14, 10).addBox(-1.0F, -4.0F, -1.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.5607F, 12.5F, 0.0607F, 0.0F, 0.0F, 3.1416F));
				bb_main.addOrReplaceChild("rods_high_r2", CubeListBuilder.create().texOffs(14, 10).addBox(-1.0F, -4.0F, -1.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0607F, 12.5F, 1.5607F, 0.0F, -1.5708F, -3.1416F));
				bb_main.addOrReplaceChild("rods_high_r3", CubeListBuilder.create().texOffs(14, 10).addBox(-1.0F, -4.0F, -1.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.4393F, 12.5F, 0.0607F, 0.0F, 0.0F, 3.1416F));
				bb_main.addOrReplaceChild("rods_high_r4", CubeListBuilder.create().texOffs(14, 10).addBox(-1.0F, -4.0F, -1.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0607F, 12.5F, -1.4393F, 0.0F, -1.5708F, -3.1416F));
				bb_main.addOrReplaceChild("rods_low_r1", CubeListBuilder.create().texOffs(14, 10).addBox(-1.0F, -4.0F, -1.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.1213F, 4.5F, -1.0F, 0.0F, 0.7854F, -3.1416F));
				bb_main.addOrReplaceChild("rods_low_r2", CubeListBuilder.create().texOffs(14, 10).addBox(-1.0F, -4.0F, -1.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, 4.5F, 1.1213F, 0.0F, 0.7854F, -3.1416F));
				bb_main.addOrReplaceChild("rods_low_r3", CubeListBuilder.create().texOffs(14, 10).addBox(-1.0F, -4.0F, -1.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.1213F, 4.5F, 1.1213F, 0.0F, -0.7854F, -3.1416F));
				bb_main.addOrReplaceChild("rods_low_r4", CubeListBuilder.create().texOffs(14, 10).addBox(-1.0F, -4.0F, -1.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, 4.5F, -1.0F, 0.0F, -0.7854F, -3.1416F));
				bb_main.addOrReplaceChild("body_r1", CubeListBuilder.create().texOffs(30, 1).addBox(2.1213F, -1.0F, -1.0F, 1.0F, 17.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 1.0F, 0.0F, 0.0F, 1.5708F, 0.0F));
				bb_main.addOrReplaceChild("body_r2", CubeListBuilder.create().texOffs(30, 1).addBox(2.1213F, -1.0F, -1.0F, 1.0F, 17.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 1.0F, 0.0F, -3.1416F, 0.0F, 3.1416F));
				bb_main.addOrReplaceChild("body_r3", CubeListBuilder.create().texOffs(30, 1).addBox(2.1213F, -1.0F, -1.0F, 1.0F, 17.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 1.0F, 0.0F, 0.0F, -1.5708F, 0.0F));
				bb_main.addOrReplaceChild("body_r4", CubeListBuilder.create().texOffs(22, 0).addBox(1.9142F, -1.0F, -1.5F, 1.0F, 17.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 1.0F, 0.0F, 0.0F, 0.7854F, 0.0F));
				bb_main.addOrReplaceChild("body_r5", CubeListBuilder.create().texOffs(22, 0).addBox(1.9142F, -1.0F, -1.5F, 1.0F, 17.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 1.0F, 0.0F, -3.1416F, 0.7854F, 3.1416F));
				bb_main.addOrReplaceChild("body_r6", CubeListBuilder.create().texOffs(22, 0).addBox(1.9142F, -1.0F, -1.5F, 1.0F, 17.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 1.0F, 0.0F, -3.1416F, -0.7854F, 3.1416F));
				bb_main.addOrReplaceChild("body_r7", CubeListBuilder.create().texOffs(22, 0).addBox(1.9142F, -1.0F, -1.5F, 1.0F, 17.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 1.0F, 0.0F, 0.0F, -0.7854F, 0.0F));
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
		return LayerDefinition.create(meshdefinition, 36, 20);
	}

	@Override
	public ResourceLocation getTexture(int stage) {
		return ResourceLocation.fromNamespaceAndPath(CreateMissiles.MOD_ID,"textures/entity/flaming_chassis.png");
	}

	@Override
	public int getStageCount() {
		return layers.size();
	}
}