package got.client.model.customarmor;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;

@SideOnly(Side.CLIENT)
public class CustomArmorLeggingsModel extends ModelBiped {
    private final ModelRenderer innerBodyLayer;

    public CustomArmorLeggingsModel() {
        super(0.0F);
        textureWidth = 64;
        textureHeight = 64;

        bipedHead = new ModelRenderer(this);
        bipedHeadwear = new ModelRenderer(this);

        bipedBody = new ModelRenderer(this);
        bipedBody.setRotationPoint(0.0F, 0.0F, 0.0F);

        bipedRightArm = new ModelRenderer(this);
        bipedLeftArm = new ModelRenderer(this);

        bipedRightLeg = new ModelRenderer(this, 0, 16);
        bipedRightLeg.setRotationPoint(-1.9F, 12.0F, 0.0F);
        bipedRightLeg.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, 0.55F);

        bipedLeftLeg = new ModelRenderer(this, 0, 16);
        bipedLeftLeg.mirror = true;
        bipedLeftLeg.setRotationPoint(1.9F, 12.0F, 0.0F);
        bipedLeftLeg.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, 0.55F);

        innerBodyLayer = new ModelRenderer(this);
        innerBodyLayer.setRotationPoint(0.0F, 24.0F, 0.0F);
        bipedBody.addChild(innerBodyLayer);
        innerBodyLayer.addBox(-4.0F, -24.0F, -2.0F, 8, 12, 4, 0.56F);
    }
}
