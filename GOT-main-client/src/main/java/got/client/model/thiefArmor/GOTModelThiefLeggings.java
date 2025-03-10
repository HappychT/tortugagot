package got.client.model.thiefArmor;

import got.client.model.GOTModelBiped;
import net.minecraft.client.model.ModelRenderer;

public class GOTModelThiefLeggings extends GOTModelBiped {
    private final ModelRenderer Body;
    private final ModelRenderer LeftLeg;
    private final ModelRenderer RightLeg;

    public GOTModelThiefLeggings() {
        this.textureWidth = 128;
        this.textureHeight = 128;

        this.Body = new ModelRenderer(this);
        this.Body.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.Body.setTextureOffset(0, 42).addBox(-4.0F, 10.0F, -2.1F, 8, 2, 4, 0.1F);

        this.LeftLeg = new ModelRenderer(this);
        this.LeftLeg.setTextureOffset(32, 0).addBox(-2.0F, 0.0F, -2.1F, 4, 9, 4, 0.1F);
        this.LeftLeg.setTextureOffset(48, 29).addBox(-2.0F, 0.0F, -2.1F, 4, 2, 4, 0.25F);
        this.LeftLeg.setTextureOffset(56, 47).addBox(-2.0F, 4.0F, -2.1F, 4, 4, 2, 0.25F);

        this.RightLeg = new ModelRenderer(this);
        this.RightLeg.setTextureOffset(32, 16).addBox(-2.0F, 0.0F, -2.1F, 4, 9, 4, 0.1F);
        this.RightLeg.setTextureOffset(56, 41).addBox(-2.0F, 0.0F, -2.1F, 4, 2, 4, 0.25F);
        this.RightLeg.setTextureOffset(56, 53).addBox(-2.0F, 4.0F, -2.1F, 4, 4, 2, 0.25F);

        this.bipedHead.cubeList.clear();
        this.bipedLeftArm.cubeList.clear();
        this.bipedRightArm.cubeList.clear();
        this.bipedBody.addChild(this.Body);
        this.bipedLeftLeg.addChild(this.LeftLeg);
        this.bipedRightLeg.addChild(this.RightLeg);
    }
}