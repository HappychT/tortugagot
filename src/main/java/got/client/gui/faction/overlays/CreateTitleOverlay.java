package got.client.gui.faction.overlays;

import brain.factions.Faction;
import brain.factions.network.PacketFactionManage;
import got.client.gui.faction.GOTGuiFactions;
import got.client.gui.faction.GuiCustomButton;
import got.client.gui.utils.GuiApi;
import got.client.utils.UtilO;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.util.MathHelper;
import org.apache.commons.lang3.StringUtils;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.awt.Color;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class CreateTitleOverlay implements IOverlayRenderer {
    private final GOTGuiFactions parent;
    private GuiTextField titleNameField;
    private GuiButton buttonCreateTitle, buttonCancelTitle;
    private final Map<Faction.Permission, Boolean> titlePermissions = new EnumMap<>(Faction.Permission.class);

    // Добавляем переменную для прокрутки
    private float scroll = 0.0f;

    public CreateTitleOverlay(GOTGuiFactions parent) {
        this.parent = parent;
    }

    @Override
    public void initGui(List<GuiButton> buttonList) {
        int overlayX = parent.getBaseWidth() / 2;
        int overlayY = parent.getBaseHeight() / 2;

        // Сдвигаем элементы, чтобы уместить прокручиваемый список
        titleNameField = new GuiTextField(parent.getFontRenderer(), overlayX - 100, overlayY - 80, 200, 20);
        buttonCreateTitle = new GuiCustomButton(17, overlayX - 102, overlayY + 90, 100, 20, "Создать");
        buttonCancelTitle = new GuiCustomButton(18, overlayX + 2, overlayY + 90, 100, 20, "Отмена");

        buttonList.add(buttonCreateTitle);
        buttonList.add(buttonCancelTitle);

        titleNameField.setFocused(true);
        titleNameField.setMaxStringLength(30);
        titlePermissions.clear();
        for (Faction.Permission perm : Faction.Permission.values()) {
            titlePermissions.put(perm, false);
        }

        scroll = 0.0f; // Сбрасываем скролл при открытии
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, int scaledMouseX, int scaledMouseY, float partialTicks) {
        int overlayX = parent.getBaseWidth() / 2;
        int overlayY = parent.getBaseHeight() / 2;

        // Рисуем красивый темный фон окна
        UtilO.drawRoundedRectangle(overlayX - 120, overlayY - 120, 240, 240, 7, new Color(10, 10, 10, 220).getRGB(), 0);

        buttonCreateTitle.visible = buttonCancelTitle.visible = true;

        parent.drawCenteredString("Создание титула", overlayX, overlayY - 110, 0xFFFFFF);
        parent.drawString("Название:", overlayX - 100, overlayY - 92, 0xA0A0A0);
        titleNameField.drawTextBox();

        // Настройки зоны прокрутки для чекбоксов
        int listX = overlayX - 100;
        int listY = overlayY - 50;
        int listWidth = 200;
        int listHeight = 130;

        // Рисуем подложку для списка пермишенов
        Gui.drawRect(listX - 1, listY - 1, listX + listWidth + 1, listY + listHeight + 1, 0xFF000000);
        Gui.drawRect(listX, listY, listX + listWidth, listY + listHeight, 0x80000000);

        // Включаем обрезку, чтобы чекбоксы не вылезали за рамки
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        GuiApi.glScissor(parent.getGuiLeft() + (int)(listX * parent.getScaleFactor()),
                parent.getGuiTop() + (int)(listY * parent.getScaleFactor()),
                (int)(listWidth * parent.getScaleFactor()),
                (int)(listHeight * parent.getScaleFactor()), true);

        Faction.Permission[] perms = Faction.Permission.values();
        int totalHeight = perms.length * 16;
        int scrollOffset = (totalHeight > listHeight) ? (int)(this.scroll * (totalHeight - listHeight)) : 0;

        int checkY = listY + 4 - scrollOffset;
        for (Faction.Permission perm : perms) {
            // Отрисовываем только те, которые сейчас видны в окне
            if (checkY + 15 > listY && checkY < listY + listHeight) {
                boolean checked = titlePermissions.get(perm);
                drawCheckbox(listX + 5, checkY, perm.getDescription(), checked);
            }
            checkY += 16;
        }

        GL11.glDisable(GL11.GL_SCISSOR_TEST);
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
                titleNameField.setText("§cИмя пустое!");
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
            int listX = overlayX - 100;
            int listY = overlayY - 50;
            int listWidth = 200;
            int listHeight = 130;

            // Проверяем клик только внутри зоны списка
            if (parent.isHover(listX, listY, listWidth, listHeight, scaledMouseX, scaledMouseY)) {
                Faction.Permission[] perms = Faction.Permission.values();
                int totalHeight = perms.length * 16;
                int scrollOffset = (totalHeight > listHeight) ? (int)(this.scroll * (totalHeight - listHeight)) : 0;

                int checkY = listY + 4 - scrollOffset;
                for (Faction.Permission perm : perms) {
                    if (scaledMouseY >= checkY && scaledMouseY < checkY + 16) {
                        titlePermissions.put(perm, !titlePermissions.get(perm));
                        return;
                    }
                    checkY += 16;
                }
            }
        }
    }

    @Override
    public void handleMouseInput() {
        int k = Mouse.getEventDWheel();
        if (k != 0) {
            int listHeight = 130;
            int totalHeight = Faction.Permission.values().length * 16;
            if (totalHeight > listHeight) {
                float scrollAmount = (k > 0 ? -1 : 1) * 20.0f / (totalHeight - listHeight);
                scroll = MathHelper.clamp_float(scroll + scrollAmount, 0.0f, 1.0f);
            }
        }
    }

    @Override
    public void keyTyped(char c, int key) {
        if(titleNameField.isFocused()) titleNameField.textboxKeyTyped(c, key);
    }

    @Override
    public void update() {
        titleNameField.updateCursorCounter();
    }

    @Override
    public boolean isTextFieldFocused() {
        return titleNameField.isFocused();
    }
}