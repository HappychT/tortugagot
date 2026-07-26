package got.client.gui.tutorial;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.Minecraft;
import got.common.network.GOTPacketHandler;
import got.common.network.clientToServer.GOTPacketTutorialAction;

public class GOTGuiTutorialWelcome extends GuiScreen {
    private int guiLeft;
    private int guiTop;
    private int xSize = 256;
    private int ySize = 166;

    public GOTGuiTutorialWelcome() {
    }

    @Override
    public void initGui() {
        super.initGui();
        this.guiLeft = (this.width - this.xSize) / 2;
        this.guiTop = (this.height - this.ySize) / 2;

        this.buttonList.clear();
        // ID 0, Начать -> Встать в очередь
        this.buttonList.add(new GuiButton(0, this.width / 2 - 80, this.guiTop + 100, 160, 20, brain.tutorial.client.TutorialTextsClient.get("welcome.button_queue")));
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) {
        // Prevent closing with Esc or inventory key
        if (keyCode == 1 || keyCode == this.mc.gameSettings.keyBindInventory.getKeyCode()) {
            return; // Ignore escape/inventory
        }
        super.keyTyped(typedChar, keyCode);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        // Draw dark translucent background
        this.drawDefaultBackground();
        
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glColor4f(0.0F, 0.0F, 0.0F, 0.7F);
        
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.addVertex(0, this.height, 0.0D);
        tessellator.addVertex(this.width, this.height, 0.0D);
        tessellator.addVertex(this.width, 0, 0.0D);
        tessellator.addVertex(0, 0, 0.0D);
        tessellator.draw();
        
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        
        String title = brain.tutorial.client.TutorialTextsClient.get("welcome.title");
        GL11.glPushMatrix();
        GL11.glScalef(2.0F, 2.0F, 2.0F);
        this.fontRendererObj.drawStringWithShadow(title, (this.width / 2 - this.fontRendererObj.getStringWidth(title)) / 2, (this.guiTop + 20) / 2, 0xFFAA00);
        GL11.glPopMatrix();
        
        String desc = brain.tutorial.client.TutorialTextsClient.get("welcome.description");
        this.fontRendererObj.drawStringWithShadow(desc, this.width / 2 - this.fontRendererObj.getStringWidth(desc) / 2, this.guiTop + 60, 0xFFFFFF);

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if (button.id == 0) {
            GOTPacketHandler.networkWrapper.sendToServer(new GOTPacketTutorialAction("join_queue"));
            this.mc.displayGuiScreen(null);
        }
    }
}
