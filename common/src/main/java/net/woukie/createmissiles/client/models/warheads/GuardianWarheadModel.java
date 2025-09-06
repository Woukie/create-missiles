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

public class GuardianWarheadModel implements MissilePartModel {
	private final List<Map<String, Vector3f>> attachments = List.of(
			new HashMap<>() {{
				put("bottom", new Vector3f(0, 0, 0));
				put("top", new Vector3f(0, 0, 0));
			}},
			new HashMap<>() {{
				put("bottom", new Vector3f(0, 0, 0));
				put("top", new Vector3f(0, 6, 0));
			}},
			new HashMap<>() {{
				put("bottom", new Vector3f(0, 0, 0));
				put("top", new Vector3f(0, 6, 0));
			}},
			new HashMap<>() {{
				put("bottom", new Vector3f(0, 0, 0));
				put("top", new Vector3f(0, 6.6f, 0));
			}}
	);

	private final List<Function<PartDefinition, PartDefinition>> layers = List.of(
			partDefinition -> partDefinition.addOrReplaceChild("bb_main", CubeListBuilder.create(), PartPose.ZERO),
			partDefinition -> {
				PartDefinition bb_main = partDefinition.addOrReplaceChild("bb_main", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));
				bb_main.addOrReplaceChild("head_r1", CubeListBuilder.create().texOffs(24, 0).addBox(-3.0F, -3.0F, -3.0F, 6.0F, 6.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 3.0F, 0.0F, 3.1416F, 0.7854F, 0.0F));
				return bb_main;
			},
			partDefinition -> {
				PartDefinition bb_main = partDefinition.addOrReplaceChild("bb_main", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));
				bb_main.addOrReplaceChild("insides_r1", CubeListBuilder.create().texOffs(0, 20).addBox(-2.5F, -2.5F, -2.5F, 5.0F, 5.0F, 5.0F, new CubeDeformation(0.0F))
						.texOffs(48, 0).addBox(-3.0F, -3.0F, -3.0F, 6.0F, 6.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 3.0F, 0.0F, 3.1416F, 0.7854F, 0.0F));
				return bb_main;
			},
			partDefinition -> {
				PartDefinition guardian = partDefinition.addOrReplaceChild("guardian", CubeListBuilder.create().texOffs(12, 2).addBox(-1.0F, -1.0F, -1.6563F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
						.texOffs(14, 2).addBox(-0.5F, -0.5F, -2.1563F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
						.texOffs(15, -1).addBox(0.0F, -1.0F, -2.6563F, 0.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.2431F, 3.0F, -0.2431F, 0.0F, -0.7854F, 0.0F));
				PartDefinition Spike12_r1 = guardian.addOrReplaceChild("Spike12_r1", CubeListBuilder.create().texOffs(14, 0).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.1464F, 0.0F, 1.4902F, 0.0F, 0.7854F, 0.0F));
				PartDefinition Spike11_r1 = guardian.addOrReplaceChild("Spike11_r1", CubeListBuilder.create().texOffs(14, 0).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -1.1464F, -0.8027F, 0.7854F, 0.0F, 0.0F));
				PartDefinition Spike10_r1 = guardian.addOrReplaceChild("Spike10_r1", CubeListBuilder.create().texOffs(14, 0).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 1.1464F, -0.8027F, -0.7854F, 0.0F, 0.0F));
				PartDefinition Spike9_r1 = guardian.addOrReplaceChild("Spike9_r1", CubeListBuilder.create().texOffs(14, 0).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 1.1464F, 1.4902F, 0.7854F, 0.0F, 0.0F));
				PartDefinition Spike8_r1 = guardian.addOrReplaceChild("Spike8_r1", CubeListBuilder.create().texOffs(14, 0).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -1.1464F, 1.4902F, -0.7854F, 0.0F, 0.0F));
				PartDefinition Spike7_r1 = guardian.addOrReplaceChild("Spike7_r1", CubeListBuilder.create().texOffs(14, 0).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.1464F, -1.1464F, 0.3437F, 0.0F, 0.0F, -0.7854F));
				PartDefinition Spike6_r1 = guardian.addOrReplaceChild("Spike6_r1", CubeListBuilder.create().texOffs(14, 0).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.1464F, -1.1464F, 0.3437F, 0.0F, 0.0F, 0.7854F));
				PartDefinition Spike5_r1 = guardian.addOrReplaceChild("Spike5_r1", CubeListBuilder.create().texOffs(14, 0).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.1464F, 1.1464F, 0.3437F, 0.0F, 0.0F, -0.7854F));
				PartDefinition Spike4_r1 = guardian.addOrReplaceChild("Spike4_r1", CubeListBuilder.create().texOffs(14, 0).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.1464F, 1.1464F, 0.3437F, 0.0F, 0.0F, 0.7854F));
				PartDefinition Spike3_r1 = guardian.addOrReplaceChild("Spike3_r1", CubeListBuilder.create().texOffs(14, 0).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.1464F, 0.0F, -0.8027F, 0.0F, -0.7854F, 0.0F));
				PartDefinition Spike2_r1 = guardian.addOrReplaceChild("Spike2_r1", CubeListBuilder.create().texOffs(14, 0).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.1464F, 0.0F, -0.8027F, 0.0F, 0.7854F, 0.0F));
				PartDefinition Spike1_r1 = guardian.addOrReplaceChild("Spike1_r1", CubeListBuilder.create().texOffs(14, 0).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.1464F, 0.0F, 1.4902F, 0.0F, -0.7854F, 0.0F));
				PartDefinition Body_r1 = guardian.addOrReplaceChild("Body_r1", CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, -1.5F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.3437F, 0.0F, 0.0F, -3.1416F));
				PartDefinition bowl = partDefinition.addOrReplaceChild("bowl", CubeListBuilder.create(), PartPose.offset(0.0F, 2.4736F, 0.0F));
				PartDefinition cube_r1 = bowl.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(30, 2).addBox(-2.0F, -1.0F, -0.5F, 4.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.3536F, 0.9656F, -2.3536F, -2.3562F, 0.0F, -1.5708F));
				PartDefinition cube_r2 = bowl.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(30, 2).addBox(-2.0F, -1.0F, -0.5F, 4.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.3536F, 0.9656F, -2.3536F, 2.3562F, 0.0F, -1.5708F));
				PartDefinition cube_r3 = bowl.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(30, 2).addBox(-2.0F, -1.0F, -0.5F, 4.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.3536F, 0.9656F, 2.3536F, 0.7854F, 0.0F, -1.5708F));
				PartDefinition cube_r4 = bowl.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(30, 2).addBox(-2.0F, -1.0F, -0.5F, 4.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.3536F, 0.9656F, 2.3536F, -0.7854F, 0.0F, -1.5708F));
				PartDefinition cube_r5 = bowl.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(30, 2).addBox(-2.0F, -1.0F, -0.5F, 4.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.3536F, -1.4129F, -0.025F, 0.0F, -1.5708F, 2.3562F));
				PartDefinition cube_r6 = bowl.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(30, 2).addBox(-2.0F, -1.0F, -0.5F, 4.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.3536F, -1.4129F, 0.025F, 0.0F, 1.5708F, -2.3562F));
				PartDefinition cube_r7 = bowl.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(30, 2).addBox(-2.0F, -1.0F, -0.5F, 4.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.025F, -1.4129F, -2.3536F, 2.3562F, 0.0F, 0.0F));
				PartDefinition cube_r8 = bowl.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(30, 2).addBox(-2.0F, -1.0F, -0.5F, 4.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.025F, -1.4129F, 2.3536F, -0.7854F, 0.0F, -3.1416F));
				PartDefinition cube_r9 = bowl.addOrReplaceChild("cube_r9", CubeListBuilder.create().texOffs(20, 0).addBox(-2.0F, -2.0F, -0.5F, 4.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -1.9736F, 0.0F, 1.5708F, 0.0F, 0.0F));
				PartDefinition cube_r10 = bowl.addOrReplaceChild("cube_r10", CubeListBuilder.create().texOffs(20, 0).addBox(-2.0F, -2.0F, -0.5F, 4.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.9142F, 0.9406F, 0.0F, 0.0F, 1.5708F, 3.1416F));
				PartDefinition cube_r11 = bowl.addOrReplaceChild("cube_r11", CubeListBuilder.create().texOffs(20, 0).addBox(-2.0F, -2.0F, -0.5F, 4.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.9142F, 0.9406F, 0.0F, 0.0F, -1.5708F, -3.1416F));
				PartDefinition cube_r12 = bowl.addOrReplaceChild("cube_r12", CubeListBuilder.create().texOffs(20, 0).addBox(-2.0F, -2.0F, -0.5F, 4.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.9406F, -2.9142F, -3.1416F, 0.0F, 0.0F));
				PartDefinition cube_r13 = bowl.addOrReplaceChild("cube_r13", CubeListBuilder.create().texOffs(20, 0).addBox(-2.0F, -2.0F, -0.5F, 4.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.9406F, 2.9142F, 0.0F, 0.0F, -3.1416F));
				return guardian;
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
		return LayerDefinition.create(meshdefinition, 40, 15);
	}

	@Override
	public ResourceLocation getTexture(int stage) {
		return new ResourceLocation(CreateMissiles.MOD_ID,"textures/entity/guardian_warhead.png");
	}

	@Override
	public int getStageCount() {
		return layers.size();
	}
}