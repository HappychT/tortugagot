package got.client.gui.faction.overlays;

import brain.factions.Faction;
import brain.factions.servers.CollectionGoal;
import got.client.gui.faction.GOTGuiFactions;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import java.util.ArrayList;
import java.util.List;

public class SelectGoalOverlay implements IOverlayRenderer {
    private final GOTGuiFactions parent;
    private String actionType;
    private List<String> goalNames;

    public SelectGoalOverlay(GOTGuiFactions parent) {
        this.parent = parent;
    }

    public void setActionType(String type) {
        this.actionType = type;
    }

    @Override
    public void initGui(List<GuiButton> buttonList) {
        goalNames = new ArrayList<>();
        goalNames.add("Основная казна");
        Faction factionData = parent.getFactionData();
        if (factionData != null) {
            for (CollectionGoal goal : factionData.getCollectionGoals()) {
                goalNames.add(goal.getName());
            }
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        int overlayX = parent.getGuiLeft() + parent.getXSize() / 2;
        int overlayY = parent.getGuiTop() + parent.getYSize() / 2;
        int width = 220;
        int height = 180;
        int left = overlayX - width / 2;
        int top = overlayY - height / 2;

        String title = "deposit".equals(actionType) ? "Куда пожертвовать?" : "Откуда снять?";
        parent.drawCenteredString(title, overlayX, top + 10, 0xFFFFFF);

        int entryY = top + 30;
        for (String goalName : goalNames) {
            if (parent.isHover(overlayX - 100, entryY, 200, 20)) {
                Gui.drawRect(overlayX - 100, entryY, overlayX + 100, entryY + 20, 0x50FFFFFF);
            }
            parent.drawCenteredString(goalName, overlayX, entryY + 6, 0xFFFFFF);
            entryY += 22;
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int button) {
        if (button == 0) {
            int overlayX = parent.getGuiLeft() + parent.getXSize() / 2;
            int top = parent.getGuiTop() + (parent.getYSize() - 180) / 2;

            int entryY = top + 30;
            for (String goalName : goalNames) {
                if (parent.isHover(overlayX - 100, entryY, 200, 20)) {
                    TreasuryActionOverlay treasuryOverlay = (TreasuryActionOverlay) parent.getOverlay(GOTGuiFactions.Overlay.TREASURY_ACTION);
                    treasuryOverlay.setActionType(actionType);
                    treasuryOverlay.treasuryGoalTarget = goalName.equals("Основная казна") ? "" : goalName;
                    parent.setCurrentOverlay(GOTGuiFactions.Overlay.TREASURY_ACTION, true);
                    return;
                }
                entryY += 22;
            }
        }
    }

    @Override public void actionPerformed(GuiButton button) {}
    @Override public void keyTyped(char c, int key) {}
    @Override public void update() {}
    @Override public void handleMouseInput() {}
    @Override public boolean isTextFieldFocused() { return false; }
}