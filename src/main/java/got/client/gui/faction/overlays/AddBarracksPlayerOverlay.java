package got.client.gui.faction.overlays;

import brain.factions.Faction;
import brain.factions.network.PacketFactionManage;
import brain.factions.servers.BarracksManager;
import got.GOT;
import got.client.gui.faction.GOTGuiFactions;
import got.client.gui.utils.GuiApi;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.MathHelper;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import java.util.List;
import java.util.stream.Collectors;
import java.util.ArrayList;
import java.util.Set;

public class AddBarracksPlayerOverlay implements IOverlayRenderer {
    private final GOTGuiFactions parent;
    private GuiButton buttonCancel;
    private float playerListScroll = 0.0f;
    private List<String> availablePlayers = new ArrayList<>();
    private String structureId;

    public void setAvailablePlayers(Set<String> allPlayers, List<BarracksManager.PlayerProfile> barracksPlayers) {
        Set<String> barracksPlayerNames = barracksPlayers.stream()
                .map(BarracksManager.PlayerProfile::getName)
                .collect(Collectors.toSet());

        this.availablePlayers = allPlayers.stream()
                .filter(name -> !barracksPlayerNames.contains(name))
                .sorted()
                .collect(Collectors.toList());
    }

    public void setStructureId(String id) {
        this.structureId = id;
    }

    public AddBarracksPlayerOverlay(GOTGuiFactions parent) {
        this.parent = parent;
    }

    @Override
    public void initGui(List<GuiButton> buttonList) {
        final int overlayX = parent.getGuiLeft() + parent.getXSize() / 2;
        final int overlayY = parent.getGuiTop() + parent.getYSize() / 2;
        buttonCancel = new GuiButton(42, overlayX - 50, overlayY + 70, 100, 20, "Отмена");
        buttonList.add(buttonCancel);
        playerListScroll = 0.0f;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        final int overlayX = parent.getGuiLeft() + parent.getXSize() / 2;
        final int overlayY = parent.getGuiTop() + parent.getYSize() / 2;

        parent.drawCenteredString("Добавить игрока в казарму", overlayX, overlayY - 80, 0xFFFFFF);
        buttonCancel.visible = true;

        if(availablePlayers.isEmpty()) {
            parent.drawCenteredString("§7Все игроки фракции уже в казарме", overlayX, overlayY - 40, 0xAAAAAA);
        } else {
            int listX = overlayX - 100;
            int listY = overlayY - 60;
            int listWidth = 200;
            int listHeight = 120;
            int totalContentHeight = availablePlayers.size() * 22;

            if (totalContentHeight > listHeight) {
                // скролбаррррррр
            }

            GL11.glEnable(GL11.GL_SCISSOR_TEST);
            GuiApi.glScissor(listX, listY, listWidth, listHeight, false);

            int scrollOffset = (totalContentHeight > listHeight) ? (int)(this.playerListScroll * (totalContentHeight - listHeight)) : 0;

            for (int i = 0; i < availablePlayers.size(); i++) {
                int playerY = listY + i * 22 - scrollOffset;
                if(playerY + 22 < listY || playerY > listY + listHeight) continue;

                if (parent.isHover(listX, playerY, listWidth, 20)) {
                    Gui.drawRect(listX, playerY, listX + listWidth, playerY + 20, 0x50FFFFFF);
                }
                parent.drawCenteredString(availablePlayers.get(i), overlayX, playerY + 6, 0xFFFFFF);
            }
            GL11.glDisable(GL11.GL_SCISSOR_TEST);
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int button) {
        if (button == 0 && structureId != null) {
            int listX = parent.getGuiLeft() + parent.getXSize() / 2 - 100;
            int listY = parent.getGuiTop() + parent.getYSize() / 2 - 60;
            int listWidth = 200;
            int listHeight = 120;
            int totalContentHeight = availablePlayers.size() * 22;
            int scrollOffset = (totalContentHeight > listHeight) ? (int) (playerListScroll * (totalContentHeight - listHeight)) : 0;

            if (parent.isHover(listX, listY, listWidth, listHeight)) {
                for (int i = 0; i < availablePlayers.size(); i++) {
                    int playerY = listY + i * 22 - scrollOffset;
                    if (parent.isHover(listX, playerY, listWidth, 20)) {
                        String playerName = availablePlayers.get(i);
                        brain.factions.servers.CoreFaction.brainChannel.sendToServer(PacketFactionManage.barracksAction("add_player", playerName, this.structureId));
                        parent.setCurrentOverlay(GOTGuiFactions.Overlay.NONE, true);
                        return;
                    }
                }
            }
        }
    }

    @Override
    public void actionPerformed(GuiButton button) {
        if (button == buttonCancel) {
            parent.setCurrentOverlay(GOTGuiFactions.Overlay.NONE, false);
        }
    }

    @Override
    public void handleMouseInput() {
        int k = Mouse.getEventDWheel();
        if (k != 0 && !availablePlayers.isEmpty()) {
            int listHeight = 120;
            int totalContentHeight = availablePlayers.size() * 22;
            if (totalContentHeight > listHeight) {
                float scrollAmount = (k > 0 ? -1 : 1) * 20.0f / (totalContentHeight - listHeight);
                playerListScroll = MathHelper.clamp_float(playerListScroll + scrollAmount, 0.0f, 1.0f);
            }
        }
    }

    @Override public void keyTyped(char c, int key) {}
    @Override public void update() {}
    @Override
    public boolean isTextFieldFocused() {
        return false;
    }
}