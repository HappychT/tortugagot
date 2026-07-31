package com.tortugagot.togcore.client.gui;

import com.tortugagot.togcore.client.TOGClientTechnologyData;
import com.tortugagot.togcore.network.TOGPacketHandler;
import com.tortugagot.togcore.network.TOGPacketTechnologyRequest;
import com.tortugagot.togcore.network.TOGPacketTechnologyUnlock;
import com.tortugagot.togcore.technology.TOGTechnology;
import com.tortugagot.togcore.technology.TOGTechnologyBranch;
import com.tortugagot.togcore.technology.TOGTechnologyRegistry;
import got.client.gui.GOTGuiButton;
import got.client.gui.GOTGuiMenuBase;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.util.List;

public class TOGGuiTechnologyTree extends GOTGuiMenuBase {
    private static final int NODE_SIZE = 16;
    private static final int PANEL_BG = 0xDD151515;
    private static final int PANEL_BORDER = 0xFF3B342B;

    private TOGTechnology selectedTechnology;
    private TOGTechnology hoveredTechnology;
    private GOTGuiButton unlockButton;

    @Override
    public void actionPerformed(GuiButton button) {
        if (button.enabled && button == unlockButton && selectedTechnology != null) {
            TOGPacketHandler.networkWrapper.sendToServer(new TOGPacketTechnologyUnlock(selectedTechnology.getId()));
            return;
        }
        super.actionPerformed(button);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        drawFrame();
        drawBranchLabels();
        drawConnections();
        drawNodes(mouseX, mouseY);
        drawDetails();
        updateUnlockButton();
        super.drawScreen(mouseX, mouseY, partialTicks);
        drawHoveredTechnology(mouseX, mouseY);
    }

