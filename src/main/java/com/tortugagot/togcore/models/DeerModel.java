package com.tortugagot.togcore.models;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;

public class DeerModel extends ModelBase {

    private final ModelRenderer bs_deer_female;
    private final ModelRenderer neck;
    private final ModelRenderer head;
    private final ModelRenderer cube_r1;
    private final ModelRenderer cube_r2;
    private final ModelRenderer cube_r3;
    private final ModelRenderer cube_r4;
    private final ModelRenderer left_front;
    private final ModelRenderer left_back;
    private final ModelRenderer right_front;
    private final ModelRenderer right_back;
    private final ModelRenderer tail;

    private static float prevLimbSwing = 0F;

    public DeerModel() {
        textureWidth = 64;
        textureHeight = 64;

        bs_deer_female = new ModelRenderer(this);
        bs_deer_female.setRotationPoint(0.0F, 22.9375F, -0.9375F);
        bs_deer_female.cubeList.add(new ModelBox(bs_deer_female, 10, 32, -5.5F, -15.9375F, -5.5625F, 10, 9, 16, 0.0F));

        neck = new ModelRenderer(this);
        neck.setRotationPoint(0.0F, -9.225F, -5.0475F);
        bs_deer_female.addChild(neck);
        setRotationAngle(neck, 0.4363F, 0.0F, 0.0F);
        neck.cubeList.add(new ModelBox(neck, 2, 30, -3.5F, -12.2899F, -1.6087F, 6, 12, 6, 0.0F));

        head = new ModelRenderer(this);
        head.setRotationPoint(0.0F, -9.34F, 0.6115F);
        neck.addChild(head);
        setRotationAngle(head, -0.4363F, 0.0F, 0.0F);
        head.cubeList.add(new ModelBox(head, 0, 17, -4.0F, -6.3725F, -3.1265F, 7, 6, 7, 0.0F));
        head.cubeList.add(new ModelBox(head, 0, 11, -2.5F, -3.3725F, -6.1265F, 4, 3, 3, 0.0F));

        cube_r1 = new ModelRenderer(this);
        cube_r1.setRotationPoint(-3.0F, -10.3725F, 2.3735F);
        head.addChild(cube_r1);
        setRotationAngle(cube_r1, 0.0F, -0.3927F, 0.0F);
        cube_r1.cubeList.add(new ModelBox(cube_r1, 46, 7, 0.8827F, -5.0F, -3.0761F, 0, 11, 9, 0.0F));

        cube_r2 = new ModelRenderer(this);
        cube_r2.setRotationPoint(2.0F, -10.3725F, 2.3735F);
        head.addChild(cube_r2);
        setRotationAngle(cube_r2, 0.0F, 0.3927F, 0.0F);
        cube_r2.cubeList.add(new ModelBox(cube_r2, 46, 18, -0.8827F, -5.0F, -3.0761F, 0, 11, 9, 0.0F));

        cube_r3 = new ModelRenderer(this);
        cube_r3.setRotationPoint(-6.7F, -4.2225F, -1.1265F);
        head.addChild(cube_r3);
        setRotationAngle(cube_r3, 0.0F, 0.0F, 0.3927F);
        cube_r3.cubeList.add(new ModelBox(cube_r3, 21, 20, 0.0F, -4.05F, 1.0F, 4, 4, 0, 0.0F));

        cube_r4 = new ModelRenderer(this);
        cube_r4.setRotationPoint(5.6F, -4.2225F, -1.1265F);
        head.addChild(cube_r4);
        setRotationAngle(cube_r4, 0.0F, 0.0F, -0.3927F);
        cube_r4.cubeList.add(new ModelBox(cube_r4, 0, 0, -4.0F, -4.05F, 1.0F, 4, 4, 0, 0.0F));

        left_front = new ModelRenderer(this);
        left_front.setRotationPoint(2.1375F, -7.705F, -2.265F);
        bs_deer_female.addChild(left_front);
        left_front.cubeList.add(new ModelBox(left_front, 16, 0, -1.6375F, -4.2325F, -1.2975F, 3, 13, 3, 0.0F));

        left_back = new ModelRenderer(this);
        left_back.setRotationPoint(2.1375F, -7.705F, 7.14F);
        bs_deer_female.addChild(left_back);
        left_back.cubeList.add(new ModelBox(left_back, 52, 0, -1.6375F, -4.2325F, -1.7025F, 3, 13, 3, 0.0F));

        right_front = new ModelRenderer(this);
        right_front.setRotationPoint(-2.1375F, -7.705F, -2.265F);
        bs_deer_female.addChild(right_front);
        right_front.cubeList.add(new ModelBox(right_front, 28, 0, -1.8625F, -4.2325F, -1.2975F, 3, 13, 3, 0.0F));

        right_back = new ModelRenderer(this);
        right_back.setRotationPoint(-2.1375F, -7.705F, 7.14F);
        bs_deer_female.addChild(right_back);
        right_back.cubeList.add(new ModelBox(right_back, 40, 0, -1.8625F, -4.2325F, -1.7025F, 3, 13, 3, 0.0F));

        tail = new ModelRenderer(this);
        tail.setRotationPoint(0.0F, -13.4725F, 7.7775F);
        bs_deer_female.addChild(tail);
        tail.cubeList.add(new ModelBox(tail, 0, 4, -2.0F, -0.965F, 2.66F, 4, 3, 4, 0.0F));
    }

    @Override
    public void render(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw,
        float headPitch, float scaleFactor) {
        setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, entity);
        bs_deer_female.render(scaleFactor);
    }

    public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }

    @Override
    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw,
        float headPitch, float scaleFactor, Entity entity) {

        if (limbSwing < 55 && limbSwing == prevLimbSwing) {
            neck.rotateAngleX = MathHelper.sin((float) Math.toRadians(130));
            neck.rotateAngleX += MathHelper.sin(ageInTicks * 0.15f) * 0.03F;
        } else {
            neck.rotateAngleX = MathHelper.sin((float) Math.toRadians(40));
            neck.rotateAngleX += MathHelper.sin(limbSwing * 1.6f) * 0.1f * limbSwingAmount;
            bs_deer_female.rotateAngleX = MathHelper.sin(limbSwing * 0.8f) * (limbSwingAmount * 0.05f);
            left_front.rotateAngleX = MathHelper.sin(limbSwing * 0.8f) * (limbSwingAmount * 1.8f);
            left_back.rotateAngleX = -MathHelper.sin(limbSwing * -0.8f) * (limbSwingAmount * 1.8f);
            right_front.rotateAngleX = -left_front.rotateAngleX;
            right_back.rotateAngleX = -left_back.rotateAngleX;
            tail.rotateAngleX = MathHelper.sin(limbSwing * 1.6f) * (limbSwingAmount * 0.2f);
        }
        prevLimbSwing = limbSwing;
    }
}
