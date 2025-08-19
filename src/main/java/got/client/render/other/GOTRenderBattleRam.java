package got.client.render.other;

import org.lwjgl.opengl.GL11;

import got.client.model.GOTModelBattleRam;
import got.common.entity.other.GOTEntityBattleRam;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public class GOTRenderBattleRam extends RenderLiving {
    public static ResourceLocation TEXTURE = new ResourceLocation("got:textures/model/battle_ram.png");
    public ModelBase model = new GOTModelBattleRam();

    public GOTRenderBattleRam() {
        super(new GOTModelBattleRam(), 0.5F);
        shadowSize = 1.0f;
    }

    @Override
    public void doRender(Entity p_76986_1_, double p_76986_2_, double p_76986_4_, double p_76986_6_, float p_76986_8_, float p_76986_9_) {
        this.doRender((GOTEntityBattleRam) p_76986_1_, p_76986_2_, p_76986_4_, p_76986_6_, p_76986_8_, p_76986_9_);
    }

    public void doRender(GOTEntityBattleRam entity, double x, double y, double z, float entityYaw, float partialTicks) {
        GL11.glPushMatrix();
        setupTranslation(x, y, z);
        setupRotation(entity, entityYaw);
        bindEntityTexture(entity);
        model.render(entity, partialTicks, 0.0f, 0.0f, 0.0f, 0.0f, 0.0625f);
        GL11.glPopMatrix();
    }

    @Override
    public ResourceLocation getEntityTexture(Entity p_110775_1_) {
        return TEXTURE;
    }

    public void setupRotation(Entity entity, float entityYaw) {
        GL11.glRotatef(180.0F, 0.0f, 0.0f, 1.0f);
    }

    public void setupTranslation(double x, double y, double z) {
        GL11.glTranslatef((float) x, (float) y + 1.5f, (float) z);
    }
}
