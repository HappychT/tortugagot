package got.client.gui.utils;

import java.net.URI;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;


public class GuiApi {
    private static final List<String> tempList = new ArrayList<>();
    private static final List<String> tempSplitted = new ArrayList<>();
    
	public static void drawTexturedQuadFit(ResourceLocation loc, double x, double y, double width, double height) {
		Minecraft.getMinecraft().getTextureManager().bindTexture(loc);
		Tessellator tessellator = Tessellator.instance;
		
		GL11.glPushMatrix();
		tessellator.startDrawingQuads();
		tessellator.addVertexWithUV(x + width, y + height, 0.0, 1.0, 1.0);
		tessellator.addVertexWithUV(x + width, y, 0.0, 1.0, 0.0);
		tessellator.addVertexWithUV(x, y, 0.0, 0.0, 0.0);
		tessellator.addVertexWithUV(x, y + height, 0.0, 0.0, 1.0);
		tessellator.draw();
		GL11.glPopMatrix();
	}
	
	public static void drawTexturedQuadFitBLEND(ResourceLocation loc, double x, double y, double width, double height) {
		Minecraft.getMinecraft().getTextureManager().bindTexture(loc);
		Tessellator tessellator = Tessellator.instance;
		
		GL11.glPushMatrix();
		GL11.glEnable(GL11.GL_BLEND);
		tessellator.startDrawingQuads();
		tessellator.addVertexWithUV(x + width, y + height, 0.0, 1.0, 1.0);
		tessellator.addVertexWithUV(x + width, y, 0.0, 1.0, 0.0);
		tessellator.addVertexWithUV(x, y, 0.0, 0.0, 0.0);
		tessellator.addVertexWithUV(x, y + height, 0.0, 0.0, 1.0);
		tessellator.draw();
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glPopMatrix();
	}

	public static void drawTexturedModalRect(float posX, float posY, int p_73729_3_, int p_73729_4_, int p_73729_5_, int p_73729_6_) {
		float f = 0.00390625F;
		float f1 = 0.00390625F;
		Tessellator tessellator = Tessellator.instance;
		tessellator.startDrawingQuads();
		tessellator.addVertexWithUV((posX + 0.0F), (posY + p_73729_6_), 0.0D, ((p_73729_3_ + 0) * f), ((p_73729_4_ + p_73729_6_) * f1));
		tessellator.addVertexWithUV((posX + p_73729_5_), (posY + p_73729_6_), 0.0D, ((p_73729_3_ + p_73729_5_) * f), ((p_73729_4_ + p_73729_6_) * f1));
		tessellator.addVertexWithUV((posX + p_73729_5_), (posY + 0.0F), 0.0D, ((p_73729_3_ + p_73729_5_) * f), ((p_73729_4_ + 0) * f1));
		tessellator.addVertexWithUV((posX + 0.0F), (posY + 0.0F), 0.0D, ((p_73729_3_ + 0) * f), ((p_73729_4_ + 0) * f1));
		tessellator.draw();
	}

    public static void glScissor(int posX, int posY, int endX, int endY, boolean test){
        Minecraft mc = Minecraft.getMinecraft();
        ScaledResolution resolution = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
        int scale = resolution.getScaleFactor();

        int scissorWidth = endX * scale;
        int scissorHeight = endY * scale;
        int scissorX = posX * scale;
        int scissorY = mc.displayHeight - scissorHeight - (posY * scale);
  
        GL11.glScissor(scissorX, scissorY, scissorWidth, scissorHeight);
        if(test) {
        	GuiApi.drawRect(posX, posY, endX, endY, 0xFF123456);
        }
    }
    
    public static void go(String url) {
        try {
            Class<?> throwable = Class.forName("java.awt.Desktop");
            Object object = throwable.getMethod("getDesktop", new Class[0]).invoke(null);
            throwable.getMethod("browse", new Class[]{URI.class}).invoke(object, new URI(url));
        } catch (Throwable var3) {
            var3.printStackTrace();
        }

    }
    
   
    
    public static void renderToolTip(GuiScreen screen, ItemStack p_146285_1_, int p_146285_2_, int p_146285_3_)
    {
        List list = p_146285_1_.getTooltip(Minecraft.getMinecraft().thePlayer, Minecraft.getMinecraft().gameSettings.advancedItemTooltips);

        for (int k = 0; k < list.size(); ++k)
        {
            if (k == 0)
            {
                list.set(k, p_146285_1_.getRarity().rarityColor + (String)list.get(k));
            }
            else
            {
                list.set(k, EnumChatFormatting.GRAY + (String)list.get(k));
            }
        }

        FontRenderer font = p_146285_1_.getItem().getFontRenderer(p_146285_1_);
        drawHoveringText(screen, list, p_146285_2_, p_146285_3_,  Minecraft.getMinecraft().fontRenderer);
    }

