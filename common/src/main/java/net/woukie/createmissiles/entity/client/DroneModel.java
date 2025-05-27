package net.woukie.createmissiles.entity.client;

// Made with Blockbench 4.12.4
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

public class DroneModel<T extends Entity> extends EntityModel<T> {
    // This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("modid", "drone"), "main");
    private final ModelPart bb_main;

    public DroneModel() {
        this.bb_main = root.getChild("bb_main");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition bb_main = partdefinition.addOrReplaceChild("bb_main", CubeListBuilder.create().texOffs(-26, -24).addBox(-2.0F, -14.0F, -14.0F, 4.0F, 4.0F, 26.0F, new CubeDeformation(0.0F))
                .texOffs(-8, -5).addBox(-2.5F, -15.0F, 12.0F, 5.0F, 5.0F, 7.0F, new CubeDeformation(0.0F))
                .texOffs(-5, -5).addBox(-1.0F, -16.0F, -14.0F, 2.0F, 2.0F, 7.0F, new CubeDeformation(0.0F))
                .texOffs(-21, -2).addBox(-23.0F, -12.0F, -2.0F, 21.0F, 2.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(-7, 1).addBox(-12.0F, -12.0F, 2.0F, 10.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(-21, -2).addBox(2.0F, -12.0F, -2.0F, 21.0F, 2.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(-7, 1).addBox(2.0F, -12.0F, 2.0F, 10.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(-9, -5).addBox(-3.0F, -16.0F, -21.0F, 6.0F, 6.0F, 7.0F, new CubeDeformation(0.0F))
                .texOffs(0, -1).addBox(9.0F, -3.0F, -10.0F, 1.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(0, -1).addBox(-10.0F, -3.0F, -10.0F, 1.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(0, -1).addBox(-0.5F, -3.0F, 12.0F, 1.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition cube_r1 = bb_main.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(-8, 1).addBox(-12.2856F, 1.5321F, -1.0F, 11.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -10.0F, -9.0F, 0.0F, 3.1416F, 2.2689F));

        PartDefinition cube_r2 = bb_main.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(-9, -10).addBox(-0.5F, -1.0F, -1.0F, 1.0F, 1.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -9.0F, 5.0F, -0.6109F, 0.0F, 0.0F));

        PartDefinition cube_r3 = bb_main.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(-8, 1).addBox(-11.0F, 0.0F, -2.0F, 11.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.0F, -10.0F, -7.0F, 0.0F, 0.0F, -2.2689F));

        PartDefinition cube_r4 = bb_main.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(-13, -1).addBox(0.0F, 0.0F, -2.0F, 14.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.0F, -14.0F, -17.0F, 0.0F, 0.0F, -0.4363F));

        PartDefinition cube_r5 = bb_main.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(-13, -1).addBox(-14.0F, 0.0F, -2.0F, 14.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.0F, -14.0F, -17.0F, 0.0F, 0.0F, 0.4363F));

        return LayerDefinition.create(meshdefinition, 16, 16);
    }

    @Override
    public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        bb_main.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}