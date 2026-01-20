package brain.armors;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class KingGuardBoots extends ModelBiped {
    public KingGuardBoots(float scale) {
        super(scale, 0, 128, 128);
        textureWidth = 128;
        textureHeight = 128;

        this.bipedRightLeg = new ModelRenderer(this, 80, 64);
        this.bipedRightLeg.setRotationPoint(-1.9F, 12.0F, 0.0F);
        this.bipedRightLeg.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, scale+0.1f );
        this.bipedRightLeg.setTextureOffset(49, 104).addBox(-2.0F, 10.25F, -3.0F, 4, 2, 5, scale+0.1f);

        this.bipedLeftLeg = new ModelRenderer(this, 80, 64);
        this.bipedLeftLeg.mirror = true;
        this.bipedLeftLeg.setRotationPoint(1.9F, 12.0F, 0.0F);
        this.bipedLeftLeg.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, scale +0.1f);
        this.bipedLeftLeg.setTextureOffset(49, 104).addBox(-2.0F, 10.25F, -3.0F, 4, 2, 5, scale +0.1f);
    }

    @Override
    public void render(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        this.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entity);
        this.bipedRightLeg.render(scale);
        this.bipedLeftLeg.render(scale);
    }
}