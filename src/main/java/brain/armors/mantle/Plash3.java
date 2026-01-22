package brain.armors.mantle;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

@SideOnly(Side.CLIENT)
public class Plash3 extends ModelBiped {
    private final ModelRenderer bone2; 
    
    public Plash3(float scale) {
        super(scale, 0, 64, 64);
        textureWidth = 64;
        textureHeight = 64;

        this.bipedHead = new ModelRenderer(this, 0, 28);
        this.bipedHead.setRotationPoint(0.0F, 0.0F, 0.0F); 

        this.bipedHead.addBox(-5.0F, -9.0F, -5.0F, 10, 10, 10, scale);

        bone2 = new ModelRenderer(this);
        bone2.setRotationPoint(-5.0F, 0.5F, 0.0F);
        bone2.setTextureOffset(40, 28);
        bone2.addBox(9.75F, -2.0F, -3.0F, 5, 3, 6, scale);
        bone2.setTextureOffset(40, 37);
        bone2.addBox(-4.75F, -2.0F, -3.0F, 5, 3, 6, scale);

      
        ModelRenderer cube_r1 = new ModelRenderer(this);
        cube_r1.setRotationPoint(5.0F, -1.5F, 3.5F);
        cube_r1.setTextureOffset(0, 0);
        cube_r1.addBox(-9.0F, 0.0F, -4.0F, 18, 22, 6, scale);
        setRotationAngle(cube_r1, 0.0873F, 0.0F, 0.0F);
        bone2.addChild(cube_r1);
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
}