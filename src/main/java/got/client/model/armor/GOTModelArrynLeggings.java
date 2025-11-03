package got.client.model.armor;

import got.client.model.GOTModelBiped;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;

public class GOTModelArrynLeggings extends GOTModelBiped {
    private final ModelRenderer right_leg;
    private final ModelRenderer left_leg;

    public GOTModelArrynLeggings() {
        textureWidth = 32;
        textureHeight = 32;

        right_leg = new ModelRenderer(this);
        right_leg.setRotationPoint(0.0F, 0.0F, 0.0F);
        right_leg.cubeList.add(new ModelBox(right_leg, 0, 0, -2.0F, 0.0F, -2.0F, 4, 12, 4, 0.3F));

        ModelRenderer right_skirt_r1 = new ModelRenderer(this);
        right_skirt_r1.setRotationPoint(-6.1F, -1.0F, 8.0F);
        right_leg.addChild(right_skirt_r1);
        setRotationAngle(right_skirt_r1, 0.0F, 0.0F, 0.0873F);
        right_skirt_r1.cubeList.add(new ModelBox(right_skirt_r1, 0, 16, -3.9F, 0.0F, -2.0F, 3, 7, 4, 0.8F));

        left_leg = new ModelRenderer(this);
        left_leg.setRotationPoint(0.0F, 0.0F, 0.0F);
        left_leg.cubeList.add(new ModelBox(left_leg, 0, 0, -2.0F, 0.0F, -2.0F, 4, 12, 4, 0.3F));

        ModelRenderer left_skirt_r1 = new ModelRenderer(this);
        left_skirt_r1.setRotationPoint(-9.9F, -1.0F, 8.0F);
        left_leg.addChild(left_skirt_r1);
        setRotationAngle(left_skirt_r1, 0.0F, 0.0F, -0.0873F);
        left_skirt_r1.cubeList.add(new ModelBox(left_skirt_r1, 0, 16, 0.9F, 0.0F, -2.0F, 3, 7, 4, 0.8F));

        this.bipedRightLeg.addChild(right_leg);
        this.bipedLeftLeg.addChild(left_leg);

        this.bipedHead.cubeList.clear();
        this.bipedBody.cubeList.clear();
        this.bipedRightArm.cubeList.clear();
        this.bipedLeftArm.cubeList.clear();
    }

    public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}

