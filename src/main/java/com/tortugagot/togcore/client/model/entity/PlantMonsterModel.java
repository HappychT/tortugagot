package com.tortugagot.togcore.client.model.entity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;

public class PlantMonsterModel extends ModelBase {

    private final ModelRenderer body;
    private final ModelRenderer cube_r1;
    private final ModelRenderer cube_r2;
    private final ModelRenderer cube_r3;
    private final ModelRenderer cube_r4;
    private final ModelRenderer upper_body;
    private final ModelRenderer cube_r5;
    private final ModelRenderer chest;
    private final ModelRenderer cube_r6;
    private final ModelRenderer left_arm;
    private final ModelRenderer cube_r7;
    private final ModelRenderer cube_r8;
    private final ModelRenderer right_arm;
    private final ModelRenderer cube_r9;
    private final ModelRenderer cube_r10;
    private final ModelRenderer h_head;
    private final ModelRenderer cube_r11;
    private final ModelRenderer cube_r12;
    private final ModelRenderer cube_r13;
    private final ModelRenderer cube_r14;
    private final ModelRenderer cube_r15;
    private final ModelRenderer h_jaw;
    private final ModelRenderer cube_r16;
    private final ModelRenderer h_tongue;
    private final ModelRenderer cube_r17;
    private final ModelRenderer hitbox;

