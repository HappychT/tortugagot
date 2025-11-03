package got.client.gui.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import java.awt.Color;
import java.awt.Font;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

public class UnicodeFontRenderer extends FontRenderer {
    private final Font awtFont;
    private final Map<Character, Glyph> glyphs = new HashMap<>();

    public UnicodeFontRenderer(Font font) {
        super(Minecraft.getMinecraft().gameSettings, new ResourceLocation("textures/font/ascii.png"), Minecraft.getMinecraft().getTextureManager(), false);
        this.awtFont = font;
        this.FONT_HEIGHT = getFontHeight();
    }

    @Override
    public int drawString(String text, int x, int y, int color) {
        return drawString(text, (float) x, (float) y, color, false);
    }

    @Override
    public int drawStringWithShadow(String text, int x, int y, int color) {
        drawString(text, (float) x + 1, (float) y + 1, color, true);
        return drawString(text, (float) x, (float) y, color, false);
    }

    @Override
    public int getCharWidth(char c) {
        return getStringWidth(String.valueOf(c));
    }

    @Override
    public int getStringWidth(String text) {
        if (text == null || text.isEmpty()) {
            return 0;
        }
        int width = 0;
        for (char c : text.toCharArray()) {
            Glyph glyph = getGlyph(c);
            if (glyph != null) {
                width += glyph.width;
            }
        }
        return width / 2;
    }

    public int getFontHeight() {
        return (int) (getGlyph('A').height / 2.0f);
    }

    private Glyph getGlyph(char c) {
        if (!glyphs.containsKey(c)) {
            try {
                glyphs.put(c, createGlyph(c));
            } catch (Exception e) {
                System.err.println("Failed to create glyph for character: " + c);
                //e.printStackTrace();
                return null;
            }
        }
        return glyphs.get(c);
    }

    private int drawString(String text, float x, float y, int color, boolean shadow) {
        if (text == null || text.isEmpty()) {
            return 0;
        }

        GL11.glPushMatrix();
        GL11.glScalef(0.5F, 0.5F, 0.5F);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        x *= 2.0F;
        y *= 2.0F;

        float r, g, b, a;
        if (shadow) {
            int shadowColor = (color & 16579836) >> 2 | color & -16777216;
            r = (float) (shadowColor >> 16 & 255) / 255.0F;
            g = (float) (shadowColor >> 8 & 255) / 255.0F;
            b = (float) (shadowColor & 255) / 255.0F;
            a = (float) (shadowColor >> 24 & 255) / 255.0F;
        } else {
            r = (float) (color >> 16 & 255) / 255.0F;
            g = (float) (color >> 8 & 255) / 255.0F;
            b = (float) (color & 255) / 255.0F;
            a = (float) (color >> 24 & 255) / 255.0F;
        }
        GL11.glColor4f(r, g, b, a);

        float currentX = x;
        for (char c : text.toCharArray()) {
            Glyph glyph = getGlyph(c);
            if (glyph != null) {
                glyph.draw(currentX, y);
                currentX += glyph.width;
            }
        }

        GL11.glDisable(GL11.GL_BLEND);
        GL11.glPopMatrix();

        return (int) (currentX / 2.0F);
    }

    private Glyph createGlyph(char c) {
        String s = String.valueOf(c);
        int imgSize = 256;

        BufferedImage image = new BufferedImage(imgSize, imgSize, BufferedImage.TYPE_INT_ARGB);
        java.awt.Graphics2D g = image.createGraphics();
        g.setFont(awtFont);
        g.setColor(new Color(255, 255, 255, 0));
        g.fillRect(0, 0, imgSize, imgSize);
        g.setColor(Color.WHITE);

        FontRenderContext frc = g.getFontRenderContext();
        Rectangle2D bounds = awtFont.getStringBounds(s, frc);
        g.drawString(s, 0, (int) -bounds.getY());
        g.dispose();

        int charWidth = (int) Math.ceil(bounds.getWidth());
        int charHeight = (int) Math.ceil(bounds.getHeight());
        if(charWidth <= 0) charWidth = 1;
        if(charHeight <= 0) charHeight = awtFont.getSize();

        BufferedImage finalImage = new BufferedImage(charWidth, charHeight, BufferedImage.TYPE_INT_ARGB);
        java.awt.Graphics2D finalG = finalImage.createGraphics();
        finalG.drawImage(image, 0, 0, charWidth, charHeight, 0, 0, charWidth, charHeight, null);
        finalG.dispose();

        int textureId = GL11.glGenTextures();

        int[] pixels = new int[charWidth * charHeight];
        finalImage.getRGB(0, 0, charWidth, charHeight, pixels, 0, charWidth);
        ByteBuffer buffer = BufferUtils.createByteBuffer(charWidth * charHeight * 4);
        for (int y = 0; y < charHeight; y++) {
            for (int x = 0; x < charWidth; x++) {
                int pixel = pixels[y * charWidth + x];
                buffer.put((byte) ((pixel >> 16) & 0xFF));
                buffer.put((byte) ((pixel >> 8) & 0xFF));
                buffer.put((byte) (pixel & 0xFF));
                buffer.put((byte) ((pixel >> 24) & 0xFF));
            }
        }
        buffer.flip();

        GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureId);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, charWidth, charHeight, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);

        return new Glyph(textureId, charWidth, charHeight);
    }

    private static class Glyph {
        public final int textureId;
        public final int width;
        public final int height;

        public Glyph(int textureId, int width, int height) {
            this.textureId = textureId;
            this.width = width;
            this.height = height;
        }

        public void draw(float x, float y) {
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureId);
            GL11.glBegin(GL11.GL_QUADS);
            {
                GL11.glTexCoord2f(0, 0);
                GL11.glVertex2f(x, y);
                GL11.glTexCoord2f(0, 1);
                GL11.glVertex2f(x, y + height);
                GL11.glTexCoord2f(1, 1);
                GL11.glVertex2f(x + width, y + height);
                GL11.glTexCoord2f(1, 0);
                GL11.glVertex2f(x + width, y);
            }
            GL11.glEnd();
        }
    }
}