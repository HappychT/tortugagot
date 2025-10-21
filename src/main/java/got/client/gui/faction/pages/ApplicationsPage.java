package got.client.gui.faction.pages;

import brain.factions.Faction;
import brain.factions.network.PacketMessage;
import got.client.gui.faction.GOTGuiFactions;
import got.client.gui.utils.GuiApi;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import org.lwjgl.opengl.GL11;
import java.util.ArrayList;
import java.util.List;

public class ApplicationsPage implements IPageRenderer {
    private final GOTGuiFactions parent;
    private List<String> applicants = new ArrayList<>();
    private float scroll;

    public ApplicationsPage(GOTGuiFactions parent) {
        this.parent = parent;
    }

    @Override
    public void onOpened() {
        Faction factionData = parent.getFactionData();
        if (factionData != null) {
            applicants = new ArrayList<>(factionData.getApplications().keySet());
        } else {
            applicants.clear();
        }
        scroll = 0f;
    }

    @Override
    public void initGui(List<GuiButton> buttonList) {}

    @Override
    public void drawScreen(int mouseX, int mouseY, int scaledMouseX, int scaledMouseY, float partialTicks) {
        parent.drawCenteredString("Заявки на вступление", parent.getBaseWidth() / 2, 15, 0xFFFFFF);

        int listX = 25;
        int listY = 40;
        int listWidth = parent.getBaseWidth() - 50;
        int listHeight = parent.getBaseHeight() - 80;

        if (!applicants.isEmpty()) {
            GL11.glEnable(GL11.GL_SCISSOR_TEST);
            GuiApi.glScissor(parent.getGuiLeft() + (int)(listX * parent.getScaleFactor()),
                    parent.getGuiTop() + (int)(listY * parent.getScaleFactor()),
                    (int)(listWidth * parent.getScaleFactor()),
                    (int)(listHeight * parent.getScaleFactor()),
                    true);

            int totalHeight = applicants.size() * 22;
            int scrollOffset = (totalHeight > listHeight) ? (int)(scroll * (totalHeight - listHeight)) : 0;

            for(int i = 0; i < applicants.size(); i++) {
                int currentY = listY + i * 22 - scrollOffset;
                if(currentY + 22 < listY || currentY > listY + listHeight) continue;

                if(parent.isHover(listX, currentY, listWidth, 20, scaledMouseX, scaledMouseY)) {
                    Gui.drawRect(listX, currentY, listX + listWidth, currentY + 20, 0x50FFFFFF);
                }
                parent.drawString(applicants.get(i), listX + 10, currentY + 6, 0xFFFFFF);

                int buttonX = listX + listWidth - 120;
                boolean hoverAccept = parent.isHover(buttonX, currentY, 50, 20, scaledMouseX, scaledMouseY);
                parent.getFontRenderer().drawString("§a" + (hoverAccept ? "§l§nПринять" : "Принять"), buttonX, currentY + 6, 0xFFFFFF);

                boolean hoverReject = parent.isHover(buttonX + 60, currentY, 50, 20, scaledMouseX, scaledMouseY);
                parent.getFontRenderer().drawString("§c" + (hoverReject ? "§l§nОтклонить" : "Отклонить"), buttonX + 60, currentY + 6, 0xFFFFFF);
            }

            GL11.glDisable(GL11.GL_SCISSOR_TEST);
        } else {
            parent.drawCenteredString("§7Нет активных заявок.", parent.getBaseWidth() / 2, parent.getBaseHeight() / 2, 0xAAAAAA);
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int scaledMouseX, int scaledMouseY, int button) {
        if (button == 0 && !applicants.isEmpty()) {
            int listX = 25;
            int listY = 40;
            int listWidth = parent.getBaseWidth() - 50;
            int listHeight = parent.getBaseHeight() - 80;

            if (!parent.isHover(listX, listY, listWidth, listHeight, scaledMouseX, scaledMouseY)) return;

            int totalHeight = applicants.size() * 22;
            int scrollOffset = (totalHeight > listHeight) ? (int)(scroll * (totalHeight - listHeight)) : 0;

            int index = (scaledMouseY - listY + scrollOffset) / 22;

            if (index >= 0 && index < applicants.size()) {
                String applicant = applicants.get(index);
                int buttonStartX = listX + listWidth - 120;

                if(parent.isHover(buttonStartX, listY + index * 22 - scrollOffset, 50, 20, scaledMouseX, scaledMouseY)) {
                    brain.factions.servers.CoreFaction.brainChannel.sendToServer(new PacketMessage("applicat#" + applicant + "#1"));
                    return;
                }
                if(parent.isHover(buttonStartX + 60, listY + index * 22 - scrollOffset, 50, 20, scaledMouseX, scaledMouseY)) {
                    brain.factions.servers.CoreFaction.brainChannel.sendToServer(new PacketMessage("applicat#" + applicant + "#-1"));
                }
            }
        }
    }

    @Override public void actionPerformed(GuiButton button) {}
    @Override public void keyTyped(char c, int key) {}
    @Override public void handleMouseInput() {}
    @Override public void update() {}
    @Override public void onGuiClosed() {}
    @Override public boolean isTextFieldFocused() { return false; }
}