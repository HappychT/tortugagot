package got.client.gui.faction.pages;

import brain.factions.Faction;
import brain.factions.network.PacketMessage;
import got.GOT;
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
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        parent.drawCenteredString("Заявки на вступление", parent.width / 2, parent.getGuiTop() + 15, 0xFFFFFF);

        int x = parent.getGuiLeft() + 25;
        int y = parent.getGuiTop() + 40;
        int listWidth = parent.getXSize() - 50;
        int listHeight = parent.getYSize() - 80;

        if (!applicants.isEmpty()) {
            GL11.glEnable(GL11.GL_SCISSOR_TEST);
            GuiApi.glScissor(x, y, listWidth, listHeight, false);

            int totalHeight = applicants.size() * 22;
            int scrollOffset = (int)(scroll * (totalHeight - listHeight));

            for(int i = 0; i < applicants.size(); i++) {
                int currentY = y + i * 22 - scrollOffset;
                if(currentY + 22 < y || currentY > y + listHeight) continue;

                if(parent.isHover(x, currentY, listWidth, 20)) {
                    Gui.drawRect(x, currentY, x + listWidth, currentY + 20, 0x50FFFFFF);
                }
                parent.drawString(applicants.get(i), x + 5, currentY + 6, 0xFFFFFF);

                int buttonX = x + listWidth - 50;
                boolean hoverAccept = parent.isHover(buttonX, currentY + 5, 20, 10);
                parent.drawString("§a" + (hoverAccept ? "§lПринять" : "Принять"), buttonX - 30, currentY + 6, 0xFFFFFF);

                boolean hoverReject = parent.isHover(buttonX + 40, currentY + 5, 20, 10);
                parent.drawString("§c" + (hoverReject ? "§lОтклонить" : "Отклонить"), buttonX + 20, currentY + 6, 0xFFFFFF);
            }

            GL11.glDisable(GL11.GL_SCISSOR_TEST);
        } else {
            parent.drawCenteredString("§7Нет активных заявок.", parent.width / 2, parent.getGuiTop() + parent.getYSize() / 2, 0xAAAAAA);
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int button) {
        if (button == 0 && !applicants.isEmpty()) {
            int listX = parent.getGuiLeft() + 25;
            int listY = parent.getGuiTop() + 40;
            int listWidth = parent.getXSize() - 50;
            int listHeight = parent.getYSize() - 80;

            if (!parent.isHover(listX, listY, listWidth, listHeight)) return;

            int totalHeight = applicants.size() * 22;
            int scrollOffset = (int)(scroll * (totalHeight - listHeight));

            int index = (mouseY - listY + scrollOffset) / 22;

            if (index >= 0 && index < applicants.size()) {
                String applicant = applicants.get(index);
                int buttonStartX = listX + listWidth - 80;

                if(mouseX >= buttonStartX && mouseX < buttonStartX + 50) {
                    brain.factions.servers.CoreFaction.brainChannel.sendToServer(new PacketMessage("applicat#" + applicant + "#1"));
                    return;
                }
                if(mouseX >= buttonStartX + 60 && mouseX < buttonStartX + 110) {
                    brain.factions.servers.CoreFaction.brainChannel.sendToServer(new PacketMessage("applicat#" + applicant + "#-1"));
                }
            }
        }
    }

    @Override public void initGui(List<GuiButton> buttonList) {}
    @Override public void actionPerformed(GuiButton button) {}
    @Override public void keyTyped(char c, int key) {}
    @Override public void handleMouseInput() {}
    @Override public void update() {}
    @Override public void onGuiClosed() {}
    @Override
    public boolean isTextFieldFocused() {
        return false;
    }
}