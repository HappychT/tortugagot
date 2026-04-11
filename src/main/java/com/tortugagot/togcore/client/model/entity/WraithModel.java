package com.tortugagot.togcore.client.model.entity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;

public class WraithModel extends ModelBase {

    private final ModelRenderer body;
    private final ModelRenderer h_head;
    private final ModelRenderer upper_body;
    private final ModelRenderer upper_body_cloth;
    private final ModelRenderer left_arm;
    private final ModelRenderer cube_r1;
    private final ModelRenderer cube_r2;
    private final ModelRenderer cube_r3;
    private final ModelRenderer cube_r4;
    private final ModelRenderer left_arm_bone;
    private final ModelRenderer cube_r5;
    private final ModelRenderer right_arm;
    private final ModelRenderer cube_r6;
    private final ModelRenderer cube_r7;
    private final ModelRenderer cube_r8;
    private final ModelRenderer cube_r9;
    private final ModelRenderer right_arm_bone;
    private final ModelRenderer cube_r10;
    private final ModelRenderer hitbox;

    public WraithModel() {
        textureWidth = 64;
        textureHeight = 64;

        body = new ModelRenderer(this);
        body.setRotationPoint(0.0F, 10.0F, 0.0F);

        h_head = new ModelRenderer(this);
        h_head.setRotationPoint(-0.0002F, -2.1729F, 1.3423F);
        body.addChild(h_head);
        h_head.cubeList.add(new ModelBox(h_head, 12, 2, -3.9998F, -7.8271F, -4.3423F, 8, 6, 8, 0.0F));
        h_head.cubeList.add(new ModelBox(h_head, 9, 49, -2.9998F, -1.8271F, -4.3423F, 2, 2, 2, 0.0F));
        h_head.cubeList.add(new ModelBox(h_head, 9, 45, 1.0002F, -1.8271F, -4.3423F, 2, 2, 2, 0.0F));
        h_head.cubeList.add(new ModelBox(h_head, 18, 56, -3.9998F, -1.8271F, -2.3423F, 8, 2, 6, 0.0F));
        h_head.cubeList.add(new ModelBox(h_head, 0, -9, 4.5002F, -8.3271F, -4.8423F, 0, 10, 9, 0.0F));
        h_head.cubeList.add(new ModelBox(h_head, 35, 16, -4.5008F, -8.3261F, -4.8423F, 9, 0, 9, 0.0F));
        h_head.cubeList.add(new ModelBox(h_head, 0, 61, -4.5008F, -8.3261F, -4.8423F, 9, 3, 0, 0.0F));
        h_head.cubeList.add(new ModelBox(h_head, 44, 6, -4.5008F, -8.3261F, 4.1577F, 9, 10, 0, 0.0F));
        h_head.cubeList.add(new ModelBox(h_head, 11, 43, 1.0002F, -4.8271F, -4.4423F, 2, 2, 0, 0.0F));
        h_head.cubeList.add(new ModelBox(h_head, 22, 61, 1.5002F, -4.8271F, -4.5173F, 1, 1, 0, 0.0F));
        h_head.cubeList.add(new ModelBox(h_head, 0, -9, -4.4998F, -8.3271F, -4.8423F, 0, 10, 9, 0.0F));
        h_head.cubeList.add(new ModelBox(h_head, 11, 43, -2.9998F, -4.8271F, -4.4423F, 2, 2, 0, 0.0F));
        h_head.cubeList.add(new ModelBox(h_head, 22, 61, -2.4998F, -4.8271F, -4.5173F, 1, 1, 0, 0.0F));

        upper_body = new ModelRenderer(this);
        upper_body.setRotationPoint(0.0F, -1.7859F, 1.8554F);
        body.addChild(upper_body);
        upper_body.cubeList.add(new ModelBox(upper_body, 45, 53, -1.0F, -0.2188F, -0.9375F, 2, 7, 2, 0.0F));
        upper_body.cubeList.add(new ModelBox(upper_body, 48, 37, 0.75F, 0.2813F, -3.9375F, 3, 1, 0, 0.0F));
        upper_body.cubeList.add(new ModelBox(upper_body, 46, 31, 3.75F, 0.2813F, -3.9375F, 0, 1, 4, 0.0F));
        upper_body.cubeList.add(new ModelBox(upper_body, 48, 36, 0.75F, 0.2813F, 0.0625F, 3, 1, 0, 0.0F));
        upper_body.cubeList.add(new ModelBox(upper_body, 48, 37, -3.75F, 0.2813F, -3.9375F, 3, 1, 0, 0.0F));
        upper_body.cubeList.add(new ModelBox(upper_body, 48, 36, -3.75F, 0.2813F, 0.0625F, 3, 1, 0, 0.0F));
        upper_body.cubeList.add(new ModelBox(upper_body, 46, 31, -3.75F, 0.2813F, -3.9375F, 0, 1, 4, 0.0F));
        upper_body.cubeList.add(new ModelBox(upper_body, 48, 36, 0.75F, 2.2813F, 0.0625F, 3, 1, 0, 0.0F));
        upper_body.cubeList.add(new ModelBox(upper_body, 46, 31, 3.75F, 2.2813F, -3.9375F, 0, 1, 4, 0.0F));
        upper_body.cubeList.add(new ModelBox(upper_body, 48, 37, 0.75F, 2.2813F, -3.9375F, 3, 1, 0, 0.0F));
        upper_body.cubeList.add(new ModelBox(upper_body, 48, 36, -3.75F, 2.2813F, 0.0625F, 3, 1, 0, 0.0F));
        upper_body.cubeList.add(new ModelBox(upper_body, 46, 31, -3.75F, 2.2813F, -3.9375F, 0, 1, 4, 0.0F));
        upper_body.cubeList.add(new ModelBox(upper_body, 48, 37, -3.75F, 2.2813F, -3.9375F, 3, 1, 0, 0.0F));

        upper_body_cloth = new ModelRenderer(this);
        upper_body_cloth.setRotationPoint(0.0F, -0.2188F, 3.0625F);
        upper_body.addChild(upper_body_cloth);
        upper_body_cloth.cubeList.add(new ModelBox(upper_body_cloth, 12, 47, -4.0F, 0.0F, -6.0F, 0, 8, 6, 0.0F));
        upper_body_cloth.cubeList.add(new ModelBox(upper_body_cloth, 44, 25, -4.0F, 0.0F, 0.0F, 8, 8, 0, 0.0F));
        upper_body_cloth.cubeList.add(new ModelBox(upper_body_cloth, 12, 47, 4.0F, 0.0F, -6.0F, 0, 8, 6, 0.0F));

        left_arm = new ModelRenderer(this);
        left_arm.setRotationPoint(5.4288F, 1.086F, 1.163F);
        upper_body.addChild(left_arm);

        cube_r1 = new ModelRenderer(this);
        cube_r1.setRotationPoint(-0.0333F, 0.7F, -0.0055F);
        left_arm.addChild(cube_r1);
        setRotationAngle(cube_r1, 0.0F, -0.3927F, 0.0F);
        cube_r1.cubeList.add(new ModelBox(cube_r1, 0, 15, -1.5F, -2.5F, 0.0F, 3, 5, 0, 0.0F));

        cube_r2 = new ModelRenderer(this);
        cube_r2.setRotationPoint(1.1148F, -1.8F, -2.7772F);
        left_arm.addChild(cube_r2);
        setRotationAngle(cube_r2, 0.0F, -0.3927F, 0.0F);
        cube_r2.cubeList.add(new ModelBox(cube_r2, 51, 56, -1.5F, 0.0F, -3.0F, 3, 0, 6, 0.0F));

        cube_r3 = new ModelRenderer(this);
        cube_r3.setRotationPoint(2.5006F, 0.7F, -2.2031F);
        left_arm.addChild(cube_r3);
        setRotationAngle(cube_r3, 0.0F, -0.3927F, 0.0F);
        cube_r3.cubeList.add(new ModelBox(cube_r3, 0, 50, 0.0F, -2.5F, -3.0F, 0, 5, 6, 0.0F));

        cube_r4 = new ModelRenderer(this);
        cube_r4.setRotationPoint(-0.271F, 0.7F, -3.3512F);
        left_arm.addChild(cube_r4);
        setRotationAngle(cube_r4, 0.0F, -0.3927F, 0.0F);
        cube_r4.cubeList.add(new ModelBox(cube_r4, 0, 4, 0.0F, -2.5F, -3.0F, 0, 5, 6, 0.0F));

        left_arm_bone = new ModelRenderer(this);
        left_arm_bone.setRotationPoint(0.4388F, -0.3F, -1.163F);
        left_arm.addChild(left_arm_bone);

        cube_r5 = new ModelRenderer(this);
        cube_r5.setRotationPoint(1.25F, 0.0F, -3.0F);
        left_arm_bone.addChild(cube_r5);
        setRotationAngle(cube_r5, 0.0F, -0.3927F, 0.0F);
        cube_r5.cubeList.add(new ModelBox(cube_r5, 46, 55, -1.0F, -1.0F, -3.5F, 2, 2, 7, 0.0F));

        right_arm = new ModelRenderer(this);
        right_arm.setRotationPoint(-5.4288F, 1.086F, 1.163F);
        upper_body.addChild(right_arm);

        cube_r6 = new ModelRenderer(this);
        cube_r6.setRotationPoint(0.0333F, 0.7F, -0.0055F);
        right_arm.addChild(cube_r6);
        setRotationAngle(cube_r6, 0.0F, 0.3927F, 0.0F);
        cube_r6.cubeList.add(new ModelBox(cube_r6, 0, 15, -1.5F, -2.5F, 0.0F, 3, 5, 0, 0.0F));

        cube_r7 = new ModelRenderer(this);
        cube_r7.setRotationPoint(-1.1148F, -1.8F, -2.7772F);
        right_arm.addChild(cube_r7);
        setRotationAngle(cube_r7, 0.0F, 0.3927F, 0.0F);
        cube_r7.cubeList.add(new ModelBox(cube_r7, 51, 56, -1.5F, 0.0F, -3.0F, 3, 0, 6, 0.0F));

        cube_r8 = new ModelRenderer(this);
        cube_r8.setRotationPoint(-2.5006F, 0.7F, -2.2031F);
        right_arm.addChild(cube_r8);
        setRotationAngle(cube_r8, 0.0F, 0.3927F, 0.0F);
        cube_r8.cubeList.add(new ModelBox(cube_r8, 0, 50, 0.0F, -2.5F, -3.0F, 0, 5, 6, 0.0F));

        cube_r9 = new ModelRenderer(this);
        cube_r9.setRotationPoint(0.271F, 0.7F, -3.3512F);
        right_arm.addChild(cube_r9);
        setRotationAngle(cube_r9, 0.0F, 0.3927F, 0.0F);
        cube_r9.cubeList.add(new ModelBox(cube_r9, 0, 4, 0.0F, -2.5F, -3.0F, 0, 5, 6, 0.0F));

        right_arm_bone = new ModelRenderer(this);
        right_arm_bone.setRotationPoint(-0.4388F, -0.3F, -1.163F);
        right_arm.addChild(right_arm_bone);

        cube_r10 = new ModelRenderer(this);
        cube_r10.setRotationPoint(-1.25F, 0.0F, -3.0F);
        right_arm_bone.addChild(cube_r10);
        setRotationAngle(cube_r10, 0.0F, 0.3927F, 0.0F);
        cube_r10.cubeList.add(new ModelBox(cube_r10, 46, 55, -1.0F, -1.0F, -3.5F, 2, 2, 7, 0.0F));

        hitbox = new ModelRenderer(this);
        hitbox.setRotationPoint(0.0F, 3.5F, 0.0F);
        hitbox.cubeList.add(new ModelBox(hitbox, 0, 12, -6.0F, -4.5F, -6.0F, 12, 25, 12, 0.0F));
    }

    @Override
    public void render(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw,
        float headPitch, float scaleFactor) {
        setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, entity);
        body.render(scaleFactor);
    }

    public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }

    @Override
    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw,
        float headPitch, float scaleFactor, Entity entity) {

        body.rotateAngleX = 0.15f;
        body.rotateAngleY = 0.0f;
        body.rotateAngleZ = 0.0f;
        body.rotationPointY = MathHelper.cos((ageInTicks * 0.3f) * 0.4f) * 2.5f;
        left_arm.rotateAngleX = -MathHelper.cos((ageInTicks * 0.3f) * 0.4f) * 0.25f;
        right_arm.rotateAngleX = left_arm.rotateAngleX;
    }
}
