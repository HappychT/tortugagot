package com.tortugagot.togcore.models;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;

public class StoneGolemModel extends ModelBase {

    private final ModelRenderer body;
    private final ModelRenderer left_leg;
    private final ModelRenderer cube_r1;
    private final ModelRenderer left_foot;
    private final ModelRenderer right_leg;
    private final ModelRenderer cube_r2;
    private final ModelRenderer right_foot;
    private final ModelRenderer upper_body;
    private final ModelRenderer chest;
    private final ModelRenderer cube_r3;
    private final ModelRenderer h_head;
    private final ModelRenderer cube_r4;
    private final ModelRenderer cube_r5;
    private final ModelRenderer cube_r6;
    private final ModelRenderer cube_r7;
    private final ModelRenderer cube_r8;
    private final ModelRenderer h_jaw;
    private final ModelRenderer right_arm;
    private final ModelRenderer cube_r9;
    private final ModelRenderer cube_r10;
    private final ModelRenderer cube_r11;
    private final ModelRenderer cube_r12;
    private final ModelRenderer cube_r13;
    private final ModelRenderer right_hand;
    private final ModelRenderer cube_r14;
    private final ModelRenderer left_arm;
    private final ModelRenderer cube_r15;
    private final ModelRenderer cube_r16;
    private final ModelRenderer cube_r17;
    private final ModelRenderer cube_r18;
    private final ModelRenderer cube_r19;
    private final ModelRenderer left_hand;
    private final ModelRenderer cube_r20;
    private final ModelRenderer hitbox;

