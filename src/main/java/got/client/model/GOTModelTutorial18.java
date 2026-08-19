package got.client.model;

import org.lwjgl.opengl.GL11;

import got.client.render.other.GOTGlowingEyes;
import got.common.entity.other.GOTEntityNPC;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class GOTModelTutorial18 extends GOTModelBiped implements GOTGlowingEyes.Model {

    public ModelRenderer bipedLeftArmwear;
    public ModelRenderer bipedRightArmwear;
    public ModelRenderer bipedLeftLegwear;
    public ModelRenderer bipedRightLegwear;
    public ModelRenderer bipedBodyWear;

    public GOTModelTutorial18(float scale) {
        super(scale, 0.0f, 64, 64);
        
        // 1.8 Layout Base Parts
        this.bipedHead = new ModelRenderer(this, 0, 0);
        this.bipedHead.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, scale);
        this.bipedHead.setRotationPoint(0.0F, 0.0F, 0.0F);

        this.bipedHeadwear = new ModelRenderer(this, 32, 0);
        this.bipedHeadwear.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, scale + 0.5F);
        this.bipedHeadwear.setRotationPoint(0.0F, 0.0F, 0.0F);

        this.bipedBody = new ModelRenderer(this, 16, 16);
        this.bipedBody.addBox(-4.0F, 0.0F, -2.0F, 8, 12, 4, scale);
        this.bipedBody.setRotationPoint(0.0F, 0.0F, 0.0F);

        this.bipedRightArm = new ModelRenderer(this, 40, 16);
        this.bipedRightArm.addBox(-3.0F, -2.0F, -2.0F, 4, 12, 4, scale);
        this.bipedRightArm.setRotationPoint(-5.0F, 2.0F, 0.0F);

        this.bipedRightLeg = new ModelRenderer(this, 0, 16);
        this.bipedRightLeg.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, scale);
        this.bipedRightLeg.setRotationPoint(-1.9F, 12.0F, 0.0F);

        // 1.8 specific Left Arm/Leg (not mirrored)
        this.bipedLeftArm = new ModelRenderer(this, 32, 48);
        this.bipedLeftArm.addBox(-1.0F, -2.0F, -2.0F, 4, 12, 4, scale);
        this.bipedLeftArm.setRotationPoint(5.0F, 2.0F, 0.0F);

        this.bipedLeftLeg = new ModelRenderer(this, 16, 48);
        this.bipedLeftLeg.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, scale);
        this.bipedLeftLeg.setRotationPoint(1.9F, 12.0F, 0.0F);

        // 1.8 Overlays
        this.bipedBodyWear = new ModelRenderer(this, 16, 32);
        this.bipedBodyWear.addBox(-4.0F, 0.0F, -2.0F, 8, 12, 4, scale + 0.25F);
        this.bipedBodyWear.setRotationPoint(0.0F, 0.0F, 0.0F);

        this.bipedRightArmwear = new ModelRenderer(this, 40, 32);
        this.bipedRightArmwear.addBox(-3.0F, -2.0F, -2.0F, 4, 12, 4, scale + 0.25F);
        this.bipedRightArmwear.setRotationPoint(-5.0F, 2.0F, 0.0F);

        this.bipedLeftArmwear = new ModelRenderer(this, 48, 48);
        this.bipedLeftArmwear.addBox(-1.0F, -2.0F, -2.0F, 4, 12, 4, scale + 0.25F);
        this.bipedLeftArmwear.setRotationPoint(5.0F, 2.0F, 0.0F);

        this.bipedRightLegwear = new ModelRenderer(this, 0, 32);
        this.bipedRightLegwear.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, scale + 0.25F);
        this.bipedRightLegwear.setRotationPoint(-1.9F, 12.0F, 0.0F);

        this.bipedLeftLegwear = new ModelRenderer(this, 0, 48);
        this.bipedLeftLegwear.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, scale + 0.25F);
        this.bipedLeftLegwear.setRotationPoint(1.9F, 12.0F, 0.0F);
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        this.setRotationAngles(f, f1, f2, f3, f4, f5, entity);

        // Sync overlay rotations
        this.bipedBodyWear.rotateAngleX = this.bipedBody.rotateAngleX;
        this.bipedBodyWear.rotateAngleY = this.bipedBody.rotateAngleY;
        this.bipedBodyWear.rotateAngleZ = this.bipedBody.rotateAngleZ;
        this.bipedBodyWear.rotationPointY = this.bipedBody.rotationPointY;
        this.bipedBodyWear.rotationPointX = this.bipedBody.rotationPointX;
        this.bipedBodyWear.rotationPointZ = this.bipedBody.rotationPointZ;

        this.bipedRightArmwear.rotateAngleX = this.bipedRightArm.rotateAngleX;
        this.bipedRightArmwear.rotateAngleY = this.bipedRightArm.rotateAngleY;
        this.bipedRightArmwear.rotateAngleZ = this.bipedRightArm.rotateAngleZ;
        this.bipedRightArmwear.rotationPointY = this.bipedRightArm.rotationPointY;
        this.bipedRightArmwear.rotationPointX = this.bipedRightArm.rotationPointX;
        this.bipedRightArmwear.rotationPointZ = this.bipedRightArm.rotationPointZ;

        this.bipedLeftArmwear.rotateAngleX = this.bipedLeftArm.rotateAngleX;
        this.bipedLeftArmwear.rotateAngleY = this.bipedLeftArm.rotateAngleY;
        this.bipedLeftArmwear.rotateAngleZ = this.bipedLeftArm.rotateAngleZ;
        this.bipedLeftArmwear.rotationPointY = this.bipedLeftArm.rotationPointY;
        this.bipedLeftArmwear.rotationPointX = this.bipedLeftArm.rotationPointX;
        this.bipedLeftArmwear.rotationPointZ = this.bipedLeftArm.rotationPointZ;

        this.bipedRightLegwear.rotateAngleX = this.bipedRightLeg.rotateAngleX;
        this.bipedRightLegwear.rotateAngleY = this.bipedRightLeg.rotateAngleY;
        this.bipedRightLegwear.rotateAngleZ = this.bipedRightLeg.rotateAngleZ;
        this.bipedRightLegwear.rotationPointY = this.bipedRightLeg.rotationPointY;
        this.bipedRightLegwear.rotationPointZ = this.bipedRightLeg.rotationPointZ;
        this.bipedRightLegwear.rotationPointX = this.bipedRightLeg.rotationPointX;

        this.bipedLeftLegwear.rotateAngleX = this.bipedLeftLeg.rotateAngleX;
        this.bipedLeftLegwear.rotateAngleY = this.bipedLeftLeg.rotateAngleY;
        this.bipedLeftLegwear.rotateAngleZ = this.bipedLeftLeg.rotateAngleZ;
        this.bipedLeftLegwear.rotationPointY = this.bipedLeftLeg.rotationPointY;
        this.bipedLeftLegwear.rotationPointZ = this.bipedLeftLeg.rotationPointZ;
        this.bipedLeftLegwear.rotationPointX = this.bipedLeftLeg.rotationPointX;

        if (this.isChild) {
            float f6 = 2.0F;
            GL11.glPushMatrix();
            GL11.glScalef(1.5F / f6, 1.5F / f6, 1.5F / f6);
            GL11.glTranslatef(0.0F, 16.0F * f5, 0.0F);
            this.bipedHead.render(f5);
            this.bipedHeadwear.render(f5);
            GL11.glPopMatrix();

            GL11.glPushMatrix();
            GL11.glScalef(1.0F / f6, 1.0F / f6, 1.0F / f6);
            GL11.glTranslatef(0.0F, 24.0F * f5, 0.0F);
            this.bipedBody.render(f5);
            this.bipedRightArm.render(f5);
            this.bipedLeftArm.render(f5);
            this.bipedRightLeg.render(f5);
            this.bipedLeftLeg.render(f5);
            this.bipedBodyWear.render(f5);
            this.bipedRightArmwear.render(f5);
            this.bipedLeftArmwear.render(f5);
            this.bipedRightLegwear.render(f5);
            this.bipedLeftLegwear.render(f5);
            GL11.glPopMatrix();
        } else {
            this.bipedHead.render(f5);
            this.bipedHeadwear.render(f5);
            this.bipedBody.render(f5);
            this.bipedRightArm.render(f5);
            this.bipedLeftArm.render(f5);
            this.bipedRightLeg.render(f5);
            this.bipedLeftLeg.render(f5);
            this.bipedBodyWear.render(f5);
            this.bipedRightArmwear.render(f5);
            this.bipedLeftArmwear.render(f5);
            this.bipedRightLegwear.render(f5);
            this.bipedLeftLegwear.render(f5);
        }
    }

    @Override
    public void renderGlowingEyes(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        this.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
        this.bipedHead.render(f5);
    }
}
