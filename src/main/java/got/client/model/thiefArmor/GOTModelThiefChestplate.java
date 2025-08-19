package got.client.model.thiefArmor;

import got.client.model.GOTModelBiped;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;

public class GOTModelThiefChestplate extends GOTModelBiped {
    private final ModelRenderer Body;
    private final ModelRenderer BodyLayer_r1;
    private final ModelRenderer BodyLayer_r2;
    private final ModelRenderer Body_r1;
    private final ModelRenderer Body_r2;
    private final ModelRenderer bone;
    private final ModelRenderer cube_r1;
    private final ModelRenderer cube_r2;
    private final ModelRenderer LeftArm;
    private final ModelRenderer LeftArmLayer_r1;
    private final ModelRenderer RightArm;
    private final ModelRenderer RightArmLayer_r1;

    public GOTModelThiefChestplate() {
        this.textureWidth = 128;
        this.textureHeight = 128;

        this.Body = new ModelRenderer(this);
        this.Body.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.Body.setTextureOffset(0, 32).addBox(-4.0F, 0.0F, -2.0F, 8, 11, 4, 0.1F);
        this.Body.setTextureOffset(16, 48).addBox(-2.5F, 7.7F, -3.0F, 2, 3, 1, 0.25F);

        this.BodyLayer_r1 = new ModelRenderer(this);
        this.BodyLayer_r1.setRotationPoint(-0.55F, 22.0F, 0.0F);
        this.Body.addChild(this.BodyLayer_r1);
        setRotationAngle(this.BodyLayer_r1, 0.0F, 0.0F, 0.0436F);
        this.BodyLayer_r1.cubeList.add(new ModelBox(this.BodyLayer_r1, 24, 32, -4.0F, -15.0F, -2.0F, 8, 3, 4, 0.25F));

        this.BodyLayer_r2 = new ModelRenderer(this);
        this.BodyLayer_r2.setRotationPoint(2.4F, 23.4F, 1.0F);
        this.Body.addChild(this.BodyLayer_r2);
        setRotationAngle(this.BodyLayer_r2, -3.1416F, 0.0F, 3.098F);
        this.BodyLayer_r2.cubeList.add(new ModelBox(this.BodyLayer_r2, 16, 48, -2.0F, -15.0F, -2.0F, 2, 3, 1, 0.25F));

        this.Body_r1 = new ModelRenderer(this);
        this.Body_r1.setRotationPoint(0.0F, 1.0F, 2.2F);
        this.Body.addChild(this.Body_r1);
        setRotationAngle(this.Body_r1, 0.2616F, 0.0113F, -0.0421F);
        this.Body_r1.cubeList.add(new ModelBox(this.Body_r1, 0, 17, 0.0F, -1.0F, 0.0F, 5, 15, 0, 0.0F));

        this.Body_r2 = new ModelRenderer(this);
        this.Body_r2.setRotationPoint(0.0F, 1.0F, 2.2F);
        this.Body.addChild(this.Body_r2);
        setRotationAngle(this.Body_r2, 0.2616F, -0.0113F, 0.0421F);
        this.Body_r2.cubeList.add(new ModelBox(this.Body_r2, 0, 17, -5.0F, -1.0F, 0.0F, 5, 15, 0, 0.0F));

        this.bone = new ModelRenderer(this);
        this.bone.setRotationPoint(4.0F, 9.0F, -4.25F);
        this.Body.addChild(this.bone);
        setRotationAngle(this.bone, -2.3787F, 0.2482F, 0.054F);
        this.bone.setTextureOffset(64, 0).addBox(0.0F, -14.0F, -2.0F, 0, 9, 3, 0.0F);
        this.bone.setTextureOffset(70, 0).addBox(-0.5F, -5.1F, -1.25F, 1, 5, 2, 0.0F);
        this.bone.setTextureOffset(64, 12).addBox(-0.25F, -3.0F, -1.5F, 1, 1, 2, 0.0F);
        this.bone.setTextureOffset(76, 0).addBox(-0.25F, -5.0F, -1.5F, 1, 1, 2, 0.0F);
        this.bone.setTextureOffset(76, 3).addBox(-0.75F, -5.0F, -1.5F, 1, 1, 2, 0.0F);
        this.bone.setTextureOffset(70, 13).addBox(-0.75F, -3.0F, -1.5F, 1, 1, 2, 0.0F);
        this.bone.setTextureOffset(76, 13).addBox(-0.75F, -1.0F, -1.5F, 1, 1, 2, 0.0F);
        this.bone.setTextureOffset(78, 6).addBox(-0.25F, -1.0F, -1.5F, 1, 1, 2, 0.0F);

        this.cube_r1 = new ModelRenderer(this);
        this.cube_r1.setRotationPoint(0.0F, -5.7F, -0.7F);
        this.bone.addChild(this.cube_r1);
        setRotationAngle(this.cube_r1, 0.5236F, 0.0F, 0.0F);
        this.cube_r1.setTextureOffset(70, 10).addBox(-1.0F, -0.0912F, -2.0419F, 2, 1, 2, 0.0F);

        this.cube_r2 = new ModelRenderer(this);
        this.cube_r2.setRotationPoint(0.0F, 0.0F, 1.3F);
        this.bone.addChild(this.cube_r2);
        setRotationAngle(this.cube_r2, 0.1745F, 0.0F, 0.0F);
        this.cube_r2.setTextureOffset(70, 7).addBox(-1.0F, -6.0F, -1.0F, 2, 1, 2, 0.0F);

        this.LeftArm = new ModelRenderer(this);
        this.LeftArm.setTextureOffset(0, 48).addBox(-1.0F, 3.75F, -2.0F, 4, 5, 4, 0.25F);
        this.LeftArm.setTextureOffset(24, 46).addBox(-1.0F, -1.0F, -2.0F, 4, 11, 4, 0.1F);

        this.LeftArmLayer_r1 = new ModelRenderer(this);
        this.LeftArmLayer_r1.setRotationPoint(0.75F, 3.75F, 0.0F);
        this.LeftArm.addChild(this.LeftArmLayer_r1);
        setRotationAngle(this.LeftArmLayer_r1, 0.0F, 0.0F, 0.3491F);
        this.LeftArmLayer_r1.setTextureOffset(48, 9).addBox(-3.0F, -6.0F, -2.0F, 4, 3, 4, 0.25F);

        this.RightArm = new ModelRenderer(this);
        this.RightArm.setTextureOffset(48, 0).addBox(-3.0F, 3.75F, -2.0F, 4, 5, 4, 0.25F);
        this.RightArm.setTextureOffset(40, 46).addBox(-3.0F, -1.0F, -2.0F, 4, 11, 4, 0.1F);

        this.RightArmLayer_r1 = new ModelRenderer(this);
        this.RightArmLayer_r1.setRotationPoint(-0.75F, 3.75F, 0.0F);
        this.RightArm.addChild(this.RightArmLayer_r1);
        setRotationAngle(this.RightArmLayer_r1, 0.0F, 0.0F, -0.3491F);
        this.RightArmLayer_r1.setTextureOffset(48, 16).addBox(-1.0F, -6.0F, -2.0F, 4, 3, 4, 0.25F);

        this.bipedHead.cubeList.clear();
        this.bipedBody.addChild(this.Body);
        this.bipedLeftArm.addChild(this.LeftArm);
        this.bipedRightArm.addChild(this.RightArm);
        this.bipedLeftLeg.cubeList.clear();
        this.bipedRightLeg.cubeList.clear();
    }

    public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}