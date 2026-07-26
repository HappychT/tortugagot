package brain.armors;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class KingGuardHelmet extends ModelBiped {
    private final ModelRenderer helmet_out_layer_r1;
    private final ModelRenderer helmet_out_layer_r2;

    public KingGuardHelmet(float scale) {
        super(scale, 0, 128, 128);
        textureWidth = 128;
        textureHeight = 128;

        this.bipedHead = new ModelRenderer(this, 1, 33);
        this.bipedHead.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.bipedHead.addBox(-4.5F, -9.0F, -4.5F, 9, 9, 9, scale );
        this.bipedHead.setTextureOffset(1, 14).addBox(-4.5F, -9.0F, -4.5F, 9, 9, 9, scale );
        this.bipedHead.setTextureOffset(1, 52).addBox(-4.5F, -9.0F, -4.5F, 9, 9, 9, scale );
        this.bipedHead.setTextureOffset(92, 82).addBox(-0.5F, -11.25F, -5.0F, 1, 4, 10, scale);

        helmet_out_layer_r1 = new ModelRenderer(this, 92, 82);
        helmet_out_layer_r1.mirror = true;
        helmet_out_layer_r1.setRotationPoint(3.0F, 14.0F, -1.0F);
        this.bipedHead.addChild(helmet_out_layer_r1);
        setRotationAngle(helmet_out_layer_r1, 0.0F, 0.0F, -0.0873F);
        helmet_out_layer_r1.addBox(-4.5F, -25.0F, -4.0F, 1, 4, 10, scale);

        helmet_out_layer_r2 = new ModelRenderer(this, 92, 82);
        helmet_out_layer_r2.setRotationPoint(-3.0F, 14.0F, -1.0F);
        this.bipedHead.addChild(helmet_out_layer_r2);
        setRotationAngle(helmet_out_layer_r2, 0.0F, 0.0F, 0.0873F);
        helmet_out_layer_r2.addBox(3.5F, -25.0F, -4.0F, 1, 4, 10, scale);
    }

    @Override
    public void render(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        this.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entity);
        this.bipedHead.render(scale);
    }

    @Override
    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entity) {
        super.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, entity);

        this.bipedHead.rotateAngleX = headPitch * 0.017453292F;
        this.bipedHead.rotateAngleY = netHeadYaw * 0.017453292F;
    }

    public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}