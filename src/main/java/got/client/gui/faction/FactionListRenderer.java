package got.client.gui.faction;

import got.client.gui.faction.pages.IPageRenderer;
import got.client.gui.utils.GuiApi;
import got.client.utils.UtilO;
import got.common.faction.GOTFaction;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.MathHelper;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import java.awt.Color;
import java.util.List;

public class FactionListRenderer implements IPageRenderer {

    private final GOTGuiFactions parent;
    private float currentScroll = 0.0F;
    private boolean isScrolling = false;

    public FactionListRenderer(GOTGuiFactions parent) {
        this.parent = parent;
    }

    @Override
    public void initGui(List<GuiButton> buttonList) {}

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        parent.drawCenteredString("Фракции", parent.width / 2, parent.getGuiTop() + 15, 0xFFFFFF);

        int listX = parent.getGuiLeft() + 15;
        int listY = parent.getGuiTop() + 40;
        int listWidth = parent.getXSize() - 40;
        int listHeight = parent.getYSize() - 85;

        int columns = 4;
        int boxWidth = (listWidth - (columns - 1) * 10) / columns;
        int boxHeight = 50;
        int rowGap = 10;

        int totalRows = (int)Math.ceil((double)parent.currentFactionList.size() / columns);
        int totalContentHeight = totalRows * (boxHeight + rowGap) - rowGap;

        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        GuiApi.glScissor(listX, listY, listWidth + 10, listHeight, false);

        int scrollOffset = (int)(this.currentScroll * (totalContentHeight - listHeight));

        for (int i = 0; i < parent.currentFactionList.size(); i++) {
            int row = i / columns;
            int y = listY + row * (boxHeight + rowGap) - scrollOffset;

            if (y + boxHeight < listY || y > listY + listHeight) continue;

            int col = i % columns;
            int x = listX + col * (boxWidth + 10);

            GOTFaction faction = parent.currentFactionList.get(i);
            boolean isHovered = parent.isHover(x, y, boxWidth, boxHeight);
            int color = isHovered ? new Color(70, 70, 70, 200).getRGB() : new Color(40, 40, 40, 150).getRGB();
            UtilO.drawRoundedRectangle(x, y, boxWidth, boxHeight, 5, color, 0);

            parent.drawCenteredString(faction.factionName(), x + boxWidth / 2, y + 20, faction.getFactionColor());
        }

        GL11.glDisable(GL11.GL_SCISSOR_TEST);

        if (totalContentHeight > listHeight) {
            int scrollBarX = listX + listWidth + 5;
            int scrollBarHeight = listHeight;
            int scrollWidgetHeight = Math.max(10, (int)((float)listHeight / (float)totalContentHeight * (float)scrollBarHeight));

            Gui.drawRect(scrollBarX, listY, scrollBarX + 5, listY + scrollBarHeight, 0x80000000);
            int scrollWidgetY = listY + (int)(this.currentScroll * (scrollBarHeight - scrollWidgetHeight));
            Gui.drawRect(scrollBarX, scrollWidgetY, scrollBarX + 5, scrollWidgetY + scrollWidgetHeight, 0xFFC0C0C0);
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int button) {
        if (button == 0) {
            int listX = parent.getGuiLeft() + 15;
            int listY = parent.getGuiTop() + 40;
            int listWidth = parent.getXSize() - 40;
            int listHeight = parent.getYSize() - 85;

            if (mouseX >= listX + listWidth + 5 && mouseX < listX + listWidth + 10) {
                isScrolling = true;
            }

            if (parent.isHover(listX, listY, listWidth, listHeight)) {
                int columns = 4;
                int boxWidth = (listWidth - (columns - 1) * 10) / columns;
                int boxHeight = 50;
                int rowGap = 10;
                int totalRows = (int)Math.ceil((double)parent.currentFactionList.size() / columns);
                int totalContentHeight = totalRows * (boxHeight + rowGap) - rowGap;
                int scrollOffset = (totalContentHeight > listHeight) ? (int)(this.currentScroll * (totalContentHeight - listHeight)) : 0;

                for (int i = 0; i < parent.currentFactionList.size(); i++) {
                    int col = i % columns;
                    int row = i / columns;
                    int boxX = listX + col * (boxWidth + 10);
                    int boxY = listY + row * (boxHeight + rowGap) - scrollOffset;

                    if (parent.isHover(boxX, boxY, boxWidth, boxHeight)) {
                        parent.setCurrentFaction(parent.currentFactionList.get(i), GOTGuiFactions.View.FACTION);
                        return;
                    }
                }
            }
        }
    }

    @Override
    public void handleMouseInput() {
        int scroll = Mouse.getEventDWheel();
        if (scroll != 0) {
            int listHeight = parent.getYSize() - 85;
            int columns = 4;
            int boxHeight = 50;
            int rowGap = 10;
            int totalRows = (int)Math.ceil((double)parent.currentFactionList.size() / columns);
            int totalContentHeight = totalRows * (boxHeight + rowGap) - rowGap;

            if (totalContentHeight > listHeight) {
                float amount = (scroll > 0 ? -1 : 1) * 20.0f / (totalContentHeight - listHeight);
                this.currentScroll = MathHelper.clamp_float(this.currentScroll + amount, 0.0F, 1.0F);
            }
        }
    }

    @Override public void onOpened() { currentScroll = 0.0f; }
    @Override public void update() {}
    @Override public void keyTyped(char c, int key) {}
    @Override public void actionPerformed(GuiButton button) {}
    @Override public void onGuiClosed() {}
    @Override
    public boolean isTextFieldFocused() {
        return false;
    }
}