package got.client.gui.faction.pages;

import brain.factions.Faction;
import got.client.GOTTickHandlerClient;
import got.client.gui.faction.GOTGuiFactions;
import got.client.gui.faction.GuiCustomButton;
import got.common.GOTLevelData;
import got.common.GOTPlayerData;
import got.common.faction.GOTFaction;
import got.common.faction.GOTFactionRelations;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

public class WarCouncilPage implements IPageRenderer {
    private final GOTGuiFactions parent;

    private GuiButton buttonDeclareWar, buttonMakeAlliance, buttonMakePeace, buttonDeclareHostility;
    private GuiButton buttonPayWarTax;
    private GuiButton buttonIncomingProposals;

    private List<GOTFaction> allies = new ArrayList<>();
    private List<GOTFaction> enemies = new ArrayList<>();
    private List<GOTFaction> wars = new ArrayList<>();

    private static final ResourceLocation PLATE_TEX = new ResourceLocation("got", "textures/gui/faction/info_plate.png");

    public WarCouncilPage(GOTGuiFactions parent) {
        this.parent = parent;
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
        int frameWidth = (int) (202 / 1.2f);
        int buttonHeight = 26 * 2;
        int leftX = 65;
        int startY = 60;
        int rightX = parent.getBaseWidth() - 65 - frameWidth;
        int gap = 7;

        buttonDeclareWar = new GuiCustomButton(23, leftX, startY, frameWidth, buttonHeight, "Объявить войну");
        buttonMakeAlliance = new GuiCustomButton(24, leftX, startY + buttonHeight + gap, frameWidth, buttonHeight, "Предложить союз");
        buttonMakePeace = new GuiCustomButton(25, leftX, startY + 2 * (buttonHeight + gap), frameWidth, buttonHeight, "Предложить мир");

        buttonDeclareHostility = new GuiCustomButton(26, rightX, startY, frameWidth, buttonHeight, "Объявить вражду");
        buttonIncomingProposals = new GuiCustomButton(30, rightX, startY + buttonHeight + gap, frameWidth, buttonHeight, "Входящие предложения");
        buttonPayWarTax = new GuiCustomButton(27, rightX, startY + 2 * (buttonHeight + gap), frameWidth, buttonHeight, "Оплатить налог");

        buttonList.add(buttonDeclareWar);
        buttonList.add(buttonMakeAlliance);
        buttonList.add(buttonMakePeace);
        buttonList.add(buttonDeclareHostility);
        buttonList.add(buttonIncomingProposals);
        buttonList.add(buttonPayWarTax);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, int scaledMouseX, int scaledMouseY, float partialTicks) {
        parent.drawCenteredString("Военный совет", parent.getBaseWidth() / 2, 10, 0xFFFFFF);

        Faction factionData = parent.getFactionData();
        if (factionData == null) return;

        boolean isLeader = parent.isPlayerLeader();
        GOTPlayerData pd = GOTLevelData.getData(parent.mc.thePlayer);

        buttonDeclareWar.visible = isLeader;
        buttonMakeAlliance.visible = isLeader;
        buttonMakePeace.visible = isLeader;
        buttonDeclareHostility.visible = isLeader;
        buttonIncomingProposals.visible = isLeader;
        buttonPayWarTax.visible = true;

        int proposalCount = factionData.getProposals().size();
        buttonIncomingProposals.displayString = "Входящие предложения (" + proposalCount + ")";
        buttonIncomingProposals.enabled = proposalCount > 0;

        long treasury = factionData.getTreasury();
        buttonDeclareWar.enabled = treasury >= 10000 && isLeader;
        buttonMakeAlliance.enabled = isLeader;
        buttonMakePeace.enabled = isLeader;
        buttonDeclareHostility.enabled = treasury >= 5000 && isLeader;



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

        float alignment = pd.getAlignment(GOTGuiFactions.currentFaction);
        GOTTickHandlerClient.renderAlignmentBar(alignment, false, GOTGuiFactions.currentFaction, parent.getBaseWidth() / 2, plateY + plateHeight + 35, true, true, true, true);
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
        if (!parent.isPlayerLeader()) return;

        if (button == buttonDeclareWar || button == buttonMakeAlliance || button == buttonMakePeace || button == buttonDeclareHostility) {
            parent.setCurrentView(GOTGuiFactions.View.MAP);
        } else if (button == buttonIncomingProposals) {
            parent.setCurrentOverlay(GOTGuiFactions.Overlay.POLITICS_INCOMING_PROPOSALS, true);
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int scaledMouseX, int scaledMouseY, int button) {}
    @Override public void keyTyped(char c, int key) {}
    @Override public void handleMouseInput() {}
    @Override public void update() {}
    @Override public void onGuiClosed() {}
    @Override
    public boolean isTextFieldFocused() {
        return false;
    }
}