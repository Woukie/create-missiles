// Made with Blockbench 4.12.6
package net.woukie.createmissiles.client.models;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.woukie.createmissiles.CreateMissiles;
import net.woukie.createmissiles.entity.drone.Drone;
import org.jetbrains.annotations.NotNull;

public class DroneModel<T extends Drone> extends EntityModel<T> {
    // This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(CreateMissiles.MOD_ID, "drone"), "main");

    private final ModelPart body;
    private final ModelPart head;
    private final ModelPart left_wing;
    private final ModelPart left_wing_tip;
    private final ModelPart right_wing;
    private final ModelPart right_wing_tip;
    private final ModelPart tail;
    private final ModelPart tail2;
    private final ModelPart bb_main;

    public DroneModel(ModelPart root) {
        this.body = root.getChild("body");
        this.head = root.getChild("head");
        this.left_wing = root.getChild("left_wing");
        this.left_wing_tip = root.getChild("left_wing_tip");
        this.right_wing = root.getChild("right_wing");
        this.right_wing_tip = root.getChild("right_wing_tip");
        this.tail = root.getChild("tail");
        this.tail2 = root.getChild("tail2");
        this.bb_main = root.getChild("bb_main");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 8).addBox(-3.0F, -2.0F, -8.0F, 5.0F, 3.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offset(0.5F, 19.0F, 0.0F));
        PartDefinition head = partdefinition.addOrReplaceChild("head", CubeListBuilder.create(), PartPose.offset(0.5F, 20.25F, -8.0F));
        PartDefinition left_wing = partdefinition.addOrReplaceChild("left_wing", CubeListBuilder.create(), PartPose.offset(2.5F, 17.0F, -8.0F));
        PartDefinition left_wing_r1 = left_wing.addOrReplaceChild("left_wing_r1", CubeListBuilder.create().texOffs(23, 12).addBox(0.0F, 0.0F, -5.0F, 6.0F, 2.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 5.0F, 0.0F, 0.0F, 0.0698F));
        PartDefinition left_wing_tip = partdefinition.addOrReplaceChild("left_wing_tip", CubeListBuilder.create(), PartPose.offset(8.5F, 17.0F, -8.0F));
        PartDefinition right_wing = partdefinition.addOrReplaceChild("right_wing", CubeListBuilder.create(), PartPose.offset(-2.5F, 17.0F, -8.0F));
        PartDefinition right_wing_r1 = right_wing.addOrReplaceChild("right_wing_r1", CubeListBuilder.create().texOffs(23, 12).mirror().addBox(-6.0F, 0.0F, -5.0F, 6.0F, 2.0F, 9.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 5.0F, 0.0F, 0.0F, -0.0698F));
        PartDefinition right_wing_tip = partdefinition.addOrReplaceChild("right_wing_tip", CubeListBuilder.create(), PartPose.offset(-8.5F, 17.0F, -8.0F));
        PartDefinition right_wing_tip_r1 = right_wing_tip.addOrReplaceChild("right_wing_tip_r1", CubeListBuilder.create().texOffs(19, 0).addBox(-3.0F, -0.5F, -4.5F, 6.0F, 1.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(19.9724F, 0.7105F, 4.5F, 0.0F, 0.0F, -0.0698F));
        PartDefinition right_wing_tip_r2 = right_wing_tip.addOrReplaceChild("right_wing_tip_r2", CubeListBuilder.create().texOffs(19, 0).mirror().addBox(0.0F, 0.0F, -9.0F, 6.0F, 1.0F, 9.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-6.0F, 0.0F, 9.0F, 0.0F, 0.0F, 0.0698F));
        PartDefinition tail = partdefinition.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(3, 20).addBox(-2.0F, 0.0F, 0.0F, 3.0F, 2.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.5F, 17.0F, 1.0F));
        PartDefinition tail2 = partdefinition.addOrReplaceChild("tail2", CubeListBuilder.create(), PartPose.offset(0.5F, 17.5F, 7.0F));
        PartDefinition tail2_r1 = tail2.addOrReplaceChild("tail2_r1", CubeListBuilder.create().texOffs(4, 29).addBox(-0.5F, -0.5F, -0.05F, 1.0F, 1.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5F, 0.125F, -0.4F, -0.829F, 0.0F, 0.0F));
        PartDefinition bb_main = partdefinition.addOrReplaceChild("bb_main", CubeListBuilder.create().texOffs(0, 36).addBox(-0.5F, -7.7071F, -7.2929F, 19.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(19, 10).mirror().addBox(-16.5F, -7.0F, -8.0F, 2.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(19, 10).mirror().addBox(14.5F, -7.0F, -8.0F, 2.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(11, 0).addBox(-5.0F, -3.0F, -8.0F, 1.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(11, 0).addBox(4.0F, -3.0F, -8.0F, 1.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(11, 0).addBox(-0.5F, -3.2F, 2.6F, 1.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));
        PartDefinition cube_r1 = bb_main.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(7, 0).addBox(-0.5F, 0.0F, 0.0F, 1.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.1F, -5.05F, 0.625F, 0.6981F, 0.0F, 0.0F));
        PartDefinition cube_r2 = bb_main.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(7, 0).addBox(-0.5F, -2.5F, -0.5F, 1.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.435F, -3.3266F, -6.053F, -0.2182F, 0.0F, -0.5672F));
        PartDefinition cube_r3 = bb_main.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(7, 0).addBox(-0.5F, -2.5F, -0.5F, 1.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.435F, -3.3266F, -6.053F, -0.2182F, 0.0F, 0.5672F));
        PartDefinition cube_r4 = bb_main.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(19, 0).addBox(0.0F, 0.0F, 0.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.5F, -7.0F, 4.0F, 0.0F, -1.5708F, 1.0472F));
        PartDefinition cube_r5 = bb_main.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(19, 0).addBox(0.0F, 0.0F, 0.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.5F, -7.0F, 6.0F, 0.0F, 1.5708F, -1.0472F));
        PartDefinition right_wing_tip_r3 = bb_main.addOrReplaceChild("right_wing_tip_r3", CubeListBuilder.create().texOffs(12, 23).addBox(0.0F, 0.0F, 0.0F, 5.0F, 1.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(16.5F, -7.0F, -7.975F, 0.0F, 0.0F, 0.7418F));
        PartDefinition cube_r6 = bb_main.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(19, 0).addBox(-1.0F, -0.5F, -0.5F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(19, 0).addBox(-32.0F, -0.5F, -0.5F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(15.5F, -7.0F, -7.2929F, 0.7854F, 0.0F, 0.0F));
        PartDefinition cube_r7 = bb_main.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(19, 0).addBox(-1.0F, -0.5F, -0.5F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(15.5F, -7.0F, -5.2929F, -2.3562F, 0.0F, 3.1416F));
        PartDefinition cube_r8 = bb_main.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(19, 2).addBox(-1.0F, -0.5F, -0.5F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(15.5F, -6.5F, -5.0858F, 3.1416F, 0.0F, 3.1416F));
        PartDefinition right_wing_tip_r4 = bb_main.addOrReplaceChild("right_wing_tip_r4", CubeListBuilder.create().texOffs(12, 23).mirror().addBox(-5.0F, 0.0F, -9.0F, 5.0F, 1.0F, 9.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-16.5F, -7.0F, 1.025F, 0.0F, 0.0F, -0.7418F));
        PartDefinition cube_r9 = bb_main.addOrReplaceChild("cube_r9", CubeListBuilder.create().texOffs(0, 39).addBox(-9.0F, -2.091F, 1.0F, 16.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.0F, -5.775F, -1.0F, 0.0F, 1.5708F, 0.0F));
        PartDefinition cube_r10 = bb_main.addOrReplaceChild("cube_r10", CubeListBuilder.create().texOffs(0, 50).addBox(-3.5F, -3.5F, -3.5F, 7.0F, 7.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -5.0F, -10.5F, 1.1257F, 0.0F, 0.0F));
        PartDefinition cube_r11 = bb_main.addOrReplaceChild("cube_r11", CubeListBuilder.create().texOffs(19, 2).addBox(-1.0F, -0.5F, -0.5F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-15.5F, -6.5F, -5.0858F, 3.1416F, 0.0F, 3.1416F));
        PartDefinition cube_r12 = bb_main.addOrReplaceChild("cube_r12", CubeListBuilder.create().texOffs(19, 0).addBox(-1.0F, -0.5F, -0.5F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-15.5F, -7.0F, -5.2929F, -2.3562F, 0.0F, 3.1416F));
        PartDefinition cube_r13 = bb_main.addOrReplaceChild("cube_r13", CubeListBuilder.create().texOffs(1, 36).addBox(-9.0F, -0.5F, -1.0F, 18.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-9.5F, -7.2071F, -6.2929F, -3.1416F, 0.0F, 3.1416F));
        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, int i2) {
        body.render(poseStack, vertexConsumer, packedLight, packedOverlay);
        head.render(poseStack, vertexConsumer, packedLight, packedOverlay);
        left_wing.render(poseStack, vertexConsumer, packedLight, packedOverlay);
        left_wing_tip.render(poseStack, vertexConsumer, packedLight, packedOverlay);
        right_wing.render(poseStack, vertexConsumer, packedLight, packedOverlay);
        right_wing_tip.render(poseStack, vertexConsumer, packedLight, packedOverlay);
        tail.render(poseStack, vertexConsumer, packedLight, packedOverlay);
        tail2.render(poseStack, vertexConsumer, packedLight, packedOverlay);
        bb_main.render(poseStack, vertexConsumer, packedLight, packedOverlay);
    }

    @Override
    public void setupAnim(@NotNull T entity, float f, float g, float h, float i, float j) {

    }
}