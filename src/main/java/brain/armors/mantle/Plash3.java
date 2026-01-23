package brain.armors.mantle;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;

@SideOnly(Side.CLIENT)
public class Plash3 extends ModelBiped {
    private final ModelRenderer bone2; 
    private final ModelRenderer cube_r1; 
    
    private float capeWave = 0.0F;
    private float prevCapeWave = 0.0F;
    
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

        cube_r1 = new ModelRenderer(this);
        cube_r1.setRotationPoint(5.0F, -1.5F, 3.5F);
        cube_r1.setTextureOffset(0, 0);
        cube_r1.addBox(-9.0F, 0.0F, -6.5F, 18, 22, 6, scale);
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
    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entity) {
        super.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, entity);

        this.bipedHead.rotateAngleX = headPitch * 0.017453292F;
        this.bipedHead.rotateAngleY = netHeadYaw * 0.017453292F;

        this.bone2.rotateAngleX = this.bipedBody.rotateAngleX;
        this.bone2.rotateAngleY = this.bipedBody.rotateAngleY;
        this.bone2.rotateAngleZ = this.bipedBody.rotateAngleZ;
        
        updateCapeAnimation(entity, limbSwing, limbSwingAmount, ageInTicks);
    }

    private void updateCapeAnimation(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks) {
        this.prevCapeWave = this.capeWave;
        
        boolean isWalking = false;
        float walkIntensity = 0.0F;
        
        if (entity instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) entity;
            isWalking = player.onGround && (Math.abs(player.motionX) > 0.05 || Math.abs(player.motionZ) > 0.05);
            
            walkIntensity = Math.min(1.0F, MathHelper.sqrt_double(player.motionX * player.motionX + player.motionZ * player.motionZ) * 10.0F);
        }
        
        float wave = MathHelper.cos(ageInTicks * 0.1F) * 0.05F;
        
        if (cube_r1 != null) {
            float baseAngle = 0.0873F; 
            
            float walkAngle = (float) Math.toRadians(45.0F);
            
            float walkSwing = 0.0F;
            if (isWalking) {
                walkSwing = MathHelper.sin(limbSwing * 0.6662F) * 0.3F * limbSwingAmount * walkIntensity;
            }
            
            float capeRotationX;
            if (isWalking) {
                capeRotationX = walkAngle + walkSwing;
            } else {
                capeRotationX = baseAngle + wave;
            }
            
            cube_r1.rotateAngleX = capeRotationX;
            
            if (entity instanceof EntityPlayer) {
                EntityPlayer player = (EntityPlayer) entity;
                float headYaw = MathHelper.wrapAngleTo180_float(player.rotationYawHead - player.renderYawOffset);
                float influence = isWalking ? 0.0005F : 0.001F;
                cube_r1.rotateAngleY = headYaw * influence;
            }
        }
        
        this.capeWave = wave;
    }

    public float getCapeWave(float partialTicks) {
        return this.prevCapeWave + (this.capeWave - this.prevCapeWave) * partialTicks;
    }

    public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}