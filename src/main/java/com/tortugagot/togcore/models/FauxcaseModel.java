package com.tortugagot.togcore.models;

import com.tortugagot.togcore.TogCore;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;

public class FauxcaseModel extends ModelBase {

    private final ModelRenderer base;
    private final ModelRenderer cube_r1;
    private final ModelRenderer cube_r2;
    private final ModelRenderer cube_r3;
    private final ModelRenderer cube_r4;
    private final ModelRenderer cube_r5;
    private final ModelRenderer right_handle;
    private final ModelRenderer left_handle;
    private final ModelRenderer lid;
    private final ModelRenderer cube_r6;
    private final ModelRenderer cube_r7;
    private final ModelRenderer right_leg;
    private final ModelRenderer left_leg;
    private final ModelRenderer tag_name;
    private final ModelRenderer tag_level;
    private final ModelRenderer hitbox;

    private static float prevLimbSwing = 0F;

    public FauxcaseModel() {
        textureWidth = 128;
        textureHeight = 128;

        base = new ModelRenderer(this);
        base.setRotationPoint(-1.0555F, 17.0F, 1.8661F);
        setRotationAngle(base, -0.1745F, 0.0F, 0.0F);
        base.cubeList.add(new ModelBox(base, 0, 38, -10.9445F, -7.0F, -10.8661F, 24, 9, 18, 0.0F));
        base.cubeList.add(new ModelBox(base, 0, 103, 10.0555F, -4.0F, 4.1339F, 4, 7, 4, 0.0F));
        base.cubeList.add(new ModelBox(base, 0, 103, -11.9445F, -4.0F, 4.1339F, 4, 7, 4, 0.0F));
        base.cubeList.add(new ModelBox(base, 66, 49, -11.9445F, -8.0F, -11.8661F, 26, 4, 3, 0.0F));
        base.cubeList.add(new ModelBox(base, 100, 74, -9.9445F, -8.5F, -12.8661F, 9, 9, 5, 0.0F));
        base.cubeList.add(new ModelBox(base, 17, 117, -1.9445F, -4.0F, -11.8661F, 6, 7, 3, 0.0F));
        base.cubeList.add(new ModelBox(base, 66, 43, -11.9445F, -8.0F, 6.1339F, 26, 4, 2, 0.0F));
        base.cubeList.add(new ModelBox(base, 94, 93, -11.9445F, -8.0F, -8.8661F, 2, 4, 15, 0.0F));
        base.cubeList.add(new ModelBox(base, 114, 112, -13.9445F, -8.0F, -3.8661F, 2, 2, 5, 0.0F));
        base.cubeList.add(new ModelBox(base, 94, 93, 12.0555F, -8.0F, -8.8661F, 2, 4, 15, 0.0F));
        base.cubeList.add(new ModelBox(base, 114, 112, 14.0555F, -8.0F, -3.8661F, 2, 2, 5, 0.0F));
        base.cubeList.add(new ModelBox(base, 62, 94, -9.4445F, -12.0F, -8.8661F, 0, 5, 15, 0.0F));
        base.cubeList.add(new ModelBox(base, 62, 94, 11.5555F, -12.0F, -8.8661F, 0, 5, 15, 0.0F));

        cube_r1 = new ModelRenderer(this);
        cube_r1.setRotationPoint(13.1109F, -16.0F, -10.0F);
        base.addChild(cube_r1);
        setRotationAngle(cube_r1, 0.0F, 0.7854F, 0.0F);
        cube_r1.cubeList.add(new ModelBox(cube_r1, 43, 101, -3.25F, 11.0F, -1.25F, 4, 8, 2, 0.0F));
        cube_r1.cubeList.add(new ModelBox(cube_r1, 0, 114, 0.75F, 9.0F, -2.25F, 4, 10, 4, 0.0F));

        cube_r2 = new ModelRenderer(this);
        cube_r2.setRotationPoint(-11.0F, -16.0F, -10.0F);
        base.addChild(cube_r2);
        setRotationAngle(cube_r2, 0.0F, -0.7854F, 0.0F);
        cube_r2.cubeList.add(new ModelBox(cube_r2, 43, 101, -0.75F, 11.0F, -1.25F, 4, 8, 2, 0.0F));
        cube_r2.cubeList.add(new ModelBox(cube_r2, 0, 114, -4.75F, 9.0F, -2.25F, 4, 10, 4, 0.0F));

        cube_r3 = new ModelRenderer(this);
        cube_r3.setRotationPoint(1.0555F, -9.7716F, -7.7181F);
        base.addChild(cube_r3);
        setRotationAngle(cube_r3, -0.3927F, 0.0F, 0.0F);
        cube_r3.cubeList.add(new ModelBox(cube_r3, 62, 122, -11.0F, -3.0F, 0.0F, 22, 6, 0, 0.0F));

        cube_r4 = new ModelRenderer(this);
        cube_r4.setRotationPoint(1.0555F, -3.0F, -12.3661F);
        base.addChild(cube_r4);
        setRotationAngle(cube_r4, 0.0F, 0.0F, -0.7854F);
        cube_r4.cubeList.add(new ModelBox(cube_r4, 110, 119, -3.0F, -3.0F, -1.5F, 6, 6, 3, 0.0F));

        cube_r5 = new ModelRenderer(this);
        cube_r5.setRotationPoint(6.0555F, 1.6976F, 4.6136F);
        base.addChild(cube_r5);
        setRotationAngle(cube_r5, -0.3927F, 0.0F, 0.0F);
        cube_r5.cubeList.add(new ModelBox(cube_r5, 24, 66, -12.0F, -8.0F, -11.0F, 14, 4, 10, 0.0F));

        right_handle = new ModelRenderer(this);
        right_handle.setRotationPoint(-12.9445F, -7.0F, -1.3661F);
        base.addChild(right_handle);
        setRotationAngle(right_handle, 0.0F, 0.0F, 0.48F);
        right_handle.cubeList.add(new ModelBox(right_handle, 0, 53, 0.0F, -1.0F, -6.0F, 0, 12, 12, 0.0F));
        right_handle.cubeList.add(new ModelBox(right_handle, 120, 88, -1.0F, 8.5F, -1.0F, 2, 3, 2, 0.0F));

        left_handle = new ModelRenderer(this);
        left_handle.setRotationPoint(15.0555F, -7.0F, -1.3661F);
        base.addChild(left_handle);
        setRotationAngle(left_handle, 0.0F, 0.0F, -0.48F);
        left_handle.cubeList.add(new ModelBox(left_handle, 120, 88, -1.0F, 8.5F, -1.0F, 2, 3, 2, 0.0F));
        left_handle.cubeList.add(new ModelBox(left_handle, 0, 53, 0.0F, -1.0F, -6.0F, 0, 12, 12, 0.0F));

        lid = new ModelRenderer(this);
        lid.setRotationPoint(1.0555F, -8.0F, 8.1339F);
        base.addChild(lid);
        setRotationAngle(lid, -0.9599F, 0.0F, 0.0F);
        lid.cubeList.add(new ModelBox(lid, 23, 100, -10.0F, -1.0F, -1.0F, 8, 2, 2, 0.0F));
        lid.cubeList.add(new ModelBox(lid, 23, 100, 2.0F, -1.0F, -1.0F, 8, 2, 2, 0.0F));
        lid.cubeList.add(new ModelBox(lid, 113, 99, -14.0F, -6.0F, -16.0F, 2, 6, 3, 0.0F));
        lid.cubeList.add(new ModelBox(lid, 113, 90, -14.0F, -6.0F, -7.0F, 2, 6, 3, 0.0F));
        lid.cubeList.add(new ModelBox(lid, 93, 99, -14.0F, -6.0F, -13.0F, 2, 3, 6, 0.0F));
        lid.cubeList.add(new ModelBox(lid, 20, 80, -12.0F, -7.0F, -19.0F, 24, 5, 3, 0.0F));
        lid.cubeList.add(new ModelBox(lid, 25, 110, -7.0F, -8.0F, -20.0F, 4, 6, 1, 0.0F));
        lid.cubeList.add(new ModelBox(lid, 25, 104, -7.0F, -7.0F, -1.0F, 4, 5, 1, 0.0F));
        lid.cubeList.add(new ModelBox(lid, 16, 108, -7.0F, -8.0F, -19.0F, 4, 1, 19, 0.0F));
        lid.cubeList.add(new ModelBox(lid, 62, 65, -12.0F, -7.0F, -4.0F, 24, 5, 3, 0.0F));
        lid.cubeList.add(new ModelBox(lid, 24, 0, -12.0F, -7.0F, -16.0F, 24, 3, 12, 0.0F));
        lid.cubeList.add(new ModelBox(lid, 16, 88, -14.0F, -2.0F, -20.0F, 28, 2, 4, 0.0F));
        lid.cubeList.add(new ModelBox(lid, 62, 114, -11.0F, 0.0F, -18.0F, 23, 8, 0, 0.0F));
        lid.cubeList.add(new ModelBox(lid, 62, 86, -11.5F, -4.0F, -18.0F, 0, 8, 15, 0.0F));
        lid.cubeList.add(new ModelBox(lid, 62, 86, 11.5F, -4.0F, -18.0F, 0, 8, 15, 0.0F));
        lid.cubeList.add(new ModelBox(lid, 10, 94, -14.0F, -2.0F, -4.0F, 28, 2, 4, 0.0F));
        lid.cubeList.add(new ModelBox(lid, 16, 108, 3.0F, -8.0F, -19.0F, 4, 1, 19, 0.0F));
        lid.cubeList.add(new ModelBox(lid, 25, 110, 3.0F, -8.0F, -20.0F, 4, 6, 1, 0.0F));
        lid.cubeList.add(new ModelBox(lid, 25, 104, 3.0F, -7.0F, -1.0F, 4, 5, 1, 0.0F));
        lid.cubeList.add(new ModelBox(lid, 113, 99, 12.0F, -6.0F, -16.0F, 2, 6, 3, 0.0F));
        lid.cubeList.add(new ModelBox(lid, 93, 99, 12.0F, -6.0F, -13.0F, 2, 3, 6, 0.0F));
        lid.cubeList.add(new ModelBox(lid, 113, 90, 12.0F, -6.0F, -7.0F, 2, 6, 3, 0.0F));

        cube_r6 = new ModelRenderer(this);
        cube_r6.setRotationPoint(12.0555F, -8.0F, -18.1339F);
        lid.addChild(cube_r6);
        setRotationAngle(cube_r6, 0.0F, 0.7854F, 0.0F);
        cube_r6.cubeList.add(new ModelBox(cube_r6, 43, 111, 0.75F, -3.0F, -2.25F, 4, 12, 4, 0.0F));
        cube_r6.cubeList.add(new ModelBox(cube_r6, 0, 95, -2.25F, 0.0F, -1.25F, 3, 6, 2, 0.0F));

        cube_r7 = new ModelRenderer(this);
        cube_r7.setRotationPoint(-12.0555F, -8.0F, -18.1339F);
        lid.addChild(cube_r7);
        setRotationAngle(cube_r7, 0.0F, -0.7854F, 0.0F);
        cube_r7.cubeList.add(new ModelBox(cube_r7, 0, 95, -0.75F, 0.0F, -1.25F, 3, 6, 2, 0.0F));
        cube_r7.cubeList.add(new ModelBox(cube_r7, 43, 111, -4.75F, -3.0F, -2.25F, 4, 12, 4, 0.0F));

        right_leg = new ModelRenderer(this);
        right_leg.setRotationPoint(-6.4445F, 0.0F, -0.3661F);
        base.addChild(right_leg);
        setRotationAngle(right_leg, 0.2182F, 0.0F, -0.1309F);
        right_leg.cubeList.add(new ModelBox(right_leg, 0, 77, -2.5F, 0.0F, -2.5F, 5, 7, 5, 0.0F));

        left_leg = new ModelRenderer(this);
        left_leg.setRotationPoint(8.5555F, 0.0F, -0.3661F);
        base.addChild(left_leg);
        setRotationAngle(left_leg, 0.2182F, 0.0F, 0.1309F);
        left_leg.cubeList.add(new ModelBox(left_leg, 0, 77, -2.5F, 0.0F, -2.5F, 5, 7, 5, 0.0F));

        tag_name = new ModelRenderer(this);
        tag_name.setRotationPoint(0.0F, -14.0F, 0.0F);

        tag_level = new ModelRenderer(this);
        tag_level.setRotationPoint(0.0F, 4.0F, 0.0F);
        tag_name.addChild(tag_level);

        hitbox = new ModelRenderer(this);
        hitbox.setRotationPoint(0.0F, 12.0F, 0.0F);
        hitbox.cubeList.add(new ModelBox(hitbox, 0, 24, -12.0F, -12.0F, -12.0F, 24, 24, 24, 0.0F));
    }

