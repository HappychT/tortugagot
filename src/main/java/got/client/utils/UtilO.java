package got.client.utils;



import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import static org.lwjgl.opengl.GL11.*;

public class UtilO {


    public static void drawRoundedRectangle(int x, int y, int w, int h, int radius, int color, int index) {
        if (index == -1) {
            Gui.drawRect(x, y, w + x, y + h, color);
        }
        else if (index == 0) {
            Gui.drawRect(x + radius, y + radius, x + w - radius, y + h - radius, color);
            Gui.drawRect(x + radius, y, x + w - radius, y + radius, color);
            Gui.drawRect(x + w - radius, y + radius, x + w, y + h - radius, color);
            Gui.drawRect(x + radius, y + h - radius, x + w - radius, y + h, color);
            Gui.drawRect(x, y + radius, x + radius, y + h - radius, color);
            drawCircle(x + radius, y + radius, radius, 180, 270, color);
            drawCircle(x + w - radius, y + radius, radius, 270, 360, color);
            drawCircle(x + radius, y + h - radius, radius, 90, 180, color);
            drawCircle(x + w - radius, y + h - radius, radius, 0, 90, color);
        }
        else if (index == 1) {
            Gui.drawRect(0, 0, 0, 0, -1);
            Gui.drawRect(x + radius, y + radius, x + w - radius, y + h - radius, color);
            Gui.drawRect(x + radius, y, x + w - radius, y + radius, color);
            Gui.drawRect(x + w - radius, y + radius, x + w, y + h - radius, color);
            Gui.drawRect(x, y + h - radius, x + w, y + h, color);
            Gui.drawRect(x, y + radius, x + radius, y + h - radius, color);
            drawCircle(x + radius, y + radius, radius, 180, 270, color);
            drawCircle(x + w - radius, y + radius, radius, 270, 360, color);
        }
        else if (index == 2) {
            Gui.drawRect(0, 0, 0, 0, -1);
            Gui.drawRect(x + radius, y + radius, x + w - radius, y + h - radius, color);
            Gui.drawRect(x, y, x + w, y + radius, color);
            Gui.drawRect(x + w - radius, y + radius, x + w, y + h - radius, color);
            Gui.drawRect(x + radius, y + h - radius, x + w - radius, y + h, color);
            Gui.drawRect(x, y + radius, x + radius, y + h - radius, color);
            drawCircle(x + radius, y + h - radius, radius, 90, 180, color);
            drawCircle(x + w - radius, y + h - radius, radius, 0, 90, color);
        }
        else if (index == 3) {
            int thickness = 1;
            Gui.drawRect(x + radius, y, x + w - radius, y + thickness, color);
            Gui.drawRect(x + radius, y + h - thickness, x + w - radius, y + h, color);
            Gui.drawRect(x, y + radius, x + thickness, y + h - radius, color);
            Gui.drawRect(x + w - thickness, y + radius, x + w, y + h - radius, color);

            drawCircleOutline(x + radius, y + radius, radius, 180, 270, thickness, color); // верхний левый
            drawCircleOutline(x + w - radius, y + radius, radius, 270, 360, thickness, color); // верхний правый
            drawCircleOutline(x + radius, y + h - radius, radius, 90, 180, thickness, color); // нижний левый
            drawCircleOutline(x + w - radius, y + h - radius, radius, 0, 90, thickness, color); // нижний правый
        }
    }


    public static void drawRectangle(int x, int y, int w, int h, int color) {
        Gui.drawRect(x, y, x + w, y + h, color);
    }



    public static void drawOutlinedRectangle(int x, int y, int w, int h, int t, int color) {
        drawRectangle(x, y, w, t, color);
        drawRectangle(x + w - t, y, t, h, color);
        drawRectangle(x, y + h - t, w, t, color);
        drawRectangle(x, y, t, h, color);
    }



    public static void drawGradientRectangle(int x, int y, int w, int h, int startColor, int endColor) {
        float f = (float) (startColor >> 24 & 255) / 255.0F;
        float f1 = (float) (startColor >> 16 & 255) / 255.0F;
        float f2 = (float) (startColor >> 8 & 255) / 255.0F;
        float f3 = (float) (startColor & 255) / 255.0F;
        float f4 = (float) (endColor >> 24 & 255) / 255.0F;
        float f5 = (float) (endColor >> 16 & 255) / 255.0F;
        float f6 = (float) (endColor >> 8 & 255) / 255.0F;
        float f7 = (float) (endColor & 255) / 255.0F;
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        GL11.glShadeModel(GL11.GL_SMOOTH);
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.setColorRGBA_F(f1, f2, f3, f);
        tessellator.addVertex(x + w, y, 0);
        tessellator.addVertex(x, y, 0);
        tessellator.setColorRGBA_F(f5, f6, f7, f4);
        tessellator.addVertex(x, y + h, 0);
        tessellator.addVertex(x + w, y + h, 0);
        tessellator.draw();
        GL11.glShadeModel(GL11.GL_FLAT);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
    }



