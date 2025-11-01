package got.client.gui.faction.overlays;

import brain.factions.Faction;
import brain.factions.network.PacketFactionManage;
import got.client.gui.faction.GOTGuiFactions;
import got.client.gui.faction.GuiCustomButton;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import org.apache.commons.lang3.StringUtils;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class CreateTitleOverlay implements IOverlayRenderer {
    private final GOTGuiFactions parent;
    private GuiTextField titleNameField;
    private GuiButton buttonCreateTitle, buttonCancelTitle;
    private final Map<Faction.Permission, Boolean> titlePermissions = new EnumMap<>(Faction.Permission.class);

    public CreateTitleOverlay(GOTGuiFactions parent) {
        this.parent = parent;
    }

    @Override
    public void initGui(List<GuiButton> buttonList) {
        int overlayX = parent.getBaseWidth() / 2;
        int overlayY = parent.getBaseHeight() / 2;

        titleNameField = new GuiTextField(parent.getFontRenderer(), overlayX - 100, overlayY - 60, 200, 20);
        buttonCreateTitle = new GuiCustomButton(17, overlayX - 102, overlayY + 80, 100, 20, "Создать");
        buttonCancelTitle = new GuiCustomButton(18, overlayX + 2, overlayY + 80, 100, 20, "Отмена");

        buttonList.add(buttonCreateTitle);
        buttonList.add(buttonCancelTitle);
        titleNameField.setFocused(true);
        titleNameField.setMaxStringLength(30);
        titlePermissions.clear();
        for (Faction.Permission perm : Faction.Permission.values()) {
            titlePermissions.put(perm, false);
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, int scaledMouseX, int scaledMouseY, float partialTicks) {
        int overlayX = parent.getBaseWidth() / 2;
        int overlayY = parent.getBaseHeight() / 2;
        buttonCreateTitle.visible = buttonCancelTitle.visible = true;

        parent.drawCenteredString("Создание титула", overlayX, overlayY - 80, 0xFFFFFF);
        parent.drawString("Название:", overlayX - 100, overlayY - 72, 0xA0A0A0);
        titleNameField.drawTextBox();

        int checkY = overlayY - 40;
        int checkX = overlayX - 100;
        for (Faction.Permission perm : Faction.Permission.values()) {
            boolean checked = titlePermissions.get(perm);
            drawCheckbox(checkX, checkY, perm.getDescription(), checked);
            checkY += 15;
        }
    }

    private void drawCheckbox(int x, int y, String label, boolean isChecked) {
        final int boxSize = 10;
        Gui.drawRect(x, y, x + boxSize, y + boxSize, isChecked ? 0xFFFFFFFF : 0xFF000000);
        Gui.drawRect(x + 1, y + 1, x + boxSize - 1, y + boxSize - 1, 0xFF555555);
        if (isChecked) {
            parent.drawCenteredString("✓", x + boxSize / 2 + 1, y + 1, 0xFF00DD00);
        }
        parent.drawString(label, x + boxSize + 4, y + 1, 0xFFFFFFFF);
    }

    @Override
    public void actionPerformed(GuiButton button) {
        if (button == buttonCreateTitle) {
            String name = titleNameField.getText();
            if (StringUtils.isNotBlank(name)) {
                long perms = 0L;
                for (Map.Entry<Faction.Permission, Boolean> entry : titlePermissions.entrySet()) {
                    if (entry.getValue()) perms |= entry.getKey().getBit();
                }
                brain.factions.servers.CoreFaction.brainChannel.sendToServer(PacketFactionManage.createTitle(name, perms));
                parent.setCurrentOverlay(GOTGuiFactions.Overlay.NONE, true);
            } else {
                titleNameField.setText("§cИмя не может быть пустым!");
            }
        } else if (button == buttonCancelTitle) {
            parent.setCurrentOverlay(GOTGuiFactions.Overlay.NONE, true);
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int scaledMouseX, int scaledMouseY, int button) {
        titleNameField.mouseClicked(scaledMouseX, scaledMouseY, button);

        if (button == 0) {
            int overlayX = parent.getBaseWidth() / 2;
            int overlayY = parent.getBaseHeight() / 2;
            int checkY = overlayY - 40;
            int checkX = overlayX - 100;
            for (Faction.Permission perm : Faction.Permission.values()) {
                if (parent.isHover(checkX, checkY, 150, 15, scaledMouseX, scaledMouseY)) {
                    titlePermissions.put(perm, !titlePermissions.get(perm));
                    return;
                }
                checkY += 15;
            }
        }
    }
    @Override public void keyTyped(char c, int key) { if(titleNameField.isFocused()) titleNameField.textboxKeyTyped(c, key); }
    @Override public void update() { titleNameField.updateCursorCounter(); }
    @Override public void handleMouseInput() {}
    @Override public boolean isTextFieldFocused() { return titleNameField.isFocused(); }
}