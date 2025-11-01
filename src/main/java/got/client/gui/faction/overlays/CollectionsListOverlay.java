package got.client.gui.faction.overlays;

import brain.factions.Faction;
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
import java.util.List;

public class CollectionsListOverlay implements IOverlayRenderer {

    private final GOTGuiFactions parent;
    private List<Object> financeTargets = new ArrayList<>();
    private GuiButton buttonNewCollection;
    private float scroll;

    public CollectionsListOverlay(GOTGuiFactions parent) {
        this.parent = parent;
    }

    @Override
    public void initGui(List<GuiButton> buttonList) {
        Faction factionData = parent.getFactionData();
        financeTargets.clear();
        financeTargets.add("treasury");

        if (factionData != null) {
            financeTargets.addAll(factionData.getCollectionGoals());
        }
        this.scroll = 0f;

        int width = 350;
        int x = (parent.getBaseWidth() - width) / 2;
        int y = (parent.getBaseHeight() - 250) / 2;
        buttonNewCollection = new GuiCustomButton(11, x + width - 115, y + 10, 100, 20, "Новый сбор");
        buttonList.add(buttonNewCollection);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, int scaledMouseX, int scaledMouseY, float partialTicks) {
        int width = 350;
        int height = 250;
        int x = (parent.getBaseWidth() - width) / 2;
        int y = (parent.getBaseHeight() - height) / 2;

        UtilO.drawRoundedRectangle(x, y, width, height, 7, new Color(10, 10, 10, 220).getRGB(), 0);
        parent.drawCenteredString("Казна и сборы", x + width / 2, y + 15, 0xFFFFFF);

        buttonNewCollection.visible = parent.isPlayerLeader();

        int listX = x + 15;
        int listY = y + 45;
        int listWidth = width - 30;
        int listHeight = height - 60;

        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        GuiApi.glScissor(parent.getGuiLeft() + (int)(listX * parent.getScaleFactor()), parent.getGuiTop() + (int)(listY * parent.getScaleFactor()), (int)(listWidth * parent.getScaleFactor()), (int)(listHeight * parent.getScaleFactor()), true);


        int totalContentHeight = financeTargets.size() * 40;
        int scrollOffset = (totalContentHeight > listHeight) ? (int)(this.scroll * (totalContentHeight - listHeight)) : 0;

        for (int i = 0; i < financeTargets.size(); i++) {
            int currentY = listY + i * 40 - scrollOffset;
            if (currentY + 40 < listY || currentY > listY + listHeight) continue;

            Object target = financeTargets.get(i);
            boolean hovered = parent.isHover(listX, currentY, listWidth, 35, scaledMouseX, scaledMouseY);
            UtilO.drawRoundedRectangle(listX, currentY, listWidth, 35, 5, hovered ? new Color(70, 70, 70, 150).getRGB() : new Color(40, 40, 40, 150).getRGB(), 0);

            if (target instanceof String) {
                parent.drawString("§lОсновная казна", listX + 10, currentY + 8, 0xFFD700);
                parent.drawString("§e" + parent.getFactionData().getTreasury(), listX + 10, currentY + 20, 0xFFFFFF);
            } else {
                CollectionGoal goal = (CollectionGoal) target;
                parent.drawString("§l" + goal.getName(), listX + 10, currentY + 5, 0xFFFFFF);
                drawProgressBar(goal, listX + 10, currentY + 18, listWidth - 20);
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
        if (button == buttonNewCollection) {
            parent.setCurrentOverlay(GOTGuiFactions.Overlay.CREATE_COLLECTION, true);
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int scaledMouseX, int scaledMouseY, int button) {
        if (button == 0) {
            int width = 350;
            int x = (parent.getBaseWidth() - width) / 2;
            int y = (parent.getBaseHeight() - 250) / 2;
            int listX = x + 15;
            int listY = y + 45;
            int listWidth = width - 30;
            int listHeight = 250 - 60;

            if (parent.isHover(listX, listY, listWidth, listHeight, scaledMouseX, scaledMouseY)) {
                int totalContentHeight = financeTargets.size() * 40;
                int scrollOffset = (totalContentHeight > listHeight) ? (int)(this.scroll * (totalContentHeight - listHeight)) : 0;
                int index = (scaledMouseY - listY + scrollOffset) / 40;

                if (index >= 0 && index < financeTargets.size()) {
                    Object target = financeTargets.get(index);
                    if (target instanceof String) {
                        parent.selectedCollectionGoal = null;
                    } else {
                        parent.selectedCollectionGoal = (CollectionGoal) target;
                    }
                    parent.setCurrentOverlay(GOTGuiFactions.Overlay.TREASURY_DETAILS, true);
                }
            }
        }
    }

    @Override public void keyTyped(char c, int key) {}
    @Override public void handleMouseInput() {
        int dWheel = Mouse.getEventDWheel();
        if (dWheel != 0) {
            int listHeight = 250 - 60;
            int totalContentHeight = financeTargets.size() * 40;
            if (totalContentHeight > listHeight) {
                float amount = (dWheel > 0 ? -1 : 1) * 20.0f / (totalContentHeight - listHeight);
                this.scroll = MathHelper.clamp_float(this.scroll + amount, 0.0f, 1.0f);
            }
        }
    }
    @Override public void update() {}
    @Override public boolean isTextFieldFocused() { return false; }
}