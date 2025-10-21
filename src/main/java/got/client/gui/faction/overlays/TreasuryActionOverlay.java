package got.client.gui.faction.overlays;

import brain.factions.network.PacketFactionManage;
import got.client.gui.faction.GOTGuiFactions;
import got.client.gui.faction.GuiCustomButton;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import java.util.List;

public class TreasuryActionOverlay implements IOverlayRenderer {
    private final GOTGuiFactions parent;
    private GuiTextField treasuryAmountField;
    private GuiButton treasuryConfirmButton, treasuryCancelButton;

    private String treasuryActionType = "deposit";
    public String treasuryGoalTarget = "";

    public TreasuryActionOverlay(GOTGuiFactions parent) {
        this.parent = parent;
    }

    public void setActionType(String type) {
        this.treasuryActionType = type;
    }

    @Override
    public void initGui(List<GuiButton> buttonList) {
        int overlayX = parent.getBaseWidth() / 2;
        int overlayY = parent.getBaseHeight() / 2;

        treasuryAmountField = new GuiTextField(parent.getFontRenderer(), overlayX - 100, overlayY - 10, 200, 20);
        treasuryConfirmButton = new GuiCustomButton(19, overlayX - 102, overlayY + 20, 100, 20, "Подтвердить");
        treasuryCancelButton = new GuiCustomButton(20, overlayX + 2, overlayY + 20, 100, 20, "Отмена");

        buttonList.add(treasuryConfirmButton);
        buttonList.add(treasuryCancelButton);

        treasuryAmountField.setFocused(true);
        treasuryAmountField.setMaxStringLength(18);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, int scaledMouseX, int scaledMouseY, float partialTicks) {
        int overlayX = parent.getBaseWidth() / 2;
        int overlayY = parent.getBaseHeight() / 2;
        String title = treasuryActionType.equals("deposit") ? "Пожертвовать" : "Снять средства";

        treasuryConfirmButton.visible = true;
        treasuryCancelButton.visible = true;

        parent.drawCenteredString(title, overlayX, overlayY - 30, 0xFFFFFF);
        parent.drawString("Сумма:", overlayX - 100, overlayY - 22, 0xA0A0A0);
        treasuryAmountField.drawTextBox();

        treasuryConfirmButton.drawButton(parent.mc, mouseX, mouseY);
        treasuryCancelButton.drawButton(parent.mc, mouseX, mouseY);
    }

    @Override
    public void actionPerformed(GuiButton button) {
        if (button == treasuryConfirmButton) {
            try {
                long amount = Long.parseLong(treasuryAmountField.getText());
                if (amount > 0) {
                    brain.factions.servers.CoreFaction.brainChannel.sendToServer(PacketFactionManage.treasury(treasuryActionType, amount, treasuryGoalTarget));
                    parent.setCurrentOverlay(GOTGuiFactions.Overlay.NONE, true);
                } else {
                    treasuryAmountField.setText("§cСумма должна быть > 0!");
                }
            } catch (NumberFormatException e) {
                treasuryAmountField.setText("§cНеверное число!");
            }
        } else if (button == treasuryCancelButton) {
            parent.setCurrentOverlay(GOTGuiFactions.Overlay.NONE, true);
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int scaledMouseX, int scaledMouseY, int button) {
        treasuryAmountField.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public void keyTyped(char c, int key) {
        if (treasuryAmountField.isFocused()) {
            if (Character.isDigit(c) || key == 14 || key == 203 || key == 205) {
                treasuryAmountField.textboxKeyTyped(c, key);
            }
        }
    }

    @Override
    public void update() {
        treasuryAmountField.updateCursorCounter();
    }

    @Override public void handleMouseInput() {}
    @Override
    public boolean isTextFieldFocused() {
        return treasuryAmountField.isFocused();
    }
}