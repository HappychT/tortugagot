package brain.armors;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class KingGuardBody extends ModelBiped {
    private final ModelRenderer left_arm_head_extension_r1;
    private final ModelRenderer right_arm_head_extension_r1;
    private final ModelRenderer cape_template9;
    private final ModelRenderer armorCape9;
    private final ModelRenderer capeMid9;
    private final ModelRenderer capeLow;

    private ModelRenderer leftArmPart;
    private ModelRenderer rightArmPart;

    private float capeWave = 0.0F;
    private float prevCapeWave = 0.0F;

    public KingGuardBody(float scale) {
        super(scale, 0, 128, 128);
        textureWidth = 128;
        textureHeight = 128;

        this.bipedBody = new ModelRenderer(this, 0, 0);
        this.bipedBody.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.bipedBody.addBox(-4.0F, 0.0F, -2.0F, 8, 12, 4, scale);
        this.bipedBody.setTextureOffset(48, 48).addBox(-4.0F, 0.0F, -2.0F, 8, 12, 4, scale + 0.1F);
        this.bipedBody.setTextureOffset(48, 32).addBox(-4.0F, 0.0F, -2.0F, 8, 12, 4, scale + 0.1F);
        this.bipedBody.setTextureOffset(48, 16).addBox(-4.0F, 0.0F, -2.0F, 8, 12, 4, scale + 0.15F);

        this.bipedLeftArm = new ModelRenderer(this);
        this.bipedLeftArm.setRotationPoint(5.0F, 2.0F, 0.0F);

        leftArmPart = new ModelRenderer(this, 32, 96);
        leftArmPart.addBox(-1.0F, -2.0F, -2.0F, 4, 12, 4, scale + 0.15F);
        leftArmPart.setTextureOffset(72, 32).addBox(-1.0F, -2.0F, -2.0F, 4, 12, 4, scale+ 0.15F);
        leftArmPart.setTextureOffset(63, 79).addBox(-1.5F, -2.0F, -2.5F, 5, 12, 5, scale + 0.15F);
        this.bipedLeftArm.addChild(leftArmPart);

        left_arm_head_extension_r1 = new ModelRenderer(this, 89, 38);
        left_arm_head_extension_r1.setRotationPoint(-6.0F, 12.0F, -3.0F);
        this.bipedLeftArm.addChild(left_arm_head_extension_r1);
        setRotationAngle(left_arm_head_extension_r1, 0.0F, 0.0F, 0.0436F);
        left_arm_head_extension_r1.addBox(3.5F, -15.5F, 0.5F, 6, 5, 5, scale + 0.5F);
        left_arm_head_extension_r1.setTextureOffset(89, 16).addBox(3.5F, -15.25F, 0.5F, 6, 5, 5, scale + 0.5F);
        left_arm_head_extension_r1.setTextureOffset(89, 27).addBox(3.5F, -15.5F, 0.5F, 6, 5, 5, scale + 0.51F);

        this.bipedRightArm = new ModelRenderer(this);
        this.bipedRightArm.setRotationPoint(-5.0F, 2.0F, 0.0F);

        rightArmPart = new ModelRenderer(this, 32, 96);
        rightArmPart.mirror = true;
        rightArmPart.addBox(-3.0F, -2.0F, -2.0F, 4, 12, 4, scale + 0.1F);
        rightArmPart.setTextureOffset(72, 32).addBox(-3.0F, -2.0F, -2.0F, 4, 12, 4, scale + 0.1F);
        rightArmPart.setTextureOffset(63, 79).addBox(-3.5F, -2.0F, -2.5F, 5, 12, 5, scale + 0.1F);
        this.bipedRightArm.addChild(rightArmPart);

        right_arm_head_extension_r1 = new ModelRenderer(this, 89, 38);
        right_arm_head_extension_r1.mirror = true;
        right_arm_head_extension_r1.setRotationPoint(7.0F, 12.0F, -1.0F);
        this.bipedRightArm.addChild(right_arm_head_extension_r1);
        setRotationAngle(right_arm_head_extension_r1, 0.0F, 0.0F, -0.0436F);
        right_arm_head_extension_r1.addBox(-10.5F, -15.5F, -1.5F, 6, 5, 5, scale + 0.5F);
        right_arm_head_extension_r1.setTextureOffset(89, 16).addBox(-10.5F, -15.25F, -1.5F, 6, 5, 5, scale + 0.25F);
        right_arm_head_extension_r1.setTextureOffset(89, 27).addBox(-10.5F, -15.5F, -1.5F, 6, 5, 5, scale + 0.51f);

        cape_template9 = new ModelRenderer(this);
        cape_template9.setRotationPoint(0.0F, 1.0F, 2.75F);
        this.bipedBody.addChild(cape_template9);

        armorCape9 = new ModelRenderer(this, 97, 99);
        armorCape9.setRotationPoint(0.0F, -2.0F, 0.0F);
        cape_template9.addChild(armorCape9);
        setRotationAngle(armorCape9, 0.0873F, 0.0F, 0.0F);
        armorCape9.addBox(-6.5F, 1.5F, -0.65F, 13, 12, 1, scale);
        armorCape9.setTextureOffset(83, 92).addBox(-7.0F, 0.5F, -0.5F, 3, 3, 1, scale);
        armorCape9.setTextureOffset(83, 92).addBox(4.0F, 0.5F, -0.5F, 3, 3, 1, scale);

        capeMid9 = new ModelRenderer(this, 97, 114);
        capeMid9.setRotationPoint(21.5F, 13.5F, 0.35F);
        armorCape9.addChild(capeMid9);
        setRotationAngle(capeMid9, 0.0873F, 0.0F, 0.0F);
        capeMid9.addBox(-28.0F, -0.1305F, -0.9914F, 13, 6, 1, scale - 0.01F);

        capeLow = new ModelRenderer(this, 97, 122);
        capeLow.setRotationPoint(-21.0F, 6.0F, -1.0F);
        capeMid9.addChild(capeLow);
        setRotationAngle(capeLow, 0.1309F, 0.0F, 0.0F);
        capeLow.addBox(-7.0F, -0.1271F, 0.0311F, 13, 1, 1, scale);
        capeLow.setTextureOffset(96, 123).addBox(4.0F, 0.8729F, 0.0311F, 2, 2, 1, scale);
        capeLow.setTextureOffset(96, 123).addBox(-7.0F, 0.8729F, 0.0311F, 2, 2, 1, scale);
        capeLow.setTextureOffset(100, 123).addBox(-4.0F, 0.8729F, 0.0311F, 3, 2, 1, scale);
        capeLow.setTextureOffset(111, 122).addBox(3.0F, 0.8729F, 0.0311F, 1, 1, 1, scale);
        capeLow.setTextureOffset(111, 122).addBox(-5.0F, 0.8729F, 0.0311F, 1, 1, 1, scale);
        capeLow.setTextureOffset(115, 124).addBox(-1.0F, 0.8729F, 0.0311F, 1, 1, 1, scale);
        capeLow.setTextureOffset(98, 124).addBox(-7.0F, 2.8729F, 0.0311F, 1, 1, 1, scale);
        capeLow.setTextureOffset(111, 122).addBox(-2.9F, 2.8729F, 0.0311F, 1, 1, 1, scale);
        capeLow.setTextureOffset(111, 122).addBox(1.0F, 2.8729F, 0.0311F, 1, 1, 1, scale);
        capeLow.setTextureOffset(97, 124).addBox(5.0F, 2.8729F, 0.0311F, 1, 1, 1, scale);
        capeLow.setTextureOffset(100, 123).addBox(0.0F, 0.8729F, 0.0311F, 3, 2, 1, scale);
    }

    @Override
    public void render(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        this.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entity);
        this.bipedBody.render(scale);
        this.bipedLeftArm.render(scale);
        this.bipedRightArm.render(scale);
    }

    @Override
    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entity) {
        super.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, entity);


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

        if (cape_template9 != null && armorCape9 != null && capeMid9 != null && capeLow != null) {
            float baseAngle = 0.0873F;
            float walkAngle = (float) Math.toRadians(45.0F);

            float walkSwing = 0.0F;
            float capeRotationX;
            if (isWalking) {
                capeRotationX = walkAngle + walkSwing;
            } else {
                capeRotationX = baseAngle + wave;
            }

            armorCape9.rotateAngleX = capeRotationX;

            float midCapeRotationX;
            if (isWalking) {
                midCapeRotationX = walkAngle + walkSwing * 0.8F + MathHelper.sin(ageInTicks * 0.15F) * 0.02F;
            } else {
                midCapeRotationX = baseAngle + MathHelper.sin(ageInTicks * 0.1F + 0.5F) * 0.03F;
            }
            capeMid9.rotateAngleX = midCapeRotationX;

            float lowCapeRotationX;
            if (isWalking) {
                float lowSwing = MathHelper.sin(limbSwing * 1.2F + 1.0F) * 0.4F * limbSwingAmount * walkIntensity;
                lowCapeRotationX = 0.1309F + lowSwing + MathHelper.sin(ageInTicks * 0.2F) * 0.05F;
            } else {
                lowCapeRotationX = 0.1309F + MathHelper.sin(ageInTicks * 0.15F + 1.0F) * 0.04F;
            }
            capeLow.rotateAngleX = lowCapeRotationX;

            if (entity instanceof EntityPlayer) {
                EntityPlayer player = (EntityPlayer) entity;
                float headYaw = MathHelper.wrapAngleTo180_float(player.rotationYawHead - player.renderYawOffset);
                float influence = isWalking ? 0.0005F : 0.001F;
                armorCape9.rotateAngleY = headYaw * influence;
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