package com.tortugagot.togcore.models;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;

public class DuckModel extends ModelBase {

    private final ModelRenderer bs_mallard_duck_male;
    private final ModelRenderer right_wing;
    private final ModelRenderer left_wing;
    private final ModelRenderer neck;
    private final ModelRenderer cube_r1;
    private final ModelRenderer head;
    private final ModelRenderer left_foot;
    private final ModelRenderer right_foot;

    public DuckModel() {
        textureWidth = 32;
        textureHeight = 32;

        bs_mallard_duck_male = new ModelRenderer(this);
        bs_mallard_duck_male.setRotationPoint(0.0F, 23.0F, 2.0F);
        bs_mallard_duck_male.cubeList
                .add(new ModelBox(bs_mallard_duck_male, 4, 0, -3.0F, -6.0F, -6.0F, 6, 5, 8, 0.0F));
        bs_mallard_duck_male.cubeList
                .add(new ModelBox(bs_mallard_duck_male, 0, 14, -3.0F, -6.0F, 2.0F, 6, 3, 2, 0.0F));

        right_wing = new ModelRenderer(this);
        right_wing.setRotationPoint(-3.05F, -6.0F, -6.0F);
        bs_mallard_duck_male.addChild(right_wing);
        right_wing.cubeList.add(new ModelBox(right_wing, 0, -8, 0.0F, 0.0F, 0.0F, 0, 5, 8, 0.0F));

        left_wing = new ModelRenderer(this);
        left_wing.setRotationPoint(3.05F, -6.0F, -6.0F);
        bs_mallard_duck_male.addChild(left_wing);
        left_wing.cubeList.add(new ModelBox(left_wing, 0, -8, 0.0F, 0.0F, 0.0F, 0, 5, 8, 0.0F));

        neck = new ModelRenderer(this);
        neck.setRotationPoint(0.0F, -3.9773F, -4.8163F);
        bs_mallard_duck_male.addChild(neck);
        neck.cubeList.add(new ModelBox(neck, 0, 20, -1.475F, -1.4752F, -2.9861F, 3, 3, 3, 0.0F));

        cube_r1 = new ModelRenderer(this);
        cube_r1.setRotationPoint(0.0F, -0.3251F, -0.6991F);
        neck.addChild(cube_r1);
        setRotationAngle(cube_r1, -0.3927F, 0.0F, 0.0F);
        cube_r1.cubeList.add(new ModelBox(cube_r1, 20, 25, -1.475F, -2.1874F, -2.5531F, 3, 2, 3, 0.0F));

        head = new ModelRenderer(this);
        head.setRotationPoint(0.0F, -2.0083F, -0.5324F);
        neck.addChild(head);
        head.cubeList.add(new ModelBox(head, 20, 17, -1.5F, -1.1667F, -1.9167F, 3, 1, 3, 0.0F));
        head.cubeList.add(new ModelBox(head, 22, 21, -1.45F, -3.1667F, -4.9167F, 3, 2, 2, 0.0F));
        head.cubeList.add(new ModelBox(head, 24, 13, -1.45F, -4.1667F, -2.9167F, 3, 3, 1, 0.0F));
        head.cubeList.add(new ModelBox(head, 0, 26, -1.45F, -4.1667F, -1.9167F, 3, 3, 3, 0.0F));

        left_foot = new ModelRenderer(this);
        left_foot.setRotationPoint(2.0F, -1.75F, -2.0F);
        bs_mallard_duck_male.addChild(left_foot);
        left_foot.cubeList.add(new ModelBox(left_foot, 18, 29, -0.5F, -0.25F, 0.0F, 1, 3, 0, 0.0F));
        left_foot.cubeList.add(new ModelBox(left_foot, 18, 29, -0.5F, -0.25F, 0.0F, 1, 3, 0, 0.0F));
        left_foot.cubeList.add(new ModelBox(left_foot, 24, 30, -1.5F, 2.75F, -2.0F, 3, 0, 2, 0.0F));
        left_foot.cubeList.add(new ModelBox(left_foot, 24, 30, -1.5F, 2.75F, -2.0F, 3, 0, 2, 0.0F));

        right_foot = new ModelRenderer(this);
        right_foot.setRotationPoint(-2.0F, -1.75F, -2.0F);
        bs_mallard_duck_male.addChild(right_foot);
        right_foot.cubeList.add(new ModelBox(right_foot, 16, 29, -0.5F, -0.25F, 0.0F, 1, 3, 0, 0.0F));
        right_foot.cubeList.add(new ModelBox(right_foot, 16, 29, -0.5F, -0.25F, 0.0F, 1, 3, 0, 0.0F));
        right_foot.cubeList.add(new ModelBox(right_foot, 18, 30, -1.5F, 2.75F, -2.0F, 3, 0, 2, 0.0F));
        right_foot.cubeList.add(new ModelBox(right_foot, 18, 30, -1.5F, 2.75F, -2.0F, 3, 0, 2, 0.0F));
    
    }

    @Override
    public void render(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw,
        float headPitch, float scaleFactor) {
        setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, entity);
        bs_mallard_duck_male.render(scaleFactor);
    }

    public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }

    @Override
    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw,
        float headPitch, float scaleFactor, Entity entity) {

        bs_mallard_duck_male.rotateAngleY = MathHelper.sin(limbSwing * 2f) * (limbSwingAmount * 0.05f);
        bs_mallard_duck_male.rotateAngleZ = MathHelper.sin(limbSwing * 2f) * (limbSwingAmount * 0.15f);
        right_foot.rotateAngleX = MathHelper.sin(limbSwing * 2f) * (limbSwingAmount * 0.9f);
        left_foot.rotateAngleX = -right_foot.rotateAngleX;
    }
}
