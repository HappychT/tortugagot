package got.client.gui.faction.pages;

import brain.factions.Faction;
import brain.factions.network.PacketMessage;
import got.client.GOTTickHandlerClient;
import got.client.gui.faction.GOTGuiFactions;
import got.client.utils.UtilO;
import got.common.GOTLevelData;
import got.common.GOTPlayerData;
import net.minecraft.client.gui.GuiButton;

import java.awt.Color;
import java.util.Collections;
import java.util.List;

public class FrontPage implements IPageRenderer {

    private final GOTGuiFactions parent;
    private GuiButton buttonApply, buttonViewMap;

    public FrontPage(GOTGuiFactions parent) {
        this.parent = parent;
    }

    @Override
    public void onOpened() {}

    @Override
    public void initGui(List<GuiButton> buttonList) {
        int guiLeft = parent.getGuiLeft();
        int guiTop = parent.getGuiTop();
        int xSize = parent.getXSize();
        int ySize = parent.getYSize();

        int buttonWidth = 150;
        int bottomRowY = guiTop + ySize - 60;

        this.buttonApply = new GuiButton(10, guiLeft + (xSize - buttonWidth) / 2, bottomRowY, buttonWidth, 20, "Подать заявку");
        this.buttonViewMap = new GuiButton(11, guiLeft + xSize - 150 - 15, guiTop + 10, buttonWidth, 20, "Дипломатия");

        buttonList.add(buttonApply);
        buttonList.add(buttonViewMap);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        parent.drawCenteredString(GOTGuiFactions.currentFaction.factionName(), parent.getGuiLeft() + parent.getXSize() / 2, parent.getGuiTop() + 15, 0xFFFFFF);
        buttonViewMap.visible = true;

        Faction factionData = parent.getFactionData();
        if (factionData == null) {
            parent.drawCenteredString("§cНет данных о фракции.", parent.width / 2, parent.height / 2, 0xFF5555);
            return;
        }

        GOTPlayerData pd = GOTLevelData.getData(parent.mc.thePlayer);

        int guiLeft = parent.getGuiLeft();
        int guiTop = parent.getGuiTop();
        int xSize = parent.getXSize();
        int ySize = parent.getYSize();
        int margin = 20;

        int bannerX = guiLeft + margin;
        int bannerY = guiTop + 40;
        int bannerWidth = (int)(xSize * 0.25);
        int bannerHeight = (int)(ySize * 0.6);

        UtilO.drawRoundedRectangle(bannerX, bannerY, bannerWidth, bannerHeight, 5, new Color(50,50,50,100).getRGB(), 0);
        parent.drawCenteredString("Знамя", bannerX + bannerWidth / 2, bannerY + bannerHeight / 2, 0xFFFFFF);

        int infoX = bannerX + bannerWidth + margin;
        int infoY = bannerY;
        parent.drawString("Лидер: §7" + (factionData.getLeaderName().isEmpty() ? "Нет" : factionData.getLeaderName()), infoX, infoY, 0xFFFFFF);
        infoY += 12;
        parent.drawString("Заместитель: §7" + (factionData.getAssistantName().isEmpty() ? "Нет" : factionData.getAssistantName()), infoX, infoY, 0xFFFFFF);
        infoY += 12;
        parent.drawString("Столица: §7" + factionData.getCapitalName(), infoX, infoY, 0xFFFFFF);
        infoY += 12;
        parent.drawString("Участников: §7" + factionData.getPlayers().size(), infoX, infoY, 0xFFFFFF);
        infoY += 12;
        parent.drawString("Казна: §e" + factionData.getTreasury(), infoX, infoY, 0xFFFFFF);

        infoY = bannerY + bannerHeight - 10;
        float alignment = pd.getAlignment(GOTGuiFactions.currentFaction);
        GOTTickHandlerClient.renderAlignmentBar(alignment, false, GOTGuiFactions.currentFaction, infoX, infoY, true, true, true, true);

        if (pd.getPledgeFaction() == null) {
            buttonApply.visible = true;
            buttonApply.enabled = alignment >= 500.0f;
            if (parent.isHover(buttonApply.xPosition, buttonApply.yPosition, buttonApply.width, buttonApply.height) && !buttonApply.enabled) {
                parent.drawTooltip(Collections.singletonList("§cВам необходимо набрать 500 репутации."), mouseX, mouseY);
            }
        } else {
            buttonApply.visible = false;
        }
    }

    @Override
    public void actionPerformed(GuiButton button) {
        if (button == buttonApply) {
            brain.factions.servers.CoreFaction.brainChannel.sendToServer(new PacketMessage("sendApplication#" + GOTGuiFactions.currentFaction.codeName()));
        } else if (button == buttonViewMap) {
            parent.setCurrentView(GOTGuiFactions.View.MAP);
        }
    }

    @Override public void mouseClicked(int mouseX, int mouseY, int button) {}
    @Override public void keyTyped(char c, int key) {}
    @Override public void handleMouseInput() {}
    @Override public void update() {}
    @Override public void onGuiClosed() {}
    @Override public boolean isTextFieldFocused() { return false; }
}