package got.client.gui.faction.pages;

import brain.factions.Faction;
import brain.factions.network.PacketMessage;
import got.client.GOTTickHandlerClient;
import got.client.gui.faction.GOTGuiFactions;
import got.client.gui.faction.GuiCustomButton;
import got.client.utils.UtilO;
import got.common.GOTLevelData;
import got.common.GOTPlayerData;
import net.minecraft.client.gui.GuiButton;

import java.awt.Color;
import java.util.Collections;
import java.util.List;

public class FrontPage implements IPageRenderer {

    private final GOTGuiFactions parent;
    private GuiButton buttonApply, buttonViewMap;
    private GuiButton buttonTeleportHome, buttonSetHome, buttonMembers, buttonCollections, buttonLeaveFaction;

    public FrontPage(GOTGuiFactions parent) {
        this.parent = parent;
    }

    @Override
    public void onOpened() {}

    @Override
    public void initGui(List<GuiButton> buttonList) {
        int buttonY = parent.getBaseHeight() - 60;
        int managementButtonWidth = 130;
        int gap = 10;

        int row1Y = 130;
        int row2Y = row1Y + 20 + gap;

        int col1X = 250;
        int col2X = col1X + managementButtonWidth + gap;


        this.buttonApply = new GuiCustomButton(10, parent.getBaseWidth() / 2 - 75, buttonY, 150, 20, "Подать заявку");
        this.buttonViewMap = new GuiCustomButton(11, parent.getBaseWidth() - 165, 10, 150, 20, "Дипломатия");
        buttonMembers = new GuiCustomButton(7, col1X, row1Y, managementButtonWidth, 20, "Участники");
        buttonCollections = new GuiCustomButton(22, col2X, row1Y, managementButtonWidth, 20, "Казна и сборы");
        buttonSetHome = new GuiCustomButton(6, col1X, row2Y, managementButtonWidth, 20, "Установить дом");
        buttonTeleportHome = new GuiCustomButton(5, col2X, row2Y, managementButtonWidth, 20, "Телепорт домой");
        buttonLeaveFaction = new GuiCustomButton(23, 20, buttonY, 150, 20, "§cПокинуть фракцию");


        buttonList.add(buttonApply);
        buttonList.add(buttonViewMap);
        buttonList.add(buttonMembers);
        buttonList.add(buttonCollections);
        buttonList.add(buttonSetHome);
        buttonList.add(buttonTeleportHome);
        buttonList.add(buttonLeaveFaction);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, int scaledMouseX, int scaledMouseY, float partialTicks) {
        parent.drawCenteredString(GOTGuiFactions.currentFaction.factionName(), parent.getBaseWidth() / 2, 15, 0xFFFFFF);
        buttonViewMap.visible = true;

        Faction factionData = parent.getFactionData();
        if (factionData == null) {
            parent.drawCenteredString("§cНет данных о фракции.", parent.getBaseWidth() / 2, parent.getBaseHeight() / 2, 0xFF5555);
            return;
        }

        GOTPlayerData pd = GOTLevelData.getData(parent.mc.thePlayer);
        boolean isMember = parent.isPlayerMember();

        int bannerX = 20;
        int bannerY = 40;
        int bannerWidth = 200;
        int bannerHeight = 300;

        UtilO.drawRoundedRectangle(bannerX, bannerY, bannerWidth, bannerHeight, 5, new Color(50,50,50,100).getRGB(), 0);
        parent.drawCenteredString("Знамя", bannerX + bannerWidth / 2, bannerY + bannerHeight / 2, 0xFFFFFF);


        int infoX = 250;
        int infoY = 40;

        parent.drawString("Лидер: §7" + (factionData.getLeaderName().isEmpty() ? "Нет" : factionData.getLeaderName()), infoX, infoY, 0xFFFFFF);
        infoY += 12;
        parent.drawString("Заместитель: §7" + (factionData.getAssistantName().isEmpty() ? "Нет" : factionData.getAssistantName()), infoX, infoY, 0xFFFFFF);
        infoY += 12;
        parent.drawString("Столица: §7" + factionData.getCapitalName(), infoX, infoY, 0xFFFFFF);
        infoY += 12;
        parent.drawString("Участников: §7" + factionData.getPlayers().size(), infoX, infoY, 0xFFFFFF);


        float alignment = pd.getAlignment(GOTGuiFactions.currentFaction);
        GOTTickHandlerClient.renderAlignmentBar(alignment, false, GOTGuiFactions.currentFaction, infoX, bannerY + bannerHeight - 10, true, true, true, true);


        if (isMember) {
            buttonApply.visible = false;
            buttonMembers.visible = true;
            buttonCollections.visible = true;
            buttonSetHome.visible = true;
            buttonTeleportHome.visible = true;
            buttonLeaveFaction.visible = true;
            buttonSetHome.enabled = parent.isPlayerLeader();
        } else {
            buttonApply.visible = true;
            buttonApply.enabled = alignment >= 500.0f;
            if (parent.isHover(buttonApply.xPosition, buttonApply.yPosition, buttonApply.width, buttonApply.height, scaledMouseX, scaledMouseY) && !buttonApply.enabled) {
                parent.drawTooltip(Collections.singletonList("§cВам необходимо набрать 500 репутации."), mouseX, mouseY);
            }
            buttonMembers.visible = false;
            buttonCollections.visible = false;
            buttonSetHome.visible = false;
            buttonTeleportHome.visible = false;
            buttonLeaveFaction.visible = false;
        }
    }

    @Override
    public void actionPerformed(GuiButton button) {
        if (button == buttonApply) {
            brain.factions.servers.CoreFaction.brainChannel.sendToServer(new PacketMessage("sendApplication#" + GOTGuiFactions.currentFaction.codeName()));
        } else if (button == buttonViewMap) {
            parent.setCurrentView(GOTGuiFactions.View.MAP);
        } else if (button == buttonTeleportHome) {
            brain.factions.servers.CoreFaction.brainChannel.sendToServer(new PacketMessage("home"));
            parent.mc.displayGuiScreen(null);
        } else if (button == buttonSetHome) {
            brain.factions.servers.CoreFaction.brainChannel.sendToServer(new PacketMessage("setHome"));
        } else if (button == buttonMembers) {
            parent.setCurrentPage(GOTGuiFactions.Page.PLAYER_LIST);
        } else if (button == buttonCollections) {
            parent.setCurrentOverlay(GOTGuiFactions.Overlay.COLLECTIONS_LIST, true);
        } else if (button == buttonLeaveFaction) {
            parent.setCurrentOverlay(GOTGuiFactions.Overlay.CONFIRM_LEAVE, true);
        }
    }

    @Override public void mouseClicked(int mouseX, int mouseY, int scaledMouseX, int scaledMouseY, int button) {}
    @Override public void keyTyped(char c, int key) {}
    @Override public void handleMouseInput() {}
    @Override public void update() {}
    @Override public void onGuiClosed() {}
    @Override public boolean isTextFieldFocused() { return false; }
}