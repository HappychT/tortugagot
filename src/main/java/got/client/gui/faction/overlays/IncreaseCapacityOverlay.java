package got.client.gui.faction.overlays;

import brain.factions.network.PacketFactionManage;
import got.client.gui.faction.GOTGuiFactions;
import got.client.gui.faction.GuiCustomButton;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import java.util.List;

public class IncreaseCapacityOverlay implements IOverlayRenderer {
    private final GOTGuiFactions parent;
    private GuiTextField increaseCapacityAmountField;
    private GuiButton buttonConfirm, buttonCancel;

    public IncreaseCapacityOverlay(GOTGuiFactions parent) {
        this.parent = parent;
    }

    @Override
    public void initGui(List<GuiButton> buttonList) {
        int overlayX = parent.getBaseWidth() / 2;
        int overlayY = parent.getBaseHeight() / 2;

        increaseCapacityAmountField = new GuiTextField(parent.getFontRenderer(), overlayX - 100, overlayY - 10, 200, 20);
        buttonConfirm = new GuiCustomButton(43, overlayX - 102, overlayY + 20, 100, 20, "Подтвердить");
        buttonCancel = new GuiCustomButton(44, overlayX + 2, overlayY + 20, 100, 20, "Отмена");

        buttonList.add(buttonConfirm);
        buttonList.add(buttonCancel);
        increaseCapacityAmountField.setFocused(true);
        increaseCapacityAmountField.setMaxStringLength(9);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, int scaledMouseX, int scaledMouseY, float partialTicks) {
        int overlayX = parent.getBaseWidth() / 2;
        int overlayY = parent.getBaseHeight() / 2;

        buttonConfirm.visible = true;
        buttonCancel.visible = true;

        parent.drawCenteredString("Увеличить вместимость казармы", overlayX, overlayY - 40, 0xFFFFFF);
        parent.drawCenteredString("Стоимость: 500 прод. за 1 слот", overlayX, overlayY - 28, 0xAAAAAA);
        parent.drawString("Количество слотов:", overlayX - 100, overlayY - 22, 0xA0A0A0);
        increaseCapacityAmountField.drawTextBox();

    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int scaledMouseX, int scaledMouseY, int button) {
        increaseCapacityAmountField.mouseClicked(scaledMouseX, scaledMouseY, button);
    }

    @Override
    public void actionPerformed(GuiButton button) {
        if (button == buttonConfirm) {
            try {
                int amount = Integer.parseInt(increaseCapacityAmountField.getText());
                if (amount > 0) {
                    brain.factions.servers.CoreFaction.brainChannel.sendToServer(PacketFactionManage.increaseBarracksCapacity("", amount));
                    parent.setCurrentOverlay(GOTGuiFactions.Overlay.NONE, true);
                } else {
                    increaseCapacityAmountField.setText("§cДолжно быть > 0!");
                }
            } catch (NumberFormatException e) {
                increaseCapacityAmountField.setText("§cНеверное число!");
            }
        } else if (button == buttonCancel) {
            parent.setCurrentOverlay(GOTGuiFactions.Overlay.NONE, true);
        }
    }

    @Override
    public void keyTyped(char c, int key) {
        if (increaseCapacityAmountField.isFocused() && (Character.isDigit(c) || key == 14 || key == 203 || key == 205)) {
            increaseCapacityAmountField.textboxKeyTyped(c, key);
        }
    }

    @Override
    public void update() {
        increaseCapacityAmountField.updateCursorCounter();
    }

    @Override public void handleMouseInput() {}

    @Override
    public boolean isTextFieldFocused() {
        return increaseCapacityAmountField.isFocused();
    }
}