
package got.client.gui.faction;

import brain.factions.Faction;
import brain.factions.network.PacketFactionManage;
import brain.factions.network.PacketInfoFactions;
import brain.factions.network.PacketMessage;
import brain.factions.structures.FactionStructureSlot;
import got.GOT;
import got.client.gui.GOTGuiMap;
import got.client.gui.GOTGuiRendererMap;
import got.client.gui.faction.pages.IPageRenderer;
import got.client.utils.UtilO;
import got.common.faction.GOTFaction;
import got.common.faction.GOTFactionRelations;
import net.minecraft.client.gui.GuiButton;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class MapView implements IPageRenderer {

    private final GOTGuiFactions parent;
    private final GOTGuiMap mapGui;
    private final GOTGuiRendererMap mapRenderer;

    private float mapPosX = 810.0f;
    private float mapPosY = 730.0f;
    private float prevMapPosX;
    private float prevMapPosY;
    private boolean isMapDragging;
    private int mapDragMouseDownX;
    private int mapDragMouseDownY;
    private float zoomPower = -2.0f;

    private List<FactionStructureSlot> structureSlots = new ArrayList<>();
    private GuiButton[] diplomacyButtons;

    public MapView(GOTGuiFactions parent) {
        this.parent = parent;
        this.mapGui = new GOTGuiMap();
        this.mapRenderer = new GOTGuiRendererMap();
        this.mapRenderer.setSepia(true);
    }

    public void receiveStructureData(List<FactionStructureSlot> newStructures) {
        this.structureSlots = newStructures != null ? newStructures : new ArrayList<>();
    }

    @Override
    public void onOpened() {
        if (GOTGuiFactions.currentFaction != null && GOTGuiFactions.currentFaction.factionMapInfo != null) {
            this.mapPosX = GOTGuiFactions.currentFaction.factionMapInfo.mapX;
            this.mapPosY = GOTGuiFactions.currentFaction.factionMapInfo.mapY;
        }
        brain.factions.servers.CoreFaction.brainChannel.sendToServer(new PacketMessage("requestStructures"));
        this.prevMapPosX = this.mapPosX;
        this.prevMapPosY = this.mapPosY;
    }

    @Override
    public void initGui(List<GuiButton> buttonList) {
        mapGui.setWorldAndResolution(parent.mc, parent.width, parent.height);

        diplomacyButtons = new GuiButton[4];
        diplomacyButtons[0] = new GuiButton(101, 0, 0, 100, 20, "Объявить войну");
        diplomacyButtons[1] = new GuiButton(102, 0, 0, 100, 20, "Предложить союз");
        diplomacyButtons[2] = new GuiButton(103, 0, 0, 100, 20, "Предложить мир");
        diplomacyButtons[3] = new GuiButton(104, 0, 0, 100, 20, "Объявить вражду");

        for (GuiButton button : diplomacyButtons) {
            buttonList.add(button);
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        updateMapPosition(mouseX, mouseY);

        mapRenderer.zoomExp = this.zoomPower;
        mapRenderer.zoomStable = (float) Math.pow(2.0, this.zoomPower);
        mapRenderer.mapX = this.mapPosX;
        mapRenderer.mapY = this.mapPosY;
        mapRenderer.prevMapX = this.prevMapPosX;
        mapRenderer.prevMapY = this.prevMapPosY;

        mapRenderer.renderMap(parent, mapGui, partialTicks, parent.getGuiLeft(), parent.getGuiTop(), parent.getXSize(), parent.getYSize());

        drawFactions();
        drawStructures();
        drawMapContextMenu(mouseX, mouseY);
    }

    private void updateMapPosition(int mouseX, int mouseY) {
        if (this.isMapDragging) {
            if (Mouse.isButtonDown(0)) {
                float dx = (mouseX - this.mapDragMouseDownX) / mapRenderer.zoomStable;
                float dy = (mouseY - this.mapDragMouseDownY) / mapRenderer.zoomStable;
                this.mapPosX -= dx;
                this.mapPosY -= dy;
                this.mapDragMouseDownX = mouseX;
                this.mapDragMouseDownY = mouseY;
            } else {
                this.isMapDragging = false;
            }
        }
    }

    private void drawFactions() {
        for (GOTFaction faction : parent.currentFactionList) {
            if (faction.factionMapInfo != null) {
                float[] capitalPos = transformMapCoords(faction.factionMapInfo.mapX, faction.factionMapInfo.mapY);
                int capitalX = (int) capitalPos[0];
                int capitalY = (int) capitalPos[1];
                int capitalRadius = 5;

                if (isCoordsVisible(capitalX, capitalY, capitalRadius)) {
                    int baseColor = faction.getFactionColor();
                    int finalColor = (baseColor & 0x00FFFFFF) | (0x80 << 24);

                    UtilO.drawCircle2(capitalX, capitalY, capitalRadius, finalColor);
                }
            }
        }
    }

    private void drawStructures() {
        for (FactionStructureSlot slot : this.structureSlots) {
            float[] pos = transformMapCoords(slot.mapX, slot.mapY);
            int structX = (int) pos[0];
            int structY = (int) pos[1];
            int structRadius = 4;

            if (isCoordsVisible(structX, structY, structRadius)) {
                int color = slot.ownerFactionID == null ? Color.GRAY.getRGB() : Color.WHITE.getRGB();
                UtilO.drawCircle2(structX, structY, structRadius, color);

                Faction ownerFactionData = PacketInfoFactions.getFactions().get(slot.ownerFactionID);
                if (ownerFactionData != null && slot.id.equals(ownerFactionData.getMainFortressId())) {
                    UtilO.drawCircle2(structX, structY, structRadius + 2, new Color(255, 215, 0, 150).getRGB());
                }
            }
        }
    }

    private void drawMapContextMenu(int mouseX, int mouseY) {
        for (FactionStructureSlot slot : this.structureSlots) {
            float[] pos = transformMapCoords(slot.mapX, slot.mapY);
            if (isClickOnCircle(mouseX, mouseY, (int) pos[0], (int) pos[1], 4)) {
                List<String> tooltip = new ArrayList<>();
                tooltip.add(slot.name);
                if (slot.ownerFactionID != null) {
                    tooltip.add("§7Владелец: §f" + GOTFaction.forName(slot.ownerFactionID).factionName());
                } else {
                    tooltip.add("§7Ничейная территория");
                }
                parent.drawTooltip(tooltip, mouseX, mouseY);
                break;
            }
        }
        for (GOTFaction faction : parent.currentFactionList) {
            if (faction.factionMapInfo != null) {
                float[] pos = transformMapCoords(faction.factionMapInfo.mapX, faction.factionMapInfo.mapY);
                if (isClickOnCircle(mouseX, mouseY, (int) pos[0], (int) pos[1], 5)) {
                    parent.drawTooltip(java.util.Collections.singletonList(faction.factionName()), mouseX, mouseY);
                    break;
                }
            }
        }


        if (parent.contextMenuObject == null) return;

        int x = parent.contextMenuX;
        int y = parent.contextMenuY;
        int menuWidth = 110;

        if (parent.contextMenuObject instanceof GOTFaction) {
            GOTFaction fac = (GOTFaction) parent.contextMenuObject;
            int menuHeight = 110;

            if (x + menuWidth > parent.getGuiLeft() + parent.getXSize()) x -= menuWidth;
            if (y + menuHeight > parent.getGuiTop() + parent.getYSize()) y -= menuHeight;

            UtilO.drawRoundedRectangle(x, y, menuWidth, menuHeight, 5, new Color(20, 20, 20, 200).getRGB(), 0);
            parent.drawCenteredString(fac.factionName(), x + menuWidth / 2, y + 5, 0xFFFFFF);

            for(int i = 0; i < diplomacyButtons.length; i++) {
                diplomacyButtons[i].xPosition = x + 5;
                diplomacyButtons[i].yPosition = y + 20 + i * 22;
                diplomacyButtons[i].visible = true;
            }

        } else if (parent.contextMenuObject instanceof FactionStructureSlot) {
            FactionStructureSlot slot = (FactionStructureSlot) parent.contextMenuObject;
            int menuHeight = 40;

            if (x + menuWidth > parent.getGuiLeft() + parent.getXSize()) x -= menuWidth;
            if (y + menuHeight > parent.getGuiTop() + parent.getYSize()) y -= menuHeight;

            UtilO.drawRoundedRectangle(x, y, menuWidth, menuHeight, 5, new Color(20, 20, 20, 200).getRGB(), 0);
            parent.drawCenteredString(slot.name, x + menuWidth / 2, y + 5, 0xFFFFFF);
            if (slot.ownerFactionID == null) {
                parent.drawCenteredString("Ничья", x + menuWidth / 2, y + 22, 0xFFFFFF);
            } else {
                parent.drawCenteredString("Владелец: §a" + GOTFaction.forName(slot.ownerFactionID).factionName(), x + menuWidth / 2, y + 22, 0xFFFFFF);
            }
        }
    }

    @Override
    public void actionPerformed(GuiButton button) {
        if (parent.contextMenuObject == null || !(parent.contextMenuObject instanceof GOTFaction)) return;

        GOTFaction fac = (GOTFaction) parent.contextMenuObject;
        String actionType = "";

        if (button.id == 101) {
            brain.factions.servers.CoreFaction.brainChannel.sendToServer(PacketFactionManage.politicsAction("declare_war", fac.codeName()));
            parent.setCurrentView(GOTGuiFactions.View.FACTION);
            parent.setCurrentPage(GOTGuiFactions.Page.POLITICS);
            parent.contextMenuObject = null;
            return;
        } else if (button.id == 104) {
            brain.factions.servers.CoreFaction.brainChannel.sendToServer(PacketFactionManage.politicsAction("declare_hostility", fac.codeName()));
            parent.setCurrentView(GOTGuiFactions.View.FACTION);
            parent.setCurrentPage(GOTGuiFactions.Page.POLITICS);
            parent.contextMenuObject = null;
            return;
        } else if (button.id == 102) {
            actionType = GOTFactionRelations.Relation.ALLY.codeName();
        } else if (button.id == 103) {
            actionType = GOTFactionRelations.Relation.NEUTRAL.codeName();
        }

        if (!actionType.isEmpty()) {
            parent.diplomacyActionType = actionType;
            parent.setCurrentOverlay(GOTGuiFactions.Overlay.POLITICS_PROPOSE, true);
        }
    }

    @Override
    public void mouseClicked(int x, int y, int b) {
        if (b == 1 && parent.contextMenuObject == null) {
            for (GOTFaction faction : parent.currentFactionList) {
                if (faction != GOTGuiFactions.currentFaction && faction.factionMapInfo != null) {
                    float[] pos = transformMapCoords(faction.factionMapInfo.mapX, faction.factionMapInfo.mapY);
                    if (isClickOnCircle(x, y, (int) pos[0], (int) pos[1], 5)) {
                        parent.contextMenuObject = faction;
                        parent.contextMenuX = x;
                        parent.contextMenuY = y;
                        return;
                    }
                }
            }
            for (FactionStructureSlot slot : this.structureSlots) {
                float[] pos = transformMapCoords(slot.mapX, slot.mapY);
                if (isClickOnCircle(x, y, (int) pos[0], (int) pos[1], 4)) {
                    parent.contextMenuObject = slot;
                    parent.contextMenuX = x;
                    parent.contextMenuY = y;
                    return;
                }
            }
        } else if (b == 0) {
            if (parent.contextMenuObject != null && !isClickInContextMenu(x, y)) {
                parent.contextMenuObject = null;
                return;
            }

            this.isMapDragging = true;
            this.mapDragMouseDownX = x;
            this.mapDragMouseDownY = y;
        }
    }

    private boolean isClickOnCircle(int mouseX, int mouseY, int circleX, int circleY, int radius) {
        int dx = mouseX - circleX;
        int dy = mouseY - circleY;
        return dx * dx + dy * dy < radius * radius;
    }

    private boolean isClickInContextMenu(int mouseX, int mouseY) {
        if (parent.contextMenuObject == null) return false;
        int x = parent.contextMenuX;
        int y = parent.contextMenuY;
        int menuWidth = 110;
        int menuHeight = (parent.contextMenuObject instanceof GOTFaction) ? 110 : 40;

        if (x + menuWidth > parent.getGuiLeft() + parent.getXSize()) x -= menuWidth;
        if (y + menuHeight > parent.getGuiTop() + parent.getYSize()) y -= menuHeight;

        return mouseX >= x && mouseX < x + menuWidth && mouseY >= y && mouseY < y + menuHeight;
    }

    @Override
    public void update() {
        this.prevMapPosX = this.mapPosX;
        this.prevMapPosY = this.mapPosY;
    }

    private float[] transformMapCoords(float x, float z) {
        x -= this.mapPosX;
        z -= this.mapPosY;
        x *= mapRenderer.zoomStable;
        z *= mapRenderer.zoomStable;
        return new float[]{x + (parent.getGuiLeft() + parent.getXSize() / 2), z + (parent.getGuiTop() + parent.getYSize() / 2)};
    }

    private boolean isCoordsVisible(int x, int y, int radius) {
        return x + radius > parent.getGuiLeft() && x - radius < parent.getGuiLeft() + parent.getXSize() &&
                y + radius > parent.getGuiTop() && y - radius < parent.getGuiTop() + parent.getYSize();
    }

    @Override public void keyTyped(char c, int key) {}
    @Override public void handleMouseInput() {}
    @Override public void onGuiClosed() {}
    @Override
    public boolean isTextFieldFocused() {
        return false;
    }
}