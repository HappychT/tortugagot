package got.client.model.customarmor;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;

@SideOnly(Side.CLIENT)
public class CustomArmorBootsModel extends ModelBiped {
    private final ModelRenderer rightBoot;
    private final ModelRenderer leftBoot;

    public CustomArmorBootsModel() {
        super(0.0F);
        textureWidth = 64;
        textureHeight = 64;

        bipedHead = new ModelRenderer(this);
        bipedHeadwear = new ModelRenderer(this);
        bipedBody = new ModelRenderer(this);
        bipedRightArm = new ModelRenderer(this);
        bipedLeftArm = new ModelRenderer(this);

        bipedRightLeg = new ModelRenderer(this);
        bipedRightLeg.setRotationPoint(-1.9F, 12.0F, 0.0F);
        rightBoot = new ModelRenderer(this);
        rightBoot.setRotationPoint(1.9F, 12.0F, 0.0F);
        bipedRightLeg.addChild(rightBoot);
        rightBoot.setTextureOffset(0, 6).addBox(-3.9F, -6.0F, -2.0F, 4, 6, 4, 0.6F);
        rightBoot.setTextureOffset(16, 9).addBox(-3.9F, -1.75F, -3.0F, 4, 2, 5, 0.4F);

        bipedLeftLeg = new ModelRenderer(this);
        bipedLeftLeg.setRotationPoint(1.9F, 12.0F, 0.0F);
        leftBoot = new ModelRenderer(this);
        leftBoot.setRotationPoint(-1.9F, 12.0F, 0.0F);
        bipedLeftLeg.addChild(leftBoot);
        leftBoot.mirror = true;
        leftBoot.setTextureOffset(0, 6).addBox(-0.068F, -6.0F, -2.0F, 4, 6, 4, 0.6F);
        leftBoot.setTextureOffset(16, 9).addBox(-0.1F, -1.75F, -3.0F, 4, 2, 5, 0.4F);
        leftBoot.mirror = false;
    }
}
