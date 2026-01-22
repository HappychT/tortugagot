package com.tortugagot.togcore.models;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;

public class BeaverModel extends ModelBase {

    private final ModelRenderer bs_beaver;
    private final ModelRenderer left_front;
    private final ModelRenderer right_front;
    private final ModelRenderer left_back;
    private final ModelRenderer right_back;
    private final ModelRenderer tail;
    private final ModelRenderer cube_r1;
    private final ModelRenderer head;
    private final ModelRenderer cube_r2;
    private final ModelRenderer cube_r3;

    public BeaverModel() {
        textureWidth = 64;
        textureHeight = 64;

        bs_beaver = new ModelRenderer(this);
        bs_beaver.setRotationPoint(0.0F, 21.75F, 6.5F);
        bs_beaver.cubeList.add(new ModelBox(bs_beaver, 0, 46, -4.0F, -6.5F, -11.5F, 8, 7, 11, 0.0F));

        left_front = new ModelRenderer(this);
        left_front.setRotationPoint(2.0F, 0.25F, -9.5F);
        bs_beaver.addChild(left_front);
        left_front.cubeList.add(new ModelBox(left_front, 52, 21, -1.5F, -2.0F, -1.5F, 3, 4, 3, 0.0F));

        right_front = new ModelRenderer(this);
        right_front.setRotationPoint(-2.0F, 0.25F, -9.5F);
        bs_beaver.addChild(right_front);
        right_front.cubeList.add(new ModelBox(right_front, 52, 14, -1.5F, -2.0F, -1.5F, 3, 4, 3, 0.0F));

        left_back = new ModelRenderer(this);
        left_back.setRotationPoint(2.0F, 0.25F, -2.5F);
        bs_beaver.addChild(left_back);
        left_back.cubeList.add(new ModelBox(left_back, 52, 7, -1.5F, -2.0F, -1.5F, 3, 4, 3, 0.0F));

        right_back = new ModelRenderer(this);
        right_back.setRotationPoint(-2.0F, 0.25F, -2.5F);
        bs_beaver.addChild(right_back);
        right_back.cubeList.add(new ModelBox(right_back, 52, 0, -1.5F, -2.0F, -1.5F, 3, 4, 3, 0.0F));

        tail = new ModelRenderer(this);
        tail.setRotationPoint(0.0F, -3.5391F, -0.4898F);
        bs_beaver.addChild(tail);

        cube_r1 = new ModelRenderer(this);
        cube_r1.setRotationPoint(0.0F, 0.7891F, 4.4898F);
        tail.addChild(cube_r1);
        setRotationAngle(cube_r1, -0.3927F, 0.0F, 0.0F);
        cube_r1.cubeList.add(new ModelBox(cube_r1, 0, 0, -3.0F, 0.0F, -4.25F, 6, 2, 10, 0.0F));

        head = new ModelRenderer(this);
        head.setRotationPoint(-0.075F, -3.0F, -11.5F);
        bs_beaver.addChild(head);
        head.cubeList.add(new ModelBox(head, 38, 54, -2.925F, -3.0F, -4.0F, 6, 6, 4, 0.0F));
        head.cubeList.add(new ModelBox(head, 44, 43, -1.925F, 0.0F, -5.0F, 4, 2, 1, 0.0F));
        head.cubeList.add(new ModelBox(head, 38, 57, -0.925F, 2.0F, -4.5F, 2, 1, 0, 0.0F));
        head.cubeList.add(new ModelBox(head, 41, 46, -2.925F, -3.0F, 0.0F, 6, 6, 2, 0.0F));

        cube_r2 = new ModelRenderer(this);
        cube_r2.setRotationPoint(-2.0F, -3.0F, -2.0F);
        head.addChild(cube_r2);
        setRotationAngle(cube_r2, 0.0F, 0.3927F, 0.0F);
        cube_r2.cubeList.add(new ModelBox(cube_r2, 58, 28, -2.0F, -1.0F, 0.0F, 2, 2, 0, 0.0F));

        cube_r3 = new ModelRenderer(this);
        cube_r3.setRotationPoint(2.15F, -3.0F, -2.0F);
        head.addChild(cube_r3);
        setRotationAngle(cube_r3, 0.0F, -0.3927F, 0.0F);
        cube_r3.cubeList.add(new ModelBox(cube_r3, 54, 28, 0.0F, -1.0F, 0.0F, 2, 2, 0, 0.0F));
    }

    @Override
    public void render(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw,
        float headPitch, float scaleFactor) {
        setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, entity);
        bs_beaver.render(scaleFactor);
    }

    public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }

    @Override
    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw,
        float headPitch, float scaleFactor, Entity entity) {
        bs_beaver.rotateAngleZ = MathHelper.sin(limbSwing * 1.5f) * (limbSwingAmount * 0.2f);
        tail.rotateAngleX = MathHelper.sin(limbSwing * 1.5f) * (limbSwingAmount * 0.2f);
        head.rotateAngleZ = MathHelper.sin(limbSwing * 1.5f) * (limbSwingAmount * 0.2f);
        left_front.rotateAngleX = MathHelper.sin(limbSwing * 1.5f) * (limbSwingAmount * 2f);
        left_back.rotateAngleX = -MathHelper.sin(limbSwing * -1.5f) * (limbSwingAmount * 2f);
        right_front.rotateAngleX = -left_front.rotateAngleX;
        right_back.rotateAngleX = -left_back.rotateAngleX;

    }
}
