package got.client.gui;

import net.minecraft.client.gui.Gui;
import org.lwjgl.opengl.GL11;

import got.common.entity.animal.GOTEntityDirewolf;
import got.common.inventory.GOTContainerDirewolfInventory;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;

public class GOTGuiDirewolfInventory extends GuiContainer {
    private static final ResourceLocation GUI_TEXTURE = new ResourceLocation("textures/gui/container/horse.png");

    private final GOTEntityDirewolf mount;
    private final InventoryPlayer playerInventory;
    private float mouseX;
    private float mouseY;

    public GOTGuiDirewolfInventory(InventoryPlayer playerInv, GOTEntityDirewolf mount) {
        super(new GOTContainerDirewolfInventory(playerInv, mount.getMountInventory(), mount));
        this.mount = mount;
        this.playerInventory = playerInv;
        allowUserInput = false;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        mc.getTextureManager().bindTexture(GUI_TEXTURE);
        int x = (width - xSize) / 2;
        int y = (height - ySize) / 2;
        drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
        drawTexturedModalRect(x + 7, y + 35, 0, ySize + 54, 18, 18);
        GuiInventory.func_147046_a(x + 51, y + 60, 17, x + 51 - this.mouseX, y + 75 - 50 - this.mouseY, mount);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        fontRendererObj.drawString(mount.hasCustomNameTag() ? mount.getCustomNameTag() : mount.getCommandSenderName(), 8, 6, 4210752);
        fontRendererObj.drawString(playerInventory.hasCustomInventoryName() ? playerInventory.getInventoryName() : I18n.format(playerInventory.getInventoryName()), 8, ySize - 96 + 2, 4210752);
        GOTGuiMountStats.draw(Gui::drawRect, fontRendererObj, mount, mount.getTotalArmorValue());
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.mouseX = mouseX;
        this.mouseY = mouseY;
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}