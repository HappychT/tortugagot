package got.client.model.thiefArmor;

import got.client.model.GOTModelBiped;
import net.minecraft.client.model.ModelRenderer;

public class GOTModelThiefBoots extends GOTModelBiped {
    private final ModelRenderer LeftLeg;
    private final ModelRenderer RightLeg;

    public GOTModelThiefBoots() {
        this.textureWidth = 128;
        this.textureHeight = 128;

        this.LeftLeg = new ModelRenderer(this);
        this.LeftLeg.setTextureOffset(32, 7).addBox(-2.0F, 7.0F, -2.1F, 4, 5, 4, 0.1F);
        this.LeftLeg.setTextureOffset(48, 23).addBox(-2.0F, 10.0F, -2.1F, 4, 2, 4, 0.25F);

        this.RightLeg = new ModelRenderer(this);
        this.RightLeg.setTextureOffset(32, 23).addBox(-2.0F, 7.0F, -2.1F, 4, 5, 4, 0.1F);
        this.RightLeg.setTextureOffset(48, 35).addBox(-2.0F, 10.0F, -2.1F, 4, 2, 4, 0.25F);

        this.bipedHead.cubeList.clear();
        this.bipedBody.cubeList.clear();
        this.bipedLeftArm.cubeList.clear();
        this.bipedRightArm.cubeList.clear();
        this.bipedLeftLeg.addChild(this.LeftLeg);
        this.bipedRightLeg.addChild(this.RightLeg);
    }
}