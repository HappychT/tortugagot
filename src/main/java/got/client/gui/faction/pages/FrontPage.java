package got.client.gui.faction.pages;

import brain.factions.Faction;
import brain.factions.network.PacketMessage;
import got.client.GOTTickHandlerClient;
import got.client.gui.GOTGuiMap;
import got.client.gui.GOTGuiRendererMap;
import got.client.gui.faction.GOTGuiButtonPledge;
import got.client.gui.faction.GOTGuiFactions;
import got.client.gui.faction.GuiCustomButton;
import got.client.gui.faction.GuiTexturedButton;
import got.client.gui.utils.GuiApi;
import got.client.utils.UtilO;
import got.common.GOTLevelData;
import got.common.GOTPlayerData;
import got.common.faction.GOTFaction;
import got.common.faction.GOTFactionRelations;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
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

    private static final ResourceLocation PLATE_TEX = new ResourceLocation("got", "textures/gui/faction/info_plate.png");
    private static final ResourceLocation MAP_FRAME_TEX = new ResourceLocation("got", "textures/gui/faction/map_frame.png");

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
        int pledgeButtonBaseY = parent.getBaseHeight() - 72;
        int pledgeButtonY = pledgeButtonBaseY;

        int pledgeButtonWidth = 64;
        int pledgeButtonHeight = 64;
        int pledgeButtonX = (int) (parent.getBaseWidth() / 1.25f - ((float) pledgeButtonWidth / 2));

        this.buttonPledge = new GOTGuiButtonPledge(parent, 9, pledgeButtonX, pledgeButtonY, pledgeButtonWidth, pledgeButtonHeight, GOTGuiFactions.currentFaction);

        int frameWidth = 168; // (int) (202 / 1.2f);
        int mapWidgetX = 65;
        int mapWidgetY = 60;
        int frameHeight = 111; // (int) (134 / 1.2f);

        int diplomacyButtonHeight = 63;
        int diplomacyButtonY = mapWidgetY + frameHeight + 10;

        this.buttonViewMap = new GuiTexturedButton(11, mapWidgetX, diplomacyButtonY, frameWidth, diplomacyButtonHeight, "diplomacy_button");
        buttonWarCouncil = new GuiTexturedButton(50, mapWidgetX, diplomacyButtonY + diplomacyButtonHeight + 10, frameWidth, diplomacyButtonHeight, "warcouncil_button");
        buttonList.add(buttonWarCouncil);

        int rightButtonsX = parent.getBaseWidth() - mapWidgetX - frameWidth;
        int rightButtonsY = mapWidgetY;
        int rightGap = 10;

        buttonMembers = new GuiTexturedButton(7, rightButtonsX, rightButtonsY, frameWidth, diplomacyButtonHeight, "players_button");
        buttonCollections = new GuiTexturedButton(22, rightButtonsX, rightButtonsY + diplomacyButtonHeight + rightGap, frameWidth, diplomacyButtonHeight, "treasure_button");
        buttonSetHome = new GuiTexturedButton(6, rightButtonsX, rightButtonsY + 2 * (diplomacyButtonHeight + rightGap), frameWidth, diplomacyButtonHeight, "sethome_button");
        buttonTeleportHome = new GuiTexturedButton(5, rightButtonsX, rightButtonsY + 3 * (diplomacyButtonHeight + rightGap), frameWidth, diplomacyButtonHeight, "tp_button");
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
        parent.drawCenteredString(GOTGuiFactions.currentFaction.factionName(), parent.getBaseWidth() / 2, 10, 0xFFFFFF);

        Faction factionData = parent.getFactionData();
        if (factionData == null) {
            parent.drawCenteredString("§cНет данных о фракции.", parent.getBaseWidth() / 2, parent.getBaseHeight() / 2, 0xFF5555);
            return;
        }

        buttonWarCouncil.visible = parent.isPlayerLeader();
        GOTPlayerData pd = GOTLevelData.getData(parent.mc.thePlayer);
        boolean isMember = parent.isPlayerMember();

        int bannerWidth = (int) (181 / 1.25f);
        int bannerHeight = (int) (178 / 1.25f);
        int bannerX = (parent.getBaseWidth() - bannerWidth) / 2;
        int bannerY = 15;

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        parent.mc.getTextureManager().bindTexture(GOTGuiFactions.getFactionBanner(GOTGuiFactions.currentFaction));
        parent.drawScaledCustomSizeModalRect(bannerX, bannerY, 0, 0, 362, 356, bannerWidth, bannerHeight, 362.0F, 356.0F);

        int plateWidth = 250;
        int plateHeight = 172;
        int plateX = (parent.getBaseWidth() - plateWidth) / 2;
        int plateY = bannerY + bannerHeight + 5;

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        parent.mc.getTextureManager().bindTexture(PLATE_TEX);
        parent.drawScaledCustomSizeModalRect(plateX, plateY, 0, 0, 500, 344, plateWidth, plateHeight, 500.0F, 344.0F);

        int infoX = plateX + 15;
        int infoY = plateY + 15;

        parent.drawString("Лидер: §7" + (factionData.getLeaderName().isEmpty() ? "Нет" : factionData.getLeaderName()), infoX, infoY, 0xFFFFFFFF);
        infoY += 12;
        parent.drawString("Заместитель: §7" + (factionData.getAssistantName().isEmpty() ? "Нет" : factionData.getAssistantName()), infoX, infoY, 0xFFFFFFFF);
        infoY += 12;
        parent.drawString("Столица: §7" + factionData.getCapitalName(), infoX, infoY, 0xFFFFFFFF);
        infoY += 12;
        parent.drawString("Участников: §7" + factionData.getPlayers().size(), infoX, infoY, 0xFFFFFFFF);

        infoY += 20;
        int politicsColWidth = 70;
        drawRelationList("Союз:", allies, infoX, infoY, 0x55FF55, politicsColWidth);
        drawRelationList("Война:", wars, infoX + politicsColWidth, infoY, 0xFF5555, politicsColWidth);
        drawRelationList("Враги:", enemies, infoX + politicsColWidth * 2, infoY, 0xFFAA00, politicsColWidth);

        int frameWidth = (int) (202 / 1.2f);
        int frameHeight = (int) (134 / 1.2f);
        int mapWidgetX = 65;
        int mapWidgetY = 60;

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        parent.mc.getTextureManager().bindTexture(MAP_FRAME_TEX);
        parent.drawScaledCustomSizeModalRect(mapWidgetX, mapWidgetY,  0, 0, 404, 268, frameWidth, frameHeight, 404.0F, 268.0F);

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
                parent.getGuiLeft() + (int)((mapWidgetX + 5) * parent.getScaleFactor()),
                parent.getGuiTop() + (int)((mapWidgetY + 5) * parent.getScaleFactor()),
                (int)((frameWidth - 10) * parent.getScaleFactor()),
                (int)((frameHeight - 10) * parent.getScaleFactor()),
                true
        );

        miniMapRenderer.renderMap(parent, mapGui, partialTicks,
                mapWidgetX + 5, mapWidgetY + 5,
                mapWidgetX + frameWidth - 5, mapWidgetY + frameHeight - 5);

        GL11.glDisable(GL11.GL_SCISSOR_TEST);
        GL11.glPopMatrix();
        GL11.glPopAttrib();

        float alignment = pd.getAlignment(GOTGuiFactions.currentFaction);

        int barY = plateY + plateHeight + 35;

        GOTTickHandlerClient.renderAlignmentBar(alignment, false, GOTGuiFactions.currentFaction, parent.getBaseWidth() / 2, barY, false, false, false, true);

        got.common.faction.GOTFactionRank rank = GOTGuiFactions.currentFaction.getRank(alignment);
        String rankName = rank.getShortNameWithGender(pd);

        String centerText = rankName + " (" + (int)alignment + ")";

        int textWidth = parent.getFontRenderer().getStringWidth(centerText);
        int textX = parent.getBaseWidth() / 2 - textWidth / 2;

        int textY = barY + 3;

        parent.getFontRenderer().drawStringWithShadow(centerText, textX, textY, 0xFFFFFFFF);
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