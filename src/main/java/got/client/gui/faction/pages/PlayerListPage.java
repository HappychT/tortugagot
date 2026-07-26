package got.client.gui.faction.pages;

import brain.factions.Faction;
import brain.factions.network.PacketFactionManage;
import got.client.gui.faction.GOTGuiFactions;
import got.client.gui.faction.GuiCustomButton;
import got.client.gui.faction.GuiTexturedButton;
import got.client.gui.utils.GuiApi;
import got.client.utils.UtilO;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import java.awt.Color;
import java.util.*;
import java.util.stream.Collectors;

public class PlayerListPage implements IPageRenderer {
    private final GOTGuiFactions parent;
    private GuiButton buttonManageTitles, buttonTitleHierarchy, buttonApplications;
    private GuiButton buttonPlayerKick, buttonPlayerSetTitle;
    private List<Map.Entry<String, Faction.PlayerData>> sortedPlayers = new ArrayList<>();
    private float scroll;

    public PlayerListPage(GOTGuiFactions parent) {
        this.parent = parent;
    }

    @Override
    public void onOpened() {
        Faction factionData = parent.getFactionData();
        if (factionData != null) {
            sortedPlayers = factionData.getPlayers().entrySet().stream()
                    .sorted(Comparator.comparing(e -> e.getValue().getTitle()))
                    .collect(Collectors.toList());
        } else {
            sortedPlayers.clear();
        }
        scroll = 0f;
    }

    @Override
    public void initGui(List<GuiButton> buttonList) {
        int frameWidth = 168;
        int buttonHeight = 63;
        int rightButtonsX = parent.getBaseWidth() - 65 - frameWidth;
        int rightButtonsY = 60;
        int gap = 10;

        buttonManageTitles = new GuiTexturedButton(16, rightButtonsX, rightButtonsY, frameWidth, buttonHeight, "prefixmanage_button");
        buttonTitleHierarchy = new GuiTexturedButton(17, rightButtonsX, rightButtonsY + buttonHeight + gap, frameWidth, buttonHeight, "prefixinfo_button");
        buttonApplications = new GuiTexturedButton(18, rightButtonsX, rightButtonsY + 2 * (buttonHeight + gap), frameWidth, buttonHeight, "request_button");
        buttonPlayerKick = new GuiCustomButton(100, 0, 0, 100, 20, "Выгнать");
        buttonPlayerSetTitle = new GuiCustomButton(101, 0, 0, 100, 20, "Назначить титул");

        buttonList.add(buttonManageTitles);
        buttonList.add(buttonTitleHierarchy);
        buttonList.add(buttonApplications);
        buttonList.add(buttonPlayerKick);
        buttonList.add(buttonPlayerSetTitle);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, int scaledMouseX, int scaledMouseY, float partialTicks) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        parent.mc.getTextureManager().bindTexture(new ResourceLocation("got", "textures/gui/faction/playersinfo_title.png"));
        parent.drawScaledCustomSizeModalRect(parent.getBaseWidth() / 2 - 100, 10, 0, 0, 1474, 186, 200, 25, 1474.0F, 186.0F);

        Faction factionData = parent.getFactionData();
        boolean isLeader = parent.isPlayerLeader();

        buttonManageTitles.visible = isLeader;
        buttonTitleHierarchy.visible = isLeader;
        buttonApplications.visible = isLeader;

        if (factionData != null) {
            buttonApplications.enabled = !factionData.getApplications().isEmpty();
            if (isLeader && !factionData.getApplications().isEmpty()) {
                int rightButtonsX = parent.getBaseWidth() - 65 - 168;
                parent.drawString("§e+" + factionData.getApplications().size(), rightButtonsX + 168 - 25, 60 + 2 * (63 + 10) + 5, 0xFFFFFF);
            }
        }
        positionContextMenuButtons();

        int rightButtonsX = parent.getBaseWidth() - 65 - (int) (202 / 1.2f);
        int listX = 60;
        int listY = 55;
        int listWidth = rightButtonsX - listX - 80;
        int listHeight = 14 * 20;

