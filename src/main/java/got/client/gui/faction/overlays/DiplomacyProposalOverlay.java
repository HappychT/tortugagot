package got.client.gui.faction.overlays;

import brain.factions.Faction;
import brain.factions.network.PacketFactionManage;
import got.client.gui.faction.GOTGuiFactions;
import got.client.gui.faction.GuiCustomButton;
import got.common.faction.GOTFaction;
import got.common.faction.GOTFactionRelations;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.util.ChatComponentText;
import org.lwjgl.input.Keyboard;
import java.util.List;

public class DiplomacyProposalOverlay implements IOverlayRenderer {
    private final GOTGuiFactions parent;
    private GuiButton buttonPropose, buttonCancel;
    private GuiTextField costField;
    private GOTFaction targetFaction;
    private GOTFactionRelations.Relation relationType;

    public DiplomacyProposalOverlay(GOTGuiFactions parent) {
        this.parent = parent;
    }

    @Override
    public void initGui(List<GuiButton> buttonList) {
        Keyboard.enableRepeatEvents(true);
        int overlayX = parent.getBaseWidth() / 2;
        int overlayY = parent.getBaseHeight() / 2;
        buttonPropose = new GuiCustomButton(50, overlayX - 102, overlayY + 40, 100, 20, "Предложить");
        buttonCancel = new GuiCustomButton(51, overlayX + 2, overlayY + 40, 100, 20, "Отмена");
        buttonList.add(buttonPropose);
        buttonList.add(buttonCancel);

        costField = new GuiTextField(parent.getFontRenderer(), overlayX - 50, overlayY, 100, 20);
        costField.setMaxStringLength(9);
        costField.setText("0");

        if (parent.contextMenuObject instanceof GOTFaction) {
            this.targetFaction = (GOTFaction) parent.contextMenuObject;
            this.relationType = GOTFactionRelations.Relation.forName(parent.diplomacyActionType);
        } else {
            this.targetFaction = null;
            this.relationType = null;
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, int scaledMouseX, int scaledMouseY, float partialTicks) {
        int overlayX = parent.getBaseWidth() / 2;
        int overlayY = parent.getBaseHeight() / 2;

        buttonPropose.visible = buttonCancel.visible = true;

        if (targetFaction != null && relationType != null) {
            String title = "Предложение о " + relationType.getDisplayName();
            parent.drawCenteredString(title, overlayX, overlayY - 50, 0xFFFFFF);
            parent.drawCenteredString("Отправить предложение фракции: §e" + targetFaction.factionName(), overlayX, overlayY - 20, 0xFFFFFF);

            parent.drawCenteredString("Предлагаемая сумма:", overlayX, overlayY - 10, 0xFFFFFF);
            costField.drawTextBox();

        } else {
            parent.drawCenteredString("§cОшибка: Цель не выбрана", overlayX, overlayY - 20, 0xFF5555);
            buttonPropose.enabled = false;
        }
    }

    @Override
    public void actionPerformed(GuiButton button) {
        if (button == buttonPropose) {
            try {
                long cost = Long.parseLong(costField.getText());
                Faction factionData = parent.getFactionData();
                if (targetFaction != null && relationType != null && factionData != null) {
                    if (factionData.getTreasury() >= cost) {
                        brain.factions.servers.CoreFaction.brainChannel.sendToServer(PacketFactionManage.proposePolitics(relationType, targetFaction.codeName(), cost));
                        close();
                    } else {
                        parent.mc.thePlayer.addChatMessage(new ChatComponentText("§cВ казне недостаточно средств."));
                    }
                }
            } catch (NumberFormatException e) {
                costField.setText("§cНеверное число!");
            }
        } else if (button == buttonCancel) {
            close();
        }
    }

    private void close() {
        Keyboard.enableRepeatEvents(false);
        parent.contextMenuObject = null;
        parent.setCurrentOverlay(GOTGuiFactions.Overlay.NONE, false);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int scaledMouseX, int scaledMouseY, int button) {
        costField.mouseClicked(scaledMouseX, scaledMouseY, button);
    }

    @Override
    public void keyTyped(char c, int key) {
        if (costField.isFocused()) {
            if (Character.isDigit(c) || key == Keyboard.KEY_BACK || key == Keyboard.KEY_DELETE || key == Keyboard.KEY_LEFT || key == Keyboard.KEY_RIGHT) {
                costField.textboxKeyTyped(c, key);
            }
        }
    }

    @Override public void update() { costField.updateCursorCounter(); }
    @Override public void handleMouseInput() {}
    @Override public boolean isTextFieldFocused() { return costField.isFocused(); }
}