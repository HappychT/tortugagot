package got.client.gui.faction.pages;

import brain.factions.Faction;
import brain.factions.network.PacketFactionManage;
import brain.factions.network.PacketMessage;
import brain.factions.servers.CollectionGoal;
import got.client.gui.faction.GOTGuiFactions;
import got.client.gui.faction.overlays.SelectGoalOverlay;
import got.client.utils.UtilO;
import net.minecraft.client.gui.GuiButton;
import java.awt.Color;
import java.util.List;

public class ManagementPage implements IPageRenderer {
    private final GOTGuiFactions parent;
    private GuiButton buttonTeleportHome, buttonSetHome, buttonMembers;
    private GuiButton buttonDonate, buttonNewCollection, buttonWithdraw;
    private GuiButton[] closeGoalButtons = new GuiButton[5];

    public ManagementPage(GOTGuiFactions parent) {
        this.parent = parent;
    }

    @Override
    public void onOpened() {}

    @Override
    public void initGui(List<GuiButton> buttonList) {
        int guiLeft = parent.getGuiLeft();
        int guiTop = parent.getGuiTop();
        int ySize = parent.getYSize();
        int margin = 25;
        int buttonWidth = 120;
        int buttonHeight = 20;

        int bottomRowY = guiTop + ySize - 55;
        int topRowY = bottomRowY - 25;

        buttonTeleportHome = new GuiButton(5, guiLeft + margin, topRowY, buttonWidth, buttonHeight, "Телепорт домой");
        buttonSetHome = new GuiButton(6, guiLeft + margin, bottomRowY, buttonWidth, buttonHeight, "Установить дом");
        buttonMembers = new GuiButton(7, guiLeft + margin + buttonWidth + 10, topRowY, buttonWidth, buttonHeight, "Участники");

        buttonDonate = new GuiButton(9, 0, 0, buttonWidth, buttonHeight, "Пожертвовать");
        buttonNewCollection = new GuiButton(11, 0, 0, buttonWidth, buttonHeight, "Новый сбор");
        buttonWithdraw = new GuiButton(21, 0, 0, buttonWidth, buttonHeight, "Снять средства");

        for(int i = 0; i < closeGoalButtons.length; i++){
            closeGoalButtons[i] = new GuiButton(200 + i, 0, 0, 60, 18, "Завершить");
            buttonList.add(closeGoalButtons[i]);
        }

        buttonList.add(buttonTeleportHome);
        buttonList.add(buttonSetHome);
        buttonList.add(buttonMembers);
        buttonList.add(buttonDonate);
        buttonList.add(buttonNewCollection);
        buttonList.add(buttonWithdraw);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        parent.drawCenteredString("Управление Фракцией", parent.getGuiLeft() + parent.getXSize()/2, parent.getGuiTop() + 15, 0xFFFFFF);

        buttonTeleportHome.visible = buttonSetHome.visible = buttonMembers.visible = true;

        Faction factionData = parent.getFactionData();
        if (factionData == null) return;

        boolean isLeader = parent.isPlayerLeader();
        buttonNewCollection.visible = isLeader;
        buttonWithdraw.visible = isLeader;

        int margin = 25;
        int topMargin = 40;
        int guiLeft = parent.getGuiLeft();
        int guiTop = parent.getGuiTop();

        int bannerX = guiLeft + margin;
        int bannerY = guiTop + topMargin;
        int bannerWidth = 80;
        int bannerHeight = 120;
        UtilO.drawRoundedRectangle(bannerX, bannerY, bannerWidth, bannerHeight, 5, new Color(50,50,50,100).getRGB(), 0);
        parent.drawCenteredString("Знамя", bannerX + bannerWidth / 2, bannerY + bannerHeight/2, 0xFFFFFF);

        int infoX = bannerX + bannerWidth + margin;
        int infoY = bannerY;
        parent.drawString("Казна: §e" + factionData.getTreasury(), infoX, infoY, 0xFFFFFF);
        infoY += 15;
        parent.drawString("Текущие сборы:", infoX, infoY, 0xFFFFFF);
        infoY += 12;

        for(GuiButton btn : closeGoalButtons) btn.visible = false;

        if (factionData.getCollectionGoals().isEmpty()) {
            parent.drawString("§7Нет активных сборов", infoX + 5, infoY, 0xAAAAAA);
        } else {
            int goalIndex = 0;
            for (CollectionGoal goal : factionData.getCollectionGoals()) {
                if(goalIndex >= closeGoalButtons.length) break;

                boolean complete = goal.isComplete();
                String color = complete ? "§a" : "§e";
                parent.drawString(goal.getName() + ": " + color + goal.getCurrentAmount() + "/" + goal.getTargetAmount(), infoX + 5, infoY, 0xFFFFFF);

                if (complete && isLeader) {
                    GuiButton closeBtn = closeGoalButtons[goalIndex];
                    closeBtn.xPosition = infoX + parent.getFontRenderer().getStringWidth(goal.getName()) + 180;
                    closeBtn.yPosition = infoY - 4;
                    closeBtn.visible = true;
                }
                infoY += 12;
                goalIndex++;
            }
        }

        buttonDonate.xPosition = infoX;
        buttonDonate.yPosition = infoY + 5;
        buttonDonate.visible = true;

        buttonNewCollection.xPosition = infoX + 130;
        buttonNewCollection.yPosition = infoY + 5;

        buttonWithdraw.xPosition = infoX + 130;
        buttonWithdraw.yPosition = infoY + 30;
    }