        parent.mc.getTextureManager().bindTexture(new ResourceLocation("got", "textures/gui/faction/playersinfo_plate.png"));
        parent.drawScaledCustomSizeModalRect(listX - 5, listY - 20, 0, 0, 1090, 567, listWidth + 10, listHeight + 40, 1090.0F, 567.0F);
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        GuiApi.glScissor(
                parent.getGuiLeft() + (int)(listX * parent.getScaleFactor()),
                parent.getGuiTop() + (int)(listY * parent.getScaleFactor()),
                (int)(listWidth * parent.getScaleFactor()),
                (int)(listHeight * parent.getScaleFactor()),
                true
        );

        int totalHeight = sortedPlayers.size() * 20;
        int scrollOffset = (totalHeight > listHeight) ? (int)(scroll * (totalHeight - listHeight)) : 0;

        for (int i = 0; i < sortedPlayers.size(); i++) {
            int currentY = listY + i * 20 - scrollOffset;
            if (currentY + 20 < listY || currentY > listY + listHeight) continue;

            Map.Entry<String, Faction.PlayerData> entry = sortedPlayers.get(i);

            if (parent.isHover(listX, currentY, listWidth, 20, scaledMouseX, scaledMouseY)) {
                Gui.drawRect(listX, currentY, listX + listWidth, currentY + 20, 0x50FFFFFF);
            }

            parent.drawString(entry.getKey() + " (" + entry.getValue().getTitle() + ")", listX + 25, currentY + 6, 0xFFFFFFFF);
            parent.getFontRenderer().drawString("Вступил: " + parent.getDateFormat().format(new Date(entry.getValue().getJoinDate())), listX + listWidth - 175, currentY + 6, 0xAAAAAA);
        }
        GL11.glDisable(GL11.GL_SCISSOR_TEST);
    }

    public void drawPlayerContextMenuAfterButtons(int mouseX, int mouseY) {
        if (parent.contextMenuObject instanceof String) {
            String playerName = (String) parent.contextMenuObject;
            int x = parent.contextMenuX;
            int y = parent.contextMenuY;
            int menuWidth = 110;
            int menuHeight = 70;

            if (x + menuWidth > parent.getGuiLeft() + parent.getXSize()) x -= menuWidth;
            if (y + menuHeight > parent.getGuiTop() + parent.getYSize()) y -= menuHeight;

            UtilO.drawRoundedRectangle(x, y, menuWidth, menuHeight, 5, new Color(20, 20, 20, 220).getRGB(), 0);
            parent.getFontRenderer().drawString(playerName, x + (menuWidth - parent.getFontRenderer().getStringWidth(playerName)) / 2, y + 5, 0xFFFFFF);
        }
    }

    private void positionContextMenuButtons() {
        if (parent.contextMenuObject instanceof String) {
            int x = parent.contextMenuX;
            int y = parent.contextMenuY;
            int menuWidth = 110;

            if (x + menuWidth > parent.getGuiLeft() + parent.getXSize()) x -= menuWidth;
            if (y + 70 > parent.getGuiTop() + parent.getYSize()) y -= 70;

            buttonPlayerKick.xPosition = x + 5;
            buttonPlayerKick.yPosition = y + 20;
            buttonPlayerKick.visible = parent.isPlayerLeader();

            buttonPlayerSetTitle.xPosition = x + 5;
            buttonPlayerSetTitle.yPosition = y + 42;
            buttonPlayerSetTitle.visible = parent.isPlayerLeader();
        } else {
            buttonPlayerKick.visible = false;
            buttonPlayerSetTitle.visible = false;
        }
    }

    public int[] getPlayerRowBounds(String playerName) {
        int rightButtonsX = parent.getBaseWidth() - 65 - (int) (202 / 1.2f);
        int listX = 60;
        int listY = 55;
        int listWidth = rightButtonsX - listX - 80;
        int listHeight = 14 * 20;
        int totalHeight = sortedPlayers.size() * 20;
        int scrollOffset = (totalHeight > listHeight) ? (int)(scroll * (totalHeight - listHeight)) : 0;

        for (int i = 0; i < sortedPlayers.size(); i++) {
            if (sortedPlayers.get(i).getKey().equals(playerName)) {
                int currentY = listY + i * 20 - scrollOffset;
                if (currentY + 20 < listY || currentY > listY + listHeight) return null;
                return new int[]{listX, currentY, listWidth, 20};
            }
        }
        return null;
    }

    @Override
    public void actionPerformed(GuiButton button) {
        if (button == buttonManageTitles) {
            parent.setCurrentOverlay(GOTGuiFactions.Overlay.CREATE_TITLE, true);
        } else if (button == buttonTitleHierarchy) {
            parent.setCurrentPage(GOTGuiFactions.Page.TITLE_HIERARCHY);
        } else if (button == buttonApplications) {
            parent.setCurrentPage(GOTGuiFactions.Page.APPLICATIONS);
        } else if (parent.contextMenuObject instanceof String) {
            String playerName = (String) parent.contextMenuObject;
            if (button == buttonPlayerKick) {
                brain.factions.servers.CoreFaction.brainChannel.sendToServer(PacketFactionManage.managePlayer(playerName, "kick", ""));
                parent.contextMenuObject = null;
            } else if (button == buttonPlayerSetTitle) {
                parent.selectedPlayerName = playerName;
                parent.setCurrentOverlay(GOTGuiFactions.Overlay.ASSIGN_TITLE, true);
                parent.contextMenuObject = null;
            }
        }
    }

    private boolean isMouseOverButton(GuiButton button, int mouseX, int mouseY) {
        if (button == null || !button.visible) return false;
        return mouseX >= button.xPosition && mouseY >= button.yPosition && mouseX < button.xPosition + button.width && mouseY < button.yPosition + button.height;
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int scaledMouseX, int scaledMouseY, int button) {
        if (button == 1 && parent.contextMenuObject == null && parent.isPlayerLeader()) {
            int rightButtonsX = parent.getBaseWidth() - 65 - (int) (202 / 1.2f);
            int listX = 60;
            int listY = 55;
            int listWidth = rightButtonsX - listX - 80;
            int listHeight = 14 * 20;
            if (parent.isHover(listX, listY, listWidth, listHeight, scaledMouseX, scaledMouseY)) {                int totalHeight = sortedPlayers.size() * 20;
                int scrollOffset = (totalHeight > listHeight) ? (int)(scroll * (totalHeight - listHeight)) : 0;

                int index = (scaledMouseY - listY + scrollOffset) / 20;
                if (index >= 0 && index < sortedPlayers.size()) {
                    parent.contextMenuObject = sortedPlayers.get(index).getKey();
                    parent.contextMenuX = scaledMouseX;
                    parent.contextMenuY = scaledMouseY;
                }
            }
        } else if (button == 0) {
            if (parent.contextMenuObject != null && !isMouseOverButton(buttonPlayerKick, scaledMouseX, scaledMouseY) && !isMouseOverButton(buttonPlayerSetTitle, scaledMouseX, scaledMouseY)) {
                parent.contextMenuObject = null;
            }
        }
    }

    @Override
    public void handleMouseInput() {
        int k = Mouse.getEventDWheel();
        if (k != 0) {
            int listHeight = 14 * 20;
            int totalContentHeight = sortedPlayers.size() * 20;

            if (totalContentHeight > listHeight) {
                float scrollAmount = (k > 0 ? -1 : 1) * 20.0f / (totalContentHeight - listHeight);
                scroll = MathHelper.clamp_float(scroll + scrollAmount, 0.0f, 1.0f);
            }
        }
    }

    @Override public void keyTyped(char c, int key) {}
    @Override public void update() {}
    @Override public void onGuiClosed() {}
    @Override public boolean isTextFieldFocused() { return false; }
}