	public static void drawHoveringText(GuiScreen screen, List p_146283_1_, int p_146283_2_, int p_146283_3_, FontRenderer font) {
		if (!p_146283_1_.isEmpty()) {
			GL11.glPushMatrix();
			int k = 0;
			Iterator iterator = p_146283_1_.iterator();

			while (iterator.hasNext()) {
				String s = (String) iterator.next();
				int l = font.getStringWidth(s);

				if (l > k) {
					k = l;
				}
			}

			int j2 = p_146283_2_ + 12;
			int k2 = p_146283_3_ - 12;
			int i1 = 8;

			if (p_146283_1_.size() > 1) {
				i1 += 2 + (p_146283_1_.size() - 1) * 10;
			}

			if (j2 + k > screen.width) {
				j2 -= 28 + k;
			}

			if (k2 + i1 + 6 > screen.height) {
				k2 = screen.height - i1 - 6;
			}

			
			int j1 = -267386864;
			drawGradientRect(j2 - 3, k2 - 4, j2 + k + 3, k2 - 3, j1, j1);
			drawGradientRect(j2 - 3, k2 + i1 + 3, j2 + k + 3, k2 + i1 + 4, j1, j1);
			drawGradientRect(j2 - 3, k2 - 3, j2 + k + 3, k2 + i1 + 3, j1, j1);
			drawGradientRect(j2 - 4, k2 - 3, j2 - 3, k2 + i1 + 3, j1, j1);
			drawGradientRect(j2 + k + 3, k2 - 3, j2 + k + 4, k2 + i1 + 3, j1, j1);
			int k1 = 1347420415;
			int l1 = (k1 & 16711422) >> 1 | k1 & -16777216;
			drawGradientRect(j2 - 3, k2 - 3 + 1, j2 - 3 + 1, k2 + i1 + 3 - 1, k1, l1);
			drawGradientRect(j2 + k + 2, k2 - 3 + 1, j2 + k + 3, k2 + i1 + 3 - 1, k1, l1);
			drawGradientRect(j2 - 3, k2 - 3, j2 + k + 3, k2 - 3 + 1, k1, k1);
			drawGradientRect(j2 - 3, k2 + i1 + 2, j2 + k + 3, k2 + i1 + 3, l1, l1);

			for (int i2 = 0; i2 < p_146283_1_.size(); ++i2) {
				String s1 = (String) p_146283_1_.get(i2);
				font.drawStringWithShadow(s1, j2, k2, -1);

				if (i2 == 0) {
					k2 += 2;
				}

				k2 += 10;
			}
			GL11.glColor4f(1, 1, 1, 1);
			GL11.glPopMatrix();
		}
    }

    public static void drawGradientRect(int p_73733_1_, int p_73733_2_, int p_73733_3_, int p_73733_4_, int p_73733_5_, int p_73733_6_)
    {
        float f = (float)(p_73733_5_ >> 24 & 255) / 255.0F;
        float f1 = (float)(p_73733_5_ >> 16 & 255) / 255.0F;
        float f2 = (float)(p_73733_5_ >> 8 & 255) / 255.0F;
        float f3 = (float)(p_73733_5_ & 255) / 255.0F;
        float f4 = (float)(p_73733_6_ >> 24 & 255) / 255.0F;
        float f5 = (float)(p_73733_6_ >> 16 & 255) / 255.0F;
        float f6 = (float)(p_73733_6_ >> 8 & 255) / 255.0F;
        float f7 = (float)(p_73733_6_ & 255) / 255.0F;
        GL11.glPushMatrix();
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        GL11.glShadeModel(GL11.GL_SMOOTH);
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.setColorRGBA_F(f1, f2, f3, f);
        tessellator.addVertex((double)p_73733_3_, (double)p_73733_2_, 0);
        tessellator.addVertex((double)p_73733_1_, (double)p_73733_2_, 0);
        tessellator.setColorRGBA_F(f5, f6, f7, f4);
        tessellator.addVertex((double)p_73733_1_, (double)p_73733_4_, 0);
        tessellator.addVertex((double)p_73733_3_, (double)p_73733_4_, 0);
        tessellator.draw();
        GL11.glShadeModel(GL11.GL_FLAT);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glPopMatrix();
    }
    
	public static void drawItem(ItemStack item, float x, float y , float scale) {
		GL11.glPushMatrix();
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		GL11.glScalef(scale, scale, scale);
		GL11.glTranslatef( x / scale, y / scale, 0);
		RenderHelper.enableGUIStandardItemLighting();
		RenderItem.getInstance().renderItemAndEffectIntoGUI(Minecraft.getMinecraft().fontRenderer, Minecraft.getMinecraft().getTextureManager(), item, 0, 0);
		RenderHelper.disableStandardItemLighting();
		GL11.glDisable(GL12.GL_RESCALE_NORMAL);	
		GL11.glPopMatrix();
	}
	
    
	public static void drawScaleText(String text, double x, double y, float scale, boolean isShadow, int color) {
		GL11.glPushMatrix();
		GL11.glTranslated(x, y, 0);
		GL11.glScalef(scale, scale, scale);
		if (isShadow) {
			Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(text, 0, 0, color);
		} else {
			Minecraft.getMinecraft().fontRenderer.drawString(text, 0, 0, color);
		}
		GL11.glPopMatrix();
	}

