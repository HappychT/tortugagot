package got.client.gui.faction.overlays;

import brain.factions.network.PacketFactionManage;
import got.client.gui.faction.GOTGuiFactions;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import java.util.List;
import got.client.gui.faction.GuiCustomButton;

public class DepositProvisionsOverlay implements IOverlayRenderer {
    private final GOTGuiFactions parent;
    private GuiTextField amountField;
    private GuiButton confirmButton, cancelButton;
    private String structureId;

    public DepositProvisionsOverlay(GOTGuiFactions parent) {
        this.parent = parent;
    }

    public void setStructureId(String id) {
        this.structureId = id;
    }

    @Override
    public void initGui(List<GuiButton> buttonList) {
        int overlayX = parent.getBaseWidth() / 2;
        int overlayY = parent.getBaseHeight() / 2;

        amountField = new GuiTextField(parent.getFontRenderer(), overlayX - 100, overlayY - 10, 200, 20);
        confirmButton = new GuiCustomButton(60, overlayX - 102, overlayY + 20, 100, 20, "Подтвердить");
        cancelButton = new GuiCustomButton(61, overlayX + 2, overlayY + 20, 100, 20, "Отмена");

        buttonList.add(confirmButton);
        buttonList.add(cancelButton);

        amountField.setFocused(true);
        amountField.setMaxStringLength(9);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, int scaledMouseX, int scaledMouseY, float partialTicks) {
        int overlayX = parent.getBaseWidth() / 2;
        int overlayY = parent.getBaseHeight() / 2;

        confirmButton.visible = true;
        cancelButton.visible = true;

        parent.drawCenteredString("Внести продовольствие", overlayX, overlayY - 30, 0xFFFFFF);
        parent.drawString("Количество:", overlayX - 100, overlayY - 22, 0xA0A0A0);
        amountField.drawTextBox();
    }

    @Override
    public void actionPerformed(GuiButton button) {
        if (button == confirmButton) {
            try {
                int amount = Integer.parseInt(amountField.getText());
                if (amount > 0 && structureId != null) {
                    brain.factions.servers.CoreFaction.brainChannel.sendToServer(PacketFactionManage.depositProvisions(amount, structureId));
                    parent.setCurrentOverlay(GOTGuiFactions.Overlay.NONE, false);
                } else {
                    amountField.setText("§cКоличество должно быть > 0!");
                }
            } catch (NumberFormatException e) {
                amountField.setText("§cНеверное число!");
            }
        } else if (button == cancelButton) {
            parent.setCurrentOverlay(GOTGuiFactions.Overlay.NONE, false);
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int scaledMouseX, int scaledMouseY, int button) {
        amountField.mouseClicked(scaledMouseX, scaledMouseY, button);
    }

    @Override
    public void keyTyped(char c, int key) {
        if (amountField.isFocused() && (Character.isDigit(c) || key == 14 || key == 203 || key == 205)) {
            amountField.textboxKeyTyped(c, key);
        }
    }

    @Override public void update() { amountField.updateCursorCounter(); }
    @Override public void handleMouseInput() {}
    @Override public boolean isTextFieldFocused() { return amountField.isFocused(); }
}