    public StoneGolemModel() {
        textureWidth = 128;
        textureHeight = 128;

        body = new ModelRenderer(this);
        body.setRotationPoint(0.0F, 9.0F, 0.0F);

        left_leg = new ModelRenderer(this);
        left_leg.setRotationPoint(5.0F, 2.051F, 5.0792F);
        body.addChild(left_leg);
        setRotationAngle(left_leg, 0.0F, -0.1745F, 0.0F);

        cube_r1 = new ModelRenderer(this);
        cube_r1.setRotationPoint(-5.0F, -9.0756F, -5.0792F);
        left_leg.addChild(cube_r1);
        setRotationAngle(cube_r1, -0.7854F, 0.0F, 0.0F);
        cube_r1.cubeList.add(new ModelBox(cube_r1, 39, 84, 1.0F, 2.2794F, 6.7935F, 8, 10, 7, 0.0F));

        left_foot = new ModelRenderer(this);
        left_foot.setRotationPoint(0.0F, 4.6497F, -5.0521F);
        left_leg.addChild(left_foot);
        setRotationAngle(left_foot, 0.0F, 0.0873F, 0.0F);
        left_foot.cubeList.add(new ModelBox(left_foot, 80, 92, -5.0F, -0.5F, -4.5F, 10, 9, 9, 0.0F));

        right_leg = new ModelRenderer(this);
        right_leg.setRotationPoint(-5.0F, 2.051F, 5.0792F);
        body.addChild(right_leg);
        setRotationAngle(right_leg, 0.0F, 0.1745F, 0.0F);

        cube_r2 = new ModelRenderer(this);
        cube_r2.setRotationPoint(5.0F, -9.0756F, -5.0792F);
        right_leg.addChild(cube_r2);
        setRotationAngle(cube_r2, -0.7854F, 0.0F, 0.0F);
        cube_r2.cubeList.add(new ModelBox(cube_r2, 39, 67, -9.0F, 2.2794F, 6.7935F, 8, 10, 7, 0.0F));

        right_foot = new ModelRenderer(this);
        right_foot.setRotationPoint(0.0F, 4.6497F, -5.0521F);
        right_leg.addChild(right_foot);
        setRotationAngle(right_foot, 0.0F, -0.0873F, 0.0F);
        right_foot.cubeList.add(new ModelBox(right_foot, 80, 110, -5.0F, -0.5F, -4.5F, 10, 9, 9, 0.0F));

        upper_body = new ModelRenderer(this);
        upper_body.setRotationPoint(0.0F, 3.4403F, 4.2195F);
        body.addChild(upper_body);
        upper_body.cubeList.add(new ModelBox(upper_body, 0, 54, -7.0F, -10.1903F, -4.5433F, 14, 11, 9, 0.0F));

        chest = new ModelRenderer(this);
        chest.setRotationPoint(0.0F, -8.5597F, 0.0433F);
        upper_body.addChild(chest);
        setRotationAngle(chest, 0.0436F, 0.0F, 0.0F);

        cube_r3 = new ModelRenderer(this);
        cube_r3.setRotationPoint(0.0F, -1.9052F, -4.2627F);
        chest.addChild(cube_r3);
        setRotationAngle(cube_r3, 0.3927F, 0.0F, 0.0F);
        cube_r3.cubeList.add(new ModelBox(cube_r3, 0, 31, -9.0F, -7.2623F, -2.8208F, 18, 11, 12, 0.0F));

        h_head = new ModelRenderer(this);
        h_head.setRotationPoint(-0.0152F, -10.1F, -2.3624F);
        chest.addChild(h_head);
        h_head.cubeList.add(new ModelBox(h_head, 90, 10, -4.9848F, -7.0306F, -7.7242F, 10, 7, 9, 0.0F));
        h_head.cubeList.add(new ModelBox(h_head, 108, 31, 0.0152F, -11.0306F, -1.7242F, 5, 4, 0, 0.0F));

        cube_r4 = new ModelRenderer(this);
        cube_r4.setRotationPoint(0.0152F, 8.6948F, -1.9004F);
        h_head.addChild(cube_r4);
        setRotationAngle(cube_r4, 0.0F, 0.0F, -0.7854F);
        cube_r4.cubeList.add(new ModelBox(cube_r4, 18, 97, 4.584F, -14.6541F, -5.8238F, 3, 0, 9, 0.0F));

        cube_r5 = new ModelRenderer(this);
        cube_r5.setRotationPoint(0.0152F, 8.6948F, -1.9004F);
        h_head.addChild(cube_r5);
        setRotationAngle(cube_r5, 0.0F, 0.7854F, 0.0F);
        cube_r5.cubeList.add(new ModelBox(cube_r5, 49, 118, -1.8568F, -18.7254F, -4.3503F, 5, 3, 0, 0.0F));

        cube_r6 = new ModelRenderer(this);
        cube_r6.setRotationPoint(0.0152F, 8.6948F, -1.9004F);
        h_head.addChild(cube_r6);
        setRotationAngle(cube_r6, 0.0F, 0.0F, 0.7854F);
        cube_r6.cubeList.add(new ModelBox(cube_r6, 18, 97, -7.584F, -14.6541F, -5.8238F, 3, 0, 9, 0.0F));

        cube_r7 = new ModelRenderer(this);
        cube_r7.setRotationPoint(0.0152F, 8.6948F, -1.9004F);
        h_head.addChild(cube_r7);
        setRotationAngle(cube_r7, -0.7854F, 0.0F, 0.0F);
        cube_r7.cubeList.add(new ModelBox(cube_r7, 6, 94, -5.0F, -13.3654F, -8.8736F, 10, 0, 3, 0.0F));

        cube_r8 = new ModelRenderer(this);
        cube_r8.setRotationPoint(0.0152F, 8.6948F, -1.9004F);
        h_head.addChild(cube_r8);
        setRotationAngle(cube_r8, 0.7854F, 0.0F, 0.0F);
        cube_r8.cubeList.add(new ModelBox(cube_r8, 105, 0, -5.0F, -15.2366F, 4.0015F, 10, 0, 3, 0.0F));

        h_jaw = new ModelRenderer(this);
        h_jaw.setRotationPoint(0.0152F, -1.0306F, -1.7242F);
        h_head.addChild(h_jaw);
        h_jaw.cubeList.add(new ModelBox(h_jaw, 6, 77, -6.0F, -1.0F, -7.0F, 12, 6, 8, 0.0F));

        right_arm = new ModelRenderer(this);
        right_arm.setRotationPoint(-10.2266F, -8.6052F, -3.0646F);
        chest.addChild(right_arm);
        setRotationAngle(right_arm, 0.3341F, -0.103F, 0.288F);
        right_arm.cubeList.add(new ModelBox(right_arm, 0, 97, -7.7734F, -1.3753F, -4.3455F, 9, 8, 9, 0.0F));

        cube_r9 = new ModelRenderer(this);
        cube_r9.setRotationPoint(10.2266F, 6.6999F, -1.1982F);
        right_arm.addChild(cube_r9);
        setRotationAngle(cube_r9, 0.7854F, 0.0F, 0.0F);
        cube_r9.cubeList.add(new ModelBox(cube_r9, 110, 35, -18.0F, -1.5716F, 9.8475F, 9, 5, 0, 0.0F));

        cube_r10 = new ModelRenderer(this);
        cube_r10.setRotationPoint(10.2266F, 6.6999F, -1.1982F);
        right_arm.addChild(cube_r10);
        setRotationAngle(cube_r10, 0.0F, 0.0F, -0.7854F);
        cube_r10.cubeList.add(new ModelBox(cube_r10, 109, 26, -12.0179F, -18.437F, -3.1473F, 5, 0, 9, 0.0F));

        cube_r11 = new ModelRenderer(this);
        cube_r11.setRotationPoint(10.2266F, 6.6999F, -1.1982F);
        right_arm.addChild(cube_r11);
        setRotationAngle(cube_r11, -0.7854F, 0.0F, 0.0F);
        cube_r11.cubeList.add(new ModelBox(cube_r11, 97, 58, -18.0F, -3.4845F, -7.9356F, 9, 5, 0, 0.0F));

        cube_r12 = new ModelRenderer(this);
        cube_r12.setRotationPoint(10.2266F, 6.6999F, -1.1982F);
        right_arm.addChild(cube_r12);
        setRotationAngle(cube_r12, -0.3927F, 0.0F, 0.0F);
        cube_r12.cubeList.add(new ModelBox(cube_r12, 28, 114, -17.0F, -1.9792F, -2.2843F, 7, 7, 7, 0.0F));

        cube_r13 = new ModelRenderer(this);
        cube_r13.setRotationPoint(10.2266F, 6.6999F, -1.1982F);
        right_arm.addChild(cube_r13);
        setRotationAngle(cube_r13, 0.0F, 0.0F, -0.3927F);
        cube_r13.cubeList.add(new ModelBox(cube_r13, 115, 65, -11.8308F, -18.169F, 0.8527F, 6, 7, 0, 0.0F));

        right_hand = new ModelRenderer(this);
        right_hand.setRotationPoint(-3.2734F, 11.0331F, -1.8716F);
        right_arm.addChild(right_hand);
        setRotationAngle(right_hand, 0.0886F, -0.1739F, -0.0154F);

        cube_r14 = new ModelRenderer(this);
        cube_r14.setRotationPoint(13.5F, -4.3331F, 0.6734F);
        right_hand.addChild(cube_r14);
        setRotationAngle(cube_r14, -0.7854F, 0.0F, 0.0F);
        cube_r14.cubeList.add(new ModelBox(cube_r14, 88, 63, -18.0F, 10.3637F, -1.9122F, 9, 6, 9, 0.0F));
        cube_r14.cubeList.add(new ModelBox(cube_r14, 84, 78, -19.0F, 7.3637F, -2.9122F, 11, 3, 11, 0.0F));
        cube_r14.cubeList.add(new ModelBox(cube_r14, 36, 101, -18.0F, 3.3637F, -1.9122F, 9, 4, 9, 0.0F));

        left_arm = new ModelRenderer(this);
        left_arm.setRotationPoint(10.2266F, -8.6052F, -3.0646F);
        chest.addChild(left_arm);
        setRotationAngle(left_arm, 0.3341F, 0.103F, -0.288F);
        left_arm.cubeList.add(new ModelBox(left_arm, 0, 97, -1.2266F, -1.3753F, -4.3455F, 9, 8, 9, 0.0F));

        cube_r15 = new ModelRenderer(this);
        cube_r15.setRotationPoint(-10.2266F, 6.6999F, -1.1982F);
        left_arm.addChild(cube_r15);
        setRotationAngle(cube_r15, 0.7854F, 0.0F, 0.0F);
        cube_r15.cubeList.add(new ModelBox(cube_r15, 110, 35, 9.0F, -1.5716F, 9.8475F, 9, 5, 0, 0.0F));

        cube_r16 = new ModelRenderer(this);
        cube_r16.setRotationPoint(-10.2266F, 6.6999F, -1.1982F);
        left_arm.addChild(cube_r16);
        setRotationAngle(cube_r16, 0.0F, 0.0F, 0.7854F);
        cube_r16.cubeList.add(new ModelBox(cube_r16, 109, 26, 7.0179F, -18.437F, -3.1473F, 5, 0, 9, 0.0F));

        cube_r17 = new ModelRenderer(this);
        cube_r17.setRotationPoint(-10.2266F, 6.6999F, -1.1982F);
        left_arm.addChild(cube_r17);
        setRotationAngle(cube_r17, -0.7854F, 0.0F, 0.0F);
        cube_r17.cubeList.add(new ModelBox(cube_r17, 97, 58, 9.0F, -3.4845F, -7.9356F, 9, 5, 0, 0.0F));

        cube_r18 = new ModelRenderer(this);
        cube_r18.setRotationPoint(-10.2266F, 6.6999F, -1.1982F);
        left_arm.addChild(cube_r18);
        setRotationAngle(cube_r18, -0.3927F, 0.0F, 0.0F);
        cube_r18.cubeList.add(new ModelBox(cube_r18, 0, 114, 10.0F, -1.9792F, -2.2843F, 7, 7, 7, 0.0F));

        cube_r19 = new ModelRenderer(this);
        cube_r19.setRotationPoint(-10.2266F, 6.6999F, -1.1982F);
        left_arm.addChild(cube_r19);
        setRotationAngle(cube_r19, 0.0F, 0.0F, 0.3927F);
        cube_r19.cubeList.add(new ModelBox(cube_r19, 115, 65, 5.8308F, -18.169F, 0.8527F, 6, 7, 0, 0.0F));

        left_hand = new ModelRenderer(this);
        left_hand.setRotationPoint(3.2734F, 11.0331F, -1.8716F);
        left_arm.addChild(left_hand);
        setRotationAngle(left_hand, 0.0886F, 0.1739F, 0.0154F);

        cube_r20 = new ModelRenderer(this);
        cube_r20.setRotationPoint(-13.5F, -4.3331F, 0.6734F);
        left_hand.addChild(cube_r20);
        setRotationAngle(cube_r20, -0.7854F, 0.0F, 0.0F);
        cube_r20.cubeList.add(new ModelBox(cube_r20, 88, 63, 9.0F, 10.3637F, -1.9122F, 9, 6, 9, 0.0F));
        cube_r20.cubeList.add(new ModelBox(cube_r20, 84, 78, 8.0F, 7.3637F, -2.9122F, 11, 3, 11, 0.0F));
        cube_r20.cubeList.add(new ModelBox(cube_r20, 36, 101, 9.0F, 3.3637F, -1.9122F, 9, 4, 9, 0.0F));

        hitbox = new ModelRenderer(this);
        hitbox.setRotationPoint(0.0F, -8.5F, 0.0F);
        hitbox.cubeList.add(new ModelBox(hitbox, 0, 22, -11.0F, -6.5F, -11.0F, 22, 39, 22, 0.0F));
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
        h_jaw.rotateAngleX = MathHelper.sin(ageInTicks * 0.15f) * 0.07f;

        body.rotateAngleY = MathHelper.sin(limbSwing * 0.6f) * (limbSwingAmount * 0.3f);
        body.rotateAngleZ = MathHelper.sin(limbSwing * 0.6f) * (limbSwingAmount * 0.15f);
        right_arm.rotateAngleX = MathHelper.sin(limbSwing * 0.6f) * (limbSwingAmount * 0.4f);
        right_hand.rotateAngleX = MathHelper.sin(limbSwing * 0.6f) * (limbSwingAmount * 1f);
        right_leg.rotateAngleX = MathHelper.sin(limbSwing * 0.6f) * (limbSwingAmount * 0.5f);
        right_foot.rotateAngleX = MathHelper.sin(limbSwing * 0.6f) * (limbSwingAmount * 0.7f);
        left_arm.rotateAngleX = -right_arm.rotateAngleX;
        left_hand.rotateAngleX = -right_hand.rotateAngleX;
        left_leg.rotateAngleX = -right_leg.rotateAngleX;
        left_foot.rotateAngleX = -right_foot.rotateAngleX;
    }
}
