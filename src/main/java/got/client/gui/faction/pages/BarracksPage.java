package got.client.gui.faction.pages;

import brain.factions.Faction;
import brain.factions.network.PacketFactionManage;
import brain.factions.network.PacketMessage;
import brain.factions.servers.BarracksManager;
import got.GOT;
import got.client.gui.faction.GOTGuiFactions;
import got.client.gui.faction.overlays.AddBarracksPlayerOverlay;
import got.client.gui.utils.GuiApi;
import got.client.utils.UtilO;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import org.lwjgl.opengl.GL11;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class BarracksPage {
    /*private final GOTGuiFactions parent;
    private GuiButton buttonAddBarracksPlayer, buttonIncreaseCapacity, buttonKickPlayer;
    private List<BarracksManager.PlayerProfile> barracksPlayers = new ArrayList<>();

    public BarracksPage(GOTGuiFactions parent) {
        this.parent = parent;
    }

    @Override
    public void onOpened() {
        brain.factions.servers.CoreFaction.brainChannel.sendToServer(new PacketMessage("requestBarracksPlayers#" + GOTGuiFactions.currentFaction.codeName()));
    }

    public void receiveBarracksPlayers(List<BarracksManager.PlayerProfile> players) {
        this.barracksPlayers = players != null ? players : new ArrayList<>();
    }

    @Override
    public void initGui(List<GuiButton> buttonList) {
        buttonAddBarracksPlayer = new GuiCustomButton(39, 0, 0, 150, 20, "Добавить игрока");
        buttonIncreaseCapacity = new GuiCustomButton(40, 0, 0, 150, 20, "Увеличить вместимость");
        buttonKickPlayer = new GuiCustomButton(100, 0, 0, 100, 20, "Исключить");

        buttonList.add(buttonAddBarracksPlayer);
        buttonList.add(buttonIncreaseCapacity);
        buttonList.add(buttonKickPlayer);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        parent.drawCenteredString("Казарма", parent.width / 2, parent.getGuiTop() + 15, 0xFFFFFF);

        Faction factionData = parent.getFactionData();
        if (factionData == null) return;

        int x = parent.getGuiLeft() + 25;
        int y = parent.getGuiTop() + 40;
        int listWidth = parent.getXSize() - 200;
        int listHeight = parent.getYSize() - 80;

        parent.drawString("Игроков: §e" + barracksPlayers.size() + "/" + factionData.getBarracksCapacity(), x, y - 15, 0xFFFFFF);

        buttonAddBarracksPlayer.xPosition = x + listWidth + 20;
        buttonAddBarracksPlayer.yPosition = y;
        buttonAddBarracksPlayer.visible = true;
        buttonAddBarracksPlayer.enabled = barracksPlayers.size() < factionData.getBarracksCapacity() && parent.isPlayerLeader();

        buttonIncreaseCapacity.xPosition = x + listWidth + 20;
        buttonIncreaseCapacity.yPosition = y + 25;
        buttonIncreaseCapacity.visible = true;
        buttonIncreaseCapacity.enabled = parent.getPlayerStatus() == GOTGuiFactions.GroupStatus.OWNER;

        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        GuiApi.glScissor(x, y, listWidth, listHeight, false);

        for(int i=0; i < barracksPlayers.size(); i++) {
            int currentY = y + i * 20;
            BarracksManager.PlayerProfile player = barracksPlayers.get(i);

            if(parent.isHover(x, currentY, listWidth, 20)) {
                Gui.drawRect(x, currentY, x + listWidth, currentY + 20, 0x50FFFFFF);
            }
            parent.drawString(player.getName(), x + 5, currentY + 6, 0xFFFFFF);
        }

        GL11.glDisable(GL11.GL_SCISSOR_TEST);

        drawBarracksContextMenu();
    }

    private void drawBarracksContextMenu() {
        if (parent.contextMenuObject instanceof BarracksManager.PlayerProfile) {
            String playerName = ((BarracksManager.PlayerProfile) parent.contextMenuObject).getName();
            int x = parent.contextMenuX;
            int y = parent.contextMenuY;
            int menuWidth = 110;
            int menuHeight = 48;

            if (x + menuWidth > parent.getGuiLeft() + parent.getXSize()) x -= menuWidth;
            if (y + menuHeight > parent.getGuiTop() + parent.getYSize()) y -= menuHeight;

            UtilO.drawRoundedRectangle(x, y, menuWidth, menuHeight, 5, new Color(20, 20, 20, 220).getRGB(), 0);
            parent.drawCenteredString(playerName, x + menuWidth / 2, y + 5, 0xFFFFFF);

            buttonKickPlayer.xPosition = x + 5;
            buttonKickPlayer.yPosition = y + 20;
            buttonKickPlayer.visible = true;
        }
    }

    @Override
    public void actionPerformed(GuiButton button) {
        if (button == buttonAddBarracksPlayer) {
            Faction factionData = parent.getFactionData();
            if (factionData != null) {
                AddBarracksPlayerOverlay overlay = (AddBarracksPlayerOverlay) parent.getOverlay(GOTGuiFactions.Overlay.ADD_BARRACKS_PLAYER);
                overlay.setAvailablePlayers(factionData.getPlayers().keySet(), this.barracksPlayers);
                parent.setCurrentOverlay(GOTGuiFactions.Overlay.ADD_BARRACKS_PLAYER, true);
            }
        } else if (button == buttonIncreaseCapacity) {
            parent.setCurrentOverlay(GOTGuiFactions.Overlay.INCREASE_BARRACKS_CAPACITY, true);
        } else if (button == buttonKickPlayer && parent.contextMenuObject instanceof BarracksManager.PlayerProfile) {
            String name = ((BarracksManager.PlayerProfile) parent.contextMenuObject).getName();
            brain.factions.servers.CoreFaction.brainChannel.sendToServer(PacketFactionManage.barracksAction("remove_player", name, String.valueOf(0)));
            parent.contextMenuObject = null;
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int button) {
        if (button == 1 && parent.isPlayerLeader()) {
            int listX = parent.getGuiLeft() + 25;
            int listY = parent.getGuiTop() + 40;
            int listWidth = parent.getXSize() - 200;
            int listHeight = parent.getYSize() - 80;

            if(parent.isHover(listX, listY, listWidth, listHeight)){
                int index = (mouseY - listY) / 20;
                if(index >= 0 && index < barracksPlayers.size()){
                    parent.contextMenuObject = barracksPlayers.get(index);
                    parent.contextMenuX = mouseX;
                    parent.contextMenuY = mouseY;
                }
            }
        } else if (button == 0) {
            parent.contextMenuObject = null;
        }
    }

    @Override public void keyTyped(char c, int key) {}
    @Override public void handleMouseInput() {}
    @Override public void update() {}
    @Override public void onGuiClosed() {}
    @Override
    public boolean isTextFieldFocused() {
        return false;
   */// }
}