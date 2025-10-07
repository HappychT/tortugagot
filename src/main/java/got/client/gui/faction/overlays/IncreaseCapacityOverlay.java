package got.client.gui.faction.overlays;

import brain.factions.network.PacketFactionManage;
import got.GOT;
import got.client.gui.faction.GOTGuiFactions;
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
        final int overlayX = parent.getGuiLeft() + parent.getXSize() / 2;
        final int overlayY = parent.getGuiTop() + parent.getYSize() / 2;

        increaseCapacityAmountField = new GuiTextField(parent.getFontRenderer(), overlayX - 100, overlayY - 10, 200, 20);
        buttonConfirm = new GuiButton(43, overlayX - 102, overlayY + 20, 100, 20, "Подтвердить");
        buttonCancel = new GuiButton(44, overlayX + 2, overlayY + 20, 100, 20, "Отмена");

        buttonList.add(buttonConfirm);
        buttonList.add(buttonCancel);
        increaseCapacityAmountField.setFocused(true);
        increaseCapacityAmountField.setMaxStringLength(9);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        final int overlayX = parent.getGuiLeft() + parent.getXSize() / 2;
        final int overlayY = parent.getGuiTop() + parent.getYSize() / 2;

        buttonConfirm.visible = buttonCancel.visible = true;
        parent.drawCenteredString("Увеличить вместимость казармы", overlayX, overlayY - 40, 0xFFFFFF);
        parent.drawCenteredString("Стоимость: 1000 за 1 слот", overlayX, overlayY - 28, 0xAAAAAA);
        parent.drawString("Количество слотов:", overlayX - 100, overlayY - 22, 0xA0A0A0);
        increaseCapacityAmountField.drawTextBox();
    }

    @Override
    public void actionPerformed(GuiButton button) {
        if (button == buttonConfirm) {
            try {
                int amount = Integer.parseInt(increaseCapacityAmountField.getText());
                if (amount > 0) {
                    brain.factions.servers.CoreFaction.brainChannel.sendToServer(PacketFactionManage.barracksAction("increase_capacity", "", String.valueOf(amount)));
                    parent.setCurrentOverlay(GOTGuiFactions.Overlay.NONE, false);
                } else {
                    increaseCapacityAmountField.setText("§cДолжно быть > 0!");
                }
            } catch (NumberFormatException e) {
                increaseCapacityAmountField.setText("§cНеверное число!");
            }
        } else if (button == buttonCancel) {
            parent.setCurrentOverlay(GOTGuiFactions.Overlay.NONE, false);
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int button) {
        increaseCapacityAmountField.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public void keyTyped(char c, int key) {
        if (Character.isDigit(c) || key == 14 || key == 203 || key == 205) {
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