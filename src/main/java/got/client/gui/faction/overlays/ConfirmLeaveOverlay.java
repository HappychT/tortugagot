package got.client.gui.faction.overlays;

import brain.factions.network.PacketMessage;
import got.client.gui.faction.GOTGuiFactions;
import got.client.gui.faction.GuiCustomButton;
import got.client.utils.UtilO;
import net.minecraft.client.gui.GuiButton;
import java.awt.Color;
import java.util.List;

public class ConfirmLeaveOverlay implements IOverlayRenderer {

    private final GOTGuiFactions parent;
    private GuiButton buttonConfirm, buttonCancel;

    public ConfirmLeaveOverlay(GOTGuiFactions parent) {
        this.parent = parent;
    }

    @Override
    public void initGui(List<GuiButton> buttonList) {
        int width = 300;
        int height = 100;
        int x = (parent.getBaseWidth() - width) / 2;
        int y = (parent.getBaseHeight() - height) / 2;

        buttonConfirm = new GuiCustomButton(1, x + (width / 2) - 105, y + 55, 100, 20, "§cДа, покинуть");
        buttonCancel = new GuiCustomButton(2, x + (width / 2) + 5, y + 55, 100, 20, "Отмена");

        buttonList.add(buttonConfirm);
        buttonList.add(buttonCancel);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, int scaledMouseX, int scaledMouseY, float partialTicks) {
        int width = 300;
        int height = 100;
        int x = (parent.getBaseWidth() - width) / 2;
        int y = (parent.getBaseHeight() - height) / 2;

        UtilO.drawRoundedRectangle(x, y, width, height, 7, new Color(10, 10, 10, 220).getRGB(), 0);

        parent.drawCenteredString("Вы уверены, что хотите", x + width / 2, y + 20, 0xFFFFFF);
        parent.drawCenteredString("покинуть эту фракцию?", x + width / 2, y + 32, 0xFFFFFF);

        buttonConfirm.visible = true;
        buttonCancel.visible = true;
    }

    @Override
    public void actionPerformed(GuiButton button) {
        if (button == buttonConfirm) {
            brain.factions.servers.CoreFaction.brainChannel.sendToServer(new PacketMessage("quet"));
            parent.mc.displayGuiScreen(null);
        } else if (button == buttonCancel) {
            parent.setCurrentOverlay(GOTGuiFactions.Overlay.NONE, true);
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int scaledMouseX, int scaledMouseY, int button) {}
    @Override public void keyTyped(char c, int key) {}
    @Override public void handleMouseInput() {}
    @Override public void update() {}
    @Override public boolean isTextFieldFocused() { return false; }
}