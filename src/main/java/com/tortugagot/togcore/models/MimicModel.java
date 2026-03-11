package com.tortugagot.togcore.models;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;

public class MimicModel extends ModelBase {
    private final ModelRenderer body;
    private final ModelRenderer upper_jaw;
    private final ModelRenderer cube_r1;
    private final ModelRenderer cube_r2;

    private static float prevLimbSwing = 0F;

    public MimicModel() {
        textureWidth = 128;
        textureHeight = 128;

        body = new ModelRenderer(this);
        body.setRotationPoint(0.0F, 17.6875F, -0.5F);
        body.cubeList.add(new ModelBox(body, 72, 95, -7.0F, -1.6875F, -6.5F, 14, 6, 14, 0.0F));
        body.cubeList.add(new ModelBox(body, 0, 94, -8.0F, -8.6875F, -7.5F, 0, 6, 16, 0.0F));
        body.cubeList.add(new ModelBox(body, 0, 17, -8.0F, -8.6875F, -7.5F, 16, 6, 0, 0.0F));
        body.cubeList.add(new ModelBox(body, 88, 120, 7.0F, -1.6875F, -8.5F, 2, 6, 2, 0.0F));
        body.cubeList.add(new ModelBox(body, 0, 23, -9.0F, 4.3125F, -8.5F, 18, 2, 2, 0.0F));
        body.cubeList.add(new ModelBox(body, 72, 120, -9.0F, -1.6875F, -8.5F, 2, 6, 2, 0.0F));
        body.cubeList.add(new ModelBox(body, 80, 120, 7.0F, -1.6875F, 7.5F, 2, 6, 2, 0.0F));
        body.cubeList.add(new ModelBox(body, 40, 120, -9.0F, -1.6875F, 7.5F, 2, 6, 2, 0.0F));
        body.cubeList.add(new ModelBox(body, 0, 47, -9.0F, 4.3125F, 7.5F, 18, 2, 2, 0.0F));
        body.cubeList.add(new ModelBox(body, 0, 72, 7.0F, 4.3125F, -6.5F, 2, 2, 14, 0.0F));
        body.cubeList.add(new ModelBox(body, 32, 88, -9.0F, 4.3125F, -6.5F, 2, 2, 14, 0.0F));
        body.cubeList.add(new ModelBox(body, 88, 115, -9.05F, -3.6875F, 7.5F, 18, 2, 2, 0.0F));
        body.cubeList.add(new ModelBox(body, 0, 88, 7.0F, -3.6875F, -6.5F, 2, 2, 14, 0.0F));
        body.cubeList.add(new ModelBox(body, 88, 0, -9.0F, -3.6875F, -8.5F, 18, 2, 2, 0.0F));
        body.cubeList.add(new ModelBox(body, 92, 64, -9.0F, -3.6875F, -6.5F, 2, 2, 14, 0.0F));
        body.cubeList.add(new ModelBox(body, 0, 100, 8.0F, -8.6875F, -7.5F, 0, 6, 16, 0.0F));

        upper_jaw = new ModelRenderer(this);
        upper_jaw.setRotationPoint(0.0F, -2.4738F, 9.1729F);
        body.addChild(upper_jaw);
        setRotationAngle(upper_jaw, 0.5236F, 0.0F, 0.0F);

        cube_r1 = new ModelRenderer(this);
        cube_r1.setRotationPoint(0.0F, -3.9919F, -6.5515F);
        upper_jaw.addChild(cube_r1);
        setRotationAngle(cube_r1, -0.7854F, 0.0F, 0.0F);
        cube_r1.cubeList.add(new ModelBox(cube_r1, 0, 51, -7.0F, -3.4853F, 5.8284F, 14, 2, 2, 0.0F));
        cube_r1.cubeList.add(new ModelBox(cube_r1, 64, 123, -9.0F, -6.4853F, 5.8284F, 2, 3, 2, 0.0F));
        cube_r1.cubeList.add(new ModelBox(cube_r1, 88, 44, 7.0F, -3.4853F, -10.1716F, 2, 2, 18, 0.0F));
        cube_r1.cubeList.add(new ModelBox(cube_r1, 0, 55, -7.0F, -3.4853F, -10.1716F, 14, 2, 2, 0.0F));
        cube_r1.cubeList.add(new ModelBox(cube_r1, 88, 24, -9.0F, -3.4853F, -10.1716F, 2, 2, 18, 0.0F));
        cube_r1.cubeList.add(new ModelBox(cube_r1, 0, 59, -7.0F, -8.4853F, -10.1716F, 14, 2, 2, 0.0F));
        cube_r1.cubeList.add(new ModelBox(cube_r1, 96, 119, -7.0F, -8.4853F, 5.8284F, 14, 2, 2, 0.0F));
        cube_r1.cubeList.add(new ModelBox(cube_r1, 88, 4, -9.0F, -8.4853F, -10.1716F, 2, 2, 18, 0.0F));
        cube_r1.cubeList.add(new ModelBox(cube_r1, 56, 123, 7.0F, -6.4853F, 5.8284F, 2, 3, 2, 0.0F));
        cube_r1.cubeList.add(new ModelBox(cube_r1, 48, 123, -9.0F, -6.4853F, -10.1716F, 2, 3, 2, 0.0F));
        cube_r1.cubeList.add(new ModelBox(cube_r1, 120, 123, 7.0F, -6.4853F, -10.1716F, 2, 3, 2, 0.0F));
        cube_r1.cubeList.add(new ModelBox(cube_r1, 0, 27, 7.0F, -8.4853F, -10.1716F, 2, 2, 18, 0.0F));
        cube_r1.cubeList.add(new ModelBox(cube_r1, 0, 0, -7.0F, -6.4853F, -8.1716F, 14, 3, 14, 0.0F));

        cube_r2 = new ModelRenderer(this);
        cube_r2.setRotationPoint(0.0F, -9.2137F, -12.6729F);
        upper_jaw.addChild(cube_r2);
        setRotationAngle(cube_r2, -0.7854F, 0.0F, 0.0F);
        cube_r2.cubeList.add(new ModelBox(cube_r2, 0, 106, 8.0F, -3.0F, -1.5F, 0, 6, 16, 0.0F));
        cube_r2.cubeList.add(new ModelBox(cube_r2, 22, 39, -8.0F, -3.0F, -1.5F, 16, 6, 0, 0.0F));
        cube_r2.cubeList.add(new ModelBox(cube_r2, 0, 88, -8.0F, -3.0F, -1.5F, 0, 6, 16, 0.0F));

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
        body.rotateAngleX = MathHelper.sin(limbSwing * 1.4f) * limbSwingAmount;
                
        if (limbSwing == prevLimbSwing) {
            upper_jaw.rotateAngleX = (float) Math.toRadians(30);
            upper_jaw.rotateAngleX += MathHelper.sin(ageInTicks * (float) Math.toRadians(6))
                    * (float) Math.toRadians(3);
        } else {
            upper_jaw.rotateAngleX = (float) Math.toRadians(-20);
            upper_jaw.rotateAngleX += MathHelper.sin(ageInTicks * (float) Math.toRadians(6))
                    * (float) Math.toRadians(8);
        }
        prevLimbSwing = limbSwing;
    }
}
