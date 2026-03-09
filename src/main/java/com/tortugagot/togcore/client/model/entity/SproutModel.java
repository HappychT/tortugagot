package com.tortugagot.togcore.client.model.entity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;

public class SproutModel extends ModelBase {

    private final ModelRenderer sprout_rex;
    private final ModelRenderer cube_r1;
    private final ModelRenderer cube_r2;
    private final ModelRenderer cube_r3;
    private final ModelRenderer cube_r4;
    private final ModelRenderer cube_r5;
    private final ModelRenderer arms;
    private final ModelRenderer cube_r6;
    private final ModelRenderer cube_r7;
    private final ModelRenderer hitbox;
    private final ModelRenderer head;
    private final ModelRenderer tag_name;
    private final ModelRenderer branch;
    private final ModelRenderer head_r1;
    private final ModelRenderer head_r2;
    private final ModelRenderer right_thigh;
    private final ModelRenderer cube_r8;
    private final ModelRenderer legs_r1;
    private final ModelRenderer right_foot;
    private final ModelRenderer left_thigh;
    private final ModelRenderer cube_r9;
    private final ModelRenderer legs_r2;
    private final ModelRenderer left_foot;

    public SproutModel() {
        textureWidth = 64;
        textureHeight = 64;

        sprout_rex = new ModelRenderer(this);
        sprout_rex.setRotationPoint(0.0344F, 18.8599F, -1.3866F);
        sprout_rex.cubeList.add(new ModelBox(sprout_rex, 0, 24, -1.4344F, 1.2546F, 5.9285F, 3, 2, 4, 0.0F));
        sprout_rex.cubeList.add(new ModelBox(sprout_rex, 38, 56, 0.7656F, -1.3393F, 5.9138F, 0, 4, 4, 0.0F));
        sprout_rex.cubeList.add(new ModelBox(sprout_rex, 30, 56, -0.7344F, -1.3393F, 5.9138F, 0, 4, 4, 0.0F));

        cube_r1 = new ModelRenderer(this);
        cube_r1.setRotationPoint(0.7656F, -4.3599F, 1.8866F);
        sprout_rex.addChild(cube_r1);
        setRotationAngle(cube_r1, 0.3927F, 0.0F, 0.0F);
        cube_r1.cubeList.add(new ModelBox(cube_r1, 50, 49, 0.0F, -2.5F, -0.5F, 0, 6, 3, 0.0F));
        cube_r1.cubeList.add(new ModelBox(cube_r1, 50, 55, -1.5F, -2.5F, -0.5F, 0, 6, 3, 0.0F));

        cube_r2 = new ModelRenderer(this);
        cube_r2.setRotationPoint(4.2156F, -4.2807F, 0.0018F);
        sprout_rex.addChild(cube_r2);
        setRotationAngle(cube_r2, -0.7854F, 0.0F, 0.0F);
        cube_r2.cubeList.add(new ModelBox(cube_r2, 45, 33, -2.15F, -1.0F, 5.35F, 0, 4, 5, 0.0F));
        cube_r2.cubeList.add(new ModelBox(cube_r2, 45, 37, -6.4F, -1.0F, 5.35F, 0, 4, 5, 0.0F));

        cube_r3 = new ModelRenderer(this);
        cube_r3.setRotationPoint(0.7656F, -0.2807F, 4.7518F);
        sprout_rex.addChild(cube_r3);
        setRotationAngle(cube_r3, -0.3927F, 0.0F, 0.0F);
        cube_r3.cubeList.add(new ModelBox(cube_r3, 56, 56, 0.2F, -2.0F, -1.4F, 0, 4, 4, 0.0F));
        cube_r3.cubeList.add(new ModelBox(cube_r3, 56, 52, -1.6F, -2.0F, -1.4F, 0, 4, 4, 0.0F));

        cube_r4 = new ModelRenderer(this);
        cube_r4.setRotationPoint(0.1156F, 1.6401F, 5.1366F);
        sprout_rex.addChild(cube_r4);
        setRotationAngle(cube_r4, -0.3927F, 0.0F, 0.0F);
        cube_r4.cubeList.add(new ModelBox(cube_r4, 0, 17, -2.175F, -1.5F, -2.6F, 4, 3, 4, 0.0F));

        cube_r5 = new ModelRenderer(this);
        cube_r5.setRotationPoint(-0.0344F, -1.8599F, 0.6866F);
        sprout_rex.addChild(cube_r5);
        setRotationAngle(cube_r5, 0.3927F, 0.0F, 0.0F);
        cube_r5.cubeList.add(new ModelBox(cube_r5, 0, 30, -2.0F, -2.25F, -2.3F, 4, 7, 5, 0.0F));

        arms = new ModelRenderer(this);
        arms.setRotationPoint(2.4656F, -1.081F, -0.1148F);
        sprout_rex.addChild(arms);
        arms.cubeList.add(new ModelBox(arms, 24, 38, -5.5F, -0.7F, -2.775F, 1, 1, 3, 0.0F));
        arms.cubeList.add(new ModelBox(arms, 24, 38, -0.5F, -0.7789F, -2.7487F, 1, 1, 3, 0.0F));
        arms.cubeList.add(new ModelBox(arms, 56, 51, -5.0F, 0.0711F, -2.7986F, 0, 1, 3, 0.0F));
        arms.cubeList.add(new ModelBox(arms, 56, 52, 0.0F, -0.0289F, -2.7986F, 0, 1, 3, 0.0F));

        cube_r6 = new ModelRenderer(this);
        cube_r6.setRotationPoint(1.0F, 0.8461F, -1.4987F);
        arms.addChild(cube_r6);
        setRotationAngle(cube_r6, 0.0F, 0.0F, -0.3927F);
        cube_r6.cubeList.add(new ModelBox(cube_r6, 42, 51, 0.0F, -1.125F, -1.25F, 0, 1, 3, 0.0F));

        cube_r7 = new ModelRenderer(this);
        cube_r7.setRotationPoint(-6.0F, 0.8461F, -1.2487F);
        arms.addChild(cube_r7);
        setRotationAngle(cube_r7, 0.0F, 0.0F, 0.3927F);
        cube_r7.cubeList.add(new ModelBox(cube_r7, 42, 50, 0.0F, -1.125F, -1.25F, 0, 1, 3, 0.0F));

        hitbox = new ModelRenderer(this);
        hitbox.setRotationPoint(-0.6844F, -6.8599F, -0.3634F);
        sprout_rex.addChild(hitbox);
        hitbox.cubeList.add(new ModelBox(hitbox, 0, 0, -4.5F, 1.0F, -3.5F, 10, 6, 10, 0.0F));

        head = new ModelRenderer(this);
        head.setRotationPoint(-0.0344F, -3.581F, -0.1148F);
        sprout_rex.addChild(head);
        head.cubeList.add(new ModelBox(head, 40, 0, -3.18F, -4.3539F, -4.1387F, 6, 6, 6, 0.0F));
        head.cubeList.add(new ModelBox(head, 54, 18, -1.06F, -2.8239F, -5.5487F, 2, 4, 2, 0.0F));
        head.cubeList.add(new ModelBox(head, 33, 1, 0.725F, -2.2939F, -4.6087F, 2, 3, 1, 0.0F));
        head.cubeList.add(new ModelBox(head, 33, 1, -3.0484F, -2.2939F, -4.6087F, 2, 3, 1, 0.0F));
        head.cubeList.add(new ModelBox(head, 45, 12, -3.71F, -3.1739F, -6.0421F, 7, 2, 2, 0.0F));

        tag_name = new ModelRenderer(this);
        tag_name.setRotationPoint(0.0F, -8.7789F, -1.9987F);
        head.addChild(tag_name);

        branch = new ModelRenderer(this);
        branch.setRotationPoint(0.2552F, -3.1755F, -2.0824F);
        head.addChild(branch);
        branch.cubeList.add(new ModelBox(branch, 0, 52, -7.881F, -6.3486F, -2.5263F, 7, 5, 6, 0.0F));
        branch.cubeList.add(new ModelBox(branch, 0, 43, 0.389F, -5.5686F, -2.2912F, 7, 4, 5, 0.0F));

        head_r1 = new ModelRenderer(this);
        head_r1.setRotationPoint(-2.8972F, 1.0046F, -0.1762F);
        branch.addChild(head_r1);
        setRotationAngle(head_r1, 0.0F, 0.0F, -0.7854F);
        head_r1.cubeList.add(new ModelBox(head_r1, 48, 18, -0.3638F, -3.7532F, -0.47F, 1, 4, 1, 0.0F));

        head_r2 = new ModelRenderer(this);
        head_r2.setRotationPoint(2.0948F, 1.0616F, -0.1762F);
        branch.addChild(head_r2);
        setRotationAngle(head_r2, 0.0F, 0.0F, 0.7854F);
        head_r2.cubeList.add(new ModelBox(head_r2, 41, 17, -0.53F, -5.0F, -0.47F, 1, 5, 1, 0.0F));

        right_thigh = new ModelRenderer(this);
        right_thigh.setRotationPoint(-2.0344F, 1.6401F, 1.3866F);
        sprout_rex.addChild(right_thigh);

        cube_r8 = new ModelRenderer(this);
        cube_r8.setRotationPoint(-1.175F, -0.1402F, 2.2745F);
        right_thigh.addChild(cube_r8);
        setRotationAngle(cube_r8, -0.7854F, 0.0F, 0.0F);
        cube_r8.cubeList.add(new ModelBox(cube_r8, 45, 41, 0.0F, -1.5F, -2.575F, 0, 3, 5, 0.0F));

        legs_r1 = new ModelRenderer(this);
        legs_r1.setRotationPoint(-1.17F, 0.0953F, -0.0703F);
        right_thigh.addChild(legs_r1);
        setRotationAngle(legs_r1, -0.7854F, 0.0F, 0.0F);
        legs_r1.cubeList.add(new ModelBox(legs_r1, 25, 48, -1.84F, -2.68F, -2.32F, 3, 5, 5, 0.0F));

        right_foot = new ModelRenderer(this);
        right_foot.setRotationPoint(-1.17F, 2.6008F, -0.0263F);
        right_thigh.addChild(right_foot);
        right_foot.cubeList.add(new ModelBox(right_foot, 29, 18, -1.2832F, 0.0768F, -3.8168F, 2, 1, 5, 0.0F));

        left_thigh = new ModelRenderer(this);
        left_thigh.setRotationPoint(1.9656F, 1.6401F, 1.3866F);
        sprout_rex.addChild(left_thigh);

        cube_r9 = new ModelRenderer(this);
        cube_r9.setRotationPoint(2.0F, -5.9207F, -1.3848F);
        left_thigh.addChild(cube_r9);
        setRotationAngle(cube_r9, -0.7854F, 0.0F, 0.0F);
        cube_r9.cubeList.add(new ModelBox(cube_r9, 45, 44, -0.8F, 0.0F, 4.1F, 0, 3, 5, 0.0F));

        legs_r2 = new ModelRenderer(this);
        legs_r2.setRotationPoint(1.165F, 0.0953F, -0.1103F);
        left_thigh.addChild(legs_r2);
        setRotationAngle(legs_r2, -0.7854F, 0.0F, 0.0F);
        legs_r2.cubeList.add(new ModelBox(legs_r2, 25, 48, -1.84F, -2.68F, -2.32F, 3, 5, 5, 0.0F));

        left_foot = new ModelRenderer(this);
        left_foot.setRotationPoint(1.215F, 2.601F, -0.0663F);
        left_thigh.addChild(left_foot);
        left_foot.cubeList.add(new ModelBox(left_foot, 29, 18, -1.2832F, 0.0766F, -3.8168F, 2, 1, 5, 0.0F));
    }

    @Override
    public void render(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw,
        float headPitch, float scaleFactor) {
        setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, entity);
        sprout_rex.render(scaleFactor);
    }

    public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }

    @Override
    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw,
        float headPitch, float scaleFactor, Entity entity) {

        branch.rotateAngleZ = MathHelper.sin((ageInTicks * 0.4f) * 0.4f) * 0.05f;

        sprout_rex.rotateAngleY = MathHelper.sin(limbSwing * 1.1f) * (limbSwingAmount * 0.3f);
        sprout_rex.rotateAngleZ = MathHelper.sin(limbSwing * 1.1f) * (limbSwingAmount * 0.3f);
        right_thigh.rotateAngleX = MathHelper.sin(limbSwing * 1.1f) * (limbSwingAmount * 0.9f);
        right_foot.rotateAngleX = MathHelper.sin(limbSwing * 1.1f) * (limbSwingAmount * 0.5f);
        left_thigh.rotateAngleX = -right_thigh.rotateAngleX;
        left_foot.rotateAngleX = -right_foot.rotateAngleX;
    }
}
