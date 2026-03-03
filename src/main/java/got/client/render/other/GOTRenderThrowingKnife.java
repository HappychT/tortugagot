package got.client.render.other;

import org.lwjgl.opengl.GL11;

import got.common.entity.other.GOTEntityThrowingKnife;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

/**
 * Renders throwing knife projectile as a textured quad (item icon).
 * Draws quad manually so it always renders in 1.7.10.
 */
public class GOTRenderThrowingKnife extends Render {

	private static final float ICON_SCALE = 0.5f;

	@Override
	public void doRender(Entity entity, double x, double y, double z, float f, float partialTicks) {
		GOTEntityThrowingKnife knife = (GOTEntityThrowingKnife) entity;
		ItemStack stack = knife.getProjectileItem();
		if (stack == null || stack.getItem() == null) {
			return;
		}
		IIcon icon = stack.getIconIndex();
		if (icon == null) {
			icon = stack.getItem().getIconFromDamage(stack.getItemDamage());
		}
		if (icon == null) {
			return;
		}

		float minU = icon.getMinU();
		float maxU = icon.getMaxU();
		float minV = icon.getMinV();
		float maxV = icon.getMaxV();
		float s = ICON_SCALE * 0.5f;

		float yaw = knife.prevRotationYaw + (knife.rotationYaw - knife.prevRotationYaw) * partialTicks;
		float pitch = knife.prevRotationPitch + (knife.rotationPitch - knife.prevRotationPitch) * partialTicks;

		GL11.glPushMatrix();
		GL11.glTranslatef((float) x, (float) y, (float) z);
		if (knife.inGround) {
			GL11.glTranslatef(0.0f, 0.22f, 0.0f);
		} else {
			GL11.glTranslatef(0.0f, 0.25f, 0.0f);
		}
		GL11.glRotatef(yaw - 90.0f, 0.0f, 1.0f, 0.0f);
		GL11.glRotatef(pitch, 0.0f, 0.0f, -1.0f);
		if (knife.inGround) {
			GL11.glRotatef(180.0f, 1.0f, 0.0f, 0.0f);
			GL11.glTranslatef(0.0f, 0.08f, 0.0f);
		}
		GL11.glEnable(32826);
		float shake = knife.shake - partialTicks;
		if (shake > 0.0f) {
			GL11.glRotatef(-MathHelper.sin(shake * 3.0f) * shake, 0.0f, 0.0f, 1.0f);
		}
		GL11.glRotatef(-135.0f, 0.0f, 0.0f, 1.0f);
		GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);

		GL11.glDisable(2884); // GL_CULL_FACE — видно с обеих сторон
		bindTexture(TextureMap.locationItemsTexture);
		Tessellator tess = Tessellator.instance;
		tess.setNormal(0.0f, 0.0f, 1.0f);
		tess.startDrawingQuads();
		tess.addVertexWithUV(-s, -s, 0.0, minU, maxV);
		tess.addVertexWithUV( s, -s, 0.0, maxU, maxV);
		tess.addVertexWithUV( s,  s, 0.0, maxU, minV);
		tess.addVertexWithUV(-s,  s, 0.0, minU, minV);
		tess.draw();
		GL11.glEnable(2884);

		GL11.glDisable(32826);
		GL11.glPopMatrix();
	}

	@Override
	public ResourceLocation getEntityTexture(Entity entity) {
		return TextureMap.locationItemsTexture;
	}
}
