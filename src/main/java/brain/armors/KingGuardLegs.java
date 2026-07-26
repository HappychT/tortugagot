package brain.armors;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class KingGuardLegs extends ModelBiped {
    private final ModelRenderer RightLegLayer_r1;
    private final ModelRenderer LeftLegLayer_r1;

    public KingGuardLegs(float scale) {
        super(scale, 0, 128, 128);
        textureWidth = 128;
        textureHeight = 128;

        this.bipedRightLeg = new ModelRenderer(this);
        this.bipedRightLeg.setRotationPoint(-1.9F, 12.0F, 0.0F);

        RightLegLayer_r1 = new ModelRenderer(this, 96, 64);
        RightLegLayer_r1.mirror = true;
        RightLegLayer_r1.setRotationPoint(-0.1F, 12.0F, 0.0F);
        this.bipedRightLeg.addChild(RightLegLayer_r1);
        setRotationAngle(RightLegLayer_r1, 0.0F, 0.0F, 0.0873F);
        RightLegLayer_r1.addBox(-3.4F, -12.0F, -2.5F, 5, 9, 5, scale + 0.09F);
        RightLegLayer_r1.setTextureOffset(32, 82).addBox(-3.4F, -12.0F, -2.5F, 5, 9, 5, scale + 0.05F);

        this.bipedLeftLeg = new ModelRenderer(this);
        this.bipedLeftLeg.setRotationPoint(1.9F, 12.0F, 0.0F);

        LeftLegLayer_r1 = new ModelRenderer(this, 96, 64);
        LeftLegLayer_r1.setRotationPoint(0.1F, 12.0F, 0.0F);
        this.bipedLeftLeg.addChild(LeftLegLayer_r1);
        setRotationAngle(LeftLegLayer_r1, 0.0F, 0.0F, -0.0873F);
        LeftLegLayer_r1.addBox(-1.6F, -12.0F, -2.5F, 5, 9, 5, scale + 0.09F);
        LeftLegLayer_r1.setTextureOffset(32, 82).addBox(-1.6F, -12.0F, -2.5F, 5, 9, 5, scale + 0.05F);
    }

    @Override
    public void render(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        this.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entity);
        this.bipedLeftLeg.render(scale);
        this.bipedRightLeg.render(scale);
    }

    @Override
    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entity) {
        super.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, entity);
    }

    public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}