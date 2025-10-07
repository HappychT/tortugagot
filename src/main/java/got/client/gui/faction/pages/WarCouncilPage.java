package got.client.gui.faction.pages;

import brain.factions.Faction;
import got.client.gui.faction.GOTGuiFactions;
import net.minecraft.client.gui.GuiButton;
import java.util.List;

public class WarCouncilPage implements IPageRenderer {
    private final GOTGuiFactions parent;
    private GuiButton buttonDeclareWar, buttonMakeAlliance, buttonMakePeace, buttonDeclareHostility;
    private GuiButton buttonPayWarTax;
    private GuiButton buttonIncomingProposals;

    public WarCouncilPage(GOTGuiFactions parent) {
        this.parent = parent;
    }

    @Override
    public void onOpened() {}

    @Override
    public void initGui(List<GuiButton> buttonList) {
        int left = parent.getGuiLeft();
        int top = parent.getGuiTop();

        int councilX = left + 25;
        int councilY = top + 40;
        buttonDeclareWar = new GuiButton(23, councilX, councilY, 150, 20, "Объявить войну");
        buttonMakeAlliance = new GuiButton(24, councilX, councilY + 25, 150, 20, "Предложить союз");
        buttonMakePeace = new GuiButton(25, councilX, councilY + 50, 150, 20, "Предложить мир");
        buttonDeclareHostility = new GuiButton(26, councilX, councilY + 75, 150, 20, "Объявить вражду");
        buttonIncomingProposals = new GuiButton(30, councilX, councilY + 100, 150, 20, "Входящие предложения");

        councilX = left + 200;
        buttonPayWarTax = new GuiButton(27, councilX, councilY, 150, 20, "Оплатить налог");

        buttonList.add(buttonDeclareWar);
        buttonList.add(buttonMakeAlliance);
        buttonList.add(buttonMakePeace);
        buttonList.add(buttonDeclareHostility);
        buttonList.add(buttonIncomingProposals);
        buttonList.add(buttonPayWarTax);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        parent.drawCenteredString("Военный совет", parent.width / 2, parent.getGuiTop() + 15, 0xFFFFFF);

        Faction factionData = parent.getFactionData();
        if (factionData == null) return;

        boolean isLeader = parent.isPlayerLeader();

        buttonDeclareWar.visible = isLeader;
        buttonMakeAlliance.visible = isLeader;
        buttonMakePeace.visible = isLeader;
        buttonDeclareHostility.visible = isLeader;
        buttonIncomingProposals.visible = isLeader;

        int proposalCount = factionData.getProposals().size();
        buttonIncomingProposals.displayString = "Входящие предложения (" + proposalCount + ")";
        buttonIncomingProposals.enabled = proposalCount > 0;

        long treasury = factionData.getTreasury();
        buttonDeclareWar.enabled = treasury >= 10000 && isLeader;
        buttonMakeAlliance.enabled = isLeader;
        buttonMakePeace.enabled = isLeader;
        buttonDeclareHostility.enabled = treasury >= 5000 && isLeader;

        buttonPayWarTax.visible = true;
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