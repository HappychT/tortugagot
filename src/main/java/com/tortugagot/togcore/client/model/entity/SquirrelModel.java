package com.tortugagot.togcore.client.model.entity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;

public class SquirrelModel extends ModelBase {

    private final ModelRenderer bs_squirrel_orange;
    private final ModelRenderer head;
    private final ModelRenderer cube_r1;
    private final ModelRenderer tail;
    private final ModelRenderer thighs;
    private final ModelRenderer feet;
    private final ModelRenderer arms;

    public SquirrelModel() {
        textureWidth = 64;
        textureHeight = 64;

        bs_squirrel_orange = new ModelRenderer(this);
        bs_squirrel_orange.setRotationPoint(0.0F, 18.0F, 5.0F);
        setRotationAngle(bs_squirrel_orange, 1.1781F, 0.0F, 0.0F);
        bs_squirrel_orange.cubeList.add(new ModelBox(bs_squirrel_orange, 14, 7, -2.5F, -7.5F, -5.0F, 5, 7, 4, 0.0F));

        head = new ModelRenderer(this);
        head.setRotationPoint(0.0F, -7.241F, -3.8264F);
        bs_squirrel_orange.addChild(head);
        setRotationAngle(head, -1.0472F, 0.0F, 0.0F);
        head.cubeList.add(new ModelBox(head, 35, 7, -1.0F, -1.259F, -4.1736F, 2, 2, 1, 0.0F));
        head.cubeList.add(new ModelBox(head, 46, 20, -2.0F, -3.259F, -3.1736F, 4, 4, 4, 0.0F));

        cube_r1 = new ModelRenderer(this);
        cube_r1.setRotationPoint(-1.0F, -3.759F, -0.1736F);
        head.addChild(cube_r1);
        setRotationAngle(cube_r1, -0.3927F, 0.0F, 0.0F);
        cube_r1.cubeList.add(new ModelBox(cube_r1, 11, 3, -0.5F, -1.25F, -0.25F, 0, 3, 2, 0.0F));
        cube_r1.cubeList.add(new ModelBox(cube_r1, 11, 0, 2.5F, -1.25F, -0.25F, 0, 3, 2, 0.0F));

        tail = new ModelRenderer(this);
        tail.setRotationPoint(0.0F, 0.25F, -3.0F);
        bs_squirrel_orange.addChild(tail);
        setRotationAngle(tail, -0.6109F, 0.0F, 0.0F);
        tail.cubeList.add(new ModelBox(tail, 0, 5, -2.0F, -9.75F, 0.0F, 4, 10, 3, 0.0F));
        tail.cubeList.add(new ModelBox(tail, 0, 18, -2.0F, -9.75F, 3.0F, 4, 7, 3, 0.0F));

        thighs = new ModelRenderer(this);
        thighs.setRotationPoint(0.0F, -1.5F, -3.4F);
        bs_squirrel_orange.addChild(thighs);
        setRotationAngle(thighs, -1.4399F, 0.0F, 0.0F);
        thighs.cubeList.add(new ModelBox(thighs, 44, 5, -3.0F, -2.0F, -2.0F, 2, 4, 4, 0.0F));
        thighs.cubeList.add(new ModelBox(thighs, 32, 10, 1.0F, -2.0F, -2.0F, 2, 4, 4, 0.0F));

        feet = new ModelRenderer(this);
        feet.setRotationPoint(0.0F, 2.0F, 2.0F);
        thighs.addChild(feet);
        setRotationAngle(feet, 0.2618F, 0.0F, 0.0F);
        feet.cubeList.add(new ModelBox(feet, 44, 0, -3.0F, 0.0F, -4.0F, 2, 1, 4, 0.0F));
        feet.cubeList.add(new ModelBox(feet, 44, 13, 1.0F, 0.0F, -4.0F, 2, 1, 4, 0.0F));

        arms = new ModelRenderer(this);
        arms.setRotationPoint(0.0F, -6.25F, -3.45F);
        bs_squirrel_orange.addChild(arms);
        setRotationAngle(arms, 0.1745F, 0.0F, 0.0F);
        arms.cubeList.add(new ModelBox(arms, 30, 20, -4.0F, -0.75F, -5.25F, 2, 2, 6, 0.0F));
        arms.cubeList.add(new ModelBox(arms, 14, 20, 2.25F, -0.75F, -5.25F, 2, 2, 6, 0.0F));
    }

    @Override
    public void render(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw,
        float headPitch, float scaleFactor) {
        setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, entity);
        bs_squirrel_orange.render(scaleFactor);
    }

    public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }

    @Override
    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw,
        float headPitch, float scaleFactor, Entity entity) {

        tail.rotateAngleY = MathHelper.sin((ageInTicks * 0.6f) * 0.4f) * 0.05f;

        head.rotateAngleX = (float) Math.toRadians(-60);
        tail.rotateAngleX = (float) Math.toRadians(-60);
        thighs.rotateAngleX = (float) Math.toRadians(-40);
        head.rotateAngleX += MathHelper.sin(limbSwing * 1.6f) * 0.8f * limbSwingAmount;
        arms.rotateAngleX = MathHelper.sin(limbSwing * 1.6f) * limbSwingAmount;
        thighs.rotateAngleX += MathHelper.sin(limbSwing * 1.6f) * limbSwingAmount;
        tail.rotateAngleX += -MathHelper.sin(limbSwing * 1.6f) * 0.3f * limbSwingAmount;

    }
}