    @Override
    public void render(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw,
            float headPitch, float scaleFactor) {
        setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, entity);
        base.render(scaleFactor);
    }

    public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }

    @Override
    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw,
            float headPitch, float scaleFactor, Entity entity) {
        base.rotateAngleY = MathHelper.sin(limbSwing * 0.9f) * (limbSwingAmount * 0.2f);
        base.rotateAngleZ = MathHelper.sin(limbSwing * 0.9f) * (limbSwingAmount * 0.3f);
        right_leg.rotateAngleX = MathHelper.sin(limbSwing * 0.9f) * 0.8f * limbSwingAmount;
        left_leg.rotateAngleX = -right_leg.rotateAngleX;
        right_handle.rotateAngleZ = MathHelper.sin(limbSwing * 0.9f) * (limbSwingAmount * 0.2f);
        left_handle.rotateAngleZ = right_handle.rotateAngleZ;

        if (limbSwing == prevLimbSwing) {
            lid.rotateAngleX = MathHelper.sin((float) Math.toRadians(-10));
            lid.rotateAngleX += MathHelper.sin(ageInTicks * (float) Math.toRadians(5)) * (float) Math.toRadians(5);
        } else {
            lid.rotateAngleX = MathHelper.sin((float) Math.toRadians(-90));
            lid.rotateAngleX += MathHelper.sin(ageInTicks * (float) Math.toRadians(5)) * (float) Math.toRadians(3);
        }
        prevLimbSwing = limbSwing;
    }
}
