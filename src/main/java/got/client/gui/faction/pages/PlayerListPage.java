package got.client.gui.faction.pages;

import brain.factions.Faction;
import brain.factions.network.PacketFactionManage;
import got.GOT;
import got.client.gui.faction.GOTGuiFactions;
import got.client.gui.utils.GuiApi;
import got.client.utils.UtilO;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import org.lwjgl.opengl.GL11;
import java.awt.Color;
import java.util.*;
import java.util.stream.Collectors;

public class PlayerListPage implements IPageRenderer {
    private final GOTGuiFactions parent;
    private GuiButton buttonManageTitles, buttonTitleHierarchy, buttonApplications;
    private GuiButton buttonPlayerKick, buttonPlayerSetTitle;
    private List<Map.Entry<String, Faction.PlayerData>> sortedPlayers = new ArrayList<>();
    private float scroll;

    public PlayerListPage(GOTGuiFactions parent) {
        this.parent = parent;
    }

    @Override
    public void onOpened() {
        Faction factionData = parent.getFactionData();
        if (factionData != null) {
            sortedPlayers = factionData.getPlayers().entrySet().stream()
                    .sorted(Comparator.comparing(e -> e.getValue().getTitle()))
                    .collect(Collectors.toList());
        } else {
            sortedPlayers.clear();
        }
        scroll = 0f;
    }

    @Override
    public void initGui(List<GuiButton> buttonList) {
        int left = parent.getGuiLeft();
        int top = parent.getGuiTop();
        int xSize = parent.getXSize();

        buttonManageTitles = new GuiButton(16, left + xSize - 170, top + 40, 150, 20, "Управление титулами");
        buttonTitleHierarchy = new GuiButton(17, left + xSize - 170, top + 65, 150, 20, "Иерархия титулов");
        buttonApplications = new GuiButton(18, left + xSize - 170, top + 90, 150, 20, "Заявки");

        buttonPlayerKick = new GuiButton(100, 0, 0, 100, 20, "Выгнать");
        buttonPlayerSetTitle = new GuiButton(101, 0, 0, 100, 20, "Назначить титул");

        buttonList.add(buttonManageTitles);
        buttonList.add(buttonTitleHierarchy);
        buttonList.add(buttonApplications);
        buttonList.add(buttonPlayerKick);
        buttonList.add(buttonPlayerSetTitle);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        parent.drawCenteredString("Список Участников", parent.width / 2, parent.getGuiTop() + 15, 0xFFFFFF);

        Faction factionData = parent.getFactionData();
        boolean isLeader = parent.isPlayerLeader();

        buttonManageTitles.visible = isLeader;
        buttonTitleHierarchy.visible = isLeader;
        buttonApplications.visible = isLeader;

        if (factionData != null) {
            buttonApplications.displayString = "Заявки (" + factionData.getApplications().size() + ")";
            buttonApplications.enabled = !factionData.getApplications().isEmpty();
        }

        int x = parent.getGuiLeft() + 25;
        int y = parent.getGuiTop() + 40;
        int listWidth = parent.getXSize() - 200;
        int listHeight = parent.getYSize() - 80;

        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        GuiApi.glScissor(x, y, listWidth, listHeight, false);

        int totalHeight = sortedPlayers.size() * 20;
        int scrollOffset = (int)(scroll * (totalHeight - listHeight));

        for (int i = 0; i < sortedPlayers.size(); i++) {
            int currentY = y + i * 20 - scrollOffset;
            if (currentY + 20 < y || currentY > y + listHeight) continue;

            Map.Entry<String, Faction.PlayerData> entry = sortedPlayers.get(i);

            if (parent.isHover(x, currentY, listWidth, 20)) {
                Gui.drawRect(x, currentY, x + listWidth, currentY + 20, 0x50FFFFFF);
            }
            parent.drawString(entry.getKey() + " (" + entry.getValue().getTitle() + ")", x + 5, currentY + 6, 0xFFFFFF);
            parent.getFontRenderer().drawString("Вступил: " + parent.getDateFormat().format(new Date(entry.getValue().getJoinDate())), x + listWidth - 120, currentY + 6, 0xAAAAAA);
        }
        GL11.glDisable(GL11.GL_SCISSOR_TEST);

        drawPlayerContextMenu();
    }

    private void drawPlayerContextMenu() {
        if (parent.contextMenuObject instanceof String) {
            String playerName = (String) parent.contextMenuObject;
            int x = parent.contextMenuX;
            int y = parent.contextMenuY;
            int menuWidth = 110;
            int menuHeight = 70;

            if (x + menuWidth > parent.getGuiLeft() + parent.getXSize()) x -= menuWidth;
            if (y + menuHeight > parent.getGuiTop() + parent.getYSize()) y -= menuHeight;

            UtilO.drawRoundedRectangle(x, y, menuWidth, menuHeight, 5, new Color(20, 20, 20, 220).getRGB(), 0);
            parent.drawCenteredString(playerName, x + menuWidth / 2, y + 5, 0xFFFFFF);

            buttonPlayerKick.xPosition = x + 5;
            buttonPlayerKick.yPosition = y + 20;
            buttonPlayerKick.visible = parent.isPlayerLeader();

            buttonPlayerSetTitle.xPosition = x + 5;
            buttonPlayerSetTitle.yPosition = y + 42;
            buttonPlayerSetTitle.visible = parent.isPlayerLeader();
        }
    }

    @Override
    public void actionPerformed(GuiButton button) {
        if (button == buttonManageTitles) {
            parent.setCurrentOverlay(GOTGuiFactions.Overlay.CREATE_TITLE, true);
        } else if (button == buttonTitleHierarchy) {
            parent.setCurrentPage(GOTGuiFactions.Page.TITLE_HIERARCHY);
        } else if (button == buttonApplications) {
            parent.setCurrentPage(GOTGuiFactions.Page.APPLICATIONS);
        } else if (parent.contextMenuObject instanceof String) {
            String playerName = (String) parent.contextMenuObject;
            if (button == buttonPlayerKick) {
                brain.factions.servers.CoreFaction.brainChannel.sendToServer(PacketFactionManage.managePlayer(playerName, "kick", ""));
                parent.contextMenuObject = null;
            } else if (button == buttonPlayerSetTitle) {
                parent.selectedPlayerName = playerName;
                parent.setCurrentOverlay(GOTGuiFactions.Overlay.ASSIGN_TITLE, true);
                parent.contextMenuObject = null;
            }
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int button) {
        if (button == 1 && parent.contextMenuObject == null) {
            int listX = parent.getGuiLeft() + 25;
            int listY = parent.getGuiTop() + 40;
            int listWidth = parent.getXSize() - 200;
            int listHeight = parent.getYSize() - 80;

            if (parent.isHover(listX, listY, listWidth, listHeight)) {
                int totalHeight = sortedPlayers.size() * 20;
                int scrollOffset = (int)(scroll * (totalHeight - listHeight));

                int index = (mouseY - listY + scrollOffset) / 20;
                if (index >= 0 && index < sortedPlayers.size()) {
                    parent.contextMenuObject = sortedPlayers.get(index).getKey();
                    parent.contextMenuX = mouseX;
                    parent.contextMenuY = mouseY;
                }
            }
        } else if (button == 0 && parent.contextMenuObject != null) {

        } else if (button == 0) {
            parent.contextMenuObject = null;
        }
    }

    @Override public void keyTyped(char c, int key) {}
    @Override public void handleMouseInput() {}
    @Override public void update() {}
    @Override public void onGuiClosed() {}
    @Override
    public boolean isTextFieldFocused() {
        return false;
    }
}