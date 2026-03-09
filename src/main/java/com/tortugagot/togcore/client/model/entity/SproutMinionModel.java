package com.tortugagot.togcore.client.model.entity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;

public class SproutMinionModel extends ModelBase {

    private final ModelRenderer sprout_blossom;
    private final ModelRenderer hitbox;
    private final ModelRenderer head;
    private final ModelRenderer branch;
    private final ModelRenderer head_r1;
    private final ModelRenderer head_r2;
    private final ModelRenderer right_thigh;
    private final ModelRenderer legs_r1;
    private final ModelRenderer right_foot;
    private final ModelRenderer left_thigh;
    private final ModelRenderer legs_r2;
    private final ModelRenderer left_foot;
    private final ModelRenderer tag_name;

    public SproutMinionModel() {
        textureWidth = 64;
        textureHeight = 64;

        sprout_blossom = new ModelRenderer(this);
        sprout_blossom.setRotationPoint(0.0F, 19.0F, 1.0F);

        hitbox = new ModelRenderer(this);
        hitbox.setRotationPoint(-0.75F, -5.0F, -2.75F);
        sprout_blossom.addChild(hitbox);
        hitbox.cubeList.add(new ModelBox(hitbox, 0, 0, -4.5F, 1.0F, -3.5F, 10, 6, 10, 0.0F));

        head = new ModelRenderer(this);
        head.setRotationPoint(0.0F, 3.0F, 2.0F);
        sprout_blossom.addChild(head);
        head.cubeList.add(new ModelBox(head, 40, 0, -3.0F, -5.075F, -6.0F, 6, 6, 6, 0.0F));
        head.cubeList.add(new ModelBox(head, 39, 1, -1.0F, -3.225F, -7.5F, 2, 3, 1, 0.0F));
        head.cubeList.add(new ModelBox(head, 57, 1, 0.9F, -2.825F, -6.5F, 2, 2, 1, 0.0F));
        head.cubeList.add(new ModelBox(head, 57, 1, -2.9F, -2.825F, -6.5F, 2, 2, 1, 0.0F));
        head.cubeList.add(new ModelBox(head, 45, 12, -3.5F, -4.075F, -8.025F, 7, 2, 2, 0.0F));

        branch = new ModelRenderer(this);
        branch.setRotationPoint(0.1831F, -3.0562F, -4.0F);
        head.addChild(branch);
        branch.cubeList.add(new ModelBox(branch, 0, 52, -7.3169F, -7.8169F, -2.5F, 7, 5, 6, 0.0F));
        branch.cubeList.add(new ModelBox(branch, 0, 43, 0.6831F, -6.8169F, -2.25F, 7, 4, 5, 0.0F));

        head_r1 = new ModelRenderer(this);
        head_r1.setRotationPoint(-2.9937F, -0.0795F, 0.0F);
        branch.addChild(head_r1);
        setRotationAngle(head_r1, 0.0F, 0.0F, -0.7854F);
        head_r1.cubeList.add(new ModelBox(head_r1, 48, 18, -0.3232F, -3.7374F, -0.5F, 1, 4, 1, 0.0F));

        head_r2 = new ModelRenderer(this);
        head_r2.setRotationPoint(2.3169F, -0.0188F, 0.0F);
        branch.addChild(head_r2);
        setRotationAngle(head_r2, 0.0F, 0.0F, 0.7854F);
        head_r2.cubeList.add(new ModelBox(head_r2, 41, 17, -0.5F, -5.0F, -0.5F, 1, 5, 1, 0.0F));

        right_thigh = new ModelRenderer(this);
        right_thigh.setRotationPoint(-4.5F, 1.0F, 0.0F);
        sprout_blossom.addChild(right_thigh);

        legs_r1 = new ModelRenderer(this);
        legs_r1.setRotationPoint(0.0F, 0.925F, 0.0F);
        right_thigh.addChild(legs_r1);
        setRotationAngle(legs_r1, -0.7854F, 0.0F, 0.0F);
        legs_r1.cubeList.add(new ModelBox(legs_r1, 26, 56, -0.5F, -2.0F, -2.0F, 2, 4, 4, 0.0F));

        right_foot = new ModelRenderer(this);
        right_foot.setRotationPoint(0.5F, 2.091F, 0.7448F);
        right_thigh.addChild(right_foot);
        right_foot.cubeList.add(new ModelBox(right_foot, 41, 19, -0.75F, 0.925F, -4.0F, 1, 1, 4, -0.02F));

        left_thigh = new ModelRenderer(this);
        left_thigh.setRotationPoint(4.0F, 1.0F, 0.0F);
        sprout_blossom.addChild(left_thigh);

        legs_r2 = new ModelRenderer(this);
        legs_r2.setRotationPoint(-0.5F, 0.925F, 0.0F);
        left_thigh.addChild(legs_r2);
        setRotationAngle(legs_r2, -0.7854F, 0.0F, 0.0F);
        legs_r2.cubeList.add(new ModelBox(legs_r2, 52, 16, -0.5F, -2.0F, -2.0F, 2, 4, 4, 0.0F));

        left_foot = new ModelRenderer(this);
        left_foot.setRotationPoint(0.0F, 2.091F, 0.7448F);
        left_thigh.addChild(left_foot);
        left_foot.cubeList.add(new ModelBox(left_foot, 30, 19, -0.75F, 0.925F, -4.0F, 1, 1, 4, -0.02F));

        tag_name = new ModelRenderer(this);
        tag_name.setRotationPoint(0.0F, -7.0F, -2.0F);
        sprout_blossom.addChild(tag_name);

    }

    @Override
    public void render(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw,
        float headPitch, float scaleFactor) {
        setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, entity);
        sprout_blossom.render(scaleFactor);
    }

    public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }

    @Override
    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw,
        float headPitch, float scaleFactor, Entity entity) {

        branch.rotateAngleZ = MathHelper.sin((ageInTicks * 0.6f) * 0.4f) * 0.05f;

        sprout_blossom.rotateAngleY = MathHelper.sin(limbSwing * 1.6f) * (limbSwingAmount * 0.4f);
        sprout_blossom.rotateAngleZ = MathHelper.sin(limbSwing * 1.6f) * (limbSwingAmount * 0.3f);
        right_thigh.rotateAngleX = MathHelper.sin(limbSwing * 1.6f) * (limbSwingAmount * 0.9f);
        right_foot.rotateAngleX = MathHelper.sin(limbSwing * 1.6f) * (limbSwingAmount * 0.5f);
        left_thigh.rotateAngleX = -right_thigh.rotateAngleX;
        left_foot.rotateAngleX = -right_foot.rotateAngleX;
    }
}
