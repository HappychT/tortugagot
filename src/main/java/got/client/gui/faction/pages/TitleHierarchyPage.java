package got.client.gui.faction.pages;

import brain.factions.Faction;
import brain.factions.network.PacketFactionManage;
import got.client.gui.faction.GOTGuiFactions;
import got.client.gui.faction.GuiCustomButton;
import got.client.gui.utils.GuiApi;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.MathHelper;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class TitleHierarchyPage implements IPageRenderer {

    private final GOTGuiFactions parent;
    private List<String> titlesSortedByHierarchy = new ArrayList<>();
    private float titleListScroll = 0.0f;
    private GuiButton saveButton, moveUpButton, moveDownButton;

    public TitleHierarchyPage(GOTGuiFactions parent) {
        this.parent = parent;
    }

    @Override
    public void onOpened() {
        Faction factionData = parent.getFactionData();
        if (factionData != null) {
            titlesSortedByHierarchy = factionData.getTitles().values().stream()
                    .sorted(Comparator.comparingInt(Faction.Title::getHierarchy))
                    .map(Faction.Title::getName)
                    .collect(Collectors.toList());
        } else {
            titlesSortedByHierarchy.clear();
        }
        titleListScroll = 0f;
    }

    @Override
    public void initGui(List<GuiButton> buttonList) {
        int xSize = parent.getBaseWidth();
        int ySize = parent.getBaseHeight();

        saveButton = new GuiCustomButton(150, xSize / 2 - 50, ySize - 60, 100, 20, "Сохранить");
        moveUpButton = new GuiCustomButton(151, 0, 0, 20, 20, "▲");
        moveDownButton = new GuiCustomButton(152, 0, 0, 20, 20, "▼");

        buttonList.add(saveButton);
        buttonList.add(moveUpButton);
        buttonList.add(moveDownButton);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, int scaledMouseX, int scaledMouseY, float partialTicks) {
        parent.drawCenteredString("Иерархия титулов", parent.getBaseWidth() / 2, 15, 0xFFFFFF);
        saveButton.visible = true;
        moveUpButton.visible = false;
        moveDownButton.visible = false;

        int listX = 25;
        int listY = 40;
        int listWidth = parent.getBaseWidth() - 50;
        int listHeight = parent.getBaseHeight() - 110;

        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        GuiApi.glScissor(parent.getGuiLeft() + (int)(listX * parent.getScaleFactor()), parent.getGuiTop() + (int)(listY * parent.getScaleFactor()), (int)(listWidth * parent.getScaleFactor()), (int)(listHeight * parent.getScaleFactor()), true);

        int totalContentHeight = titlesSortedByHierarchy.size() * 22;
        int scrollOffset = (totalContentHeight > listHeight) ? (int)(this.titleListScroll * (totalContentHeight - listHeight)) : 0;

        for (int i = 0; i < titlesSortedByHierarchy.size(); i++) {
            int drawY = listY + i * 22 - scrollOffset;
            if (drawY + 22 < listY || drawY > listY + listHeight) continue;

            String title = titlesSortedByHierarchy.get(i);

            boolean isHovering = parent.isHover(listX, drawY, listWidth, 20, scaledMouseX, scaledMouseY);
            if (isHovering) {
                Gui.drawRect(listX, drawY, listX + listWidth, drawY + 20, 0x50FFFFFF);
            }
            parent.drawString(title, listX + 10, drawY + 6, 0xFFFFFF);

            if(isHovering){
                moveUpButton.xPosition = listX + listWidth - 50;
                moveUpButton.yPosition = drawY;
                moveUpButton.enabled = (i > 0);
                moveUpButton.visible = true;

                moveDownButton.xPosition = listX + listWidth - 25;
                moveDownButton.yPosition = drawY;
                moveDownButton.enabled = (i < titlesSortedByHierarchy.size() - 1);
                moveDownButton.visible = true;
            }
        }
        GL11.glDisable(GL11.GL_SCISSOR_TEST);
    }

    @Override
    public void actionPerformed(GuiButton button) {
        if (button == saveButton) {
            brain.factions.servers.CoreFaction.brainChannel.sendToServer(PacketFactionManage.manageTitleHierarchy(titlesSortedByHierarchy));
            parent.setCurrentPage(GOTGuiFactions.Page.PLAYER_LIST);
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int scaledMouseX, int scaledMouseY, int button) {
        if(button == 0) {
            int listX = 25;
            int listY = 40;
            int listWidth = parent.getBaseWidth() - 50;
            int listHeight = parent.getBaseHeight() - 110;

            if(parent.isHover(listX, listY, listWidth, listHeight, scaledMouseX, scaledMouseY)){
                int totalContentHeight = titlesSortedByHierarchy.size() * 22;
                int scrollOffset = (totalContentHeight > listHeight) ? (int)(this.titleListScroll * (totalContentHeight - listHeight)) : 0;
                int index = (scaledMouseY - listY + scrollOffset) / 22;

                if (index >= 0 && index < titlesSortedByHierarchy.size()) {
                    int drawY = listY + index * 22 - scrollOffset;
                    if (parent.isHover(listX + listWidth - 50, drawY, 20, 20, scaledMouseX, scaledMouseY) && index > 0) {
                        Collections.swap(titlesSortedByHierarchy, index, index - 1);
                    }
                    if (parent.isHover(listX + listWidth - 25, drawY, 20, 20, scaledMouseX, scaledMouseY) && index < titlesSortedByHierarchy.size() - 1) {
                        Collections.swap(titlesSortedByHierarchy, index, index + 1);
                    }
                }
            }
        }
    }

    @Override
    public void handleMouseInput() {
        int k = Mouse.getEventDWheel();
        if (k != 0) {
            int listHeight = parent.getBaseHeight() - 110;
            int totalContentHeight = titlesSortedByHierarchy.size() * 22;
            if (totalContentHeight > listHeight) {
                float scrollAmount = (k > 0 ? -1 : 1) * 20.0f / (totalContentHeight - listHeight);
                titleListScroll = MathHelper.clamp_float(titleListScroll + scrollAmount, 0.0f, 1.0f);
            }
        }
    }

    @Override public void keyTyped(char c, int key) {}
    @Override public void onGuiClosed() {}
    @Override public void update() {}
    @Override
    public boolean isTextFieldFocused() {
        return false;
    }
}