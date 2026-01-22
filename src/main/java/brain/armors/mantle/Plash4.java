package brain.armors.mantle;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

@SideOnly(Side.CLIENT)
public class Plash4 extends ModelBiped {
    private final ModelRenderer bone2; 

    public Plash4(float scale) {
        super(scale, 0, 64, 64);
        textureWidth = 64;
        textureHeight = 64;

        this.bipedHead = new ModelRenderer(this, 0, 41); 
        this.bipedHead.setRotationPoint(0.0F, 0.0F, 0.0F); 


        this.bipedHead.addBox(-5.0F, -9.0F, -5.0F, 10, 9, 10, scale);


        bone2 = new ModelRenderer(this);
        bone2.setRotationPoint(3.0F, 0.0F, -3.0F);
        bone2.setTextureOffset(0, 0);
        bone2.addBox(-12.0F, -0.55F, -1.25F, 18, 19, 9, 0.1f);

    }

    @Override
    public void render(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {

        this.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entity);
        this.bipedHead.render(0.05818f);
        bone2.render(0.05818f);
    }

    @Override
    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks,
                                  float netHeadYaw, float headPitch, float scaleFactor, Entity entity) {
        super.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, entity);

        this.bipedHead.rotateAngleX = headPitch * 0.017453292F;
        this.bipedHead.rotateAngleY = netHeadYaw * 0.017453292F;

        this.bone2.rotateAngleX = this.bipedBody.rotateAngleX;
        this.bone2.rotateAngleY = this.bipedBody.rotateAngleY;
        this.bone2.rotateAngleZ = this.bipedBody.rotateAngleZ;
    }

    public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }

    public void syncWithBody(ModelBiped mainModel) {
        if (mainModel != null) {
            this.bipedBody.rotateAngleX = mainModel.bipedBody.rotateAngleX;
            this.bipedBody.rotateAngleY = mainModel.bipedBody.rotateAngleY;
            this.bipedBody.rotateAngleZ = mainModel.bipedBody.rotateAngleZ;
        }
    }
}