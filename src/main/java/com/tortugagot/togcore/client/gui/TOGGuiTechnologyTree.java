package com.tortugagot.togcore.client.gui;

import com.tortugagot.togcore.client.TOGClientTechnologyData;
import com.tortugagot.togcore.network.TOGPacketHandler;
import com.tortugagot.togcore.network.TOGPacketTechnologyRequest;
import com.tortugagot.togcore.network.TOGPacketTechnologyUnlock;
import com.tortugagot.togcore.technology.TOGTechnology;
import com.tortugagot.togcore.technology.TOGTechnologyBranch;
import com.tortugagot.togcore.technology.TOGTechnologyRegistry;
import com.tortugagot.togcore.technology.TOGTechnologyRules;
import got.client.gui.GOTGuiButton;
import got.client.gui.GOTGuiMenuBase;
import got.client.gui.utils.GuiApi;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.util.List;

public class TOGGuiTechnologyTree extends GOTGuiMenuBase {
    private static final int NODE_SIZE = 14;
    private static final int NODE_ICON_INSET = 1;
    private static final int CONNECTION_NODE_GAP = 2;
    private static final int CONNECTION_BUS_OFFSET = 10;
    private static final int PANEL_BG = 0xDD151515;
    private static final int PANEL_BORDER = 0xFF3B342B;
    private static final int TREE_VIEW_LEFT = 8;
    private static final int TREE_VIEW_TOP = 28;
    private static final int TREE_VIEW_MIN_RIGHT = 490;
    private static final int TREE_NODE_X_OFFSET = 28;
    private static final int TREE_NODE_Y_OFFSET = 18;
    private static final int TREE_X_SCALE_NUMERATOR = 3;
    private static final int TREE_X_SCALE_DENOMINATOR = 2;
    private static final int TREE_Y_SCALE_NUMERATOR = 2;
    private static final int TREE_Y_SCALE_DENOMINATOR = 1;
    private static final int TREE_SCROLL_STEP = 32;
    private static final int MIN_GUI_WIDTH = 720;
    private static final int MIN_GUI_HEIGHT = 350;
    private static final int MAX_GUI_WIDTH = 1040;
    private static final int MAX_GUI_HEIGHT = 560;
    private static final int SCREEN_MARGIN = 12;
    private static final int DETAILS_MIN_WIDTH = 198;
    private static final int DETAILS_MAX_WIDTH = 260;
    private static final int DETAILS_MIN_COMPACT_WIDTH = 170;
    private static final int DETAILS_TOP = 36;
    private static final int DETAILS_RIGHT_MARGIN = 16;
    private static final int DETAILS_GAP = 16;
    private static final int DETAILS_BOTTOM_MARGIN = 44;
    private static final int TREE_BOTTOM_MARGIN = 12;