    public PlantMonsterModel() {
        textureWidth = 64;
        textureHeight = 64;

        body = new ModelRenderer(this);
        body.setRotationPoint(1.325F, 14.1914F, 3.1607F);

        cube_r1 = new ModelRenderer(this);
        cube_r1.setRotationPoint(-1.325F, 0.6516F, -0.1607F);
        body.addChild(cube_r1);
        setRotationAngle(cube_r1, 0.0F, 0.0F, -0.3927F);
        cube_r1.cubeList.add(new ModelBox(cube_r1, 48, 40, -14.3811F, 3.2397F, -4.625F, 6, 0, 4, 0.0F));
        cube_r1.cubeList.add(new ModelBox(cube_r1, 48, 24, -2.3637F, 8.2175F, -4.625F, 6, 0, 4, 0.0F));

        cube_r2 = new ModelRenderer(this);
        cube_r2.setRotationPoint(-1.325F, 0.6516F, -0.1607F);
        body.addChild(cube_r2);
        setRotationAngle(cube_r2, 0.3927F, 0.0F, 0.0F);
        cube_r2.cubeList.add(new ModelBox(cube_r2, 50, 49, -2.0F, 2.0916F, -17.1528F, 4, 0, 6, 0.0F));
        cube_r2.cubeList.add(new ModelBox(cube_r2, 50, 34, -2.0F, 7.0694F, -5.1353F, 4, 0, 6, 0.0F));

        cube_r3 = new ModelRenderer(this);
        cube_r3.setRotationPoint(-1.325F, 0.6516F, -0.1607F);
        body.addChild(cube_r3);
        setRotationAngle(cube_r3, -0.3927F, 0.0F, 0.0F);
        cube_r3.cubeList.add(new ModelBox(cube_r3, 50, 28, -2.0F, 9.3655F, -6.4079F, 4, 0, 6, 0.0F));
        cube_r3.cubeList.add(new ModelBox(cube_r3, 50, 43, -2.0F, 4.3877F, 5.6095F, 4, 0, 6, 0.0F));

        cube_r4 = new ModelRenderer(this);
        cube_r4.setRotationPoint(-1.325F, 0.6516F, -0.1607F);
        body.addChild(cube_r4);
        setRotationAngle(cube_r4, 0.0F, 0.0F, 0.3927F);
        cube_r4.cubeList.add(new ModelBox(cube_r4, 48, 24, -3.6363F, 8.2175F, -4.625F, 6, 0, 4, 0.0F));
        cube_r4.cubeList.add(new ModelBox(cube_r4, 48, 40, 8.3811F, 3.2397F, -4.625F, 6, 0, 4, 0.0F));

        upper_body = new ModelRenderer(this);
        upper_body.setRotationPoint(-1.325F, 9.3446F, -3.2857F);
        body.addChild(upper_body);

        cube_r5 = new ModelRenderer(this);
        cube_r5.setRotationPoint(0.0F, -8.6931F, 3.125F);
        upper_body.addChild(cube_r5);
        setRotationAngle(cube_r5, -0.3927F, 0.0F, 0.0F);
        cube_r5.cubeList.add(new ModelBox(cube_r5, 16, 54, -1.0F, 1.4196F, -0.514F, 2, 8, 2, 0.0F));

        chest = new ModelRenderer(this);
        chest.setRotationPoint(-3.0917F, -6.7864F, 3.0291F);
        upper_body.addChild(chest);
        chest.cubeList.add(new ModelBox(chest, 0, 56, 2.0917F, -6.0264F, -1.0745F, 2, 6, 2, 0.0F));

        cube_r6 = new ModelRenderer(this);
        cube_r6.setRotationPoint(3.0917F, -1.9066F, 0.0959F);
        chest.addChild(cube_r6);
        setRotationAngle(cube_r6, 0.3927F, 0.0F, 0.0F);
        cube_r6.cubeList.add(new ModelBox(cube_r6, 8, 56, -1.0F, -9.4887F, 0.343F, 2, 6, 2, 0.0F));

        left_arm = new ModelRenderer(this);
        left_arm.setRotationPoint(4.0955F, -4.9049F, -0.4097F);
        chest.addChild(left_arm);

        cube_r7 = new ModelRenderer(this);
        cube_r7.setRotationPoint(0.0F, 0.125F, 0.0F);
        left_arm.addChild(cube_r7);
        setRotationAngle(cube_r7, 0.0F, 0.0F, 0.3927F);
        cube_r7.cubeList.add(new ModelBox(cube_r7, 21, 59, 4.2111F, -4.2046F, -2.0F, 6, 0, 4, 0.0F));

        cube_r8 = new ModelRenderer(this);
        cube_r8.setRotationPoint(0.0F, 0.125F, 0.0F);
        left_arm.addChild(cube_r8);
        setRotationAngle(cube_r8, 0.0F, 0.0F, -0.3927F);
        cube_r8.cubeList.add(new ModelBox(cube_r8, 21, 55, -0.0484F, 0.0049F, -2.0F, 6, 0, 4, 0.0F));

        right_arm = new ModelRenderer(this);
        right_arm.setRotationPoint(2.0879F, -4.9049F, -0.4097F);
        chest.addChild(right_arm);

        cube_r9 = new ModelRenderer(this);
        cube_r9.setRotationPoint(0.0F, 0.125F, 0.0F);
        right_arm.addChild(cube_r9);
        setRotationAngle(cube_r9, 0.0F, 0.0F, -0.3927F);
        cube_r9.cubeList.add(new ModelBox(cube_r9, 21, 59, -10.2111F, -4.2046F, -2.0F, 6, 0, 4, 0.0F));

        cube_r10 = new ModelRenderer(this);
        cube_r10.setRotationPoint(0.0F, 0.125F, 0.0F);
        right_arm.addChild(cube_r10);
        setRotationAngle(cube_r10, 0.0F, 0.0F, 0.3927F);
        cube_r10.cubeList.add(new ModelBox(cube_r10, 21, 55, -5.9515F, 0.0049F, -2.0F, 6, 0, 4, 0.0F));

        h_head = new ModelRenderer(this);
        h_head.setRotationPoint(3.2167F, -8.2106F, -0.8313F);
        chest.addChild(h_head);

        cube_r11 = new ModelRenderer(this);
        cube_r11.setRotationPoint(-0.125F, 6.3039F, 0.9272F);
        h_head.addChild(cube_r11);
        setRotationAngle(cube_r11, -0.7854F, 0.0F, 0.0F);
        cube_r11.cubeList.add(new ModelBox(cube_r11, 56, 16, -1.5F, -14.0472F, -8.0112F, 4, 8, 0, 0.0F));

        cube_r12 = new ModelRenderer(this);
        cube_r12.setRotationPoint(-0.125F, 6.3039F, 0.9272F);
        h_head.addChild(cube_r12);
        setRotationAngle(cube_r12, 0.7854F, 0.0F, 0.0F);
        cube_r12.cubeList.add(new ModelBox(cube_r12, 56, 8, -1.5F, -7.6847F, 5.6957F, 4, 8, 0, 0.0F));

        cube_r13 = new ModelRenderer(this);
        cube_r13.setRotationPoint(-0.125F, 6.3039F, 0.9272F);
        h_head.addChild(cube_r13);
        setRotationAngle(cube_r13, 0.0F, 0.3927F, 0.0F);
        cube_r13.cubeList.add(new ModelBox(cube_r13, 42, 12, -7.9609F, -12.2099F, -1.2496F, 8, 4, 0, 0.0F));

        cube_r14 = new ModelRenderer(this);
        cube_r14.setRotationPoint(-0.125F, 6.3039F, 0.9272F);
        h_head.addChild(cube_r14);
        setRotationAngle(cube_r14, -0.3927F, 0.0F, 0.0F);
        cube_r14.cubeList.add(new ModelBox(cube_r14, 48, 4, -4.0F, -8.6235F, -13.2338F, 8, 2, 0, 0.0F));
        cube_r14.cubeList.add(new ModelBox(cube_r14, 48, -6, -3.95F, -8.6235F, -13.2338F, 0, 2, 8, 0.0F));
        cube_r14.cubeList.add(new ModelBox(cube_r14, 48, -8, 3.95F, -8.6235F, -13.2338F, 0, 2, 8, 0.0F));
        cube_r14.cubeList.add(new ModelBox(cube_r14, 0, 0, -4.0F, -12.6235F, -13.2338F, 8, 4, 8, 0.0F));

        cube_r15 = new ModelRenderer(this);
        cube_r15.setRotationPoint(-0.125F, 6.3039F, 0.9272F);
        h_head.addChild(cube_r15);
        setRotationAngle(cube_r15, 0.0F, -0.3927F, 0.0F);
        cube_r15.cubeList.add(new ModelBox(cube_r15, 42, 8, -0.0391F, -12.2099F, -1.2496F, 8, 4, 0, 0.0F));

        h_jaw = new ModelRenderer(this);
        h_jaw.setRotationPoint(-0.125F, -3.5249F, -0.5172F);
        h_head.addChild(h_jaw);

        cube_r16 = new ModelRenderer(this);
        cube_r16.setRotationPoint(0.0F, 9.8289F, 1.4443F);
        h_jaw.addChild(cube_r16);
        setRotationAngle(cube_r16, 0.3927F, 0.0F, 0.0F);
        cube_r16.cubeList.add(new ModelBox(cube_r16, 0, 54, -4.0F, -11.8153F, -5.5355F, 8, 2, 0, 0.0F));
        cube_r16.cubeList.add(new ModelBox(cube_r16, 0, 44, -3.975F, -11.8153F, -5.5355F, 0, 2, 8, 0.0F));
        cube_r16.cubeList.add(new ModelBox(cube_r16, 0, 42, 3.975F, -11.8153F, -5.5355F, 0, 2, 8, 0.0F));
        cube_r16.cubeList.add(new ModelBox(cube_r16, 1, 12, -4.0F, -9.8153F, -5.5355F, 8, 4, 8, 0.0F));

        h_tongue = new ModelRenderer(this);
        h_tongue.setRotationPoint(0.0F, 0.8793F, -1.7489F);
        h_jaw.addChild(h_tongue);
        h_tongue.cubeList.add(new ModelBox(h_tongue, 32, 58, -2.0F, -0.4189F, -5.0087F, 4, 1, 5, 0.0F));

        cube_r17 = new ModelRenderer(this);
        cube_r17.setRotationPoint(0.0F, 8.9496F, 3.1932F);
        h_tongue.addChild(cube_r17);
        setRotationAngle(cube_r17, 0.3927F, 0.0F, 0.0F);
        cube_r17.cubeList.add(new ModelBox(cube_r17, 50, 59, -1.5F, -11.6255F, -7.8741F, 3, 1, 4, 0.0F));

        hitbox = new ModelRenderer(this);
        hitbox.setRotationPoint(0.0F, 2.5F, 0.0F);
        hitbox.cubeList.add(new ModelBox(hitbox, 0, 8, -4.0F, -3.5F, -4.0F, 8, 25, 8, 0.0F));
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

        body.rotateAngleX = 0.0f;
        body.rotateAngleY = 0.0f;
        body.rotateAngleZ = 0.0f;
        upper_body.rotateAngleZ = MathHelper.sin((ageInTicks * 0.6f) * 0.4f) * 0.08f;
        chest.rotateAngleX = MathHelper.sin((ageInTicks * 0.6f) * 0.4f) * 0.1f;
        right_arm.rotateAngleZ = MathHelper.sin((ageInTicks * 1.2f) * 0.4f) * 0.4f;
        left_arm.rotateAngleZ = -right_arm.rotateAngleZ;
        h_jaw.rotateAngleX = MathHelper.sin((ageInTicks * 0.6f) * 0.4f) * 0.15f;
    }
}
