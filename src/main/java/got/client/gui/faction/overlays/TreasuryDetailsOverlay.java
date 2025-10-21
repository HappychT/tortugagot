package got.client.gui.faction.overlays;

import brain.factions.Faction;
import brain.factions.network.PacketFactionManage;
import brain.factions.servers.CollectionGoal;
import got.client.gui.faction.GOTGuiFactions;
import got.client.gui.utils.GuiApi;
import got.client.utils.UtilO;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.MathHelper;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import got.client.gui.faction.GuiCustomButton;

public class TreasuryDetailsOverlay implements IOverlayRenderer {

    private final GOTGuiFactions parent;
    private boolean isMainTreasury;
    private CollectionGoal goal;
    private List<CollectionGoal.Transaction> history = new ArrayList<>();
    private GuiButton closeGoalButton, depositButton, withdrawButton;
    private float scroll;

    public TreasuryDetailsOverlay(GOTGuiFactions parent) {
        this.parent = parent;
    }

    @Override
    public void initGui(List<GuiButton> buttonList) {
        this.goal = parent.selectedCollectionGoal;
        this.isMainTreasury = (this.goal == null);
        Faction factionData = parent.getFactionData();

        if (factionData != null) {
            if (isMainTreasury) {
                this.history = new ArrayList<>(factionData.getTreasuryHistory());
            } else if (this.goal != null) {
                this.history = new ArrayList<>(this.goal.getHistory());
            }
        } else {
            this.history.clear();
        }
        Collections.reverse(this.history);

        int width = 400;
        int height = 300;
        int x = (parent.getBaseWidth() - width) / 2;
        int y = (parent.getBaseHeight() - height) / 2;

        int buttonY = y + height - 30;
        int buttonWidth = 100;

        depositButton = new GuiCustomButton(201, x + (width/2) - buttonWidth - 5, buttonY, buttonWidth, 20, "Пожертвовать");
        withdrawButton = new GuiCustomButton(202, x + (width/2) + 5, buttonY, buttonWidth, 20, "Снять средства");
        closeGoalButton = new GuiCustomButton(200, x + (width/2) - (buttonWidth/2), buttonY - 25, buttonWidth, 20, "Завершить сбор");

        buttonList.add(depositButton);
        buttonList.add(withdrawButton);
        buttonList.add(closeGoalButton);

        this.scroll = 0f;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, int scaledMouseX, int scaledMouseY, float partialTicks) {
        int width = 400;
        int height = 300;
        int x = (parent.getBaseWidth() - width) / 2;
        int y = (parent.getBaseHeight() - height) / 2;

        UtilO.drawRoundedRectangle(x, y, width, height, 7, new Color(10, 10, 10, 220).getRGB(), 0);

        depositButton.visible = true;
        withdrawButton.visible = parent.isPlayerLeader();
        closeGoalButton.visible = !isMainTreasury && goal != null && parent.isPlayerLeader() && goal.isComplete();

        int listY, listHeight;

        if (isMainTreasury) {
            parent.drawCenteredString("§lОсновная казна", x + width / 2, y + 15, 0xFFD700);
            if(parent.getFactionData() != null)
                parent.drawCenteredString("§e" + parent.getFactionData().getTreasury(), x + width/2, y + 30, 0xFFFFFF);
            parent.drawString("История транзакций:", x + 15, y + 50, 0xAAAAAA);
            listY = y + 65;
            listHeight = height - 105;
        } else if (goal != null) {
            parent.drawCenteredString("§l" + goal.getName(), x + width / 2, y + 15, 0xFFFFFF);
            drawProgressBar(goal, x + 15, y + 35, width - 30);
            parent.drawString("История транзакций:", x + 15, y + 55, 0xAAAAAA);
            listY = y + 70;
            listHeight = height - 110 - (closeGoalButton.visible ? 25 : 0);
        } else {
            return;
        }

        int listX = x + 15;
        int listWidth = width - 30;

        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        GuiApi.glScissor(parent.getGuiLeft() + (int)(listX * parent.getScaleFactor()), parent.getGuiTop() + (int)(listY * parent.getScaleFactor()), (int)(listWidth * parent.getScaleFactor()), (int)(listHeight * parent.getScaleFactor()), true);


        int totalContentHeight = history.size() * 20;
        int scrollOffset = (totalContentHeight > listHeight) ? (int)(this.scroll * (totalContentHeight - listHeight)) : 0;

        if (history.isEmpty()) {
            parent.drawCenteredString("§7История пуста.", x + width / 2, y + height / 2, 0xAAAAAA);
        } else {
            for (int i = 0; i < history.size(); i++) {
                int currentY = listY + i * 20 - scrollOffset;
                if (currentY + 20 < listY || currentY > listY + listHeight) continue;

                CollectionGoal.Transaction trans = history.get(i);
                String amountStr = (trans.getAmount() > 0 ? "§a+" : (trans.getAmount() < 0 ? "§c" : "§7")) + trans.getAmount();
                String text = trans.getAmount() == 0 ? "Сбор создан" : trans.getPlayerName();

                parent.drawString(trans.getFormattedDate() + " - " + text, listX + 5, currentY + 6, 0xFFFFFF);
                if(trans.getAmount() != 0) {
                    parent.getFontRenderer().drawString(amountStr, listX + listWidth - parent.getFontRenderer().getStringWidth(amountStr) - 5, currentY + 6, 0xFFFFFF);
                }
            }
        }
        GL11.glDisable(GL11.GL_SCISSOR_TEST);
    }

