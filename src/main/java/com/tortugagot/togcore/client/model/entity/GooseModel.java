package com.tortugagot.togcore.client.model.entity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;

public class GooseModel extends ModelBase {

    private final ModelRenderer goose;
    private final ModelRenderer cube_r1;
    private final ModelRenderer neck1;
    private final ModelRenderer neck2;
    private final ModelRenderer neck3;
    private final ModelRenderer h_head;
    private final ModelRenderer h_headhonk;
    private final ModelRenderer cube_r2;
    private final ModelRenderer right_leg;
    private final ModelRenderer left_leg;
    private final ModelRenderer left_wing;
    private final ModelRenderer cube_r3;
    private final ModelRenderer right_wing;
    private final ModelRenderer cube_r4;
    private final ModelRenderer empty;

    public GooseModel() {
        textureWidth = 64;
        textureHeight = 64;

        goose = new ModelRenderer(this);
        goose.setRotationPoint(0.0F, 16.25F, 2.0F);
        setRotationAngle(goose, -0.3054F, 0.0F, 0.0F);
        goose.cubeList.add(new ModelBox(goose, 16, 45, -4.0F, -4.0F, -6.0F, 8, 8, 10, 0.0F));
        goose.cubeList.add(new ModelBox(goose, 16, 27, -3.0F, -4.0F, 4.0F, 6, 6, 4, 0.0F));
        goose.cubeList.add(new ModelBox(goose, 37, 28, -3.0F, -3.0F, -9.0F, 6, 6, 3, 0.0F));

        cube_r1 = new ModelRenderer(this);
        cube_r1.setRotationPoint(0.0F, -3.5F, 7.5F);
        goose.addChild(cube_r1);
        setRotationAngle(cube_r1, -0.7854F, 0.0F, 0.0F);
        cube_r1.cubeList.add(new ModelBox(cube_r1, 3, 5, -1.5F, -2.5F, -0.5F, 3, 5, 1, 0.0F));

        neck1 = new ModelRenderer(this);
        neck1.setRotationPoint(-0.25F, -2.0F, -7.5F);
        goose.addChild(neck1);
        setRotationAngle(neck1, 0.7418F, 0.0F, 0.0F);
        neck1.cubeList.add(new ModelBox(neck1, 37, 5, -1.5F, -3.0F, -1.5F, 3, 3, 3, 0.0F));

        neck2 = new ModelRenderer(this);
        neck2.setRotationPoint(0.0F, -3.0F, 0.0F);
        neck1.addChild(neck2);
        setRotationAngle(neck2, -0.2182F, 0.0F, 0.0F);
        neck2.cubeList.add(new ModelBox(neck2, 42, 48, -1.5F, -4.0F, -1.5F, 3, 4, 3, 0.0F));

        neck3 = new ModelRenderer(this);
        neck3.setRotationPoint(0.0F, -4.0F, 0.0F);
        neck2.addChild(neck3);
        setRotationAngle(neck3, -0.2182F, 0.0F, 0.0F);
        neck3.cubeList.add(new ModelBox(neck3, 11, 43, -1.5F, -4.0F, -1.5F, 3, 4, 3, 0.0F));

        h_head = new ModelRenderer(this);
        h_head.setRotationPoint(0.0F, -4.0F, 0.0F);
        neck3.addChild(h_head);
        h_head.cubeList.add(new ModelBox(h_head, 37, 1, -1.5F, -1.0F, -1.5F, 3, 1, 3, 0.0F));

        h_headhonk = new ModelRenderer(this);
        h_headhonk.setRotationPoint(0.0F, -4.0F, 0.0F);
        neck3.addChild(h_headhonk);
        h_headhonk.cubeList.add(new ModelBox(h_headhonk, 0, 28, -1.5F, -5.0F, -3.5F, 3, 4, 5, 0.0F));
        h_headhonk.cubeList.add(new ModelBox(h_headhonk, 37, 1, -1.5F, -1.0F, -1.5F, 3, 1, 3, 0.0F));
        h_headhonk.cubeList.add(new ModelBox(h_headhonk, 37, 13, -1.5F, -3.0F, -6.5F, 3, 2, 3, 0.0F));

        cube_r2 = new ModelRenderer(this);
        cube_r2.setRotationPoint(0.25F, -1.0F, -2.5F);
        h_headhonk.addChild(cube_r2);
        setRotationAngle(cube_r2, 0.3927F, 0.0F, 0.0F);
        cube_r2.cubeList.add(new ModelBox(cube_r2, 0, 1, -1.25F, -0.5F, -3.0F, 2, 1, 3, 0.0F));

        right_leg = new ModelRenderer(this);
        right_leg.setRotationPoint(-2.5F, 4.0F, -0.5F);
        goose.addChild(right_leg);
        setRotationAngle(right_leg, 0.3054F, 0.0F, 0.0F);
        right_leg.cubeList.add(new ModelBox(right_leg, 0, 18, -0.5F, -1.0F, -0.5F, 1, 5, 1, 0.0F));
        right_leg.cubeList.add(new ModelBox(right_leg, 0, 19, -1.5F, 4.0F, -4.5F, 3, 0, 5, 0.0F));

        left_leg = new ModelRenderer(this);
        left_leg.setRotationPoint(2.5F, 4.0F, -0.5F);
        goose.addChild(left_leg);
        setRotationAngle(left_leg, 0.3054F, 0.0F, 0.0F);
        left_leg.cubeList.add(new ModelBox(left_leg, 0, 18, -0.5F, -1.0F, -0.5F, 1, 5, 1, 0.0F));
        left_leg.cubeList.add(new ModelBox(left_leg, 0, 19, -1.5F, 4.0F, -4.5F, 3, 0, 5, 0.0F));

        left_wing = new ModelRenderer(this);
        left_wing.setRotationPoint(3.0F, -4.0F, -4.0F);
        goose.addChild(left_wing);
        setRotationAngle(left_wing, -0.2602F, -0.2975F, -0.8335F);
        left_wing.cubeList.add(new ModelBox(left_wing, 20, 37, 0.0F, 0.0F, -1.0F, 9, 2, 6, 0.0F));
        left_wing.cubeList.add(new ModelBox(left_wing, 2, 50, 0.0F, 0.0F, 5.0F, 9, 2, 3, 0.0F));

        cube_r3 = new ModelRenderer(this);
        cube_r3.setRotationPoint(13.6987F, 3.0286F, 2.75F);
        left_wing.addChild(cube_r3);
        setRotationAngle(cube_r3, 0.0F, 0.0F, 0.3927F);
        cube_r3.cubeList.add(new ModelBox(cube_r3, 0, 13, -5.5F, -1.0F, -3.75F, 13, 2, 11, 0.0F));
        cube_r3.cubeList.add(new ModelBox(cube_r3, 0, 37, -5.5F, -1.0F, -3.75F, 9, 2, 4, 0.0F));

        right_wing = new ModelRenderer(this);
        right_wing.setRotationPoint(-3.0F, -4.0F, -4.0F);
        goose.addChild(right_wing);
        setRotationAngle(right_wing, -0.2602F, 0.2975F, 0.8335F);
        right_wing.cubeList.add(new ModelBox(right_wing, 20, 37, -9.0F, 0.0F, -1.0F, 9, 2, 6, 0.0F));
        right_wing.cubeList.add(new ModelBox(right_wing, 2, 50, -9.0F, 0.0F, 5.0F, 9, 2, 3, 0.0F));

        cube_r4 = new ModelRenderer(this);
        cube_r4.setRotationPoint(-13.6987F, 3.0286F, 2.75F);
        right_wing.addChild(cube_r4);
        setRotationAngle(cube_r4, 0.0F, 0.0F, -0.3927F);
        cube_r4.cubeList.add(new ModelBox(cube_r4, 0, 0, -7.5F, -1.0F, -3.75F, 13, 2, 11, 0.0F));
        cube_r4.cubeList.add(new ModelBox(cube_r4, 0, 37, -3.5F, -1.0F, -3.75F, 9, 2, 4, 0.0F));

        empty = new ModelRenderer(this);
        empty.setRotationPoint(0.0F, 24.0F, 0.0F);
        empty.cubeList.add(new ModelBox(empty, 0, 1, -1.0F, -11.0F, 0.0F, 1, 1, 1, 0.0F));
    }

    @Override
    public void render(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw,
        float headPitch, float scaleFactor) {
        setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, entity);
        goose.render(scaleFactor);
    }

    public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }

    @Override
    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw,
        float headPitch, float scaleFactor, Entity entity) {
        right_wing.rotateAngleZ = MathHelper.sin(ageInTicks * 0.2f) * 0.3f;
        left_wing.rotateAngleZ = -right_wing.rotateAngleZ;

        goose.rotateAngleX = (float) Math.toRadians(1);
        goose.rotateAngleY = MathHelper.sin(limbSwing * 1.4f) * (limbSwingAmount * 0.05f);
        goose.rotateAngleZ = MathHelper.sin(limbSwing * 1.4f) * (limbSwingAmount * 0.15f);
        right_leg.rotateAngleX = MathHelper.sin(limbSwing * 1.4f) * (limbSwingAmount * 0.9f);
        left_leg.rotateAngleX = -right_leg.rotateAngleX;
    }
}
