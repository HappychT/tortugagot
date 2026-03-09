package com.tortugagot.togcore.client.model.entity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;

public class HedgehogModel extends ModelBase {

    private final ModelRenderer bs_hedgehog;
    private final ModelRenderer cube_r1;
    private final ModelRenderer cube_r2;
    private final ModelRenderer cube_r3;
    private final ModelRenderer cube_r4;
    private final ModelRenderer cube_r5;
    private final ModelRenderer cube_r6;
    private final ModelRenderer cube_r7;
    private final ModelRenderer cube_r8;
    private final ModelRenderer cube_r9;
    private final ModelRenderer cube_r10;
    private final ModelRenderer cube_r11;
    private final ModelRenderer cube_r12;
    private final ModelRenderer cube_r13;
    private final ModelRenderer cube_r14;
    private final ModelRenderer cube_r15;
    private final ModelRenderer cube_r16;
    private final ModelRenderer cube_r17;
    private final ModelRenderer cube_r18;
    private final ModelRenderer cube_r19;
    private final ModelRenderer cube_r20;
    private final ModelRenderer cube_r21;
    private final ModelRenderer cube_r22;
    private final ModelRenderer cube_r23;
    private final ModelRenderer cube_r24;
    private final ModelRenderer cube_r25;
    private final ModelRenderer cube_r26;
    private final ModelRenderer cube_r27;
    private final ModelRenderer cube_r28;
    private final ModelRenderer head;
    private final ModelRenderer cube_r29;
    private final ModelRenderer cube_r30;
    private final ModelRenderer left_front;
    private final ModelRenderer right_front;
    private final ModelRenderer left_back;
    private final ModelRenderer right_back;