    private void drawProgressBar(CollectionGoal goal, int x, int y, int width) {
        float progress = goal.getTargetAmount() > 0 ? (float)goal.getCurrentAmount() / (float)goal.getTargetAmount() : 0;
        progress = MathHelper.clamp_float(progress, 0, 1);
        int progressWidth = (int)(width * progress);
        String progressText = goal.getCurrentAmount() + " / " + goal.getTargetAmount();

        Gui.drawRect(x, y, x + width, y + 10, 0xFF202020);
        Gui.drawRect(x, y, x + progressWidth, y + 10, goal.isComplete() ? 0xFF55FF55 : 0xFFFFAA00);
        parent.drawCenteredString(progressText, x + width / 2, y + 1, 0xFFFFFF);
    }

    @Override
    public void actionPerformed(GuiButton button) {
        TreasuryActionOverlay overlay = (TreasuryActionOverlay) parent.getOverlay(GOTGuiFactions.Overlay.TREASURY_ACTION);
        overlay.treasuryGoalTarget = isMainTreasury || goal == null ? "" : goal.getName();

        if (button == depositButton) {
            overlay.setActionType("deposit");
            parent.setCurrentOverlay(GOTGuiFactions.Overlay.TREASURY_ACTION, true);
        } else if (button == withdrawButton) {
            overlay.setActionType("withdraw");
            parent.setCurrentOverlay(GOTGuiFactions.Overlay.TREASURY_ACTION, true);
        } else if (button == closeGoalButton && !isMainTreasury && goal != null) {
            brain.factions.servers.CoreFaction.brainChannel.sendToServer(PacketFactionManage.closeCollectionGoal(goal.getName()));
            parent.setCurrentOverlay(GOTGuiFactions.Overlay.COLLECTIONS_LIST, true);
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int scaledMouseX, int scaledMouseY, int button) {}

    @Override
    public void keyTyped(char c, int key) {}

    @Override
    public void handleMouseInput() {
        int dWheel = Mouse.getEventDWheel();
        if (dWheel != 0) {
            int listHeight = 300 - 85 - (isMainTreasury ? 0 : 25);
            int totalContentHeight = history.size() * 20;
            if (totalContentHeight > listHeight) {
                float amount = (dWheel > 0 ? -1 : 1) * 20.0f / (totalContentHeight - listHeight);
                this.scroll = MathHelper.clamp_float(this.scroll + amount, 0.0f, 1.0f);
            }
        }
    }

    @Override public void update() {}
    @Override public boolean isTextFieldFocused() { return false; }
}