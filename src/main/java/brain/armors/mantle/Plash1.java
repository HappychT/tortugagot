package brain.armors.mantle;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;

@SideOnly(Side.CLIENT)
public class Plash1 extends ModelBiped {
    private final ModelRenderer bone;
    private final ModelRenderer cube_r1;
    private final ModelRenderer cube_r2;
    private final ModelRenderer cube_r3;
    private final ModelRenderer cube_r4;
    private final ModelRenderer cube_r5;
    private final ModelRenderer cube_r6;


    private float capeWave = 0.0F;
    private float prevCapeWave = 0.0F;

    public Plash1() {
        textureWidth = 128;
        textureHeight = 128;

        bone = new ModelRenderer(this);
        bone.setRotationPoint(4.0F, 0.4F, 1.7F);
        bone.cubeList.add(new ModelBox(bone, 48, 2, 0.0F, -2.0F, -2.0F, 1, 2, 4, 0.0F));
        bone.cubeList.add(new ModelBox(bone, 32, 53, -9.0F, -2.0F, -2.0F, 1, 2, 4, 0.0F));

        cube_r1 = new ModelRenderer(this);
        cube_r1.setRotationPoint(-11.0F, 0.6F, -4.1F);
        bone.addChild(cube_r1);
        setRotationAngle(cube_r1, -0.2618F, 0.0873F, -0.3491F);
        cube_r1.cubeList.add(new ModelBox(cube_r1, 120, 4, -2.0F, -2.0F, -1.0F, 4, 3, 0, 0.0F));

        cube_r2 = new ModelRenderer(this);
        cube_r2.setRotationPoint(3.0F, 0.6F, -4.1F);
        bone.addChild(cube_r2);
        setRotationAngle(cube_r2, -0.2618F, -0.0873F, 0.3491F);
        cube_r2.cubeList.add(new ModelBox(cube_r2, 120, 0, -2.0F, -2.0F, -1.0F, 4, 3, 0, 0.0F));

        cube_r3 = new ModelRenderer(this);
        cube_r3.setRotationPoint(-4.0F, 0.85F, 0.8F);
        bone.addChild(cube_r3);
        setRotationAngle(cube_r3, -0.3927F, 0.0F, 0.0F);
        cube_r3.cubeList.add(new ModelBox(cube_r3, 48, 16, -5.0F, -3.0F, 0.0F, 10, 3, 1, 0.0F));

        cube_r4 = new ModelRenderer(this);
        cube_r4.setRotationPoint(-10.5F, 1.1F, -2.4F);
        bone.addChild(cube_r4);
        setRotationAngle(cube_r4, -0.2618F, 0.0873F, -0.3491F);
        cube_r4.cubeList.add(new ModelBox(cube_r4, 26, 43, -3.5F, -2.5F, -2.5F, 7, 4, 6, 0.0F));

        cube_r5 = new ModelRenderer(this);
        cube_r5.setRotationPoint(2.5F, 1.1F, -2.4F);
        bone.addChild(cube_r5);
        setRotationAngle(cube_r5, -0.2618F, -0.0873F, 0.3491F);
        cube_r5.cubeList.add(new ModelBox(cube_r5, 0, 43, -3.5F, -2.5F, -2.5F, 7, 4, 6, 0.0F));

        cube_r6 = new ModelRenderer(this);
        cube_r6.setRotationPoint(-4.0F, 0.6F, -0.45F);
        bone.addChild(cube_r6);
        setRotationAngle(cube_r6, 0.2182F, 0.0F, 0.0F);
        cube_r6.cubeList.add(new ModelBox(cube_r6, 0, 0, -9.0F, 0.0F, -4.0F, 18, 21, 6, 0.0F));
    }

    @Override
    public void render(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        this.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entity);
        GL11.glPushMatrix();
        GL11.glTranslated(0, -0.0418f, 0);
        this.bone.render(0.0618f);
        GL11.glPopMatrix();
    }

    @Override
    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks,
                                  float netHeadYaw, float headPitch, float scaleFactor, Entity entity) {
        super.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, entity);

        this.bone.rotateAngleX = this.bipedBody.rotateAngleX;
        this.bone.rotateAngleY = this.bipedBody.rotateAngleY;
        this.bone.rotateAngleZ = this.bipedBody.rotateAngleZ;
        
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
        
        if (cube_r6 != null) {
            float baseAngle = 0.2182F; 
            
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
            
            cube_r6.rotateAngleX = capeRotationX;
            
            if (entity instanceof EntityPlayer) {
                EntityPlayer player = (EntityPlayer) entity;
                float headYaw = MathHelper.wrapAngleTo180_float(player.rotationYawHead - player.renderYawOffset);
                float influence = isWalking ? 0.0005F : 0.001F;
                cube_r6.rotateAngleY = headYaw * influence;
            }
        }
        
        this.capeWave = wave;
    }

    public float getCapeWave(float partialTicks) {
        return this.prevCapeWave + (this.capeWave - this.prevCapeWave) * partialTicks;
    }

    public void syncWithBody(ModelBiped mainModel) {
        if (mainModel != null) {
            this.bipedBody.rotateAngleX = mainModel.bipedBody.rotateAngleX;
            this.bipedBody.rotateAngleY = mainModel.bipedBody.rotateAngleY;
            this.bipedBody.rotateAngleZ = mainModel.bipedBody.rotateAngleZ;
        }
    }

    public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}