    public HedgehogModel() {
        textureWidth = 32;
        textureHeight = 32;

        bs_hedgehog = new ModelRenderer(this);
        bs_hedgehog.setRotationPoint(0.0F, 23.2F, 4.0F);
        bs_hedgehog.cubeList.add(new ModelBox(bs_hedgehog, 0, 0, -3.0F, -5.0F, -6.0F, 6, 5, 6, 0.0F));

        cube_r1 = new ModelRenderer(this);
        cube_r1.setRotationPoint(2.5F, -4.75F, -6.5F);
        bs_hedgehog.addChild(cube_r1);
        setRotationAngle(cube_r1, -0.7854F, 0.0F, 0.0F);
        cube_r1.cubeList.add(new ModelBox(cube_r1, 0, 0, -2.25F, -2.0F, 0.7F, 1, 1, 0, 0.0F));
        cube_r1.cubeList.add(new ModelBox(cube_r1, 0, 1, -4.0F, -2.0F, 0.7F, 1, 1, 0, 0.0F));

        cube_r2 = new ModelRenderer(this);
        cube_r2.setRotationPoint(2.5F, -4.75F, -4.5F);
        bs_hedgehog.addChild(cube_r2);
        setRotationAngle(cube_r2, -0.7854F, 0.0F, 0.0F);
        cube_r2.cubeList.add(new ModelBox(cube_r2, 0, 0, -2.0F, -1.825F, 0.55F, 1, 1, 0, 0.0F));
        cube_r2.cubeList.add(new ModelBox(cube_r2, 0, 2, -4.0F, -2.0F, 0.7F, 1, 1, 0, 0.0F));

        cube_r3 = new ModelRenderer(this);
        cube_r3.setRotationPoint(3.5F, -4.75F, -5.5F);
        bs_hedgehog.addChild(cube_r3);
        setRotationAngle(cube_r3, -0.7854F, 0.0F, 0.0F);
        cube_r3.cubeList.add(new ModelBox(cube_r3, 0, 0, -2.0F, -1.85F, 0.7F, 1, 1, 0, 0.0F));
        cube_r3.cubeList.add(new ModelBox(cube_r3, 0, 0, -6.0F, -2.0F, 0.7F, 1, 1, 0, 0.0F));
        cube_r3.cubeList.add(new ModelBox(cube_r3, 0, 3, -4.25F, -2.0F, 0.7F, 1, 1, 0, 0.0F));

        cube_r4 = new ModelRenderer(this);
        cube_r4.setRotationPoint(-0.5F, -4.75F, -3.5F);
        bs_hedgehog.addChild(cube_r4);
        setRotationAngle(cube_r4, -0.7854F, 0.0F, 0.0F);
        cube_r4.cubeList.add(new ModelBox(cube_r4, 0, 1, -2.0F, -1.85F, 0.7F, 1, 1, 0, 0.0F));
        cube_r4.cubeList.add(new ModelBox(cube_r4, 0, 2, 0.0F, -2.0F, 0.7F, 1, 1, 0, 0.0F));
        cube_r4.cubeList.add(new ModelBox(cube_r4, 0, 1, 2.0F, -1.75F, 0.45F, 1, 1, 0, 0.0F));

        cube_r5 = new ModelRenderer(this);
        cube_r5.setRotationPoint(2.5F, -4.75F, -2.5F);
        bs_hedgehog.addChild(cube_r5);
        setRotationAngle(cube_r5, -0.7854F, 0.0F, 0.0F);
        cube_r5.cubeList.add(new ModelBox(cube_r5, 0, 3, -2.0F, -1.85F, 0.7F, 1, 1, 0, 0.0F));
        cube_r5.cubeList.add(new ModelBox(cube_r5, 0, 2, -4.0F, -2.0F, 0.7F, 1, 1, 0, 0.0F));

        cube_r6 = new ModelRenderer(this);
        cube_r6.setRotationPoint(-0.5F, -4.75F, -2.0F);
        bs_hedgehog.addChild(cube_r6);
        setRotationAngle(cube_r6, -0.7854F, 0.0F, 0.0F);
        cube_r6.cubeList.add(new ModelBox(cube_r6, 0, 0, -2.0F, -2.0F, 0.7F, 1, 1, 0, 0.0F));
        cube_r6.cubeList.add(new ModelBox(cube_r6, 0, 1, 2.0F, -2.0F, 0.7F, 1, 1, 0, 0.0F));

        cube_r7 = new ModelRenderer(this);
        cube_r7.setRotationPoint(3.5F, 0.25F, 0.75F);
        bs_hedgehog.addChild(cube_r7);
        setRotationAngle(cube_r7, -0.7854F, 0.0F, 0.0F);
        cube_r7.cubeList.add(new ModelBox(cube_r7, -1, 2, -2.0F, -0.7F, -2.0F, 1, 0, 1, 0.0F));
        cube_r7.cubeList.add(new ModelBox(cube_r7, -1, 2, -6.0F, -0.7F, -2.0F, 1, 0, 1, 0.0F));

        cube_r8 = new ModelRenderer(this);
        cube_r8.setRotationPoint(3.5F, -2.75F, 0.75F);
        bs_hedgehog.addChild(cube_r8);
        setRotationAngle(cube_r8, -0.7854F, 0.0F, 0.0F);
        cube_r8.cubeList.add(new ModelBox(cube_r8, -1, 3, -2.0F, -0.7F, -2.0F, 1, 0, 1, 0.0F));
        cube_r8.cubeList.add(new ModelBox(cube_r8, -1, 2, -6.0F, -0.7F, -2.0F, 1, 0, 1, 0.0F));

        cube_r9 = new ModelRenderer(this);
        cube_r9.setRotationPoint(2.5F, -2.25F, 0.75F);
        bs_hedgehog.addChild(cube_r9);
        setRotationAngle(cube_r9, -0.7854F, 0.0F, 0.0F);
        cube_r9.cubeList.add(new ModelBox(cube_r9, -1, 0, -2.0F, -0.525F, -1.675F, 1, 0, 1, 0.0F));
        cube_r9.cubeList.add(new ModelBox(cube_r9, -1, 2, -3.0F, -1.05F, -2.25F, 1, 0, 1, 0.0F));
        cube_r9.cubeList.add(new ModelBox(cube_r9, -1, 0, -4.0F, -0.525F, -1.825F, 1, 0, 1, 0.0F));

        cube_r10 = new ModelRenderer(this);
        cube_r10.setRotationPoint(0.5F, -0.25F, 0.75F);
        bs_hedgehog.addChild(cube_r10);
        setRotationAngle(cube_r10, -0.7854F, 0.0F, 0.0F);
        cube_r10.cubeList.add(new ModelBox(cube_r10, -1, 0, -2.0F, -0.7F, -2.0F, 1, 0, 1, 0.0F));
        cube_r10.cubeList.add(new ModelBox(cube_r10, -1, 2, 0.0F, -0.55F, -1.825F, 1, 0, 1, 0.0F));

        cube_r11 = new ModelRenderer(this);
        cube_r11.setRotationPoint(-0.5F, -1.25F, 0.75F);
        bs_hedgehog.addChild(cube_r11);
        setRotationAngle(cube_r11, -0.7854F, 0.0F, 0.0F);
        cube_r11.cubeList.add(new ModelBox(cube_r11, -1, 3, -2.0F, -0.7F, -1.85F, 1, 0, 1, 0.0F));
        cube_r11.cubeList.add(new ModelBox(cube_r11, -1, 3, 0.0F, -0.475F, -1.775F, 1, 0, 1, 0.0F));
        cube_r11.cubeList.add(new ModelBox(cube_r11, -1, 3, 2.0F, -0.45F, -1.75F, 1, 0, 1, 0.0F));

        cube_r12 = new ModelRenderer(this);
        cube_r12.setRotationPoint(-0.5F, -4.75F, -7.0F);
        bs_hedgehog.addChild(cube_r12);
        setRotationAngle(cube_r12, -0.7854F, 0.0F, 0.0F);
        cube_r12.cubeList.add(new ModelBox(cube_r12, 0, 2, -2.0F, -2.0F, 0.7F, 1, 1, 0, 0.0F));
        cube_r12.cubeList.add(new ModelBox(cube_r12, 0, 3, 2.0F, -2.0F, 0.7F, 1, 1, 0, 0.0F));

        cube_r13 = new ModelRenderer(this);
        cube_r13.setRotationPoint(-2.75F, 0.0F, -5.25F);
        bs_hedgehog.addChild(cube_r13);
        setRotationAngle(cube_r13, 0.0F, 0.7854F, 0.0F);
        cube_r13.cubeList.add(new ModelBox(cube_r13, 0, 3, -2.0F, -2.25F, 0.7F, 1, 1, 0, 0.0F));

        cube_r14 = new ModelRenderer(this);
        cube_r14.setRotationPoint(-2.75F, -1.5F, -4.5F);
        bs_hedgehog.addChild(cube_r14);
        setRotationAngle(cube_r14, 0.0F, 0.7854F, 0.0F);
        cube_r14.cubeList.add(new ModelBox(cube_r14, 0, 1, -2.0F, -2.1F, 0.7F, 1, 1, 0, 0.0F));

        cube_r15 = new ModelRenderer(this);
        cube_r15.setRotationPoint(-2.75F, -2.5F, -5.5F);
        bs_hedgehog.addChild(cube_r15);
        setRotationAngle(cube_r15, 0.0F, 0.7854F, 0.0F);
        cube_r15.cubeList.add(new ModelBox(cube_r15, 0, 0, -2.0F, -2.25F, 0.7F, 1, 1, 0, 0.0F));

        cube_r16 = new ModelRenderer(this);
        cube_r16.setRotationPoint(-2.75F, -1.5F, -6.5F);
        bs_hedgehog.addChild(cube_r16);
        setRotationAngle(cube_r16, 0.0F, 0.7854F, 0.0F);
        cube_r16.cubeList.add(new ModelBox(cube_r16, 0, 1, -2.0F, -1.85F, 0.95F, 1, 1, 0, 0.0F));

        cube_r17 = new ModelRenderer(this);
        cube_r17.setRotationPoint(-2.75F, -2.5F, -3.5F);
        bs_hedgehog.addChild(cube_r17);
        setRotationAngle(cube_r17, 0.0F, 0.7854F, 0.0F);
        cube_r17.cubeList.add(new ModelBox(cube_r17, 0, 2, -2.0F, -2.0F, 0.7F, 1, 1, 0, 0.0F));
        cube_r17.cubeList.add(new ModelBox(cube_r17, 0, 1, -2.0F, 0.5F, 0.7F, 1, 1, 0, 0.0F));

        cube_r18 = new ModelRenderer(this);
        cube_r18.setRotationPoint(-2.75F, -1.5F, -2.75F);
        bs_hedgehog.addChild(cube_r18);
        setRotationAngle(cube_r18, 0.0F, 0.7854F, 0.0F);
        cube_r18.cubeList.add(new ModelBox(cube_r18, 0, 3, -2.0F, -1.85F, 0.7F, 1, 1, 0, 0.0F));

        cube_r19 = new ModelRenderer(this);
        cube_r19.setRotationPoint(-2.75F, -2.5F, -7.0F);
        bs_hedgehog.addChild(cube_r19);
        setRotationAngle(cube_r19, 0.0F, 0.7854F, 0.0F);
        cube_r19.cubeList.add(new ModelBox(cube_r19, 0, 0, -2.0F, -2.0F, 0.7F, 1, 1, 0, 0.0F));
        cube_r19.cubeList.add(new ModelBox(cube_r19, 0, 1, -2.0F, 0.25F, 0.7F, 1, 1, 0, 0.0F));

        cube_r20 = new ModelRenderer(this);
        cube_r20.setRotationPoint(-2.75F, -2.5F, -2.0F);
        bs_hedgehog.addChild(cube_r20);
        setRotationAngle(cube_r20, 0.0F, 0.7854F, 0.0F);
        cube_r20.cubeList.add(new ModelBox(cube_r20, 0, 2, -2.0F, -2.0F, 0.7F, 1, 1, 0, 0.0F));
        cube_r20.cubeList.add(new ModelBox(cube_r20, 0, 3, -2.0F, 0.25F, 0.7F, 1, 1, 0, 0.0F));

        cube_r21 = new ModelRenderer(this);
        cube_r21.setRotationPoint(2.75F, 0.0F, -5.25F);
        bs_hedgehog.addChild(cube_r21);
        setRotationAngle(cube_r21, 0.0F, -0.7854F, 0.0F);
        cube_r21.cubeList.add(new ModelBox(cube_r21, 0, 1, 1.0F, -2.25F, 0.7F, 1, 1, 0, 0.0F));

        cube_r22 = new ModelRenderer(this);
        cube_r22.setRotationPoint(2.75F, -1.5F, -4.5F);
        bs_hedgehog.addChild(cube_r22);
        setRotationAngle(cube_r22, 0.0F, -0.7854F, 0.0F);
        cube_r22.cubeList.add(new ModelBox(cube_r22, 0, 2, 1.0F, -2.1F, 0.7F, 1, 1, 0, 0.0F));

        cube_r23 = new ModelRenderer(this);
        cube_r23.setRotationPoint(2.75F, -2.5F, -5.5F);
        bs_hedgehog.addChild(cube_r23);
        setRotationAngle(cube_r23, 0.0F, -0.7854F, 0.0F);
        cube_r23.cubeList.add(new ModelBox(cube_r23, 0, 0, 1.0F, -2.25F, 0.7F, 1, 1, 0, 0.0F));

        cube_r24 = new ModelRenderer(this);
        cube_r24.setRotationPoint(2.75F, -1.5F, -6.5F);
        bs_hedgehog.addChild(cube_r24);
        setRotationAngle(cube_r24, 0.0F, -0.7854F, 0.0F);
        cube_r24.cubeList.add(new ModelBox(cube_r24, 0, 3, 1.0F, -1.85F, 0.95F, 1, 1, 0, 0.0F));

        cube_r25 = new ModelRenderer(this);
        cube_r25.setRotationPoint(2.75F, -2.5F, -3.5F);
        bs_hedgehog.addChild(cube_r25);
        setRotationAngle(cube_r25, 0.0F, -0.7854F, 0.0F);
        cube_r25.cubeList.add(new ModelBox(cube_r25, 0, 1, 1.0F, -2.0F, 0.7F, 1, 1, 0, 0.0F));
        cube_r25.cubeList.add(new ModelBox(cube_r25, 0, 2, 1.0F, 0.5F, 0.7F, 1, 1, 0, 0.0F));

        cube_r26 = new ModelRenderer(this);
        cube_r26.setRotationPoint(2.75F, -1.5F, -2.75F);
        bs_hedgehog.addChild(cube_r26);
        setRotationAngle(cube_r26, 0.0F, -0.7854F, 0.0F);
        cube_r26.cubeList.add(new ModelBox(cube_r26, 0, 1, 1.0F, -1.85F, 0.7F, 1, 1, 0, 0.0F));

        cube_r27 = new ModelRenderer(this);
        cube_r27.setRotationPoint(2.75F, -2.5F, -7.0F);
        bs_hedgehog.addChild(cube_r27);
        setRotationAngle(cube_r27, 0.0F, -0.7854F, 0.0F);
        cube_r27.cubeList.add(new ModelBox(cube_r27, 0, 2, 1.0F, 0.25F, 0.7F, 1, 1, 0, 0.0F));
        cube_r27.cubeList.add(new ModelBox(cube_r27, 0, 2, 1.0F, -2.0F, 0.7F, 1, 1, 0, 0.0F));

        cube_r28 = new ModelRenderer(this);
        cube_r28.setRotationPoint(2.75F, -2.5F, -2.0F);
        bs_hedgehog.addChild(cube_r28);
        setRotationAngle(cube_r28, 0.0F, -0.7854F, 0.0F);
        cube_r28.cubeList.add(new ModelBox(cube_r28, 0, 0, 1.0F, 0.25F, 0.7F, 1, 1, 0, 0.0F));
        cube_r28.cubeList.add(new ModelBox(cube_r28, 0, 0, 1.0F, -2.0F, 0.7F, 1, 1, 0, 0.0F));

        head = new ModelRenderer(this);
        head.setRotationPoint(0.0F, -0.1098F, -6.0F);
        bs_hedgehog.addChild(head);
        head.cubeList.add(new ModelBox(head, 0, 26, -2.0F, -3.8902F, -2.0F, 4, 4, 2, 0.0F));
        head.cubeList.add(new ModelBox(head, 12, 28, -1.0F, -1.8902F, -3.0F, 2, 2, 2, 0.0F));
        head.cubeList.add(new ModelBox(head, 27, 3, -2.0F, -3.8902F, 0.0F, 4, 4, 2, 0.0F));

        cube_r29 = new ModelRenderer(this);
        cube_r29.setRotationPoint(-2.0F, -4.1402F, -0.5F);
        head.addChild(cube_r29);
        setRotationAngle(cube_r29, 0.0F, 0.0F, -0.7854F);
        cube_r29.cubeList.add(new ModelBox(cube_r29, 28, 16, -1.25F, -1.0F, 0.0F, 2, 3, 0, 0.0F));

        cube_r30 = new ModelRenderer(this);
        cube_r30.setRotationPoint(2.0F, -4.1402F, -0.5F);
        head.addChild(cube_r30);
        setRotationAngle(cube_r30, 0.0F, 0.0F, 0.7854F);
        cube_r30.cubeList.add(new ModelBox(cube_r30, 28, 13, -0.75F, -1.0F, 0.0F, 2, 3, 0, 0.0F));

        left_front = new ModelRenderer(this);
        left_front.setRotationPoint(1.75F, 0.0F, -4.75F);
        bs_hedgehog.addChild(left_front);
        left_front.cubeList.add(new ModelBox(left_front, 24, 19, -1.0F, -0.1F, -1.0F, 2, 1, 2, -0.1F));

        right_front = new ModelRenderer(this);
        right_front.setRotationPoint(-1.75F, 0.0F, -4.75F);
        bs_hedgehog.addChild(right_front);
        right_front.cubeList.add(new ModelBox(right_front, 24, 29, -1.0F, -0.1F, -1.0F, 2, 1, 2, -0.1F));

        left_back = new ModelRenderer(this);
        left_back.setRotationPoint(1.75F, 0.0F, -1.25F);
        bs_hedgehog.addChild(left_back);
        left_back.cubeList.add(new ModelBox(left_back, 24, 22, -1.0F, -0.1F, -1.0F, 2, 1, 2, -0.1F));

        right_back = new ModelRenderer(this);
        right_back.setRotationPoint(-1.75F, 0.0F, -1.25F);
        bs_hedgehog.addChild(right_back);
        right_back.cubeList.add(new ModelBox(right_back, 24, 25, -1.0F, -0.1F, -1.0F, 2, 1, 2, -0.1F));
    }

    @Override
    public void render(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw,
        float headPitch, float scaleFactor) {
        setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, entity);
        bs_hedgehog.render(scaleFactor);
    }

    public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }

    @Override
    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw,
        float headPitch, float scaleFactor, Entity entity) {
        bs_hedgehog.rotateAngleZ = MathHelper.sin(limbSwing * 1.5f) * (limbSwingAmount * 0.2f);
        head.rotateAngleZ = MathHelper.sin(limbSwing * 1.5f) * (limbSwingAmount * 0.2f);
        left_front.rotateAngleX = MathHelper.sin(limbSwing * 1.5f) * (limbSwingAmount * 2f);
        left_back.rotateAngleX = -MathHelper.sin(limbSwing * -1.5f) * (limbSwingAmount * 2f);
        right_front.rotateAngleX = -left_front.rotateAngleX;
        right_back.rotateAngleX = -left_back.rotateAngleX;

    }
}
