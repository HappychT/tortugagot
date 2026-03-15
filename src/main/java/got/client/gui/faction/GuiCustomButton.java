package got.client.gui.faction;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class GuiCustomButton extends GuiButton {

    private static final ResourceLocation BUTTON_TEXTURE = new ResourceLocation("got", "textures/gui/faction/button_custom.png");
    private static final ResourceLocation BUTTON_TEXTURE_HOVER = new ResourceLocation("got", "textures/gui/faction/button_custom_hover.png");

    public GuiCustomButton(int id, int x, int y, int width, int height, String text) {
        super(id, x, y, width, height, text);
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY) {
        if (this.visible) {
            FontRenderer fontrenderer = mc.fontRenderer;
            this.field_146123_n = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;

            ResourceLocation textureToUse = (this.field_146123_n && this.enabled) ? BUTTON_TEXTURE_HOVER : BUTTON_TEXTURE;
            mc.getTextureManager().bindTexture(textureToUse);

            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

            drawScaledCustomSizeModalRect(this.xPosition, this.yPosition, 0, 0, 396, 143, this.width, this.height, 396.0F, 143.0F);

            int textColor = 14737632;
            if (!this.enabled) {
                textColor = 10526880;
            } else if (this.field_146123_n) {
                textColor = 16777120;
            }

            int maxWidth = this.width - 6;
            int maxHeight = this.height - 6;
            int stringWidth = fontrenderer.getStringWidth(this.displayString);
            float scale = 1.0F;

            if (stringWidth > 0 && maxWidth > 0 && maxHeight > 0) {
                float scaleX = (float) maxWidth / stringWidth;
                float scaleY = (float) maxHeight / 8.0F;

                float maxPossibleScale = Math.min(scaleX, scaleY);

                if (maxPossibleScale > 1.0F) {
                    scale = Math.max(1.0F, maxPossibleScale * 0.7F);
                } else {
                    scale = maxPossibleScale;
                }
            }
            GL11.glPushMatrix();

            float centerX = this.xPosition + this.width / 2.0F;
            float centerY = this.yPosition + this.height / 2.0F;

            GL11.glTranslatef(centerX, centerY, 0.0F);

            GL11.glScalef(scale, scale, 1.0F);

            this.drawCenteredString(fontrenderer, this.displayString, 0, -4, textColor);

            GL11.glPopMatrix();
        }
    }

    private void drawScaledCustomSizeModalRect(int x, int y, float u, float v, int uWidth, int vHeight, int width, int height, float tileWidth, float tileHeight) {
        float f = 1.0F / tileWidth;
        float f1 = 1.0F / tileHeight;
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV(x, y + height, this.zLevel, u * f, (v + (float)vHeight) * f1);
        tessellator.addVertexWithUV(x + width, y + height, this.zLevel, (u + (float)uWidth) * f, (v + (float)vHeight) * f1);
        tessellator.addVertexWithUV(x + width, y, this.zLevel, (u + (float)uWidth) * f, v * f1);
        tessellator.addVertexWithUV(x, y, this.zLevel, u * f, v * f1);
        tessellator.draw();
    }
}