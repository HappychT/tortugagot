package got.client.render.other;

import got.client.model.GOTModelHuorn;
import got.common.entity.other.GOTEntityHuornBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

public class GOTRenderHuorn extends RenderLiving {
    private static GOTRandomSkins faceSkins;

    public GOTRenderHuorn() {
        super(new GOTModelHuorn(), 0.0f);
        faceSkins = GOTRandomSkins.loadSkinsList("got:mob/huorn/face");
    }

    @Override
    protected ResourceLocation getEntityTexture(Entity entity) {
        GOTEntityHuornBase huorn = (GOTEntityHuornBase)entity;
        return faceSkins.getRandomSkin(huorn);
    }

    @Override
    public void doRender(EntityLiving entity, double d, double d1, double d2, float f, float f1) {
        GOTEntityHuornBase huorn = (GOTEntityHuornBase)entity;
        if (huorn.ignoringFrustumForRender) {
            huorn.ignoringFrustumForRender = false;
            huorn.ignoreFrustumCheck = false;
        }
        super.doRender(entity, d, d1, d2, f, f1);
        if (Minecraft.isGuiEnabled() && huorn.hiredNPCInfo.getHiringPlayer() == this.renderManager.livingPlayer) {
            GOTNPCRendering.renderHiredIcon(entity, d, d1 + 3.5, d2);
            GOTNPCRendering.renderNPCHealthBar(entity, d, d1 + 3.5, d2);
        }
    }

    @Override
    protected void renderLivingAt(EntityLivingBase entity, double d, double d1, double d2) {
        GOTEntityHuornBase huorn = (GOTEntityHuornBase)entity;
        if (!huorn.isHuornActive()) {
            int i = MathHelper.floor_double(huorn.posX);
            int j = MathHelper.floor_double(huorn.posY);
            int k = MathHelper.floor_double(huorn.posZ);
            d = i + 0.5 - RenderManager.renderPosX;
            d1 = j - RenderManager.renderPosY;
            d2 = k + 0.5 - RenderManager.renderPosZ;
        }
        super.renderLivingAt(entity, d, d1 -= 0.0078125, d2);
        huorn.hurtTime = 0;
    }

    @Override
    protected void rotateCorpse(EntityLivingBase entity, float f, float f1, float f2) {
        GOTEntityHuornBase huorn = (GOTEntityHuornBase)entity;
        if (!huorn.isHuornActive()) {
            f1 = 0.0f;
        }
        super.rotateCorpse(entity, f, f1, f2);
    }

    @Override
    protected float handleRotationFloat(EntityLivingBase entity, float f) {
        return f;
    }
}