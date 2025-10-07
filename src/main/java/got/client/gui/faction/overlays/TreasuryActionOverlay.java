package got.client.gui.faction.overlays;

import brain.factions.network.PacketFactionManage;
import got.GOT;
import got.client.gui.faction.GOTGuiFactions;
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
        final int overlayX = parent.getGuiLeft() + parent.getXSize() / 2;
        final int overlayY = parent.getGuiTop() + parent.getYSize() / 2;

        treasuryAmountField = new GuiTextField(parent.getFontRenderer(), overlayX - 100, overlayY - 10, 200, 20);
        treasuryConfirmButton = new GuiButton(19, overlayX - 102, overlayY + 20, 100, 20, "Подтвердить");
        treasuryCancelButton = new GuiButton(20, overlayX + 2, overlayY + 20, 100, 20, "Отмена");

        buttonList.add(treasuryConfirmButton);
        buttonList.add(treasuryCancelButton);

        treasuryAmountField.setFocused(true);
        treasuryAmountField.setMaxStringLength(18);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        final int overlayX = parent.getGuiLeft() + parent.getXSize() / 2;
        final int overlayY = parent.getGuiTop() + parent.getYSize() / 2;
        String title = treasuryActionType.equals("deposit") ? "Пожертвовать в казну" : "Снять из казны";

        treasuryConfirmButton.visible = true;
        treasuryCancelButton.visible = true;

        parent.drawCenteredString(title, overlayX, overlayY - 30, 0xFFFFFF);
        parent.drawString("Сумма:", overlayX - 100, overlayY - 22, 0xA0A0A0);
        treasuryAmountField.drawTextBox();
    }

    @Override
    public void actionPerformed(GuiButton button) {
        if (button == treasuryConfirmButton) {
            try {
                long amount = Long.parseLong(treasuryAmountField.getText());
                if (amount > 0) {
                    brain.factions.servers.CoreFaction.brainChannel.sendToServer(PacketFactionManage.treasury(treasuryActionType, amount, treasuryGoalTarget));
                    parent.setCurrentOverlay(GOTGuiFactions.Overlay.NONE, false);
                } else {
                    treasuryAmountField.setText("§cСумма должна быть > 0!");
                }
            } catch (NumberFormatException e) {
                treasuryAmountField.setText("§cНеверное число!");
            }
        } else if (button == treasuryCancelButton) {
            parent.setCurrentOverlay(GOTGuiFactions.Overlay.NONE, false);
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int button) {
        treasuryAmountField.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public void keyTyped(char c, int key) {
        if (Character.isDigit(c) || key == 14 || key == 203 || key == 205) {
            treasuryAmountField.textboxKeyTyped(c, key);
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