    private TOGTechnology selectedTechnology;
    private TOGTechnology hoveredTechnology;
    private GOTGuiButton unlockButton;
    private int treeViewRight = TREE_VIEW_MIN_RIGHT;
    private int treeViewBottom = 338;
    private int detailsLeft = 506;
    private int detailsWidth = DETAILS_MIN_WIDTH;
    private int treeScrollX;
    private int treeScrollY;
    private int currentMouseX;
    private int currentMouseY;
    private int previousMouseX;
    private int previousMouseY;
    private boolean draggingTree;
    private boolean wasMouseDown;

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
        currentMouseX = mouseX;
        currentMouseY = mouseY;
        updateTreeDrag(mouseX, mouseY);
        drawDefaultBackground();
        drawFrame();
        drawTreeViewport(mouseX, mouseY);
        drawTreeScrollBars();
        drawDetails();
        updateUnlockButton();
        super.drawScreen(mouseX, mouseY, partialTicks);
        drawHoveredTechnology(mouseX, mouseY);
    }

    @Override
    public void initGui() {
        configureLayout();
        super.initGui();
        clampTreeScroll();
        unlockButton = new GOTGuiButton(2001, guiLeft + detailsLeft + detailsWidth - 120, guiTop + ySize - 32, 120, 20, "Открыть");
        buttonList.add(unlockButton);
        if (selectedTechnology == null && !TOGTechnologyRegistry.getAll().isEmpty()) {
            selectedTechnology = TOGTechnologyRegistry.getAll().get(0);
        }
        TOGPacketHandler.networkWrapper.sendToServer(new TOGPacketTechnologyRequest());
        updateUnlockButton();
    }

    private void configureLayout() {
        int availableWidth = Math.max(320, width - SCREEN_MARGIN * 2);
        int availableHeight = Math.max(240, height - SCREEN_MARGIN * 2);
        xSize = clamp(availableWidth, MIN_GUI_WIDTH, MAX_GUI_WIDTH);
        ySize = clamp(availableHeight, MIN_GUI_HEIGHT, MAX_GUI_HEIGHT);
        xSize = Math.min(xSize, availableWidth);
        ySize = Math.min(ySize, availableHeight);
        int preferredDetailsWidth = clamp(xSize / 4, DETAILS_MIN_WIDTH, DETAILS_MAX_WIDTH);
        int maxDetailsWidth = Math.max(DETAILS_MIN_COMPACT_WIDTH, xSize - TREE_VIEW_MIN_RIGHT - DETAILS_GAP - DETAILS_RIGHT_MARGIN);
        detailsWidth = Math.min(preferredDetailsWidth, maxDetailsWidth);
        detailsLeft = xSize - DETAILS_RIGHT_MARGIN - detailsWidth;
        treeViewRight = Math.max(TREE_VIEW_LEFT + 160, detailsLeft - DETAILS_GAP);
        treeViewBottom = Math.max(TREE_VIEW_TOP + 160, ySize - TREE_BOTTOM_MARGIN);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (mouseButton == 0 && isMouseInTree(mouseX, mouseY)) {
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

    @Override
    public void handleMouseInput() {
        super.handleMouseInput();
        int wheel = Mouse.getEventDWheel();
        if (wheel != 0 && isMouseInTree(currentMouseX, currentMouseY)) {
            scrollTree(0, wheel > 0 ? -TREE_SCROLL_STEP : TREE_SCROLL_STEP);
        }
    }

    private void drawFrame() {
        Gui.drawRect(guiLeft, guiTop, guiLeft + xSize, guiTop + ySize, PANEL_BG);
        drawBorder(guiLeft, guiTop, xSize, ySize, PANEL_BORDER);
        Gui.drawRect(guiLeft + TREE_VIEW_LEFT, guiTop + TREE_VIEW_TOP, guiLeft + treeViewRight, guiTop + treeViewBottom, 0x33000000);
        drawBorder(guiLeft + TREE_VIEW_LEFT, guiTop + TREE_VIEW_TOP, treeViewRight - TREE_VIEW_LEFT, treeViewBottom - TREE_VIEW_TOP, 0x66443A2E);
        Gui.drawRect(guiLeft + treeViewRight, guiTop, guiLeft + treeViewRight + 2, guiTop + ySize, 0xFF2D2922);
        fontRendererObj.drawString("Древо технологий", guiLeft + 14, guiTop + 12, 0xE8D9B8);
        String points = "Очки мастерства: " + TOGClientTechnologyData.getMasteryPoints();
        fontRendererObj.drawString(points, guiLeft + xSize - 14 - fontRendererObj.getStringWidth(points), guiTop + 12, 0xE8D9B8);
        if (!TOGClientTechnologyData.isSynced()) {
            fontRendererObj.drawString("Синхронизация...", guiLeft + 14, guiTop + ySize - 18, 0xAAAAAA);
        }
    }

    private void drawTreeViewport(int mouseX, int mouseY) {
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        GuiApi.glScissor(guiLeft + TREE_VIEW_LEFT, guiTop + TREE_VIEW_TOP, treeViewRight - TREE_VIEW_LEFT, treeViewBottom - TREE_VIEW_TOP, false);
        drawBranchLabels();
        drawConnections();
        drawNodes(mouseX, mouseY);
        GL11.glDisable(GL11.GL_SCISSOR_TEST);
    }

    private void drawTreeScrollBars() {
        int maxScrollX = getMaxTreeScrollX();
        int maxScrollY = getMaxTreeScrollY();
        if (maxScrollY > 0) {
            int trackX = guiLeft + treeViewRight - 5;
            int trackTop = guiTop + TREE_VIEW_TOP + 2;
            int trackHeight = treeViewBottom - TREE_VIEW_TOP - 4;
            int knobHeight = Math.max(20, trackHeight * (treeViewBottom - TREE_VIEW_TOP) / getTreeContentHeight());
            int knobY = trackTop + treeScrollY * (trackHeight - knobHeight) / maxScrollY;
            Gui.drawRect(trackX, trackTop, trackX + 3, trackTop + trackHeight, 0x552A2620);
            Gui.drawRect(trackX, knobY, trackX + 3, knobY + knobHeight, 0xAA8C7A5E);
        }
        if (maxScrollX > 0) {
            int trackLeft = guiLeft + TREE_VIEW_LEFT + 2;
            int trackY = guiTop + treeViewBottom - 5;
            int trackWidth = treeViewRight - TREE_VIEW_LEFT - 8;
            int knobWidth = Math.max(24, trackWidth * (treeViewRight - TREE_VIEW_LEFT) / getTreeContentWidth());
            int knobX = trackLeft + treeScrollX * (trackWidth - knobWidth) / maxScrollX;
            Gui.drawRect(trackLeft, trackY, trackLeft + trackWidth, trackY + 3, 0x552A2620);
            Gui.drawRect(knobX, trackY, knobX + knobWidth, trackY + 3, 0xAA8C7A5E);
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
            String[] requiredChoiceGroup = TOGTechnologyRules.getRequiredStaminaChoiceGroup(technology.getId());
            if (requiredChoiceGroup != null) {
                for (String choiceId : requiredChoiceGroup) {
                    TOGTechnology choice = TOGTechnologyRegistry.get(choiceId);
                    if (choice != null) {
                        int color = hasUnlocked(choice) && (hasUnlocked(technology) || getBlockedReason(technology) == null) ? 0xFF777777 : 0xFF484848;
                        drawConnection(choice, technology, color);
                    }
                }
            }
        }
    }

    private void drawConnection(TOGTechnology prerequisite, TOGTechnology technology, int color) {
        int startY = nodeCenterY(prerequisite);
        int endY = nodeCenterY(technology);
        int prerequisiteLeft = nodeX(prerequisite);
        int prerequisiteRight = prerequisiteLeft + NODE_SIZE;
        int technologyLeft = nodeX(technology);
        int technologyRight = technologyLeft + NODE_SIZE;
        int startX;
        int endX;
        int busXOffset;

        if (technologyLeft > prerequisiteRight) {
            startX = prerequisiteRight + CONNECTION_NODE_GAP;
            endX = technologyLeft - CONNECTION_NODE_GAP;
            busXOffset = CONNECTION_BUS_OFFSET;
        } else if (technologyRight < prerequisiteLeft) {
            startX = prerequisiteLeft - CONNECTION_NODE_GAP;
            endX = technologyRight + CONNECTION_NODE_GAP;
            busXOffset = -CONNECTION_BUS_OFFSET;
        } else {
            startX = prerequisiteLeft - CONNECTION_NODE_GAP;
            endX = technologyLeft - CONNECTION_NODE_GAP;
            busXOffset = -CONNECTION_BUS_OFFSET;
        }

        if (startY == endY) {
            drawLine(startX, startY, endX, endY, color);
            return;
        }

        int busX = startX + busXOffset;

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
                Gui.drawRect(x - 1, y - 1, x + NODE_SIZE + 1, y + NODE_SIZE + 1, 0x22FFF0A0);
            }
            Gui.drawRect(x - 1, y - 1, x + NODE_SIZE + 1, y + NODE_SIZE + 1, border);
            Gui.drawRect(x, y, x + NODE_SIZE, y + NODE_SIZE, 0xFF080808);
            if (!unlocked) {
                Gui.drawRect(x, y, x + NODE_SIZE, y + NODE_SIZE, available ? 0x33000000 : 0x55000000);
            }
            drawTechnologyIcon(technology, x, y, unlocked, available);

            if (isMouseInTree(mouseX, mouseY) && mouseX >= x && mouseX < x + NODE_SIZE && mouseY >= y && mouseY < y + NODE_SIZE) {
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
        int iconSize = NODE_SIZE - NODE_ICON_INSET * 2;
        Gui.func_152125_a(x + NODE_ICON_INSET, y + NODE_ICON_INSET, 0.0F, 0.0F, 16, 16, iconSize, iconSize, 16.0F, 16.0F);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glDisable(GL11.GL_BLEND);
    }

    private ResourceLocation iconTexture(TOGTechnology technology) {
        String iconId = technology.getId();
        if (isWarriorExtensionIcon(iconId)) {
            iconId = warriorExtensionIcon(iconId);
        }
        return new ResourceLocation("togcore", "textures/gui/technology/" + iconId + ".png");
    }

    private boolean isWarriorExtensionIcon(String iconId) {
        return iconId.indexOf("_final_damage_") >= 0
                || TOGTechnologyRules.isWarriorStaminaChoice(iconId)
                || iconId.endsWith("_passive")
                || iconId.startsWith("bow_arrow_save_")
                || "bow_stamina".equals(iconId)
                || iconId.startsWith("crossbow_bolt_save_")
                || "crossbow_stamina".equals(iconId);
    }

    private String warriorExtensionIcon(String iconId) {
        if ("shield_passive".equals(iconId)) {
            return "shield_passive";
        }
        if (iconId.startsWith("two_handed_sword_")) {
            return "two_handed_sword_mastery";
        }
        if (iconId.startsWith("crossbow_")) {
            return "crossbow_mastery";
        }
        if (iconId.startsWith("claymore_")) {
            return "claymore_mastery";
        }
        if (iconId.startsWith("sword_")) {
            return "sword_mastery";
        }
        if (iconId.startsWith("dagger_")) {
            return "dagger_mastery";
        }
        if (iconId.startsWith("spear_")) {
            return "spear_mastery";
        }
        if (iconId.startsWith("pike_")) {
            return "pike_mastery";
        }
        if (iconId.startsWith("glaive_")) {
            return "glaive_mastery";
        }
        if (iconId.startsWith("shield_")) {
            return "shield_mastery";
        }
        if (iconId.startsWith("axe_")) {
            return "axe_mastery";
        }
        if (iconId.startsWith("hammer_")) {
            return "hammer_mastery";
        }
        if (iconId.startsWith("bow_")) {
            return "bow_mastery";
        }
        return "sword_mastery";
    }

    private void drawHoveredTechnology(int mouseX, int mouseY) {
        if (hoveredTechnology != null && isMouseInTree(mouseX, mouseY)) {
            drawCreativeTabHoveringText(hoveredTechnology.getName(), mouseX, mouseY);
        }
    }

    private void drawDetails() {
        int left = guiLeft + detailsLeft;
        int top = guiTop + DETAILS_TOP;
        int width = detailsWidth;
        Gui.drawRect(left - 8, top - 8, left + width + 8, guiTop + ySize - DETAILS_BOTTOM_MARGIN, 0xAA000000);
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
        String exclusiveChoiceId = TOGTechnologyRules.getExclusiveStaminaChoiceId(technology.getId());
        if (exclusiveChoiceId != null && TOGClientTechnologyData.hasUnlocked(exclusiveChoiceId)) {
            return "уже выбран альтернативный узел стамины: " + getTechnologyName(exclusiveChoiceId) + ". Второй вариант доступен только после полного сброса древа.";
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
        String[] requiredChoiceGroup = TOGTechnologyRules.getRequiredStaminaChoiceGroup(technology.getId());
        if (requiredChoiceGroup != null && !TOGClientTechnologyData.hasUnlocked(requiredChoiceGroup[0]) && !TOGClientTechnologyData.hasUnlocked(requiredChoiceGroup[1])) {
            return "сначала выберите один из узлов " + TOGTechnologyRules.getRequiredStaminaChoiceName(technology.getId()) + ": " + getTechnologyName(requiredChoiceGroup[0]) + " или " + getTechnologyName(requiredChoiceGroup[1]) + ".";
        }
        return null;
    }

    private String getTechnologyName(String id) {
        TOGTechnology technology = TOGTechnologyRegistry.get(id);
        return technology != null ? technology.getName() : id;
    }

    private int getUnlockCost(TOGTechnology technology) {
        return technology.getUnlockCost(TOGClientTechnologyData.getUnlockedCount());
    }

    private void updateTreeDrag(int mouseX, int mouseY) {
        boolean mouseDown = Mouse.isButtonDown(0);
        if (mouseDown && !wasMouseDown && isMouseInTree(mouseX, mouseY)) {
            draggingTree = true;
            previousMouseX = mouseX;
            previousMouseY = mouseY;
        } else if (!mouseDown) {
            draggingTree = false;
        }
        if (draggingTree) {
            int deltaX = mouseX - previousMouseX;
            int deltaY = mouseY - previousMouseY;
            if (deltaX != 0 || deltaY != 0) {
                scrollTree(-deltaX, -deltaY);
                previousMouseX = mouseX;
                previousMouseY = mouseY;
            }
        }
        wasMouseDown = mouseDown;
    }

    private void scrollTree(int deltaX, int deltaY) {
        treeScrollX += deltaX;
        treeScrollY += deltaY;
        clampTreeScroll();
    }

    private void clampTreeScroll() {
        treeScrollX = clamp(treeScrollX, 0, getMaxTreeScrollX());
        treeScrollY = clamp(treeScrollY, 0, getMaxTreeScrollY());
    }

    private int getMaxTreeScrollX() {
        return Math.max(0, getTreeContentWidth() - treeViewRight);
    }

    private int getMaxTreeScrollY() {
        return Math.max(0, getTreeContentHeight() - treeViewBottom);
    }

    private int getTreeContentWidth() {
        int max = 0;
        for (TOGTechnology technology : TOGTechnologyRegistry.getAll()) {
            max = Math.max(max, TREE_NODE_X_OFFSET + scaledTreeX(technology) + NODE_SIZE + 20);
        }
        return max;
    }

    private int getTreeContentHeight() {
        int max = 0;
        for (TOGTechnology technology : TOGTechnologyRegistry.getAll()) {
            max = Math.max(max, TREE_NODE_Y_OFFSET + scaledTreeY(technology) + NODE_SIZE + 20);
        }
        return max;
    }

    private int scaledTreeX(TOGTechnology technology) {
        return technology.getX() * TREE_X_SCALE_NUMERATOR / TREE_X_SCALE_DENOMINATOR;
    }

    private int scaledTreeY(TOGTechnology technology) {
        return technology.getY() * TREE_Y_SCALE_NUMERATOR / TREE_Y_SCALE_DENOMINATOR;
    }

    private boolean isMouseInTree(int mouseX, int mouseY) {
        return mouseX >= guiLeft + TREE_VIEW_LEFT
                && mouseX < guiLeft + treeViewRight
                && mouseY >= guiTop + TREE_VIEW_TOP
                && mouseY < guiTop + treeViewBottom;
    }

    private int clamp(int value, int min, int max) {
        if (value < min) {
            return min;
        }
        if (value > max) {
            return max;
        }
        return value;
    }

    private int nodeX(TOGTechnology technology) {
        return guiLeft + TREE_NODE_X_OFFSET + scaledTreeX(technology) - treeScrollX;
    }

    private int nodeY(TOGTechnology technology) {
        return guiTop + TREE_NODE_Y_OFFSET + scaledTreeY(technology) - treeScrollY;
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
