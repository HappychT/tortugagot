package got.client.model.armor;

import got.client.model.GOTModelBiped;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;

public class GOTModelArrynBoots extends GOTModelBiped {
    private final ModelRenderer right_leg_boot;
    private final ModelRenderer left_leg_boot;

    public GOTModelArrynBoots() {
        textureWidth = 16;
        textureHeight = 16;

        right_leg_boot = new ModelRenderer(this);
        right_leg_boot.setRotationPoint(0.0F, 0.0F, 0.0F);
        right_leg_boot.cubeList.add(new ModelBox(right_leg_boot, 0, 0, -2.0F, 0.0F, -2.0F, 4, 12, 4, 0.5F));

        left_leg_boot = new ModelRenderer(this);
        left_leg_boot.setRotationPoint(0.0F, 0.0F, 0.0F);
        left_leg_boot.cubeList.add(new ModelBox(left_leg_boot, 0, 0, -2.0F, 0.0F, -2.0F, 4, 12, 4, 0.5F));

        this.bipedRightLeg.addChild(right_leg_boot);
        this.bipedLeftLeg.addChild(left_leg_boot);

        this.bipedHead.cubeList.clear();
        this.bipedBody.cubeList.clear();
        this.bipedRightArm.cubeList.clear();
        this.bipedLeftArm.cubeList.clear();
    }
}
