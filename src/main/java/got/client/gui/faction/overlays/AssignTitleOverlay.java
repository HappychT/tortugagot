package got.client.gui.faction.overlays;

import brain.factions.Faction;
import brain.factions.network.PacketFactionManage;
import brain.factions.servers.CoreFaction;
import got.GOT;
import got.client.gui.faction.GOTGuiFactions;
import got.client.utils.UtilO;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import java.awt.Color;
import java.util.List;
import java.util.stream.Collectors;
import java.util.ArrayList;

public class AssignTitleOverlay implements IOverlayRenderer {
    private final GOTGuiFactions parent;
    private List<String> sortedTitles = new ArrayList<>();

    public AssignTitleOverlay(GOTGuiFactions parent) {
        this.parent = parent;
    }

    @Override
    public void initGui(List<GuiButton> buttonList) {
        Faction factionData = parent.getFactionData();
        if (factionData != null) {
            sortedTitles = factionData.getTitles().values().stream()
                    .sorted((t1, t2) -> Integer.compare(t1.getHierarchy(), t2.getHierarchy()))
                    .map(Faction.Title::getName)
                    .collect(Collectors.toList());
        } else {
            sortedTitles.clear();
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        final int width = 220;
        final int height = 180;
        final int overlayX = parent.getGuiLeft() + parent.getXSize() / 2;
        final int overlayY = parent.getGuiTop() + parent.getYSize() / 2;
        final int left = overlayX - width / 2;
        final int top = overlayY - height / 2;

        UtilO.drawRoundedRectangle(left, top, width, height, 7, new Color(10, 10, 10, 220).getRGB(), 0);
        parent.drawCenteredString("Назначить титул для " + parent.selectedPlayerName, overlayX, top + 10, 0xFFFFFF);

        int titleY = top + 30;
        for (String title : sortedTitles) {
            if (parent.isHover(overlayX - 100, titleY, 200, 20)) {
                Gui.drawRect(overlayX - 100, titleY, overlayX + 100, titleY + 20, 0x50FFFFFF);
            }
            parent.drawCenteredString(title, overlayX, titleY + 6, 0xFFFFFF);
            titleY += 22;
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int button) {
        if (button == 0) {
            final int overlayX = parent.getGuiLeft() + parent.getXSize() / 2;
            final int top = parent.getGuiTop() + (parent.getYSize() - 180) / 2;

            int titleY = top + 30;
            for (String title : sortedTitles) {
                if (parent.isHover(overlayX - 100, titleY, 200, 20)) {
                    CoreFaction.brainChannel.sendToServer(PacketFactionManage.managePlayer(parent.selectedPlayerName, "setTitle", title));
                    parent.setCurrentOverlay(GOTGuiFactions.Overlay.NONE, false);
                    return;
                }
                titleY += 22;
            }
        }
    }

    @Override public void actionPerformed(GuiButton button) {}
    @Override public void keyTyped(char c, int key) {}
    @Override public void update() {}
    @Override public void handleMouseInput() {}
    @Override
    public boolean isTextFieldFocused() {
        return false;
    }
}