    public static void drawHorizontalGradientRectangle(int x, int y, int w, int h, int startColor, int endColor) {
        float f = (float) (startColor >> 24 & 255) / 255.0F;
        float f1 = (float) (startColor >> 16 & 255) / 255.0F;
        float f2 = (float) (startColor >> 8 & 255) / 255.0F;
        float f3 = (float) (startColor & 255) / 255.0F;
        float f4 = (float) (endColor >> 24 & 255) / 255.0F;
        float f5 = (float) (endColor >> 16 & 255) / 255.0F;
        float f6 = (float) (endColor >> 8 & 255) / 255.0F;
        float f7 = (float) (endColor & 255) / 255.0F;
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        GL11.glShadeModel(GL11.GL_SMOOTH);
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.setColorRGBA_F(f1, f2, f3, f);
        tessellator.addVertex(x, y, 0);
        tessellator.addVertex(x, y + h, 0);
        tessellator.setColorRGBA_F(f5, f6, f7, f4);
        tessellator.addVertex(x + w, y + h, 0);
        tessellator.addVertex(x + w, y, 0);
        tessellator.draw();
        GL11.glShadeModel(GL11.GL_FLAT);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
    }

    public static void drawTexturedModalRect(int x, int y, int textureX, int textureY, int width, int height)
    {
        float f = 0.00390625F;
        float f1 = 0.00390625F;
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV(x, y + height, 0, (float)(textureX) * f, (float)(textureY + height) * f1);
        tessellator.addVertexWithUV(x + width, y + height, 0, (float)(textureX + width) * f, (float)(textureY + height) * f1);
        tessellator.addVertexWithUV(x + width, y, 0, (float)(textureX + width) * f, (float)(textureY) * f1);
        tessellator.addVertexWithUV(x, y, 0, (float)(textureX) * f, (float)(textureY) * f1);
        tessellator.draw();
    }



    public static void drawCircle(float x, float y, float r, int h, int j, int color) {
        GL11.glEnable(GL_BLEND);
        GL11.glDisable(GL11.GL_CULL_FACE);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        glBegin(GL_TRIANGLE_FAN);

        ColorHelper.color(color);

        float var;
        glVertex2f(x, y);
        for (var = h; var <= j; var++) {
            ColorHelper.color(color);
            glVertex2f((float) (r * Math.cos(Math.PI * var / 180) + x), (float) (r * Math.sin(Math.PI * var / 180) + y));
        }

        glEnd();
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glDisable(GL_BLEND);
    }
    public static void drawCircle2(float x, float y, float r, int startAngle, int endAngle, int color) {
        float alpha = (float)(color >> 24 & 255) / 255.0F;
        float red = (float)(color >> 16 & 255) / 255.0F;
        float green = (float)(color >> 8 & 255) / 255.0F;
        float blue = (float)(color & 255) / 255.0F;

        GL11.glEnable(GL_BLEND);
        GL11.glDisable(GL11.GL_CULL_FACE);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        glBegin(GL_TRIANGLE_FAN);

        glColor4f(red, green, blue, alpha);
        glVertex2f(x, y);

        for (int i = startAngle; i <= endAngle; i++) {
            glVertex2f((float) (r * Math.cos(Math.PI * i / 180) + x), (float) (r * Math.sin(Math.PI * i / 180) + y));
        }

        glEnd();
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glDisable(GL_BLEND);
    }

    public static void drawCircle2(float x, float y, float r, int color) {
        drawCircle2(x, y, r, 0, 360, color);
    }
    public static void drawTetragon(float x, float y, float radius, int color) {
        float r = (color >> 16 & 255) / 255.0F;
        float g = (color >> 8 & 255) / 255.0F;
        float b = (color & 255) / 255.0F;
        float a = (color >> 24 & 255) / 255.0F;

        Tessellator tess = Tessellator.instance;

        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glColor4f(r, g, b, a);

        tess.startDrawingQuads();
        tess.addVertex(x - radius, y - radius, 0.0D);
        tess.addVertex(x + radius, y - radius, 0.0D);
        tess.addVertex(x + radius, y + radius, 0.0D);
        tess.addVertex(x - radius, y + radius, 0.0D);
        tess.draw();

        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_BLEND);
    }
    public static void drawCircleOutline(float x, float y, float r, int startAngle, int endAngle, float thickness, int color) {
        GL11.glEnable(GL_BLEND);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_CULL_FACE);
        GL11.glLineWidth(thickness);
        GL11.glBegin(GL_LINE_STRIP);

        ColorHelper.color(color);

        for (float angle = startAngle; angle <= endAngle; angle++) {
            float rad = (float) Math.toRadians(angle);
            float dx = (float) (Math.cos(rad) * r + x);
            float dy = (float) (Math.sin(rad) * r + y);
            GL11.glVertex2f(dx, dy);
        }

        GL11.glEnd();
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glDisable(GL_BLEND);
    }

}