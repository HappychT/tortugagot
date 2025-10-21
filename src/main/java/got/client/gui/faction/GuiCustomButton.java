package got.client.gui.faction;

import got.client.utils.UtilO;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import org.lwjgl.opengl.GL11;
import java.awt.Color;

public class GuiCustomButton extends GuiButton {

    public GuiCustomButton(int id, int x, int y, int width, int height, String text) {
        super(id, x, y, width, height, text);
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY) {
        if (this.visible) {
            FontRenderer fontrenderer = mc.fontRenderer;
            this.field_146123_n = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;

            Color baseColor = new Color(40, 40, 40, 200);
            Color finalColor;

            if (!this.enabled) {
                finalColor = new Color(10, 10, 10, 150);
            } else if (this.field_146123_n) {
                finalColor = baseColor.brighter();
            } else {
                finalColor = baseColor;
            }

            UtilO.drawRoundedRectangle(this.xPosition, this.yPosition, this.width, this.height, 5, finalColor.getRGB(), 0);

            int textColor = 14737632;
            if (!this.enabled) {
                textColor = 10526880;
            } else if (this.field_146123_n) {
                textColor = 16777120;
            }

            this.drawCenteredString(fontrenderer, this.displayString, this.xPosition + this.width / 2, this.yPosition + (this.height - 8) / 2, textColor);
        }
    }
}