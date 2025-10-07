package got.client.gui.faction.pages;

import brain.factions.Faction;
import got.client.gui.faction.GOTGuiFactions;
import got.common.faction.GOTFaction;
import got.common.faction.GOTFactionRelations;
import net.minecraft.client.gui.GuiButton;
import java.util.ArrayList;
import java.util.List;

public class PoliticsPage implements IPageRenderer {

    private final GOTGuiFactions parent;
    private List<GOTFaction> allies = new ArrayList<>();
    private List<GOTFaction> enemies = new ArrayList<>();
    private List<GOTFaction> wars = new ArrayList<>();
    private GuiButton buttonWarCouncil;

    public PoliticsPage(GOTGuiFactions parent) {
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
        int left = parent.getGuiLeft();
        int top = parent.getGuiTop();
        int xSize = parent.getXSize();
        buttonWarCouncil = new GuiButton(50, left + xSize - 175, top + 15, 150, 20, "Военный совет");
        buttonList.add(buttonWarCouncil);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        if (GOTGuiFactions.currentFaction == null) return;
        parent.drawCenteredString("Политика - " + GOTGuiFactions.currentFaction.factionName(), parent.width / 2, parent.getGuiTop() + 15, 0xFFFFFF);

        Faction factionData = parent.getFactionData();
        if (factionData != null) {
            buttonWarCouncil.visible = parent.isPlayerLeader();
        } else {
            buttonWarCouncil.visible = false;
        }

        final int x = parent.getGuiLeft() + 25;
        final int y = parent.getGuiTop() + 40;
        final int columnWidth = (parent.getXSize() - 50) / 3;

        drawRelationList("Союз:", allies, x, y, 0x55FF55, columnWidth);
        drawRelationList("Война:", wars, x + columnWidth + 10, y, 0xFF5555, columnWidth);
        drawRelationList("Враждебность:", enemies, x + (columnWidth + 10) * 2, y, 0xFFAA00, columnWidth);
    }

    private void drawRelationList(String title, List<GOTFaction> factions, int x, int y, int color, int width) {
        parent.drawString(title, x, y, 0xFFFFFF);
        y += 12;
        if (factions.isEmpty()) {
            parent.drawString("  §7Нет", x, y, 0xAAAAAA);
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
        if (button == buttonWarCouncil) {
            parent.setCurrentPage(GOTGuiFactions.Page.WAR_COUNCIL);
        }
    }
    @Override public void mouseClicked(int mouseX, int mouseY, int button) {}
    @Override public void keyTyped(char c, int key) {}
    @Override public void handleMouseInput() {}
    @Override public void update() {}
    @Override public void onGuiClosed() {}
    @Override
    public boolean isTextFieldFocused() {
        return false;
    }
}