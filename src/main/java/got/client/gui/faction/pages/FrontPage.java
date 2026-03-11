package got.client.gui.faction.pages;

import brain.factions.Faction;
import brain.factions.network.PacketMessage;
import got.client.GOTTickHandlerClient;
import got.client.gui.GOTGuiMap;
import got.client.gui.GOTGuiRendererMap;
import got.client.gui.faction.GOTGuiButtonPledge;
import got.client.gui.faction.GOTGuiFactions;
import got.client.gui.faction.GuiCustomButton;
import got.client.gui.utils.GuiApi;
import got.client.utils.UtilO;
import got.common.GOTLevelData;
import got.common.GOTPlayerData;
import got.common.faction.GOTFaction;
import got.common.faction.GOTFactionRelations;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import org.lwjgl.opengl.GL11;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FrontPage implements IPageRenderer {

    private final GOTGuiFactions parent;
    private GOTGuiButtonPledge buttonPledge;
    private GuiButton buttonViewMap;
    private GuiButton buttonTeleportHome, buttonSetHome, buttonMembers, buttonCollections, buttonLeaveFaction;

    private GOTGuiMap mapGui;
    private GOTGuiRendererMap miniMapRenderer;

    private List<GOTFaction> allies = new ArrayList<>();
    private List<GOTFaction> enemies = new ArrayList<>();
    private List<GOTFaction> wars = new ArrayList<>();
    private GuiButton buttonWarCouncil;

    public FrontPage(GOTGuiFactions parent) {
        this.parent = parent;
        this.mapGui = new GOTGuiMap();
        this.miniMapRenderer = new GOTGuiRendererMap();
        this.miniMapRenderer.setSepia(false);
    }

    @Override
    public void onOpened() {
        allies.clear();
        enemies.clear();
        wars.clear();

        final GOTFaction current = GOTGuiFactions.currentFaction;
        if (current == null) return;

        for (GOTFaction other : GOTFaction.values()) {
            if (other == current || !other.isPlayableAlignmentFaction()) continue;

            GOTFactionRelations.Relation relation = GOTFactionRelations.getRelations(current, other);
            if (relation == GOTFactionRelations.Relation.ALLY) allies.add(other);
            if (relation == GOTFactionRelations.Relation.ENEMY) enemies.add(other);
            if (relation == GOTFactionRelations.Relation.MORTAL_ENEMY) wars.add(other);
        }
    }

    @Override
    public void initGui(List<GuiButton> buttonList) {
        int gap = 10;
        int buttonHeight = 20;
        int managementButtonWidth = 130;


        int pledgeButtonBaseY = parent.getBaseHeight() - 60;
        int pledgeButtonY = pledgeButtonBaseY - 15;

        int pledgeButtonWidth = 150 * 2;
        int pledgeButtonHeight = buttonHeight * 2;
        int pledgeButtonX = parent.getBaseWidth() / 2 - (pledgeButtonWidth / 2);

        this.buttonPledge = new GOTGuiButtonPledge(parent, 9, pledgeButtonX, pledgeButtonY, pledgeButtonWidth, pledgeButtonHeight, GOTGuiFactions.currentFaction);

        int mapWidgetWidth = (int)(150 * 1.3);
        int mapWidgetHeight = (int)(100 * 1.3);
        int mapWidgetX = parent.getBaseWidth() - 15 - mapWidgetWidth;
        int mapWidgetY = 35;

        int diplomacyButtonHeight = (int)(buttonHeight * 1.3); // 26
        int diplomacyButtonY = mapWidgetY + mapWidgetHeight + 5;
        this.buttonViewMap = new GuiCustomButton(11, mapWidgetX, diplomacyButtonY, mapWidgetWidth, diplomacyButtonHeight, "Дипломатия");

        int mgmt_row2Y = pledgeButtonY - gap - buttonHeight;
        int mgmt_row1Y = mgmt_row2Y - gap - buttonHeight;


        int rightAlignX = parent.getBaseWidth() - 15;
        int mgmt_col2X = rightAlignX - managementButtonWidth;
        int mgmt_col1X = mgmt_col2X - gap - managementButtonWidth;

        buttonMembers = new GuiCustomButton(7, mgmt_col1X, mgmt_row1Y + 63, managementButtonWidth, buttonHeight, "Участники");
        buttonCollections = new GuiCustomButton(22, mgmt_col2X, mgmt_row1Y + 63, managementButtonWidth, buttonHeight, "Казна и сборы");
        buttonSetHome = new GuiCustomButton(6, mgmt_col1X, mgmt_row2Y + 63, managementButtonWidth, buttonHeight, "Установить дом");
        buttonTeleportHome = new GuiCustomButton(5, mgmt_col2X, mgmt_row2Y + 63, managementButtonWidth, buttonHeight, "Телепорт домой");

        int xSize = parent.getBaseWidth();
        buttonWarCouncil = new GuiCustomButton(50, xSize - 175, 10, 150, 20, "Военный совет");
        buttonList.add(buttonWarCouncil);

        buttonList.add(buttonPledge);
        buttonList.add(buttonViewMap);
        buttonList.add(buttonMembers);
        buttonList.add(buttonCollections);
        buttonList.add(buttonSetHome);
        buttonList.add(buttonTeleportHome);

        this.mapGui.setWorldAndResolution(parent.mc, parent.width, parent.height);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, int scaledMouseX, int scaledMouseY, float partialTicks) {
        parent.drawCenteredString(GOTGuiFactions.currentFaction.factionName(), parent.getBaseWidth() / 2, 15, 0xFFFFFF);

        Faction factionData = parent.getFactionData();
        if (factionData == null) {
            parent.drawCenteredString("§cНет данных о фракции.", parent.getBaseWidth() / 2, parent.getBaseHeight() / 2, 0xFF5555);
            return;
        }

        if (factionData != null) {
            buttonWarCouncil.visible = parent.isPlayerLeader();
        } else {
            buttonWarCouncil.visible = false;
        }

        GOTPlayerData pd = GOTLevelData.getData(parent.mc.thePlayer);
        boolean isMember = parent.isPlayerMember();

        int bannerX = 20;
        int bannerY = 40;
        int bannerWidth = 200;
        int bannerHeight = 300;

        UtilO.drawRoundedRectangle(bannerX, bannerY, bannerWidth, bannerHeight, 5, new Color(50, 50, 50, 100).getRGB(), 0);
        parent.drawCenteredString("Знамя", bannerX + bannerWidth / 2, bannerY + bannerHeight / 2, 0xFFFFFF);

        int infoX = 250;
        int infoY = 40;

        parent.drawString("Лидер: §7" + (factionData.getLeaderName().isEmpty() ? "Нет" : factionData.getLeaderName()), infoX, infoY, 0xFFFFFFFF);
        infoY += 12;
        parent.drawString("Заместитель: §7" + (factionData.getAssistantName().isEmpty() ? "Нет" : factionData.getAssistantName()), infoX, infoY, 0xFFFFFFFF);
        infoY += 12;
        parent.drawString("Столица: §7" + factionData.getCapitalName(), infoX, infoY, 0xFFFFFFFF);
        infoY += 12;
        parent.drawString("Участников: §7" + factionData.getPlayers().size(), infoX, infoY, 0xFFFFFFFF);
        infoY += 12;

        infoY += 20;
        int politicsColWidth = 120;
        int politicsColGap = 10;
        drawRelationList("Союз:", allies, infoX, infoY, 0x55FF55, politicsColWidth);
        drawRelationList("Война:", wars, infoX + politicsColWidth + politicsColGap, infoY, 0xFF5555, politicsColWidth);
        drawRelationList("Враждебность:", enemies, infoX + (politicsColWidth + politicsColGap) * 2, infoY, 0xFFAA00, politicsColWidth);

        int mapWidgetWidth = (int)(150 * 1.3);
        int mapWidgetHeight = (int)(100 * 1.3);
        int mapWidgetX = parent.getBaseWidth() - 15 - mapWidgetWidth;
        int mapWidgetY = 35;

        UtilO.drawRoundedRectangle(mapWidgetX, mapWidgetY, mapWidgetWidth, mapWidgetHeight, 2, new Color(10, 10, 10, 200).getRGB(), 0);

        GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);
        GL11.glPushMatrix();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        float zoom = -1.5f;
        miniMapRenderer.zoomExp = zoom;
        miniMapRenderer.zoomStable = (float) Math.pow(1.5, zoom);

        if (GOTGuiFactions.currentFaction != null && GOTGuiFactions.currentFaction.factionMapInfo != null) {
            miniMapRenderer.mapX = GOTGuiFactions.currentFaction.factionMapInfo.mapX;
            miniMapRenderer.mapY = GOTGuiFactions.currentFaction.factionMapInfo.mapY;
        } else {
            miniMapRenderer.mapX = 810.0f;
            miniMapRenderer.mapY = 730.0f;
        }
        miniMapRenderer.prevMapX = miniMapRenderer.mapX;
        miniMapRenderer.prevMapY = miniMapRenderer.mapY;

        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        GuiApi.glScissor(
                parent.getGuiLeft() + (int)(mapWidgetX * parent.getScaleFactor()),
                parent.getGuiTop() + (int)(mapWidgetY * parent.getScaleFactor()),
                (int)(mapWidgetWidth * parent.getScaleFactor()),
                (int)(mapWidgetHeight * parent.getScaleFactor()),
                true
        );

        miniMapRenderer.renderMap(parent, mapGui, partialTicks,
                mapWidgetX, mapWidgetY,
                mapWidgetX + mapWidgetWidth, mapWidgetY + mapWidgetHeight);

        GL11.glDisable(GL11.GL_SCISSOR_TEST);

        GL11.glPopMatrix();
        GL11.glPopAttrib();

        float alignment = pd.getAlignment(GOTGuiFactions.currentFaction);
        GOTTickHandlerClient.renderAlignmentBar(alignment, false, GOTGuiFactions.currentFaction, infoX - 125, bannerY + bannerHeight + 10, true, true, true, true);

        if (buttonPledge != null) buttonPledge.visible = true;

        if (isMember) {
            buttonMembers.visible = true;
            buttonCollections.visible = true;
            buttonSetHome.visible = true;
            buttonTeleportHome.visible = true;
            buttonViewMap.visible = true;
            buttonSetHome.enabled = parent.isPlayerLeader();
        } else {
            buttonMembers.visible = false;
            buttonCollections.visible = false;
            buttonSetHome.visible = false;
            buttonTeleportHome.visible = false;
            buttonViewMap.visible = false;
        }

        if (buttonPledge != null) {
            buttonPledge.updatePledgeState();
        }
    }

    private void drawRelationList(String title, List<GOTFaction> factions, int x, int y, int color, int width) {
        parent.drawString(title, x, y, 0xFFFFFF);
        y += 12;
        if (factions.isEmpty()) {
            parent.drawString(" §7Нет", x, y, 0xAAAAAA);
        } else {
            for (GOTFaction fac : factions) {
                List<String> lines = parent.getFontRenderer().listFormattedStringToWidth("- " + fac.factionName(), width - 4);
                for (String line : lines) {
                    parent.drawString(line, x + 2, y, color);
                    y += 12;
                }
            }
        }
    }

    @Override
    public void actionPerformed(GuiButton button) {
        if (button == buttonViewMap) {
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
        else if (button == buttonWarCouncil) {
            parent.setCurrentPage(GOTGuiFactions.Page.WAR_COUNCIL);
        }
    }

    @Override public void mouseClicked(int mouseX, int mouseY, int scaledMouseX, int scaledMouseY, int button) {}
    @Override public void keyTyped(char c, int key) {}
    @Override public void handleMouseInput() {}
    @Override
    public void update() {
        if (miniMapRenderer != null) {
            miniMapRenderer.updateTick();
        }
    }
    @Override public void onGuiClosed() {}
    @Override public boolean isTextFieldFocused() { return false; }
}