package got.client.gui.faction;

import brain.factions.Faction;
import brain.factions.network.PacketFactionManage;
import brain.factions.network.PacketInfoFactions;
import brain.factions.network.PacketMessage;
import brain.factions.network.PacketFactionStructures;
import brain.factions.structures.FactionStructureSlot;
import got.client.gui.GOTGuiMap;
import got.client.gui.GOTGuiRendererMap;
import got.client.gui.faction.pages.IPageRenderer;
import got.client.utils.UtilO;
import got.common.faction.GOTFaction;
import got.common.faction.GOTFactionRelations;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.MathHelper;
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
    private List<GOTFaction> playableFactionsOnMap = new ArrayList<>();

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

        this.playableFactionsOnMap.clear();
        for (GOTFaction f : GOTFaction.values()) {
            if (GOTGuiFactions.PLAYABLE_FACTION_NAMES.contains(f.name())) {
                this.playableFactionsOnMap.add(f);
            }
        }
    }

    @Override
    public void initGui(List<GuiButton> buttonList) {
        mapGui.setWorldAndResolution(parent.mc, parent.width, parent.height);

        diplomacyButtons = new GuiCustomButton[4];
        diplomacyButtons[0] = new GuiCustomButton(101, 0, 0, 100, 20, "Объявить войну");
        diplomacyButtons[1] = new GuiCustomButton(102, 0, 0, 100, 20, "Предложить союз");
        diplomacyButtons[2] = new GuiCustomButton(103, 0, 0, 100, 20, "Предложить мир");
        diplomacyButtons[3] = new GuiCustomButton(104, 0, 0, 100, 20, "Объявить вражду");

        for (GuiButton button : diplomacyButtons) {
            buttonList.add(button);
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, int scaledMouseX, int scaledMouseY, float partialTicks) {
        updateMapPosition(scaledMouseX, scaledMouseY);

        mapRenderer.zoomExp = this.zoomPower;
        mapRenderer.zoomStable = (float) Math.pow(2.0, this.zoomPower);
        mapRenderer.mapX = this.mapPosX;
        mapRenderer.mapY = this.mapPosY;
        mapRenderer.prevMapX = this.prevMapPosX;
        mapRenderer.prevMapY = this.prevMapPosY;

        mapRenderer.renderMap(parent, mapGui, partialTicks, 0, 0, parent.getBaseWidth(), parent.getBaseHeight());

        GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        drawFactions();
        drawStructures();

        GL11.glPopAttrib();

        positionDiplomacyContextMenuButtons();

    }

    public void drawDiplomacyContextMenuAfterButtons(int mouseX, int mouseY) {
        if (parent.contextMenuObject instanceof GOTFaction) {
            GOTFaction fac = (GOTFaction) parent.contextMenuObject;

            int x = parent.contextMenuX;
            int y = parent.contextMenuY;
            int menuWidth = 110;
            int menuHeight = 110;

            if (x + menuWidth > parent.getBaseWidth()) x -= menuWidth;
            if (y + menuHeight > parent.getBaseHeight()) y -= menuHeight;

            UtilO.drawRoundedRectangle(x, y, menuWidth, menuHeight, 5, new Color(20, 20, 20, 200).getRGB(), 0);
            parent.drawCenteredString(fac.factionName(), x + menuWidth / 2, y + 5, 0xFFFFFFFF);
        }
    }

    private void positionDiplomacyContextMenuButtons() {
        if (parent.contextMenuObject instanceof GOTFaction) {
            int x = parent.contextMenuX;
            int y = parent.contextMenuY;
            int menuWidth = 110;
            int menuHeight = 110;

            if (x + menuWidth > parent.getBaseWidth()) x -= menuWidth;
            if (y + menuHeight > parent.getBaseHeight()) y -= menuHeight;

            for (int i = 0; i < diplomacyButtons.length; i++) {
                diplomacyButtons[i].xPosition = x + 5;
                diplomacyButtons[i].yPosition = y + 20 + i * 22;
                diplomacyButtons[i].visible = true;
            }
        } else {
            for (GuiButton button : diplomacyButtons) {
                button.visible = false;
            }
        }
    }

    public void drawTooltips(int mouseX, int mouseY, int scaledMouseX, int scaledMouseY) {
        if (parent.contextMenuObject != null) return;

        float[] transformedMouse = untransformMapCoords(scaledMouseX, scaledMouseY);
        float detectionRadiusMultiplier = 1.5f;

        for (FactionStructureSlot slot : this.structureSlots) {
            float checkRadius = 4.0f * detectionRadiusMultiplier;
            if (isClickOnTransformedCircle(transformedMouse, slot.mapX, slot.mapY, checkRadius)) {
                List<String> tooltip = new ArrayList<>();
                tooltip.add("§l" + slot.name);

                if (slot.ownerFactionID != null) {
                    GOTFaction ownerFaction = GOTFaction.forName(slot.ownerFactionID);
                    if (ownerFaction != null) {
                        tooltip.add("§7Владелец: §f" + ownerFaction.factionName());
                    } else {
                        tooltip.add("§7Владелец: §f" + slot.ownerFactionID);
                    }


                    if (parent.getFactionData() != null) {
                        GOTFaction playerFaction = GOTFaction.forName(parent.getFactionData().getID());
                        if (playerFaction != null && ownerFaction != null) {
                            GOTFactionRelations.Relation relation = GOTFactionRelations.getRelations(playerFaction, ownerFaction);

                            if (relation == GOTFactionRelations.Relation.ALLY || playerFaction == ownerFaction) {
                                tooltip.add("§7Уровень: §b" + slot.level);
                            } else if (relation == GOTFactionRelations.Relation.ENEMY || relation == GOTFactionRelations.Relation.MORTAL_ENEMY) {
                                tooltip.add("§7Рейд-тайм: §c" + PacketFactionStructures.raidTimeString);
                            }
                        }
                    }
                } else {
                    tooltip.add("§7Ничейная территория");
                }
                tooltip.add(String.format("§8[X: %d, Y: %d, Z: %d]", slot.xCoord, slot.yCoord, slot.zCoord));

                parent.drawHoveringTextPublic(tooltip, mouseX, mouseY);
                return;
            }
        }

        for (GOTFaction faction : this.playableFactionsOnMap) {
            if (faction.factionMapInfo != null) {
                float checkRadius = 5.0f * detectionRadiusMultiplier;
                if (isClickOnTransformedCircle(transformedMouse, faction.factionMapInfo.mapX, faction.factionMapInfo.mapY, checkRadius)) {
                    parent.drawHoveringTextPublic(java.util.Collections.singletonList(faction.factionName()), mouseX, mouseY);
                    return;
                }
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

    private boolean isMouseOverButton(GuiButton button, int scaledMouseX, int scaledMouseY) {
        if (button == null || !button.visible) return false;
        return scaledMouseX >= button.xPosition && scaledMouseY >= button.yPosition && scaledMouseX < button.xPosition + button.width && scaledMouseY < button.yPosition + button.height;
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int scaledMouseX, int scaledMouseY, int button) {
        if (button == 1 && parent.contextMenuObject == null) {
            float[] transformedMouse = untransformMapCoords(scaledMouseX, scaledMouseY);
            for (GOTFaction faction : this.playableFactionsOnMap) {
                if (faction != GOTGuiFactions.currentFaction && faction.factionMapInfo != null) {
                    float checkRadius = 5.0f * 1.5f;
                    if (isClickOnTransformedCircle(transformedMouse, faction.factionMapInfo.mapX, faction.factionMapInfo.mapY, checkRadius)) {
                        parent.contextMenuObject = faction;
                        parent.contextMenuX = scaledMouseX;
                        parent.contextMenuY = scaledMouseY;
                        return;
                    }
                }
            }
        } else if (button == 0) {
            boolean isOverAnyButton = false;
            for(GuiButton b : diplomacyButtons) {
                if(isMouseOverButton(b, scaledMouseX, scaledMouseY)) {
                    isOverAnyButton = true;
                    break;
                }
            }

            if (parent.contextMenuObject != null && !isClickInContextMenu(scaledMouseX, scaledMouseY) && !isOverAnyButton) {
                parent.contextMenuObject = null;
                return;
            }

            if (parent.contextMenuObject == null) {
                this.isMapDragging = true;
                this.mapDragMouseDownX = scaledMouseX;
                this.mapDragMouseDownY = scaledMouseY;
            }
        }
    }

    private void updateMapPosition(int scaledMouseX, int scaledMouseY) {
        if (this.isMapDragging) {
            if (Mouse.isButtonDown(0)) {
                float dx = (scaledMouseX - this.mapDragMouseDownX) / mapRenderer.zoomStable;
                float dy = (scaledMouseY - this.mapDragMouseDownY) / mapRenderer.zoomStable;
                this.mapPosX -= dx;
                this.mapPosY -= dy;
                this.mapDragMouseDownX = scaledMouseX;
                this.mapDragMouseDownY = scaledMouseY;
            } else {
                this.isMapDragging = false;
            }
        }
    }

    private void drawFactions() {
        for (GOTFaction faction : this.playableFactionsOnMap) {
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
                int color = slot.ownerFactionID == null ? new Color(128, 128, 128, 200).getRGB() : new Color(255, 255, 255, 200).getRGB();
                UtilO.drawCircle2(structX, structY, structRadius, color);

                Faction ownerFactionData = PacketInfoFactions.getFactions().get(slot.ownerFactionID);
                if (ownerFactionData != null && slot.id != null && slot.id.equals(ownerFactionData.getMainFortressId())) {
                }
            }
        }
    }

    private boolean isClickOnTransformedCircle(float[] transformedMouse, float circleX, float circleY, float radius) {
        float dx = transformedMouse[0] - circleX;
        float dy = transformedMouse[1] - circleY;
        return dx * dx + dy * dy < radius * radius;
    }

    private boolean isClickInContextMenu(int scaledMouseX, int scaledMouseY) {
        if (parent.contextMenuObject == null) return false;

        int x = parent.contextMenuX;
        int y = parent.contextMenuY;
        int menuWidth = 110;
        int menuHeight = 110;

        if (x + menuWidth > parent.getBaseWidth()) x -= menuWidth;
        if (y + menuHeight > parent.getBaseHeight()) y -= menuHeight;

        return scaledMouseX >= x && scaledMouseX < x + menuWidth &&
                scaledMouseY >= y && scaledMouseY < y + menuHeight;
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
        return new float[]{x + parent.getBaseWidth() / 2, z + parent.getBaseHeight() / 2};
    }

    private float[] untransformMapCoords(float x, float z) {
        x -= parent.getBaseWidth() / 2;
        z -= parent.getBaseHeight() / 2;
        x /= mapRenderer.zoomStable;
        z /= mapRenderer.zoomStable;
        return new float[]{x + this.mapPosX, z + this.mapPosY};
    }

    private boolean isCoordsVisible(int x, int y, int radius) {
        return x + radius > 0 && x - radius < parent.getBaseWidth() &&
                y + radius > 0 && y - radius < parent.getBaseHeight();
    }

    @Override public void keyTyped(char c, int key) {}
    @Override public void onGuiClosed() {}
    @Override public boolean isTextFieldFocused() { return false; }

    @Override
    public void handleMouseInput() {
        int wheel = Mouse.getEventDWheel();
        if (wheel != 0) {
            float newZoom = this.zoomPower + (float)wheel / 120.0F * 0.5F;
            this.zoomPower = MathHelper.clamp_float(newZoom, -4.0F, 1.0F);
        }
    }
}