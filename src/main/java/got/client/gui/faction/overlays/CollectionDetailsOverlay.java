package got.client.gui.faction.overlays;

import brain.factions.network.PacketFactionManage;
import brain.factions.servers.CollectionGoal;
import got.client.gui.faction.GOTGuiFactions;
import got.client.gui.faction.GuiCustomButton;
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

public class CollectionDetailsOverlay implements IOverlayRenderer {

    private final GOTGuiFactions parent;
    private CollectionGoal goal;
    private List<CollectionGoal.Transaction> history = new ArrayList<>();
    private GuiButton closeGoalButton;
    private float scroll;

    public CollectionDetailsOverlay(GOTGuiFactions parent) {
        this.parent = parent;
    }

    @Override
    public void initGui(List<GuiButton> buttonList) {
        this.goal = parent.selectedCollectionGoal;
        if (this.goal != null) {
            this.history = new ArrayList<>(this.goal.getHistory());
            Collections.reverse(this.history);
        } else {
            this.history.clear();
        }

        int width = 400;
        int x = (parent.getBaseWidth() - width) / 2;
        int y = (parent.getBaseHeight() - 300) / 2;

        closeGoalButton = new GuiCustomButton(200, x + width - 115, y + 10, 100, 20, "Завершить сбор");
        buttonList.add(closeGoalButton);
        this.scroll = 0f;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, int scaledMouseX, int scaledMouseY, float partialTicks) {
        if (goal == null) return;

        int width = 400;
        int height = 300;
        int x = (parent.getBaseWidth() - width) / 2;
        int y = (parent.getBaseHeight() - height) / 2;

        UtilO.drawRoundedRectangle(x, y, width, height, 7, new Color(10, 10, 10, 220).getRGB(), 0);
        parent.drawCenteredString("§l" + goal.getName(), x + width / 2, y + 15, 0xFFFFFF);

        closeGoalButton.visible = parent.isPlayerLeader() && goal.isComplete();

        drawProgressBar(goal, x + 15, y + 35, width - 30);
        parent.drawString("История транзакций:", x + 15, y + 55, 0xAAAAAA);

        int listX = x + 15;
        int listY = y + 70;
        int listWidth = width - 30;
        int listHeight = height - 85;

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
                String amountStr = (trans.getAmount() > 0 ? "§a+" : "§c") + trans.getAmount();
                parent.drawString(trans.getFormattedDate() + " - " + trans.getPlayerName(), listX + 5, currentY + 6, 0xFFFFFF);
                parent.getFontRenderer().drawString(amountStr, listX + listWidth - parent.getFontRenderer().getStringWidth(amountStr) - 5, currentY + 6, 0xFFFFFF);
            }
        }
        GL11.glDisable(GL11.GL_SCISSOR_TEST);
    }

    private void drawProgressBar(CollectionGoal goal, int x, int y, int width) {
        float progress = (float)goal.getCurrentAmount() / (float)goal.getTargetAmount();
        progress = MathHelper.clamp_float(progress, 0, 1);
        int progressWidth = (int)(width * progress);
        String progressText = goal.getCurrentAmount() + " / " + goal.getTargetAmount();

        Gui.drawRect(x, y, x + width, y + 10, 0xFF202020);
        Gui.drawRect(x, y, x + progressWidth, y + 10, goal.isComplete() ? 0xFF55FF55 : 0xFFFFAA00);
        parent.drawCenteredString(progressText, x + width / 2, y + 1, 0xFFFFFF);
    }

    @Override
    public void actionPerformed(GuiButton button) {
        if (button == closeGoalButton && goal != null) {
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
            int listHeight = 300 - 85;
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