    @Override
    public void actionPerformed(GuiButton button) {
        if (button == buttonTeleportHome) {
            brain.factions.servers.CoreFaction.brainChannel.sendToServer(new PacketMessage("home"));
            parent.mc.displayGuiScreen(null);
        } else if (button == buttonSetHome) {
            brain.factions.servers.CoreFaction.brainChannel.sendToServer(new PacketMessage("setHome"));
        } else if (button == buttonMembers) {
            parent.setCurrentPage(GOTGuiFactions.Page.PLAYER_LIST);
        } else if (button == buttonNewCollection) {
            parent.setCurrentOverlay(GOTGuiFactions.Overlay.CREATE_COLLECTION, true);
        } else if (button == buttonDonate) {
            SelectGoalOverlay overlay = (SelectGoalOverlay) parent.getOverlay(GOTGuiFactions.Overlay.SELECT_GOAL);
            overlay.setActionType("deposit");
            parent.setCurrentOverlay(GOTGuiFactions.Overlay.SELECT_GOAL, true);
        } else if (button == buttonWithdraw) {
            SelectGoalOverlay overlay = (SelectGoalOverlay) parent.getOverlay(GOTGuiFactions.Overlay.SELECT_GOAL);
            overlay.setActionType("withdraw");
            parent.setCurrentOverlay(GOTGuiFactions.Overlay.SELECT_GOAL, true);
        } else if (button.id >= 200 && button.id < 200 + closeGoalButtons.length) {
            int goalIndex = button.id - 200;
            Faction factionData = parent.getFactionData();
            if (factionData != null && goalIndex < factionData.getCollectionGoals().size()) {
                CollectionGoal goal = factionData.getCollectionGoals().get(goalIndex);
                if (goal.isComplete()) {
                    brain.factions.servers.CoreFaction.brainChannel.sendToServer(PacketFactionManage.closeCollectionGoal(goal.getName()));
                }
            }
        }
    }
    @Override public void handleMouseInput() {}
    @Override public void mouseClicked(int mouseX, int mouseY, int button) {}
    @Override public void keyTyped(char c, int key) {}
    @Override public void update() {}
    @Override public void onGuiClosed() {}
    @Override public boolean isTextFieldFocused() { return false; }
}