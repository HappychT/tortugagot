package got.client.gui.faction;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import got.common.network.GOTPacketHandler;
import got.common.network.clientToServer.GOTPacketTutorialAction;

public class GOTGuiStructureHeart extends GuiScreen {
    private static final ResourceLocation background = new ResourceLocation("got:textures/gui/faction_base.png");
    private int guiLeft;
    private int guiTop;
    private int xSize = 256;
    private int ySize = 166;

    public GOTGuiStructureHeart() {
    }

    @Override
    public void initGui() {
        super.initGui();
        this.guiLeft = (this.width - this.xSize) / 2;
        this.guiTop = (this.height - this.ySize) / 2;

        this.buttonList.clear();
        this.buttonList.add(new GuiButton(0, this.guiLeft + 48, this.guiTop + 70, 160, 20, "Получить ресурсы"));
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        drawRect(this.guiLeft, this.guiTop, this.guiLeft + this.xSize, this.guiTop + this.ySize, 0xC0000000);

        String title = "Ресурсная Точка";
        this.fontRendererObj.drawString(title, this.guiLeft + this.xSize / 2 - this.fontRendererObj.getStringWidth(title) / 2, this.guiTop + 20, 0xFFFFFF);
        
        String desc = "Во время обучения вы можете получить ресурсы отсюда.";
        this.fontRendererObj.drawString(desc, this.guiLeft + this.xSize / 2 - this.fontRendererObj.getStringWidth(desc) / 2, this.guiTop + 40, 0xAAAAAA);

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if (button.id == 0) {
            // Send packet to server to give wood and advance tutorial
            GOTPacketHandler.networkWrapper.sendToServer(new GOTPacketTutorialAction("stage9_resources"));
            this.mc.displayGuiScreen(null);
        }
    }
}
