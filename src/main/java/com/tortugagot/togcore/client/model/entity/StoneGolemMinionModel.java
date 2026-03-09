package com.tortugagot.togcore.client.model.entity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;

public class StoneGolemMinionModel extends ModelBase {

    private final ModelRenderer body;
    private final ModelRenderer head;
    private final ModelRenderer cube_r1;
    private final ModelRenderer cube_r2;
    private final ModelRenderer cube_r3;
    private final ModelRenderer cube_r4;
    private final ModelRenderer cube_r5;
    private final ModelRenderer cube_r6;
    private final ModelRenderer cube_r7;
    private final ModelRenderer left_arm;
    private final ModelRenderer cube_r8;
    private final ModelRenderer right_arm;
    private final ModelRenderer cube_r9;
    private final ModelRenderer left_leg;
    private final ModelRenderer right_leg;
    private final ModelRenderer hitbox;

    public StoneGolemMinionModel() {
        textureWidth = 128;
        textureHeight = 128;

        body = new ModelRenderer(this);
        body.setRotationPoint(0.0F, 17.0F, 0.0F);

        head = new ModelRenderer(this);
        head.setRotationPoint(-0.0553F, -0.6113F, 2.4995F);
        body.addChild(head);
        head.cubeList.add(new ModelBox(head, 0, 11, -1.9447F, -1.3137F, -9.9995F, 4, 5, 2, 0.0F));
        head.cubeList.add(new ModelBox(head, -11, 0, -6.9447F, -7.4137F, -7.9995F, 14, 0, 11, 0.0F));
        head.cubeList.add(new ModelBox(head, 0, 106, -6.9447F, -7.3137F, -7.9995F, 14, 11, 11, 0.0F));

        cube_r1 = new ModelRenderer(this);
        cube_r1.setRotationPoint(-0.9605F, -4.2248F, -2.4995F);
        head.addChild(cube_r1);
        setRotationAngle(cube_r1, 0.0F, 0.0F, -0.7854F);
        cube_r1.cubeList.add(new ModelBox(cube_r1, -11, 18, -5.9758F, -6.4861F, -5.5F, 4, 0, 11, 0.0F));

        cube_r2 = new ModelRenderer(this);
        cube_r2.setRotationPoint(3.0553F, -2.0765F, -2.4995F);
        head.addChild(cube_r2);
        setRotationAngle(cube_r2, 0.0F, 0.0F, 0.7854F);
        cube_r2.cubeList.add(new ModelBox(cube_r2, 10, 0, -13.2919F, -2.0844F, 4.1585F, 8, 4, 0, 0.0F));

        cube_r3 = new ModelRenderer(this);
        cube_r3.setRotationPoint(3.0553F, -2.0765F, -2.4995F);
        head.addChild(cube_r3);
        setRotationAngle(cube_r3, 0.0F, 0.3927F, 0.0F);
        cube_r3.cubeList.add(new ModelBox(cube_r3, 8, 55, -7.6921F, -9.3372F, -5.0682F, 8, 4, 0, 0.0F));

        cube_r4 = new ModelRenderer(this);
        cube_r4.setRotationPoint(3.0553F, -2.0765F, -2.4995F);
        head.addChild(cube_r4);
        setRotationAngle(cube_r4, 0.0F, 0.0F, 0.3927F);
        cube_r4.cubeList.add(new ModelBox(cube_r4, 8, 63, -2.8384F, -11.5551F, 0.6061F, 4, 7, 0, 0.0F));

        cube_r5 = new ModelRenderer(this);
        cube_r5.setRotationPoint(1.0711F, -4.2248F, -2.4995F);
        head.addChild(cube_r5);
        setRotationAngle(cube_r5, 0.0F, 0.0F, 0.7854F);
        cube_r5.cubeList.add(new ModelBox(cube_r5, -11, 18, 1.9758F, -6.4861F, -5.5F, 4, 0, 11, 0.0F));

        cube_r6 = new ModelRenderer(this);
        cube_r6.setRotationPoint(2.7474F, -4.5876F, -4.1237F);
        head.addChild(cube_r6);
        setRotationAngle(cube_r6, -0.7854F, 0.0F, 0.0F);
        cube_r6.cubeList.add(new ModelBox(cube_r6, 12, 72, -9.6921F, -7.0356F, 3.0385F, 14, 0, 4, 0.0F));

        cube_r7 = new ModelRenderer(this);
        cube_r7.setRotationPoint(0.0553F, -5.9998F, -9.413F);
        head.addChild(cube_r7);
        setRotationAngle(cube_r7, 0.7854F, 0.0F, 0.0F);
        cube_r7.cubeList.add(new ModelBox(cube_r7, 12, 68, -7.0F, 0.0F, -2.0F, 14, 0, 4, 0.0F));

        left_arm = new ModelRenderer(this);
        left_arm.setRotationPoint(7.2128F, -3.8966F, 0.0F);
        body.addChild(left_arm);

        cube_r8 = new ModelRenderer(this);
        cube_r8.setRotationPoint(-7.2128F, 2.7087F, 0.0F);
        left_arm.addChild(cube_r8);
        setRotationAngle(cube_r8, 0.0F, 0.0F, -0.3927F);
        cube_r8.cubeList.add(new ModelBox(cube_r8, 0, 85, 5.3065F, -0.228F, -3.0F, 5, 9, 6, 0.0F));
        cube_r8.cubeList.add(new ModelBox(cube_r8, 0, 62, 10.3065F, 2.772F, 0.0F, 4, 6, 0, 0.0F));

        right_arm = new ModelRenderer(this);
        right_arm.setRotationPoint(-7.2128F, -3.8966F, 0.0F);
        body.addChild(right_arm);

        cube_r9 = new ModelRenderer(this);
        cube_r9.setRotationPoint(7.2128F, 2.7087F, 0.0F);
        right_arm.addChild(cube_r9);
        setRotationAngle(cube_r9, 0.0F, 0.0F, 0.3927F);
        cube_r9.cubeList.add(new ModelBox(cube_r9, 0, 70, -10.3065F, -0.228F, -3.0F, 5, 9, 6, 0.0F));
        cube_r9.cubeList.add(new ModelBox(cube_r9, 0, 56, -14.3065F, 2.772F, 0.0F, 4, 6, 0, 0.0F));

        left_leg = new ModelRenderer(this);
        left_leg.setRotationPoint(5.0579F, 3.075F, 0.0F);
        body.addChild(left_leg);
        left_leg.cubeList.add(new ModelBox(left_leg, 22, 90, -2.0F, 0.0F, -3.0F, 4, 4, 6, 0.0F));

        right_leg = new ModelRenderer(this);
        right_leg.setRotationPoint(-5.0579F, 3.075F, 0.0F);
        body.addChild(right_leg);
        right_leg.cubeList.add(new ModelBox(right_leg, 22, 80, -2.0F, 0.0F, -3.0F, 4, 4, 6, 0.0F));

        hitbox = new ModelRenderer(this);
        hitbox.setRotationPoint(0.0F, 15.5F, -0.5F);
        hitbox.cubeList.add(new ModelBox(hitbox, 0, 13, -6.5F, -8.5F, -6.5F, 13, 17, 13, 0.0F));
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

        body.rotateAngleY = MathHelper.sin(limbSwing * 1.1f) * (limbSwingAmount * 0.4f);
        body.rotateAngleZ = MathHelper.sin(limbSwing * 1.1f) * (limbSwingAmount * 0.3f);

        right_arm.rotateAngleX = MathHelper.sin(limbSwing * 1.2f) * (limbSwingAmount * 0.3f);
        right_arm.rotateAngleZ = MathHelper.sin(limbSwing * 1.2f) * (limbSwingAmount * 0.5f);
        right_leg.rotateAngleX = MathHelper.sin(limbSwing * 1.2f) * (limbSwingAmount * 0.4f);
        left_arm.rotateAngleX = -right_arm.rotateAngleX;
        left_arm.rotateAngleZ = -right_arm.rotateAngleZ;
        left_leg.rotateAngleX = -right_leg.rotateAngleX;
    }
}
