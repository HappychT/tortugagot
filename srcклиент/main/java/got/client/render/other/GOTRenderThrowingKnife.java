package got.client.render.other;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.common.FMLLog;
import got.common.entity.other.GOTEntityThrowingKnife;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

public class GOTRenderThrowingKnife extends Render {
    @Override
    public void doRender(Entity entity, double d, double d1, double d2, float f, float f1) {
        GOTEntityThrowingKnife knife = (GOTEntityThrowingKnife) entity;
        GL11.glPushMatrix();
        GL11.glTranslatef((float) d, (float) d1, (float) d2);
        if (!knife.inGround) {
            GL11.glTranslatef(0.0f, 0.5f, 0.0f);
        }
        GL11.glRotatef(knife.prevRotationYaw + (knife.rotationYaw - knife.prevRotationYaw) * f1 - 90.0f, 0.0f, 1.0f, 0.0f);
        if (!knife.inGround) {
            GL11.glRotatef(knife.rotationPitch + (knife.inGround ? 0.0f : 45.0f * f1), 0.0f, 0.0f, -1.0f);
        } else {
            GL11.glRotatef(-90.0f, 0.0f, 0.0f, 1.0f);
            GL11.glTranslatef(0.0f, 0.75f, 0.0f);
        }
        GL11.glEnable(32826);
        float f2 = knife.shake - f1;
        if (f2 > 0.0f) {
            float f3 = -MathHelper.sin(f2 * 3.0f) * f2;
            GL11.glRotatef(f3, 0.0f, 0.0f, 1.0f);
        }
        GL11.glRotatef(-135.0f, 0.0f, 0.0f, 1.0f);
        ItemStack knifeItem = knife.getProjectileItem();
        IIcon icon = knifeItem.getIconIndex();
        if (icon == null) {
            FMLLog.severe("Error rendering throwing Knife: no icon for " + knifeItem.toString());
            GL11.glPopMatrix();
            return;
        }
        bindEntityTexture(entity);
        Tessellator tessellator = Tessellator.instance;
        ItemRenderer.renderItemIn2D(tessellator, icon.getMaxU(), icon.getMinV(), icon.getMinU(), icon.getMaxV(), icon.getIconWidth(), icon.getIconHeight(), 0.0625f);
        GL11.glDisable(32826);
        GL11.glPopMatrix();
    }

    @Override
    public ResourceLocation getEntityTexture(Entity entity) {
        return TextureMap.locationItemsTexture;
    }
}