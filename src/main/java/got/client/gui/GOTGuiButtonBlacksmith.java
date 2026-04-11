package got.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import java.util.Iterator;
import java.util.List;

import static got.client.gui.utils.GuiApi.drawHoveringText;


public class GOTGuiButtonBlacksmith extends GuiButton {

    public GOTGuiButtonBlacksmith(int id, int x, int y, int width, int height, String text) {
        super(id, x, y, width, height, text);
        this.id = id;
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY) {
//        flag = mc.gameSettings.guiScale == 3;
//        if (flag)
//            this.field_146123_n = mouseX >= guiLeft + xPosition / 2 && mouseY >= guiTop + yPosition / 2 && mouseX < guiLeft + xPosition / 2 + width / 2 && mouseY < guiTop + yPosition / 2 + height / 2;
        this.field_146123_n = mouseX >= xPosition && mouseY >= yPosition && mouseX < xPosition + width && mouseY < yPosition + height;

        if (!this.enabled && visible) {
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

            drawRect(xPosition, yPosition, xPosition + width, yPosition + height, 0xCC666666);

            GL11.glDisable(GL11.GL_BLEND);
        }
        
        if (this.field_146123_n && this.visible) {

            if (this.enabled) {
                GL11.glEnable(GL11.GL_BLEND);
                GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

//                if (mc.gameSettings.guiScale == 0)
//                    drawRect(guiLeft + xPosition / 2, guiTop + yPosition / 2, guiLeft + xPosition / 2 + width / 2, guiTop + yPosition / 2 + height / 2, 0x60FFFFFF);
                drawRect(xPosition, yPosition, xPosition + width, yPosition + height, 0x60FFFFFF);

                GL11.glDisable(GL11.GL_BLEND);
            }
        }
    }
    //    @Override
//    public void drawButton(Minecraft mc, int i, int j) {
//        if (visible) {
//            mc.getTextureManager().bindTexture(button);
//            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
//            field_146123_n = i >= xPosition && j >= yPosition && i < xPosition + width && j < yPosition + height;
////            drawTexturedModalRect(xPosition, yPosition, 0, 0, width, height);
//            GOTGuiFactionBlacksmith.drawTextureCustomSize(xPosition, yPosition, 0, 0, width, height , width, height);
//            mouseDragged(mc, i, j);
//        }
//    }
}