    @Override
    public void initGui() {
        xSize = 620;
        ySize = 320;
        super.initGui();
        unlockButton = new GOTGuiButton(2001, guiLeft + 458, guiTop + ySize - 32, 120, 20, "Открыть");
        buttonList.add(unlockButton);
        if (selectedTechnology == null && !TOGTechnologyRegistry.getAll().isEmpty()) {
            selectedTechnology = TOGTechnologyRegistry.getAll().get(0);
        }
        TOGPacketHandler.networkWrapper.sendToServer(new TOGPacketTechnologyRequest());
        updateUnlockButton();
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (mouseButton == 0) {
            for (TOGTechnology technology : TOGTechnologyRegistry.getAll()) {
                int x = nodeX(technology);
                int y = nodeY(technology);
                if (mouseX >= x && mouseX < x + NODE_SIZE && mouseY >= y && mouseY < y + NODE_SIZE) {
                    selectedTechnology = technology;
                    updateUnlockButton();
                    return;
                }
            }
        }
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    public void refresh() {
        updateUnlockButton();
    }

    private void drawFrame() {
        Gui.drawRect(guiLeft, guiTop, guiLeft + xSize, guiTop + ySize, PANEL_BG);
        drawBorder(guiLeft, guiTop, xSize, ySize, PANEL_BORDER);
        Gui.drawRect(guiLeft + 390, guiTop, guiLeft + 392, guiTop + ySize, 0xFF2D2922);
        fontRendererObj.drawString("Древо технологий", guiLeft + 14, guiTop + 12, 0xE8D9B8);
        String points = "Очки мастерства: " + TOGClientTechnologyData.getMasteryPoints();
        fontRendererObj.drawString(points, guiLeft + xSize - 14 - fontRendererObj.getStringWidth(points), guiTop + 12, 0xE8D9B8);
        if (!TOGClientTechnologyData.isSynced()) {
            fontRendererObj.drawString("Синхронизация...", guiLeft + 14, guiTop + ySize - 18, 0xAAAAAA);
        }
    }

    private void drawBranchLabels() {
        drawBranchLane(TOGTechnologyBranch.CRAFTSMAN, "craftsman_i");
        drawBranchLane(TOGTechnologyBranch.WARRIOR, "polearm_mastery");
        drawBranchLane(TOGTechnologyBranch.GATHERER, "hunter_i");
    }

    private void drawBranchLane(TOGTechnologyBranch branch, String rootTechnologyId) {
        TOGTechnology rootTechnology = TOGTechnologyRegistry.get(rootTechnologyId);
        int centerY = rootTechnology != null ? nodeCenterY(rootTechnology) : guiTop + 80;
        drawBranchLabel(branch, guiLeft + 16, centerY - 4);
    }

    private void drawBranchLabel(TOGTechnologyBranch branch, int x, int y) {
        Gui.drawRect(x - 5, y - 3, x + 7, y + 9, 0xFF000000 | branch.getColor());
        fontRendererObj.drawString(branch.getDisplayName(), x + 12, y, branch.getColor());
    }

    private void drawConnections() {
        for (TOGTechnology technology : TOGTechnologyRegistry.getAll()) {
            for (String prerequisiteId : technology.getPrerequisites()) {
                TOGTechnology prerequisite = TOGTechnologyRegistry.get(prerequisiteId);
                if (prerequisite != null) {
                    int color = hasUnlocked(prerequisite) && (hasUnlocked(technology) || getBlockedReason(technology) == null) ? 0xFF777777 : 0xFF484848;
                    drawConnection(prerequisite, technology, color);
                }
            }
        }
    }

    private void drawConnection(TOGTechnology prerequisite, TOGTechnology technology, int color) {
        int startY = nodeCenterY(prerequisite);
        int endY = nodeCenterY(technology);
        int startX = nodeX(prerequisite) + NODE_SIZE;
        int endX = nodeX(technology);

        if (startY == endY) {
            drawLine(startX, startY, endX, endY, color);
            return;
        }

        int busX;
        if (endX > startX) {
            busX = startX + 8;
        } else {
            startX = nodeX(prerequisite);
            endX = nodeX(technology);
            busX = startX - 8;
        }

        drawLine(startX, startY, busX, startY, color);
        drawLine(busX, startY, busX, endY, color);
        drawLine(busX, endY, endX, endY, color);
    }

    private void drawNodes(int mouseX, int mouseY) {
        hoveredTechnology = null;
        for (TOGTechnology technology : TOGTechnologyRegistry.getAll()) {
            int x = nodeX(technology);
            int y = nodeY(technology);
            boolean unlocked = hasUnlocked(technology);
            boolean available = !unlocked && getBlockedReason(technology) == null;
            boolean selected = technology == selectedTechnology;
            int border = selected ? 0xFFFFFFFF : unlocked ? 0xFFEAD27D : available ? 0xFFB5D98A : 0xFF555555;

            if (unlocked) {
                Gui.drawRect(x - 4, y - 4, x + NODE_SIZE + 4, y + NODE_SIZE + 4, 0x22FFF0A0);
                Gui.drawRect(x - 3, y - 3, x + NODE_SIZE + 3, y + NODE_SIZE + 3, 0x33FFE6A6);
            }
            Gui.drawRect(x - 2, y - 2, x + NODE_SIZE + 2, y + NODE_SIZE + 2, border);
            Gui.drawRect(x - 1, y - 1, x + NODE_SIZE + 1, y + NODE_SIZE + 1, 0xFF080808);
            if (!unlocked) {
                Gui.drawRect(x, y, x + NODE_SIZE, y + NODE_SIZE, available ? 0x33000000 : 0x55000000);
            }
            drawTechnologyIcon(technology, x, y, unlocked, available);

            if (mouseX >= x && mouseX < x + NODE_SIZE && mouseY >= y && mouseY < y + NODE_SIZE) {
                hoveredTechnology = technology;
            }
        }
    }

    private void drawTechnologyIcon(TOGTechnology technology, int x, int y, boolean unlocked, boolean available) {
        mc.getTextureManager().bindTexture(iconTexture(technology));
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        if (unlocked) {
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        } else if (available) {
            GL11.glColor4f(0.85F, 0.85F, 0.85F, 1.0F);
        } else {
            GL11.glColor4f(0.60F, 0.60F, 0.60F, 1.0F);
        }
        Gui.func_152125_a(x, y, 0.0F, 0.0F, NODE_SIZE, NODE_SIZE, NODE_SIZE, NODE_SIZE, 16.0F, 16.0F);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glDisable(GL11.GL_BLEND);
    }

    private ResourceLocation iconTexture(TOGTechnology technology) {
        return new ResourceLocation("togcore", "textures/gui/technology/" + technology.getId() + ".png");
    }

    private void drawHoveredTechnology(int mouseX, int mouseY) {
        if (hoveredTechnology != null) {
            drawCreativeTabHoveringText(hoveredTechnology.getName(), mouseX, mouseY);
        }
    }

    private void drawDetails() {
        int left = guiLeft + 406;
        int top = guiTop + 36;
        int width = 198;
        Gui.drawRect(left - 8, top - 8, left + width + 8, guiTop + ySize - 44, 0xAA000000);
        if (selectedTechnology == null) {
            fontRendererObj.drawSplitString("Выберите технологию.", left, top, width, 0xD0D0D0);
            return;
        }

        TOGTechnology technology = selectedTechnology;
        int y = top;
        fontRendererObj.drawSplitString(technology.getName(), left, y, width, technology.getDisplayColor());
        y += 24;
        fontRendererObj.drawSplitString(technology.getDescription(), left, y, width, 0xD7D7D7);
        y += lineHeight(technology.getDescription(), width) + 8;
        fontRendererObj.drawString(getPriceText(technology), left, y, 0xE8D9B8);
        y += 13;
        drawWrapped("Требуется: " + getPrerequisiteText(technology), left, y, width, 0xC8C8C8);
        y += lineHeight("Требуется: " + getPrerequisiteText(technology), width) + 5;
        if (technology.getUnlocks().length() > 0 && !technology.getUnlocks().equals(technology.getDescription())) {
            drawWrapped("Открывает: " + technology.getUnlocks(), left, y, width, 0xBFD7B5);
            y += lineHeight("Открывает: " + technology.getUnlocks(), width) + 5;
        }
        String state;
        int stateColor;
        if (hasUnlocked(technology)) {
            state = "Состояние: открыта.";
            stateColor = 0xA8E08F;
        } else {
            String reason = getBlockedReason(technology);
            if (reason == null) {
                state = "Состояние: доступна для открытия.";
                stateColor = 0xE6D58D;
            } else {
                state = "Заблокировано: " + reason;
                stateColor = 0xE08080;
            }
        }
        drawWrapped(state, left, y, width, stateColor);
    }

    private void drawWrapped(String text, int x, int y, int width, int color) {
        fontRendererObj.drawSplitString(text, x, y, width, color);
    }

    private int lineHeight(String text, int width) {
        List lines = fontRendererObj.listFormattedStringToWidth(text, width);
        return Math.max(1, lines.size()) * fontRendererObj.FONT_HEIGHT;
    }

    private String getPrerequisiteText(TOGTechnology technology) {
        if (technology.getPrerequisites().isEmpty()) {
            return "нет";
        }
        StringBuilder builder = new StringBuilder();
        for (String prerequisiteId : technology.getPrerequisites()) {
            TOGTechnology prerequisite = TOGTechnologyRegistry.get(prerequisiteId);
            if (builder.length() > 0) {
                builder.append(", ");
            }
            builder.append(prerequisite != null ? prerequisite.getName() : prerequisiteId);
        }
        return builder.toString();
    }

    private String getPriceText(TOGTechnology technology) {
        if (hasUnlocked(technology)) {
            return "Базовая цена: " + technology.getBaseCost();
        }
        return "Цена открытия: " + getUnlockCost(technology);
    }

    private void updateUnlockButton() {
        if (unlockButton == null) {
            return;
        }
        boolean visible = selectedTechnology != null && !hasUnlocked(selectedTechnology);
        unlockButton.visible = visible;
        unlockButton.enabled = visible && getBlockedReason(selectedTechnology) == null;
    }

    private boolean hasUnlocked(TOGTechnology technology) {
        return TOGClientTechnologyData.hasUnlocked(technology.getId());
    }

    private String getBlockedReason(TOGTechnology technology) {
        if (technology == null) {
            return "Технология не выбрана.";
        }
        if (hasUnlocked(technology)) {
            return "Технология уже открыта.";
        }
        int unlockCost = getUnlockCost(technology);
        if (TOGClientTechnologyData.getMasteryPoints() < unlockCost) {
            return "нужно " + unlockCost + " очков мастерства.";
        }
        for (String prerequisiteId : technology.getPrerequisites()) {
            if (!TOGClientTechnologyData.hasUnlocked(prerequisiteId)) {
                TOGTechnology prerequisite = TOGTechnologyRegistry.get(prerequisiteId);
                return "сначала откройте " + (prerequisite != null ? prerequisite.getName() : prerequisiteId) + ".";
            }
        }
        return null;
    }

    private int getUnlockCost(TOGTechnology technology) {
        return technology.getUnlockCost(TOGClientTechnologyData.getUnlockedCount());
    }

    private int nodeX(TOGTechnology technology) {
        return guiLeft + 28 + technology.getX();
    }

    private int nodeY(TOGTechnology technology) {
        return guiTop + 26 + technology.getY();
    }

    private int nodeCenterX(TOGTechnology technology) {
        return nodeX(technology) + NODE_SIZE / 2;
    }

    private int nodeCenterY(TOGTechnology technology) {
        return nodeY(technology) + NODE_SIZE / 2;
    }

    private void drawBorder(int x, int y, int w, int h, int color) {
        Gui.drawRect(x, y, x + w, y + 1, color);
        Gui.drawRect(x, y + h - 1, x + w, y + h, color);
        Gui.drawRect(x, y, x + 1, y + h, color);
        Gui.drawRect(x + w - 1, y, x + w, y + h, color);
    }

    private int dim(int color, int min) {
        int r = Math.max(min, (color >> 16 & 0xFF) / 2);
        int g = Math.max(min, (color >> 8 & 0xFF) / 2);
        int b = Math.max(min, (color & 0xFF) / 2);
        return r << 16 | g << 8 | b;
    }

    private void drawLine(int x1, int y1, int x2, int y2, int color) {
        float alpha = (color >> 24 & 0xFF) / 255.0F;
        float red = (color >> 16 & 0xFF) / 255.0F;
        float green = (color >> 8 & 0xFF) / 255.0F;
        float blue = (color & 0xFF) / 255.0F;
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glLineWidth(1.0F);
        GL11.glColor4f(red, green, blue, alpha);
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawing(GL11.GL_LINES);
        tessellator.addVertex(x1, y1, 0.0D);
        tessellator.addVertex(x2, y2, 0.0D);
        tessellator.draw();
        GL11.glLineWidth(1.0F);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
    }
}
