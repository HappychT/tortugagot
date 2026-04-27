package got.client.gui.faction;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class GuiTexturedButton extends GuiButton {

    private ResourceLocation texture;

    public GuiTexturedButton(int id, int x, int y, int width, int height, String textureName) {
        super(id, x, y, width, height, "");
        this.texture = new ResourceLocation("got", "textures/gui/faction/" + textureName + ".png");
    }

    // ДОБАВЛЕНО: Метод для динамической смены текстуры
    public void setTexture(String textureName) {
        this.texture = new ResourceLocation("got", "textures/gui/faction/" + textureName + ".png");
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY) {
        if (this.visible) {
            this.field_146123_n = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;

            mc.getTextureManager().bindTexture(this.texture);

            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

            if (!this.enabled) {
                GL11.glColor4f(0.5F, 0.5F, 0.5F, 1.0F);
            } else if (this.field_146123_n) {
                GL11.glColor4f(1.0F, 1.0F, 0.8F, 1.0F);
            }

            drawScaledCustomSizeModalRect(this.xPosition, this.yPosition, 0, 0, 1, 1, this.width, this.height, 1.0F, 1.0F);
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