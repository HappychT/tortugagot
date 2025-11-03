package got.client.model.armor;

import got.client.model.GOTModelBiped;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;

public class GOTModelArrynHelmet extends GOTModelBiped {
    private final ModelRenderer helmet;

    public GOTModelArrynHelmet() {
        textureWidth = 64;
        textureHeight = 64;

        helmet = new ModelRenderer(this);
        helmet.setRotationPoint(0.0F, 0.0F, 0.0F);
        helmet.cubeList.add(new ModelBox(helmet, 0, 0, -4.0F, -8.0F, -4.0F, 8, 8, 8, 0.3F));
        helmet.cubeList.add(new ModelBox(helmet, 0, 16, -4.0F, -8.0F, -4.0F, 8, 8, 8, 0.6F));
        helmet.cubeList.add(new ModelBox(helmet, 0, 31, -4.0F, -16.0F, -2.0F, 8, 12, 6, 0.8F));

        this.bipedHead.addChild(helmet);

        this.bipedBody.cubeList.clear();
        this.bipedRightArm.cubeList.clear();
        this.bipedLeftArm.cubeList.clear();
        this.bipedRightLeg.cubeList.clear();
        this.bipedLeftLeg.cubeList.clear();
    }
}