	public static void drawGradientRect(float g, float top, float right, float bottom, int startColor, int endColor, String type) {
		float f = (startColor >> 24 & 0xFF) / 255.0f;
		float f2 = (startColor >> 16 & 0xFF) / 255.0f;
		float f3 = (startColor >> 8 & 0xFF) / 255.0f;
		float f4 = (startColor & 0xFF) / 255.0f;
		float f5 = (endColor >> 24 & 0xFF) / 255.0f;
		float f6 = (endColor >> 16 & 0xFF) / 255.0f;
		float f7 = (endColor >> 8 & 0xFF) / 255.0f;
		float f8 = (endColor & 0xFF) / 255.0f;

		right = g + right;
		bottom = top + bottom;

		GL11.glDisable(3553);
		GL11.glEnable(3042);
		GL11.glDisable(3008);
		OpenGlHelper.glBlendFunc(770, 771, 1, 0);
		GL11.glShadeModel(7425);
		Tessellator tessellator = Tessellator.instance;
		tessellator.startDrawingQuads();
		switch (type) {
		case "rigth":
			tessellator.setColorRGBA_F(f2, f3, f4, f);
			tessellator.addVertex((double) right, (double) top, 0.0);
			tessellator.setColorRGBA_F(f6, f7, f8, f5);
			tessellator.addVertex((double) g, (double) top, 0.0);
			tessellator.setColorRGBA_F(f6, f7, f8, f5);
			tessellator.addVertex((double) g, (double) bottom, 0.0);
			tessellator.setColorRGBA_F(f2, f3, f4, f);
			tessellator.addVertex((double) right, (double) bottom, 0.0);
			break;
		case "left":
			tessellator.setColorRGBA_F(f6, f7, f8, f5);
			tessellator.addVertex((double) right, (double) top, 0.0);
			tessellator.setColorRGBA_F(f2, f3, f4, f);
			tessellator.addVertex((double) g, (double) top, 0.0);
			tessellator.setColorRGBA_F(f2, f3, f4, f);
			tessellator.addVertex((double) g, (double) bottom, 0.0);
			tessellator.setColorRGBA_F(f6, f7, f8, f5);
			tessellator.addVertex((double) right, (double) bottom, 0.0);
			break;
		case "up":
			tessellator.setColorRGBA_F(f2, f3, f4, f);
			tessellator.addVertex((double) right, (double) top, 0.0);
			tessellator.setColorRGBA_F(f2, f3, f4, f);
			tessellator.addVertex((double) g, (double) top, 0.0);
			tessellator.setColorRGBA_F(f6, f7, f8, f5);
			tessellator.addVertex((double) g, (double) bottom, 0.0);
			tessellator.setColorRGBA_F(f6, f7, f8, f5);
			tessellator.addVertex((double) right, (double) bottom, 0.0);
			break;
		case "down":
			tessellator.setColorRGBA_F(f6, f7, f8, f5);
			tessellator.addVertex((double) right, (double) top, 0.0);
			tessellator.setColorRGBA_F(f6, f7, f8, f5);
			tessellator.addVertex((double) g, (double) top, 0.0);
			tessellator.setColorRGBA_F(f2, f3, f4, f);
			tessellator.addVertex((double) g, (double) bottom, 0.0);
			tessellator.setColorRGBA_F(f2, f3, f4, f);
			tessellator.addVertex((double) right, (double) bottom, 0.0);
			break;
		default:
			break;
		}
		tessellator.draw();
		GL11.glShadeModel(7424);
		GL11.glDisable(3042);
		GL11.glEnable(3008);
		GL11.glEnable(3553);
	}

	public static void drawRect(double left, double top, double right, double bottom, int color) {
		right = left + right;
		bottom = top + bottom;

		float f3 = (color >> 24 & 0xFF) / 255.0f;
		float f4 = (color >> 16 & 0xFF) / 255.0f;
		float f5 = (color >> 8 & 0xFF) / 255.0f;
		float f6 = (color & 0xFF) / 255.0f;
		Tessellator tessellator = Tessellator.instance;
		tessellator.startDrawingQuads();
		GL11.glPushMatrix();
		GL11.glEnable(3042);
		GL11.glDisable(3553);
		GL11.glColor4f(f4, f5, f6, f3);
		tessellator.addVertex(left, bottom, 0.0);
		tessellator.addVertex(right, bottom, 0.0);
		tessellator.addVertex(right, top, 0.0);
		tessellator.addVertex(left, top, 0.0);
		tessellator.draw();
		GL11.glEnable(3553);
	//	GL11.glDisable(3042);
		GL11.glPopMatrix();
	}

	public static void playSound(String name) {
		Minecraft.getMinecraft().thePlayer.playSound(name, 1.0f, 1.0f);
	}
	



	   
}
