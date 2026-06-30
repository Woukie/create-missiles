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
				put("top", new Vector3f(0, 10, 0));
			}},
			new HashMap<>() {{
				put("bottom", new Vector3f(0, 0, 0));
				put("top", new Vector3f(0, 10, 0));
			}},
			new HashMap<>() {{
				put("bottom", new Vector3f(0, 0, 0));
				put("top", new Vector3f(0, 10, 0));
			}},
			new HashMap<>() {{
				put("bottom", new Vector3f(0, 0, 0));
				put("top", new Vector3f(0, 10, 0));
			}},
			new HashMap<>() {{
				put("bottom", new Vector3f(0, 0, 0));
				put("top", new Vector3f(0, 10, 0));
			}},
			new HashMap<>() {{
				put("bottom", new Vector3f(0, 0, 0));
				put("top", new Vector3f(0, 10, 0));
			}}
	);

	private final List<Function<PartDefinition, PartDefinition>> layers = List.of(
			partDefinition -> partDefinition.addOrReplaceChild("bb_main", CubeListBuilder.create(), PartPose.ZERO),
			partDefinition -> {
				PartDefinition bb_main = partDefinition.addOrReplaceChild("bb_main", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));
				bb_main.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(22, 42).addBox(-0.5F, -0.5F, -2.0F, 1.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.5F, 0.5F, 0.0F, 3.1416F, 0.0F, 0.0F));
				bb_main.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(22, 30).addBox(-0.5F, -5.0F, -0.5F, 1.0F, 10.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.5F, 5.0F, 2.5F, 0.0F, 0.0F, 3.1416F));
				bb_main.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(22, 37).addBox(-0.5F, -0.5F, -2.0F, 1.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.5F, 9.5F, 0.0F, 0.0F, 0.0F, 3.1416F));
				bb_main.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(22, 30).addBox(-0.5F, -5.0F, -0.5F, 1.0F, 10.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.5F, 5.0F, -2.5F, 0.0F, 1.5708F, 3.1416F));
				bb_main.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(22, 42).addBox(-0.5F, -0.5F, -2.0F, 1.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.5F, 0.5F, 0.0F, 0.0F, 0.0F, -3.1416F));
				bb_main.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(22, 37).addBox(-0.5F, -0.5F, -2.0F, 1.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.5F, 9.5F, 0.0F, -3.1416F, 0.0F, 0.0F));
				bb_main.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(22, 30).addBox(-0.5F, -5.0F, -0.5F, 1.0F, 10.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.5F, 5.0F, -2.5F, -3.1416F, 0.0F, 0.0F));
				bb_main.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(22, 30).addBox(-0.5F, -5.0F, -0.5F, 1.0F, 10.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.5F, 5.0F, 2.5F, 0.0F, -1.5708F, 3.1416F));
				bb_main.addOrReplaceChild("cube_r9", CubeListBuilder.create().texOffs(2, 26).addBox(-2.0F, -5.0F, -3.0F, 4.0F, 10.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 5.0F, 0.0F, -3.1416F, 0.0F, 0.0F));
				return bb_main;
			},
			partDefinition -> {
				PartDefinition bb_main = partDefinition.addOrReplaceChild("bb_main", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));
				bb_main.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(22, 42).addBox(-0.5F, -0.5F, -2.0F, 1.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.5F, 0.5F, 0.0F, 3.1416F, 0.0F, 0.0F));
				bb_main.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(22, 30).addBox(-0.5F, -5.0F, -0.5F, 1.0F, 10.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.5F, 5.0F, 2.5F, 0.0F, 0.0F, 3.1416F));
				bb_main.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(22, 37).addBox(-0.5F, -0.5F, -2.0F, 1.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.5F, 9.5F, 0.0F, 0.0F, 0.0F, 3.1416F));
				bb_main.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(22, 30).addBox(-0.5F, -5.0F, -0.5F, 1.0F, 10.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.5F, 5.0F, -2.5F, 0.0F, 1.5708F, 3.1416F));
				bb_main.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(22, 42).addBox(-0.5F, -0.5F, -2.0F, 1.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.5F, 0.5F, 0.0F, 0.0F, 0.0F, -3.1416F));
				bb_main.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(22, 37).addBox(-0.5F, -0.5F, -2.0F, 1.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.5F, 9.5F, 0.0F, -3.1416F, 0.0F, 0.0F));
				bb_main.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(22, 30).addBox(-0.5F, -5.0F, -0.5F, 1.0F, 10.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.5F, 5.0F, -2.5F, -3.1416F, 0.0F, 0.0F));
				bb_main.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(22, 30).addBox(-0.5F, -5.0F, -0.5F, 1.0F, 10.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.5F, 5.0F, 2.5F, 0.0F, -1.5708F, 3.1416F));
				bb_main.addOrReplaceChild("cube_r9", CubeListBuilder.create().texOffs(28, 32).addBox(-0.5F, -4.0F, -0.5F, 1.0F, 8.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.5F, 5.0F, 3.0F, 0.0F, 0.0F, 3.1416F));
				bb_main.addOrReplaceChild("cube_r10", CubeListBuilder.create().texOffs(28, 32).addBox(-0.5F, -4.0F, -0.5F, 1.0F, 8.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.5F, 5.0F, 3.0F, 0.0F, 1.5708F, 3.1416F));
				bb_main.addOrReplaceChild("cube_r11", CubeListBuilder.create().texOffs(28, 32).addBox(-0.5F, -4.0F, -0.5F, 1.0F, 8.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.5F, 5.0F, -3.0F, 0.0F, -1.5708F, 3.1416F));
				bb_main.addOrReplaceChild("cube_r12", CubeListBuilder.create().texOffs(28, 32).addBox(-0.5F, -4.0F, -0.5F, 1.0F, 8.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.5F, 5.0F, -3.0F, -3.1416F, 0.0F, 0.0F));
				bb_main.addOrReplaceChild("cube_r13", CubeListBuilder.create().texOffs(2, 26).addBox(-2.0F, -5.0F, -3.0F, 4.0F, 10.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 5.0F, 0.0F, -3.1416F, 0.0F, 0.0F));
				return bb_main;
			},
			partDefinition -> {
				PartDefinition bb_main = partDefinition.addOrReplaceChild("bb_main", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));
				bb_main.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(22, 42).addBox(-0.5F, -0.5F, -2.0F, 1.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.5F, 0.5F, 0.0F, 3.1416F, 0.0F, 0.0F));
				bb_main.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(22, 30).addBox(-0.5F, -5.0F, -0.5F, 1.0F, 10.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.5F, 5.0F, 2.5F, 0.0F, 0.0F, 3.1416F));
				bb_main.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(22, 37).addBox(-0.5F, -0.5F, -2.0F, 1.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.5F, 9.5F, 0.0F, 0.0F, 0.0F, 3.1416F));
				bb_main.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(22, 30).addBox(-0.5F, -5.0F, -0.5F, 1.0F, 10.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.5F, 5.0F, -2.5F, 0.0F, 1.5708F, 3.1416F));
				bb_main.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(32, 35).addBox(-0.5F, -1.0F, -2.0F, 1.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.1156F, 8.088F, 0.0F, 0.0F, 0.0F, -2.5744F));
				bb_main.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(22, 42).addBox(-0.5F, -0.5F, -2.0F, 1.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.5F, 0.5F, 0.0F, 0.0F, 0.0F, -3.1416F));
				bb_main.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(22, 37).addBox(-0.5F, -0.5F, -2.0F, 1.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.5F, 9.5F, 0.0F, -3.1416F, 0.0F, 0.0F));
				bb_main.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(32, 35).addBox(-0.5F, -1.0F, -2.0F, 1.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.1156F, 8.088F, 0.0F, -3.1416F, 0.0F, -0.5672F));
				bb_main.addOrReplaceChild("cube_r9", CubeListBuilder.create().texOffs(22, 30).addBox(-0.5F, -5.0F, -0.5F, 1.0F, 10.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.5F, 5.0F, -2.5F, -3.1416F, 0.0F, 0.0F));
				bb_main.addOrReplaceChild("cube_r10", CubeListBuilder.create().texOffs(22, 30).addBox(-0.5F, -5.0F, -0.5F, 1.0F, 10.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.5F, 5.0F, 2.5F, 0.0F, -1.5708F, 3.1416F));
				bb_main.addOrReplaceChild("cube_r11", CubeListBuilder.create().texOffs(28, 32).addBox(-0.5F, -4.0F, -0.5F, 1.0F, 8.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.5F, 5.0F, 3.0F, 0.0F, 0.0F, 3.1416F));
				bb_main.addOrReplaceChild("cube_r12", CubeListBuilder.create().texOffs(28, 32).addBox(-0.5F, -4.0F, -0.5F, 1.0F, 8.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.5F, 5.0F, 3.0F, 0.0F, 1.5708F, 3.1416F));
				bb_main.addOrReplaceChild("cube_r13", CubeListBuilder.create().texOffs(28, 32).addBox(-0.5F, -4.0F, -0.5F, 1.0F, 8.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.5F, 5.0F, -3.0F, 0.0F, -1.5708F, 3.1416F));
				bb_main.addOrReplaceChild("cube_r14", CubeListBuilder.create().texOffs(28, 32).addBox(-0.5F, -4.0F, -0.5F, 1.0F, 8.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.5F, 5.0F, -3.0F, -3.1416F, 0.0F, 0.0F));
				bb_main.addOrReplaceChild("cube_r15", CubeListBuilder.create().texOffs(2, 26).addBox(-2.0F, -5.0F, -3.0F, 4.0F, 10.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 5.0F, 0.0F, -3.1416F, 0.0F, 0.0F));
				return bb_main;
			},
			partDefinition -> {
				PartDefinition bb_main = partDefinition.addOrReplaceChild("bb_main", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));
				bb_main.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(22, 42).addBox(-0.5F, -0.5F, -2.0F, 1.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.5F, 0.5F, 0.0F, 3.1416F, 0.0F, 0.0F));
				bb_main.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(32, 35).addBox(-0.5F, -1.0F, -2.0F, 1.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.1156F, 6.088F, 0.0F, 0.0F, 0.0F, -2.5744F));
				bb_main.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(22, 30).addBox(-0.5F, -5.0F, -0.5F, 1.0F, 10.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.5F, 5.0F, 2.5F, 0.0F, 0.0F, 3.1416F));
				bb_main.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(22, 37).addBox(-0.5F, -0.5F, -2.0F, 1.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.5F, 9.5F, 0.0F, 0.0F, 0.0F, 3.1416F));
				bb_main.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(22, 30).addBox(-0.5F, -5.0F, -0.5F, 1.0F, 10.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.5F, 5.0F, -2.5F, 0.0F, 1.5708F, 3.1416F));
				bb_main.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(32, 35).addBox(-0.5F, -1.0F, -2.0F, 1.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.1156F, 8.088F, 0.0F, 0.0F, 0.0F, -2.5744F));
				bb_main.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(22, 42).addBox(-0.5F, -0.5F, -2.0F, 1.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.5F, 0.5F, 0.0F, 0.0F, 0.0F, -3.1416F));
				bb_main.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(32, 35).addBox(-0.5F, -1.0F, -2.0F, 1.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.1156F, 6.088F, 0.0F, -3.1416F, 0.0F, -0.5672F));
				bb_main.addOrReplaceChild("cube_r9", CubeListBuilder.create().texOffs(22, 37).addBox(-0.5F, -0.5F, -2.0F, 1.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.5F, 9.5F, 0.0F, -3.1416F, 0.0F, 0.0F));
				bb_main.addOrReplaceChild("cube_r10", CubeListBuilder.create().texOffs(32, 35).addBox(-0.5F, -1.0F, -2.0F, 1.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.1156F, 8.088F, 0.0F, -3.1416F, 0.0F, -0.5672F));
				bb_main.addOrReplaceChild("cube_r11", CubeListBuilder.create().texOffs(22, 30).addBox(-0.5F, -5.0F, -0.5F, 1.0F, 10.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.5F, 5.0F, -2.5F, -3.1416F, 0.0F, 0.0F));
				bb_main.addOrReplaceChild("cube_r12", CubeListBuilder.create().texOffs(22, 30).addBox(-0.5F, -5.0F, -0.5F, 1.0F, 10.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.5F, 5.0F, 2.5F, 0.0F, -1.5708F, 3.1416F));
				bb_main.addOrReplaceChild("cube_r13", CubeListBuilder.create().texOffs(28, 32).addBox(-0.5F, -4.0F, -0.5F, 1.0F, 8.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.5F, 5.0F, 3.0F, 0.0F, 0.0F, 3.1416F));
				bb_main.addOrReplaceChild("cube_r14", CubeListBuilder.create().texOffs(28, 32).addBox(-0.5F, -4.0F, -0.5F, 1.0F, 8.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.5F, 5.0F, 3.0F, 0.0F, 1.5708F, 3.1416F));
				bb_main.addOrReplaceChild("cube_r15", CubeListBuilder.create().texOffs(28, 32).addBox(-0.5F, -4.0F, -0.5F, 1.0F, 8.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.5F, 5.0F, -3.0F, 0.0F, -1.5708F, 3.1416F));
				bb_main.addOrReplaceChild("cube_r16", CubeListBuilder.create().texOffs(28, 32).addBox(-0.5F, -4.0F, -0.5F, 1.0F, 8.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.5F, 5.0F, -3.0F, -3.1416F, 0.0F, 0.0F));
				bb_main.addOrReplaceChild("cube_r17", CubeListBuilder.create().texOffs(2, 26).addBox(-2.0F, -5.0F, -3.0F, 4.0F, 10.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 5.0F, 0.0F, -3.1416F, 0.0F, 0.0F));
				return bb_main;
			},
			partDefinition -> {
				PartDefinition bb_main = partDefinition.addOrReplaceChild("bb_main", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));
				bb_main.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(22, 42).addBox(-0.5F, -0.5F, -2.0F, 1.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.5F, 0.5F, 0.0F, 3.1416F, 0.0F, 0.0F));
				bb_main.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(32, 35).addBox(-0.5F, -1.0F, -2.0F, 1.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.1156F, 6.088F, 0.0F, 0.0F, 0.0F, -2.5744F));
				bb_main.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(22, 30).addBox(-0.5F, -5.0F, -0.5F, 1.0F, 10.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.5F, 5.0F, 2.5F, 0.0F, 0.0F, 3.1416F));
				bb_main.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(22, 37).addBox(-0.5F, -0.5F, -2.0F, 1.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.5F, 9.5F, 0.0F, 0.0F, 0.0F, 3.1416F));
				bb_main.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(22, 30).addBox(-0.5F, -5.0F, -0.5F, 1.0F, 10.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.5F, 5.0F, -2.5F, 0.0F, 1.5708F, 3.1416F));
				bb_main.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(32, 35).addBox(-0.5F, -1.0F, -2.0F, 1.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.1156F, 8.088F, 0.0F, 0.0F, 0.0F, -2.5744F));
				bb_main.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(32, 35).addBox(-0.5F, -1.0F, -2.0F, 1.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.1156F, 4.088F, 0.0F, 0.0F, 0.0F, -2.5744F));
				bb_main.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(22, 42).addBox(-0.5F, -0.5F, -2.0F, 1.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.5F, 0.5F, 0.0F, 0.0F, 0.0F, -3.1416F));
				bb_main.addOrReplaceChild("cube_r9", CubeListBuilder.create().texOffs(32, 35).addBox(-0.5F, -1.0F, -2.0F, 1.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.1156F, 4.088F, 0.0F, -3.1416F, 0.0F, -0.5672F));
				bb_main.addOrReplaceChild("cube_r10", CubeListBuilder.create().texOffs(32, 35).addBox(-0.5F, -1.0F, -2.0F, 1.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.1156F, 6.088F, 0.0F, -3.1416F, 0.0F, -0.5672F));
				bb_main.addOrReplaceChild("cube_r11", CubeListBuilder.create().texOffs(22, 37).addBox(-0.5F, -0.5F, -2.0F, 1.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.5F, 9.5F, 0.0F, -3.1416F, 0.0F, 0.0F));
				bb_main.addOrReplaceChild("cube_r12", CubeListBuilder.create().texOffs(32, 35).addBox(-0.5F, -1.0F, -2.0F, 1.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.1156F, 8.088F, 0.0F, -3.1416F, 0.0F, -0.5672F));
				bb_main.addOrReplaceChild("cube_r13", CubeListBuilder.create().texOffs(22, 30).addBox(-0.5F, -5.0F, -0.5F, 1.0F, 10.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.5F, 5.0F, -2.5F, -3.1416F, 0.0F, 0.0F));
				bb_main.addOrReplaceChild("cube_r14", CubeListBuilder.create().texOffs(22, 30).addBox(-0.5F, -5.0F, -0.5F, 1.0F, 10.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.5F, 5.0F, 2.5F, 0.0F, -1.5708F, 3.1416F));
				bb_main.addOrReplaceChild("cube_r15", CubeListBuilder.create().texOffs(28, 32).addBox(-0.5F, -4.0F, -0.5F, 1.0F, 8.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.5F, 5.0F, 3.0F, 0.0F, 0.0F, 3.1416F));
				bb_main.addOrReplaceChild("cube_r16", CubeListBuilder.create().texOffs(28, 32).addBox(-0.5F, -4.0F, -0.5F, 1.0F, 8.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.5F, 5.0F, 3.0F, 0.0F, 1.5708F, 3.1416F));
				bb_main.addOrReplaceChild("cube_r17", CubeListBuilder.create().texOffs(28, 32).addBox(-0.5F, -4.0F, -0.5F, 1.0F, 8.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.5F, 5.0F, -3.0F, 0.0F, -1.5708F, 3.1416F));
				bb_main.addOrReplaceChild("cube_r18", CubeListBuilder.create().texOffs(28, 32).addBox(-0.5F, -4.0F, -0.5F, 1.0F, 8.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.5F, 5.0F, -3.0F, -3.1416F, 0.0F, 0.0F));
				bb_main.addOrReplaceChild("cube_r19", CubeListBuilder.create().texOffs(2, 26).addBox(-2.0F, -5.0F, -3.0F, 4.0F, 10.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 5.0F, 0.0F, -3.1416F, 0.0F, 0.0F));
				return bb_main;
			},
			partDefinition -> {
				PartDefinition bb_main = partDefinition.addOrReplaceChild("bb_main", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));
				bb_main.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(22, 42).addBox(-0.5F, -0.5F, -2.0F, 1.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.5F, 0.5F, 0.0F, 3.1416F, 0.0F, 0.0F));
				bb_main.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(32, 35).addBox(-0.5F, -1.0F, -2.0F, 1.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.1156F, 6.088F, 0.0F, 0.0F, 0.0F, -2.5744F));
				bb_main.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(22, 30).addBox(-0.5F, -5.0F, -0.5F, 1.0F, 10.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.5F, 5.0F, 2.5F, 0.0F, 0.0F, 3.1416F));
				bb_main.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(22, 37).addBox(-0.5F, -0.5F, -2.0F, 1.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.5F, 9.5F, 0.0F, 0.0F, 0.0F, 3.1416F));
				bb_main.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(22, 30).addBox(-0.5F, -5.0F, -0.5F, 1.0F, 10.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.5F, 5.0F, -2.5F, 0.0F, 1.5708F, 3.1416F));
				bb_main.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(32, 35).addBox(-0.5F, -1.0F, -2.0F, 1.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.1156F, 8.088F, 0.0F, 0.0F, 0.0F, -2.5744F));
				bb_main.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(32, 35).addBox(-0.5F, -1.0F, -2.0F, 1.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.1156F, 4.088F, 0.0F, 0.0F, 0.0F, -2.5744F));
				bb_main.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(32, 35).addBox(-0.5F, -1.0F, -2.0F, 1.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.1156F, 2.088F, 0.0F, 0.0F, 0.0F, -2.5744F));
				bb_main.addOrReplaceChild("cube_r9", CubeListBuilder.create().texOffs(22, 42).addBox(-0.5F, -0.5F, -2.0F, 1.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.5F, 0.5F, 0.0F, 0.0F, 0.0F, -3.1416F));
				bb_main.addOrReplaceChild("cube_r10", CubeListBuilder.create().texOffs(32, 35).addBox(-0.5F, -1.0F, -2.0F, 1.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.1156F, 2.088F, 0.0F, -3.1416F, 0.0F, -0.5672F));
				bb_main.addOrReplaceChild("cube_r11", CubeListBuilder.create().texOffs(32, 35).addBox(-0.5F, -1.0F, -2.0F, 1.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.1156F, 4.088F, 0.0F, -3.1416F, 0.0F, -0.5672F));
				bb_main.addOrReplaceChild("cube_r12", CubeListBuilder.create().texOffs(32, 35).addBox(-0.5F, -1.0F, -2.0F, 1.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.1156F, 6.088F, 0.0F, -3.1416F, 0.0F, -0.5672F));
				bb_main.addOrReplaceChild("cube_r13", CubeListBuilder.create().texOffs(22, 37).addBox(-0.5F, -0.5F, -2.0F, 1.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.5F, 9.5F, 0.0F, -3.1416F, 0.0F, 0.0F));
				bb_main.addOrReplaceChild("cube_r14", CubeListBuilder.create().texOffs(32, 35).addBox(-0.5F, -1.0F, -2.0F, 1.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.1156F, 8.088F, 0.0F, -3.1416F, 0.0F, -0.5672F));
				bb_main.addOrReplaceChild("cube_r15", CubeListBuilder.create().texOffs(22, 30).addBox(-0.5F, -5.0F, -0.5F, 1.0F, 10.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.5F, 5.0F, -2.5F, -3.1416F, 0.0F, 0.0F));
				bb_main.addOrReplaceChild("cube_r16", CubeListBuilder.create().texOffs(22, 30).addBox(-0.5F, -5.0F, -0.5F, 1.0F, 10.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.5F, 5.0F, 2.5F, 0.0F, -1.5708F, 3.1416F));
				bb_main.addOrReplaceChild("cube_r17", CubeListBuilder.create().texOffs(28, 32).addBox(-0.5F, -4.0F, -0.5F, 1.0F, 8.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.5F, 5.0F, 3.0F, 0.0F, 0.0F, 3.1416F));
				bb_main.addOrReplaceChild("cube_r18", CubeListBuilder.create().texOffs(28, 32).addBox(-0.5F, -4.0F, -0.5F, 1.0F, 8.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.5F, 5.0F, 3.0F, 0.0F, 1.5708F, 3.1416F));
				bb_main.addOrReplaceChild("cube_r19", CubeListBuilder.create().texOffs(28, 32).addBox(-0.5F, -4.0F, -0.5F, 1.0F, 8.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.5F, 5.0F, -3.0F, 0.0F, -1.5708F, 3.1416F));
				bb_main.addOrReplaceChild("cube_r20", CubeListBuilder.create().texOffs(28, 32).addBox(-0.5F, -4.0F, -0.5F, 1.0F, 8.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.5F, 5.0F, -3.0F, -3.1416F, 0.0F, 0.0F));
				bb_main.addOrReplaceChild("cube_r21", CubeListBuilder.create().texOffs(2, 26).addBox(-2.0F, -5.0F, -3.0F, 4.0F, 10.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 5.0F, 0.0F, -3.1416F, 0.0F, 0.0F));
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
		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public ResourceLocation getTexture(int stage) {
		return ResourceLocation.fromNamespaceAndPath(CreateMissiles.MOD_ID,"textures/entity/excavator.png");
	}

	@Override
	public int getStageCount() {
		return layers.size();
	}
}