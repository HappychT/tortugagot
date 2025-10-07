package got.client.gui.faction.overlays;

import brain.factions.network.PacketFactionCreateCollection;
import got.client.gui.faction.GOTGuiFactions;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import org.apache.commons.lang3.StringUtils;
import java.util.List;

public class CreateCollectionOverlay implements IOverlayRenderer {
    private final GOTGuiFactions parent;
    private GuiTextField collectionNameField, collectionAmountField;
    private GuiButton buttonConfirmCollection, buttonCancelCollection;

    public CreateCollectionOverlay(GOTGuiFactions parent) {
        this.parent = parent;
    }

    @Override
    public void initGui(List<GuiButton> buttonList) {
        int overlayWidth = 300;
        int overlayHeight = 150;
        int overlayX = parent.getGuiLeft() + (parent.getXSize() - overlayWidth) / 2;
        int overlayY = parent.getGuiTop() + (parent.getYSize() - overlayHeight) / 2;

        int fieldWidth = 200;
        int fieldX = overlayX + (overlayWidth - fieldWidth) / 2;

        collectionNameField = new GuiTextField(parent.getFontRenderer(), fieldX, overlayY + 40, fieldWidth, 20);
        collectionAmountField = new GuiTextField(parent.getFontRenderer(), fieldX, overlayY + 80, fieldWidth, 20);

        int buttonWidth = 100;
        int buttonsY = overlayY + 110;
        buttonConfirmCollection = new GuiButton(12, overlayX + (overlayWidth / 2) - buttonWidth - 2, buttonsY, buttonWidth, 20, "Подтвердить");
        buttonCancelCollection = new GuiButton(13, overlayX + (overlayWidth / 2) + 2, buttonsY, buttonWidth, 20, "Отмена");


        buttonList.add(buttonConfirmCollection);
        buttonList.add(buttonCancelCollection);

        collectionNameField.setFocused(true);
        collectionNameField.setMaxStringLength(50);
        collectionAmountField.setMaxStringLength(18);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        int overlayWidth = 300;
        int overlayHeight = 150;
        int overlayX = parent.getGuiLeft() + (parent.getXSize() - overlayWidth) / 2;
        int overlayY = parent.getGuiTop() + (parent.getYSize() - overlayHeight) / 2;

        parent.drawPanel(overlayX, overlayY, overlayWidth, overlayHeight);

        buttonConfirmCollection.visible = buttonCancelCollection.visible = true;
        parent.drawCenteredString("Создание нового сбора", overlayX + overlayWidth/2, overlayY + 15, 0xFFFFFF);

        parent.drawString("Название:", collectionNameField.xPosition, collectionNameField.yPosition - 10, 0xA0A0A0);
        collectionNameField.drawTextBox();

        parent.drawString("Цель (сумма):", collectionAmountField.xPosition, collectionAmountField.yPosition - 10, 0xA0A0A0);
        collectionAmountField.drawTextBox();
    }

    @Override
    public void actionPerformed(GuiButton button) {
        if (button == buttonConfirmCollection) {
            try {
                String name = this.collectionNameField.getText();
                if (StringUtils.isBlank(name)) {
                    this.collectionNameField.setText("§cИмя не может быть пустым!");
                    return;
                }
                long amount = Long.parseLong(this.collectionAmountField.getText());
                if (amount > 0) {
                    brain.factions.servers.CoreFaction.brainChannel.sendToServer(new PacketFactionCreateCollection(name, amount));
                    parent.setCurrentOverlay(GOTGuiFactions.Overlay.NONE, false);
                } else {
                    this.collectionAmountField.setText("§cСумма должна быть > 0!");
                }
            } catch (NumberFormatException e) {
                this.collectionAmountField.setText("§cНеверное число!");
            }
        } else if (button == buttonCancelCollection) {
            parent.setCurrentOverlay(GOTGuiFactions.Overlay.NONE, false);
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int button) {
        collectionNameField.mouseClicked(mouseX, mouseY, button);
        collectionAmountField.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public void keyTyped(char c, int key) {
        if (collectionNameField.isFocused()) {
            collectionNameField.textboxKeyTyped(c, key);
        } else if (collectionAmountField.isFocused()) {
            if (Character.isDigit(c) || key == 14 || key == 203 || key == 205) {
                collectionAmountField.textboxKeyTyped(c, key);
            }
        }
    }

    @Override
    public void update() {
        collectionNameField.updateCursorCounter();
        collectionAmountField.updateCursorCounter();
    }

    @Override public void handleMouseInput() {}
    @Override
    public boolean isTextFieldFocused() {
        return collectionNameField.isFocused() || collectionAmountField.isFocused();
    }
}